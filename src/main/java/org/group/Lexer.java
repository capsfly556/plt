package org.group;

import java.util.*;

public class Lexer {
    // Define token types
    enum TokenType {
        KEYWORD, IDENTIFIER, OPERATOR, LITERAL, DELIMITER, ERROR
    }

    // Define keywords
    private static final Set<String> KEYWORDS = new HashSet<>(Arrays.asList("if", "else", "while", "return"));

    // Define operators
    private static final Set<String> OPERATORS = new HashSet<>(Arrays.asList("+", "-", "*", "/", "==", "!="));

    // Define delimiters
    private static final Set<Character> DELIMITERS = new HashSet<>(Arrays.asList(';', ',', '(', ')', '{', '}'));

    public static void main(String[] args) {
        // Store multiple input programs for testing in a list
        List<String> inputs = Arrays.asList(
                // Example 1: Simple Conditional Statement
                "if (x == 5) {\n    return x + 1;\n}",

                // Example 2: Loop with an Error (Unrecognized Symbol)
                "while (y != 10) {\n    y = y * 2 @ 3;\n}",

                // Example 3: Function Declaration and Call with Identifiers
                "int add(int a, int b) {\n    return a + b;\n}\nadd(2, 3);",

                // Example 4: Multiple Operators and Delimiters
                "x = y + (z * 2) - 1;",

                // Example 5: String with an Unrecognized Token (Lexical Error)
                "if (a == 100) {\n    result = a + #;\n}"
        );

        // Loop through each input program, scan tokens, and print the result
        for (int i = 0; i < inputs.size(); i++) {
            System.out.println("Example " + (i + 1) + " Tokens:");
            printTokens(scan(inputs.get(i)));
            System.out.println(); // Add space between examples
        }
    }

    // Method to print the tokens
    private static void printTokens(List<Token> tokens) {
        for (Token token : tokens) {
            System.out.println(token);
        }
    }

    // Token class to hold token type and value
    static class Token {
        TokenType type;
        String value;

        Token(TokenType type, String value) {
            this.type = type;
            this.value = value;
        }

        @Override
        public String toString() {
            return "<" + type + ", " + value + ">";
        }
    }

    // Main scanning function
    public static List<Token> scan(String input) {
        List<Token> tokens = new ArrayList<>();
        int length = input.length();
        int i = 0;

        while (i < length) {
            char current = input.charAt(i);

            // Skip whitespace
            if (Character.isWhitespace(current)) {
                i++;
                continue;
            }

            // Handle keywords or identifiers
            if (Character.isLetter(current)) {
                StringBuilder sb = new StringBuilder();
                while (i < length && (Character.isLetterOrDigit(input.charAt(i)))) {
                    sb.append(input.charAt(i));
                    i++;
                }
                String word = sb.toString();
                if (KEYWORDS.contains(word)) {
                    tokens.add(new Token(TokenType.KEYWORD, word));
                } else {
                    tokens.add(new Token(TokenType.IDENTIFIER, word));
                }
            }
            // Handle numbers (literals)
            else if (Character.isDigit(current)) {
                StringBuilder sb = new StringBuilder();
                while (i < length && Character.isDigit(input.charAt(i))) {
                    sb.append(input.charAt(i));
                    i++;
                }
                tokens.add(new Token(TokenType.LITERAL, sb.toString()));
            }
            // Handle operators
            else if (isOperator(input, i)) {
                StringBuilder sb = new StringBuilder();
                while (i < length && isOperator(input, i)) {
                    sb.append(input.charAt(i));
                    i++;
                }
                tokens.add(new Token(TokenType.OPERATOR, sb.toString()));
            }
            // Handle delimiters
            else if (DELIMITERS.contains(current)) {
                tokens.add(new Token(TokenType.DELIMITER, Character.toString(current)));
                i++;
            }
            // Handle unrecognized characters (lexical errors)
            else {
                tokens.add(new Token(TokenType.ERROR, "Unrecognized token '" + current + "' at index " + i));
                i++;
            }
        }

        return tokens;
    }

    // Helper method to check if the current part of the string is an operator
    private static boolean isOperator(String input, int index) {
        return OPERATORS.contains(String.valueOf(input.charAt(index))) ||
                (index < input.length() - 1 && OPERATORS.contains(input.substring(index, index + 2)));
    }
}
