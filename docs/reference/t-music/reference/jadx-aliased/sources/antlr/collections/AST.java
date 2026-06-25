package antlr.collections;

import antlr.Token;

/* loaded from: classes3.dex */
public interface AST {
    void addChild(AST ast);

    boolean equals(AST ast);

    boolean equalsList(AST ast);

    boolean equalsListPartial(AST ast);

    boolean equalsTree(AST ast);

    boolean equalsTreePartial(AST ast);

    ASTEnumeration findAll(AST ast);

    ASTEnumeration findAllPartial(AST ast);

    int getColumn();

    AST getFirstChild();

    int getLine();

    AST getNextSibling();

    int getNumberOfChildren();

    String getText();

    int getType();

    void initialize(int i, String str);

    void initialize(Token token);

    void initialize(AST ast);

    void setFirstChild(AST ast);

    void setNextSibling(AST ast);

    void setText(String str);

    void setType(int i);

    String toString();

    String toStringList();

    String toStringTree();
}
