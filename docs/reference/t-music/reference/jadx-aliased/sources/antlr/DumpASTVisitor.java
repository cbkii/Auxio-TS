package antlr;

import antlr.collections.AST;
import java.io.PrintStream;
import p054a.p055a.p056a.p003a.C0000a;

/* loaded from: classes3.dex */
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
        for (AST ast2 = ast; ast2 != null && ast2.getFirstChild() == null; ast2 = ast2.getNextSibling()) {
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
            StringBuilder m5a = C0000a.m5a(" [");
            m5a.append(ast.getType());
            m5a.append("] ");
            printStream2.print(m5a.toString());
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
