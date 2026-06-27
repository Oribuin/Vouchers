package xyz.oribuin.vouchers.action;

import dev.rosewood.rosegarden.hook.PlaceholderAPIHook;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import org.bukkit.entity.Player;
import xyz.oribuin.vouchers.VoucherPlugin;
import xyz.oribuin.vouchers.action.impl.BroadcastAction;
import xyz.oribuin.vouchers.action.impl.CloseAction;
import xyz.oribuin.vouchers.action.impl.ConsoleAction;
import xyz.oribuin.vouchers.action.impl.MessageAction;
import xyz.oribuin.vouchers.action.impl.PlayerAction;
import xyz.oribuin.vouchers.util.VoucherUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private static final Pattern ACTION_PATTERN = Pattern.compile("(\\[.*?]\\s)*");

    /**
     * Run all the plugin actions through a series of comomands
     *
     * @param player       The player to run the actions for
     * @param commands     The commands to run
     * @param placeholders The placeholders to apply to the commands
     */
    public static void run(Player player, List<String> commands, StringPlaceholders placeholders) {
        for (String command : commands) {
            Matcher matcher = ACTION_PATTERN.matcher(command);
            if (!matcher.find()) {
                VoucherPlugin.get().getLogger().warning("Could not find valid action type format: " + command);
                continue;
            }

            ActionType type = match(matcher.group());
            if (type == null) {
                VoucherPlugin.get().getLogger().warning("Invalid action type for command: " + command);
                continue;
            }

            // Remove additional space after [text
            String content = PlaceholderAPIHook.applyPlaceholders(player, command.replace(matcher.group(), ""));
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
