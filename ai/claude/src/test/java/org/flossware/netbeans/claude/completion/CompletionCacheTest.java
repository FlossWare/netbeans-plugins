/* Copyright 2026 FlossWare. */
package org.flossware.netbeans.claude.completion;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Arrays;
import static org.assertj.core.api.Assertions.*;

class CompletionCacheTest {

    @Test
    void testGetInstance() {
        CompletionCache cache = CompletionCache.getInstance();
        assertThat(cache).isNotNull();
    }

    @Test
    void testGetInstance_ReturnsSameInstance() {
        CompletionCache cache1 = CompletionCache.getInstance();
        CompletionCache cache2 = CompletionCache.getInstance();
        assertThat(cache1).isSameAs(cache2);
    }

    @Test
    void testPut_And_Get() {
        CompletionCache cache = CompletionCache.getInstance();
        String key = "testKey";
        ClaudeCompletionItem item = new ClaudeCompletionItem("code", "pre", 10, 0);
        List<ClaudeCompletionItem> items = Arrays.asList(item);

        cache.put(key, items);
        List<ClaudeCompletionItem> result = cache.get(key);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
    }

    @Test
    void testGet_NonExistentKey() {
        CompletionCache cache = CompletionCache.getInstance();
        List<ClaudeCompletionItem> result = cache.get("nonExistentKey");
        assertThat(result).isNull();
    }

    @Test
    void testClear() {
        CompletionCache cache = CompletionCache.getInstance();
        ClaudeCompletionItem item = new ClaudeCompletionItem("code", "pre", 10, 0);
        cache.put("key1", Arrays.asList(item));
        cache.clear();

        List<ClaudeCompletionItem> result = cache.get("key1");
        assertThat(result).isNull();
    }

    @Test
    void testPut_Overwrites() {
        CompletionCache cache = CompletionCache.getInstance();
        String key = "sameKey";
        ClaudeCompletionItem item1 = new ClaudeCompletionItem("code1", "pre", 10, 0);
        ClaudeCompletionItem item2 = new ClaudeCompletionItem("code2", "pre", 10, 0);

        cache.put(key, Arrays.asList(item1));
        cache.put(key, Arrays.asList(item2));

        List<ClaudeCompletionItem> result = cache.get(key);
        assertThat(result.get(0).getSortText()).isEqualTo("code2");
    }

    @Test
    void testMultipleKeys() {
        CompletionCache cache = CompletionCache.getInstance();
        cache.clear(); // Start fresh

        ClaudeCompletionItem item1 = new ClaudeCompletionItem("code1", "pre", 10, 0);
        ClaudeCompletionItem item2 = new ClaudeCompletionItem("code2", "pre", 10, 0);
        ClaudeCompletionItem item3 = new ClaudeCompletionItem("code3", "pre", 10, 0);

        cache.put("key1", Arrays.asList(item1));
        cache.put("key2", Arrays.asList(item2));
        cache.put("key3", Arrays.asList(item3));

        assertThat(cache.get("key1").get(0).getSortText()).isEqualTo("code1");
        assertThat(cache.get("key2").get(0).getSortText()).isEqualTo("code2");
        assertThat(cache.get("key3").get(0).getSortText()).isEqualTo("code3");
    }
}
