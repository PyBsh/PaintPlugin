package com.github.pybsh.paint

import io.github.monun.kommand.kommand
import net.kyori.adventure.text.Component.text
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitTask
import java.util.*

object PaintKommand {
    private fun getInstance(): Plugin {
        return PaintMain.instance
    }

    val Page1: MutableMap<Int, ItemStack> = mutableMapOf(
        0 to ItemStack(Material.ARROW),
        1 to ItemStack(Material.RED_CONCRETE),
        2 to ItemStack(Material.ORANGE_CONCRETE),
        3 to ItemStack(Material.YELLOW_CONCRETE),
        4 to ItemStack(Material.PINK_CONCRETE),
        5 to ItemStack(Material.MAGENTA_CONCRETE),
        6 to ItemStack(Material.PURPLE_CONCRETE),
        7 to ItemStack(Material.PAPER),
        8 to ItemStack(Material.BARRIER)
    )

    val Page2: MutableMap<Int, ItemStack> = mutableMapOf(
        0 to ItemStack(Material.ARROW),
        1 to ItemStack(Material.BLUE_CONCRETE),
        2 to ItemStack(Material.CYAN_CONCRETE),
        3 to ItemStack(Material.LIGHT_BLUE_CONCRETE),
        4 to ItemStack(Material.GREEN_CONCRETE),
        5 to ItemStack(Material.LIME_CONCRETE),
        6 to ItemStack(Material.AIR),
        7 to ItemStack(Material.PAPER),
        8 to ItemStack(Material.BARRIER)
    )

    val Page3: MutableMap<Int, ItemStack> = mutableMapOf(
        0 to ItemStack(Material.ARROW),
        1 to ItemStack(Material.BLACK_CONCRETE),
        2 to ItemStack(Material.GRAY_CONCRETE),
        3 to ItemStack(Material.BROWN_CONCRETE),
        4 to ItemStack(Material.LIGHT_GRAY_CONCRETE),
        5 to ItemStack(Material.WHITE_CONCRETE),
        6 to ItemStack(Material.AIR),
        7 to ItemStack(Material.PAPER),
        8 to ItemStack(Material.BARRIER)
    )

    val Page4: MutableMap<Int, ItemStack> = mutableMapOf(
        0 to ItemStack(Material.ARROW),
        1 to ItemStack(Material.RED_TERRACOTTA),
        2 to ItemStack(Material.ORANGE_TERRACOTTA),
        3 to ItemStack(Material.YELLOW_TERRACOTTA),
        4 to ItemStack(Material.PINK_TERRACOTTA),
        5 to ItemStack(Material.MAGENTA_TERRACOTTA),
        6 to ItemStack(Material.PURPLE_TERRACOTTA),
        7 to ItemStack(Material.PAPER),
        8 to ItemStack(Material.BARRIER)
    )

    val Page5: MutableMap<Int, ItemStack> = mutableMapOf(
        0 to ItemStack(Material.ARROW),
        1 to ItemStack(Material.BLUE_TERRACOTTA),
        2 to ItemStack(Material.CYAN_TERRACOTTA),
        3 to ItemStack(Material.LIGHT_BLUE_TERRACOTTA),
        4 to ItemStack(Material.GREEN_TERRACOTTA),
        5 to ItemStack(Material.LIME_TERRACOTTA),
        6 to ItemStack(Material.AIR),
        7 to ItemStack(Material.PAPER),
        8 to ItemStack(Material.BARRIER)
    )

    val Page6: MutableMap<Int, ItemStack> = mutableMapOf(
        0 to ItemStack(Material.ARROW),
        1 to ItemStack(Material.BLACK_TERRACOTTA),
        2 to ItemStack(Material.GRAY_TERRACOTTA),
        3 to ItemStack(Material.BROWN_TERRACOTTA),
        4 to ItemStack(Material.LIGHT_GRAY_TERRACOTTA),
        5 to ItemStack(Material.WHITE_TERRACOTTA),
        6 to ItemStack(Material.AIR),
        7 to ItemStack(Material.PAPER),
        8 to ItemStack(Material.BARRIER)
    )

    private val config = getInstance().config
    private val scheduler = getInstance().server.scheduler
    private var task: MutableMap<UUID, BukkitTask> = mutableMapOf()

    fun paintKommand() {
        getInstance().kommand {
            register("paint") {
                requires { playerOrNull != null && isOp }
                executes {
                    player.sendMessage(text("??????????????? ??????!"))

                    config.set("${player.uniqueId}.PaintMod", true)
                    config.set("${player.uniqueId}.Page", 1)
                    config.set("${player.uniqueId}.Pen",false)

                    player.inventory.heldItemSlot = 0
                    val t = scheduler.runTaskTimer(getInstance(), Runnable {
                        val flag = config.getBoolean("${player.uniqueId}.PaintMod")

                        if(flag) {
                            val page = config.getInt("${player.uniqueId}.Page")

                            when (page) {
                                1 -> Page1.forEach { x -> player.inventory.setItem(x.key, x.value) }
                                2 -> Page2.forEach { x -> player.inventory.setItem(x.key, x.value) }
                                3 -> Page3.forEach { x -> player.inventory.setItem(x.key, x.value) }
                                4 -> Page4.forEach { x -> player.inventory.setItem(x.key, x.value) }
                                5 -> Page5.forEach { x -> player.inventory.setItem(x.key, x.value) }
                                6 -> Page6.forEach { x -> player.inventory.setItem(x.key, x.value) }
                            }

                            val slot = player.inventory.heldItemSlot
                            val slotItem = player.inventory.getItem(slot)

                            if (slotItem != null) {
                                if (slotItem.type == Material.BARRIER) {
                                    config.set("${player.uniqueId}.PaintMod", false)
                                    config.set("${player.uniqueId}.Pen",false)
                                    player.sendMessage("??????????????? ??????!")
                                    player.inventory.clear()
                                    scheduler.cancelTask(task[player.uniqueId]!!.taskId)
                                } else if (slotItem.type == Material.PAPER) {
                                    if (page >= 6) config.set("${player.uniqueId}.Page", 1)
                                    else config.set("${player.uniqueId}.Page", page + 1)
                                    player.inventory.heldItemSlot = 0
                                } else if (slotItem.type.isBlock) {
                                    config.set("${player.uniqueId}.Block", slotItem)
                                    player.sendMessage("???????????? ${slotItem.i18NDisplayName}??? ??????!")
                                    player.inventory.heldItemSlot = 0
                                }
                            }


                            getInstance().saveConfig()
                        }
                    }, 0L ,0L)
                    task[player.uniqueId] = t
                }
            }
        }
    }
}