package Transpiler.CodeGeneration;

import java.util.*;
import Transpiler.AST.*;
import java.io.*;

public class CodeGenerator
{
    private List<ASTNode> program ;
    private String filename;
    private List<String> code = new ArrayList<>();
    public int indent = 0;
    public CodeGenerator(List<ASTNode> l , String s)
    {
        program = l;
        filename = s;
    }

    public void start()
    {
        emit("import java.util.*;");
        emit("");
        emit("public class " + filename + " {");
        indent++;
        emit("public static void main (String[] args) { ");

        // my code
        //emit("System.out.println(\"Hello Wrold\");");
        emit("}");
        indent--;
        emit("}");

        outputCode();
        compileAndRun();
    }

    private void outputCode()
    {
        try
        {
            FileWriter writer = new FileWriter(filename + ".java");
            for(String line : code)
            {
                writer.write(line);
                writer.write("\n");
            }
            writer.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    private void compileAndRun()
    {
        try
        {
            ProcessBuilder pb = new ProcessBuilder( "javac" , filename + ".java");
            pb.inheritIO();
            Process compile = pb.start(); //Compile the code
            compile.waitFor();

            pb = new ProcessBuilder("java" , filename);
            pb.inheritIO();
            Process run = pb.start();
            run.waitFor();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void emit(String line)
    {
        String s = "\t".repeat(indent);
        code.add(s + line);
    }
}
