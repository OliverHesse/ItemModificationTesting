package me.lucent

import me.lucent.Enums.ChipEffectCondition
import me.lucent.Inventories.PlayerInventoryHolder
import me.lucent.commands.modCommand
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin

class ItemModificationTesting : JavaPlugin() {

    val playerInventoryHolder:PlayerInventoryHolder = PlayerInventoryHolder(this)



    fun giveTestItems(player: Player){
        player.inventory.clear();

        logger.info("Building item")
        playerInventoryHolder.setupInventoriesForPlayer(player);



        val key: NamespacedKey = NamespacedKey(this,"PassiveSlots");
        val sword:ItemStack = ItemStack(Material.DIAMOND_SWORD,1)
        val chip1 = "frostDamage1"
        val chip2 = "frostDamageUp2"
        val chip3 = "corpseExplosion2"
        val chip4 = "frostDamageUp3"


        sword.editMeta {
            it.persistentDataContainer.set(key, PersistentDataType.INTEGER,7);
            it.persistentDataContainer.set(NamespacedKey(this,"PassiveChips"), PersistentDataType.STRING,listOf(chip1,chip2).joinToString("\n"))
        }

        player.inventory.addItem(sword)

        playerInventoryHolder.getPassiveInventory(player)!!.addPassiveChip(chip1,200);
        playerInventoryHolder.getPassiveInventory(player)!!.addPassiveChip(chip2,200);
        playerInventoryHolder.getPassiveInventory(player)!!.addPassiveChip(chip3,200);
        playerInventoryHolder.getPassiveInventory(player)!!.addPassiveChip(chip4,200);

    }


    override fun onEnable() {

        if(!dataFolder.exists()) dataFolder.mkdir()
        saveResource("PassiveChips.yml",true)

        // Plugin startup logic
        val plugin:Plugin = this

        getCommand("mod")?.setExecutor(modCommand(this))
        server.pluginManager.registerEvents(object : Listener{
            @EventHandler
            fun onPlayerJoinEvent(e:PlayerJoinEvent){
                giveTestItems(e.player)
            }
        },this);
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }


}
