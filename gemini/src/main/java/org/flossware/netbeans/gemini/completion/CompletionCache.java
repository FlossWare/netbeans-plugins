package org.flossware.netbeans.gemini.completion;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Cache for completion results to avoid excessive API calls
 */
public class CompletionCache {

    private static CompletionCache instance;
    private final Map<String, CacheEntry> cache;

    private CompletionCache() {
        this.cache = new ConcurrentHashMap<>();
        startCleanupTask();
    }

    public static synchronized CompletionCache getInstance() {
        if (instance == null) {
            instance = new CompletionCache();
        }
        return instance;
    }

    /**
     * Get cached completion items
     */
    public List<GeminiCompletionItem> get(String key) {
        CacheEntry entry = cache.get(key);
        if (entry == null) {
            return null;
        }

        // Check if expired
        if (entry.isExpired()) {
            cache.remove(key);
            return null;
        }

        return entry.getItems();
    }

    /**
     * Put completion items in cache
     */
    public void put(String key, List<GeminiCompletionItem> items) {
        int ttlSeconds = GeminiCompletionSettings.getCacheTTLSeconds();
        cache.put(key, new CacheEntry(items, ttlSeconds));

        // Limit cache size
        if (cache.size() > 100) {
            cleanupOldEntries();
        }
    }

    /**
     * Clear all cached items
     */
    public void clear() {
        cache.clear();
    }

    /**
     * Remove expired entries
     */
    private void cleanupOldEntries() {
        cache.entrySet().removeIf(entry -> entry.getValue().isExpired());
    }

    /**
     * Start background cleanup task
     */
    private void startCleanupTask() {
        Thread cleanupThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(60000); // Clean every minute
                    cleanupOldEntries();
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        cleanupThread.setDaemon(true);
        cleanupThread.setName("Gemini-Completion-Cache-Cleanup");
        cleanupThread.start();
    }

    /**
     * Cache entry with expiration
     */
    private static class CacheEntry {
        private final List<GeminiCompletionItem> items;
        private final long expirationTime;

        public CacheEntry(List<GeminiCompletionItem> items, int ttlSeconds) {
            this.items = items;
            this.expirationTime = System.currentTimeMillis() + (ttlSeconds * 1000L);
        }

        public List<GeminiCompletionItem> getItems() {
            return items;
        }

        public boolean isExpired() {
            return System.currentTimeMillis() > expirationTime;
        }
    }
}
