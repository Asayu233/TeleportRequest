package xyz.asayu233.tpask.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import xyz.asayu233.tpask.manager.RequestManager;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class TPAskCommand {
    private static final List<String> aliases = List.of("tpa", "tpask");

    private TPAskCommand() {}

    public static void register(@NotNull CommandDispatcher<CommandSourceStack> dispatcher) {
        for (String alias : aliases)
            dispatcher.register(Commands
                .literal(alias)
                .requires(CommandSourceStack::isPlayer)
                .then(Commands
                    .argument("player", EntityArgument.player())
                    .suggests(TPAskCommand::suggest)
                    .executes(TPAskCommand::execute)
                )
            );
    }

    @NotNull
    private static CompletableFuture<Suggestions> suggest(@NotNull CommandContext<CommandSourceStack> context,
                                                          @NotNull SuggestionsBuilder builder) {
        ServerPlayer player = context.getSource().getPlayer();
        return SharedSuggestionProvider.suggest(
            context.getSource()
                .getServer()
                .getPlayerList()
                .getPlayers()
                .stream()
                .filter(p -> p != player)
                .map(p -> p.getGameProfile().name()),
            builder
        );
    }

    private static int execute(@NotNull CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer sender = context.getSource().getPlayerOrException();
        ServerPlayer target = EntityArgument.getPlayer(context, "player");
        RequestManager.INSTANCE.requestTo(sender, target);
        return 1;
    }
}
