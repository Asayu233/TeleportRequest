package xyz.asayu233.tpask;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import org.slf4j.Logger;
import xyz.asayu233.tpask.command.*;
import xyz.asayu233.tpask.manager.BaseManager;
import xyz.asayu233.tpask.manager.MessageManager;
import xyz.asayu233.tpask.manager.RequestManager;

import java.util.List;

public final class TeleportRequest implements ModInitializer {
    public static final TeleportRequest INSTANCE = new TeleportRequest();
    private final List<BaseManager> managers = List.of(
        MessageManager.INSTANCE,
        RequestManager.INSTANCE
    );

    private TeleportRequest() {}

    @Override
    public void onInitialize() {
        this.managers.forEach(BaseManager::init);
        CommandRegistrationCallback.EVENT.register(
            (d, a, e) -> {
                TPAskCommand.register(d);
                TPAskHereCommand.register(d);
                TPCancelCommand.register(d);
                TPAcceptCommand.register(d);
                TPDenyCommand.register(d);
                HomeCommand.register(d);
                SpawnCommand.register(d);
                BackCommand.register(d);
            }
        );
    }
}
