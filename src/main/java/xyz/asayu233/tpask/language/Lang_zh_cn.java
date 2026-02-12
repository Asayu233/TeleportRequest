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

public final class Lang_zh_cn implements MessageManager.Language {
    private final Component txtPrefix = Component.empty()
        .append(Component.literal("[").withStyle(ChatFormatting.GRAY))
        .append(Component.literal("传送请求").withStyle(ChatFormatting.LIGHT_PURPLE))
        .append(Component.literal("]").withStyle(ChatFormatting.GRAY))
        .append(" ");

    private final Component txtAcceptBtn = Component.literal("[接受]").withStyle(ChatFormatting.GREEN).withStyle(style ->
        style.withHoverEvent(new HoverEvent.ShowText(Component.literal("点击接受传送请求"))));
    private final Component txtDenyBtn = Component.literal("[拒绝]").withStyle(ChatFormatting.RED).withStyle(style ->
        style.withHoverEvent(new HoverEvent.ShowText(Component.literal("点击拒绝传送请求"))));
    private final Component txtCancelBtn = Component.literal("[取消]").withStyle(ChatFormatting.AQUA).withStyle(style -> style
        .withHoverEvent(new HoverEvent.ShowText(Component.literal("点击取消传送请求"))));

    private final Component txtCancelWait = Component.literal("正在等待传送至 ").withStyle(ChatFormatting.RED);
    private final Component txtReqSent = Component.literal("已发送传送请求至 ").withStyle(ChatFormatting.YELLOW);
    private final Component txtCancelReqHead = Component.literal("已取消对 ").withStyle(ChatFormatting.YELLOW);
    private final Component txtCancelReqTail = Component.literal(" 的传送请求").withStyle(ChatFormatting.YELLOW);
    private final Component txtReqAccepted = Component.literal(" 接受了你的传送请求").withStyle(ChatFormatting.GREEN);
    private final Component txtReqDenied = Component.literal(" 拒绝了你的传送请求").withStyle(ChatFormatting.RED);
    private final Component txtReqTimeout = Component.literal(" 超时未响应").withStyle(ChatFormatting.YELLOW);
    private final Component txtTargetDisconnectedHead = Component.literal("你请求传送的玩家 ").withStyle(ChatFormatting.YELLOW);
    private final Component txtTargetDisconnectedTail = Component.literal(" 已离线！").withStyle(ChatFormatting.YELLOW);
    private final Component txtTargetNotFoundHead = Component.literal("你没有对 ").withStyle(ChatFormatting.RED);
    private final Component txtTargetNotFoundTail = Component.literal(" 的传送请求待处理！").withStyle(ChatFormatting.RED);

    private final Component txtArriveReqReceived = Component.literal(" 请求传送到你的位置... ").withStyle(ChatFormatting.YELLOW);
    private final Component txtSummonReqReceived = Component.literal(" 请求你传送到他的位置... ").withStyle(ChatFormatting.YELLOW);
    private final Component txtReqCanceled = Component.literal(" 取消了他的传送请求").withStyle(ChatFormatting.YELLOW);
    private final Component txtAcceptReqHead = Component.literal("已接受 ").withStyle(ChatFormatting.GREEN);
    private final Component txtAcceptReqTail = Component.literal(" 的传送请求").withStyle(ChatFormatting.GREEN);
    private final Component txtDenyReqHead = Component.literal("已拒绝 ").withStyle(ChatFormatting.RED);
    private final Component txtDenyReqTail = Component.literal(" 的传送请求").withStyle(ChatFormatting.RED);
    private final Component txtReqExpiredHead = Component.literal("来自 ").withStyle(ChatFormatting.YELLOW);
    private final Component txtReqExpiredTail = Component.literal(" 的传送请求已超时").withStyle(ChatFormatting.YELLOW);
    private final Component txtSourceNotFoundHead = Component.literal("你没有来自 ").withStyle(ChatFormatting.RED);
    private final Component txtSourceNotFoundTail = Component.literal(" 的传送请求待处理！").withStyle(ChatFormatting.RED);

    private final Component msgSelfTpTip = Component.empty().append(this.txtPrefix)
        .append(Component.literal("无法对自己发起传送请求！").withStyle(ChatFormatting.RED));
    private final Component msgCancelTip = Component.empty().append(this.txtPrefix)
        .append(Component.literal("请先取消已有的传送请求！").withStyle(ChatFormatting.RED));
    private final Component msgNoSentTip = Component.empty().append(this.txtPrefix)
        .append(Component.literal("你没有已发送的传送请求！").withStyle(ChatFormatting.RED));
    private final Component msgNoPendingTip = Component.empty().append(this.txtPrefix)
        .append(Component.literal("你没有待处理的传送请求！").withStyle(ChatFormatting.RED));
    private final Component msgNoDeathPos = Component.empty().append(this.txtPrefix)
        .append(Component.literal("尚无有效的死亡点！").withStyle(ChatFormatting.RED));

    @Override
    public void sendReqSentMsg(@NotNull ServerPlayer player, @NotNull ServerPlayer target) {
        player.sendSystemMessage(Component.empty()
            .append(this.txtPrefix)
            .append(this.txtReqSent)
            .append(Objects.requireNonNull(target.getDisplayName()).copy().withStyle(ChatFormatting.GOLD))
            .append(" ")
            .append(this.txtCancelBtn.copy().withStyle(style -> style.withClickEvent(
                new ClickEvent.RunCommand("/tpx " + target.getGameProfile().name()))))
        );
    }

    @Override
    public void sendCancelReqMsg(@NotNull ServerPlayer player, @NotNull ServerPlayer target) {
        player.sendSystemMessage(Component.empty()
            .append(this.txtPrefix)
            .append(this.txtCancelReqHead)
            .append(Objects.requireNonNull(target.getDisplayName()).copy().withStyle(ChatFormatting.GOLD))
            .append(this.txtCancelReqTail)
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
            .append(this.txtTargetNotFoundHead)
            .append(Objects.requireNonNull(target.getDisplayName()).copy().withStyle(ChatFormatting.GOLD))
            .append(this.txtTargetNotFoundTail)
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
            .append(this.txtReqCanceled)
        );
    }

    @Override
    public void sendAcceptReqMsg(@NotNull ServerPlayer player, @NotNull ServerPlayer source) {
        player.sendSystemMessage(Component.empty()
            .append(this.txtPrefix)
            .append(this.txtAcceptReqHead)
            .append(Objects.requireNonNull(source.getDisplayName()).copy().withStyle(ChatFormatting.GOLD))
            .append(this.txtAcceptReqTail)
        );
        player.playSound(SoundEvents.PLAYER_TELEPORT, 3f, 1f);
    }

    @Override
    public void sendDenyReqMsg(@NotNull ServerPlayer player, @NotNull ServerPlayer source) {
        player.sendSystemMessage(Component.empty()
            .append(this.txtPrefix)
            .append(this.txtDenyReqHead)
            .append(Objects.requireNonNull(source.getDisplayName()).copy().withStyle(ChatFormatting.GOLD))
            .append(this.txtDenyReqTail)
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
            .append(this.txtSourceNotFoundHead)
            .append(Objects.requireNonNull(source.getDisplayName()).copy().withStyle(ChatFormatting.GOLD))
            .append(this.txtSourceNotFoundTail)
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
