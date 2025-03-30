package me.lucent

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Item
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

    fun build(itemToMod:ItemStack,player: Player,plugin:ItemModificationTesting,pageNum:Int,inventory: Inventory=plugin.server.createInventory(null,54, Component.text("Mod Item"))):Inventory?{

        plugin.logger.info("current page num: $pageNum")


        val container:PersistentDataContainer = itemToMod.itemMeta.persistentDataContainer;
        if(!container.has(NamespacedKey(plugin,"PassiveSlots"), PersistentDataType.INTEGER)){
            return null;
        }

        var slots:Int = container.get(NamespacedKey(plugin,"PassiveSlots"), PersistentDataType.INTEGER)!!;
        val initialSlots = slots


        plugin.logger.info(invinString)

        val itemChipContainer = container.get(NamespacedKey(plugin,"PassiveChips"), PersistentDataType.STRING)!!;
        val itemChips = PassiveChip.decodeChips(itemChipContainer);

        val inventoryChips = plugin.chipInventoryHolder.getInventory(player)!!.getChips()
        var currIndex = 0;
        var currentConsumed = inventoryChips[0].count;

        var offset = (pageNum-1)*10

        val pagesLeft = plugin.chipInventoryHolder.getInventory(player)!!.getPages()-pageNum;
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


                    }else{
                        inventory.setItem(index,ItemStack(Material.BLACK_STAINED_GLASS_PANE,1))
                    }
                }
                'I'->inventory.setItem(index,itemToMod)
                'B'->{
                    if(pageNum >1) inventory.setItem(index,ItemStack(Material.ARROW,1))
                    else inventory.setItem(index,ItemStack(Material.BLACK_STAINED_GLASS_PANE,1))

                }
                'P'->inventory.setItem(index,ItemStack(Material.CYAN_STAINED_GLASS_PANE,pageNum))
                'F'->{
                    //TODO just....clean up shits messy

                    if(pagesLeft > 0) inventory.setItem(index,ItemStack(Material.ARROW,1))
                    else inventory.setItem(index,ItemStack(Material.BLACK_STAINED_GLASS_PANE,1))

                }
                'E'-> {
                    //TODO Try and tidy this up
                    while(offset > 0){
                        if (currentConsumed > 64){
                            currentConsumed -= 64
                            offset -= 1;
                        }else{
                            currIndex +=1
                            offset -=1
                            currentConsumed = if (currIndex < inventoryChips.size) inventoryChips[currIndex].count else 0
                        }
                    }
                    if (currIndex >= inventoryChips.size) {
                        inventory.setItem(index,null);
                        continue
                    };

                    val chip = inventoryChips[currIndex].copy()
                    if (currentConsumed> 64) {
                        chip.count = 64;
                        currentConsumed -= 64;
                    } else {
                        currIndex += 1
                        chip.count = currentConsumed
                        currentConsumed = if (currIndex < inventoryChips.size) inventoryChips[currIndex].count else 0
                    }
                    inventory.setItem(index,PassiveChip.generateChipItemStack(plugin,chip))


                }

            }
        }

        //TODO add in proper formating and chips
        return inventory

    }

    fun open(itemToMod: ItemStack,player:Player,plugin:ItemModificationTesting){
        val inventory:Inventory = build(itemToMod,player,plugin,1)!!
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
                e.isCancelled = true;
                plugin.logger.info("modding item from player ${Eventplayer.name}");

                //TODO page swapping
                if(e.slot == invinString.indexOf('F') && e.currentItem == ItemStack(Material.ARROW,1)){
                    //Next page was clicked
                    if(build(itemToMod,player,plugin, e.clickedInventory!!.getItem(invinString.indexOf('P'))!!.amount+1,immutableInventory) != null){
                        player.updateInventory()
                    }else{
                        player.closeInventory()
                    }

                }
                if(e.slot == invinString.indexOf('B') && e.currentItem == ItemStack(Material.ARROW,1)){
                    //Back page was clicked
                    if(build(itemToMod,player,plugin, e.clickedInventory!!.getItem(invinString.indexOf('P'))!!.amount-1,immutableInventory) != null){
                        player.updateInventory()
                    }else{
                        player.closeInventory()
                    }
                }

                //TODO add chips swapping


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