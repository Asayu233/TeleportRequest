package xyz.asayu233.tpask.command;

import com.mojang.authlib.GameProfile;
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
import xyz.asayu233.tpask.manager.ConfigManager;
import xyz.asayu233.tpask.manager.RequestManager;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class TPAcceptCommand {
    private static final List<String> aliases = List.of("tpy", "tpaccept");

    private TPAcceptCommand() {}

    public static void register(@NotNull CommandDispatcher<CommandSourceStack> dispatcher) {
        if (ConfigManager.INSTANCE.getConfig().isRequestEnabled()) {
            for (String alias : aliases)
                dispatcher.register(Commands
                    .literal(alias)
                    .requires(CommandSourceStack::isPlayer)
                    .then(Commands
                        .argument("player", EntityArgument.player())
                        .suggests(TPAcceptCommand::suggest)
                        .executes(TPAcceptCommand::execute)
                    )
                );
        }
    }

    @NotNull
    private static CompletableFuture<Suggestions> suggest(@NotNull CommandContext<CommandSourceStack> context,
                                                          @NotNull SuggestionsBuilder builder) {
        ServerPlayer player = context.getSource().getPlayer();
        return player != null ? SharedSuggestionProvider.suggest(
            RequestManager.INSTANCE
                .getPendingRequests(player)
                .stream()
                .map(GameProfile::name),
            builder
        ) : Suggestions.empty();
    }

    private static int execute(@NotNull CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer sender = context.getSource().getPlayerOrException();
        ServerPlayer target = EntityArgument.getPlayer(context, "player");
        RequestManager.INSTANCE.acceptRequest(sender, target);
        return 1;
    }
}
