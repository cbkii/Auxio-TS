package antlr;

import antlr.collections.AST;
import java.io.PrintStream;
import p000a.p001a.p002a.p003a.C0000a;

/* JADX INFO: loaded from: classes3.dex */
public class DumpASTVisitor implements ASTVisitor {
    public int level = 0;

    private void tabs() {
        for (int i = 0; i < this.level; i++) {
            System.out.print("   ");
        }
    }

    @Override // antlr.ASTVisitor
    public void visit(AST ast) {
        PrintStream printStream;
        String text;
        for (AST nextSibling = ast; nextSibling != null && nextSibling.getFirstChild() == null; nextSibling = nextSibling.getNextSibling()) {
        }
        while (ast != null) {
            tabs();
            if (ast.getText() == null) {
                printStream = System.out;
                text = "nil";
            } else {
                printStream = System.out;
                text = ast.getText();
            }
            printStream.print(text);
            PrintStream printStream2 = System.out;
            StringBuilder sbM5a = C0000a.m5a(" [");
            sbM5a.append(ast.getType());
            sbM5a.append("] ");
            printStream2.print(sbM5a.toString());
            System.out.println("");
            if (ast.getFirstChild() != null) {
                this.level++;
                visit(ast.getFirstChild());
                this.level--;
            }
            ast = ast.getNextSibling();
        }
    }
}
