package xyz.oribuin.vouchers.requirement.impl;

import org.bukkit.entity.Player;
import xyz.oribuin.vouchers.requirement.Requirement;

import java.util.Objects;

public class PermissionRequirement extends Requirement {

    public PermissionRequirement(Object input, Object output, boolean inverted) {
        super(input, output, inverted);
    }

    @Override
    public boolean evaluate(Player player) {
        Objects.requireNonNull(this.input, "An input must be provided for the Permission Requirement.");

        String input = this.parse(player, this.input);
        if (input == null) return false;

        return this.inverted != player.hasPermission(input);
    }

}
