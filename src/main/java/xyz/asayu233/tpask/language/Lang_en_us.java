package xyz.asayu233.tpask.language;

import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.protocol.game.ClientboundSoundEntityPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import org.jetbrains.annotations.NotNull;
import xyz.asayu233.tpask.manager.MessageManager;

import java.util.Objects;

public final class Lang_en_us implements MessageManager.Language {
    private final Component txtPrefix = Component.empty()
        .append(Component.literal("[").withStyle(ChatFormatting.GRAY))
        .append(Component.literal("Teleport Request").withStyle(ChatFormatting.LIGHT_PURPLE))
        .append(Component.literal("]").withStyle(ChatFormatting.GRAY))
        .append(" ");

    private final Component txtAcceptBtn = Component.literal("[Accept]").withStyle(ChatFormatting.GREEN).withStyle(style ->
        style.withHoverEvent(new HoverEvent.ShowText(Component.literal("Click to accept this request."))));
    private final Component txtDenyBtn = Component.literal("[Deny]").withStyle(ChatFormatting.RED).withStyle(style ->
        style.withHoverEvent(new HoverEvent.ShowText(Component.literal("Click to deny this request."))));
    private final Component txtCancelBtn = Component.literal("[Cancel]").withStyle(ChatFormatting.AQUA).withStyle(style -> style
        .withHoverEvent(new HoverEvent.ShowText(Component.literal("Click to cancel this request."))));

    private final Component txtCancelWait = Component.literal("You are waiting for a response from ").withStyle(ChatFormatting.RED);
    private final Component txtReqSent = Component.literal("Requested to player ").withStyle(ChatFormatting.YELLOW);
    private final Component txtCancelReq = Component.literal("Cancelled the request to ").withStyle(ChatFormatting.YELLOW);
    private final Component txtReqAccepted = Component.literal(" accepted your request.").withStyle(ChatFormatting.GREEN);
    private final Component txtReqDenied = Component.literal(" denied your request.").withStyle(ChatFormatting.RED);
    private final Component txtReqTimeout = Component.literal(" did not respond in time.").withStyle(ChatFormatting.YELLOW);
    private final Component txtTargetDisconnectedHead = Component.literal("Target player ").withStyle(ChatFormatting.YELLOW);
    private final Component txtTargetDisconnectedTail = Component.literal(" is offline!").withStyle(ChatFormatting.YELLOW);
    private final Component txtTargetNotFound = Component.literal("No pending request to ").withStyle(ChatFormatting.RED);

    private final Component txtArriveReqReceived = Component.literal(" wants to teleport to you... ").withStyle(ChatFormatting.YELLOW);
    private final Component txtSummonReqReceived = Component.literal(" wants you to teleport to them... ").withStyle(ChatFormatting.YELLOW);
    private final Component txtReqCancelled = Component.literal(" cancelled their request.").withStyle(ChatFormatting.YELLOW);
    private final Component txtAcceptReq = Component.literal("Accepted the request from ").withStyle(ChatFormatting.GREEN);
    private final Component txtDenyReq = Component.literal("Denied the request from ").withStyle(ChatFormatting.RED);
    private final Component txtReqExpiredHead = Component.literal("The request from ").withStyle(ChatFormatting.YELLOW);
    private final Component txtReqExpiredTail = Component.literal(" has expired.").withStyle(ChatFormatting.YELLOW);
    private final Component txtSourceNotFound = Component.literal("No pending request from ").withStyle(ChatFormatting.RED);

    private final Component msgSelfTpTip = Component.empty().append(this.txtPrefix)
        .append(Component.literal("Cannot request to yourself!").withStyle(ChatFormatting.RED));
    private final Component msgCancelTip = Component.empty().append(this.txtPrefix)
        .append(Component.literal("Please cancel existing requests first!").withStyle(ChatFormatting.RED));
    private final Component msgNoSentTip = Component.empty().append(this.txtPrefix)
        .append(Component.literal("You have no sent requests!").withStyle(ChatFormatting.RED));
    private final Component msgNoPendingTip = Component.empty().append(this.txtPrefix)
        .append(Component.literal("You have no pending request!").withStyle(ChatFormatting.RED));
    private final Component msgNoDeathPos = Component.empty().append(this.txtPrefix)
        .append(Component.literal("You have no valid death location yet!").withStyle(ChatFormatting.RED));

