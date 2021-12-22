import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String string = scanner.nextLine();
        AST_Parser parser = new AST_Parser();
        List<AST_Parser.Lexeme> lexemes = parser.lexAnalyze(string);
        AST_Parser.LexemeBuffer lexemeBuffer = new AST_Parser.LexemeBuffer(lexemes);
        AST result = parser.expression(lexemeBuffer);
        AST_Printer printer = new AST_Printer();
        printer.print(result);
    }
}
