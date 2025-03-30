package me.lucent

import org.bukkit.entity.Item
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import kotlin.reflect.KCallable
import kotlin.reflect.KFunction


object ChipExecutors {

    val executorNameToFunction = listOf(
        ::onSlotFrostDamage1,
        ::onSlotDefault).associateBy { it.name }

    fun onSlotFrostDamage1(plugin:ItemModificationTesting,player: Player,item:ItemStack,unbind:Boolean=false){}



    fun onSlotDefault(plugin:ItemModificationTesting,player: Player,item:ItemStack){
        plugin.logger.info("slotted chip into item ${item.toString()}")

    }
}