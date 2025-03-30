package me.lucent

import org.bukkit.entity.Item
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import kotlin.reflect.KCallable
import kotlin.reflect.KFunction


object ChipExecutors {

    val executorNameToFunction = listOf(
        ::onSlotIncreaseFrostDamage50,
        ::onSlotIncreaseHeatDamage50,
        ::onSlotDefault,
        ::onSlotIncreaseRadiationDamage50).associateBy { it.name }



    fun onSlotIncreaseFrostDamage50(plugin:ItemModificationTesting,player: Player,item:ItemStack,unbind:Boolean=false){
        //EXAMPLE

    }
    fun onSlotIncreaseHeatDamage50(plugin:ItemModificationTesting,player: Player,item:ItemStack){
        //EXAMPLE

    }
    fun onSlotIncreaseRadiationDamage50(plugin:ItemModificationTesting,player: Player,item:ItemStack){
        //EXAMPLE

    }

    fun onSlotDefault(plugin:ItemModificationTesting,player: Player,item:ItemStack){
        plugin.logger.info("slotted chip into item ${item.toString()}")

    }
}