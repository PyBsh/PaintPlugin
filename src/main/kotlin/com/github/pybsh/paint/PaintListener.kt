package com.github.pybsh.paint

import net.kyori.adventure.text.Component.text
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin

class PaintListener: Listener {
    private fun getInstance(): Plugin {
        return PaintMain.instance
    }

    private val config = getInstance().config
    private val scheduler = getInstance().server.scheduler

    @EventHandler
    fun onPlayerInteract(e: PlayerInteractEvent){
        val player = e.player
        val action = e.action
        val item = player.inventory.itemInMainHand

        if(action == Action.LEFT_CLICK_AIR && item == ItemStack(Material.ARROW)){
            val f = config.getBoolean("${player.uniqueId}.Pen")
            config.set("${player.uniqueId}.Pen",!f)

            if(!f) player.sendMessage(text("펜 내리기!"))
            else player.sendMessage(text("펜 올리기!"))

            scheduler.runTaskTimer(getInstance(), Runnable{
                val flag = config.getBoolean("${player.uniqueId}.Pen")
                if(flag) {
                    val block = player.getTargetBlock(100)
                    val canvas = config.getItemStack("${player.uniqueId}.Block")

                    if (block != null && canvas != null) block.type = canvas.type
                }
            }, 0L, 0L)

        }

    }

}