package io.github.willqi.pizzaserver.server.network.protocol.data;

public enum PlayerAction {
    START_BREAK,
    ABORT_BREAK,
    STOP_BREAK,
    GET_UPDATED_BLOCK,
    DROP_ITEM,
    START_SLEEPING,
    STOP_SLEEPING,
    RESPAWN,
    JUMP,
    START_SPRINT,
    STOP_SPRINT,
    START_SNEAK,
    STOP_SNEAK,
    DIMENSION_CHANGE_REQUEST,
    DIMENSION_CHANGE_ACK,
    START_GLIDE,
    STOP_GLIDE,
    BUILD_DENIED,
    CONTINUE_BREAK,
    SET_ENCHANTMENT_SEED,
    START_SWIMMING,
    STOP_SWIMMING,
    START_SPIN_ATTACK,
    STOP_SPIN_ATTACK,
    INTERACT_BLOCK,
    BLOCK_PREDICT_DESTROY,
    BLOCK_CONTINUE_DESTROY;
}