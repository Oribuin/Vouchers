package xyz.oribuin.vouchers.requirement.impl;

import org.bukkit.entity.Player;
import xyz.oribuin.vouchers.requirement.Requirement;

public class ExpRequirement extends Requirement {

    protected ExpRequirement(Object input, boolean inverted) {
        super(input, inverted);
    }

    @Override
    public boolean evaluate(Player player) {
        if (!(this.input instanceof String result)) return false;

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
