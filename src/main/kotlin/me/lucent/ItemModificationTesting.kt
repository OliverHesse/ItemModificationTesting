package me.lucent

import me.lucent.Enums.ChipEffectCondition
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
    val chipInventoryHolder:PlayerInventoryHolder = PlayerInventoryHolder(this);


    fun testingChipSerialization(){

        val chip1:PassiveChip = PassiveChip("TestChip","Passive",ChipEffectCondition.Slotted,"onSlotIncreaseFrostDamage50","Increases Frost damage by 50 points")
        val chip2:PassiveChip = PassiveChip("TestChip2","Passive",ChipEffectCondition.Slotted,"onSlotIncreaseFrostDamage50","Increases Frost damage by 50 points")
        val testString:String = PassiveChip.serializeChips(listOf(chip1,chip2))
        logger.info(testString);
        val backToNormal = PassiveChip.decodeChips(testString);
        logger.info(backToNormal.toString());
    }

    fun giveTestItems(player: Player){
        logger.info("Building item")
        chipInventoryHolder.createInventory(player);



        val key: NamespacedKey = NamespacedKey(this,"PassiveSlots");
        val sword:ItemStack = ItemStack(Material.DIAMOND_SWORD,1)
        val chip1:PassiveChip = PassiveChip("Frost Damage Up","Passive",ChipEffectCondition.Slotted,"onSlotIncreaseFrostDamage50","Increases Frost damage by 50 points")
        val chip2:PassiveChip = PassiveChip("Default Slot Effect","Passive",ChipEffectCondition.Slotted,"onSlotDefault","a default effect")



        sword.editMeta {
            it.persistentDataContainer.set(key, PersistentDataType.INTEGER,7);
            it.persistentDataContainer.set(NamespacedKey(this,"PassiveChips"), PersistentDataType.STRING,PassiveChip.serializeChips(listOf(chip1,chip2)))
        }

        player.inventory.addItem(sword)
        chip1.count = 350;
        chip2.count = 370;
        chipInventoryHolder.addChip(player,chip1);
        chipInventoryHolder.addChip(player,chip2);

    }


    override fun onEnable() {
        // Plugin startup logic
        val plugin:Plugin = this
        testingChipSerialization();
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
