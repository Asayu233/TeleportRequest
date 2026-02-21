package xyz.asayu233.tpask.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.portal.TeleportTransition;
import org.jetbrains.annotations.NotNull;
import xyz.asayu233.tpask.manager.ConfigManager;

import java.util.List;

public final class HomeCommand {
    private static final List<String> aliases = List.of("home");

    private HomeCommand() {}

    public static void register(@NotNull CommandDispatcher<CommandSourceStack> dispatcher) {
        if (ConfigManager.INSTANCE.getConfig().isHomeEnabled()) {
            for (var alias : aliases)
                dispatcher.register(Commands
                    .literal(alias)
                    .requires(CommandSourceStack::isPlayer)
                    .executes(HomeCommand::execute)
                );
        }
    }

    private static int execute(@NotNull CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        if (player.getRespawnConfig() != null) {
            var tp = player.findRespawnPositionAndUseSpawnBlock(false, TeleportTransition.DO_NOTHING);
            if (!tp.missingRespawnBlock()) {
                player.teleport(tp);
                return 1;
            }
        }
        player.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.NO_RESPAWN_BLOCK_AVAILABLE, 0F));
        return 0;
    }
}
