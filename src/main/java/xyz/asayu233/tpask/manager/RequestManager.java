package xyz.asayu233.tpask.manager;

import com.mojang.authlib.GameProfile;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.Relative;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public final class RequestManager extends BaseManager {
    public static final RequestManager INSTANCE = new RequestManager();
    private final Map<UUID, Map<UUID, RequestTask>> req_by_sender = new HashMap<>();
    private final Map<UUID, Map<UUID, RequestTask>> req_by_receiver = new HashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private RequestManager() {}

    @NotNull
    public Collection<GameProfile> getSentRequests(@NotNull ServerPlayer sender) {
        Map<UUID, RequestTask> sent = this.req_by_sender.get(sender.getUUID());
        PlayerList players = sender.level().getServer().getPlayerList();
        return sent == null ? Collections.emptyList() :
            sent.entrySet().stream()
                .filter(x -> x.getValue().type != null)
                .map(x -> players.getPlayer(x.getKey()))
                .filter(Objects::nonNull)
                .map(ServerPlayer::getGameProfile)
                .collect(Collectors.toSet());
    }

    @NotNull
    public Collection<GameProfile> getPendingRequests(@NotNull ServerPlayer sender) {
        Map<UUID, RequestTask> pending = this.req_by_receiver.get(sender.getUUID());
        PlayerList players = Objects.requireNonNull(sender.level().getServer()).getPlayerList();
        return pending == null ? Collections.emptyList() :
            pending.keySet().stream()
                .map(players::getPlayer)
                .filter(Objects::nonNull)
                .map(ServerPlayer::getGameProfile)
                .collect(Collectors.toSet());
    }

    public void requestTo(@NotNull ServerPlayer sender, @NotNull ServerPlayer target) {
        if (sender == target) {
            MessageManager.INSTANCE.sendSelfTpMsg(sender);
            return;
        }

        Map<UUID, RequestTask> sent = this.req_by_sender
            .computeIfAbsent(sender.getUUID(), x -> new HashMap<>());
        if (sent.values().stream().anyMatch(x -> x.type != null)) {
            MessageManager.INSTANCE.sendCancelTip(sender);
            return;
        }

        RequestTask req = sent.computeIfAbsent(
            target.getUUID(), x -> new RequestTask(target));
        req.clear();

        req.type = RequestTask.TeleportType.ARRIVE;
        req.task = this.scheduler.schedule(() -> {
            try {
                Objects.requireNonNull(sender.level().getServer()).execute(() -> {
                    MessageManager.INSTANCE.sendReqTimeoutMsg(sender, target);
                    MessageManager.INSTANCE.sendExpiredReqMsg(target, sender);

                    this.req_by_receiver.get(target.getUUID()).remove(sender.getUUID());
                    req.clear();
                });
            } catch (Throwable ignored) {
            }
        }, 60, TimeUnit.SECONDS);

        this.req_by_receiver
            .computeIfAbsent(target.getUUID(), x -> new HashMap<>())
            .put(sender.getUUID(), req);

        MessageManager.INSTANCE.sendReqSentMsg(sender, target);
        MessageManager.INSTANCE.sendArriveReqReceivedMsg(target, sender);
    }

    public void requestHere(@NotNull ServerPlayer sender, @NotNull ServerPlayer target) {
        if (sender == target) {
            MessageManager.INSTANCE.sendSelfTpMsg(sender);
            return;
        }

        Map<UUID, RequestTask> sent = this.req_by_sender
            .computeIfAbsent(sender.getUUID(), x -> new HashMap<>());
        Optional<RequestTask> pending = sent.values().stream()
            .filter(x -> x.type == RequestTask.TeleportType.ARRIVE).findFirst();

        if (pending.isPresent()) {
            PlayerList players = Objects.requireNonNull(sender.level().getServer()).getPlayerList();
            ServerPlayer player = players.getPlayer(pending.get().target);
            if (player != null) {
                MessageManager.INSTANCE.sendCancelTip(sender, player);
                return;
            }
        }

        RequestTask req = sent.computeIfAbsent(
            target.getUUID(), x -> new RequestTask(target));
        req.clear();

        req.type = RequestTask.TeleportType.SUMMON;
        req.task = this.scheduler.schedule(() -> {
            try {
                Objects.requireNonNull(sender.level().getServer()).execute(() -> {
                    MessageManager.INSTANCE.sendReqTimeoutMsg(sender, target);
                    MessageManager.INSTANCE.sendExpiredReqMsg(target, sender);

                    this.req_by_receiver.get(target.getUUID()).remove(sender.getUUID());
                    req.clear();
                });
            } catch (Throwable ignored) {
            }
        }, 60, TimeUnit.SECONDS);

        this.req_by_receiver
            .computeIfAbsent(target.getUUID(), x -> new HashMap<>())
            .put(sender.getUUID(), req);

        MessageManager.INSTANCE.sendReqSentMsg(sender, target);
        MessageManager.INSTANCE.sendSummonReqReceivedMsg(target, sender);
    }

    public void cancelRequest(@NotNull ServerPlayer sender, @NotNull ServerPlayer target) {
        Map<UUID, RequestTask> sent = this.req_by_sender.get(sender.getUUID());
        if (sent == null || sent.isEmpty()) {
            MessageManager.INSTANCE.sendNoSentTip(sender);
            return;
        }

        RequestTask req = sent.get(target.getUUID());
        if (req == null || req.type == null) {
            MessageManager.INSTANCE.sendTargetNotFoundMsg(sender, target);
            return;
        }

        MessageManager.INSTANCE.sendCancelReqMsg(sender, target);
        MessageManager.INSTANCE.sendReqCancelledMsg(target, sender);

        this.req_by_receiver.get(target.getUUID()).remove(sender.getUUID());
        req.clear();
    }

    public void acceptRequest(@NotNull ServerPlayer sender, @NotNull ServerPlayer source) {
        Map<UUID, RequestTask> pending = this.req_by_receiver.get(sender.getUUID());
        if (pending == null) {
            MessageManager.INSTANCE.sendNoPendingTip(sender);
            return;
        }

        RequestTask req = pending.get(source.getUUID());
        if (req == null || req.type == null) {
            MessageManager.INSTANCE.sendSourceNotFoundMsg(sender, source);
            return;
        }

        if (req.type == RequestTask.TeleportType.ARRIVE)
            source.teleportTo(
                sender.level(), sender.getX(), sender.getY(), sender.getZ(),
                Relative.DELTA, sender.getYRot(), sender.getXRot(), false
            );
        else if (req.type == RequestTask.TeleportType.SUMMON)
            sender.teleportTo(
                source.level(), source.getX(), source.getY(), source.getZ(),
                Relative.DELTA, source.getYRot(), source.getXRot(), false
            );

        MessageManager.INSTANCE.sendReqAcceptedMsg(source, sender);
        MessageManager.INSTANCE.sendAcceptReqMsg(sender, source);

        pending.remove(source.getUUID());
        req.clear();
    }

    public void denyRequest(@NotNull ServerPlayer sender, @NotNull ServerPlayer source) {
        Map<UUID, RequestTask> pending = this.req_by_receiver.get(sender.getUUID());
        if (pending == null) {
            MessageManager.INSTANCE.sendNoPendingTip(sender);
            return;
        }

        RequestTask req = pending.get(source.getUUID());
        if (req == null || req.type == null) {
            MessageManager.INSTANCE.sendSourceNotFoundMsg(sender, source);
            return;
        }

        MessageManager.INSTANCE.sendReqDeniedMsg(source, sender);
        MessageManager.INSTANCE.sendDenyReqMsg(sender, source);

        pending.remove(source.getUUID());
        req.clear();
    }

    public void onPlayerDisconnect(@NotNull ServerPlayer player) {
        PlayerList players = Objects.requireNonNull(player.level().getServer()).getPlayerList();

        Map<UUID, RequestTask> sent = this.req_by_sender.get(player.getUUID());
        if (sent != null) {
            sent.forEach((uuid, req) -> {
                if (req.type == null)
                    return;

                ServerPlayer sender = players.getPlayer(req.target);
                if (sender != null)
                    MessageManager.INSTANCE.sendReqCancelledMsg(sender, player);
                this.req_by_receiver.get(req.target).remove(player.getUUID());
                req.clear();
            });
            sent.clear();
        }

        Map<UUID, RequestTask> pending = this.req_by_receiver.get(player.getUUID());
        if (pending != null) {
            pending.forEach((uuid, req) -> {
                if (req.type == null)
                    return;

                ServerPlayer target = players.getPlayer(uuid);
                if (target != null)
                    MessageManager.INSTANCE.sendTargetDisconnectedMsg(target, player);
                req.clear();
            });
            pending.clear();
        }
    }

    @Override
    public void init() {
        ServerPlayConnectionEvents.DISCONNECT.register(
            (l, s) -> this.onPlayerDisconnect(l.player)
        );
        ServerLifecycleEvents.SERVER_STOPPING.register(s -> {
            this.req_by_sender.values().stream()
                .flatMap(x -> x.values().stream())
                .forEach(RequestTask::clear);
            this.req_by_sender.clear();
            this.req_by_receiver.clear();
        });
    }

    private static final class RequestTask {
        public final UUID target;
        public ScheduledFuture<?> task;
        public TeleportType type;

        public RequestTask(@NotNull ServerPlayer target) {
            this.target = target.getUUID();
        }

        public void clear() {
            if (this.task != null && !this.task.isDone())
                this.task.cancel(true);
            this.task = null;
            this.type = null;
        }

        public enum TeleportType {
            ARRIVE, SUMMON
        }
    }
}
