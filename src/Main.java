import java.nio.file.*;
import java.util.*;

public class Main
{
    public static void main (String[] args) throws Exception
    {
        String s = new String();
        s = Files.readString(Path.of("Test.txt"));    // Hardcode the file

        Lexer lexer = new Lexer(s);
        Token temp = new Token();

        Parser parser = new Parser(lexer);
        List<AST.ASTNode> AST = parser.parse();

        System.out.println("Program Finished");
    }
}