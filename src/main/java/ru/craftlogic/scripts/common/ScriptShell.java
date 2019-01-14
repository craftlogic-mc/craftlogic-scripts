package ru.craftlogic.scripts.common;

import net.minecraft.util.text.ITextComponent;
import ru.craftlogic.api.world.CommandSender;

public abstract class ScriptShell extends ScriptBase<ScriptContainerFile> {
    private CommandSender getSender() {
        return ((CommandSender) getBinding().getVariable("me"));
    }

    @Override
    public void showChatMessage(ITextComponent message) {
        getSender().sendMessage(message);
    }

    @Override
    public String toString() {
        return getClass().getName();
    }
}
