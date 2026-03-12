import java.nio.file.*;

public class Main
{
    public static void main (String[] args) throws Exception
    {
        String s = new String();
        s = Files.readString(Path.of("Test.txt"));    // Hardcode the file

        Lexer lexer = new Lexer(s);
        Token temp = new Token();

        while(temp.type != Token.Token_type.EOF)
        {
            temp.printToken();
            temp = lexer.nextToken();
        }
        System.out.println("Program Finished");
    }
}