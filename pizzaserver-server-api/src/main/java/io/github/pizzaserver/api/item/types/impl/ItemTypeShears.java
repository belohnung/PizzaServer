package io.github.pizzaserver.api.item.types.impl;

import io.github.pizzaserver.api.item.ToolTypes;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.item.data.ToolTypeID;
import io.github.pizzaserver.api.item.types.ItemTypeID;
import io.github.pizzaserver.api.item.types.VanillaItemType;
import io.github.pizzaserver.api.item.types.components.DurableItemComponent;

public class ItemTypeShears extends VanillaItemType implements DurableItemComponent {

    @Override
    public String getItemId() {
        return ItemTypeID.SHEARS;
    }

    @Override
    public String getName() {
        return "Shears";
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public ToolType getToolType() {
        return ToolTypes.getToolType(ToolTypeID.SHEARS);
    }

    @Override
    public int getMaxDurability() {
        return 60;
    }

}