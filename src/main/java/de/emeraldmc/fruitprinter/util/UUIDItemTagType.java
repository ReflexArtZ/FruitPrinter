package de.emeraldmc.fruitprinter.util;

import org.bukkit.inventory.meta.tags.ItemTagAdapterContext;
import org.bukkit.inventory.meta.tags.ItemTagType;

import java.nio.ByteBuffer;
import java.util.UUID;

public class UUIDItemTagType implements ItemTagType<byte[], UUID> {

    @Override
    public Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @Override
    public Class<UUID> getComplexType() {
        return UUID.class;
    }

    @Override
    public byte[] toPrimitive(UUID uuid, ItemTagAdapterContext itemTagAdapterContext) {
        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.putLong(uuid.getLeastSignificantBits());
        buffer.putLong(uuid.getMostSignificantBits());
        return buffer.array();
    }

    @Override
    public UUID fromPrimitive(byte[] bytes, ItemTagAdapterContext itemTagAdapterContext) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        long leastBits = buffer.getLong();
        long mostBits = buffer.getLong();
        return new UUID(mostBits, leastBits);
    }
}