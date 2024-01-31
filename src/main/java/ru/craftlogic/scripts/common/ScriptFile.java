package ru.craftlogic.scripts.common;

import groovy.lang.Closure;
import net.minecraft.command.ICommand;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.DimensionType;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import ru.craftlogic.api.command.CommandBase.Syntax;
import ru.craftlogic.api.server.Server;
import ru.craftlogic.api.world.Dimension;
import ru.craftlogic.api.world.World;
import ru.craftlogic.scripts.Events;

import java.util.*;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public abstract class ScriptFile extends ScriptBase<ScriptContainerFile> {
    Server server;
    protected final WorldsMap worlds = new WorldsMap();

    @Override
    protected void showChatMessage(ITextComponent message) {
        server.sendMessage(message);
    }

    @Override
    protected ITextComponent getPrefix() {
        return this.container.getPrefix();
    }

    protected void loaded(Closure<Void> handler) {
        this.container.loadingHandler = handler;
    }

    protected void unloaded(Closure<Void> handler) {
        this.container.unloadingHandler = handler;
    }

    protected void when(String event, Closure<Void> handler) {
        Class<? extends Event> type = Objects.requireNonNull(Events.get(event), "Unknown event type: " + event);
        when(type, handler);
    }

    protected void when(Class<? extends Event> event, Closure<Void> handler) {
        when(event, EventPriority.NORMAL, handler);
    }

    protected void when(String event, EventPriority priority, Closure<Void> handler) {
        Class<? extends Event> type = Objects.requireNonNull(Events.get(event), "Unknown event type: " + event);
        when(type, priority, handler);
    }

    protected void when(Class<? extends Event> event, EventPriority priority, Closure<Void> handler) {
        container.when(event, priority, handler);
    }

    protected void command(String name, Closure<Void> handler) {
        command(new LinkedHashMap<>(), name, handler);
    }

    protected void command(Map<String, Object> data, String name, Closure<Void> handler) {
        int opLevel = data.containsKey("opLevel") ? (int)data.get("opLevel") : 4;
        List<String> aliases = parseList(data, "aliases", emptyList());
        List<Syntax> defSyntax = singletonList(new Syntax("", "commands." + name));
        List<Syntax> syntax = parseSyntax(data, name, "syntax", defSyntax);
        if (syntax.isEmpty()) {
            syntax.addAll(defSyntax);
        }
        ICommand cmd = this.container.registerCommand(name, syntax, aliases, opLevel,
            ctx -> {
                handler.setDelegate(ctx);
                handler.call(ctx);
                handler.setDelegate(null);
            }
        );
        this.container.commands.add(cmd);
    }

    protected void payload(String channel, Closure<NBTTagCompound> callback) {
        container.payloadHandler.put(channel, callback);
    }

    protected UUID nextTick(Closure<Void> task) {
        return task(task);
    }

    protected UUID task(Closure<Void> task) {
        return server.addTask(server -> task.run());
    }

    protected UUID delayedTask(long delay, Closure<Void> task) {
        return server.addDelayedTask(server -> task.run(), delay);
    }

    private List<String> parseList(Map<String, Object> map, String key, List<String> def) {
        return map.containsKey(key) ? parseList(map.get(key)) : def;
    }

    private List<Syntax> parseSyntax(Map<String, Object> map, String name, String key, List<Syntax> def) {
        if (map.containsKey(key)) {
            Object v = map.get(key);
            if (v instanceof String) {
                return singletonList(new Syntax((String) v, "commands." + name));
            } else if (v instanceof Syntax) {
                return singletonList((Syntax) v);
            } else if (v instanceof Collection) {
                Collection syntax = (Collection) v;
                List<Syntax> result = new ArrayList<>();
                for (Object s : syntax) {
                    if (s instanceof String) {
                        result.add(new Syntax((String) s, "commands." + name));
                    } else if (s instanceof Syntax) {
                        result.add((Syntax) s);
                    } else {
                        throw new IllegalArgumentException("Invalid command syntax object for command `" + name + "`");
                    }
                }
                return result;
            } else {
                throw new IllegalArgumentException("Invalid command syntax object for command `" + name + "`");
            }
        } else {
            return def;
        }
    }

    private List<String> parseList(Object obj) {
        if (obj instanceof String) {
            return singletonList((String)obj);
        } else {
            return (List<String>)obj;
        }
    }

    private class WorldsMap {
        public World getAt(String id) {
            Dimension dim = dimensions.getAt(id);
            return dim != null ? server.getWorldManager().get(dim) : null;
        }
    }
}
