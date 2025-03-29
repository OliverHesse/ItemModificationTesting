package me.lucent.Enums



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
    onDefence
}