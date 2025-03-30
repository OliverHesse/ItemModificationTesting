package me.lucent

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.Plugin
import java.io.File

object ItemConstructor {

    fun buildPassiveChip(plugin:Plugin,chip:String,amount:Int):ItemStack{
        plugin.logger.info("creating item stack for $chip")
        val passiveChipDataFile = YamlConfiguration.loadConfiguration(File(plugin.dataFolder,"/PassiveChips.yml"))
        val item = ItemStack(Material.REDSTONE,amount);
        var textColor = TextColor.color(0)
        plugin.logger.info(passiveChipDataFile.toString())
        val chipConfig = passiveChipDataFile.getConfigurationSection(chip)!!
        when(chipConfig.getString("rarity")){
            "Bronze"->textColor = TextColor.color(205, 127, 50)
            "Silver"->textColor = TextColor.color(192, 192, 192)
            "Gold"->textColor = TextColor.color(255,215,0)
        }
        val lore = buildList<Component> {
            add(Component.text("chip type: " + "Passive").color(TextColor.color(97, 97, 97)))
            add(Component.text("item type: " + chipConfig.getString("equipmentTag")!!).color(TextColor.color(97, 97, 97)))
            add(Component.text("Effect: "))
            add(Component.text(chipConfig.getString("effect")!!))
        }
        item.editMeta {
            it.displayName(Component.text(chipConfig.getString("name")!!).color(textColor).decorate(TextDecoration.BOLD))
            it.persistentDataContainer.set(NamespacedKey(plugin,"PassiveChipId"), PersistentDataType.STRING,chip)
            it.lore(lore)
        }
        return item
    }
}