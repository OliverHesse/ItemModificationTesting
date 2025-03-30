package me.lucent

import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import java.util.UUID

class PlayerInventoryHolder(val plugin: Plugin){
    private var inventories:HashMap<UUID,ChipInventory> = HashMap();
    fun createInventory(player: Player){
        inventories.put(player.uniqueId, ChipInventory(plugin))
    }
    fun addChip(player: Player,chip: PassiveChip){
        inventories[player.uniqueId]!!.addChip(chip)
    }
    fun getInventory(player: Player):ChipInventory?{
        return inventories[player.uniqueId];
    }
}