    @Override
    public void sendReqSentMsg(@NotNull ServerPlayer player, @NotNull ServerPlayer target) {
        player.sendSystemMessage(Component.empty()
            .append(this.txtPrefix)
            .append(this.txtReqSent)
            .append(Objects.requireNonNull(target.getDisplayName()).copy().withStyle(ChatFormatting.GOLD))
            .append(" ")
            .append(this.txtCancelBtn)
        );
    }

    @Override
    public void sendCancelReqMsg(@NotNull ServerPlayer player, @NotNull ServerPlayer target) {
        player.sendSystemMessage(Component.empty()
            .append(this.txtPrefix)
            .append(this.txtCancelReq)
            .append(Objects.requireNonNull(target.getDisplayName()).copy().withStyle(ChatFormatting.GOLD))
        );
    }

    @Override
    public void sendReqAcceptedMsg(@NotNull ServerPlayer player, @NotNull ServerPlayer target) {
        player.sendSystemMessage(Component.empty()
            .append(this.txtPrefix)
            .append(Objects.requireNonNull(target.getDisplayName()).copy().withStyle(ChatFormatting.GOLD))
            .append(this.txtReqAccepted)
        );
        player.level().playSound(null, player, SoundEvents.PLAYER_TELEPORT, SoundSource.PLAYERS, 3f, 1f);
    }

