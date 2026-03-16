package Transpiler.Semantic;

import java.util.*;

public class AST
{
    public class ASTNode
    {
        enum nodeType
        {
            BlockNode , LetNode , AssignmentNode , InputNode , PrintNode , IfNode , ElseIfNode , ElseNode , SwapNode , WhileNode , ContinueNode , BreakNode , ExpressionNode
        }
        nodeType type;
    }

    public class StatementNode extends ASTNode {}

    public class BlockNode extends StatementNode
    {
        public BlockNode() {type = nodeType.BlockNode;}
        List<AST.ASTNode> snippet ;
    }

    public class LetNode extends StatementNode
    {
        public LetNode() {type = nodeType.LetNode;}
        String name ;
        ExpressionNode expression;
    }

    public class AssignmentNode extends StatementNode
    {
        public AssignmentNode() {type = nodeType.AssignmentNode;}
        String name ;
        ExpressionNode expression;
    }

    public class InputNode extends StatementNode
    {
        public InputNode() {type = nodeType.InputNode;}
        String name;
    }

    public class PrintNode extends StatementNode
    {
        public PrintNode() {type = nodeType.PrintNode;}
        ExpressionNode expression;
    }

    public class IfNode extends StatementNode
    {
        public IfNode() {type = nodeType.IfNode;}
        ExpressionNode condition;
        BlockNode block;
        List<AST.ElseifNode> elseifPart;
        ElseNode elsePart;
    }

    public class ElseifNode extends StatementNode
    {
        public ElseifNode() {type = nodeType.ElseIfNode;}
        ExpressionNode condition;
        BlockNode block;
    }

    public class ElseNode extends StatementNode
    {
        public ElseNode() {type = nodeType.ElseNode;}
        BlockNode block;
    }

    public class SwapNode extends StatementNode
    {
        public SwapNode() {type = nodeType.SwapNode;}
        String left , right;
    }

    public class WhileNode extends StatementNode
    {
        public WhileNode() {type = nodeType.WhileNode;}
        ExpressionNode condition;
        BlockNode block;
    }

    public class ContinueNode extends StatementNode {public ContinueNode() {type = nodeType.ContinueNode;}}

    public class BreakNode extends StatementNode {public BreakNode() {type = nodeType.BreakNode;}} // Stmt Nodes end here


    public class ExpressionNode extends ASTNode
    {
        public ExpressionNode() {type = nodeType.ExpressionNode;}
        enum opType
        {
            Identifier , Constant , Add , Minus , Multiply , Divide , Mod , Range
        }
        ExpressionNode leftNode;
        opType op;
        ExpressionNode rightNode;
    }

    public class IdentifierNode extends ExpressionNode { String name;}

    public class ConstantNode extends ExpressionNode {int value;}

    public class RangeNode extends ExpressionNode {int start , end;}
}