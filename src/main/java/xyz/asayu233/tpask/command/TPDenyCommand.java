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
import xyz.asayu233.tpask.manager.RequestManager;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class TPDenyCommand {
    private static final List<String> aliases = List.of("tpn", "tpdeny");

    private TPDenyCommand() {}

    public static void register(@NotNull CommandDispatcher<CommandSourceStack> dispatcher) {
        for (String alias : aliases)
            dispatcher.register(Commands
                .literal(alias)
                .requires(CommandSourceStack::isPlayer)
                .then(Commands
                    .argument("player", EntityArgument.player())
                    .suggests(TPDenyCommand::suggests)
                    .executes(TPDenyCommand::execute)
                )
            );
    }

    @NotNull
    private static CompletableFuture<Suggestions> suggests(@NotNull CommandContext<CommandSourceStack> context,
                                                           @NotNull SuggestionsBuilder builder) {
        ServerPlayer player = context.getSource().getPlayer();
        if (player != null)
            return SharedSuggestionProvider.suggest(
                RequestManager.INSTANCE
                    .getPendingRequests(player)
                    .stream()
                    .map(GameProfile::name),
                builder
            );
        return Suggestions.empty();
    }

    private static int execute(@NotNull CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer sender = context.getSource().getPlayerOrException();
        ServerPlayer target = EntityArgument.getPlayer(context, "player");
        RequestManager.INSTANCE.denyRequest(sender, target);
        return 1;
    }
}
