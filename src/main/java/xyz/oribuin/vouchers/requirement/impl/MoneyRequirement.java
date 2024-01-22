package xyz.oribuin.vouchers.requirement.impl;

import org.bukkit.entity.Player;
import xyz.oribuin.vouchers.hook.economy.VaultProvider;
import xyz.oribuin.vouchers.requirement.Requirement;

import java.util.Objects;

public class MoneyRequirement extends Requirement {

    public MoneyRequirement(Object input, Object output, boolean inverted) {
        super(input, output, inverted);
    }

    @Override
    public boolean evaluate(Player player) {
        Objects.requireNonNull(this.input, "An input must be provided for the Money Requirement.");

        try {
            double input = Double.parseDouble(this.parse(player, this.input));
            return this.inverted != VaultProvider.get().has(player, input);
        } catch (NumberFormatException ex) {
            return false;
        }

    }

}
