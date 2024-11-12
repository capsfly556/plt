// Parser.java
package org.group;

import java.util.*;

public class Parser {
    private List<Lexer.Token> tokens;
    private int position;

    public Parser(List<Lexer.Token> tokens) {
        this.tokens = tokens;
        this.position = 0;
    }

    public ASTNode parseProgram() throws ParseException {
        List<StatementNode> statements = new ArrayList<>();
        while (!isAtEnd()) {
            statements.add(parseStatement());
        }
        return new ProgramNode(statements);
    }

    private StatementNode parseStatement() throws ParseException {
        if (match(Lexer.TokenType.IF)) {
            return parseIfStatement();
        } else if (match(Lexer.TokenType.LBRACE)) {
            return parseBlock();
        } else if (check(Lexer.TokenType.IDENTIFIER)) {
            return parseAssignmentStatement();
        } else {
            throw error("Expected statement");
        }
    }

    private IfStatementNode parseIfStatement() throws ParseException {
        consume(Lexer.TokenType.LPAREN, "Expected '(' after 'if'");
        ExpressionNode condition = parseExpression();
        consume(Lexer.TokenType.RPAREN, "Expected ')' after condition");
        StatementNode thenStatement = parseStatement();
        return new IfStatementNode(condition, thenStatement);
    }

    private AssignmentNode parseAssignmentStatement() throws ParseException {
        Lexer.Token identifier = consume(Lexer.TokenType.IDENTIFIER, "Expected identifier");
        consume(Lexer.TokenType.ASSIGN, "Expected '=' after identifier");
        ExpressionNode expression = parseExpression();
        consume(Lexer.TokenType.SEMICOLON, "Expected ';' after expression");
        return new AssignmentNode(identifier.value, expression);
    }

    private BlockNode parseBlock() throws ParseException {
        List<StatementNode> statements = new ArrayList<>();
        while (!check(Lexer.TokenType.RBRACE) && !isAtEnd()) {
            statements.add(parseStatement());
        }
        consume(Lexer.TokenType.RBRACE, "Expected '}' after block");
        return new BlockNode(statements);
    }

    private ExpressionNode parseExpression() throws ParseException {
        ExpressionNode expr = parseTerm();
        while (match(Lexer.TokenType.PLUS, Lexer.TokenType.MINUS)) {
            Lexer.TokenType operator = previous().type;
            ExpressionNode right = parseTerm();
            expr = new BinaryExpressionNode(expr, operator, right);
        }
        return expr;
    }

    private ExpressionNode parseTerm() throws ParseException {
        ExpressionNode expr = parseFactor();
        while (match(Lexer.TokenType.MULTIPLY, Lexer.TokenType.DIVIDE)) {
            Lexer.TokenType operator = previous().type;
            ExpressionNode right = parseFactor();
            expr = new BinaryExpressionNode(expr, operator, right);
        }
        return expr;
    }

    private ExpressionNode parseFactor() throws ParseException {
        if (match(Lexer.TokenType.IDENTIFIER)) {
            return new IdentifierNode(previous().value);
        } else if (match(Lexer.TokenType.LITERAL)) {
            return new LiteralNode(previous().value);
        } else if (match(Lexer.TokenType.LPAREN)) {
            ExpressionNode expr = parseExpression();
            consume(Lexer.TokenType.RPAREN, "Expected ')' after expression");
            return expr;
        } else {
            throw error("Expected expression");
        }
    }

    // Helper methods
    private boolean match(Lexer.TokenType... types) {
        for (Lexer.TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private boolean check(Lexer.TokenType type) {
        if (isAtEnd()) return false;
        return peek().type == type;
    }

    private Lexer.Token advance() {
        if (!isAtEnd()) position++;
        return previous();
    }

    private boolean isAtEnd() {
        return peek().type == Lexer.TokenType.EOF;
    }

    private Lexer.Token peek() {
        return tokens.get(position);
    }

    private Lexer.Token previous() {
        return tokens.get(position - 1);
    }

    private Lexer.Token consume(Lexer.TokenType type, String message) throws ParseException {
        if (check(type)) return advance();
        throw error(message);
    }

    private ParseException error(String message) {
        return new ParseException("Parse error at token '" + peek().value + "': " + message);
    }

    public static class ParseException extends Exception {
        public ParseException(String message) {
            super(message);
        }
    }
}
