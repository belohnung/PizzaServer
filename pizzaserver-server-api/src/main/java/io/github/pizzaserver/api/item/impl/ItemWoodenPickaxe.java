package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.item.descriptors.ToolItemComponent;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.descriptors.DurableItemComponent;

public class ItemWoodenPickaxe extends Item implements DurableItemComponent, ToolItemComponent {

    public ItemWoodenPickaxe() {
        this(1);
    }

    public ItemWoodenPickaxe(int count) {
        this(count, 0);
    }

    public ItemWoodenPickaxe(int count, int meta) {
        super(ItemID.WOODEN_PICKAXE, count, meta);
    }

    @Override
    public String getName() {
        return "Wooden Pickaxe";
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public int getDamage() {
        return 2;
    }

    @Override
    public ToolType getToolType() {
        return ToolType.PICKAXE;
    }

    @Override
    public ToolTier getToolTier() {
        return ToolTier.WOOD;
    }

    @Override
    public int getMaxDurability() {
        return 60;
    }

}
