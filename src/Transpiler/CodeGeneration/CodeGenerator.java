package Transpiler.CodeGeneration;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import Transpiler.AST.*;

import javax.tools.*;
import java.io.*;

public class CodeGenerator
{
    private List<ASTNode> program ;
    private String filename;
    private List<String> code = new ArrayList<>();
    private FileWriter writer;
    private String[] classes;
    public File file;
    public int indent = 0;

    public CodeGenerator(List<ASTNode> l , String s , File f , String[] c) throws IOException
    {
        program = l;
        filename = s;
        file = f;
        classes = c;
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


        //My code starts here
        emit("Range r = new Range(1 , 10);");
        emit("System.out.println(\"Hello World\");");
        //My code ends here


        indent--;
        emit("}");
        indent--;
        emit("}");
        outputCode();
        writer.close();
        compileAndRun();
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
            if(c != 0) throw new RuntimeException("Compilation of generated code failed");
            File dir = new File("output");
            URLClassLoader classLoader = new URLClassLoader(new URL[]{dir.toURI().toURL()});
            Class<?> cls = Class.forName(filename, true, classLoader);

            Method main = cls.getMethod("main", String[].class);

            main.invoke(null, (Object) new String[]{});
        }
        catch(Exception e)
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
