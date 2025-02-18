package io.github.pizzaserver.api.block.impl;

import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.BaseBlock;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.descriptors.BlockEntityContainer;
import io.github.pizzaserver.api.blockentity.impl.BlockEntityChest;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.utils.HorizontalDirection;

import java.util.ArrayList;
import java.util.List;

public class BlockChest extends BaseBlock implements BlockEntityContainer<BlockEntityChest> {

    private static final List<NbtMap> BLOCK_STATES = new ArrayList<>() {
        {
            for (int i = 0; i < 6; i++) {
                this.add(NbtMap.builder()
                        .putInt("facing_direction", i)
                        .build());
            }
        }
    };


    public BlockChest() {
        this(HorizontalDirection.NORTH);
    }

    public BlockChest(HorizontalDirection direction) {
        this.setDirection(direction);
    }

    public HorizontalDirection getDirection() {
        return HorizontalDirection.fromBlockStateIndex(this.getBlockState());
    }

    public void setDirection(HorizontalDirection direction) {
        this.setBlockState(direction.getBlockStateIndex());
    }

    @Override
    public String getBlockId() {
        return BlockID.CHEST;
    }

    @Override
    public String getName() {
        return "Chest";
    }

    @Override
    public float getHardness() {
        return 2.5f;
    }

    @Override
    public List<NbtMap> getNBTStates() {
        return BLOCK_STATES;
    }

    @Override
    public boolean canBeMinedWithHand() {
        return true;
    }

    @Override
    public ToolType getToolTypeRequired() {
        return ToolType.AXE;
    }

    @Override
    public ToolTier getToolTierRequired() {
        return ToolTier.WOOD;
    }

}
