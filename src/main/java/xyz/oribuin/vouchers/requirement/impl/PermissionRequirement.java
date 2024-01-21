package xyz.oribuin.vouchers.requirement.impl;

import org.bukkit.entity.Player;
import xyz.oribuin.vouchers.requirement.Requirement;

public class PermissionRequirement extends Requirement {

    protected PermissionRequirement(Object input, boolean inverted) {
        super(input, inverted);
    }

    @Override
    public boolean evaluate(Player player) {
        if (!(this.input instanceof String result)) return false;

        return this.inverted != player.hasPermission(result);
    }

}
