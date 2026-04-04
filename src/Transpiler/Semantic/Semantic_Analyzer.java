package Transpiler.Semantic;

import Transpiler.AST.*;
import java.util.*;

public class Semantic_Analyzer
{
    private SymbolTable_Manager symboltable = new SymbolTable_Manager();
    private List<ASTNode> AST;
    private int loopdepth = 0;

    public Semantic_Analyzer(List<ASTNode> l)
    {
        AST = l;
    }

    public void start()
    {
        symboltable.addScope(); //Global scope start
        for(ASTNode node : AST)
        {
            analyze(node);
        }
        symboltable.exitScope(); //Global scope end
    }

    private void analyze(ASTNode node) // Actual analysis
    {
        switch (node.type)
        {
            case nodeType.LetNode :
            {
                LetNode temp = (LetNode) node;
                symbolType t = analyzeExpression(temp.expression);
                if(t == symbolType.Bool) throw new RuntimeException("Expression expected during intialization , line : " + temp.line);
                Symbol s = new Symbol(temp.name , t);
                if(!symboltable.declare(s)) throw new RuntimeException("Duplicate variable declaration is not allowed , line : " + node.line);
                return;
            }
            case nodeType.AssignmentNode :
            {
                AssignmentNode temp = (AssignmentNode) node;
                Symbol s = symboltable.lookup(temp.name);
                if(s == null) throw new RuntimeException("Variable " + temp.name + " need to be declared ,line : " + temp.line);
                symbolType t = analyzeExpression(temp.expression);
                if( t == symbolType.Bool) throw new RuntimeException("Expression expected in intialization , line : " + temp.line);
                s.type = t;
                return;
            }
            case nodeType.InputNode:
            {
                InputNode temp = (InputNode) node;
                Symbol s =symboltable.lookup(temp.name);
                if(s == null) throw new RuntimeException("Variable " + temp.name + " need to be declared in line : " + temp.line);
                return;
            }
            case nodeType.PrintNode:
            {
                PrintNode temp = (PrintNode) node;
                symbolType t = analyzeExpression(temp.expression);
                return;
            }
            case IfNode:
            {
                IfNode temp = (IfNode) node;
                if(analyzeExpression(temp.condition) != symbolType.Bool) throw new RuntimeException("Condition exprected in if statement , line : " + temp.line);
                analyzeBlock(temp.block);
                if(temp.elseifPart != null) for(ElseifNode n : temp.elseifPart) analyzeElseIfPart(n);
                if(temp.elsePart != null) analyzeBlock(temp.elsePart.block);
                return;
            }
            case SwapNode :
            {
                SwapNode temp = (SwapNode) node;
                Symbol s = symboltable.lookup(temp.left);
                if(s == null) throw new RuntimeException("Variable " + temp.left + " need to be declared ,line : " + temp.line);
                s = symboltable.lookup(temp.right);
                if(s==null) throw new RuntimeException("Variable " + temp.right + " need to be declared ,line : " + temp.line);
                return;
            }
            case WhileNode:
            {
                WhileNode temp = (WhileNode) node;
                loopdepth++;
                symbolType t =analyzeExpression(temp.condition);
                if(t != symbolType.Bool) throw new RuntimeException("Condition expected in while statement , line : " + temp.line);
                analyzeBlock(temp.block);
                loopdepth--;
                return;
            }
            case ContinueNode:
            {
                if(loopdepth < 1) throw new RuntimeException("Continue should be used within loop , line : " + node.line);
                return;
            }
            case BreakNode:
            {
                if(loopdepth < 1) throw new RuntimeException("Break should be used within loop , line : " + node.line);
                return;
            }
            case BlockNode:
            {
                analyzeBlock((BlockNode) node);
                return;
            }
        }
    }

    private void analyzeBlock(BlockNode node)
    {
        Iterator<ASTNode> itr = node.codeSnippet.iterator();
        symboltable.addScope();
        while(itr.hasNext())
        {
            analyze(itr.next());
        }
        symboltable.exitScope();
    }

    private void analyzeElseIfPart(ElseifNode node)
    {
        symbolType t = analyzeExpression(node.condition);
        if(t != symbolType.Bool) throw new RuntimeException("Condition exprected in if statement , line : " + node.line);
        analyzeBlock(node.block);
    }

    private symbolType analyzeExpression(ExpressionNode node)
    {
        checkExpressionIdentifiers(node);
        if(node.op != opType.Equals && node.op != opType.NotEquals && node.op != opType.Less && node.op != opType.LE && node.op != opType.Greater && node.op != opType.GE)
        {
            if(node.op == opType.Range) return symbolType.Range;
            return symbolType.Int;
        }
        else if (analyzeCondition(node))
        {
            return symbolType.Bool;
        }
        throw new RuntimeException("Expression is not correctly framed , line : " + node.line);
    }

    private void checkExpressionIdentifiers(ExpressionNode node)
    {
        if(node == null) return;
        if(node instanceof IdentifierNode) {
            IdentifierNode temp = (IdentifierNode) node;
            Symbol s = symboltable.lookup(temp.name);
            if(s == null) throw new RuntimeException("Variable " + temp.name + " need to be declared in line : " + temp.line);
            return;
        }
        checkExpressionIdentifiers(node.leftNode);
        checkExpressionIdentifiers(node.rightNode);
    }

    private boolean analyzeCondition(ExpressionNode node)
    {
        if(node.op == opType.Equals || node.op == opType.NotEquals || node.op == opType.Less || node.op == opType.LE || node.op == opType.Greater || node.op == opType.GE)
        {
            if(node.leftNode.op != opType.Equals && node.leftNode.op != opType.NotEquals && node.leftNode.op != opType.Less && node.leftNode.op != opType.LE && node.leftNode.op != opType.Greater && node.leftNode.op != opType.GE)
                if(node.rightNode.op != opType.Equals && node.rightNode.op != opType.NotEquals && node.rightNode.op != opType.Less && node.rightNode.op != opType.LE && node.rightNode.op != opType.Greater && node.rightNode.op != opType.GE)
                    return true;
            throw new RuntimeException("Multiple comparisions are not allowed , line : " + node.line);
        }
        return false;
    }
}
