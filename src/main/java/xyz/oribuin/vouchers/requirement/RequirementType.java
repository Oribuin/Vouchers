package xyz.oribuin.vouchers.requirement;

import xyz.oribuin.vouchers.requirement.impl.EqualsRequirement;
import xyz.oribuin.vouchers.requirement.impl.ExpRequirement;
import xyz.oribuin.vouchers.requirement.impl.MathRequirement;
import xyz.oribuin.vouchers.requirement.impl.PermissionRequirement;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public enum RequirementType {
    PERMISSION("permission", PermissionRequirement.class),
    EXP("exp", ExpRequirement.class),
    MATH("math", MathRequirement.class),
    EQUALS("equals", EqualsRequirement.class)
    ;
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
            throw new RuntimeException(e);
        }
    }

    public String getName() {
        return name;
    }

    public Class<? extends Requirement> getClazz() {
        return clazz;
    }


}
