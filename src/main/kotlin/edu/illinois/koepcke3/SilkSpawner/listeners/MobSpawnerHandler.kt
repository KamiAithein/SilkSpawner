package edu.illinois.koepcke3.SilkSpawner.listeners

import edu.illinois.koepcke3.SilkSpawner.SilkSpawner
import edu.illinois.koepcke3.util.EntityMapper
import org.bukkit.Material
import org.bukkit.Tag
import org.bukkit.block.CreatureSpawner
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

class MobSpawnerHandler(private val plugin: SilkSpawner) : Listener {
    private fun CreatureSpawner.getSilkSpawnerLore(): List<String> = listOf("${plugin.companion.name}:${this.spawnedType}")
    private fun Player.sendMessageDebug(message: String): Unit = if(plugin.companion.debug) this.sendMessage(message) else Unit

    private val spawnerDropRate = 0.33F //eggDropRate = 1-0.33=0.67

    /**
     * @param tool the used to break an item
     * @return true iff tool should be able to break and drop spawner
     */
    private fun toolCanBreakSpawner(tool: ItemStack): Boolean {
        val tags = plugin.companion.getPluginTag(tool.itemMeta)
        return tags.any{it == "${plugin.companion.name}:dragonscale_pickaxe"} && tool.containsEnchantment(Enchantment.SILK_TOUCH)
    }

    ///spawner break check
    /// must encode spawner information into spawner block item to remember spawner type
    @EventHandler
    fun onBlockBreakEvent(event: BlockBreakEvent) {
        val block = event.block
        val toolCanBreak = toolCanBreakSpawner(event.player.inventory.itemInMainHand)
        event.player.sendMessageDebug("$toolCanBreak")

        if(block.type == Material.SPAWNER && toolCanBreak) {
            val drop: ItemStack //egg or spawner
            val spawner = block.state as CreatureSpawner
            event.player.sendMessageDebug("broken")

            val chanceSuccessBreak = Math.random()
            if(chanceSuccessBreak <= spawnerDropRate) {
                drop = ItemStack(Material.SPAWNER, 1)
                val dropMeta = drop.itemMeta
                dropMeta?.lore = spawner.getSilkSpawnerLore() //labels with spawner type for place event
                drop.itemMeta = dropMeta
            } else {
                val dropEntityEgg = EntityMapper.entityToEgg.getOrDefault(spawner.spawnedType, Material.PIG_SPAWN_EGG)
                drop = ItemStack(dropEntityEgg, 1)
            }
            spawner.world.dropItemNaturally(spawner.location, drop)
            event.player.sendMessageDebug("spawned")
        }
    }
    ///sets correct spawner type based on mob type encoded in lore of spawner block item
    @EventHandler
    fun onBlockPlaceEvent(event: BlockPlaceEvent) {
        event.player.sendMessageDebug("block placed!")
        val block = event.blockPlaced
        val blockItem = event.itemInHand
        if(block.type == Material.SPAWNER) {
            event.player.sendMessageDebug("spawner placed!")
            val spawner = block.state as CreatureSpawner
            //is modified by this plugin
            val modifiedLore = plugin.companion.getPluginTag(blockItem.itemMeta)
            if(!modifiedLore.isNullOrEmpty()) {
                event.player.sendMessageDebug("found valid lore!")
                event.player.sendMessageDebug(modifiedLore[0])
                val entityStr = modifiedLore[0].split(":")[1]
                spawner.spawnedType = EntityType.valueOf(entityStr)
                spawner.update(true)
            }
        }
    }

}