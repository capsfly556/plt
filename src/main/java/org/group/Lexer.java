package org.group;

import java.util.*;

public class Lexer {
    // Define token gitypes
    enum TokenType {
        // Keywords
        IF,

        // Operators
        PLUS, MINUS, MULTIPLY, DIVIDE, ASSIGN,

        // Delimiters
        SEMICOLON, LPAREN, RPAREN, LBRACE, RBRACE,

        // Identifiers and literals
        IDENTIFIER, LITERAL,

        // Errors and end of file
        ERROR, EOF
    }

    // Keyword mapping
    private static final Map<String, TokenType> KEYWORDS = new HashMap<>();
    static {
        KEYWORDS.put("if", TokenType.IF);
    }

    // Operator mapping
    private static final Map<String, TokenType> OPERATORS = new HashMap<>();
    static {
        OPERATORS.put("+", TokenType.PLUS);
        OPERATORS.put("-", TokenType.MINUS);
        OPERATORS.put("*", TokenType.MULTIPLY);
        OPERATORS.put("/", TokenType.DIVIDE);
        OPERATORS.put("=", TokenType.ASSIGN);
    }

    // Delimiter mapping
    private static final Map<Character, TokenType> DELIMITERS = new HashMap<>();
    static {
        DELIMITERS.put(';', TokenType.SEMICOLON);
        DELIMITERS.put('(', TokenType.LPAREN);
        DELIMITERS.put(')', TokenType.RPAREN);
        DELIMITERS.put('{', TokenType.LBRACE);
        DELIMITERS.put('}', TokenType.RBRACE);
    }

    // Token class
    static class Token {
        TokenType type;
        String value;

        Token(TokenType type, String value) {
            this.type = type;
            this.value = value;
        }

        @Override
        public String toString() {
            return "<" + type + ", '" + value + "'>";
        }
    }

    // Scanning function
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
                while (i < length && Character.isLetterOrDigit(input.charAt(i))) {
                    sb.append(input.charAt(i));
                    i++;
                }
                String word = sb.toString();
                if (KEYWORDS.containsKey(word)) {
                    tokens.add(new Token(KEYWORDS.get(word), word));
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
            else if (isOperatorStart(current)) {
                String op = String.valueOf(current);
                if (OPERATORS.containsKey(op)) {
                    tokens.add(new Token(OPERATORS.get(op), op));
                    i++;
                } else {
                    tokens.add(new Token(TokenType.ERROR, "Unknown operator '" + current + "' at index " + i));
                    i++;
                }
            }
            // Handle delimiters
            else if (DELIMITERS.containsKey(current)) {
                tokens.add(new Token(DELIMITERS.get(current), String.valueOf(current)));
                i++;
            }
            // Handle unrecognized characters (lexical errors)
            else {
                tokens.add(new Token(TokenType.ERROR, "Unrecognized token '" + current + "' at index " + i));
                i++;
            }
        }

        tokens.add(new Token(TokenType.EOF, ""));
        return tokens;
    }

    // Check if a character can be the start of an operator
    private static boolean isOperatorStart(char c) {
        return OPERATORS.containsKey(String.valueOf(c));
    }

    // Print the list of tokens
    private static void printTokens(List<Token> tokens) {
        for (Token token : tokens) {
            System.out.println(token);
        }
    }


    public static void main(String[] args) {
        // Test inputs
        List<String> inputs = Arrays.asList(
                // Example 1: Simple assignment statement
                "x = a + 5;",

                // Example 2: Simple if statement
                "if (x) { y = x - 1; }",

                // Example 3: Nested expressions
                "z = (x + y) * (a - b);",

                // Example 4: Input with errors
                "if x { y = 2; }",

                // Example 5: Multiple statements
                "{ x = 1; y = x + 2; }"
        );

        // Scan and parse each input
        for (int i = 0; i < inputs.size(); i++) {
            System.out.println("Example " + (i + 1) + " Tokens:");
            List<Token> tokens = scan(inputs.get(i));
            printTokens(tokens);

            System.out.println("Example " + (i + 1) + " AST:");
            Parser parser = new Parser(tokens);
            ASTNode ast = null;
            try {
                ast = parser.parseProgram();
                ast.print(""); // Implement the print method in ASTNode
            } catch (Parser.ParseException e) {
                System.out.println(e.getMessage());
                continue;
            }

            // Code Generation
            CodeGenerator codeGenerator = new CodeGenerator();
            List<String> code = codeGenerator.generate(ast);

            // Output generated code
            System.out.println("Generated Code:");
            for (String line : code) {
                System.out.println(line);
            }

            System.out.println(); // Separate different examples
        }
    }

}
