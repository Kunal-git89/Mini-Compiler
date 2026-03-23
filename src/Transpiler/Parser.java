package Transpiler;

import Transpiler.AST.*;
import java.util.*;

public class Parser
{
    List<ASTNode> Program = new ArrayList<>();
    private Lexer lexer;
    private Token currToken;
    public int line;

    public  Parser(Lexer l)
    {
        lexer = l;
        int i = lexer.line;
        currToken = lexer.nextToken();
        line = i;
    }

    private void advance()
    {
        line = lexer.line;
        currToken = lexer.nextToken();
    }

    private boolean checkCurrToken(Token.Token_type t)
    {
        return currToken.type == t;
    }

    private boolean consume(Token.Token_type t)
    {
        if(checkCurrToken(t))
        {
            advance();
            return true;
        }
        return false;
    }

    public List<ASTNode> parse()
    {
        while(!checkCurrToken(Token.Token_type.EOF))
        {
            ASTNode temp = parseStatement();
            if(temp == null)
            {
                throw new RuntimeException("Error while parsing in line : " + line);
            }
            Program.add(temp);
        }
        return Program;
    }

    private ASTNode parseStatement()
    {
        switch (currToken.type)
        {
            case Token.Token_type.Let :
            {
                advance();
                LetNode res = parseLetStmt();
                if (res == null) return null;
                return res;
            }

            case Token.Token_type.Identifier:
            {
                AssignmentNode res = parseAssignStmt();
                if(res == null) return null;
                return res;
            }
            case Token.Token_type.Input :
            {
                advance();
                InputNode res = parseInputStmt();
                if (res == null) return null;
                return res;
            }

            case Token.Token_type.Print :
            {
                advance();
                PrintNode res = parsePrint();
                if (res == null) return null;
                return res;
            }

            case Token.Token_type.Swap :
            {
                advance();
                SwapNode res = new SwapNode(line);
                if (!checkCurrToken(Token.Token_type.Identifier)) return null;
                res.left = currToken.name;
                advance();
                if (!checkCurrToken(Token.Token_type.Identifier)) return null;
                res.right = currToken.name;
                advance();
                if (!consume(Token.Token_type.Semicolon)) return null;
                return res;
            }

            case Token.Token_type.While :
            {
                advance();
                WhileNode res = new WhileNode(line);
                if (!consume(Token.Token_type.Lp)) return null;

                ExpressionNode e = parseExpression();
                if (e == null) return null;
                res.condition = e;

                if (!consume(Token.Token_type.Rp)) return null;

                BlockNode b = parseBlock();
                if (b == null) return null;
                res.block = b;

                return res;
            }

            case Token.Token_type.Continue :
            {
                advance();
                if (!consume(Token.Token_type.Semicolon)) return null;
                return new ContinueNode(line);
            }

            case Token.Token_type.Break :
            {
                advance();
                if (!consume(Token.Token_type.Semicolon)) return null;
                return new BreakNode(line);
            }

            case Token.Token_type.If :
            {
                advance();
                IfNode res = parseIf();
                if (res == null) return null;
                return res;
            }

            default : return null;
        }
    }

    private BlockNode parseBlock()
    {
        BlockNode res = new BlockNode(line);
        if(!consume(Token.Token_type.LBrac)) return null;
        while(!checkCurrToken(Token.Token_type.RBrac) && !checkCurrToken(Token.Token_type.EOF))
        {
            ASTNode temp = parseStatement();
            if (temp == null) return null;
            res.codeSnippet.add(temp);
        }
        if(!consume(Token.Token_type.RBrac)) return null;
        return res;
    }

    private LetNode parseLetStmt()
    {
        LetNode res = new LetNode(line);
        if(!checkCurrToken(Token.Token_type.Identifier)) return null;
        res.name = currToken.name;
        advance();
        if(!consume(Token.Token_type.Assign)) return null;
        ExpressionNode temp = parseExpression();
        if(temp == null) return null;
        res.expression = temp;
        if(!consume(Token.Token_type.Semicolon)) return null;
        return res;
    }

