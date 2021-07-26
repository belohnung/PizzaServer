package io.github.willqi.pizzaserver.server.network.protocol.packets;

import io.github.willqi.pizzaserver.server.network.protocol.data.PlayerAnimateAction;

public class PlayerAnimatePacket extends ImplBedrockPacket {

    public static final int ID = 0x2c;

    private PlayerAnimateAction action;
    private long entityRuntimeID;
    private float rowingTime;

    public PlayerAnimatePacket() {
        super(ID);
    }

    public PlayerAnimateAction getAction() {
        return action;
    }

    public void setAction(PlayerAnimateAction action) {
        this.action = action;
    }

    public long getEntityRuntimeID() {
        return entityRuntimeID;
    }

    public void setEntityRuntimeID(long entityRuntimeID) {
        this.entityRuntimeID = entityRuntimeID;
    }

    public float getRowingTime() {
        return rowingTime;
    }

    public void setRowingTime(float rowingTime) {
        this.rowingTime = rowingTime;
    }
}
