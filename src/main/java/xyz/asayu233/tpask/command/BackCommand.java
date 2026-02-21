package xyz.asayu233.tpask.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import xyz.asayu233.tpask.manager.ConfigManager;
import xyz.asayu233.tpask.manager.MessageManager;

import java.util.List;
import java.util.Set;

public final class BackCommand {
    private static final List<String> aliases = List.of("back");

    private BackCommand() {}

    public static void register(@NotNull CommandDispatcher<CommandSourceStack> dispatcher) {
        if (ConfigManager.INSTANCE.getConfig().isBackEnabled()) {
            for (var alias : aliases) {
                dispatcher.register(Commands
                    .literal(alias)
                    .requires(CommandSourceStack::isPlayer)
                    .executes(BackCommand::execute)
                );
            }
        }
    }

    private static int execute(@NotNull CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        var optional = player.getLastDeathLocation();
        if (optional.isPresent()) {
            var lastDeath = optional.get();
            var level = player.level().getServer().getLevel(lastDeath.dimension());
            if (level == null) {
                MessageManager.INSTANCE.sendNoDeathPosTip(player);
                return 0;
            }

            var pos = lastDeath.pos();
            player.teleportTo(level,
                pos.getX() + 0.5d, pos.getY(), pos.getZ() + 0.5d,
                Set.of(), player.getYRot(), player.getXRot(), false
            );
            return 1;
        } else {
            MessageManager.INSTANCE.sendNoDeathPosTip(player);
            return 0;
        }
    }
}
