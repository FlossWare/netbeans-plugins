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

package org.flossware.netbeans.cohere.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CohereServiceTest {

    private CohereService service;

    @Mock
    private CohereClient mockClient;

    @BeforeEach
    void setUp() {
        try {
            java.lang.reflect.Field instance = CohereService.class.getDeclaredField("instance");
            instance.setAccessible(true);
            instance.set(null, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        service = CohereService.getInstance();

        try {
            java.lang.reflect.Field client = CohereService.class.getDeclaredField("client");
            client.setAccessible(true);
            client.set(service, mockClient);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetInstance_ReturnsSameInstance() {
        CohereService instance1 = CohereService.getInstance();
        CohereService instance2 = CohereService.getInstance();

        assertThat(instance1).isSameAs(instance2);
    }

    @Test
    void testSendMessageAsync_Success() throws Exception {
        String expectedResponse = "Hello from Cohere!";
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

        CompletableFuture<String> future = service.sendMessageWithContextAsync("Analyze code", "public class Test {}");
        String result = future.get(5, TimeUnit.SECONDS);

        assertThat(result).isEqualTo(expectedResponse);
        verify(mockClient).sendMessageWithContext("Analyze code", "public class Test {}");
    }
}
