package ru.craftlogic.scripts;

import net.minecraftforge.event.*;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.event.world.NoteBlockEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import ru.craftlogic.api.event.player.PlayerSneakEvent;

import java.util.HashMap;
import java.util.Map;

public class Events {
    private static final Map<String, Class<? extends Event>> REGISTRY = new HashMap<>();

    static void init() {
        REGISTRY.clear();
        register("entity:enter_chunk", EntityEvent.EnteringChunk.class);
        register("entity:check_can_update", EntityEvent.CanUpdate.class);
        register("entity:construct", EntityEvent.EntityConstructing.class);
        register("entity:interact", PlayerInteractEvent.EntityInteract.class);
        register("entity:interact_precise", PlayerInteractEvent.EntityInteractSpecific.class);
        register("living:update", LivingEvent.LivingUpdateEvent.class);
        register("living:jump", LivingEvent.LivingJumpEvent.class);
        register("registry", RegistryEvent.class);
        register("loot_table:load", LootTableLoadEvent.class);
        register("difficulty:change", DifficultyChangeEvent.class);
        register("command", CommandEvent.class);
        register("anvil:update", AnvilUpdateEvent.class);
        register("portal:spawn", BlockEvent.PortalSpawnEvent.class);
        register("crop:grow:pre", BlockEvent.CropGrowEvent.Pre.class);
        register("crop:grow:post", BlockEvent.CropGrowEvent.Post.class);
        register("fluid_source:create", BlockEvent.CreateFluidSourceEvent.class);
        register("block:neighbor_notify", BlockEvent.NeighborNotifyEvent.class);
        register("block:multi_place", BlockEvent.MultiPlaceEvent.class);
        register("block:place", BlockEvent.PlaceEvent.class);
        register("block:break", BlockEvent.BreakEvent.class);
        register("block:harvest_drops", BlockEvent.HarvestDropsEvent.class);
        register("block:hit_empty", PlayerInteractEvent.LeftClickEmpty.class);
        register("block:hit", PlayerInteractEvent.LeftClickBlock.class);
        register("block:interact_empty", PlayerInteractEvent.RightClickEmpty.class);
        register("block:interact", PlayerInteractEvent.RightClickBlock.class);
        register("explosion", ExplosionEvent.class);
        register("explosion:start", ExplosionEvent.Start.class);
        register("explosion:detonate", ExplosionEvent.Detonate.class);
        register("note_block:change", NoteBlockEvent.Change.class);
        register("note_block:play", NoteBlockEvent.Play.class);
        register("player:chat", ServerChatEvent.class);
        register("player:sneak", PlayerSneakEvent.class);
        register("player:respawn", PlayerEvent.PlayerRespawnEvent.class);
        register("player:login", PlayerEvent.PlayerLoggedInEvent.class);
        register("player:logout", PlayerEvent.PlayerLoggedOutEvent.class);
        register("player:tick", TickEvent.PlayerTickEvent.class);
        register("server:tick", TickEvent.ServerTickEvent.class);
        register("client:tick", TickEvent.ClientTickEvent.class);
        register("world:tick", TickEvent.WorldTickEvent.class);
        //TODO
    }

    public static void register(String name, Class<? extends Event> eventType) {
        if (REGISTRY.containsKey(name)) {
            throw new IllegalStateException("Event type " + name + " is already registered!");
        }
        REGISTRY.put(name, eventType);
    }

    public static Class<? extends Event> get(String name) {
        return REGISTRY.get(name);
    }
}
