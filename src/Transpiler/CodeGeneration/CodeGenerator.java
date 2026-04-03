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
        emit("Scanner sc = new Scanner(System.in);");

        //My code starts here
        for(ASTNode node : program)
        {
            switch (node.type)
            {
                case nodeType.LetNode :
                    emitLet((LetNode)node);
                    break;

                case nodeType.AssignmentNode:
                    emitAssignment((AssignmentNode) node);
                    break;

                case nodeType.SwapNode:
                    emitSwap((SwapNode) node);
                    break;

                case nodeType.InputNode:
                    emitInput((InputNode) node);
                    break;

                case nodeType.PrintNode:
                    emitPrint((PrintNode) node);
                    break;
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
                line = line.concat("Operation.Range(" + emitArithematic(node.expression.leftNode) + " , " + emitArithematic(node.expression.rightNode) + ");");
                symbolTable.declare(new Symbol(node.name , symbolType.Range));
            }
            else if (getOpType(node.expression) == symbolType.Bool)
            {
                line = line.concat(emitComaprator(node.expression) + ";");
                symbolTable.declare(new Symbol(node.name , symbolType.Bool));
            }
            else
            {
                line = line.concat(emitArithematic(node.expression) + ";");
                symbolTable.declare(new Symbol(node.name , symbolType.Int));
            }
            emit(line);
    }

    private void emitAssignment (AssignmentNode node)
    {
        Symbol s = symbolTable.lookup(node.name);
        String line = node.name + " = ";
        switch(getOpType(node.expression))
        {
            case symbolType.Int:
                line = line.concat(emitArithematic(node.expression) + ";");
                break;

            case symbolType.Range:
                line = line.concat("Operation.Range(" + emitArithematic(node.expression.leftNode) + " , " + emitArithematic(node.expression.rightNode) + ");");
                break;

            case symbolType.Bool:
                line = line.concat(emitComaprator(node.expression) + ";");
                break;
        }
        emit(line);
    }

    private void emitSwap(SwapNode node)
    {
        emit("Operation.Swap(" + node.left + " , " + node.right + ");");
    }

    private void emitInput(InputNode node)
    {
        emit(node.name + " = new Variable( sc.nextInt());");
    }

    private void emitPrint(PrintNode node)
    {
        switch(getOpType(node.expression))
        {
            case symbolType.Int:
                emit(emitArithematic(node.expression) + ".printVariable();");
                break;

            case symbolType.Range:
                emit("Operation.Range(" + emitArithematic(node.expression.leftNode) + " , " + emitArithematic(node.expression.rightNode) + ").printVariable();");
                break;

            case symbolType.Bool:
                emit(emitComaprator(node.expression) + ".printVariable();");
                break;
        }
    }

    private String emitArithematic(ExpressionNode node)
    {
        if(node.op == opType.Identifier)
        {
            Symbol s = symbolTable.lookup(((IdentifierNode)node).name);
            if(s.type == symbolType.Int) return ((IdentifierNode)node).name;
            else if (s.type == symbolType.Range) return ((IdentifierNode)node).name;
            else return ((IdentifierNode)node).name;
        }
        else if (node.op == opType.Constant)
        {
            return "new Variable(" + String.valueOf(((ConstantNode)node).value) + ")";
        }
        String left = emitArithematic(node.leftNode);
        String right = emitArithematic(node.rightNode);
        switch(node.op)
        {
            case opType.Add:
                return "Operation.Add("+left + " , " + right + ")";

            case opType.Minus:
                return "Operation.Minus("+left + " , " + right + ")";

            case opType.Multiply:
                return "Operation.Multiply("+left + " , " + right + ")";

            case opType.Divide:
                return "Operation.Divide("+left + " , " + right + ")";

            case opType.Mod:
                return "Operation.Mod("+left + " , " + right + ")";
        }
        return null;
    }

    private String emitComaprator(ExpressionNode node)
    {
        if(node.op == opType.Identifier)
        {
            Symbol s = symbolTable.lookup(((IdentifierNode)node).name);
            if(s.type == symbolType.Int) return ((IdentifierNode)node).name;
            else if (s.type == symbolType.Range) return ((IdentifierNode)node).name;
            else return ((IdentifierNode)node).name;
        }
        else if (node.op == opType.Constant)
        {
            return "new Variable(" + String.valueOf(((ConstantNode)node).value) + ")";
        }

        String left = emitArithematic(node.leftNode);
        String right = emitArithematic(node.rightNode);
        switch(node.op)
        {
            case opType.Less:
                return "Operation.Less(" + left + " , " + right + ")";

            case opType.LE:
                return "Operation.LE(" + left + " , " + right + ")";

            case opType.Greater:
                return "Operation.Greater(" + left + " , " + right + ")";

            case opType.GE:
                return "Operation.GE(" + left + " , " + right + ")";

            case opType.Equals:
                return "Operation.Equals(" + left + " , " + right + ")";

            case opType.NotEquals:
                return "Operation.NotEquals(" + left + " , " + right + ")";
        }
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
