package Transpiler.CodeGeneration;

import java.lang.reflect.Method;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import Transpiler.AST.*;
import Transpiler.Semantic.*;

import javax.tools.*;
import java.io.*;

public class CodeGenerator
{
    private List<ASTNode> program ;
    private String filename;
    private List<String> code = new ArrayList<>();
    private FileWriter writer;
    private String[] classes;
    private SymbolTable_Manager symbolTable;
    public File file;
    public int indent = 0;

    public CodeGenerator(List<ASTNode> l , String s , File f , String[] c) throws IOException
    {
        program = l;
        filename = s;
        file = f;
        classes = c;
        symbolTable = new SymbolTable_Manager();
    }

    public void start() throws IOException
    {
        writer = new FileWriter(file);
        emit("import java.util.*;");
        emit("");
        writeclasses();
        emit("public class " + filename + " {");
        indent++;
        emit("public static void main (String[] args) { ");
        indent++;
        symbolTable.addScope();

        //My code starts here
        for(ASTNode node : program)
        {
            switch (node.type)
            {
                case nodeType.LetNode :
                    emitLet((LetNode)node);
                    break;

                case nodeType.AssignmentNode:

            }
        }
        //My code ends here

        symbolTable.existScope();
        indent--;
        emit("}");
        indent--;
        emit("}");
        outputCode();
        writer.close();
        compileAndRun();
    }

    private void emitLet(LetNode node)
    {
            String line = "Variable " + node.name + " = ";
            if(getOpType(node.expression) == symbolType.Range)
            {
                line = line.concat("Operation.gRange(" + emitArithematic(node.expression.leftNode) + " , " + emitArithematic(node.expression.rightNode) + ");");
                symbolTable.declare(new Symbol(node.name , symbolType.Range));
            }
            else
            {
                line = line.concat("new Variable(" + emitArithematic(node.expression) + ");");
                symbolTable.declare(new Symbol(node.name , symbolType.Int));
            }
            emit(line);
    }

    private void emitAssignment (AssignmentNode node)
    {
        Symbol s = symbolTable.lookup(node.name);
        String line = node.name;
        switch(getOpType(node.expression))
        {
            case symbolType.Int:
                line = line.concat("assignInt(");
                break;

            case symbolType.Range:
                line = line.concat("assignRange(");
                break;

            case symbolType.Bool:
                line = line.concat("assignBool(");
                break;
        }
    }

    private String emitArithematic(ExpressionNode node)
    {
        if(node.op == opType.Identifier)
        {
            Symbol s = symbolTable.lookup(((IdentifierNode)node).name);
            if(s.type == symbolType.Int) return ((IdentifierNode)node).name + ".getInt()";
            else return ((IdentifierNode)node).name + ".getRange()";
        }
        else if (node.op == opType.Constant)
        {
            return String.valueOf(((ConstantNode)node).value);
        }
        String str = emitArithematic(node.leftNode);
        switch(node.op)
        {
            case opType.Add:
                str = str.concat(" + ");
                break;

            case opType.Minus:
                str = str.concat(" - ");
                break;

            case opType.Multiply:
                str = str.concat(" * ");
                break;

            case opType.Divide:
                str = str.concat(" / ");
                break;

            case opType.Mod:
                str = str.concat(" % ");
                break;
        }
        str = str.concat(emitArithematic(node.rightNode));
        return str;
    }

    private void emitComaprator(ExpressionNode node)
    {
        if(node.op == opType.Identifier)
        {
            emit(((IdentifierNode)node).name);
            return;
        }
        else if (node.op == opType.Constant)
        {
            emit(String.valueOf(((ConstantNode)node).value));
            return;
        }

        emitRange(node.leftNode);
        switch(node.op)
        {
            case opType.Less:
                emit(" < ");
                break;

            case opType.LE:
                emit(" <= ");
                break;

            case opType.Greater:
                emit(" > ");
                break;

            case opType.GE:
                emit(" >= ");
                break;

            case opType.Equals:
                emit(" == ");
                break;

            case opType.NotEquals:
                emit(" != ");
                break;
        }
        emitRange(node.rightNode);
    }

    private String emitRange(ExpressionNode node)
    {
        return null;
    }

    private symbolType getOpType(ExpressionNode node)
    {
        if(node.op == opType.Equals || node.op == opType.NotEquals || node.op == opType.Less || node.op == opType.LE || node.op == opType.Greater || node.op == opType.GE)
            return symbolType.Bool;
        else if (node.op == opType.Range) return symbolType.Range;
        else return symbolType.Int;
    }

    private void writeclasses() throws IOException
    {
        for(String str : classes)
        {
            String s = Files.readString(Path.of(str));
            s = s.replace("package Transpiler.CodeGeneration;" , "");
            emit(s);
            emit("\n");
        }
    }


    private void outputCode()
    {
        try
        {
            for(String line : code)
            {
                writer.write(line);
                writer.write("\n");
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    private void compileAndRun()
    {
        try {
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            int c = compiler.run(null, null, null, "-d", "output", file.getName());
            if(c != 0) throw new RuntimeException("Compilation of generated code failed , install JVM 21(recommended)");
            File dir = new File("output");
            URLClassLoader classLoader = new URLClassLoader(new URL[]{dir.toURI().toURL()});
            Class<?> cls = Class.forName(filename, true, classLoader);

            Method main = cls.getMethod("main", String[].class);

            main.invoke(null, (Object) new String[]{});
        }
        catch(Exception e)
        {
            e.getCause().printStackTrace();
        }
    }

    private void emit(String line)
    {
        String s = "\t".repeat(indent);
        code.add(s + line);
    }
}
