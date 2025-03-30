package me.lucent.GUI

import me.lucent.ItemConstructor
import me.lucent.ItemModificationTesting
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Item
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.ListPersistentDataType
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.Plugin
import java.io.File
import javax.naming.Name
import kotlin.math.max

object PassiveChipModGUI {
    val invinString = """
            ####CCCCC
            ####CCCCC
            #I#######
            ####EEEEE
            ####EEEEE
            ######BPF
        """.trimIndent().replace("\n","")

    fun build(itemToMod: ItemStack, player: Player, plugin: ItemModificationTesting, pageNum:Int, inventory: Inventory =plugin.server.createInventory(null,54, Component.text("Mod Item"))): Inventory?{

        plugin.logger.info("current page num: $pageNum")


        val container: PersistentDataContainer = itemToMod.itemMeta.persistentDataContainer;
        if(!container.has(NamespacedKey(plugin,"PassiveSlots"), PersistentDataType.INTEGER)){
            return null;
        }

        var slots:Int = container.get(NamespacedKey(plugin,"PassiveSlots"), PersistentDataType.INTEGER)!!;
        val initialSlots = slots


        plugin.logger.info(invinString)

        val itemPassiveChips:List<String> = container.get(NamespacedKey(plugin,"PassiveChips"), PersistentDataType.STRING)!!.split('\n');


        val passiveChipInventory = plugin.playerInventoryHolder.getPassiveInventory(player)!!
        val inventoryPassiveChips = passiveChipInventory.getPassiveChips()
        var currIndex = 0;
        var currentConsumed = inventoryPassiveChips[0].value;

        var offset = (pageNum-1)*10

        val pagesLeft = passiveChipInventory.getPassiveChipPages()-pageNum;

        for((index,char) in invinString.withIndex()){
            plugin.logger.info(index.toString())
            when (char){
                '#'->inventory.setItem(index, ItemStack(Material.BLACK_STAINED_GLASS_PANE,1))
                'C'->{

                    if(slots != 0){
                        plugin.logger.info("checking chip slot for index $index")
                        plugin.logger.info((itemPassiveChips.size-slots).toString())
                        if(initialSlots-slots < itemPassiveChips.size){
                            plugin.logger.info("creating chip for index $index")
                            inventory.setItem(index, ItemConstructor.buildPassiveChip(plugin,itemPassiveChips[initialSlots-slots],1))
                        }else{
                            inventory.setItem(index,null)
                        }

                        slots -=1;


                    }else{
                        inventory.setItem(index, ItemStack(Material.BLACK_STAINED_GLASS_PANE,1))
                    }
                }
                'I'->inventory.setItem(index,itemToMod)
                'B'->{
                    if(pageNum >1) inventory.setItem(index, ItemStack(Material.ARROW,1))
                    else inventory.setItem(index, ItemStack(Material.BLACK_STAINED_GLASS_PANE,1))

                }
                'P'->inventory.setItem(index, ItemStack(Material.CYAN_STAINED_GLASS_PANE,pageNum))
                'F'->{
                    //TODO just....clean up shits messy

                    if(pagesLeft > 0) inventory.setItem(index, ItemStack(Material.ARROW,1))
                    else inventory.setItem(index, ItemStack(Material.BLACK_STAINED_GLASS_PANE,1))

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
                            currentConsumed = if (currIndex < inventoryPassiveChips.size) inventoryPassiveChips[currIndex].value else 0
                        }
                    }
                    if (currIndex >= inventoryPassiveChips.size) {
                        inventory.setItem(index,null);
                        continue
                    };
                    plugin.logger.info("Number Of chips ${inventoryPassiveChips[currIndex].value}")
                    var amount = 0
                    var newItem = ItemConstructor.buildPassiveChip(plugin,inventoryPassiveChips[currIndex].key,1)
                    if (currentConsumed> 64) {
                        amount = 64;
                        currentConsumed -= 64;
                    } else {
                        currIndex += 1
                        amount = currentConsumed
                        currentConsumed = if (currIndex < inventoryPassiveChips.size) inventoryPassiveChips[currIndex].value else 0
                    }
                    newItem.amount = amount
                    inventory.setItem(index, newItem)


                }

            }
        }

        //TODO add in proper formating and chips
        return inventory

    }

    fun open(itemToMod: ItemStack, player: Player, plugin: ItemModificationTesting){
        val inventory: Inventory = build(itemToMod,player,plugin,1)!!
        registerIndividualListener(inventory,player,itemToMod,plugin);
        player.openInventory(inventory);
    }

    fun updateInventoryChips(inventory: Inventory, player: Player, chipPage:Int){

    }

    fun registerIndividualListener(immutableInventory: Inventory, player: Player, itemToMod: ItemStack, plugin: ItemModificationTesting){
        Bukkit.getPluginManager().registerEvents(object : Listener {



            @EventHandler
            fun onInventoryClick(e: InventoryClickEvent){
                if (e.clickedInventory != immutableInventory) return;
                e.isCancelled = true;
                plugin.logger.info("modding item from player ${player.name}");

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

                if(e.currentItem!!.type == Material.REDSTONE && e.slot > 18){
                    //Chip from inventory selected
                    //Slot into weapon if slots are available


                    val maxSlots = itemToMod.itemMeta.persistentDataContainer.get(
                        NamespacedKey(plugin,"PassiveSlots"),
                        PersistentDataType.INTEGER)
                    val passiveChips = itemToMod.itemMeta.persistentDataContainer.get(
                        NamespacedKey(plugin,"PassiveChips"),
                        PersistentDataType.STRING
                    )!!.split('\n').toMutableList()
                    val chipId = e.currentItem!!.itemMeta.persistentDataContainer.get(
                        NamespacedKey(plugin,"PassiveChipId"),
                        PersistentDataType.STRING)

                    //no free passive chip slots
                    if(passiveChips.size == maxSlots) return

                    passiveChips.add(chipId!!)
                    plugin.playerInventoryHolder.getPassiveInventory(player)!!.removePassiveChip(chipId,1)
                    itemToMod.editMeta {
                        it.persistentDataContainer.set(
                            NamespacedKey(plugin,"PassiveChips"),
                            PersistentDataType.STRING,
                            passiveChips.joinToString("\n"))
                    }
                    build(itemToMod,player,plugin,e.clickedInventory!!.getItem(invinString.indexOf('P'))!!.amount,immutableInventory)

                    //TODO call onSlot if it is registered for that event



                }


                if(e.currentItem!!.type == Material.REDSTONE && e.slot < 18){
                    //remove from weapon and add to inventory
                    //Chip from inventory selected
                    //Slot into weapon if slots are available

                    val passiveChips = itemToMod.itemMeta.persistentDataContainer.get(
                        NamespacedKey(plugin,"PassiveChips"),
                        PersistentDataType.STRING
                    )!!.split('\n').toMutableList()
                    val chipId = e.currentItem!!.itemMeta.persistentDataContainer.get(
                        NamespacedKey(plugin,"PassiveChipId"),
                        PersistentDataType.STRING)


                    passiveChips.remove(chipId!!)
                    plugin.playerInventoryHolder.getPassiveInventory(player)!!.addPassiveChip(chipId,1)
                    itemToMod.editMeta {
                        it.persistentDataContainer.set(
                            NamespacedKey(plugin,"PassiveChips"),
                            PersistentDataType.STRING,
                            passiveChips.joinToString("\n"))
                    }
                    build(itemToMod,player,plugin,e.clickedInventory!!.getItem(invinString.indexOf('P'))!!.amount,immutableInventory)


                    //TODO call onSlot unbind = true if it is registered for that event

                }

            }

            @EventHandler
            fun onInventoryDrag(e: InventoryDragEvent){
                if(e.inventory == immutableInventory) e.isCancelled = true;
            }

            @EventHandler
            fun onInventoryClose(e: InventoryCloseEvent){
                InventoryClickEvent.getHandlerList().unregister(this);
                InventoryDragEvent.getHandlerList().unregister(this);
                InventoryCloseEvent.getHandlerList().unregister(this);
            }

        },plugin)
    }

}