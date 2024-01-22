package xyz.oribuin.vouchers.requirement.impl;

import org.bukkit.entity.Player;
import xyz.oribuin.vouchers.hook.economy.PointsProvider;
import xyz.oribuin.vouchers.hook.economy.VaultProvider;
import xyz.oribuin.vouchers.requirement.Requirement;

import java.util.Objects;

public class PointsRequirement extends Requirement {

    public PointsRequirement(Object input, Object output, boolean inverted) {
        super(input, output, inverted);
    }

    @Override
    public boolean evaluate(Player player) {
        Objects.requireNonNull(this.input, "An input must be provided for the Money Requirement.");

        try {
            int input = Integer.parseInt(this.parse(player, this.input));
            return this.inverted != PointsProvider.get().has(player, input);
        } catch (NumberFormatException ex) {
            return false;
        }

    }

}
