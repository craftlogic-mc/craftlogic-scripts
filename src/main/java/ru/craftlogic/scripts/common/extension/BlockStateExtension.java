package ru.craftlogic.scripts.common.extension;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;

public class BlockStateExtension {
    private static IProperty<?> findProperty(IBlockState state, String property) {
        for (IProperty<?> prop : state.getPropertyKeys()) {
            if (prop.getName().equals(property)) {
                return prop;
            }
        }
        throw new IllegalArgumentException("No such property: '" + property + "' for state " + state);
    }

    public static IBlockState cycleProperty(IBlockState state, String property) {
        return state.cycleProperty(findProperty(state, property));
    }

    public static IBlockState withProperty(IBlockState state, String property, Comparable value) {
        IProperty prop = findProperty(state, property);
        Comparable val = value instanceof String ? (Comparable) prop.parseValue((String) value).get() : value;
        return state.withProperty(prop, val);
    }

    public static IBlockState set(IBlockState state, String property, Comparable value) {
        return withProperty(state, property, value);
    }

    public static Comparable getValue(IBlockState state, String property) {
        return state.getValue(findProperty(state, property));
    }

    public static Comparable get(IBlockState state, String property) {
        return getValue(state, property);
    }
}
