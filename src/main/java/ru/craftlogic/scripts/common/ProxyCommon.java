package ru.craftlogic.scripts.common;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import ru.craftlogic.api.event.server.ServerAddManagersEvent;
import ru.craftlogic.api.network.AdvancedMessage;
import ru.craftlogic.api.network.AdvancedMessageHandler;
import ru.craftlogic.api.server.Server;
import ru.craftlogic.scripts.ScriptManager;
import ru.craftlogic.scripts.network.message.MessageCustom;
import ru.craftlogic.scripts.network.message.MessageShowScriptScreen;
import ru.craftlogic.util.ReflectiveUsage;

import static ru.craftlogic.scripts.CraftScripts.NETWORK;

@ReflectiveUsage
public class ProxyCommon extends AdvancedMessageHandler {
    public void preInit() {

    }

    public void init() {
        NETWORK.registerMessage(this::handleShowScriptScreen, MessageShowScriptScreen.class, Side.CLIENT);
        NETWORK.registerMessage(this::handleClientCustom, MessageCustom.class, Side.CLIENT);
        NETWORK.registerMessage(this::handleServerCustom, MessageCustom.class, Side.SERVER);
    }

    public void postInit() {

    }

    protected AdvancedMessage handleClientCustom(MessageCustom message, MessageContext context) {
        return null;
    }

    protected AdvancedMessage handleServerCustom(MessageCustom message, MessageContext context) {
        Server server = Server.from(getServer(context));
        ScriptManager scriptManager = server.getManager(ScriptManager.class);
        if (scriptManager.isEnabled()) {
            String channel = message.getChannel();
            for (ScriptContainerFile script : scriptManager.getAllLoadedScripts()) {
                NBTTagCompound response = script.handlePayload(channel, message.getData());
                if (response != null) {
                    return new MessageCustom(channel, response);
                }
            }
        }
        return null;
    }

    protected AdvancedMessage handleShowScriptScreen(MessageShowScriptScreen message, MessageContext context) {
        return null;
    }

    @SubscribeEvent
    public void onServerAddManagers(ServerAddManagersEvent event) {
        event.addManager(ScriptManager.class, ScriptManager::new);
    }
}
