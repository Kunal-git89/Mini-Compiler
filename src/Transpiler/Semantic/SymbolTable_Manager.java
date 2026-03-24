package Transpiler.Semantic;

import Transpiler.Semantic.Symbol;
import java.util.*;

public class SymbolTable_Manager
{
    private Stack<HashMap<String , Symbol>> stack = new Stack<>();

    public void addScope()
    {
        stack.push(new HashMap<>());
    }

    public void existScope()
    {
        stack.pop();
    }

    public boolean declare(Symbol s)
    {
        HashMap<String , Symbol> map = stack.peek();
        if(map.containsKey(s.name)) return false;
        map.put(s.name , s);
        return true;
    }

    public Symbol lookup(String name)
    {
        for(int i = stack.size()- 1 ; i >= 0 ; i--)
        {
            if(stack.get(i).containsKey(name)) return stack.get(i).get(name);
        }
        return null;
    }
}
