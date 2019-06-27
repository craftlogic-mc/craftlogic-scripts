package ru.craftlogic.scripts;

import groovy.lang.Binding;
import groovy.lang.GroovyRuntimeException;
import groovy.lang.GroovyShell;
import net.minecraft.command.CommandException;
import ru.craftlogic.api.command.*;
import ru.craftlogic.api.command.CommandContext.Argument;
import ru.craftlogic.api.text.Text;
import ru.craftlogic.api.text.TextString;
import ru.craftlogic.api.text.TextTranslation;
import ru.craftlogic.api.world.Player;
import ru.craftlogic.scripts.common.ScriptContainer;
import ru.craftlogic.scripts.common.ScriptContainerFile;
import ru.craftlogic.scripts.common.ScriptShell;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ScriptCommands implements CommandRegistrar {
    @Command(
        name = "script",
        syntax = {
            "list",
            "load <id>...",
            "unload <id:Script>..."
        }
    )
    public static void commandScript(CommandContext ctx) throws Exception {
        ScriptManager scriptManager = ctx.server().getManager(ScriptManager.class);
        if (!scriptManager.isEnabled()) {
            throw new CommandException("commands.script.disabled");
        }
        switch (ctx.action(0)) {
            case "list": {
                Collection<ScriptContainerFile> loadedScripts = scriptManager.getAllLoadedScripts();
                if (loadedScripts.isEmpty()) {
                    ctx.sendMessage(Text.translation("commands.script.list.empty").gray());
                } else {
                    TextTranslation msg = Text.translation("commands.script.list").yellow();
                    boolean first = true;
                    TextString entries = Text.string();
                    for (ScriptContainerFile script : loadedScripts) {
                        if (first) first = false;
                        else entries.appendText(", ", Text::yellow);
                        String id = script.getId();
                        entries.appendText(script.getName(), arg ->
                            arg.gold().hoverTextTranslate("commands.script.entry.click", a -> a.arg(id + ".gs"))
                                      .runCommand("/script unload " + id)
                        );
                    }
                    ctx.sendMessage(msg.arg(entries));
                }
                break;
            }
            case "load": {
                String id = ctx.get("id").asString();
                boolean reload = scriptManager.isLoaded(id);
                try {
                    ScriptContainer container = scriptManager.loadScript(id, reload, true);
                    if (container != null) {
                        ctx.sendMessage(
                            Text.translation("commands.script.load.success")
                                .arg(id, Text::darkGreen)
                                .green()
                        );
                    } else {
                        ctx.sendMessage(Text.translation("commands.script.unload.failed.generic").red());
                    }
                } catch (Exception e) {
                    ctx.sendMessage(
                        Text.translation("commands.script.unload.failed")
                            .arg(id)
                            .arg(e.getMessage())
                            .red()
                    );
                    throw e;
                }
                break;
            }
            case "unload": {
                String id = ctx.get("id").asString();
                try {
                    if (scriptManager.unloadScript(id)) {
                        ctx.sendMessage(
                            Text.translation("commands.script.unload.success")
                                .green()
                                .arg(id, arg -> arg.darkGreen().suggestCommand("/script load " + id))
                        );
                    } else {
                        ctx.sendMessage("commands.script.unload.fail.generic");
                    }
                } catch (Exception e) {
                    ctx.sendMessage(
                        Text.translation("commands.script.unload.fail")
                            .red()
                            .arg(id, Text::darkRed)
                            .arg(e.getMessage(), null)
                    );
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    @Command(
        name = "shell>",
        aliases = {"s>", ">"},
        syntax = "<value>..."
    )
    public static void commandShell(CommandContext ctx) throws CommandException {
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

    @Command(
        name = "screen",
        syntax = {
            "<id> <player:Player> <args>...",
            "<id> <player:Player>",
            "<id>"
        }
    )
    public static void commandScreen(CommandContext ctx) throws CommandException {
        String id = ctx.get("id").asString();
        String args = ctx.getIfPresent("args", Argument::asString).orElse("");
        Player player = ctx.has("player") ? ctx.get("player").asPlayer() : ctx.senderAsPlayer();
        CraftScripts.showScreen(id, player.getEntity(), args);
    }

    @ArgumentCompleter(type = "Script")
    public static List<String> completerScriptId(ArgumentCompletionContext ctx) {
        return ctx.server().getManager(ScriptManager.class).getAllLoadedScripts()
                .stream()
                .map(ScriptContainer::getName)
                .collect(Collectors.toList());
    }
}
