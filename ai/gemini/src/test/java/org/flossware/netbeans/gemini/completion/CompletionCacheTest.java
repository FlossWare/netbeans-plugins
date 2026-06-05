/*
 * Copyright 2026 FlossWare.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.flossware.netbeans.gemini.completion;

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
        String key = "geminiTestKey";
        GeminiCompletionItem item = new GeminiCompletionItem("code", "pre", 10, 0);
        List<GeminiCompletionItem> items = Arrays.asList(item);

        cache.put(key, items);
        List<GeminiCompletionItem> result = cache.get(key);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
    }

    @Test
    void testGet_NonExistentKey() {
        CompletionCache cache = CompletionCache.getInstance();
        List<GeminiCompletionItem> result = cache.get("nonExistentGeminiKey");
        assertThat(result).isNull();
    }

    @Test
    void testClear() {
        CompletionCache cache = CompletionCache.getInstance();
        GeminiCompletionItem item = new GeminiCompletionItem("code", "pre", 10, 0);
        cache.put("geminiKey1", Arrays.asList(item));
        cache.clear();

        List<GeminiCompletionItem> result = cache.get("geminiKey1");
        assertThat(result).isNull();
    }

    @Test
    void testPut_Overwrites() {
        CompletionCache cache = CompletionCache.getInstance();
        String key = "geminiSameKey";
        GeminiCompletionItem item1 = new GeminiCompletionItem("code1", "pre", 10, 0);
        GeminiCompletionItem item2 = new GeminiCompletionItem("code2", "pre", 10, 0);

        cache.put(key, Arrays.asList(item1));
        cache.put(key, Arrays.asList(item2));

        List<GeminiCompletionItem> result = cache.get(key);
        assertThat(result.get(0).getSortText()).isEqualTo("code2");
    }

    @Test
    void testMultipleKeys() {
        CompletionCache cache = CompletionCache.getInstance();
        cache.clear();

        GeminiCompletionItem item1 = new GeminiCompletionItem("code1", "pre", 10, 0);
        GeminiCompletionItem item2 = new GeminiCompletionItem("code2", "pre", 10, 0);
        GeminiCompletionItem item3 = new GeminiCompletionItem("code3", "pre", 10, 0);

        cache.put("gKey1", Arrays.asList(item1));
        cache.put("gKey2", Arrays.asList(item2));
        cache.put("gKey3", Arrays.asList(item3));

        assertThat(cache.get("gKey1").get(0).getSortText()).isEqualTo("code1");
        assertThat(cache.get("gKey2").get(0).getSortText()).isEqualTo("code2");
        assertThat(cache.get("gKey3").get(0).getSortText()).isEqualTo("code3");
    }

    @Test
    void testPut_EmptyList() {
        CompletionCache cache = CompletionCache.getInstance();
        String key = "emptyGeminiKey";
        cache.put(key, Arrays.asList());

        List<GeminiCompletionItem> result = cache.get(key);
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    @Test
    void testPut_MultipleItems() {
        CompletionCache cache = CompletionCache.getInstance();
        String key = "multiGeminiKey";
        GeminiCompletionItem item1 = new GeminiCompletionItem("code1", "pre", 10, 0);
        GeminiCompletionItem item2 = new GeminiCompletionItem("code2", "pre", 10, 1);

        cache.put(key, Arrays.asList(item1, item2));
        List<GeminiCompletionItem> result = cache.get(key);

        assertThat(result).hasSize(2);
    }
}
