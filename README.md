# SilkSpawner
## What is it?
Silk Spawner is a spigot plugin for 1.16 that adds the ability to pick up spawners and place them while maintaining the correct mob type of the spawner. 
Spawners can be picked up by any netherite pickaxe that has been crafted with a dragon scale and has the enchantment silk touch. 
A dragon scale can be achieved through a percent drop chance when killing the ender dragon.

Spawners do not drop spawner blocks 100% of the time, there is a % chance of the spawner breaking and instead dropping the egg of the creature that was being contained.
Eggs can be used to set the spawn type of the spawner. This system is intended to incentivize and reward hunting spawners; even if the spawner breaks when you mine it, you receive a monster egg that you can use on another spawner.

## Additions
 + Ability to break mob spawners with a percent chance of dropping the mob spawner block
 + "Dragon Scale" item which is used to create a dragon scale pick, needed to craft a "Dragon scale pickaxe"
 + "Dragon scale pickaxe" item that when enchanted with silk touch has a percent chance to drop a mob spawner
 
## Developers
  + spawner drop rate set in SilkSpawner/listeners/MobSpawnerHandler
  + dragon scale drop rate set in SilkSpawner/listeners/DragonScaleListener
