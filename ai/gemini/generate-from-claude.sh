#!/bin/bash

# Script to generate Gemini plugin classes from Claude plugin
# Run from netbeans-gemini directory

CLAUDE_BASE="../claude/src/main/java/org/flossware/netbeans/claude"
GEMINI_BASE="src/main/java/org/flossware/netbeans/gemini"

# Function to copy and adapt a file
adapt_file() {
    local claude_file=$1
    local gemini_file=$2

    echo "Adapting: $claude_file -> $gemini_file"

    # Copy file
    cp "$claude_file" "$gemini_file"

    # Perform replacements
    sed -i 's/claude/gemini/g' "$gemini_file"
    sed -i 's/Claude/Gemini/g' "$gemini_file"
    sed -i 's/Anthropic/Google/g' "$gemini_file"
    sed -i 's/anthropic/google/g' "$gemini_file"
    sed -i 's/ANTHROPIC/GOOGLE/g' "$gemini_file"
}

echo "=== Generating Gemini Plugin from Claude Plugin ==="
echo ""

# API Layer
echo "1. API Layer"
adapt_file "$CLAUDE_BASE/api/ClaudeService.java" "$GEMINI_BASE/api/GeminiService.java"

# Completion Layer
echo "2. Completion Layer"
adapt_file "$CLAUDE_BASE/completion/ClaudeCompletionProvider.java" "$GEMINI_BASE/completion/GeminiCompletionProvider.java"
adapt_file "$CLAUDE_BASE/completion/ClaudeCompletionQuery.java" "$GEMINI_BASE/completion/GeminiCompletionQuery.java"
adapt_file "$CLAUDE_BASE/completion/ClaudeCompletionItem.java" "$GEMINI_BASE/completion/GeminiCompletionItem.java"
adapt_file "$CLAUDE_BASE/completion/ClaudeCompletionDocumentation.java" "$GEMINI_BASE/completion/GeminiCompletionDocumentation.java"
adapt_file "$CLAUDE_BASE/completion/ClaudeCompletionSettings.java" "$GEMINI_BASE/completion/GeminiCompletionSettings.java"
adapt_file "$CLAUDE_BASE/completion/CompletionCache.java" "$GEMINI_BASE/completion/CompletionCache.java"
adapt_file "$CLAUDE_BASE/completion/CompletionContextBuilder.java" "$GEMINI_BASE/completion/CompletionContextBuilder.java"

# UI Layer
echo "3. UI Layer"
adapt_file "$CLAUDE_BASE/ui/ClaudeWindowTopComponent.java" "$GEMINI_BASE/ui/GeminiWindowTopComponent.java"
adapt_file "$CLAUDE_BASE/ui/CodeInsertDialog.java" "$GEMINI_BASE/ui/CodeInsertDialog.java"
adapt_file "$CLAUDE_BASE/ui/ChatMessagePanel.java" "$GEMINI_BASE/ui/ChatMessagePanel.java"

# Actions
echo "4. Actions"
adapt_file "$CLAUDE_BASE/actions/OpenClaudeAction.java" "$GEMINI_BASE/actions/OpenGeminiAction.java"
adapt_file "$CLAUDE_BASE/actions/AskClaudeAboutSelectionAction.java" "$GEMINI_BASE/actions/AskGeminiAboutSelectionAction.java"
adapt_file "$CLAUDE_BASE/actions/ExplainCodeAction.java" "$GEMINI_BASE/actions/ExplainCodeAction.java"
adapt_file "$CLAUDE_BASE/actions/RefactorWithClaudeAction.java" "$GEMINI_BASE/actions/RefactorWithGeminiAction.java"
adapt_file "$CLAUDE_BASE/actions/ShowProjectContextAction.java" "$GEMINI_BASE/actions/ShowProjectContextAction.java"

# Options
echo "5. Options"
adapt_file "$CLAUDE_BASE/options/ClaudeOptionsPanelController.java" "$GEMINI_BASE/options/GeminiOptionsPanelController.java"
adapt_file "$CLAUDE_BASE/options/ClaudeOptionsPanel.java" "$GEMINI_BASE/options/GeminiOptionsPanel.java"

# Context
echo "6. Context"
adapt_file "$CLAUDE_BASE/context/ProjectContext.java" "$GEMINI_BASE/context/ProjectContext.java"
adapt_file "$CLAUDE_BASE/context/ProjectContextManager.java" "$GEMINI_BASE/context/ProjectContextManager.java"

# Utils
echo "7. Utils"
adapt_file "$CLAUDE_BASE/util/CodeExtractor.java" "$GEMINI_BASE/util/CodeExtractor.java"
adapt_file "$CLAUDE_BASE/util/EditorUtil.java" "$GEMINI_BASE/util/EditorUtil.java"

# Resources
echo "8. Resources"
CLAUDE_RES="../claude/src/main/resources/org/flossware/netbeans/claude"
GEMINI_RES="src/main/resources/org/flossware/netbeans/gemini"

adapt_file "$CLAUDE_RES/Bundle.properties" "$GEMINI_RES/Bundle.properties"
adapt_file "$CLAUDE_RES/layer.xml" "$GEMINI_RES/layer.xml"
adapt_file "$CLAUDE_RES/ClaudeWindowTopComponentSettings.xml" "$GEMINI_RES/GeminiWindowTopComponentSettings.xml"
adapt_file "$CLAUDE_RES/ClaudeWindowTopComponentWstcref.xml" "$GEMINI_RES/GeminiWindowTopComponentWstcref.xml"

echo ""
echo "=== Generation Complete! ==="
echo ""
echo "⚠️  Manual steps required:"
echo "1. Review all generated files"
echo "2. Update GeminiClient usage (already created differently)"
echo "3. Create gemini-icon.png (16x16)"
echo "4. Update model names in GeminiOptionsPanel"
echo "5. Test build: mvn clean package"
echo ""
