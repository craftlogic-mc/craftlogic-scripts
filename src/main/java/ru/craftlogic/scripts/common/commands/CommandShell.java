package ru.craftlogic.scripts.common.commands;

import groovy.lang.Binding;
import groovy.lang.GroovyRuntimeException;
import groovy.lang.GroovyShell;
import net.minecraft.command.CommandException;
import ru.craftlogic.api.command.CommandBase;
import ru.craftlogic.api.command.CommandContext;
import ru.craftlogic.api.text.Text;
import ru.craftlogic.api.text.TextString;
import ru.craftlogic.api.text.TextTranslation;
import ru.craftlogic.api.world.Player;
import ru.craftlogic.scripts.CraftScripts;
import ru.craftlogic.scripts.ScriptManager;
import ru.craftlogic.scripts.common.ScriptContainer;
import ru.craftlogic.scripts.common.ScriptContainerFile;
import ru.craftlogic.scripts.common.ScriptShell;

import java.util.Collection;
import java.util.Collections;

public class CommandShell extends CommandBase {
    public CommandShell() {
        super("shell>", 4, "<value>...");
        Collections.addAll(aliases, "s>", ">");
    }

    @Override
    protected void execute(CommandContext ctx) throws CommandException {
        String id = ctx.get("id").asString();
        String args = ctx.getIfPresent("args", CommandContext.Argument::asString).orElse("");
        Player player = ctx.has("player") ? ctx.get("player").asPlayer() : ctx.senderAsPlayer();
        CraftScripts.showScreen(id, player.getEntity(), args);
    }
}
