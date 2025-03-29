package me.lucent.Enums

import me.lucent.ChipExecutors


//Slotted condition executor args Plugin,Player,ItemStack
//onAttack condition executor args Plugin, Player,ItemStack,Target
//onTargetKilled condition executor args Plugin, Player,ItemStack,Target
//onMoved condition executor args Plugin, Player,ItemStack
//onDefence condition executor args plugin, Player,ItemStack,Attacker
enum class ChipEffectCondition {
    Slotted,
    OnAttack,
    OnTargetKilled,
    OnMoved,
    onDefence;

    fun fromString(enumString:String):ChipEffectCondition?{
        return ChipEffectCondition.entries.find { it.toString() == enumString }
    }
}