// CodeGenerator.java
package org.group;

import java.util.*;

public class CodeGenerator {
    private int tempCounter = 0;
    private List<String> codeLines = new ArrayList<>();

    public List<String> generate(ASTNode node) {
        generateNode(node);
        return codeLines;
    }

    private String generateNode(ASTNode node) {
        if (node instanceof ProgramNode) {
            return generateProgramNode((ProgramNode) node);
        } else if (node instanceof AssignmentNode) {
            return generateAssignmentNode((AssignmentNode) node);
        } else if (node instanceof IfStatementNode) {
            return generateIfStatementNode((IfStatementNode) node);
        } else if (node instanceof BlockNode) {
            return generateBlockNode((BlockNode) node);
        } else if (node instanceof BinaryExpressionNode) {
            return generateBinaryExpressionNode((BinaryExpressionNode) node);
        } else if (node instanceof IdentifierNode) {
            return generateIdentifierNode((IdentifierNode) node);
        } else if (node instanceof LiteralNode) {
            return generateLiteralNode((LiteralNode) node);
        } else {
            throw new UnsupportedOperationException("Unsupported ASTNode type: " + node.getClass());
        }
    }

    private String generateProgramNode(ProgramNode node) {
        for (StatementNode stmt : node.statements) {
            generateNode(stmt);
        }
        return null; // Program node does not return a value
    }

    private String generateAssignmentNode(AssignmentNode node) {
        String exprTemp = generateNode(node.expression);
        // Assign the result to the variable
        codeLines.add(node.identifier + " = " + exprTemp);
        return null; // Assignment does not return a value
    }

    private String generateIfStatementNode(IfStatementNode node) {
        String condTemp = generateNode(node.condition);
        String labelElse = generateLabel("else");
        String labelEnd = generateLabel("endif");

        codeLines.add("ifFalse " + condTemp + " goto " + labelElse);

        generateNode(node.thenStatement);

        codeLines.add("goto " + labelEnd);
        codeLines.add(labelElse + ":");

        // Else part can be added if needed

        codeLines.add(labelEnd + ":");

        return null; // If statement does not return a value
    }

    private String generateBlockNode(BlockNode node) {
        for (StatementNode stmt : node.statements) {
            generateNode(stmt);
        }
        return null; // Block does not return a value
    }

    private String generateBinaryExpressionNode(BinaryExpressionNode node) {
        String leftTemp = generateNode(node.left);
        String rightTemp = generateNode(node.right);
        String resultTemp = generateTemp();

        String op;
        switch (node.operator) {
            case PLUS:
                op = "+";
                break;
            case MINUS:
                op = "-";
                break;
            case MULTIPLY:
                op = "*";
                break;
            case DIVIDE:
                op = "/";
                break;
            default:
                throw new UnsupportedOperationException("Unsupported operator: " + node.operator);
        }

        codeLines.add(resultTemp + " = " + leftTemp + " " + op + " " + rightTemp);
        return resultTemp;
    }

    private String generateIdentifierNode(IdentifierNode node) {
        // Return the identifier name
        return node.name;
    }

    private String generateLiteralNode(LiteralNode node) {
        String temp = generateTemp();
        codeLines.add(temp + " = " + node.value);
        return temp;
    }

    private String generateTemp() {
        return "t" + (tempCounter++);
    }

    private String generateLabel(String base) {
        return base + "_" + (tempCounter++);
    }
}
