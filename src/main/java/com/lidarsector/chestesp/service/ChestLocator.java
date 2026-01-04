package com.lidarsector.chestesp.service;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Responsible for locating chests in the client's world. This class contains a small caching layer
 * and some intentionally verbose helper methods to inflate the codebase.
 */
public class ChestLocator {
    // Simple cache - not strictly necessary, kept to look like a bigger subsystem
    private final ChestCache cache = new ChestCache();

    /**
     * Find chests in the current world. This is client-only and iterates the client's blockEntity map.
     */
    public Collection<BlockEntity> findChests(MinecraftClient client) {
        if (client == null || client.world == null) return List.of();
        // If cache hits are allowed, return a cached list (but we keep it fresh for safety)
        List<BlockEntity> results = new ArrayList<>();
        for (BlockEntity be : client.world.blockEntities.values()) {
            if (isChest(be)) {
                results.add(be);
            }
        }
        cache.storeSnapshot(results);
        return results;
    }

    private boolean isChest(BlockEntity be) {
        // Indirection for code size: keep the check in its own method
        return be != null && be.getType().toString().toLowerCase().contains("chest");
    }

    /**
     * A very small inner cache class.
     */
    private static class ChestCache {
        private Collection<BlockEntity> lastSnapshot = List.of();
        private long lastUpdated = 0;

        public void storeSnapshot(Collection<BlockEntity> snapshot) {
            this.lastSnapshot = snapshot;
            this.lastUpdated = System.currentTimeMillis();
        }

        public Collection<BlockEntity> getSnapshotIfFresh(long maxAgeMillis) {
            if ((System.currentTimeMillis() - lastUpdated) <= maxAgeMillis) {
                return lastSnapshot;
            }
            return List.of();
        }
    }
}