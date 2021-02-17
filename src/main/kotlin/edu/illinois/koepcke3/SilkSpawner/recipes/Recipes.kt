package edu.illinois.koepcke3.SilkSpawner.recipes

import edu.illinois.koepcke3.SilkSpawner.SilkSpawner
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.ShapelessRecipe

/**
 * Object containing recipes, must be initialized before use
 */
object Recipes {

    /**
     * must be run at least once before object usage
     */
    fun init(plugin: SilkSpawner) {
        //Setting a recipe is a process so we store the recipes as a string:supplier pair such that
        //  the suppliers can be run easily while still maintaining some semblance of encapsulation
        val rep = mapOf(
            Pair("dragonscale_pickaxe", {
                val name = "dragonscale_pickaxe"
                val item = ItemStack(Material.NETHERITE_PICKAXE)
                val meta = item.itemMeta
                val lore = meta?.lore ?: mutableListOf()
                lore.add("Reinforced with Enderdragon scales")
                lore.add("If combined with Silk Touch,")
                lore.add("this could probably pick up ${ChatColor.YELLOW}anything!")
                lore.add("...probably")
                lore.add("${plugin.companion.name}:${name}")
                meta?.lore = lore
                item.itemMeta = meta

                val key = NamespacedKey(plugin, name)

                val recipe = ShapelessRecipe(key, item)

                recipe.addIngredient(Material.FLINT)
                recipe.addIngredient(Material.NETHERITE_PICKAXE)

                Bukkit.addRecipe(recipe)
            })
        )
        rep.forEach{it.value.invoke()}
    }
}