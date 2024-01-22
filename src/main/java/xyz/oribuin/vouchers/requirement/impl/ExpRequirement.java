package xyz.oribuin.vouchers.requirement.impl;

import org.bukkit.entity.Player;
import xyz.oribuin.vouchers.requirement.Requirement;

import java.util.Objects;

public class ExpRequirement extends Requirement {

    public ExpRequirement(Object input, Object output, boolean inverted) {
        super(input, output, inverted);
    }

    @Override
    public boolean evaluate(Player player) {
        Objects.requireNonNull(this.input, "An input must be provided for the Experience Requirement.");

        String result = this.parse(player, this.input);
        if (input == null) return false;

        // Check if the requirement is a level requirement.
        boolean levelRequirement = result.endsWith("L");
        if (levelRequirement) {
            result = result.substring(0, result.length() - 1);
        }

        // Check if the requirement is a number.
        int requirement;
        try {
            requirement = Integer.parseInt(result);
        } catch (NumberFormatException ex) {
            return false;
        }

        // Check if the requirement is a level requirement or an exp requirement.
        if (levelRequirement) {
            return this.inverted != (player.getLevel() >= requirement);
        }

        // Check if the player has the required exp.
        return this.inverted != (player.getTotalExperience() >= requirement);
    }


}
