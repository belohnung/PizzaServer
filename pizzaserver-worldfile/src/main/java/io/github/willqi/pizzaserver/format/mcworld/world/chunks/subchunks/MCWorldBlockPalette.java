package io.github.willqi.pizzaserver.format.mcworld.world.chunks.subchunks;

import io.github.willqi.pizzaserver.format.api.chunks.subchunks.BlockPalette;
import io.github.willqi.pizzaserver.format.BlockRuntimeMapper;
import io.github.willqi.pizzaserver.format.exceptions.world.chunks.ChunkParseException;
import io.github.willqi.pizzaserver.format.mcworld.utils.VarInts;
import io.github.willqi.pizzaserver.nbt.streams.le.LittleEndianDataInputStream;
import io.github.willqi.pizzaserver.nbt.streams.le.LittleEndianDataOutputStream;
import io.github.willqi.pizzaserver.nbt.streams.nbt.NBTInputStream;
import io.github.willqi.pizzaserver.nbt.streams.nbt.NBTOutputStream;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;
import io.github.willqi.pizzaserver.nbt.tags.NBTInteger;
import io.github.willqi.pizzaserver.nbt.tags.NBTString;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MCWorldBlockPalette implements BlockPalette {

    private final List<Entry> data = new ArrayList<>();


    /**
     * Entries require the following 3 properties
     * NBTString property: name
     * NBTInteger property: version (You can fill this out with zero when serializing to the network)
     * NBTCompound property: states (the block state)
     * @param data
     */
    @Override
    public void add(NBTCompound data) {
        this.data.add(new MCWorldEntry(data, this.data.size()));
    }

    @Override
    public List<Entry> getAllEntries() {
        return Collections.unmodifiableList(data);
    }

    @Override
    public boolean hasEntry(NBTCompound data) {
        Entry entry = new MCWorldEntry(data, this.data.size());
        for (Entry storedEntry : this.getAllEntries()) {
            if (storedEntry.getId().equals(entry.getId()) && storedEntry.getState().equals(entry.getState())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Entry getEntry(int index) {
        return this.data.get(index);
    }

    @Override
    public byte[] serializeForDisk() throws IOException {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        buffer.writeIntLE(this.data.size());
        NBTOutputStream outputStream = new NBTOutputStream(new LittleEndianDataOutputStream(new ByteBufOutputStream(buffer)));
        for (BlockPalette.Entry data : this.getAllEntries()) {
            NBTCompound compound = new NBTCompound();
            compound.put("name", new NBTString("name", data.getId()))
                    .put("version", new NBTInteger("version", data.getVersion()))
                    .put("states", data.getState());

            outputStream.writeCompound(compound);
        }
        byte[] serialized = new byte[buffer.readableBytes()];
        buffer.readBytes(serialized);
        buffer.release();

        return serialized;
    }

    @Override
    public byte[] serializeForNetwork(BlockRuntimeMapper runtimeMapper) {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        VarInts.writeInt(buffer, this.getAllEntries().size());
        for (BlockPalette.Entry data : this.getAllEntries()) {
            int id = runtimeMapper.getBlockRuntimeId(data.getId(), data.getState());
            VarInts.writeInt(buffer, id);
        }
        byte[] serialized = new byte[buffer.readableBytes()];
        buffer.readBytes(serialized);
        buffer.release();

        return serialized;
    }

    /**
     * Retrieve a block palette from chunk data at the correct index
     * @param buffer
     * @throws IOException
     */
    public void parse(ByteBuf buffer) throws IOException {
        int paletteLength = buffer.readIntLE();
        NBTInputStream inputStream = new NBTInputStream(new LittleEndianDataInputStream(new ByteBufInputStream(buffer)));
        try {
            for (int i = 0; i < paletteLength; i++) {
                NBTCompound compound = inputStream.readCompound();
                this.add(compound);
            }
        } catch (IOException exception) {
            throw new ChunkParseException("Failed to parse chunk palette.", exception);
        }
    }


    public static class MCWorldEntry implements Entry {

        private final String name;
        private final int id;
        private final int version;
        private final NBTCompound state;


        public MCWorldEntry(NBTCompound data, int id) {
            this.name = data.getString("name").getValue();
            this.version = data.getInteger("version").getValue();
            this.state = data.getCompound("states");
            this.id = id;
        }

        public String getId() {
            return this.name;
        }

        public int getPaletteIndex() {
            return this.id;
        }

        public int getVersion() {
            return this.version;
        }

        public NBTCompound getState() {
            return this.state;
        }


    }

}
