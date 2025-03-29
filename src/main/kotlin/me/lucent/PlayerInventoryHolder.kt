package me.lucent

import org.bukkit.entity.Player
import java.util.UUID
import kotlin.uuid.Uuid

class PlayerInventoryHolder {
    private var inventories:HashMap<UUID,ChipInventory> = HashMap();
    fun createInventory(player: Player){
        inventories.put(player.uniqueId, ChipInventory())
    }
    fun getInventory(player: Player):ChipInventory?{
        return inventories[player.uniqueId];
    }
}