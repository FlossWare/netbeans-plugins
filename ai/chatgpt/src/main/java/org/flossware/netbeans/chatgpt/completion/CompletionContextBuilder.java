package org.flossware.netbeans.chatgpt.completion;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.netbeans.api.lexer.TokenHierarchy;
import org.netbeans.api.lexer.TokenSequence;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;

/**
 * Builds context for code completion from editor state
 */
public class CompletionContextBuilder {

    private final JTextComponent component;
    private final int caretOffset;
    private final Document document;

    public CompletionContextBuilder(JTextComponent component, int caretOffset) {
        this.component = component;
        this.caretOffset = caretOffset;
        this.document = component.getDocument();
    }

    /**
     * Build completion context for ChatGPT
     */
    public CompletionContext build() {
        CompletionContext context = new CompletionContext();

        try {
            // Get file information
            DataObject dobj = (DataObject) document.getProperty(Document.StreamDescriptionProperty);
            if (dobj != null) {
                FileObject file = dobj.getPrimaryFile();
                context.setFileName(file.getNameExt());
                context.setMimeType(file.getMIMEType());
                context.setFileExtension(file.getExt());
            }

            // Get text before and after cursor
            String textBefore = document.getText(0, caretOffset);
            String textAfter = document.getText(caretOffset, document.getLength() - caretOffset);

            context.setTextBefore(textBefore);
            context.setTextAfter(textAfter);
            context.setCaretOffset(caretOffset);

            // Extract current line
            context.setCurrentLine(extractCurrentLine());

            // Extract surrounding context (e.g., current method/class)
            context.setSurroundingContext(extractSurroundingContext());

            // Get prefix (word being typed)
            context.setPrefix(extractPrefix());

            // Determine context type (method, field, class, etc.)
            context.setContextType(determineContextType());

        } catch (BadLocationException ex) {
            // Handle gracefully
        }

        return context;
    }

    /**
     * Extract the current line being edited
     */
    private String extractCurrentLine() throws BadLocationException {
        int lineStart = javax.swing.text.Utilities.getRowStart(component, caretOffset);
        int lineEnd = javax.swing.text.Utilities.getRowEnd(component, caretOffset);
        return document.getText(lineStart, lineEnd - lineStart);
    }

    /**
     * Extract surrounding context (current method/class)
     */
    private String extractSurroundingContext() {
        try {
            // Get up to 1000 characters before cursor for context
            int start = Math.max(0, caretOffset - 1000);
            int length = Math.min(1000, caretOffset - start);
            return document.getText(start, length);
        } catch (BadLocationException ex) {
            return "";
        }
    }

    /**
     * Extract the prefix (partial word being typed)
     */
    private String extractPrefix() {
        try {
            int start = caretOffset;
            String text = document.getText(0, caretOffset);

            // Find start of current word
            for (int i = caretOffset - 1; i >= 0; i--) {
                char c = text.charAt(i);
                if (!Character.isJavaIdentifierPart(c) && c != '.') {
                    start = i + 1;
                    break;
                }
                if (i == 0) {
                    start = 0;
                }
            }

            return document.getText(start, caretOffset - start);
        } catch (BadLocationException ex) {
            return "";
        }
    }

    /**
     * Determine what type of completion context we're in
     */
    private String determineContextType() {
        String prefix = extractPrefix();

        if (prefix.contains(".")) {
            return "method_call";
        }

        try {
            String textBefore = document.getText(Math.max(0, caretOffset - 50),
                                                 Math.min(50, caretOffset));

            if (textBefore.contains("new ")) {
                return "constructor";
            }
            if (textBefore.contains("import ")) {
                return "import";
            }
            if (textBefore.contains("extends ") || textBefore.contains("implements ")) {
                return "type";
            }

            // Check for method context
            if (textBefore.contains("(")) {
                return "parameter";
            }

        } catch (BadLocationException ex) {
            // Ignore
        }

        return "general";
    }

    /**
     * Represents the context for code completion
     */
    public static class CompletionContext {
        private String fileName;
        private String mimeType;
        private String fileExtension;
        private String textBefore;
        private String textAfter;
        private String currentLine;
        private String surroundingContext;
        private String prefix;
        private String contextType;
        private int caretOffset;

        // Getters and setters
        public String getFileName() { return fileName; }
        public void setFileName(String fileName) { this.fileName = fileName; }

        public String getMimeType() { return mimeType; }
        public void setMimeType(String mimeType) { this.mimeType = mimeType; }

        public String getFileExtension() { return fileExtension; }
        public void setFileExtension(String fileExtension) { this.fileExtension = fileExtension; }

        public String getTextBefore() { return textBefore; }
        public void setTextBefore(String textBefore) { this.textBefore = textBefore; }

        public String getTextAfter() { return textAfter; }
        public void setTextAfter(String textAfter) { this.textAfter = textAfter; }

        public String getCurrentLine() { return currentLine; }
        public void setCurrentLine(String currentLine) { this.currentLine = currentLine; }

        public String getSurroundingContext() { return surroundingContext; }
        public void setSurroundingContext(String surroundingContext) {
            this.surroundingContext = surroundingContext;
        }

        public String getPrefix() { return prefix; }
        public void setPrefix(String prefix) { this.prefix = prefix; }

        public String getContextType() { return contextType; }
        public void setContextType(String contextType) { this.contextType = contextType; }

        public int getCaretOffset() { return caretOffset; }
        public void setCaretOffset(int caretOffset) { this.caretOffset = caretOffset; }

        /**
         * Build a prompt for ChatGPT based on this context
         */
        public String buildPrompt() {
            StringBuilder prompt = new StringBuilder();

            prompt.append("You are a code completion assistant. ");
            prompt.append("Provide ONLY the code completion, no explanations.\n\n");

            prompt.append("File: ").append(fileName).append("\n");
            prompt.append("Language: ").append(getLanguageFromExtension()).append("\n");
            prompt.append("Context Type: ").append(contextType).append("\n\n");

            prompt.append("Code before cursor:\n```\n");
            prompt.append(surroundingContext);
            prompt.append("\n```\n\n");

            prompt.append("Current line: ").append(currentLine).append("\n");
            prompt.append("Prefix being typed: ").append(prefix).append("\n\n");

            prompt.append("Provide a concise code completion. ");
            prompt.append("Return ONLY the code to be inserted, nothing else.");

            return prompt.toString();
        }

        private String getLanguageFromExtension() {
            if (fileExtension == null) return "unknown";
            switch (fileExtension.toLowerCase()) {
                case "java": return "Java";
                case "py": return "Python";
                case "js": return "JavaScript";
                case "ts": return "TypeScript";
                case "html": return "HTML";
                case "css": return "CSS";
                case "xml": return "XML";
                default: return fileExtension;
            }
        }
    }
}
