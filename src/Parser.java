import java.util.*;

public class Parser
{
    List<AST.ASTNode> Program = new ArrayList<>();
    private Lexer lexer;
    private Token currToken;

    public void parser(Lexer l)
    {
        lexer = l;
        currToken = lexer.nextToken();
    }

    private void advance()
    {
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

    public List<AST.ASTNode> parse()
    {
        if(currToken.type == Token.Token_type.Start) advance();
        while(!checkCurrToken(Token.Token_type.EOF))
        {
            if(!parseStatement())
            {
                System.out.println("Error while parsing");
                return null;
            }
        }
        System.out.println("Parsed successfully");
        return Program;
    }

    private boolean parseStatement()
    {
        switch (currToken.type)
        {
            case Token.Token_type.Let :
                advance();
                if(!parseLetStmt()) return false;
                break;

            case Token.Token_type.Identifier:
                advance();
                if(!parseAssignStmt()) return false;
                break;

            case Token.Token_type.Input :
                advance();
                if(!parseInputStmt()) return false;
                break;

            case Token.Token_type.Print :
                advance();
                if(!parsePrint()) return false;
                break;

            case Token.Token_type.Swap :
                advance();
                if(!consume(Token.Token_type.Identifier)) return false;
                if(!consume(Token.Token_type.Identifier)) return false;
                if(!consume(Token.Token_type.Semicolon)) return false;
                return true;

            case Token.Token_type.While :
                advance();
                if(!consume(Token.Token_type.Lp)) return false;
                if(!parseExpression()) return false;
                if(!consume(Token.Token_type.Rp)) return false;
                if(!parseBlock()) return false;
                return true;

            case Token.Token_type.Continue :
                advance();
                if(!consume(Token.Token_type.Semicolon)) return false;

            case Token.Token_type.Break :
                advance();
                if(!consume(Token.Token_type.Semicolon)) return false;

            case Token.Token_type.If :
                advance();

        }
        return true;
    }

    private boolean parseBlock()
    {
        if(!consume(Token.Token_type.LBrac)) return false;
        if(!parseStatement()) return false;
        if(!consume(Token.Token_type.RBrac)) return false;
        return true;
    }

    private boolean parseLetStmt()
    {
        advance();
        if(!consume(Token.Token_type.Identifier)) return false;
        if(!consume(Token.Token_type.Assign)) return false;
        if(!parseExpression()) return false;
        if(!consume(Token.Token_type.Semicolon)) return false;
        return true;
    }

    private boolean parseAssignStmt()
    {
        if(!consume(Token.Token_type.Assign)) return false;
        if(!parseExpression()) return false;
        if(!consume(Token.Token_type.Semicolon)) return false;
        return true;
    }

    private boolean parseInputStmt()
    {
        if(checkCurrToken(Token.Token_type.Identifier))
        {
            advance();
        }
        else if(checkCurrToken(Token.Token_type.Lp))
        {
            advance();
            if(!consume(Token.Token_type.Identifier)) return false;
            if(!consume(Token.Token_type.Rp)) return false;
        }
        if(!consume(Token.Token_type.Semicolon)) return false;
        return true;
    }

    private boolean parsePrint()
    {
        if(checkCurrToken(Token.Token_type.Lp))
        {
            advance();
            if(!parseExpression()) return false;
            if(!consume(Token.Token_type.Rp)) return false;
        }
        else
        {
            if(!parseExpression()) return false;
        }
        if(!consume(Token.Token_type.Semicolon)) return false;
        return true;
    }

    private boolean parseIf()
    {
        if(!consume(Token.Token_type.Lp)) return false;
        if(!parseExpression()) return false;
        if(!consume(Token.Token_type.Rp)) return false;
        if(!parseBlock()) return false;
        if(!parseElseIfStmt()) return false;
        if(!parseElseStmt()) return false;
        return true;
    }

    private boolean parseElseIfStmt()
    {
        while(checkCurrToken(Token.Token_type.Elseif))
        {
            if(!consume(Token.Token_type.Elseif)) return false;
            if(!consume(Token.Token_type.Lp)) return false;
            if(!parseExpression()) return false;
            if(!consume(Token.Token_type.Rp)) return false;
            if(!parseBlock()) return false;
        }
        return true;
    }

    private boolean parseElseStmt()
    {
        if(consume(Token.Token_type.Else)) if(!parseBlock()) return false;
        return true;
    }

    private boolean parseExpression()
    {
        if(!parseEquality()) return false;
        return true;
    }

    private boolean parseEquality()
    {
        if(!parseComparision()) return false;
        while (checkCurrToken(Token.Token_type.Equals) || checkCurrToken(Token.Token_type.NotEquals))
        {
            advance();
            if(!parseComparision()) return false;
        }
        return true;
    }

    private boolean parseComparision()
    {
        if(!parseRangeExp()) return false;
        while(checkCurrToken(Token.Token_type.Less) || checkCurrToken(Token.Token_type.LE) || checkCurrToken(Token.Token_type.Greater) || checkCurrToken(Token.Token_type.GE))
        {
            advance();
            if(!parseRangeExp()) return false;
        }
        return true;
    }

    private boolean parseRangeExp()
    {
        if(!parseTerm()) return false;
        if(consume(Token.Token_type.Dots)) if(!parseTerm()) return false;
        return true;
    }

    private boolean parseTerm()
    {
        if(!parseFactor()) return false;
        while(checkCurrToken(Token.Token_type.Add) || checkCurrToken(Token.Token_type.Minus))
        {
            advance();
            if(!parseFactor()) return true;
        }
        return true;
    }

    private boolean parseFactor()
    {
        if(!parsePrimary()) return false;
        while(checkCurrToken(Token.Token_type.Multiply) || checkCurrToken(Token.Token_type.Divide) || checkCurrToken(Token.Token_type.Mod))
        {
            advance();
            if(!parsePrimary()) return false;
        }
        return true;
    }

    private boolean parsePrimary()
    {
        switch (currToken.type)
        {
            case Token.Token_type.Identifier :
                advance();
                break;

            case Token.Token_type.Constant :
                advance();
                break;

            case Token.Token_type.Lp :
                advance();
                if(!parseExpression()) return false;
                if(!consume(Token.Token_type.Rp)) return false;
                break;
        }
        return true;
    }
}