package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.descriptors.FuelItemComponent;

public class ItemCharcoal extends Item implements FuelItemComponent {

    public ItemCharcoal() {
        this(1);
    }

    public ItemCharcoal(int count) {
        super(ItemID.CHARCOAL, count);
    }

    @Override
    public String getName() {
        return "Charcoal";
    }

    @Override
    public int getFuelTicks() {
        return 1600;
    }

}
