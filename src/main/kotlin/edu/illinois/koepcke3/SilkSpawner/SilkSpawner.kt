package edu.illinois.koepcke3.SilkSpawner

import edu.illinois.koepcke3.SilkSpawner.listeners.DragonScaleListener
import edu.illinois.koepcke3.SilkSpawner.listeners.MobSpawnerHandler
import edu.illinois.koepcke3.SilkSpawner.recipes.Recipes
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.plugin.java.JavaPlugin

class SilkSpawner : JavaPlugin() {
    override fun onEnable() {
        super.onEnable()
        this.server.pluginManager.registerEvents(MobSpawnerHandler(this), this)
        this.server.pluginManager.registerEvents(DragonScaleListener(this), this)

        Recipes.init(this)
    }
    companion object {
        val name = "SilkSpawner"
        var debug = false
        fun getPluginTag(meta: ItemMeta?): List<String> {
            return meta?.lore?.filter{it.contains(name)} ?: listOf()
        }
    }
    var companion = Companion
}