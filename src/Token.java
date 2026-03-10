public class Token
{
    enum Token_type
    {
        Identifier , Constant,
        Add , Minus , Multiply , Divide, Less , LE, Greater , GE, Equals , NotEquals,
        Assign ,Semicolon,
        Lp , Rp , LBrac , RBrac,
        Let , If , Elseif , Else , While , Continue , Break , Input , Print,
        Error , EOF , Start
    }
    public String name;
    public int value;
    public Token_type type;

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
