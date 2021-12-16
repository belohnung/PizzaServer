package io.github.pizzaserver.server.player;

import com.nukkitx.protocol.bedrock.data.LevelEventType;
import com.nukkitx.protocol.bedrock.packet.LevelEventPacket;
import io.github.pizzaserver.api.item.ItemStack;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.utils.BlockLocation;

import java.util.Optional;

public class PlayerBlockBreakingManager {

    private final Player player;

    private BlockLocation blockMiningLocation;
    private long startBlockBreakTick;
    private long endBlockBreakTick;

    private int breakTicks;


    public PlayerBlockBreakingManager(Player player) {
        this.player = player;
    }

    public Optional<Block> getBlock() {
        if (this.blockMiningLocation != null) {
            return Optional.of(this.blockMiningLocation.getBlock());
        } else {
            return Optional.empty();
        }
    }

    public boolean isBreakingBlock() {
        return this.getBlock().isPresent();
    }

    public void breakBlock() {
        Block block = this.blockMiningLocation.getBlock();

        if (block.getBlockEntity() != null) {
            block.getBlockEntity().onBreak(this.player);
        }
        this.player.getWorld().setAndUpdateBlock(block.getBlockState().getResultBlock(), block.getLocation().toVector3i());
        this.resetMiningData();
    }

    public boolean canBreakBlock() {
        // Due to issues with latency, we require at least 80% of the ticks to have ticked in order to break the block.
        // We cannot go 100% because it can cause visual annoyances for the client.
        // (this is because of the delay between the start break action being retrieved and the client thinking it broke the block)
        long ticksRequiredToBreakBlock = (long) ((this.endBlockBreakTick - this.startBlockBreakTick) * 0.8) + this.startBlockBreakTick;
        boolean enoughTicksPassed = this.player.getServer().getTick() >= ticksRequiredToBreakBlock;

        return this.isBreakingBlock() && enoughTicksPassed;
    }

    /**
     * Retrieve the amount of ticks it would take to break the current block the player is mining.
     * @return the amount of ticks or -1 if the player is not breaking a block
     */
    public int getBreakTicks() {
        if (this.getBlock().isPresent()) {
            Block block = this.getBlock().get();
            ItemStack heldItem = this.player.getInventory().getHeldItem();

            boolean isCorrectTool = heldItem.getItemType().getToolType().isCorrectTool(block);
            float breakTime = block.getBlockState().getToughness() * (isCorrectTool ? 1.5f : 5f);

            boolean isBestTool = heldItem.getItemType().getToolType().isBestTool(block);
            if (isBestTool) {
                breakTime /= this.player.getInventory().getHeldItem().getItemType().getToolStrength(block);
            }
            // TODO: haste/mining fatigue checks
            // TODO: Underwater check
            // TODO: on ground check

            return (int) Math.ceil(breakTime * 20);
        } else {
            return -1;
        }
    }

    public void startBreaking(BlockLocation blockLocation) {
        if (this.isBreakingBlock()) {
            this.stopBreaking();
        }

        this.blockMiningLocation = blockLocation;
        this.breakTicks = this.getBreakTicks();
        this.startBlockBreakTick = this.player.getServer().getTick();
        this.endBlockBreakTick = this.startBlockBreakTick + this.breakTicks;

        if (this.breakTicks > 0) {
            LevelEventPacket breakStartPacket = new LevelEventPacket();
            breakStartPacket.setType(LevelEventType.BLOCK_START_BREAK);
            breakStartPacket.setPosition(this.blockMiningLocation.toVector3f());
            breakStartPacket.setData(65535 / this.breakTicks);

            for (Player player : this.blockMiningLocation.getChunk().getViewers()) {
                player.sendPacket(breakStartPacket);
            }
        }
    }

    public void stopBreaking() {
        LevelEventPacket breakStopPacket = new LevelEventPacket();
        breakStopPacket.setType(LevelEventType.BLOCK_STOP_BREAK);
        breakStopPacket.setPosition(this.blockMiningLocation.toVector3f());
        for (Player player : this.blockMiningLocation.getChunk().getViewers()) {
            player.sendPacket(breakStopPacket);
        }

        this.resetMiningData();
    }

    public void sendUpdatedBreakProgress() {
        if (this.breakTicks > 0) {
            LevelEventPacket breakUpdatePacket = new LevelEventPacket();
            breakUpdatePacket.setType(LevelEventType.BLOCK_UPDATE_BREAK);
            breakUpdatePacket.setPosition(this.blockMiningLocation.toVector3f());
            breakUpdatePacket.setData(65535 / this.breakTicks);

            for (Player player : this.blockMiningLocation.getChunk().getViewers()) {
                player.sendPacket(breakUpdatePacket);
            }
        }
    }

    public void onChangedHeldItemWhileBreaking() {
        long ticksMinedSoFar = Math.min(this.player.getServer().getTick() - this.startBlockBreakTick, this.breakTicks);
        float blockDestructionPercentage;
        if (this.breakTicks == 0) {
            blockDestructionPercentage = 1;
        } else {
            blockDestructionPercentage = (float) ticksMinedSoFar / this.breakTicks;
        }

        this.breakTicks = this.getBreakTicks();
        this.endBlockBreakTick = this.startBlockBreakTick + (int) Math.ceil(this.breakTicks * (1 - blockDestructionPercentage));

        this.sendUpdatedBreakProgress();
    }

    private void resetMiningData() {
        this.blockMiningLocation = null;
        this.startBlockBreakTick = 0;
        this.endBlockBreakTick = 0;
        this.breakTicks = 0;
    }

}