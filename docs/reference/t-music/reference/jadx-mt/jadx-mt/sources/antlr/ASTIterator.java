package antlr;

import antlr.collections.AST;

/* JADX INFO: loaded from: classes3.dex */
public class ASTIterator {
    public AST cursor;
    public AST original;

    public ASTIterator(AST ast) {
        this.cursor = null;
        this.original = null;
        this.cursor = ast;
        this.original = ast;
    }

    public boolean isSubtree(AST ast, AST ast2) {
        if (ast2 == null) {
            return true;
        }
        if (ast == null) {
            return false;
        }
        while (ast != null && ast2 != null) {
            if (ast.getType() != ast2.getType()) {
                return false;
            }
            if (ast.getFirstChild() != null && !isSubtree(ast.getFirstChild(), ast2.getFirstChild())) {
                return false;
            }
            ast = ast.getNextSibling();
            ast2 = ast2.getNextSibling();
        }
        return true;
    }

    public AST next(AST ast) {
        if (this.cursor == null) {
            return null;
        }
        while (true) {
            AST ast2 = this.cursor;
            if (ast2 == null) {
                return null;
            }
            if (ast2.getType() == ast.getType() && this.cursor.getFirstChild() != null && isSubtree(this.cursor.getFirstChild(), ast.getFirstChild())) {
                return this.cursor;
            }
            this.cursor = this.cursor.getNextSibling();
        }
    }
}
