package ru.craftlogic.scripts.common.extension;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

public class TextComponentExtension {
    public static ITextComponent plus(ITextComponent self, ITextComponent other) {
        ITextComponent result = self.createCopy();
        result.appendSibling(other);
        return result;
    }

    public static ITextComponent plus(ITextComponent self, String text) {
        return plus(self, new TextComponentString(text));
    }

    public static ITextComponent plus(String self, ITextComponent other) {
        return plus(new TextComponentString(self), other);
    }

    //style

    public static ITextComponent color(ITextComponent self, TextFormatting color) {
        ITextComponent result = self.createCopy();
        result.getStyle().setColor(color);
        return result;
    }

    public static ITextComponent color(String self, TextFormatting color) {
        return color(new TextComponentString(self), color);
    }

    public static ITextComponent bold(ITextComponent self) {
        ITextComponent result = self.createCopy();
        result.getStyle().setBold(true);
        return result;
    }

    public static ITextComponent bold(String self) {
        return bold(new TextComponentString(self));
    }

    public static ITextComponent italic(ITextComponent self) {
        ITextComponent result = self.createCopy();
        result.getStyle().setItalic(true);
        return result;
    }

    public static ITextComponent italic(String self) {
        return italic(new TextComponentString(self));
    }

    public static ITextComponent underlined(ITextComponent self) {
        ITextComponent result = self.createCopy();
        result.getStyle().setUnderlined(true);
        return result;
    }

    public static ITextComponent underlined(String self) {
        return underlined(new TextComponentString(self));
    }

    public static ITextComponent strikethrough(ITextComponent self) {
        ITextComponent result = self.createCopy();
        result.getStyle().setStrikethrough(true);
        return result;
    }

    public static ITextComponent strikethrough(String self) {
        return strikethrough(new TextComponentString(self));
    }

    public static ITextComponent obfuscated(ITextComponent self) {
        ITextComponent result = self.createCopy();
        result.getStyle().setObfuscated(true);
        return result;
    }

    public static ITextComponent obfuscated(String self) {
        return obfuscated(new TextComponentString(self));
    }

    //Click event

    public static ITextComponent openURL(ITextComponent self, String arg) {
        ITextComponent result = self.createCopy();
        result.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, arg));
        return result;
    }

    public static ITextComponent openURL(String self, String arg) {
        return openURL(new TextComponentString(self), arg);
    }

    public static ITextComponent exec(ITextComponent self, String arg) {
        ITextComponent result = self.createCopy();
        result.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, arg));
        return result;
    }

    public static ITextComponent exec(String self, String arg) {
        return exec(new TextComponentString(self), arg);
    }

    public static ITextComponent suggest(ITextComponent self, String arg) {
        ITextComponent result = self.createCopy();
        result.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, arg));
        return result;
    }

    public static ITextComponent suggest(String self, String arg) {
        return suggest(new TextComponentString(self), arg);
    }

    //Hover events

    public static ITextComponent tooltip(ITextComponent self, TextComponentString arg) {
        ITextComponent result = self.createCopy();
        result.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, arg));
        return result;
    }

    public static ITextComponent tooltip(String self, TextComponentString arg) {
        return tooltip(new TextComponentString(self), arg);
    }

    public static ITextComponent tooltip(ITextComponent self, String arg) {
        return tooltip(self, new TextComponentString(arg));
    }

    public static ITextComponent tooltip(String self, String arg) {
        return tooltip(new TextComponentString(self), arg);
    }

    //direct colors

    public static ITextComponent black(ITextComponent self) {
        return color(self, TextFormatting.BLACK);
    }

    public static ITextComponent black(String self) {
        return black(new TextComponentString(self));
    }

    public static ITextComponent darkBlue(ITextComponent self) {
        return color(self, TextFormatting.DARK_BLUE);
    }

    public static ITextComponent darkBlue(String self) {
        return darkBlue(new TextComponentString(self));
    }

    public static ITextComponent darkGreen(ITextComponent self) {
        return color(self, TextFormatting.DARK_GREEN);
    }

    public static ITextComponent darkGreen(String self) {
        return darkGreen(new TextComponentString(self));
    }

    public static ITextComponent darkAqua(ITextComponent self) {
        return color(self, TextFormatting.DARK_AQUA);
    }

    public static ITextComponent darkAqua(String self) {
        return darkAqua(new TextComponentString(self));
    }

    public static ITextComponent darkRed(ITextComponent self) {
        return color(self, TextFormatting.DARK_RED);
    }

    public static ITextComponent darkRed(String self) {
        return darkRed(new TextComponentString(self));
    }

    public static ITextComponent darkPurple(ITextComponent self) {
        return color(self, TextFormatting.DARK_PURPLE);
    }

    public static ITextComponent darkPurple(String self) {
        return darkPurple(new TextComponentString(self));
    }

    public static ITextComponent gold(ITextComponent self) {
        return color(self, TextFormatting.GOLD);
    }

    public static ITextComponent gold(String self) {
        return gold(new TextComponentString(self));
    }

    public static ITextComponent gray(ITextComponent self) {
        return color(self, TextFormatting.GRAY);
    }

    public static ITextComponent gray(String self) {
        return gray(new TextComponentString(self));
    }

    public static ITextComponent darkGray(ITextComponent self) {
        return color(self, TextFormatting.DARK_GRAY);
    }

    public static ITextComponent darkGray(String self) {
        return darkGray(new TextComponentString(self));
    }

    public static ITextComponent blue(ITextComponent self) {
        return color(self, TextFormatting.BLUE);
    }

    public static ITextComponent blue(String self) {
        return blue(new TextComponentString(self));
    }

    public static ITextComponent green(ITextComponent self) {
        return color(self, TextFormatting.GREEN);
    }

    public static ITextComponent green(String self) {
        return green(new TextComponentString(self));
    }

    public static ITextComponent aqua(ITextComponent self) {
        return color(self, TextFormatting.AQUA);
    }

    public static ITextComponent aqua(String self) {
        return aqua(new TextComponentString(self));
    }

    public static ITextComponent red(ITextComponent self) {
        return color(self, TextFormatting.RED);
    }

    public static ITextComponent red(String self) {
        return red(new TextComponentString(self));
    }

    public static ITextComponent lightPurple(ITextComponent self) {
        return color(self, TextFormatting.LIGHT_PURPLE);
    }

    public static ITextComponent lightPurple(String self) {
        return lightPurple(new TextComponentString(self));
    }

    public static ITextComponent yellow(ITextComponent self) {
        return color(self, TextFormatting.YELLOW);
    }

    public static ITextComponent yellow(String self) {
        return yellow(new TextComponentString(self));
    }

    public static ITextComponent white(ITextComponent self) {
        return color(self, TextFormatting.WHITE);
    }

    public static ITextComponent white(String self) {
        return white(new TextComponentString(self));
    }
}
