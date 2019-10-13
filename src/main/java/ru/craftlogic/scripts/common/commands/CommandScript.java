package ru.craftlogic.scripts.common.commands;

import net.minecraft.command.CommandException;
import ru.craftlogic.api.command.CommandBase;
import ru.craftlogic.api.command.CommandContext;
import ru.craftlogic.api.text.Text;
import ru.craftlogic.api.text.TextString;
import ru.craftlogic.api.text.TextTranslation;
import ru.craftlogic.scripts.ScriptManager;
import ru.craftlogic.scripts.common.ScriptContainer;
import ru.craftlogic.scripts.common.ScriptContainerFile;

import java.util.Collection;
import java.util.List;

public class CommandScript extends CommandBase {
    public CommandScript() {
        super("script", 4,
            "list",
            "load <id>...",
            "unload <id:Script>..."
        );
    }

    @Override
    protected void execute(CommandContext ctx) throws Throwable {
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
}
