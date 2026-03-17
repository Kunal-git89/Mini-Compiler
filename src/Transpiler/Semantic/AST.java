package Transpiler.Semantic;

import java.beans.Expression;
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
        public int line;
    }

    abstract public static class StatementNode extends ASTNode
    {
        @Override
        public void printNode() {}
    }

    public static class BlockNode extends StatementNode
    {
        public List<ASTNode> codeSnippet;
        public BlockNode(){}
        public BlockNode(int a)
        {
            type = nodeType.BlockNode;
            codeSnippet = new ArrayList<>();
            line = a;
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
        public LetNode(){}
        public LetNode(int a)
        {
            type = nodeType.LetNode;
            line =a ;
        }
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
        public AssignmentNode(){}
        public AssignmentNode(int a)
        {
            type = nodeType.AssignmentNode;
            line = a;
        }
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
        public InputNode(){}
        public InputNode(int a)
        {
            type = nodeType.InputNode;
            line = a;
        }
        public String name;

        @Override
        public void printNode()
        {
            System.out.println("Input " + name);
        }
    }

    public static class PrintNode extends StatementNode
    {
        public PrintNode(){}
        public PrintNode(int a)
        {
            type = nodeType.PrintNode;
            line = a;
        }
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
        public IfNode(){}
        public IfNode(int a)
        {
            type = nodeType.IfNode;
            line = a;
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
        public ElseifNode() {}
        public ElseifNode(int a)
        {
            type = nodeType.ElseIfNode;
            line = a;
        }
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
        public ElseNode() {}
        public ElseNode(int a)
        {
            type = nodeType.ElseNode;
            line = a;
        }
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
        public SwapNode() {}
        public SwapNode(int a)
        {
            type = nodeType.SwapNode;
            line = a;
        }
        public String left , right;

        @Override
        public void printNode()
        {
            System.out.println("Swap : " + left + " , " + right);
        }
    }

    public static class WhileNode extends StatementNode
    {
        public WhileNode(){}
        public WhileNode(int a)
        {
            type = nodeType.WhileNode;
            line = a;
        }
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
        public ContinueNode(){}
        public ContinueNode(int a)
        {
            type = nodeType.ContinueNode;
            line = a;
        }

        @Override
        public void printNode() {
            System.out.println("Continue");
        }
    }

    public static class BreakNode extends StatementNode
    {
        public BreakNode(){}
        public BreakNode(int a)
        {
            type = nodeType.BreakNode;
            line = a;
        }

        @Override
        public void printNode() {
            System.out.println("Break");
        }
    } // Stmt ends here


    public static class ExpressionNode extends ASTNode
    {
        public ExpressionNode(){}
        public ExpressionNode(int a)
        {
            type = nodeType.ExpressionNode;
            line = a;
        }
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
        public IdentifierNode(String s , int a)
        {
            op = opType.Identifier;
            name = s;
            line = a;
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
        public ConstantNode(int v , int a)
        {
            op = opType.Constant;
            value = v;
            line =a;
        }
        int value;
        @Override
        public void printNode()
        {
            System.out.println("Constant : " + value);
        }
    }
}