package xyz.oribuin.vouchers.requirement;

import xyz.oribuin.vouchers.VoucherPlugin;
import xyz.oribuin.vouchers.requirement.impl.ContainsRequirement;
import xyz.oribuin.vouchers.requirement.impl.EqualsRequirement;
import xyz.oribuin.vouchers.requirement.impl.ExpRequirement;
import xyz.oribuin.vouchers.requirement.impl.MathRequirement;
import xyz.oribuin.vouchers.requirement.impl.MoneyRequirement;
import xyz.oribuin.vouchers.requirement.impl.PermissionRequirement;
import xyz.oribuin.vouchers.requirement.impl.PointsRequirement;
import xyz.oribuin.vouchers.requirement.impl.RegexRequirement;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public enum RequirementType {
    CONTAINS("contains", ContainsRequirement.class),
    EQUALS("equals", EqualsRequirement.class),
    EXP("exp", ExpRequirement.class),
    MATH("math", MathRequirement.class),
    HAS_MONEY("has money", MoneyRequirement.class),
    PERMISSION("permission", PermissionRequirement.class),
    HAS_POINTS("has points", PointsRequirement.class),
    REGEX("regex", RegexRequirement.class);

    private final String name;
    private final Class<? extends Requirement> clazz;

    RequirementType(String name, Class<? extends Requirement> clazz) {
        this.name = name;
        this.clazz = clazz;
    }

    /**
     * Create a new requirement from a string. Example: "has permission" "has exp"
     *
     * @param name  The requirement identifier.
     * @param input The input for the requirement.
     * @return The requirement object.
     */
    public static Requirement create(String name, Object input, Object output) {
        boolean inverted = name.startsWith("!");

        if (inverted) {
            name = name.substring(1);
        }

        String finalName = name;
        RequirementType type = Arrays.stream(values())
                .filter(t -> t.getName().equalsIgnoreCase(finalName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid requirement type: " + finalName));

        try {
            return type.clazz.getConstructor(Object.class, Object.class, boolean.class).newInstance(input, output, inverted);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            VoucherPlugin.get().getLogger().severe("Failed to create requirement type: " + type.name() + " [" + e.getCause() + "]");
            return null;
        }
    }

    public String getName() {
        return name;
    }

    public Class<? extends Requirement> getClazz() {
        return clazz;
    }


}
