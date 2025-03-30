package me.lucent

import org.bukkit.plugin.Plugin

class ChipInventory(val plugin: Plugin){

    private var chips:HashMap<String,PassiveChip> = HashMap()
    fun addChip(chip: PassiveChip){
        if(chips.containsKey(chip.chipName)){ chips[chip.chipName]!!.count += chip.count; return;}
        chips[chip.chipName] = chip

    }
    fun removeChip(chipName:String,count:Int){
        chips[chipName]!!.count -= count;
        if(chips[chipName]!!.count <= 0) chips.remove(chipName);
    }

    fun getChips():List<PassiveChip>{
        return chips.values.toList();
    }

    fun getPages():Int{
        var slotsNeeded:Int = 0;
        for((key,value) in chips){

            slotsNeeded += (value.count / 64) + 1
            plugin.logger.info("slots: $slotsNeeded" );
            plugin.logger.info("item count: ${value.count}")
            if(value.count % 64 == 0) slotsNeeded -= 1
        }
        var pages = (slotsNeeded/10)+1
        if(slotsNeeded%10 == 0) pages -=1
        return  pages;
    }



}