    @Override
    public void sendReqDeniedMsg(@NotNull ServerPlayer player, @NotNull ServerPlayer target) {
        player.sendSystemMessage(Component.empty()
            .append(this.txtPrefix)
            .append(Objects.requireNonNull(target.getDisplayName()).copy().withStyle(ChatFormatting.GOLD))
            .append(this.txtReqDenied)
        );
        player.connection.send(new ClientboundSoundEntityPacket(
            BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.ANVIL_LAND),
            SoundSource.PLAYERS, player, 1.5f, 0.75f, player.getRandom().nextLong()
        ));
    }

    @Override
    public void sendReqTimeoutMsg(@NotNull ServerPlayer player, @NotNull ServerPlayer target) {
        player.sendSystemMessage(Component.empty()
            .append(this.txtPrefix)
            .append(Objects.requireNonNull(target.getDisplayName()).copy().withStyle(ChatFormatting.GOLD))
            .append(this.txtReqTimeout)
        );
    }

    @Override
    public void sendTargetDisconnectedMsg(@NotNull ServerPlayer player, @NotNull ServerPlayer target) {
        player.sendSystemMessage(Component.empty()
            .append(this.txtPrefix)
            .append(this.txtTargetDisconnectedHead)
            .append(Objects.requireNonNull(target.getDisplayName()).copy().withStyle(ChatFormatting.GOLD))
            .append(this.txtTargetDisconnectedTail)
        );
    }

    @Override
    public void sendTargetNotFoundMsg(@NotNull ServerPlayer player, @NotNull ServerPlayer target) {
        player.sendSystemMessage(Component.empty()
            .append(this.txtTargetNotFound)
            .append(Objects.requireNonNull(target.getDisplayName()).copy().withStyle(ChatFormatting.GOLD))
        );
    }

    @Override
    public void sendArriveReqReceivedMsg(@NotNull ServerPlayer player, @NotNull ServerPlayer source) {
        player.sendSystemMessage(Component.empty()
            .append(this.txtPrefix)
            .append(Objects.requireNonNull(source.getDisplayName()).copy().withStyle(ChatFormatting.GOLD))
            .append(this.txtArriveReqReceived)
            .append(this.txtAcceptBtn.copy().withStyle(style -> style.withClickEvent(
                new ClickEvent.RunCommand("/tpy " + source.getGameProfile().name()))))
            .append(Component.literal(" | ").withStyle(ChatFormatting.GRAY))
            .append(this.txtDenyBtn.copy().withStyle(style -> style.withClickEvent(
                new ClickEvent.RunCommand("/tpn " + source.getGameProfile().name()))))
        );
        player.connection.send(new ClientboundSoundEntityPacket(
            BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.PLAYER_LEVELUP),
            SoundSource.PLAYERS, player, 1.5f, 0.5f, player.getRandom().nextLong()
        ));
    }

    @Override
    public void sendSummonReqReceivedMsg(@NotNull ServerPlayer player, @NotNull ServerPlayer source) {
        player.sendSystemMessage(Component.empty()
            .append(this.txtPrefix)
            .append(Objects.requireNonNull(source.getDisplayName()).copy().withStyle(ChatFormatting.GOLD))
            .append(this.txtSummonReqReceived)
            .append(this.txtAcceptBtn.copy().withStyle(style -> style.withClickEvent(
                new ClickEvent.RunCommand("/tpy " + source.getGameProfile().name()))))
            .append(Component.literal(" | ").withStyle(ChatFormatting.GRAY))
            .append(this.txtDenyBtn.copy().withStyle(style -> style.withClickEvent(
                new ClickEvent.RunCommand("/tpn " + source.getGameProfile().name()))))
        );
        player.connection.send(new ClientboundSoundEntityPacket(
            BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.PLAYER_LEVELUP),
            SoundSource.PLAYERS, player, 1.5f, 0.5f, player.getRandom().nextLong()
        ));
    }

    @Override
    public void sendReqCancelledMsg(@NotNull ServerPlayer player, @NotNull ServerPlayer source) {
        player.sendSystemMessage(Component.empty()
            .append(this.txtPrefix)
            .append(Objects.requireNonNull(source.getDisplayName()).copy().withStyle(ChatFormatting.GOLD))
            .append(this.txtReqCancelled)
        );
    }

    @Override
    public void sendAcceptReqMsg(@NotNull ServerPlayer player, @NotNull ServerPlayer source) {
        player.sendSystemMessage(Component.empty()
            .append(this.txtPrefix)
            .append(this.txtAcceptReq)
            .append(Objects.requireNonNull(source.getDisplayName()).copy().withStyle(ChatFormatting.GOLD))
        );
        player.playSound(SoundEvents.PLAYER_TELEPORT, 3f, 1f);
    }

    @Override
    public void sendDenyReqMsg(@NotNull ServerPlayer player, @NotNull ServerPlayer source) {
        player.sendSystemMessage(Component.empty()
            .append(this.txtPrefix)
            .append(this.txtDenyReq)
            .append(Objects.requireNonNull(source.getDisplayName()).copy().withStyle(ChatFormatting.GOLD))
        );
    }

    @Override
    public void sendExpiredReqMsg(@NotNull ServerPlayer player, @NotNull ServerPlayer source) {
        player.sendSystemMessage(Component.empty()
            .append(this.txtPrefix)
            .append(this.txtReqExpiredHead)
            .append(Objects.requireNonNull(source.getDisplayName()).copy().withStyle(ChatFormatting.GOLD))
            .append(this.txtReqExpiredTail)
        );
    }

    @Override
    public void sendSourceNotFoundMsg(@NotNull ServerPlayer player, @NotNull ServerPlayer source) {
        player.sendSystemMessage(Component.empty()
            .append(this.txtPrefix)
            .append(this.txtSourceNotFound)
            .append(Objects.requireNonNull(source.getDisplayName()).copy().withStyle(ChatFormatting.GOLD))
        );
    }

    @Override
    public void sendSelfTpMsg(@NotNull ServerPlayer player) {
        player.sendSystemMessage(this.msgSelfTpTip);
    }

    @Override
    public void sendCancelTip(@NotNull ServerPlayer player) {
        player.sendSystemMessage(this.msgCancelTip);
    }

    @Override
    public void sendCancelTip(@NotNull ServerPlayer player, @NotNull ServerPlayer target) {
        player.sendSystemMessage(Component.empty()
            .append(this.txtCancelWait)
            .append(Objects.requireNonNull(target.getDisplayName()).copy().withStyle(ChatFormatting.GOLD))
            .append(" ")
            .append(this.txtCancelBtn.copy().withStyle(style -> style.withClickEvent(
                new ClickEvent.RunCommand("/tpx " + target.getGameProfile().name()))))
        );
    }

    @Override
    public void sendNoSentTip(@NotNull ServerPlayer player) {
        player.sendSystemMessage(this.msgNoSentTip);
    }

    @Override
    public void sendNoPendingTip(@NotNull ServerPlayer player) {
        player.sendSystemMessage(this.msgNoPendingTip);
    }

    @Override
    public void sendNoDeathPosTip(@NotNull ServerPlayer player) {
        player.sendSystemMessage(this.msgNoDeathPos);
    }
}
