package me.lucent

data class PassiveChip(val chipName:String,val chipType:String,val chipEffect: ChipEffect){

    companion object {
        fun serializeChips(chips:List<PassiveChip>){

            //converts chips to json string
        }
    }

}