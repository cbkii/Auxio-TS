package antlr;

import antlr.collections.AST;

/* loaded from: classes3.dex */
public class ASTPair {
    public AST child;
    public AST root;

    public final void advanceChildToEnd() {
        if (this.child != null) {
            while (this.child.getNextSibling() != null) {
                this.child = this.child.getNextSibling();
            }
        }
    }

    public ASTPair copy() {
        ASTPair aSTPair = new ASTPair();
        aSTPair.root = this.root;
        aSTPair.child = this.child;
        return aSTPair;
    }

    public String toString() {
        AST ast = this.root;
        String text = ast == null ? "null" : ast.getText();
        AST ast2 = this.child;
        return "[" + text + "," + (ast2 != null ? ast2.getText() : "null") + "]";
    }
}
