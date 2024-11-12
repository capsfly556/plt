// ASTNode.java
package org.group;

import java.util.List;

// Abstract AST node
abstract class ASTNode {
    public abstract void print(String prefix);
}

// Program node
class ProgramNode extends ASTNode {
    List<StatementNode> statements;

    ProgramNode(List<StatementNode> statements) {
        this.statements = statements;
    }

    @Override
    public void print(String prefix) {
        System.out.println(prefix + "Program");
        for (StatementNode stmt : statements) {
            stmt.print(prefix + "  ");
        }
    }
}

// Abstract statement node
abstract class StatementNode extends ASTNode {}

// Assignment statement node
class AssignmentNode extends StatementNode {
    String identifier;
    ExpressionNode expression;

    AssignmentNode(String identifier, ExpressionNode expression) {
        this.identifier = identifier;
        this.expression = expression;
    }

    @Override
    public void print(String prefix) {
        System.out.println(prefix + "Assignment");
        System.out.println(prefix + "  Identifier: " + identifier);
        expression.print(prefix + "  Expression: ");
    }
}

// If statement node
class IfStatementNode extends StatementNode {
    ExpressionNode condition;
    StatementNode thenStatement;

    IfStatementNode(ExpressionNode condition, StatementNode thenStatement) {
        this.condition = condition;
        this.thenStatement = thenStatement;
    }

    @Override
    public void print(String prefix) {
        System.out.println(prefix + "IfStatement");
        condition.print(prefix + "  Condition: ");
        thenStatement.print(prefix + "  Then: ");
    }
}

// Block node
class BlockNode extends StatementNode {
    List<StatementNode> statements;

    BlockNode(List<StatementNode> statements) {
        this.statements = statements;
    }

    @Override
    public void print(String prefix) {
        System.out.println(prefix + "Block");
        for (StatementNode stmt : statements) {
            stmt.print(prefix + "  ");
        }
    }
}

// Expression node
abstract class ExpressionNode extends ASTNode {}

// Binary expression node
class BinaryExpressionNode extends ExpressionNode {
    ExpressionNode left;
    Lexer.TokenType operator;
    ExpressionNode right;

    BinaryExpressionNode(ExpressionNode left, Lexer.TokenType operator, ExpressionNode right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public void print(String prefix) {
        System.out.println(prefix + "BinaryExpression (" + operator + ")");
        left.print(prefix + "  Left: ");
        right.print(prefix + "  Right: ");
    }
}

// Identifier node
class IdentifierNode extends ExpressionNode {
    String name;

    IdentifierNode(String name) {
        this.name = name;
    }

    @Override
    public void print(String prefix) {
        System.out.println(prefix + "Identifier: " + name);
    }
}

// Literal node
class LiteralNode extends ExpressionNode {
    String value;

    LiteralNode(String value) {
        this.value = value;
    }

    @Override
    public void print(String prefix) {
        System.out.println(prefix + "Literal: " + value);
    }
}
