/* Copyright 2026 FlossWare. */
package org.flossware.netbeans.claude.api;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.assertj.core.api.Assertions.*;

class ClaudeServiceTest {

    @BeforeEach
    void setUp() {
        // Reset the singleton state before each test
        // This is necessary because shutdown() makes getInstance() throw
        try {
            java.lang.reflect.Field instanceField = ClaudeService.class.getDeclaredField("instance");
            instanceField.setAccessible(true);
            instanceField.set(null, null);

            java.lang.reflect.Field hasBeenShutdownField = ClaudeService.class.getDeclaredField("hasBeenShutdown");
            hasBeenShutdownField.setAccessible(true);
            hasBeenShutdownField.set(null, false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            // Fields not found, test framework setup issue
            throw new RuntimeException(e);
        }
    }

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

        // Future should complete (may be exceptionally if API not configured)
        // Don't call join() as it may throw depending on API state
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

    @Test
    void testShutdown_PreventsGetInstance() {
        ClaudeService service = ClaudeService.getInstance();
        assertThat(service).isNotNull();

        // Shutdown the service
        ClaudeService.shutdown();

        // Attempting to get a new instance should throw
        assertThatThrownBy(ClaudeService::getInstance)
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("Service has been shut down and cannot be restarted");
    }

    @Test
    void testShutdown_IsIdempotent() {
        ClaudeService service = ClaudeService.getInstance();
        assertThat(service).isNotNull();

        // Multiple shutdown calls should not throw
        assertThatCode(() -> {
            ClaudeService.shutdown();
            ClaudeService.shutdown();
            ClaudeService.shutdown();
        }).doesNotThrowAnyException();

        // And getInstance should still throw
        assertThatThrownBy(ClaudeService::getInstance)
            .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void testInconsistentInstanceScenario() {
        // Reproduces the scenario from issue #60:
        // Thread A gets instance
        ClaudeService serviceThreadA = ClaudeService.getInstance();
        assertThat(serviceThreadA).isNotNull();

        // Thread B shuts down the service
        ClaudeService.shutdown();

        // Old instance (Thread A) is now closed
        assertThatThrownBy(() -> serviceThreadA.sendMessageAsync("test"))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("Service has been closed");

        // Thread C cannot get a new instance (fix for inconsistency)
        assertThatThrownBy(ClaudeService::getInstance)
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("Service has been shut down and cannot be restarted");

        // This ensures consistent behavior: once shut down, all code paths fail
        // rather than some paths getting a new instance (which would be inconsistent)
    }

    @Test
    void testGetInstance_ConsistentBehaviorAfterShutdown() {
        // Get initial instance
        ClaudeService service1 = ClaudeService.getInstance();
        assertThat(service1).isNotNull();

        // Shutdown
        ClaudeService.shutdown();

        // All subsequent getInstance calls should fail consistently
        for (int i = 0; i < 3; i++) {
            assertThatThrownBy(ClaudeService::getInstance)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Service has been shut down and cannot be restarted");
        }
    }
}
