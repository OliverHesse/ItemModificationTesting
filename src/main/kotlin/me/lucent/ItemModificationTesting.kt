package me.lucent

import me.lucent.commands.modCommand
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

class ItemModificationTesting : JavaPlugin() {

    override fun onEnable() {
        // Plugin startup logic
        getCommand("mod")?.setExecutor(modCommand(this))
        server.pluginManager.registerEvents(object : Listener{
            @EventHandler
            fun onPlayerJoinEvent(e:PlayerJoinEvent){
                val sword:ItemStack = ItemStack(Material.DIAMOND_SWORD,1)
                e.player.inventory.addItem(sword)
            }
        },this);
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }


}
