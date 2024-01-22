package xyz.oribuin.vouchers.requirement;

import dev.rosewood.rosegarden.hook.PlaceholderAPIHook;
import org.bukkit.entity.Player;

import java.util.Objects;

public abstract class Requirement {

    protected Object input;
    protected Object output;
    protected boolean inverted;

    /**
     * Create a new requirement object.
     *
     * @param output   The output of the requirement.
     * @param inverted If the requirement should be inverted.
     */
    public Requirement(Object input, Object output, boolean inverted) {
        this.input = input;
        this.output = output;
        this.inverted = inverted;
    }

    /**
     * @return The description of the requirement.
     */
    public abstract boolean evaluate(Player player);

    /**
     * Parse the content through placeholder api if possible
     *
     * @param player The player to parse the input for.
     * @return The parsed input.
     */
    public final String parse(Player player, Object content) {
        if (content == null) return null;

        return PlaceholderAPIHook.applyPlaceholders(player, content instanceof String
                ? (String) content
                : String.valueOf(content)
        );
    }

}
