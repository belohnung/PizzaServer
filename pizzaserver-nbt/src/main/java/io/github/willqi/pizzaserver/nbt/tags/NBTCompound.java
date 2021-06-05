package io.github.willqi.pizzaserver.nbt.tags;

import io.github.willqi.pizzaserver.nbt.exceptions.NBTLimitException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class NBTCompound extends NBTTag implements NBTContainer {

    public static final byte ID = 10;

    private final Map<String, NBTTag> data = new HashMap<>();
    private int depth;


    public NBTCompound() {}

    public NBTCompound(String name) {
        super(name);
    }


    @Override
    public int getId() {
        return ID;
    }

    public NBTByte getByte(String name) {
        return (NBTByte)this.data.get(name);
    }

    public NBTShort getShort(String name) {
        return (NBTShort)this.data.get(name);
    }

    public NBTInteger getInteger(String name) {
        return (NBTInteger)this.data.get(name);
    }

    public NBTLong getLong(String name) {
        return (NBTLong)this.data.get(name);
    }

    public NBTFloat getFloat(String name) {
        return (NBTFloat)this.data.get(name);
    }

    public NBTDouble getDouble(String name) {
        return (NBTDouble)this.data.get(name);
    }

    public NBTCompound getCompound(String name) {
        return (NBTCompound)this.data.get(name);
    }

    public NBTCompound put(String name, NBTTag tag) throws NBTLimitException {
        this.data.put(name, tag);
        if (tag instanceof NBTContainer) {
            ((NBTContainer)tag).setDepth(this.getDepth() + 1);
        }
        return this;
    }

    public NBTTag get(String name) {
        return this.data.get(name);
    }

    public Set<String> keySet() {
        return this.data.keySet();
    }

    @Override
    public int getDepth() {
        return this.depth;
    }

    @Override
    public void setDepth(int depth) {
        this.depth = depth;
        if (this.depth > 512) {
            throw new NBTLimitException("Reached maximum depth of 512.");
        }
        this.data.forEach((name, tag) -> {
            if (tag instanceof NBTContainer) {
                ((NBTContainer)tag).setDepth(this.getDepth() + 1);
            }
        });
    }

}