    private AssignmentNode parseAssignStmt()
    {
        AssignmentNode res = new AssignmentNode(line);
        res.name = currToken.name;
        advance();
        if(!consume(Token.Token_type.Assign)) return null;
        ExpressionNode temp = parseExpression();
        if(temp == null) return null;
        res.expression = temp;
        if(!consume(Token.Token_type.Semicolon)) return null;
        return res;
    }

    private InputNode parseInputStmt()
    {
        InputNode res = new InputNode(line);
        if(checkCurrToken(Token.Token_type.Identifier))
        {
            res.name = currToken.name;
            advance();
        }
        else if(checkCurrToken(Token.Token_type.Lp))
        {
            advance();
            if(!checkCurrToken(Token.Token_type.Identifier)) return null;
            res.name = currToken.name;
            advance();
            if(!consume(Token.Token_type.Rp)) return null;
        }
        if(!consume(Token.Token_type.Semicolon)) return null;
        return res;
    }

    private PrintNode parsePrint()
    {
        PrintNode res = new PrintNode(line);
        if(checkCurrToken(Token.Token_type.Lp))
        {
            advance();
            ExpressionNode temp = parseExpression();
            if(temp == null) return null;
            res.expression = temp;
            if(!consume(Token.Token_type.Rp)) return null;
        }
        else
        {
            ExpressionNode temp = parseExpression();
            if(temp == null) return null;
            res.expression = temp;
        }
        if(!consume(Token.Token_type.Semicolon)) return null;
        return res;
    }

    private IfNode parseIf()
    {
        IfNode res = new IfNode(line);
        if(!consume(Token.Token_type.Lp)) return null;
        ExpressionNode e = parseExpression();
        if(e == null) return null;
        res.condition = e;
        if(!consume(Token.Token_type.Rp)) return null;
        BlockNode b = parseBlock();
        if(b == null) return null;
        res.block = b;

        if(checkCurrToken(Token.Token_type.Elseif))
        {
            res.elseifPart = new ArrayList<>();
            ElseifNode ei = parseElseIfStmt();
            while (ei != null) {
                res.elseifPart.add(ei);
                ei = parseElseIfStmt();
            }
        }

        if(checkCurrToken(Token.Token_type.Else))
        {
            ElseNode el = parseElseStmt();
            if (el == null) return null;
            res.elsePart = el;
        }
        return res;
    }

    private ElseifNode parseElseIfStmt()
    {
        ElseifNode res = new ElseifNode(line);
        if(consume(Token.Token_type.Elseif))
        {
            if(!consume(Token.Token_type.Lp)) return null;
            ExpressionNode e = parseExpression();
            if(e == null) return null;
            res.condition = e;
            if(!consume(Token.Token_type.Rp)) return null;
            BlockNode b = parseBlock();
            if(b == null) return null;
            res.block = b;
            return res;
        }
        else return null;
    }

    private ElseNode parseElseStmt()
    {
        ElseNode res = new ElseNode(line);
        if(consume(Token.Token_type.Else))
        {
            BlockNode b = parseBlock();
            if(b == null) return null;
            res.block = b;
        }
        return res;
    }

    private ExpressionNode parseExpression()
    {
        ExpressionNode temp = parseEquality();
        if (temp == null) return null;
        return temp;
    }

    private ExpressionNode parseEquality()
    {
        ExpressionNode res = parseComparision();
        if(res == null) return null;

        while (checkCurrToken(Token.Token_type.Equals) || checkCurrToken(Token.Token_type.NotEquals))
        {
            ExpressionNode curr = new ExpressionNode(line);
            switch(currToken.type)
            {
                case Token.Token_type.Equals:
                {
                    curr.op = opType.Equals;
                    break;
                }
                case Token.Token_type.NotEquals:
                {
                    curr.op = opType.NotEquals;
                    break;
                }
            }
            advance();
            curr.rightNode = parseComparision();
            if(curr.rightNode == null) return null;
            curr.leftNode = res;
            res = curr;
        }
        return res;
    }

