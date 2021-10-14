package io.github.willqi.pizzaserver.api.event.type.block;

import io.github.willqi.pizzaserver.api.level.world.blocks.Block;
import io.github.willqi.pizzaserver.api.player.Player;

/**
 * Called when a player begins to break a block.
 */
public class BlockStartBreakEvent extends BaseBlockEvent.Cancellable {

    protected Player player;

    public BlockStartBreakEvent(Player player, Block block) {
        super(block);
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }

}