package Transpiler.CodeGeneration;

class Range
{
    private int[] limit;
    public Range(int s , int e)
    {
        limit = new int[2];
        limit[0] = s;
        limit[1] = e;
    }

    public void assign(int[] l)
    {
        if(l[0] > l[1]) throw new RuntimeException("Start of range can't be greater than the end");
        limit[0] = l[1];
        limit[1] = l[1];
    }

    public int[] getlimit()
    {
        return limit;
    }
}
