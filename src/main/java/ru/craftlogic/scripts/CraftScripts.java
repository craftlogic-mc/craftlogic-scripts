package ru.craftlogic.scripts;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import ru.craftlogic.api.CraftAPI;
import ru.craftlogic.api.dependency.RemoteDependency;
import ru.craftlogic.api.network.AdvancedNetwork;
import ru.craftlogic.api.server.Server;
import ru.craftlogic.scripts.common.ProxyCommon;
import ru.craftlogic.scripts.network.message.MessageShowScriptScreen;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Mod(modid = CraftScripts.MOD_ID, version = CraftScripts.VERSION, dependencies = "required-after:" + CraftAPI.MOD_ID)
@RemoteDependency(
    value = "org.codehaus.groovy:groovy-all:jar:" + CraftScripts.GROOVY_VERSION,
    transformerExclusions = {
        "groovy.", "org.codehaus.groovy.", "groovyjarjarantlr.", "groovyjarjarasm.", "groovyjarjarcommonscli."
    }
)
public class CraftScripts {
    public static final String MOD_ID = CraftAPI.MOD_ID + "-scripts";
    public static final String VERSION = "0.2.0-BETA";
    public static final String GROOVY_VERSION = "2.4.4";

    @SidedProxy(clientSide = "ru.craftlogic.scripts.client.ProxyClient", serverSide = "ru.craftlogic.scripts.common.ProxyCommon")
    public static ProxyCommon PROXY;
    public static final AdvancedNetwork NETWORK = new AdvancedNetwork(MOD_ID);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(PROXY);
        PROXY.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        NETWORK.openChannel();
        PROXY.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        PROXY.postInit();
    }

    public static void showScreen(String id, EntityPlayer player, String args) {
        if (player instanceof EntityPlayerMP) {
            Server server = Server.from(player.getServer());
            String name = CraftScriptsConfig.scriptsFolder + "/screens/" + id;
            Path script = server.getDataDirectory().resolve(name + ".gs");
            if (Files.exists(script)) {
                try {
                    String raw = String.join("\n", Files.readAllLines(script));
                    Path info = server.getDataDirectory().resolve(name + ".json");
                    JsonObject obj = Files.exists(info) ? new Gson().fromJson(Files.newBufferedReader(info), JsonObject.class) : new JsonObject();
                    NETWORK.sendTo(player, new MessageShowScriptScreen(id, obj, raw, args));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