    private ExpressionNode parseComparision()
    {
        ExpressionNode res = parseRangeExp();
        if(res == null) return null;

        while(checkCurrToken(Token.Token_type.Less) || checkCurrToken(Token.Token_type.LE) || checkCurrToken(Token.Token_type.Greater) || checkCurrToken(Token.Token_type.GE))
        {
            ExpressionNode curr = new ExpressionNode(line);
            switch(currToken.type)
            {
                case Token.Token_type.Less :
                {
                    curr.op = opType.Less;
                    break;
                }
                case Token.Token_type.LE :
                {
                    curr.op = opType.LE;
                    break;
                }
                case Token.Token_type.Greater :
                {
                    curr.op = opType.Greater;
                    break;
                }
                case Token.Token_type.GE :
                {
                    curr.op = opType.GE;
                    break;
                }
            }
            advance();
            curr.rightNode = parseRangeExp();
            if(curr.rightNode == null) return null;
            curr.leftNode = res;
            res = curr;
        }
        return res;
    }

    private ExpressionNode parseRangeExp()
    {
        ExpressionNode left = parseTerm();
        if(left == null) return null;
        if(consume(Token.Token_type.Dots))
        {
            ExpressionNode res = new ExpressionNode(line);
            res.op = opType.Range;
            res.rightNode = parseTerm();
            if(res.rightNode == null) return null;
            res.leftNode = left;
            return res;
        }
        return left;
    }

    private ExpressionNode parseTerm()
    {
        ExpressionNode res = parseFactor();
        if(res == null) return null;

        while(checkCurrToken(Token.Token_type.Add) || checkCurrToken(Token.Token_type.Minus))
        {
            ExpressionNode curr = new ExpressionNode(line);
            switch(currToken.type)
            {
                case Token.Token_type.Add:
                {
                    curr.op = opType.Add;
                    break;
                }
                case Token.Token_type.Minus:
                {
                    curr.op = opType.Minus;
                    break;
                }
            }
            advance();
            curr.rightNode = parseFactor();
            if(curr.rightNode == null) return null;
            curr.leftNode = res;
            res = curr;
        }
        return res;
    }

    private ExpressionNode parseFactor()
    {
        ExpressionNode res = parsePrimary();
        if(res == null) return null;

        while(checkCurrToken(Token.Token_type.Multiply) || checkCurrToken(Token.Token_type.Divide) || checkCurrToken(Token.Token_type.Mod))
        {
            ExpressionNode curr = new ExpressionNode(line);
            switch(currToken.type)
            {
                case Token.Token_type.Multiply :
                {
                    curr.op = opType.Multiply;
                    break;
                }
                case Token.Token_type.Divide :
                {
                    curr.op = opType.Divide;
                    break;
                }
                case Token.Token_type.Mod :
                {
                    curr.op = opType.Mod;
                    break;
                }
            }
            advance();
            curr.rightNode = parsePrimary();
            if(curr.rightNode == null) return null;
            curr.leftNode = res;
            res = curr;
        }
        return res;
    }

    private ExpressionNode parsePrimary()
    {
        switch (currToken.type)
        {
            case Token.Token_type.Identifier :
                String name = currToken.name;
                advance();
                return new IdentifierNode(name , line);

            case Token.Token_type.Constant :
                int value = currToken.value;
                advance();
                return new ConstantNode(value , line);

            case Token.Token_type.Lp :
                advance();
                ExpressionNode temp = parseExpression();
                if(temp == null) return null;
                if(!consume(Token.Token_type.Rp)) return null;
                return temp;

            default: return null;
        }
    }
}