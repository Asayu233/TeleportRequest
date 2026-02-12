package xyz.asayu233.tpask.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.levelgen.Heightmap;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public final class SpawnCommand {
    private static final List<String> aliases = List.of("spawn");

    private SpawnCommand() {}

    public static void register(@NotNull CommandDispatcher<CommandSourceStack> dispatcher) {
        for (var alias : aliases)
            dispatcher.register(Commands
                .literal(alias)
                .requires(CommandSourceStack::isPlayer)
                .executes(SpawnCommand::execute)
            );
    }

    private static int execute(@NotNull CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        ServerLevel level = player.level().getServer().findRespawnDimension();
        var data = level.getRespawnData();
        var height = level.getChunkAt(data.pos()).getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, data.pos().getX(), data.pos().getZ()) + 1d;
        player.teleportTo(level,
            data.pos().getX() + 0.5d, height, data.pos().getZ(),
            Set.of(), data.yaw(), data.pitch(), false
        );
        return 1;
    }
}
