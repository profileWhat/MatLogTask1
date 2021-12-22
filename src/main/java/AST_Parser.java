import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AST_Parser {
    private final HashMap<LexemeType, AST.BinopType> binopMap = new HashMap<>();
    private final HashMap<LexemeType, AST.UnopType> unopMap = new HashMap<>();
    public AST_Parser() {
        this.binopMap.put(LexemeType.OP_AND, AST.BinopType.BIN_AND);
        this.binopMap.put(LexemeType.OP_OR, AST.BinopType.BIN_OR);
        this.binopMap.put(LexemeType.OP_IMP, AST.BinopType.BIN_IMP);
        this.unopMap.put(LexemeType.OP_NEG, AST.UnopType.UN_NEG);
    }

    public enum LexemeType {
        LEFT_BRACKET, RIGHT_BRACKET,
        OP_AND, OP_OR, OP_NEG, OP_IMP, EOF, LIT
    }
    public static class Lexeme {
        LexemeType type;
        String value;

        public Lexeme(LexemeType type, String value) {
            this.type = type;
            this.value = value;
        }
        public Lexeme(LexemeType type, Character character) {
            this.type = type;
            this.value = character.toString();
        }

        @Override
        public String toString() {
            return "Lexeme {" +
                    "type=" + type +
                    ", value='" + value + '\'' +
                    "}";
        }
    }

    public static class LexemeBuffer {
        private int pos = 0;

        public List<Lexeme> lexemes;

        public LexemeBuffer(List<Lexeme> lexemes) {
            this.lexemes = lexemes;
        }

        public Lexeme next() {
            return lexemes.get(pos++);
        }

        public void back() {
            pos--;
        }

        public int getPos() {
            return pos;
        }


    }

    public List<Lexeme> lexAnalyze(String string) {
        ArrayList<Lexeme> lexemes = new ArrayList<>();
        int pos = 0;
        while (pos < string.length()) {
            char c = string.charAt(pos);
            switch (c) {
                case '(':
                    lexemes.add(new Lexeme(LexemeType.LEFT_BRACKET , c));
                    pos++;
                    continue;
                case ')':
                    lexemes.add(new Lexeme(LexemeType.RIGHT_BRACKET , c));
                    pos++;
                    continue;
                case '|':
                    lexemes.add(new Lexeme(LexemeType.OP_OR , c));
                    pos++;
                    continue;
                case '&':
                    lexemes.add(new Lexeme(LexemeType.OP_AND , c));
                    pos++;
                    continue;
                case '!':
                    lexemes.add(new Lexeme(LexemeType.OP_NEG , c));
                    pos++;
                    continue;
                case '-':
                    lexemes.add(new Lexeme(LexemeType.OP_IMP , c));
                    pos = pos + 2;
                    continue;
                default:
                    StringBuilder builder = new StringBuilder();
                    do {
                        if ((c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || (c == (char)39)) builder.append(c);
                        pos++;
                        if (pos >= string.length()) break;
                        c = string.charAt(pos);
                    } while (c != '(' && c != ')' && c != '|' && c != '&' && c != '!' && c != '-');
                    lexemes.add(new Lexeme(LexemeType.LIT , builder.toString()));
            }
        }
        lexemes.add(new Lexeme(LexemeType.EOF , ""));
        return lexemes;
    }

    public AST expression(LexemeBuffer lexemes) {
        Lexeme lexeme = lexemes.next();
        if (lexeme.type == LexemeType.EOF) {
            return null;
        } else {
            lexemes.back();
            AST ast = disjunction(lexemes, null, true);
            lexeme = lexemes.next();
            if (lexeme.type == LexemeType.OP_IMP) {
                return AST.binop(binopMap.get(lexeme.type), ast, expression(lexemes));
            } else {
                lexemes.back();
                return ast;
            }
        }
    }


   public AST lit(Lexeme lexeme) {
        return AST.lit(lexeme.value);
   }

   public AST disjunction(LexemeBuffer lexemes, AST ast, boolean first) {
        AST astLeft;
        if (first)  astLeft = conjunction(lexemes, null, true);
        else astLeft = ast;
        Lexeme lexeme = lexemes.next();
        if (lexeme.type == LexemeType.OP_OR) {
            AST astRight = conjunction(lexemes, null, true);
            astLeft =  AST.binop(binopMap.get(lexeme.type),astLeft, astRight);
            return disjunction(lexemes, astLeft, false);
        } else {
            lexemes.back();
            if (first) return astLeft;
            else return ast;
        }
   }

   public AST conjunction(LexemeBuffer lexemes, AST ast, boolean first) {
        AST astLeft;
        if (first) astLeft = denial(lexemes);
        else astLeft = ast;
        Lexeme lexeme = lexemes.next();
        if (lexeme.type == LexemeType.OP_AND) {
            AST astRight = denial(lexemes);
            astLeft  = AST.binop(binopMap.get(lexeme.type), astLeft, astRight);
            return conjunction(lexemes, astLeft, false);
        } else {
            lexemes.back();
            if (first) return astLeft;
            else return ast;
        }
   }

   public AST denial(LexemeBuffer lexemes) {
        Lexeme lexeme = lexemes.next();
        if (lexeme.type == LexemeType.OP_NEG) {
            AST ast = denial(lexemes);
            return AST.unop(unopMap.get(lexeme.type), ast);
        }
        if (lexeme.type == LexemeType.LIT) {
            return lit(lexeme);
        }
        if (lexeme.type == LexemeType.LEFT_BRACKET) {
            AST ast = expression(lexemes);
            lexeme = lexemes.next();
            if (lexeme.type != LexemeType.RIGHT_BRACKET) {
                throw new RuntimeException("Unexpected token: " + lexeme.value + " at position: " + lexemes.getPos());
            }
            return ast;
        } else throw new RuntimeException("Unexpected token: " + lexeme.value + " at position: " + lexemes.getPos());
   }
}
