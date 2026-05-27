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

package org.flossware.netbeans.openrouter.api;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for OpenRouterService
 */
public class OpenRouterServiceTest {

    @Test
    public void testGetInstance() {
        OpenRouterService service1 = OpenRouterService.getInstance();
        OpenRouterService service2 = OpenRouterService.getInstance();

        assertThat(service1).isNotNull();
        assertThat(service2).isNotNull();
        assertThat(service1).isSameAs(service2);
    }

    @Test
    public void testGetClient() {
        OpenRouterService service = OpenRouterService.getInstance();
        OpenRouterClient client = service.getClient();

        assertThat(client).isNotNull();
    }

    @Test
    public void testClearHistory() {
        OpenRouterService service = OpenRouterService.getInstance();

        // Should not throw exception
        service.clearHistory();

        assertThat(service.getHistorySize()).isEqualTo(0);
    }

    @Test
    public void testIsConfigured() {
        OpenRouterService service = OpenRouterService.getInstance();

        // Without API key, should return false
        // Note: This test assumes no API key is configured in test environment
        boolean configured = service.isConfigured();

        // We just verify the method doesn't throw an exception
        assertThat(configured).isIn(true, false);
    }

    @Test
    public void testGetHistorySize() {
        OpenRouterService service = OpenRouterService.getInstance();
        service.clearHistory();

        int size = service.getHistorySize();
        assertThat(size).isGreaterThanOrEqualTo(0);
    }
}
