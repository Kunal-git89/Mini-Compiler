package Transpiler.CodeGeneration;

class Variable
{
    private int[] limit;
    public variableType type;
    public Variable(int a , int b)
    {
        type = variableType.Range;
        limit = new int[2];
        limit[0] = a;
        limit[1] = b;
    }
    public Variable(int a)
    {
        type = variableType.Int;
        limit = new int[1];
        limit[0] = a;
    }

    public boolean isInt(){ return type == variableType.Int;}
    public boolean isRange(){ return type == variableType.Range;}

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
        limit[0] = l[1];
        limit[1] = l[1];
    }

    public int[] getRange()
    {
        return limit;
    }
}
enum variableType {Int , Range}