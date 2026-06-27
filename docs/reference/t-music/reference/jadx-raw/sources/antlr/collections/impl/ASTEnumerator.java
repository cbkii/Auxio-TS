package antlr.collections.impl;

import antlr.collections.AST;
import antlr.collections.ASTEnumeration;
import java.util.NoSuchElementException;

/* loaded from: classes3.dex */
public class ASTEnumerator implements ASTEnumeration {

    /* renamed from: i */
    public int f318i = 0;
    public VectorEnumerator nodes;

    public ASTEnumerator(Vector vector) {
        this.nodes = new VectorEnumerator(vector);
    }

    @Override // antlr.collections.ASTEnumeration
    public boolean hasMoreNodes() {
        boolean z;
        synchronized (this.nodes) {
            z = this.f318i <= this.nodes.vector.lastElement;
        }
        return z;
    }

    @Override // antlr.collections.ASTEnumeration
    public AST nextNode() {
        AST ast;
        synchronized (this.nodes) {
            if (this.f318i > this.nodes.vector.lastElement) {
                throw new NoSuchElementException("ASTEnumerator");
            }
            Object[] objArr = this.nodes.vector.data;
            int i = this.f318i;
            this.f318i = i + 1;
            ast = (AST) objArr[i];
        }
        return ast;
    }
}
