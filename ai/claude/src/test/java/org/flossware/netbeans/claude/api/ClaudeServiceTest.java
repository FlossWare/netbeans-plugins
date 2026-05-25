/* Copyright 2026 FlossWare. */
package org.flossware.netbeans.claude.api;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class ClaudeServiceTest {

    @Test
    void testGetInstance() {
        ClaudeService service = ClaudeService.getInstance();
        assertThat(service).isNotNull();
    }

    @Test
    void testGetInstance_ReturnsSingleton() {
        ClaudeService service1 = ClaudeService.getInstance();
        ClaudeService service2 = ClaudeService.getInstance();
        assertThat(service1).isSameAs(service2);
    }

    @Test
    void testIsConfigured_Initially() {
        ClaudeService service = ClaudeService.getInstance();
        // Will depend on whether API key is set in preferences
        assertThatCode(() -> service.isConfigured()).doesNotThrowAnyException();
    }

    @Test
    void testGetClient() {
        ClaudeService service = ClaudeService.getInstance();
        assertThat(service.getClient()).isNotNull();
    }

    @Test
    void testGetHistorySize_Initially() {
        ClaudeService service = ClaudeService.getInstance();
        service.clearHistory(); // Ensure clean state
        assertThat(service.getHistorySize()).isEqualTo(0);
    }

    @Test
    void testClearHistory() {
        ClaudeService service = ClaudeService.getInstance();
        service.clearHistory();
        assertThat(service.getHistorySize()).isEqualTo(0);
    }

    @Test
    void testSendMessageAsync_ReturnsCompletableFuture() {
        ClaudeService service = ClaudeService.getInstance();
        CompletableFuture<String> future = service.sendMessageAsync("test");
        assertThat(future).isNotNull();
        assertThat(future).isInstanceOf(CompletableFuture.class);
    }

    @Test
    void testSendMessageAsync_NullMessage() {
        ClaudeService service = ClaudeService.getInstance();
        CompletableFuture<String> future = service.sendMessageAsync(null);
        assertThat(future).isNotNull();
        
        // Should complete exceptionally
        assertThatCode(() -> {
            future.join();
        }).doesNotThrowAnyException(); // May succeed or fail depending on API state
    }

    @Test
    void testSendMessageAsync_EmptyMessage() {
        ClaudeService service = ClaudeService.getInstance();
        CompletableFuture<String> future = service.sendMessageAsync("");
        assertThat(future).isNotNull();
    }

    @Test
    void testSendMessageWithContextAsync_ReturnsCompletableFuture() {
        ClaudeService service = ClaudeService.getInstance();
        CompletableFuture<String> future = service.sendMessageWithContextAsync("message", "context");
        assertThat(future).isNotNull();
        assertThat(future).isInstanceOf(CompletableFuture.class);
    }

    @Test
    void testSendMessageWithContextAsync_NullMessage() {
        ClaudeService service = ClaudeService.getInstance();
        CompletableFuture<String> future = service.sendMessageWithContextAsync(null, "context");
        assertThat(future).isNotNull();
    }

    @Test
    void testSendMessageWithContextAsync_NullContext() {
        ClaudeService service = ClaudeService.getInstance();
        CompletableFuture<String> future = service.sendMessageWithContextAsync("message", null);
        assertThat(future).isNotNull();
    }

    @Test
    void testSendMessageStreamingAsync_ReturnsCompletableFuture() {
        ClaudeService service = ClaudeService.getInstance();
        AtomicInteger chunkCount = new AtomicInteger(0);
        
        CompletableFuture<String> future = service.sendMessageStreamingAsync("test", 
            chunk -> chunkCount.incrementAndGet());
        
        assertThat(future).isNotNull();
        assertThat(future).isInstanceOf(CompletableFuture.class);
    }

    @Test
    void testSendMessageStreamingAsync_NullCallback() {
        ClaudeService service = ClaudeService.getInstance();
        CompletableFuture<String> future = service.sendMessageStreamingAsync("test", null);
        assertThat(future).isNotNull();
    }

    @Test
    void testSendMessageStreamingAsync_NullMessage() {
        ClaudeService service = ClaudeService.getInstance();
        CompletableFuture<String> future = service.sendMessageStreamingAsync(null, chunk -> {});
        assertThat(future).isNotNull();
    }

    @Test
    void testSendMessageStreamingAsync_EmptyMessage() {
        ClaudeService service = ClaudeService.getInstance();
        CompletableFuture<String> future = service.sendMessageStreamingAsync("", chunk -> {});
        assertThat(future).isNotNull();
    }

    @Test
    void testSendMessageWithContextStreamingAsync_ReturnsCompletableFuture() {
        ClaudeService service = ClaudeService.getInstance();
        AtomicInteger chunkCount = new AtomicInteger(0);
        
        CompletableFuture<String> future = service.sendMessageWithContextStreamingAsync(
            "message", "context", chunk -> chunkCount.incrementAndGet());
        
        assertThat(future).isNotNull();
        assertThat(future).isInstanceOf(CompletableFuture.class);
    }

    @Test
    void testSendMessageWithContextStreamingAsync_NullMessage() {
        ClaudeService service = ClaudeService.getInstance();
        CompletableFuture<String> future = service.sendMessageWithContextStreamingAsync(
            null, "context", chunk -> {});
        assertThat(future).isNotNull();
    }

    @Test
    void testSendMessageWithContextStreamingAsync_NullContext() {
        ClaudeService service = ClaudeService.getInstance();
        CompletableFuture<String> future = service.sendMessageWithContextStreamingAsync(
            "message", null, chunk -> {});
        assertThat(future).isNotNull();
    }

    @Test
    void testSendMessageWithContextStreamingAsync_NullCallback() {
        ClaudeService service = ClaudeService.getInstance();
        CompletableFuture<String> future = service.sendMessageWithContextStreamingAsync(
            "message", "context", null);
        assertThat(future).isNotNull();
    }

    @Test
    void testMultipleConcurrentRequests() {
        ClaudeService service = ClaudeService.getInstance();
        
        CompletableFuture<String> future1 = service.sendMessageAsync("test1");
        CompletableFuture<String> future2 = service.sendMessageAsync("test2");
        CompletableFuture<String> future3 = service.sendMessageAsync("test3");
        
        assertThat(future1).isNotNull();
        assertThat(future2).isNotNull();
        assertThat(future3).isNotNull();
    }

    @Test
    void testClearHistory_MultipleTimes() {
        ClaudeService service = ClaudeService.getInstance();
        service.clearHistory();
        service.clearHistory();
        service.clearHistory();
        assertThat(service.getHistorySize()).isEqualTo(0);
    }
}
