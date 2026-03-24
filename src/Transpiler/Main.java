package Transpiler;

import Transpiler.AST.*;
import Transpiler.Semantic.Semantic_Analyzer;

import java.nio.file.*;
import java.util.*;

public class Main
{
    public static void main (String[] args) throws Exception
    {
        String s = Files.readString(Path.of("Test.txt")); // Hardcode the file

        Lexer lexer = new Lexer(s);

        Parser parser = new Parser(lexer);
        List<ASTNode> program = parser.parse();

        Semantic_Analyzer semantic = new Semantic_Analyzer(program);
        semantic.start();

        for(ASTNode node : program)
        {
            //node.printNode();
            System.out.println(node.getClass().getSimpleName());
        }
    }
}