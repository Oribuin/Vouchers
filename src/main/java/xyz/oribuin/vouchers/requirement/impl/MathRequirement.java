package xyz.oribuin.vouchers.requirement.impl;

import org.bukkit.entity.Player;
import xyz.oribuin.vouchers.requirement.Requirement;

import java.util.Objects;

public class MathRequirement extends Requirement {

    public MathRequirement(Object input, boolean inverted) {
        super(input, inverted);
    }

    @Override
    public boolean evaluate(Player player) {
        Objects.requireNonNull(this.input, "An input must be provided for the Math Requirement.");

        String input = this.parse(player, this.input);
        if (input == null) return false;

        String[] split = input.split(" ");
        if (split.length != 3) return false;

        int num1;
        int num2;

        try {
            num1 = Integer.parseInt(split[0]);
            num2 = Integer.parseInt(split[2]);
        } catch (NumberFormatException ex) {
            return false;
        }

        // This makes up my dosage of sertraline.
        return switch (split[1]) {
            case ">" -> num1 > num2;
            case "<" -> num1 < num2;
            case ">=" -> num1 >= num2;
            case "<=" -> num1 <= num2;
            case "==" -> num1 == num2;
            case "!=" -> num1 != num2;
            case "pow" -> num1 == Math.pow(num1, num2);
            case "sqrt" -> num1 == Math.sqrt(num2);
            default -> throw new IllegalStateException("Unexpected value: " + split[1]);
        };
    }

}
