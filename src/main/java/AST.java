
public class AST {
    public enum BinopType {
        BIN_AND,
        BIN_OR,
        BIN_IMP,
    }

    public static class AST_Binop {
        private final BinopType type;
        private final AST right;
        private final AST left;

        public AST_Binop(BinopType type, AST left, AST right) {
            this.right = right;
            this.left = left;
            this.type = type;
        }

        public AST getLeft() {
            return left;
        }

        public AST getRight() {
            return right;
        }

        public BinopType getType() {
            return type;
        }
    }

    public enum UnopType {
        UN_NEG,
    }
    public static class AST_Unop {
        private final UnopType type;
        private final AST operand;

        public AST_Unop(UnopType type, AST operand) {
            this.operand = operand;
            this.type = type;
        }

        public AST getOperand() {
            return operand;
        }

        public UnopType getType() {
            return type;
        }
    }

    private final AST_type type;
    private AST_Binop binop;
    private AST_Unop unop;
    private String lit;

    private AST(String lit) {
        this.lit = lit;
        this.type = AST_type.AST_LIT;
    }

    private AST(UnopType type, AST operand) {
        this.unop = new AST_Unop(type, operand);
        this.type = AST_type.AST_UNOP;
    }

    private AST(BinopType type, AST left, AST right) {
        this.binop = new AST_Binop(type, left, right);
        this.type = AST_type.AST_BINOP;
    }
    public static AST lit(String lit) {
        return new AST(lit);
    }

    public static AST unop(UnopType type, AST operand) {
        return new AST(type, operand);
    }

    public static AST binop(BinopType type, AST rigth, AST left) {
        return new AST(type, rigth, left);
    }

    public AST_Binop getBinop() {
        return type == AST_type.AST_BINOP ? binop : null;
    }

    public AST_Unop getUnop() {
        return type == AST_type.AST_UNOP ? unop : null;
    }

    public String getLit() {
        return type == AST_type.AST_LIT ? lit: null;
    }

    public AST_type getType() {
        return type;
    }


}
