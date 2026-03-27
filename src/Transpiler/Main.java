package Transpiler;

import Transpiler.AST.*;
import Transpiler.CodeGeneration.CodeGenerator;
import Transpiler.Semantic.Semantic_Analyzer;

import java.io.File;
import java.nio.file.*;
import java.util.*;

public class Main
{
    public static void main (String[] args) throws Exception
    {
        String s = Files.readString(Path.of("Test.txt")); // Hardcode the file
        String filename = "Output";
        File file = new File(filename + ".java");
        String[] includedClasses = {"src/Transpiler/CodeGeneration/Variable.java"};
        Lexer lexer = new Lexer(s);

        Parser parser = new Parser(lexer);
        List<ASTNode> program = parser.parse();

        Semantic_Analyzer semantic = new Semantic_Analyzer(program);
        semantic.start();

        CodeGenerator CG = new CodeGenerator(program , filename , file , includedClasses);
        CG.start();
    }
}