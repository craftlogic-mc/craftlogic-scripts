package ru.craftlogic.scripts;

import net.minecraftforge.common.config.Config;

@Config(modid = CraftScripts.MOD_ID)
public class CraftScriptsConfig {
    @Config.Comment("Root directory for script files")
    public static String scriptsFolder = "scripts";
}
