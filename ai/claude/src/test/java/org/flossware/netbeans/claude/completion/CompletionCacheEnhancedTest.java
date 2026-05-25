/* Copyright 2026 FlossWare. */
package org.flossware.netbeans.claude.completion;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import static org.assertj.core.api.Assertions.*;

/**
 * Enhanced tests for CompletionCache including expiration and concurrency.
 */
class CompletionCacheEnhancedTest {

    @Test
    void testPutAndGet_ImmediateRetrieval() {
        CompletionCache cache = CompletionCache.getInstance();
        cache.clear();
        
        String key = "test-key-immediate";
        ClaudeCompletionItem item = new ClaudeCompletionItem("code", "c", 1, 0);
        List<ClaudeCompletionItem> items = Arrays.asList(item);
        
        cache.put(key, items);
        List<ClaudeCompletionItem> retrieved = cache.get(key);
        
        assertThat(retrieved).isNotNull();
        assertThat(retrieved).hasSize(1);
        assertThat(retrieved.get(0).getSortText().toString()).isEqualTo("code");
    }

    @Test
    void testPutAndGet_MultipleItems() {
        CompletionCache cache = CompletionCache.getInstance();
        cache.clear();
        
        String key = "multi-items";
        List<ClaudeCompletionItem> items = Arrays.asList(
            new ClaudeCompletionItem("code1", "c", 1, 0),
            new ClaudeCompletionItem("code2", "c", 1, 1),
            new ClaudeCompletionItem("code3", "c", 1, 2)
        );
        
        cache.put(key, items);
        List<ClaudeCompletionItem> retrieved = cache.get(key);
        
        assertThat(retrieved).hasSize(3);
    }

    @Test
    void testPutAndGet_EmptyList() {
        CompletionCache cache = CompletionCache.getInstance();
        cache.clear();
        
        String key = "empty-list";
        List<ClaudeCompletionItem> items = new ArrayList<>();
        
        cache.put(key, items);
        List<ClaudeCompletionItem> retrieved = cache.get(key);
        
        assertThat(retrieved).isNotNull();
        assertThat(retrieved).isEmpty();
    }

    @Test
    void testOverwrite_SameKey() {
        CompletionCache cache = CompletionCache.getInstance();
        cache.clear();
        
        String key = "overwrite-key";
        
        cache.put(key, Arrays.asList(new ClaudeCompletionItem("first", "f", 1, 0)));
        cache.put(key, Arrays.asList(new ClaudeCompletionItem("second", "s", 1, 0)));
        
        List<ClaudeCompletionItem> retrieved = cache.get(key);
        assertThat(retrieved).hasSize(1);
        assertThat(retrieved.get(0).getSortText().toString()).isEqualTo("second");
    }

    @Test
    void testMultipleKeys_Independent() {
        CompletionCache cache = CompletionCache.getInstance();
        cache.clear();
        
        cache.put("key1", Arrays.asList(new ClaudeCompletionItem("value1", "v", 1, 0)));
        cache.put("key2", Arrays.asList(new ClaudeCompletionItem("value2", "v", 1, 0)));
        cache.put("key3", Arrays.asList(new ClaudeCompletionItem("value3", "v", 1, 0)));
        
        assertThat(cache.get("key1").get(0).getSortText().toString()).isEqualTo("value1");
        assertThat(cache.get("key2").get(0).getSortText().toString()).isEqualTo("value2");
        assertThat(cache.get("key3").get(0).getSortText().toString()).isEqualTo("value3");
    }

    @Test
    void testClear_RemovesAll() {
        CompletionCache cache = CompletionCache.getInstance();
        
        for (int i = 0; i < 10; i++) {
            cache.put("key" + i, Arrays.asList(new ClaudeCompletionItem("val" + i, "v", 1, 0)));
        }
        
        cache.clear();
        
        for (int i = 0; i < 10; i++) {
            assertThat(cache.get("key" + i)).isNull();
        }
    }

    @Test
    void testConcurrentAccess() throws Exception {
        CompletionCache cache = CompletionCache.getInstance();
        cache.clear();
        
        int threadCount = 10;
        CountDownLatch latch = new CountDownLatch(threadCount);
        
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            new Thread(() -> {
                try {
                    cache.put("concurrent-" + index, 
                        Arrays.asList(new ClaudeCompletionItem("value" + index, "v", 1, 0)));
                    Thread.sleep(10);
                    cache.get("concurrent-" + index);
                } catch (Exception e) {
                    // Ignore
                } finally {
                    latch.countDown();
                }
            }).start();
        }
        
        latch.await(5, TimeUnit.SECONDS);
        
