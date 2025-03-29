package me.lucent

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin

object ModGUI{


    fun build(itemToMod:ItemStack,player: Player,plugin:Plugin):Inventory{
        val inventory:Inventory = plugin.server.createInventory(null,27, Component.text("Mod Item"));
        inventory.setItem(0,itemToMod);

        //TODO add in proper formating and chips
        return inventory

    }

    fun open(itemToMod: ItemStack,player:Player,plugin:Plugin){
        val inventory:Inventory = build(itemToMod,player,plugin)
        registerIndividualListener(inventory,player,itemToMod,plugin);
        player.openInventory(inventory);
    }

    fun updateInventoryChips(inventory: Inventory,player: Player,chipPage:Int){

    }

    fun registerIndividualListener(immutableInventory: Inventory,player:Player,itemToMod:ItemStack,plugin:Plugin){
        Bukkit.getPluginManager().registerEvents(object : Listener{


            val Eventplayer:Player = player;
            val EventitemToMod:ItemStack = itemToMod
            var chipPage:Int = 0
            val EventPlugin:Plugin = plugin;
            @EventHandler
            fun onInventoryClick(e:InventoryClickEvent){
                if (e.clickedInventory != immutableInventory) return;
                plugin.logger.info("modding item from player ${Eventplayer.name}");
                //TODO add chips swapping

                e.isCancelled = true;
            }

            @EventHandler
            fun onInventoryDrag(e:InventoryDragEvent){
                if(e.inventory == immutableInventory) e.isCancelled = true;
            }

            @EventHandler
            fun onInventoryClose(e:InventoryCloseEvent){
                InventoryClickEvent.getHandlerList().unregister(this);
                InventoryDragEvent.getHandlerList().unregister(this);
                InventoryCloseEvent.getHandlerList().unregister(this);
            }

        },plugin)
    }

}