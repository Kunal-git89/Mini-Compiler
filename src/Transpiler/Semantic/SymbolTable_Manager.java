package Transpiler.Semantic;

import Transpiler.Semantic.SymbolTable_Entry.*;
import java.util.*;

public class SymbolTable_Manager
{
    private Stack<HashMap<String , symbol>> stack = new Stack<>();

    public void addScope()
    {
        stack.push(new HashMap<>());
    }

    public void existScope()
    {
        stack.pop();
    }

    public void declare(symbol s)
    {
        HashMap<String , symbol> map = stack.peek();
        if(map.containsKey(s.name)) throw new RuntimeException("Duplicate variable declaration ");
        map.put(s.name , s);
    }

    public symbol lookup(String name)
    {
        for(int i = stack.size()- 1 ; i >= 0 ; i--)
        {
            if(stack.get(i).containsKey(name)) return stack.get(i).get(name);
        }
        return null;
    }
}
