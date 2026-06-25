package antlr.collections.impl;

import antlr.collections.AST;

/* loaded from: classes3.dex */
public class ASTArray {
    public AST[] array;
    public int size = 0;

    public ASTArray(int i) {
        this.array = new AST[i];
    }

    public ASTArray add(AST ast) {
        AST[] astArr = this.array;
        int i = this.size;
        this.size = i + 1;
        astArr[i] = ast;
        return this;
    }
}
