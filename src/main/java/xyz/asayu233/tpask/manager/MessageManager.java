package xyz.asayu233.tpask.manager;

import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import xyz.asayu233.tpask.language.Lang_en_us;
import xyz.asayu233.tpask.language.Lang_zh_cn;

import java.util.Map;

public final class MessageManager extends BaseManager {
    public static final MessageManager INSTANCE = new MessageManager();
    private final Map<String, Language> i18n_map = Map.of(
        "en_us", new Lang_en_us(),
        "zh_cn", new Lang_zh_cn()
    );
    private final Language defaultLang = this.i18n_map.get("en_us");

    private MessageManager() {}

    public void sendReqSentMsg(@NotNull ServerPlayer player, @NotNull ServerPlayer target) {
        this.i18n_map
            .getOrDefault(player.clientInformation().language(), this.defaultLang)
            .sendReqSentMsg(player, target);
    }

    public void sendCancelReqMsg(@NotNull ServerPlayer player, @NotNull ServerPlayer target) {
        this.i18n_map
            .getOrDefault(player.clientInformation().language(), this.defaultLang)
            .sendCancelReqMsg(player, target);
    }

    public void sendReqAcceptedMsg(@NotNull ServerPlayer player, @NotNull ServerPlayer target) {
        this.i18n_map
            .getOrDefault(player.clientInformation().language(), this.defaultLang)
            .sendReqAcceptedMsg(player, target);
    }

    public void sendReqDeniedMsg(@NotNull ServerPlayer player, @NotNull ServerPlayer target) {
        this.i18n_map
            .getOrDefault(player.clientInformation().language(), this.defaultLang)
            .sendReqDeniedMsg(player, target);
    }

    public void sendReqTimeoutMsg(@NotNull ServerPlayer player, @NotNull ServerPlayer target) {
        this.i18n_map
            .getOrDefault(player.clientInformation().language(), this.defaultLang)
            .sendReqTimeoutMsg(player, target);
    }

    public void sendTargetDisconnectedMsg(@NotNull ServerPlayer player, @NotNull ServerPlayer target) {
        this.i18n_map
            .getOrDefault(player.clientInformation().language(), this.defaultLang)
            .sendTargetDisconnectedMsg(player, target);
    }

    public void sendTargetNotFoundMsg(@NotNull ServerPlayer player, @NotNull ServerPlayer target) {
        this.i18n_map
            .getOrDefault(player.clientInformation().language(), this.defaultLang)
            .sendTargetNotFoundMsg(player, target);
    }

    public void sendArriveReqReceivedMsg(@NotNull ServerPlayer player, @NotNull ServerPlayer source) {
        this.i18n_map
            .getOrDefault(player.clientInformation().language(), this.defaultLang)
            .sendArriveReqReceivedMsg(player, source);
    }

    public void sendSummonReqReceivedMsg(@NotNull ServerPlayer player, @NotNull ServerPlayer source) {
        this.i18n_map
            .getOrDefault(player.clientInformation().language(), this.defaultLang)
            .sendSummonReqReceivedMsg(player, source);
    }

    public void sendReqCancelledMsg(@NotNull ServerPlayer player, @NotNull ServerPlayer source) {
        this.i18n_map
            .getOrDefault(player.clientInformation().language(), this.defaultLang)
            .sendReqCancelledMsg(player, source);
    }

    public void sendAcceptReqMsg(@NotNull ServerPlayer player, @NotNull ServerPlayer source) {
        this.i18n_map
            .getOrDefault(player.clientInformation().language(), this.defaultLang)
            .sendAcceptReqMsg(player, source);
    }

    public void sendDenyReqMsg(@NotNull ServerPlayer player, @NotNull ServerPlayer source) {
        this.i18n_map
            .getOrDefault(player.clientInformation().language(), this.defaultLang)
            .sendDenyReqMsg(player, source);
    }

