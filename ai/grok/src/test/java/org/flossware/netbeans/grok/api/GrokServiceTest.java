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

package org.flossware.netbeans.grok.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GrokServiceTest {

    private GrokService service;

    @Mock
    private GrokClient mockClient;

    @BeforeEach
    void setUp() {
        // Reset singleton for each test
        try {
            java.lang.reflect.Field instance = GrokService.class.getDeclaredField("instance");
            instance.setAccessible(true);
            instance.set(null, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        service = GrokService.getInstance();

        // Inject mock client via reflection
        try {
            java.lang.reflect.Field client = GrokService.class.getDeclaredField("client");
            client.setAccessible(true);
            client.set(service, mockClient);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetInstance_ReturnsSameInstance() {
        GrokService instance1 = GrokService.getInstance();
        GrokService instance2 = GrokService.getInstance();

        assertThat(instance1).isSameAs(instance2);
    }

    @Test
    void testSendMessageAsync_Success() throws Exception {
        String expectedResponse = "Hello from Grok!";
        when(mockClient.sendMessage(anyString())).thenReturn(expectedResponse);

        CompletableFuture<String> future = service.sendMessageAsync("Hello");
        String result = future.get(5, TimeUnit.SECONDS);

        assertThat(result).isEqualTo(expectedResponse);
        verify(mockClient).sendMessage("Hello");
    }

    @Test
    void testSendMessageAsync_Failure() throws Exception {
        when(mockClient.sendMessage(anyString())).thenThrow(new RuntimeException("API Error"));

        CompletableFuture<String> future = service.sendMessageAsync("Hello");

        assertThatThrownBy(() -> future.get(5, TimeUnit.SECONDS))
            .hasCauseInstanceOf(RuntimeException.class)
            .hasMessageContaining("API Error");
    }

    @Test
    void testSendMessageWithContextAsync_Success() throws Exception {
        String expectedResponse = "Code analysis complete";
        when(mockClient.sendMessageWithContext(anyString(), anyString())).thenReturn(expectedResponse);

        CompletableFuture<String> future = service.sendMessageWithContextAsync("Explain this", "public class Test {}");
        String result = future.get(5, TimeUnit.SECONDS);

        assertThat(result).isEqualTo(expectedResponse);
        verify(mockClient).sendMessageWithContext("Explain this", "public class Test {}");
    }

    @Test
    void testClearHistory() {
        service.clearHistory();
        verify(mockClient).clearHistory();
    }

    @Test
    void testIsConfigured() {
        when(mockClient.isConfigured()).thenReturn(true);
        assertThat(service.isConfigured()).isTrue();

        when(mockClient.isConfigured()).thenReturn(false);
        assertThat(service.isConfigured()).isFalse();
    }

    @Test
    void testGetClient() {
        GrokClient client = service.getClient();
        assertThat(client).isEqualTo(mockClient);
    }

    @Test
    void testGetHistorySize() {
        when(mockClient.getHistorySize()).thenReturn(5);
        assertThat(service.getHistorySize()).isEqualTo(5);
    }

    @Test
    void testSendMessageStreamingAsync_Success() throws Exception {
        String expectedResponse = "Streaming response";
        Consumer<String> onChunk = mock(Consumer.class);

        when(mockClient.sendMessageStreaming(anyString(), any())).thenReturn(expectedResponse);

        CompletableFuture<String> future = service.sendMessageStreamingAsync("Hello", onChunk);
        String result = future.get(5, TimeUnit.SECONDS);

        assertThat(result).isEqualTo(expectedResponse);
        verify(mockClient).sendMessageStreaming(eq("Hello"), any());
    }

    @Test
    void testSendMessageWithContextStreamingAsync_Success() throws Exception {
        String expectedResponse = "Context streaming response";
        Consumer<String> onChunk = mock(Consumer.class);

        when(mockClient.sendMessageWithContextStreaming(anyString(), anyString(), any())).thenReturn(expectedResponse);

        CompletableFuture<String> future = service.sendMessageWithContextStreamingAsync("Question", "Code context", onChunk);
        String result = future.get(5, TimeUnit.SECONDS);

        assertThat(result).isEqualTo(expectedResponse);
        verify(mockClient).sendMessageWithContextStreaming(eq("Question"), eq("Code context"), any());
    }
}
