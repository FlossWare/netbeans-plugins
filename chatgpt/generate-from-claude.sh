#!/bin/bash

# Script to generate ChatGPT plugin classes from Claude plugin
# Run from netbeans-chatgpt directory

CLAUDE_BASE="../claude/src/main/java/org/flossware/netbeans/claude"
CHATGPT_BASE="src/main/java/org/flossware/netbeans/chatgpt"

# Function to copy and adapt a file
adapt_file() {
    local claude_file=$1
    local chatgpt_file=$2

    echo "Adapting: $claude_file -> $chatgpt_file"

    # Copy file
    cp "$claude_file" "$chatgpt_file"

    # Perform replacements
    sed -i 's/claude/chatgpt/g' "$chatgpt_file"
    sed -i 's/Claude/ChatGPT/g' "$chatgpt_file"
    sed -i 's/Anthropic/OpenAI/g' "$chatgpt_file"
    sed -i 's/anthropic/openai/g' "$chatgpt_file"
    sed -i 's/ANTHROPIC/OPENAI/g' "$chatgpt_file"
}

echo "=== Generating ChatGPT Plugin from Claude Plugin ==="
echo ""

# API Layer
echo "1. API Layer"
adapt_file "$CLAUDE_BASE/api/ClaudeService.java" "$CHATGPT_BASE/api/ChatGPTService.java"

# Completion Layer
echo "2. Completion Layer"
adapt_file "$CLAUDE_BASE/completion/ClaudeCompletionProvider.java" "$CHATGPT_BASE/completion/ChatGPTCompletionProvider.java"
adapt_file "$CLAUDE_BASE/completion/ClaudeCompletionQuery.java" "$CHATGPT_BASE/completion/ChatGPTCompletionQuery.java"
adapt_file "$CLAUDE_BASE/completion/ClaudeCompletionItem.java" "$CHATGPT_BASE/completion/ChatGPTCompletionItem.java"
adapt_file "$CLAUDE_BASE/completion/ClaudeCompletionDocumentation.java" "$CHATGPT_BASE/completion/ChatGPTCompletionDocumentation.java"
adapt_file "$CLAUDE_BASE/completion/ClaudeCompletionSettings.java" "$CHATGPT_BASE/completion/ChatGPTCompletionSettings.java"
adapt_file "$CLAUDE_BASE/completion/CompletionCache.java" "$CHATGPT_BASE/completion/CompletionCache.java"
adapt_file "$CLAUDE_BASE/completion/CompletionContextBuilder.java" "$CHATGPT_BASE/completion/CompletionContextBuilder.java"

# UI Layer
echo "3. UI Layer"
adapt_file "$CLAUDE_BASE/ui/ClaudeWindowTopComponent.java" "$CHATGPT_BASE/ui/ChatGPTWindowTopComponent.java"
adapt_file "$CLAUDE_BASE/ui/CodeInsertDialog.java" "$CHATGPT_BASE/ui/CodeInsertDialog.java"
adapt_file "$CLAUDE_BASE/ui/ChatMessagePanel.java" "$CHATGPT_BASE/ui/ChatMessagePanel.java"

# Actions
echo "4. Actions"
adapt_file "$CLAUDE_BASE/actions/OpenClaudeAction.java" "$CHATGPT_BASE/actions/OpenChatGPTAction.java"
adapt_file "$CLAUDE_BASE/actions/AskClaudeAboutSelectionAction.java" "$CHATGPT_BASE/actions/AskChatGPTAboutSelectionAction.java"
adapt_file "$CLAUDE_BASE/actions/ExplainCodeAction.java" "$CHATGPT_BASE/actions/ExplainCodeAction.java"
adapt_file "$CLAUDE_BASE/actions/RefactorWithClaudeAction.java" "$CHATGPT_BASE/actions/RefactorWithChatGPTAction.java"
adapt_file "$CLAUDE_BASE/actions/ShowProjectContextAction.java" "$CHATGPT_BASE/actions/ShowProjectContextAction.java"

# Options
echo "5. Options"
adapt_file "$CLAUDE_BASE/options/ClaudeOptionsPanelController.java" "$CHATGPT_BASE/options/ChatGPTOptionsPanelController.java"
adapt_file "$CLAUDE_BASE/options/ClaudeOptionsPanel.java" "$CHATGPT_BASE/options/ChatGPTOptionsPanel.java"

# Context
echo "6. Context"
adapt_file "$CLAUDE_BASE/context/ProjectContext.java" "$CHATGPT_BASE/context/ProjectContext.java"
adapt_file "$CLAUDE_BASE/context/ProjectContextManager.java" "$CHATGPT_BASE/context/ProjectContextManager.java"

# Utils
echo "7. Utils"
adapt_file "$CLAUDE_BASE/util/CodeExtractor.java" "$CHATGPT_BASE/util/CodeExtractor.java"
adapt_file "$CLAUDE_BASE/util/EditorUtil.java" "$CHATGPT_BASE/util/EditorUtil.java"

# Resources
echo "8. Resources"
CLAUDE_RES="../claude/src/main/resources/org/flossware/netbeans/claude"
CHATGPT_RES="src/main/resources/org/flossware/netbeans/chatgpt"

adapt_file "$CLAUDE_RES/Bundle.properties" "$CHATGPT_RES/Bundle.properties"
adapt_file "$CLAUDE_RES/layer.xml" "$CHATGPT_RES/layer.xml"
adapt_file "$CLAUDE_RES/ClaudeWindowTopComponentSettings.xml" "$CHATGPT_RES/ChatGPTWindowTopComponentSettings.xml"
adapt_file "$CLAUDE_RES/ClaudeWindowTopComponentWstcref.xml" "$CHATGPT_RES/ChatGPTWindowTopComponentWstcref.xml"

echo ""
echo "=== Generation Complete! ==="
echo ""
echo "⚠️  Manual steps required:"
echo "1. Review all generated files"
echo "2. Create ChatGPTClient.java"
echo "3. Create chatgpt-icon.png (16x16)"
echo "4. Update model names in ChatGPTOptionsPanel"
echo "5. Test build: mvn clean package"
echo ""
