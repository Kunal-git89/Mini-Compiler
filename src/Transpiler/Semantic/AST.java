package Transpiler.Semantic;

import java.util.*;

public class AST
{
    public static class ASTNode
    {
        enum nodeType
        {
            BlockNode , LetNode , AssignmentNode , InputNode , PrintNode , IfNode , ElseIfNode , ElseNode , SwapNode , WhileNode , ContinueNode , BreakNode , ExpressionNode
        }
        nodeType type;
    }

    public static class StatementNode extends ASTNode {}

    public static class BlockNode extends StatementNode
    {
        public List<AST.ASTNode> codeSnippet;
        public BlockNode()
        {
            type = nodeType.BlockNode;
            codeSnippet = new ArrayList<>();
        }
    }

    public static class LetNode extends StatementNode
    {
        public LetNode() {type = nodeType.LetNode;}
        public String name ;
        public ExpressionNode expression;
    }

    public static class AssignmentNode extends StatementNode
    {
        public AssignmentNode() {type = nodeType.AssignmentNode;}
        public String name ;
        public ExpressionNode expression;
    }

    public static class InputNode extends StatementNode
    {
        public InputNode() {type = nodeType.InputNode;}
        public String name;
    }

    public static class PrintNode extends StatementNode
    {
        public PrintNode() {type = nodeType.PrintNode;}
        public ExpressionNode expression;
    }

    public static class IfNode extends StatementNode
    {
        public ExpressionNode condition;
        public BlockNode block;
        public List<AST.ElseifNode> elseifPart;
        public ElseNode elsePart;
        public IfNode()
        {
            type = nodeType.IfNode;
        }
    }

    public static class ElseifNode extends StatementNode
    {
        public ElseifNode() {type = nodeType.ElseIfNode;}
        public ExpressionNode condition;
        public BlockNode block;
    }

    public static class ElseNode extends StatementNode
    {
        public ElseNode() {type = nodeType.ElseNode;}
        public BlockNode block;
    }

    public static class SwapNode extends StatementNode
    {
        public SwapNode() {type = nodeType.SwapNode;}
        public String left , right;
    }

    public static class WhileNode extends StatementNode
    {
        public WhileNode() {type = nodeType.WhileNode;}
        public ExpressionNode condition;
        public BlockNode block;
    }

    public static class ContinueNode extends StatementNode {public ContinueNode() {type = nodeType.ContinueNode;}}

    public static class BreakNode extends StatementNode {public BreakNode() {type = nodeType.BreakNode;}} // Stmt Nodes end here


    public static class ExpressionNode extends ASTNode
    {
        public ExpressionNode() {type = nodeType.ExpressionNode;}
        public enum opType
        {
            Identifier , Constant , Add , Minus , Multiply , Divide , Mod , Range , Less , LE , Greater , GE, Equals , NotEquals,
        }
        public ExpressionNode leftNode;
        public opType op;
        public ExpressionNode rightNode;
    }

    public static class IdentifierNode extends ExpressionNode
    {
        public IdentifierNode () {}
        public IdentifierNode(String s)
        {
            op = opType.Identifier;
            name = s;
        }
        public String name;
    }

    public static class ConstantNode extends ExpressionNode
    {
        public ConstantNode () {}
        public ConstantNode(int v)
        {
            op = opType.Constant;
            value = v;
        }
        int value;
    }
}