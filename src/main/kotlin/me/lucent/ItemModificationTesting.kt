package me.lucent

import me.lucent.commands.modCommand
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin

class ItemModificationTesting : JavaPlugin() {
    val chipInventoryHolder:PlayerInventoryHolder = PlayerInventoryHolder();


    override fun onEnable() {
        // Plugin startup logic
        val plugin:Plugin = this

        getCommand("mod")?.setExecutor(modCommand(this))
        server.pluginManager.registerEvents(object : Listener{
            @EventHandler
            fun onPlayerJoinEvent(e:PlayerJoinEvent){
                chipInventoryHolder.createInventory(e.player);
                val key: NamespacedKey = NamespacedKey(plugin,"PassiveSlots");
                val sword:ItemStack = ItemStack(Material.DIAMOND_SWORD,1)
                sword.editMeta {
                    it.persistentDataContainer.set(key, PersistentDataType.INTEGER,7);
                }

                e.player.inventory.addItem(sword)
            }
        },this);
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }


}
