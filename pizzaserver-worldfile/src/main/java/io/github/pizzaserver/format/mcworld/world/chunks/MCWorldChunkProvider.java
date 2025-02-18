package io.github.pizzaserver.format.mcworld.world.chunks;

import com.nukkitx.nbt.NBTOutputStream;
import com.nukkitx.nbt.NbtMap;
import com.nukkitx.nbt.NbtUtils;
import io.github.pizzaserver.format.api.chunks.BedrockChunkProvider;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.iq80.leveldb.DB;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.function.Supplier;

public class MCWorldChunkProvider implements BedrockChunkProvider<MCWorldChunk> {

    protected final static int OVERWORLD_DIMENSION = 0;

    protected final DB database;


    public MCWorldChunkProvider(DB database) {
        this.database = database;
    }

    @Override
    public MCWorldChunk getChunk(int x, int z, int dimension) throws IOException {
        // First extract the chunk version
        byte[] versionKey = ifOverworld(dimension,
                () -> ChunkKey.VERSION.getLevelDBKey(x, z),
                () -> ChunkKey.VERSION.getLevelDBKeyWithDimension(x, z, dimension));
        byte[] versionData = this.database.get(versionKey);

        if (versionData == null) {
            versionData = new byte[] { (byte) 21 };
            this.database.put(versionKey, versionData);
        }
        int chunkVersion = versionData[0];

        // Extract height map and biome data
        byte[] heightAndBiomeKey = ifOverworld(dimension,
                () -> ChunkKey.DATA_2D.getLevelDBKey(x, z),
                () -> ChunkKey.DATA_2D.getLevelDBKeyWithDimension(x, z, dimension));
        byte[] heightAndBiomeData = this.database.get(heightAndBiomeKey);

        if (heightAndBiomeData == null) {
            heightAndBiomeData = new byte[768];
            this.database.put(heightAndBiomeKey, heightAndBiomeData);
        }

        // Extract block entities within this chunk
        byte[] blockEntityData = this.database.get(ifOverworld(
                dimension,
                () -> ChunkKey.BLOCK_ENTITIES.getLevelDBKey(x, z),
                () -> ChunkKey.BLOCK_ENTITIES.getLevelDBKeyWithDimension(x, z, dimension)));
        if (blockEntityData == null) {
            blockEntityData = new byte[0];  // Not all chunks will have block entities.
        }

        // Extract entities within this chunk
        byte[] entityData = this.database.get(ifOverworld(
                dimension,
                () -> ChunkKey.ENTITIES.getLevelDBKey(x, z),
                () -> ChunkKey.ENTITIES.getLevelDBKeyWithDimension(x, z, dimension)));
        if (entityData == null) {
            entityData = new byte[0];   // Not all chunks have entities.
        }

        // Extract subchunks
        byte[][] subChunks = new byte[16][];
        for (int y = 0; y < 16; y++) {
            final int subChunkY = y;
            byte[] subChunk = this.database.get(ifOverworld(
                    dimension,
                    () -> ChunkKey.SUB_CHUNK_DATA.getLevelDBKey(x, z, subChunkY),
                    () -> ChunkKey.SUB_CHUNK_DATA.getLevelDBKeyWithDimension(x, z, dimension, subChunkY)));
            if (subChunk == null) {
                subChunks[y] = new byte[]{8, 0};    // empty v8 chunk
            } else {
                subChunks[y] = subChunk;
            }
        }

        return new MCWorldChunk.Builder()
                .setX(x)
                .setZ(z)
                .setDimension(dimension)
                .setChunkVersion(chunkVersion)
                .setHeightAndBiomeData(heightAndBiomeData)
                .setBlockEntityData(blockEntityData)
                .setEntityData(entityData)
                .setSubChunks(subChunks)
                .build();
    }

