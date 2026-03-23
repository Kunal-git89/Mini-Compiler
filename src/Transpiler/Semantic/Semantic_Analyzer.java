package Transpiler.Semantic;

import Transpiler.AST.*;

import java.util.*;

public class Semantic_Analyzer
{
    private SymbolTable_Manager symboltable = new SymbolTable_Manager();
    private List<ASTNode> AST = new ArrayList<>();

    public Semantic_Analyzer(List<ASTNode> l)
    {
        AST = l;
    }

    public void start()
    {
        symboltable.addScope(); //Global scope start
        for(ASTNode node : AST)
        {

        }
        symboltable.existScope(); //Global scope end
    }

    private void analyze() // Actual analysis
    {

    }
}
