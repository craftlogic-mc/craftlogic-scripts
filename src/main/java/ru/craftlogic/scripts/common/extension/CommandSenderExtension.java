package ru.craftlogic.scripts.common.extension;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.server.MinecraftServer;
import ru.craftlogic.api.world.CommandSender;
import ru.craftlogic.api.world.Player;

public class CommandSenderExtension {
    public static <T> T asType(CommandSender sender, Class<T> type) throws Exception {
        if (type == Player.class) {
            if (sender instanceof Player) {
                return (T) sender;
            } else {
                throw new CommandException("commands.generic.playerOnly");
            }
        }
        return type.cast(sender);
    }

    public static void chat(CommandSender sender, String message) {
        ICommandSender unwrapped = sender.unwrap();
        if (unwrapped instanceof EntityPlayerMP) {
            ((EntityPlayerMP) unwrapped).connection.processChatMessage(new CPacketChatMessage(message));
        } else if (unwrapped instanceof MinecraftServer) {
            ((MinecraftServer) unwrapped).getCommandManager().executeCommand(unwrapped, message);
        }
    }
}