    @Override
    public void saveChunk(MCWorldChunk bedrockChunk) throws IOException {
        byte[] chunkVersionKey = ifOverworld(
                bedrockChunk.getDimension(),
                () -> ChunkKey.VERSION.getLevelDBKey(bedrockChunk.getX(), bedrockChunk.getZ()),
                () -> ChunkKey.VERSION.getLevelDBKeyWithDimension(
                        bedrockChunk.getX(),
                        bedrockChunk.getZ(),
                        bedrockChunk.getDimension()));
        this.database.put(chunkVersionKey, new byte[]{ (byte) bedrockChunk.getChunkVersion() });


        // Data2D (height map + biome)
        ByteBuf heightAndBiomeBuffer = ByteBufAllocator.DEFAULT.buffer(768, 768);    // 512 + 256
        for (int height : bedrockChunk.getHeightMap()) {
            heightAndBiomeBuffer.writeShortLE(height);
        }
        heightAndBiomeBuffer.writeBytes(bedrockChunk.getBiomeData());

        byte[] heightAndBiomeData = new byte[768];
        heightAndBiomeBuffer.readBytes(heightAndBiomeBuffer);
        heightAndBiomeBuffer.release();

        byte[] data2DKey = ifOverworld(
                bedrockChunk.getDimension(),
                () -> ChunkKey.DATA_2D.getLevelDBKey(bedrockChunk.getX(), bedrockChunk.getZ()),
                () -> ChunkKey.DATA_2D.getLevelDBKeyWithDimension(
                        bedrockChunk.getX(),
                        bedrockChunk.getZ(),
                        bedrockChunk.getDimension()));
        this.database.put(data2DKey, heightAndBiomeData);


        // block entities
        ByteArrayOutputStream blockEntityOutput = new ByteArrayOutputStream();
        try (NBTOutputStream blockEntityNBTOutputStream = NbtUtils.createWriterLE(blockEntityOutput)) {
            for (NbtMap blockEntityCompound : bedrockChunk.getBlockEntities()) {
                blockEntityNBTOutputStream.writeTag(blockEntityCompound);
            }
        } catch (IOException exception) {
            throw new IOException("Failed to write block entity nbt when writing to disk", exception);
        }

        byte[] blockEntitiesKey = ifOverworld(
                bedrockChunk.getDimension(),
                () -> ChunkKey.BLOCK_ENTITIES.getLevelDBKey(bedrockChunk.getX(), bedrockChunk.getZ()),
                () -> ChunkKey.BLOCK_ENTITIES.getLevelDBKeyWithDimension(
                        bedrockChunk.getX(),
                        bedrockChunk.getZ(),
                        bedrockChunk.getDimension()));
        this.database.put(blockEntitiesKey, blockEntityOutput.toByteArray());


        // entities
        ByteArrayOutputStream entityOutput = new ByteArrayOutputStream();
        try (NBTOutputStream entityNBTOutputStream = NbtUtils.createWriterLE(entityOutput)) {
            for (NbtMap entityCompound : bedrockChunk.getEntities()) {
                entityNBTOutputStream.writeTag(entityCompound);
            }
        } catch (IOException exception) {
            throw new IOException("Failed to write entity nbt when writing to disk", exception);
        }

        byte[] entityKey = ifOverworld(
                bedrockChunk.getDimension(),
                () -> ChunkKey.ENTITIES.getLevelDBKey(bedrockChunk.getX(), bedrockChunk.getZ()),
                () -> ChunkKey.ENTITIES.getLevelDBKeyWithDimension(
                        bedrockChunk.getX(),
                        bedrockChunk.getZ(),
                        bedrockChunk.getDimension()));
        this.database.put(entityKey, entityOutput.toByteArray());


        // sub chunks
        for (int y = 0; y < 16; y++) {
            int chunkY = y;
            ByteBuf subChunkBuffer = ByteBufAllocator.DEFAULT.ioBuffer();
            try {
                bedrockChunk.getSubChunk(chunkY).serializeForDisk(subChunkBuffer);
                byte[] subChunkKey = ifOverworld(
                        bedrockChunk.getDimension(),
                        () -> ChunkKey.SUB_CHUNK_DATA.getLevelDBKey(bedrockChunk.getX(), bedrockChunk.getZ(), chunkY),
                        () -> ChunkKey.SUB_CHUNK_DATA.getLevelDBKeyWithDimension(
                                bedrockChunk.getX(),
                                bedrockChunk.getZ(),
                                bedrockChunk.getDimension(),
                                chunkY));

                byte[] subChunkData = new byte[subChunkBuffer.readableBytes()];
                subChunkBuffer.readBytes(subChunkData);
                this.database.put(subChunkKey, subChunkData);
            } finally {
                subChunkBuffer.release();
            }
        }

    }

    /**
     * Returns a chunk key depending on whether or not the dimension is an overworld.
     * @param dimension dimension we are checking
     * @param overworldCallback if this dimension is an overworld
     * @param otherCallback if this dimension isn't an overworld
     * @return bytes representative of the key
     */
    private static byte[] ifOverworld(int dimension, Supplier<byte[]> overworldCallback, Supplier<byte[]> otherCallback) {
        return dimension == OVERWORLD_DIMENSION ? overworldCallback.get() : otherCallback.get();
    }

    @Override
    public void close() throws IOException {
        this.database.close();
    }

}