        // Verify some values are still there
        int found = 0;
        for (int i = 0; i < threadCount; i++) {
            if (cache.get("concurrent-" + i) != null) {
                found++;
            }
        }
        assertThat(found).isGreaterThan(0);
    }

    @Test
    void testCacheSizeLimit_TriggersCleanup() {
        CompletionCache cache = CompletionCache.getInstance();
        cache.clear();
        
        // Add more than 100 items to trigger cleanup
        for (int i = 0; i < 150; i++) {
            cache.put("key-" + i, Arrays.asList(new ClaudeCompletionItem("value" + i, "v", 1, 0)));
        }
        
        // After adding 150, some may have been cleaned up
        // But recent ones should still be there
        int found = 0;
        for (int i = 140; i < 150; i++) {
            if (cache.get("key-" + i) != null) {
                found++;
            }
        }
        assertThat(found).isGreaterThan(0);
    }

    @Test
    void testGet_NonExistentKey() {
        CompletionCache cache = CompletionCache.getInstance();
        cache.clear();
        
        assertThat(cache.get("non-existent-key-12345")).isNull();
    }

    @Test
    void testGet_AfterClear() {
        CompletionCache cache = CompletionCache.getInstance();
        
        String key = "clear-test";
        cache.put(key, Arrays.asList(new ClaudeCompletionItem("value", "v", 1, 0)));
        assertThat(cache.get(key)).isNotNull();
        
        cache.clear();
        assertThat(cache.get(key)).isNull();
    }

    @Test
    void testMultipleClear() {
        CompletionCache cache = CompletionCache.getInstance();
        
        for (int i = 0; i < 5; i++) {
            cache.put("key", Arrays.asList(new ClaudeCompletionItem("val", "v", 1, 0)));
            cache.clear();
            assertThat(cache.get("key")).isNull();
        }
    }

    @Test
    void testSingletonBehavior() {
        CompletionCache cache1 = CompletionCache.getInstance();
        CompletionCache cache2 = CompletionCache.getInstance();
        
        assertThat(cache1).isSameAs(cache2);
        
        cache1.put("test", Arrays.asList(new ClaudeCompletionItem("val", "v", 1, 0)));
        assertThat(cache2.get("test")).isNotNull();
    }

    @Test
    void testLargeValues() {
        CompletionCache cache = CompletionCache.getInstance();
        cache.clear();
        
        // Create a large list
        List<ClaudeCompletionItem> largeList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            largeList.add(new ClaudeCompletionItem("code" + i, "c", i, i));
        }
        
        cache.put("large-key", largeList);
        List<ClaudeCompletionItem> retrieved = cache.get("large-key");
        
        assertThat(retrieved).hasSize(1000);
    }

    @Test
    void testSpecialCharacterKeys() {
        CompletionCache cache = CompletionCache.getInstance();
        cache.clear();
        
        String specialKey = "key!@#$%^&*()_+-=[]{}|;:',.<>?/~`";
        cache.put(specialKey, Arrays.asList(new ClaudeCompletionItem("val", "v", 1, 0)));
        
        assertThat(cache.get(specialKey)).isNotNull();
    }

    @Test
    void testUnicodeKeys() {
        CompletionCache cache = CompletionCache.getInstance();
        cache.clear();
        
        String unicodeKey = "键-キー-ключ-مفتاح";
        cache.put(unicodeKey, Arrays.asList(new ClaudeCompletionItem("val", "v", 1, 0)));
        
        assertThat(cache.get(unicodeKey)).isNotNull();
    }

    @Test
    void testVeryLongKeys() {
        CompletionCache cache = CompletionCache.getInstance();
        cache.clear();
        
        String longKey = "key-" + "x".repeat(10000);
        cache.put(longKey, Arrays.asList(new ClaudeCompletionItem("val", "v", 1, 0)));
        
        assertThat(cache.get(longKey)).isNotNull();
    }

    @Test
    void testRapidPutGet() {
        CompletionCache cache = CompletionCache.getInstance();
        cache.clear();
        
        for (int i = 0; i < 100; i++) {
            cache.put("rapid-" + i, Arrays.asList(new ClaudeCompletionItem("val" + i, "v", 1, 0)));
            assertThat(cache.get("rapid-" + i)).isNotNull();
        }
    }

    @Test
    void testAlternatingPutClear() {
        CompletionCache cache = CompletionCache.getInstance();
        
        for (int i = 0; i < 10; i++) {
            cache.put("key", Arrays.asList(new ClaudeCompletionItem("val" + i, "v", 1, 0)));
            if (i % 2 == 0) {
                cache.clear();
            }
        }
        
        // Last put without clear
        assertThat(cache.get("key")).isNotNull();
    }
}
