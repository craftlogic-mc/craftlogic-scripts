package ru.craftlogic.scripts.common;

import groovy.lang.Script;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.DimensionType;
import ru.craftlogic.api.text.Text;
import ru.craftlogic.api.world.Dimension;

public abstract class ScriptBase<C extends ScriptContainer> extends Script {
    protected C container;
    protected static final ItemsMap items = new ItemsMap();
    protected static final BlocksMap blocks = new BlocksMap();
    protected static final SoundsMap sounds = new SoundsMap();
    protected static final DimensionsMap dimensions = new DimensionsMap();

    void setContainer(C container) {
        this.container = container;
    }

    protected abstract void showChatMessage(ITextComponent message);

    protected ITextComponent getPrefix() {
        ITextComponent prefix = new TextComponentString("[>] ");
        prefix.getStyle().setColor(TextFormatting.RED);
        return prefix;
    }

    public void print(Text<?, ?> value) {
        print(value.build());
    }

    public void print(ITextComponent value) {
        ITextComponent message = new TextComponentString("");
        message.appendSibling(getPrefix());
        message.appendSibling(value);
        showChatMessage(message);
    }

    @Override
    public void print(Object value) {
        print(new TextComponentString(String.valueOf(value)));
    }

    @Override
    public void println() {
        print("\n");
    }

    @Override
    public void println(Object value) {
        print(value);
    }

    @Override
    public void printf(String format, Object value) {
        print(String.format(format, value));
    }

    @Override
    public void printf(String format, Object[] values) {
        print(String.format(format, values));
    }

    @Deprecated
    protected ItemsMap getItem() {
        return items;
    }

    @Deprecated
    protected BlocksMap getBlock() {
        return blocks;
    }

    @Deprecated
    protected SoundsMap getSound() {
        return sounds;
    }

    public static class ItemsMap {
        public Item getAt(String id) {
            return Item.REGISTRY.getObject(new ResourceLocation(id));
        }

        public ItemStack getAt(Block block, int amount) {
            return new ItemStack(block, amount);
        }

        public ItemStack getAt(Item item, int amount) {
            return new ItemStack(item, amount);
        }

        public ItemStack getAt(String id, int amount) {
            return new ItemStack(getAt(id), amount);
        }

        public ItemStack getAt(Block block, int amount, int metadata) {
            return new ItemStack(block, amount, metadata);
        }

        public ItemStack getAt(Item item, int amount, int metadata) {
            return new ItemStack(item, amount, metadata);
        }

        public ItemStack getAt(String id, int amount, int metadata) {
            return new ItemStack(getAt(id), amount, metadata);
        }
    }

    public static class BlocksMap {
        public Block getAt(String id) {
            return Block.REGISTRY.getObject(new ResourceLocation(id));
        }

        public IBlockState getAt(String id, int meta) {
            return Block.REGISTRY.getObject(new ResourceLocation(id)).getStateFromMeta(meta);
        }
    }

    public static class SoundsMap {
        public SoundEvent getAt(String id) {
            return SoundEvent.REGISTRY.getObject(new ResourceLocation(id));
        }
    }

    public static class DimensionsMap {
        public Dimension getAt(String id) {
            for (DimensionType d : DimensionType.values()) {
                if (d.getName().equals(id)) {
                    return Dimension.fromVanilla(d);
                }
            }
            return null;
        }
    }
}
