/*
 * Copyright 2026 FlossWare.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.flossware.netbeans.chatgpt.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility for extracting code blocks from ChatGPT responses
 */
public class CodeExtractor {

    private static final Pattern CODE_BLOCK_PATTERN = Pattern.compile(
            "```(?:(\\w+)\\n)?([\\s\\S]*?)```",
            Pattern.MULTILINE
    );

    /**
     * Represents a code block found in text
     */
    public static class CodeBlock {
        private final String language;
        private final String code;
        private final int startIndex;
        private final int endIndex;

        public CodeBlock(String language, String code, int startIndex, int endIndex) {
            this.language = language != null ? language : "text";
            this.code = code;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
        }

        public String getLanguage() {
            return language;
        }

        public String getCode() {
            return code;
        }

        public int getStartIndex() {
            return startIndex;
        }

        public int getEndIndex() {
            return endIndex;
        }

        @Override
        public String toString() {
            return String.format("CodeBlock{language='%s', length=%d}", language, code.length());
        }
    }

    /**
     * Extract all code blocks from text
     */
    public static List<CodeBlock> extractCodeBlocks(String text) {
        List<CodeBlock> codeBlocks = new ArrayList<>();
        Matcher matcher = CODE_BLOCK_PATTERN.matcher(text);

        while (matcher.find()) {
            String language = matcher.group(1);
            String code = matcher.group(2);
            int start = matcher.start();
            int end = matcher.end();

            codeBlocks.add(new CodeBlock(language, code, start, end));
        }

        return codeBlocks;
    }

    /**
     * Check if text contains any code blocks
     */
    public static boolean hasCodeBlocks(String text) {
        return CODE_BLOCK_PATTERN.matcher(text).find();
    }

    /**
     * Get the first code block from text
     */
    public static CodeBlock getFirstCodeBlock(String text) {
        List<CodeBlock> blocks = extractCodeBlocks(text);
        return blocks.isEmpty() ? null : blocks.get(0);
    }

    /**
     * Remove code block markers and return just the code
     */
    public static String stripCodeBlockMarkers(String codeBlock) {
        Matcher matcher = CODE_BLOCK_PATTERN.matcher(codeBlock);
        if (matcher.find()) {
            return matcher.group(2);
        }
        return codeBlock;
    }
}
