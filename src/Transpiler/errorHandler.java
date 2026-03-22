package Transpiler;

import java.util.*;

public class errorHandler
{
    static private List<String> errors = new ArrayList<>();

    public void add(String error , int line)
    {
        errors.add("Line " + line + ": " + error);
    }

    public boolean hasError() {return !errors.isEmpty();}

    public void report()
    {
        for(String error : errors)
        {
            System.out.println(error);
        }
        throw new RuntimeException("Compilation Failed");
    }
}
