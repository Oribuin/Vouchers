package xyz.oribuin.vouchers.requirement.impl;

import org.bukkit.entity.Player;
import xyz.oribuin.vouchers.requirement.Requirement;

import java.util.Objects;

public class ContainsRequirement extends Requirement {

    public ContainsRequirement(Object input, Object output, boolean inverted) {
        super(input, output, inverted);
    }

    @Override
    public boolean evaluate(Player player) {
        Objects.requireNonNull(this.input, "An input must be provided for the Contains Requirement.");
        Objects.requireNonNull(this.output, "An output must be provided for the Contains Requirement.");

        String input = this.parse(player, this.input);
        String output = this.parse(player, this.output);

        if (input == null || output == null) return false;

        return this.inverted != input.contains(output);
    }

}
