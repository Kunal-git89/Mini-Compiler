package Transpiler.Semantic;

public class SymbolTable_Entry
{
    public enum symbolType
    {
        Int , Bool , Range , Constant
    }
    public class symbol{
        public String name;
        public symbolType type;

        public symbol(String s , symbolType t)
        {
            name = s;
            type = t;
        }
    }
}