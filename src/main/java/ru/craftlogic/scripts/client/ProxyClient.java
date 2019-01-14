package ru.craftlogic.scripts.client;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import ru.craftlogic.api.network.AdvancedMessage;
import ru.craftlogic.scripts.client.screen.ScreenCustom;
import ru.craftlogic.scripts.common.ProxyCommon;
import ru.craftlogic.scripts.network.message.MessageCustom;
import ru.craftlogic.scripts.network.message.MessageShowScriptScreen;
import ru.craftlogic.util.ReflectiveUsage;

import static ru.craftlogic.scripts.CraftScripts.NETWORK;

@ReflectiveUsage
public class ProxyClient extends ProxyCommon {
    private final Minecraft client = FMLClientHandler.instance().getClient();

    @Override
    public void preInit() {
        super.preInit();
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void postInit() {
        super.postInit();
    }

    @Override
    protected AdvancedMessage handleClientCustom(MessageCustom message, MessageContext context) {
        syncTask(context, () -> {
            if (this.client.currentScreen instanceof ScreenCustom) {
                ScreenCustom screen = (ScreenCustom) this.client.currentScreen;
                String channel = message.getChannel();
                NBTTagCompound response = screen.handlePayload(channel, message.getData());
                if (response != null) {
                    NETWORK.sendToServer(new MessageCustom(channel, response));
                }
            }
        });
        return null;
    }

    @Override
    protected AdvancedMessage handleShowScriptScreen(MessageShowScriptScreen message, MessageContext context) {
        syncTask(context, () -> this.client.displayGuiScreen(
            new ScreenCustom(message.getId(), message.getInfo(), message.getScript(), message.getArgs())
        ));
        return null;
    }
}
