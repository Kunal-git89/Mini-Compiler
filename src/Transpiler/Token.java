package Transpiler;

public class Token
{
    public enum Token_type
    {
        Identifier , Constant , Range,
        Add , Minus , Multiply , Divide , Mod , Less , LE , Greater , GE, Equals , NotEquals,
        Assign , Semicolon , Dots,
        Lp , Rp , LBrac , RBrac,
        Let , If , Elseif , Else , While , Continue , Break , Input , Print , Swap,
        Error , EOF , Start
    }
    public String name;
    public int value;
    public Token_type type;

    public void printToken()
    {
        if(type == Token_type.Identifier)
        {
            System.out.println(type + " " + name);
        }
        else if(type == Token_type.Constant)
        {
            System.out.println(type + " " + value);
        }
        else System.out.println(type);
    }

    public Token(Token_type t)
    {
        type = t;
    }

    public Token(Token_type t , String s)
    {
        type = t;
        name = s;
    }

    public Token(Token_type t , int v)
    {
        type = t;
        value = v;
    }

    public Token(Token_type t , String s , int v)
    {
        type = t;
        name = s;
        value = v;
    }
    public Token()
    {
        type = Token_type.Start;
    };
}