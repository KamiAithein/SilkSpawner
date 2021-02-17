package edu.illinois.koepcke3.SilkSpawner.listeners

import edu.illinois.koepcke3.SilkSpawner.SilkSpawner
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.event.inventory.PrepareItemCraftEvent
import org.bukkit.inventory.ItemStack

class DragonScaleListener(private val plugin: SilkSpawner) : Listener {
    private val dragonScaleDropRate = 0.75

    ///Copy pickaxe enchantments to crafting pickaxe such that
    // no enchantments are lost when creating the dragonscale pick
    @EventHandler
    fun onPrepareItemCraft(event: PrepareItemCraftEvent) {
        if (plugin.companion.getPluginTag(event.recipe?.result?.itemMeta).any{it == "${plugin.companion.name}:dragonscale_pickaxe"}) {
            val result = event.recipe?.result?.clone()
            val meta = result?.itemMeta
            val pick = event.inventory.matrix.filter{it?.type == Material.NETHERITE_PICKAXE}[0]
            pick.enchantments.forEach{ (ench, lvl) ->meta?.addEnchant(ench, lvl, true)}
            result?.itemMeta = meta
            event.inventory.result = result
        }
    }
    ///Checks crafting of dragonscale pick against lore of ingredients, cancelling if invalid
    /// (we use flint as the dragonscale so it must contain the right lore)
    @EventHandler
    fun onCraftItem(event: CraftItemEvent) {
        val inventory = event.inventory
        val table = inventory.matrix
        val resTags = plugin.companion.getPluginTag(event.recipe.result.itemMeta)
        if (resTags.any { it == "${plugin.companion.name}:dragonscale_pickaxe" }) {
            if (table.any { slot -> slot?.itemMeta?.lore?.any { lore -> lore == "${plugin.companion.name}:dragonscale" } == true }) {
                event.whoClicked.sendMessage("crafted dragonscale!")
                return
            }
            event.whoClicked.sendMessage("must use dragonscale!")
            event.isCancelled = true
        }
    }

    ///on enderdragon death drop scale % chance
    @EventHandler
    fun onMobDeath(event: EntityDeathEvent) {
        if (event.entity.type == EntityType.ENDER_DRAGON) {
            val rand = Math.random()
            if(rand <= dragonScaleDropRate) {
                val dragonscale = ItemStack(Material.FLINT, 1)
                val meta = dragonscale.itemMeta
                meta?.setDisplayName("${ChatColor.LIGHT_PURPLE}Enderdragon Scale")
                meta?.addEnchant(Enchantment.DAMAGE_ALL, 2, true)
                val lore = meta?.lore ?: mutableListOf()
                lore.add("Still sharp!")
                lore.add("${plugin.companion.name}:dragonscale")
                meta?.lore = lore
                dragonscale.itemMeta = meta

                event.drops.add(dragonscale)
            }
        }
    }
}