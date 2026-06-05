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

package org.flossware.netbeans.ai.core.validation;

import java.util.Objects;

/**
 * Utility class for validating AI API messages before sending.
 *
 * <p>Provides comprehensive input validation including:
 * <ul>
 *   <li>Null/empty checks</li>
 *   <li>Message length validation</li>
 *   <li>Invalid character detection</li>
 *   <li>Encoding validation</li>
 * </ul>
 *
 * <p>This validator is shared across all AI providers to ensure
 * consistent input validation and error handling.</p>
 */
public class MessageValidator {

    /**
     * Default maximum message length in characters.
     * Corresponds to approximately 2 million tokens (conservative estimate).
     */
    public static final int DEFAULT_MAX_MESSAGE_LENGTH = 1_000_000;

    /**
     * Maximum message length for strict mode (256KB).
     * Used for providers with stricter limits.
     */
    public static final int STRICT_MAX_MESSAGE_LENGTH = 262_144;

    /**
     * Maximum message length for standard mode (1MB).
     * Used for most providers.
     */
    public static final int STANDARD_MAX_MESSAGE_LENGTH = 1_048_576;

    /**
     * Maximum message length for generous mode (10MB).
     * Used for providers with higher limits.
     */
    public static final int GENEROUS_MAX_MESSAGE_LENGTH = 10_485_760;

    private final int maxMessageLength;

    /**
     * Creates a validator with default maximum message length.
     */
    public MessageValidator() {
        this(DEFAULT_MAX_MESSAGE_LENGTH);
    }

    /**
     * Creates a validator with a custom maximum message length.
     *
     * @param maxMessageLength the maximum allowed message length in characters
     * @throws IllegalArgumentException if maxMessageLength is not positive
     */
    public MessageValidator(int maxMessageLength) {
        if (maxMessageLength <= 0) {
            throw new IllegalArgumentException("maxMessageLength must be positive, got: " + maxMessageLength);
        }
        this.maxMessageLength = maxMessageLength;
    }

    /**
     * Validates a message before sending to the API.
     *
     * @param message the message to validate
     * @throws IllegalArgumentException if validation fails
     */
    public void validateMessage(String message) {
        // Null check
        Objects.requireNonNull(message, "message cannot be null");

        // Empty check (after trimming whitespace)
        if (message.trim().isEmpty()) {
            throw new IllegalArgumentException("message cannot be empty or whitespace-only");
        }

        // Length validation
        if (message.length() > maxMessageLength) {
            throw new IllegalArgumentException(
                String.format(
                    "message exceeds maximum length of %d characters (actual: %d)",
                    maxMessageLength,
                    message.length()
                )
            );
        }

        // UTF-8 encoding validation
        validateEncoding(message);
    }

    /**
     * Validates a message and an additional context string.
     *
     * @param message the user message to validate
     * @param context the code context to validate
     * @throws IllegalArgumentException if validation fails
     */
    public void validateMessageWithContext(String message, String context) {
        // Validate message
        validateMessage(message);

        // Validate context
        Objects.requireNonNull(context, "context cannot be null");
        if (context.trim().isEmpty()) {
            throw new IllegalArgumentException("context cannot be empty or whitespace-only");
        }

        // Check combined length doesn't exceed limit
        // Estimated overhead: "Here is the code context:\n\n```\n%s\n```\n\nUser question: %s"
        int estimatedOverhead = 50;
        int combinedLength = message.length() + context.length() + estimatedOverhead;
        if (combinedLength > maxMessageLength) {
            throw new IllegalArgumentException(
                String.format(
                    "combined message and context exceed maximum length of %d characters (estimated: %d)",
                    maxMessageLength,
                    combinedLength
                )
            );
        }

        // Validate encoding of context
        validateEncoding(context);
    }

    /**
     * Validates that a string can be properly encoded/decoded in UTF-8.
     *
     * @param text the text to validate
     * @throws IllegalArgumentException if the text contains invalid characters
     */
    private void validateEncoding(String text) {
        try {
            // Test UTF-8 encoding and decoding
            String encoded = new String(text.getBytes("UTF-8"), "UTF-8");
            if (!encoded.equals(text)) {
                throw new IllegalArgumentException("message contains characters that cannot be properly encoded/decoded");
            }
        } catch (java.io.UnsupportedEncodingException e) {
            // UTF-8 is always supported, but catch in case of unexpected issues
            throw new IllegalArgumentException("UTF-8 encoding error: " + e.getMessage(), e);
        }
    }

    /**
     * Gets the maximum message length configured for this validator.
     *
     * @return the maximum message length in characters
     */
    public int getMaxMessageLength() {
        return maxMessageLength;
    }

    /**
     * Creates a validator with strict message length limits (256KB).
     * Suitable for providers with strict token limits.
     *
     * @return a new MessageValidator with strict limits
     */
    public static MessageValidator createStrict() {
        return new MessageValidator(STRICT_MAX_MESSAGE_LENGTH);
    }

    /**
     * Creates a validator with standard message length limits (1MB).
     * Suitable for most AI providers.
     *
     * @return a new MessageValidator with standard limits
     */
    public static MessageValidator createStandard() {
        return new MessageValidator(STANDARD_MAX_MESSAGE_LENGTH);
    }

    /**
     * Creates a validator with generous message length limits (10MB).
     * Suitable for providers with higher limits.
     *
     * @return a new MessageValidator with generous limits
     */
    public static MessageValidator createGenerous() {
        return new MessageValidator(GENEROUS_MAX_MESSAGE_LENGTH);
    }
}
