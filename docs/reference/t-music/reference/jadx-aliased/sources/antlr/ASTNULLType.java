package antlr;

import antlr.collections.AST;
import antlr.collections.ASTEnumeration;

/* loaded from: classes3.dex */
public class ASTNULLType implements AST {
    @Override // antlr.collections.AST
    public void addChild(AST ast) {
    }

    @Override // antlr.collections.AST
    public boolean equals(AST ast) {
        return false;
    }

    @Override // antlr.collections.AST
    public boolean equalsList(AST ast) {
        return false;
    }

    @Override // antlr.collections.AST
    public boolean equalsListPartial(AST ast) {
        return false;
    }

    @Override // antlr.collections.AST
    public boolean equalsTree(AST ast) {
        return false;
    }

    @Override // antlr.collections.AST
    public boolean equalsTreePartial(AST ast) {
        return false;
    }

    @Override // antlr.collections.AST
    public ASTEnumeration findAll(AST ast) {
        return null;
    }

    @Override // antlr.collections.AST
    public ASTEnumeration findAllPartial(AST ast) {
        return null;
    }

    @Override // antlr.collections.AST
    public int getColumn() {
        return 0;
    }

    @Override // antlr.collections.AST
    public AST getFirstChild() {
        return this;
    }

    @Override // antlr.collections.AST
    public int getLine() {
        return 0;
    }

    @Override // antlr.collections.AST
    public AST getNextSibling() {
        return this;
    }

    @Override // antlr.collections.AST
    public int getNumberOfChildren() {
        return 0;
    }

    @Override // antlr.collections.AST
    public String getText() {
        return "<ASTNULL>";
    }

    @Override // antlr.collections.AST
    public int getType() {
        return 3;
    }

    @Override // antlr.collections.AST
    public void initialize(int i, String str) {
    }

    @Override // antlr.collections.AST
    public void initialize(Token token) {
    }

    @Override // antlr.collections.AST
    public void initialize(AST ast) {
    }

    @Override // antlr.collections.AST
    public void setFirstChild(AST ast) {
    }

    @Override // antlr.collections.AST
    public void setNextSibling(AST ast) {
    }

    @Override // antlr.collections.AST
    public void setText(String str) {
    }

    @Override // antlr.collections.AST
    public void setType(int i) {
    }

    @Override // antlr.collections.AST
    public String toString() {
        return getText();
    }

    @Override // antlr.collections.AST
    public String toStringList() {
        return getText();
    }

    @Override // antlr.collections.AST
    public String toStringTree() {
        return getText();
    }
}
