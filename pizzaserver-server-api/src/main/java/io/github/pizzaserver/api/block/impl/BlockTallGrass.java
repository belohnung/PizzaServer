package io.github.pizzaserver.api.block.impl;

import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.BaseBlock;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.data.TallGrassType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BlockTallGrass extends BaseBlock {

    private static final List<NbtMap> BLOCK_STATES = new ArrayList<>() {
        {
            Arrays.asList("default", "tall", "fern", "snow").forEach(grassType -> this.add(NbtMap.builder()
                    .putString("tall_grass_type", grassType)
                    .build()));
        }
    };

    public BlockTallGrass() {
        this(TallGrassType.NORMAL);
    }

    public BlockTallGrass(TallGrassType tallGrassType) {
        this.setBlockState(tallGrassType.ordinal());
    }

    public TallGrassType getTallGrassType() {
        return TallGrassType.values()[this.getBlockState()];
    }

    public void setTallGrassType(TallGrassType tallGrassType) {
        this.setBlockState(tallGrassType.ordinal());
    }

    @Override
    public String getBlockId() {
        return BlockID.TALL_GRASS;
    }

    @Override
    public String getName() {
        if (this.getBlockState() == 1) {
            return "Grass";
        }
        return "Fern";
    }

    @Override
    public float getHardness() {
        return 0;
    }

    @Override
    public List<NbtMap> getNBTStates() {
        return Collections.unmodifiableList(BLOCK_STATES);
    }

    @Override
    public boolean hasCollision() {
        return false;
    }

    @Override
    public boolean isTransparent() {
        return true;
    }

    @Override
    public boolean isReplaceable() {
        return true;
    }

}
