package me.lucent

import me.lucent.Enums.ChipEffectCondition
import kotlin.reflect.KFunction

data class ChipEffect(val chipEffectCondition:ChipEffectCondition,val chipEffectExecutor:String,val chipEffectDescr:String)