    public void sendExpiredReqMsg(@NotNull ServerPlayer player, @NotNull ServerPlayer source) {
        this.i18n_map
            .getOrDefault(player.clientInformation().language(), this.defaultLang)
            .sendExpiredReqMsg(player, source);
    }

    public void sendSourceNotFoundMsg(@NotNull ServerPlayer player, @NotNull ServerPlayer source) {
        this.i18n_map
            .getOrDefault(player.clientInformation().language(), this.defaultLang)
            .sendSourceNotFoundMsg(player, source);
    }

    public void sendSelfTpMsg(@NotNull ServerPlayer player) {
        this.i18n_map
            .getOrDefault(player.clientInformation().language(), this.defaultLang)
            .sendSelfTpMsg(player);
    }

    public void sendCancelTip(@NotNull ServerPlayer player) {
        this.i18n_map
            .getOrDefault(player.clientInformation().language(), this.defaultLang)
            .sendCancelTip(player);
    }

    public void sendCancelTip(@NotNull ServerPlayer player, @NotNull ServerPlayer target) {
        this.i18n_map
            .getOrDefault(player.clientInformation().language(), this.defaultLang)
            .sendCancelTip(player, target);
    }

    public void sendNoSentTip(@NotNull ServerPlayer player) {
        this.i18n_map
            .getOrDefault(player.clientInformation().language(), this.defaultLang)
            .sendNoSentTip(player);
    }

    public void sendNoPendingTip(@NotNull ServerPlayer player) {
        this.i18n_map
            .getOrDefault(player.clientInformation().language(), this.defaultLang)
            .sendNoPendingTip(player);
    }

    public void sendNoDeathPosTip(@NotNull ServerPlayer player) {
        this.i18n_map
            .getOrDefault(player.clientInformation().language(), this.defaultLang)
            .sendNoDeathPosTip(player);
    }

    @Override
    public void init() {}

    public interface Language {
        void sendReqSentMsg(@NotNull ServerPlayer player, @NotNull ServerPlayer target);
        void sendCancelReqMsg(@NotNull ServerPlayer player, @NotNull ServerPlayer target);
        void sendReqAcceptedMsg(@NotNull ServerPlayer player, @NotNull ServerPlayer target);
        void sendReqDeniedMsg(@NotNull ServerPlayer player, @NotNull ServerPlayer target);
        void sendReqTimeoutMsg(@NotNull ServerPlayer player, @NotNull ServerPlayer target);
        void sendTargetDisconnectedMsg(@NotNull ServerPlayer player, @NotNull ServerPlayer target);
        void sendTargetNotFoundMsg(@NotNull ServerPlayer player, @NotNull ServerPlayer target);
        void sendArriveReqReceivedMsg(@NotNull ServerPlayer player, @NotNull ServerPlayer source);
        void sendSummonReqReceivedMsg(@NotNull ServerPlayer player, @NotNull ServerPlayer source);
        void sendReqCancelledMsg(@NotNull ServerPlayer player, @NotNull ServerPlayer source);
        void sendAcceptReqMsg(@NotNull ServerPlayer player, @NotNull ServerPlayer source);
        void sendDenyReqMsg(@NotNull ServerPlayer player, @NotNull ServerPlayer source);
        void sendExpiredReqMsg(@NotNull ServerPlayer player, @NotNull ServerPlayer source);
        void sendSourceNotFoundMsg(@NotNull ServerPlayer player, @NotNull ServerPlayer source);
        void sendSelfTpMsg(@NotNull ServerPlayer player);
        void sendCancelTip(@NotNull ServerPlayer player);
        void sendCancelTip(@NotNull ServerPlayer player, @NotNull ServerPlayer target);
        void sendNoSentTip(@NotNull ServerPlayer player);
        void sendNoPendingTip(@NotNull ServerPlayer player);
        void sendNoDeathPosTip(@NotNull ServerPlayer player);
    }
}
