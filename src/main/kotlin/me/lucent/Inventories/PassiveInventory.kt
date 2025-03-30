package me.lucent.Inventories

import org.bukkit.plugin.Plugin

class PassiveInventory(val plugin: Plugin){
    var passiveChips:HashMap<String,Int> = HashMap()
    val activeChips:HashMap<String,Int> = HashMap()


    fun addPassiveChip(chip:String,amount:Int){
        if (passiveChips.containsKey(chip)){
            passiveChips[chip] = passiveChips[chip]!! + amount;
            return
        }
        passiveChips[chip] = amount;
    }
    fun removePassiveChip(chip:String,amount:Int){
        if(!passiveChips.containsKey(chip)) return
        passiveChips[chip] = passiveChips[chip]!! - amount
        if(passiveChips[chip]!! <= 0) passiveChips.remove(chip)
    }
    fun getPassiveChips():List<MutableMap.MutableEntry<String,Int>>{
        return passiveChips.entries.toList()
    }



    fun getPassiveChipPages():Int{
        var slotsNeeded:Int = 0;
        for((key,value) in passiveChips){

            slotsNeeded += (value / 64) + 1
            plugin.logger.info("slots: $slotsNeeded" );
            plugin.logger.info("item count: $value")
            if(value % 64 == 0) slotsNeeded -= 1
        }
        var pages = (slotsNeeded/10)+1
        if(slotsNeeded%10 == 0) pages -=1
        return  pages;
    }
}