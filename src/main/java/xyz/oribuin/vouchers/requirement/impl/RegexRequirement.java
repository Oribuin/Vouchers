package xyz.oribuin.vouchers.requirement.impl;

import org.bukkit.entity.Player;
import xyz.oribuin.vouchers.requirement.Requirement;

import java.util.Objects;
import java.util.regex.Pattern;

public class RegexRequirement extends Requirement {

    public RegexRequirement(Object input, Object output, boolean inverted) {
        super(input, output, inverted);
    }

    @Override
    public boolean evaluate(Player player) {
        Objects.requireNonNull(this.input, "An input must be provided for the Regex Requirement.");
        Objects.requireNonNull(this.output, "An output must be provided for the Regex Requirement.");

        String input = this.parse(player, this.input);
        String output = this.parse(player, this.output);
        if (input == null || output == null) return false;

        return this.inverted != Pattern.compile(output).matcher(input).matches();
    }

}
