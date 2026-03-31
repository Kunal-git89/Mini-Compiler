package Transpiler.CodeGeneration;

class Variable
{
    private int[] limit;
    public variableType type;
    public Boolean b;
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


}
enum variableType {Int , Range , Bool}