package ru.craftlogic.scripts.common.extension;

public class EnumExtension {
    public static <E extends Enum<E>> boolean equals(E self, String other) {
        return self.name().equalsIgnoreCase(other);
    }
}
