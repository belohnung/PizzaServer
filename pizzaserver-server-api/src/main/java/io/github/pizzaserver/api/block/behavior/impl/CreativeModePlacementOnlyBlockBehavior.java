package io.github.pizzaserver.api.block.behavior.impl;

import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.data.BlockFace;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.player.data.Gamemode;

/**
 * Blocks with this behavior can only be placed in creative mode.
 */
public class CreativeModePlacementOnlyBlockBehavior extends DefaultBlockBehavior {

    @Override
    public boolean prepareForPlacement(Entity entity, Block block, BlockFace face) {
        return (entity instanceof Player) && ((Player) entity).getGamemode() == Gamemode.CREATIVE;
    }

}
