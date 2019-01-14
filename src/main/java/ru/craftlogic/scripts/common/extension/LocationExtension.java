package ru.craftlogic.scripts.common.extension;

import ru.craftlogic.api.world.Location;

public class LocationExtension {
    public static void cycleBlockProperty(Location location, String property) {
        location.setBlockState(BlockStateExtension.cycleProperty(location.getBlockState(), property));
    }

    public static void setBlockProperty(Location location, String property, Comparable value) {
        location.setBlockState(BlockStateExtension.withProperty(location.getBlockState(), property, value));
    }

    public static Comparable getBlockProperty(Location location, String property) {
        return BlockStateExtension.getValue(location.getBlockState(), property);
    }
}
