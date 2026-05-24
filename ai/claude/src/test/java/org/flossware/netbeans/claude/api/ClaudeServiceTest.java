package org.flossware.netbeans.claude.api;

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
class ClaudeServiceTest {

    private ClaudeService service;

    @Mock
    private ClaudeClient mockClient;

    @BeforeEach
    void setUp() {
        // Reset singleton for each test
        try {
            java.lang.reflect.Field instance = ClaudeService.class.getDeclaredField("instance");
            instance.setAccessible(true);
            instance.set(null, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        service = ClaudeService.getInstance();

        // Inject mock client via reflection
        try {
            java.lang.reflect.Field client = ClaudeService.class.getDeclaredField("client");
            client.setAccessible(true);
            client.set(service, mockClient);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetInstance_ReturnsSameInstance() {
        ClaudeService instance1 = ClaudeService.getInstance();
        ClaudeService instance2 = ClaudeService.getInstance();

        assertThat(instance1).isSameAs(instance2);
    }

    @Test
    void testSendMessageAsync_Success() throws Exception {
        String expectedResponse = "Hello from Claude!";
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
        ClaudeClient client = service.getClient();
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
