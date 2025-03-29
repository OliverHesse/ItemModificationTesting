package me.lucent

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.Plugin
import javax.naming.Name

object ModGUI{
    val invinString = """
            ####CCCCC
            ####CCCCC
            #I#######
            ####EEEEE
            ####EEEEE
            ######BPF
        """.trimIndent().replace("\n","")

    fun build(itemToMod:ItemStack,player: Player,plugin:ItemModificationTesting):Inventory?{


        val inventory:Inventory = plugin.server.createInventory(null,54, Component.text("Mod Item"));
        val container:PersistentDataContainer = itemToMod.itemMeta.persistentDataContainer;
        if(!container.has(NamespacedKey(plugin,"PassiveSlots"), PersistentDataType.INTEGER)){
            return null;
        }
        var slots:Int = container.get(NamespacedKey(plugin,"PassiveSlots"), PersistentDataType.INTEGER)!!;
        val initialSlots = slots
        plugin.logger.info(invinString)
        plugin.logger.info(invinString.length.toString())
        val itemChipContainer = container.get(NamespacedKey(plugin,"PassiveChips"), PersistentDataType.STRING)!!;
        val itemChips = PassiveChip.decodeChips(itemChipContainer);
        for((index,char) in invinString.withIndex()){
            plugin.logger.info(index.toString())
            when (char){
                '#'->inventory.setItem(index,ItemStack(Material.BLACK_STAINED_GLASS_PANE,1))
                'C'->{

                    if(slots != 0){
                        plugin.logger.info("checking chip slot for index $index")
                        plugin.logger.info((itemChips.size-slots).toString())
                        if(initialSlots-slots < itemChips.size){
                            plugin.logger.info("creating chip for index $index")
                            inventory.setItem(index,PassiveChip.generateChipItemStack(plugin,itemChips[initialSlots-slots]))
                        }

                        slots -=1;
                        //TODO add chip if available

                    }else{
                        inventory.setItem(index,ItemStack(Material.BLACK_STAINED_GLASS_PANE,1))
                    }
                }
                'I'->inventory.setItem(index,itemToMod)
                'B'->inventory.setItem(index,ItemStack(Material.BLACK_STAINED_GLASS_PANE,1))
                'P'->inventory.setItem(index,ItemStack(Material.BLACK_STAINED_GLASS_PANE,1))
                'F'->{
                    //TODO just....clean up shits messy
                    if(plugin.chipInventoryHolder.getInventory(player)!!.getPages() > 1){
                        inventory.setItem(index,ItemStack(Material.ARROW,1));
                    }else{
                        inventory.setItem(index,ItemStack(Material.BLACK_STAINED_GLASS_PANE,1))
                    }
                }
            }
        }

        //TODO add in proper formating and chips
        return inventory

    }

    fun open(itemToMod: ItemStack,player:Player,plugin:ItemModificationTesting){
        val inventory:Inventory = build(itemToMod,player,plugin)!!
        registerIndividualListener(inventory,player,itemToMod,plugin);
        player.openInventory(inventory);
    }

    fun updateInventoryChips(inventory: Inventory,player: Player,chipPage:Int){

    }

    fun registerIndividualListener(immutableInventory: Inventory,player:Player,itemToMod:ItemStack,plugin:ItemModificationTesting){
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