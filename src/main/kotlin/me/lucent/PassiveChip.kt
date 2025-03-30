package me.lucent

import me.lucent.Enums.ChipEffectCondition
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.Plugin

@Serializable
data class PassiveChip(val chipName:String, val chipType:String, val chipEffectCondition: ChipEffectCondition, val chipEffectExecutor:String, val chipEffectDescr:String,var count:Int=1){

    companion object {
        fun generateChipFromItemStack(item:ItemStack):PassiveChip?{
            return null
        }

        fun serializeChip(chip:PassiveChip):String{
            return Json.encodeToString(chip)
        }

        fun serializeChips(chips:List<PassiveChip>):String{
            return Json.encodeToString(chips);
            //converts chips to json string
        }
        fun decodeChips(jsonChipString:String):List<PassiveChip>{

            return Json.decodeFromString<List<PassiveChip>>(jsonChipString);
        }
        fun generateChipItemStack(plugin:Plugin,chip:PassiveChip): ItemStack{
            val item = ItemStack(Material.REDSTONE,chip.count)
            val lore = buildList<Component> {

                add(Component.text("type: " + chip.chipType).color(TextColor.color(97, 97, 97)))
                add(Component.text("Effect: "))
                add(Component.text(chip.chipEffectDescr))
            }

            item.editMeta {
                it.displayName(Component.text(chip.chipName).color(TextColor.color(12,221,240)).decorate(TextDecoration.BOLD))
                it.persistentDataContainer.set(NamespacedKey(plugin,"chipName"), PersistentDataType.STRING,chip.chipName)
                it.persistentDataContainer.set(NamespacedKey(plugin,"chipType"), PersistentDataType.STRING,chip.chipType)
                it.persistentDataContainer.set(NamespacedKey(plugin,"chipEffectCondition"), PersistentDataType.STRING,chip.chipEffectCondition.toString())
                it.persistentDataContainer.set(NamespacedKey(plugin,"chipEffectExecutor"), PersistentDataType.STRING,chip.chipEffectExecutor)
                it.persistentDataContainer.set(NamespacedKey(plugin,"chipEffectDescr"), PersistentDataType.STRING,chip.chipEffectDescr)
                it.lore(lore)
            }
            return item
        }
    }


}