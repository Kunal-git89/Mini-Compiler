import java.util.*;



class Variable
{
    private int[] limit;
    private Boolean b;
    public variableType type;
    public Variable(int[] l)
    {
        type = variableType.Range;
        limit = new int[2];
        limit[0] = l[0];
        limit[1] = l[1];
    }
    public Variable(int a)
    {
        type = variableType.Int;
        limit = new int[1];
        limit[0] = a;
    }
    public Variable(boolean a)
    {
        type = variableType.Bool;
        b = a;
    }

    public boolean isInt(){ return type == variableType.Int;}
    public boolean isRange(){ return type == variableType.Range;}
    public boolean isBool() {return type == variableType.Bool;}

    public void assignInt(int a)
    {
        limit[0] = a;
    }
    public int getInt()
    {
        return limit[0];
    }

    public void assignRange(int[] l)
    {
        if(l[0] > l[1]) throw new RuntimeException("Start of range can't be greater than the end");
        limit[0] = l[0];
        limit[1] = l[1];
    }
    public int[] getRange()
    {
        return limit;
    }

    public void assignBool(boolean a ) {b = a;}
    public boolean getBool() {return b;}

    public void printVariable()
    {
        switch (type)
        {
            case variableType.Int:
                System.out.println(limit[0]);
                break;
            case variableType.Range:
                System.out.println(limit[0] + ".." + limit[1]);
                break;
            case variableType.Bool :
                System.out.println(b);
                break;
        }
    }
}
enum variableType {Int , Range , Bool}




class Operation
{
    static public Variable Range(Variable a , Variable b)
    {
        if(!(a.isInt() && b.isInt())) return null;
        if(a.getInt() <= b.getInt()) return new Variable(new int[] {a.getInt() , b.getInt()});
        return null;
    }

    static public void Swap(Variable a , Variable b)
    {
        Variable temp = a;
        a = b;
        b = temp;
    }

    static public Variable Add(Variable a , Variable b)
    {
        switch(a.type)
        {
            case variableType.Int:
            {
                switch(b.type)
                {
                    case variableType.Int:
                    {
                        return new Variable(a.getInt() + b.getInt());
                    }
                    case variableType.Range:
                    {
                        return new Variable(new int[] {a.getInt() + b.getRange()[0] , a.getInt() + b.getRange()[1]});
                    }
                    case variableType.Bool:
                    {
                        return null;
                    }
                }
            }
            case variableType.Range:
            {
                switch(b.type)
                {
                    case variableType.Int: {
                        return new Variable(new int[]{a.getRange()[0] + b.getInt(), a.getRange()[1] + b.getInt()});
                    }
                    case variableType.Range: {
                        return new Variable(new int[]{a.getRange()[0] + b.getRange()[0], a.getRange()[1] + b.getRange()[1]});
                    }
                    case variableType.Bool: {
                        return null;
                    }
                }
            }
            case variableType.Bool:
            {
                switch(b.type)
                {
                    case variableType.Int:
                    {
                        return null;
                    }
                    case variableType.Range:
                    {
                        return null;
                    }
                    case variableType.Bool:
                    {
                        return new Variable(a.getBool() && b.getBool());
                    }
                }
            }
        }
        return null;
    }

    static public Variable Minus(Variable a , Variable b)
    {
        switch(a.type)
        {
            case variableType.Int:
            {
                switch(b.type)
                {
                    case variableType.Int:
                    {
                        return new Variable(a.getInt() - b.getInt());
                    }
                    case variableType.Range:
                    {
                        int x = a.getInt() - b.getRange()[0] , y = a.getInt() - b.getRange()[1];
                        if(x > y) return null;
                        return new Variable(new int[] { x , y});
                    }
                    case variableType.Bool:
                    {
                        return null;
                    }
                }
            }
            case variableType.Range:
            {
                switch(b.type)
                {
                    case variableType.Int:
                    {
                        int x = a.getRange()[0] - b.getInt() , y = a.getRange()[1] - b.getInt();
                        if(x > y) return null;
                        return new Variable(new int[] { x , y});
                    }
                    case variableType.Range:
                    {
                        int x = a.getRange()[0] - b.getRange()[0] , y = a.getRange()[1] - b.getRange()[1];
                        if(x > y) return null;
                        return new Variable(new int[] { x , y});
                    }
                    case variableType.Bool:
                    {
                        return null;
                    }
                }
            }
            case variableType.Bool:
            {
                switch(b.type)
                {
                    case variableType.Int:
                    {
                        return null;
                    }
                    case variableType.Range:
                    {
                        return null;
                    }
                    case variableType.Bool:
                    {
                        return new Variable(a.getBool() || b.getBool());
                    }
                }
            }
        }
        return null;
    }

    static public Variable Multiply(Variable a , Variable b)
    {
        if(a.isInt() && b.isInt()) return new Variable(a.getInt() * b.getInt());
        return null;
    }

    static public Variable Divide(Variable a , Variable b)
    {
        if(a.isInt() && b.isInt()) return new Variable(a.getInt() / b.getInt());
        return null;
    }

    static public Variable Mod(Variable a , Variable b)
    {
        if(a.isInt() && b.isInt()) return new Variable(a.getInt() % b.getInt());
        return null;
    }

