package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.BlockID;

public class BlockLitFurnace extends BlockFurnace {

    @Override
    public String getBlockId() {
        return BlockID.LIT_FURNACE;
    }

    @Override
    public String getName() {
        return "Lit Furnace";
    }

}
