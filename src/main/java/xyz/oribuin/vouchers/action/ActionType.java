package xyz.oribuin.vouchers.action;

import dev.rosewood.rosegarden.hook.PlaceholderAPIHook;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.oribuin.vouchers.action.impl.BroadcastAction;
import xyz.oribuin.vouchers.action.impl.CloseAction;
import xyz.oribuin.vouchers.action.impl.ConsoleAction;
import xyz.oribuin.vouchers.action.impl.MessageAction;
import xyz.oribuin.vouchers.action.impl.PlayerAction;
import xyz.oribuin.vouchers.util.VoucherUtils;

import java.util.List;

public enum ActionType {
    MESSAGE(new MessageAction()),
    CONSOLE(new ConsoleAction()),
    PLAYER(new PlayerAction()),
    BROADCAST(new BroadcastAction()),
    CLOSE(new CloseAction());

    private final Action action;

    ActionType(Action action) {
        this.action = action;
    }


    /**
     * Run all the plugin actions through a series of comomands
     *
     * @param player       The player to run the actions for
     * @param commands     The commands to run
     * @param placeholders The placeholders to apply to the commands
     */
    public static void run(Player player, List<String> commands, StringPlaceholders placeholders) {
        for (String command : commands) {
            ActionType type = match(command.trim());
            if (type == null) {
                Bukkit.getLogger().warning("Invalid action type for command: " + command);
                continue;
            }

            String content = PlaceholderAPIHook.applyPlaceholders(player, command.substring(command.indexOf("]") + 1));
            type.get().run(player, placeholders.apply(content));
        }
    }

    /**
     * Run all the plugin actions through a series of comomands
     *
     * @param player   The player to run the actions for
     * @param commands The commands to run
     */
    public static void run(Player player, List<String> commands) {
        run(player, commands, StringPlaceholders.empty());
    }

    /**
     * Find the action type from the content
     *
     * @param content The content to find the action type from
     * @return The action type
     */
    private static ActionType match(String content) {
        try {
            String action = content.trim().substring(1, content.indexOf("]"));
            return VoucherUtils.getEnum(ActionType.class, action, ActionType.CONSOLE);
        } catch (StringIndexOutOfBoundsException ignored) {
            return ActionType.MESSAGE;
        }
    }

    public Action get() {
        return this.action;
    }

}
