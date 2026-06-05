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

package org.flossware.netbeans.chatgpt.completion;

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
        String key = "chatgptTestKey";
        ChatGPTCompletionItem item = new ChatGPTCompletionItem("code", "pre", 10, 0);
        List<ChatGPTCompletionItem> items = Arrays.asList(item);

        cache.put(key, items);
        List<ChatGPTCompletionItem> result = cache.get(key);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
    }

    @Test
    void testGet_NonExistentKey() {
        CompletionCache cache = CompletionCache.getInstance();
        List<ChatGPTCompletionItem> result = cache.get("nonExistentChatGPTKey");
        assertThat(result).isNull();
    }

    @Test
    void testClear() {
        CompletionCache cache = CompletionCache.getInstance();
        ChatGPTCompletionItem item = new ChatGPTCompletionItem("code", "pre", 10, 0);
        cache.put("chatgptKey1", Arrays.asList(item));
        cache.clear();

        List<ChatGPTCompletionItem> result = cache.get("chatgptKey1");
        assertThat(result).isNull();
    }

    @Test
    void testPut_Overwrites() {
        CompletionCache cache = CompletionCache.getInstance();
        String key = "chatgptSameKey";
        ChatGPTCompletionItem item1 = new ChatGPTCompletionItem("code1", "pre", 10, 0);
        ChatGPTCompletionItem item2 = new ChatGPTCompletionItem("code2", "pre", 10, 0);

        cache.put(key, Arrays.asList(item1));
        cache.put(key, Arrays.asList(item2));

        List<ChatGPTCompletionItem> result = cache.get(key);
        assertThat(result.get(0).getSortText()).isEqualTo("code2");
    }

    @Test
    void testMultipleKeys() {
        CompletionCache cache = CompletionCache.getInstance();
        cache.clear();

        ChatGPTCompletionItem item1 = new ChatGPTCompletionItem("code1", "pre", 10, 0);
        ChatGPTCompletionItem item2 = new ChatGPTCompletionItem("code2", "pre", 10, 0);
        ChatGPTCompletionItem item3 = new ChatGPTCompletionItem("code3", "pre", 10, 0);

        cache.put("cKey1", Arrays.asList(item1));
        cache.put("cKey2", Arrays.asList(item2));
        cache.put("cKey3", Arrays.asList(item3));

        assertThat(cache.get("cKey1").get(0).getSortText()).isEqualTo("code1");
        assertThat(cache.get("cKey2").get(0).getSortText()).isEqualTo("code2");
        assertThat(cache.get("cKey3").get(0).getSortText()).isEqualTo("code3");
    }

    @Test
    void testPut_EmptyList() {
        CompletionCache cache = CompletionCache.getInstance();
        String key = "emptyChatGPTKey";
        cache.put(key, Arrays.asList());

        List<ChatGPTCompletionItem> result = cache.get(key);
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    @Test
    void testPut_MultipleItems() {
        CompletionCache cache = CompletionCache.getInstance();
        String key = "multiChatGPTKey";
        ChatGPTCompletionItem item1 = new ChatGPTCompletionItem("code1", "pre", 10, 0);
        ChatGPTCompletionItem item2 = new ChatGPTCompletionItem("code2", "pre", 10, 1);

        cache.put(key, Arrays.asList(item1, item2));
        List<ChatGPTCompletionItem> result = cache.get(key);

        assertThat(result).hasSize(2);
    }
}
