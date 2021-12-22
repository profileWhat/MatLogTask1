import java.util.HashMap;

public class AST_Printer {
    private final HashMap<AST.BinopType, String> binopMap = new HashMap<>();
    private final HashMap<AST.UnopType, String> unopMap = new HashMap<>();
    public AST_Printer() {
        this.binopMap.put(AST.BinopType.BIN_AND, "&");
        this.binopMap.put(AST.BinopType.BIN_OR, "|");
        this.binopMap.put(AST.BinopType.BIN_IMP, "->");
        this.unopMap.put(AST.UnopType.UN_NEG, "!");
    }
    public void print(AST ast) {
        if (ast != null) {
            if (ast.getType() == AST_type.AST_BINOP) printBinop(ast);
            if (ast.getType() == AST_type.AST_LIT) printLit(ast);
            if (ast.getType() == AST_type.AST_UNOP) printUnop(ast);
        } else System.out.print("NULL");
    }

    public void printBinop(AST ast) {
        System.out.print("(");
        System.out.print(binopMap.get(ast.getBinop().getType()));
        System.out.print(",");
        print(ast.getBinop().getLeft());
        System.out.print(",");
        print(ast.getBinop().getRight());
        System.out.print(")");
    }

    public void printUnop(AST ast) {
        System.out.print("(" + unopMap.get(ast.getUnop().getType()));
        print(ast.getUnop().getOperand());
        System.out.print(")");
    }

    public void printLit(AST ast) {
        System.out.print(ast.getLit());
    }
}
