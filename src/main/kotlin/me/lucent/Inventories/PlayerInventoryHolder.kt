package me.lucent.Inventories


import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import java.util.*
import kotlin.collections.HashMap

class PlayerInventoryHolder(val plugin: Plugin) {
    private var passiveInventories:HashMap<UUID, PassiveInventory> = HashMap();
    private var armoryInventories:HashMap<UUID,ArmoryInventory> = HashMap();

    fun getPassiveInventory(player: Player): PassiveInventory?{
        return passiveInventories[player.uniqueId]
    }
    fun getArmoryInventory(player: Player): ArmoryInventory?{
        return armoryInventories[player.uniqueId]
    }

    fun setupInventoriesForPlayer(player: Player){
        passiveInventories[player.uniqueId] = PassiveInventory(plugin)
        armoryInventories[player.uniqueId] = ArmoryInventory(plugin)
    }
}