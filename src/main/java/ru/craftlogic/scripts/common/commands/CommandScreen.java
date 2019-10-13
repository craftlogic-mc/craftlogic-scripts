package ru.craftlogic.scripts.common.commands;

import groovy.lang.Binding;
import groovy.lang.GroovyRuntimeException;
import groovy.lang.GroovyShell;
import net.minecraft.command.CommandException;
import ru.craftlogic.api.command.CommandBase;
import ru.craftlogic.api.command.CommandContext;
import ru.craftlogic.api.world.Player;
import ru.craftlogic.scripts.ScriptManager;
import ru.craftlogic.scripts.common.ScriptShell;

import java.util.Collections;

public class CommandScreen extends CommandBase {
    public CommandScreen() {
        super("screen", 4,
            "<id> <player:Player> <args>...",
            "<id> <player:Player>",
            "<id>"
        );
    }

    @Override
    protected void execute(CommandContext ctx) throws CommandException {
        ScriptManager scriptManager = ctx.server().getManager(ScriptManager.class);
        if (!scriptManager.isEnabled()) {
            throw new CommandException("commands.script.disabled");
        }
        GroovyShell shell = scriptManager.getShell();
        ScriptShell script;
        try {
            script = (ScriptShell) shell.parse(ctx.get("value").asString(), "@Shell");
        } catch (GroovyRuntimeException exc) {
            String msg = exc.getMessageWithoutLocationText();
            if (msg != null) {
                ctx.sendMessage(msg);
            } else {
                throw exc;
            }
            return;
        }
        Binding binding = script.getBinding();
        binding.setProperty("server", ctx.server());
        binding.setProperty("me", ctx.sender());
        for (Player player : ctx.server().getPlayerManager().getAllOnline()) {
            binding.setProperty("$" + player.getProfile().getName(), player);
        }
        script.setBinding(binding);
        try {
            Object result = script.run();
            ctx.sendMessage("Returned: %s", result);
        } catch (GroovyRuntimeException exc) {
            ctx.sendMessage(exc.getMessage());
        }
    }
}
