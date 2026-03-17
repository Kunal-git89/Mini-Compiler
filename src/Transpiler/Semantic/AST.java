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
        public void printNode() {}
    }

    abstract public static class StatementNode extends ASTNode
    {
        @Override
        public void printNode() {}
    }

    public static class BlockNode extends StatementNode
    {
        public List<ASTNode> codeSnippet;
        public BlockNode()
        {
            type = nodeType.BlockNode;
            codeSnippet = new ArrayList<>();
        }

        @Override
        public void printNode()
        {
            System.out.println("Block : ");
            for (ASTNode node : codeSnippet)
            {
                node.printNode();
            }
        }
    }

    public static class LetNode extends StatementNode
    {
        public LetNode() {type = nodeType.LetNode;}
        public String name ;
        public ExpressionNode expression;
        @Override
        public void printNode()
        {
            System.out.print("Let " + name + " ");
            expression.printNode();
        }

    }

    public static class AssignmentNode extends StatementNode
    {
        public AssignmentNode() {type = nodeType.AssignmentNode;}
        public String name ;
        public ExpressionNode expression;

        @Override
        public void printNode()
        {
            System.out.print("Assign " + name);
            expression.printNode();
        }
    }

    public static class InputNode extends StatementNode
    {
        public InputNode() {type = nodeType.InputNode;}
        public String name;

        @Override
        public void printNode()
        {
            System.out.println("Input " + name);
        }
    }

    public static class PrintNode extends StatementNode
    {
        public PrintNode() {type = nodeType.PrintNode;}
        public ExpressionNode expression;

        @Override
        public void printNode() {
            System.out.print("Print ");
            expression.printNode();
        }
    }

    public static class IfNode extends StatementNode
    {
        public ExpressionNode condition;
        public BlockNode block;
        public List<ElseifNode> elseifPart;
        public ElseNode elsePart;
        public IfNode()
        {
            type = nodeType.IfNode;
        }

        @Override
        public void printNode()
        {
            System.out.print("If condition ");
            condition.printNode();
            block.printNode();

            for(ElseifNode node : elseifPart)
            {
                node.printNode();
            }

            elsePart.printNode();
        }
    }

    public static class ElseifNode extends StatementNode
    {
        public ElseifNode() {type = nodeType.ElseIfNode;}
        public ExpressionNode condition;
        public BlockNode block;

        @Override
        public void printNode()
        {
            System.out.print("Elseif condition ");
            condition.printNode();
            block.printNode();
        }
    }

    public static class ElseNode extends StatementNode
    {
        public ElseNode() {type = nodeType.ElseNode;}
        public BlockNode block;

        @Override
        public void printNode()
        {
            System.out.print("Else");
            block.printNode();
        }
    }

    public static class SwapNode extends StatementNode
    {
        public SwapNode() {type = nodeType.SwapNode;}
        public String left , right;

        @Override
        public void printNode()
        {
            System.out.println("Swap : " + left + " , " + right);
        }
    }

    public static class WhileNode extends StatementNode
    {
        public WhileNode() {type = nodeType.WhileNode;}
        public ExpressionNode condition;
        public BlockNode block;

        @Override
        public void printNode()
        {
            System.out.print("While condition : ");
            condition.printNode();
            block.printNode();
        }
    }

    public static class ContinueNode extends StatementNode
    {
        public ContinueNode()
        {
            type = nodeType.ContinueNode;
        }

        @Override
        public void printNode() {
            System.out.println("Continue");
        }
    }

    public static class BreakNode extends StatementNode
    {
        public BreakNode()
        {
            type = nodeType.BreakNode;
        }

        @Override
        public void printNode() {
            System.out.println("Break");
        }
    } // Stmt ends here


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
        @Override
        public void printNode()
        {
            leftNode.printNode();
            System.out.print(op + " ");
            rightNode.printNode();
        }
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
        @Override
        public void printNode()
        {
            System.out.println("Identifier : " + name);
        }
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
        @Override
        public void printNode()
        {
            System.out.println("Constant : " + value);
        }
    }
}