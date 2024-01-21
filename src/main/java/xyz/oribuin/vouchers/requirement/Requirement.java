package xyz.oribuin.vouchers.requirement;

import dev.rosewood.rosegarden.hook.PlaceholderAPIHook;
import org.bukkit.entity.Player;

public abstract class Requirement {

    protected Object input;
    protected boolean inverted;

    /**
     * Create a new requirement object.
     *
     * @param inverted If the requirement should be inverted.
     */
    public Requirement(Object input, boolean inverted) {
        this.input = input;
        this.inverted = inverted;
    }

    /**
     * @return The description of the requirement.
     */
    public abstract boolean evaluate(Player player);

    /**
     * Parse the input through placeholder api.
     *
     * @param player The player to parse the input for.
     * @return The parsed input.
     */
    public final String inputAsPlaceholder(Player player) {
        if (!(this.input instanceof String result)) return null;

        return PlaceholderAPIHook.applyPlaceholders(player, result);
    }

}