    static public Variable Less(Variable a , Variable b)
    {
        switch(a.type)
        {
            case variableType.Int:
            {
                switch(b.type)
                {
                    case variableType.Int:
                    {
                        return new Variable(a.getInt() < b.getInt());
                    }
                    case variableType.Range:
                    {
                        return new Variable(a.getInt() < b.getRange()[0]);
                    }
                }
            }
            case variableType.Range:
            {
                switch(b.type)
                {
                    case variableType.Int:
                    {
                        return new Variable(b.getRange()[1] < a.getInt());
                    }
                    case variableType.Range:
                    {
                        return new Variable(a.getRange()[1] < b.getRange()[0]);
                    }
                }
            }
        }
        return null;
    }

    static public Variable LE(Variable a , Variable b)
    {
        switch(a.type)
        {
            case variableType.Int:
            {
                switch(b.type)
                {
                    case variableType.Int:
                    {
                        return new Variable(a.getInt() <= b.getInt());
                    }
                    case variableType.Range:
                    {
                        return new Variable(a.getInt() <= b.getRange()[1]);
                    }
                }
            }
            case variableType.Range:
            {
                switch(b.type)
                {
                    case variableType.Int:
                    {
                        return new Variable(a.getRange()[0] <= b.getInt());
                    }
                    case variableType.Range:
                    {
                        return new Variable(a.getRange()[1] <= b.getRange()[1]);
                    }
                }
            }
        }
        return null;
    }

    static public Variable Greater(Variable a , Variable b)
    {
        switch(a.type)
        {
            case variableType.Int:
            {
                switch(b.type)
                {
                    case variableType.Int:
                    {
                        return new Variable(a.getInt() > b.getInt());
                    }
                    case variableType.Range:
                    {
                        return new Variable(a.getInt() > b.getRange()[1]);
                    }
                }
            }
            case variableType.Range:
            {
                switch(b.type)
                {
                    case variableType.Int:
                    {
                        return new Variable(a.getRange()[0] > b.getInt());
                    }
                    case variableType.Range:
                    {
                        return new Variable(a.getRange()[0] > b.getRange()[1]);
                    }
                }
            }
        }
        return null;
    }

    static public Variable GE(Variable a , Variable b)
    {
        switch(a.type)
        {
            case variableType.Int:
            {
                switch(b.type)
                {
                    case variableType.Int:
                    {
                        return new Variable(a.getInt() >= b.getInt());
                    }
                    case variableType.Range:
                    {
                        return new Variable(a.getInt() >= b.getRange()[0]);
                    }
                }
            }
            case variableType.Range:
            {
                switch(b.type)
                {
                    case variableType.Int:
                    {
                        return new Variable(a.getRange()[1] >= b.getInt());
                    }
                    case variableType.Range:
                    {
                        return new Variable(a.getRange()[1] >= b.getRange()[1]);
                    }
                }
            }
        }
        return null;
    }

    static public Variable Equals(Variable a , Variable b)
    {
        switch(a.type)
        {
            case variableType.Int:
            {
                switch(b.type)
                {
                    case variableType.Int:
                    {
                        return new Variable(a.getInt() == b.getInt());
                    }
                    case variableType.Range:
                    {
                        return new Variable(b.getRange()[0] <= a.getInt() && a.getInt() <= b.getRange()[1]);
                    }
                }
            }
            case variableType.Range:
            {
                switch(b.type)
                {
                    case variableType.Int:
                    {
                        return new Variable(b.getRange()[0] <= a.getInt() && a.getInt() <= b.getRange()[1]);
                    }
                    case variableType.Range:
                    {
                        return new Variable(
                                a.getRange()[0] <= b.getRange()[0] && b.getRange()[0] <= a.getRange()[1] ||
                                        a.getRange()[0] <= b.getRange()[1] && b.getRange()[1] <= a.getRange()[1]
                        );
                    }
                }
            }
        }
        return null;
    }

    static public Variable NotEquals(Variable a , Variable b)
    {
        switch(a.type)
        {
            case variableType.Int:
            {
                switch(b.type)
                {
                    case variableType.Int:
                    {
                        return new Variable(!(a.getInt() == b.getInt()));
                    }
                    case variableType.Range:
                    {
                        return new Variable(!(b.getRange()[0] <= a.getInt() && a.getInt() <= b.getRange()[1]));
                    }
                }
            }
            case variableType.Range:
            {
                switch(b.type)
                {
                    case variableType.Int:
                    {
                        return new Variable(!(b.getRange()[0] <= a.getInt() && a.getInt() <= b.getRange()[1]));
                    }
                    case variableType.Range:
                    {
                        return new Variable(!(
                                a.getRange()[0] <= b.getRange()[0] && b.getRange()[0] <= a.getRange()[1] ||
                                        a.getRange()[0] <= b.getRange()[1] && b.getRange()[1] <= a.getRange()[1]
                        ));
                    }
                }
            }
        }
        return null;
    }
}


public class Output {
	public static void main (String[] args) { 
		Scanner sc = new Scanner(System.in);
		Variable a = new Variable(0);
		a = new Variable( sc.nextInt());
		while(Operation.LE(a , new Variable(100)).getBool())
		{
		if(Operation.Equals(a , new Variable(95)).getBool())
		{
		a = Operation.Add(a , new Variable(1));
		continue;
		}
		a.printVariable();
		a = Operation.Add(a , new Variable(1));
		}
	}
}
