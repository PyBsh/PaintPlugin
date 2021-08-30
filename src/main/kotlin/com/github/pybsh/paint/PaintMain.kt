package com.github.pybsh.paint

import org.bukkit.plugin.java.JavaPlugin

class PaintMain : JavaPlugin() {

    companion object {
        lateinit var instance: PaintMain
            private set
    }

    override fun onEnable() {
        instance = this
        PaintKommand.paintKommand()
        server.pluginManager.registerEvents(PaintListener(), this)
        saveDefaultConfig()
    }
}