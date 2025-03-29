package me.lucent.commands

import me.lucent.ItemModificationTesting
import me.lucent.ModGUI
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin

class modCommand(val plugin:ItemModificationTesting): CommandExecutor{
    override fun onCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<out String>): Boolean {
        if (p0 !is Player) return true;

        val player :Player = p0;
        val playerItem: ItemStack = player.inventory.itemInMainHand;

        ModGUI.open(playerItem,player,plugin);

        return true;
    }
}