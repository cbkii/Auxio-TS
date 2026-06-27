.class public abstract Lantlr/BaseAST;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Lantlr/collections/AST;
.implements Ljava/io/Serializable;


# static fields
.field public static tokenNames:[Ljava/lang/String; = null

.field public static verboseStringConversion:Z = false


# instance fields
.field public down:Lantlr/BaseAST;

.field public right:Lantlr/BaseAST;


# direct methods
.method public static constructor <clinit>()V
    .locals 0

    return-void
.end method

.method public constructor <init>()V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method

.method public static decode(Ljava/lang/String;)Ljava/lang/String;
    .locals 14

    new-instance v0, Ljava/lang/StringBuffer;

    invoke-direct {v0}, Ljava/lang/StringBuffer;-><init>()V

    const/4 v1, 0x0

    :goto_0
    invoke-virtual {p0}, Ljava/lang/String;->length()I

    move-result v2

    if-ge v1, v2, :cond_6

    invoke-virtual {p0, v1}, Ljava/lang/String;->charAt(I)C

    move-result v2

    const/16 v3, 0x26

    if-ne v2, v3, :cond_5

    add-int/lit8 v2, v1, 0x1

    invoke-virtual {p0, v2}, Ljava/lang/String;->charAt(I)C

    move-result v2

    add-int/lit8 v3, v1, 0x2

    invoke-virtual {p0, v3}, Ljava/lang/String;->charAt(I)C

    move-result v3

    add-int/lit8 v4, v1, 0x3

    invoke-virtual {p0, v4}, Ljava/lang/String;->charAt(I)C

    move-result v4

    add-int/lit8 v5, v1, 0x4

    invoke-virtual {p0, v5}, Ljava/lang/String;->charAt(I)C

    move-result v6

    add-int/lit8 v7, v1, 0x5

    invoke-virtual {p0, v7}, Ljava/lang/String;->charAt(I)C

    move-result v8

    const-string v9, "&"

    const/16 v10, 0x70

    const/16 v11, 0x61

    const/16 v12, 0x3b

    if-ne v2, v11, :cond_0

    const/16 v13, 0x6d

    if-ne v3, v13, :cond_0

    if-ne v4, v10, :cond_0

    if-ne v6, v12, :cond_0

    invoke-virtual {v0, v9}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    move v1, v7

    goto :goto_4

    :cond_0
    const/16 v7, 0x6c

    const/16 v13, 0x74

    if-ne v2, v7, :cond_1

    if-ne v3, v13, :cond_1

    if-ne v4, v12, :cond_1

    const-string v1, "<"

    :goto_1
    invoke-virtual {v0, v1}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    goto :goto_2

    :cond_1
    const/16 v7, 0x67

    if-ne v2, v7, :cond_2

    if-ne v3, v13, :cond_2

    if-ne v4, v12, :cond_2

    const-string v1, ">"

    goto :goto_1

    :goto_2
    move v1, v5

    goto :goto_4

    :cond_2
    const/16 v5, 0x71

    const/16 v7, 0x6f

    if-ne v2, v5, :cond_3

    const/16 v5, 0x75

    if-ne v3, v5, :cond_3

    if-ne v4, v7, :cond_3

    if-ne v6, v13, :cond_3

    if-ne v8, v12, :cond_3

    const-string v2, "\""

    :goto_3
    invoke-virtual {v0, v2}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    add-int/lit8 v1, v1, 0x6

    goto :goto_4

    :cond_3
    if-ne v2, v11, :cond_4

    if-ne v3, v10, :cond_4

    if-ne v4, v7, :cond_4

    const/16 v2, 0x73

    if-ne v6, v2, :cond_4

    if-ne v8, v12, :cond_4

    const-string v2, "\'"

    goto :goto_3

    :cond_4
    invoke-virtual {v0, v9}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    goto :goto_4

    :cond_5
    invoke-virtual {v0, v2}, Ljava/lang/StringBuffer;->append(C)Ljava/lang/StringBuffer;

    :goto_4
    add-int/lit8 v1, v1, 0x1

    goto/16 :goto_0

    :cond_6
    new-instance p0, Ljava/lang/String;

    invoke-direct {p0, v0}, Ljava/lang/String;-><init>(Ljava/lang/StringBuffer;)V

    return-object p0
.end method

.method public static doWorkForFindAll(Lantlr/collections/AST;Lantlr/collections/impl/Vector;Lantlr/collections/AST;Z)V
    .locals 1

    :goto_0
    if-eqz p0, :cond_4

    if-eqz p3, :cond_0

    invoke-interface {p0, p2}, Lantlr/collections/AST;->equalsTreePartial(Lantlr/collections/AST;)Z

    move-result v0

    if-nez v0, :cond_1

    :cond_0
    if-nez p3, :cond_2

    invoke-interface {p0, p2}, Lantlr/collections/AST;->equalsTree(Lantlr/collections/AST;)Z

    move-result v0

    if-eqz v0, :cond_2

    :cond_1
    invoke-virtual {p1, p0}, Lantlr/collections/impl/Vector;->appendElement(Ljava/lang/Object;)V

    :cond_2
    invoke-interface {p0}, Lantlr/collections/AST;->getFirstChild()Lantlr/collections/AST;

    move-result-object v0

    if-eqz v0, :cond_3

    invoke-interface {p0}, Lantlr/collections/AST;->getFirstChild()Lantlr/collections/AST;

    move-result-object v0

    invoke-static {v0, p1, p2, p3}, Lantlr/BaseAST;->doWorkForFindAll(Lantlr/collections/AST;Lantlr/collections/impl/Vector;Lantlr/collections/AST;Z)V

    :cond_3
    invoke-interface {p0}, Lantlr/collections/AST;->getNextSibling()Lantlr/collections/AST;

    move-result-object p0

    goto :goto_0

    :cond_4
    return-void
.end method

.method public static encode(Ljava/lang/String;)Ljava/lang/String;
    .locals 4

    new-instance v0, Ljava/lang/StringBuffer;

    invoke-direct {v0}, Ljava/lang/StringBuffer;-><init>()V

    const/4 v1, 0x0

    :goto_0
    invoke-virtual {p0}, Ljava/lang/String;->length()I

    move-result v2

    if-ge v1, v2, :cond_5

    invoke-virtual {p0, v1}, Ljava/lang/String;->charAt(I)C

    move-result v2

    const/16 v3, 0x22

    if-eq v2, v3, :cond_4

    const/16 v3, 0x3c

    if-eq v2, v3, :cond_3

    const/16 v3, 0x3e

    if-eq v2, v3, :cond_2

    const/16 v3, 0x26

    if-eq v2, v3, :cond_1

    const/16 v3, 0x27

    if-eq v2, v3, :cond_0

    invoke-virtual {v0, v2}, Ljava/lang/StringBuffer;->append(C)Ljava/lang/StringBuffer;

    goto :goto_2

    :cond_0
    const-string v2, "&apos;"

    goto :goto_1

    :cond_1
    const-string v2, "&amp;"

    goto :goto_1

    :cond_2
    const-string v2, "&gt;"

    goto :goto_1

    :cond_3
    const-string v2, "&lt;"

    goto :goto_1

    :cond_4
    const-string v2, "&quot;"

    :goto_1
    invoke-virtual {v0, v2}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    :goto_2
    add-int/lit8 v1, v1, 0x1

    goto :goto_0

    :cond_5
    new-instance p0, Ljava/lang/String;

    invoke-direct {p0, v0}, Ljava/lang/String;-><init>(Ljava/lang/StringBuffer;)V

    return-object p0
.end method

.method public static getTokenNames()[Ljava/lang/String;
    .locals 1

    sget-object v0, Lantlr/BaseAST;->tokenNames:[Ljava/lang/String;

    return-object v0
.end method

.method public static setVerboseStringConversion(Z[Ljava/lang/String;)V
    .locals 0

    sput-boolean p0, Lantlr/BaseAST;->verboseStringConversion:Z

    sput-object p1, Lantlr/BaseAST;->tokenNames:[Ljava/lang/String;

    return-void
.end method


# virtual methods
.method public addChild(Lantlr/collections/AST;)V
    .locals 1

    if-nez p1, :cond_0

    return-void

    :cond_0
    iget-object v0, p0, Lantlr/BaseAST;->down:Lantlr/BaseAST;

    if-eqz v0, :cond_2

    :goto_0
    iget-object p0, v0, Lantlr/BaseAST;->right:Lantlr/BaseAST;

    if-eqz p0, :cond_1

    move-object v0, p0

    goto :goto_0

    :cond_1
    check-cast p1, Lantlr/BaseAST;

    iput-object p1, v0, Lantlr/BaseAST;->right:Lantlr/BaseAST;

    goto :goto_1

    :cond_2
    check-cast p1, Lantlr/BaseAST;

    iput-object p1, p0, Lantlr/BaseAST;->down:Lantlr/BaseAST;

    :goto_1
    return-void
.end method

.method public equals(Lantlr/collections/AST;)Z
    .locals 4

    const/4 v0, 0x0

    if-nez p1, :cond_0

    return v0

    :cond_0
    invoke-virtual {p0}, Lantlr/BaseAST;->getText()Ljava/lang/String;

    move-result-object v1

    if-nez v1, :cond_1

    invoke-interface {p1}, Lantlr/collections/AST;->getText()Ljava/lang/String;

    move-result-object v1

    if-nez v1, :cond_2

    :cond_1
    invoke-virtual {p0}, Lantlr/BaseAST;->getText()Ljava/lang/String;

    move-result-object v1

    if-eqz v1, :cond_3

    invoke-interface {p1}, Lantlr/collections/AST;->getText()Ljava/lang/String;

    move-result-object v1

    if-nez v1, :cond_3

    :cond_2
    return v0

    :cond_3
    invoke-virtual {p0}, Lantlr/BaseAST;->getText()Ljava/lang/String;

    move-result-object v1

    const/4 v2, 0x1

    if-nez v1, :cond_5

    invoke-interface {p1}, Lantlr/collections/AST;->getText()Ljava/lang/String;

    move-result-object v1

    if-nez v1, :cond_5

    invoke-virtual {p0}, Lantlr/BaseAST;->getType()I

    move-result p0

    invoke-interface {p1}, Lantlr/collections/AST;->getType()I

    move-result p1

    if-ne p0, p1, :cond_4

    move v0, v2

    :cond_4
    return v0

    :cond_5
    invoke-virtual {p0}, Lantlr/BaseAST;->getText()Ljava/lang/String;

    move-result-object v1

    invoke-interface {p1}, Lantlr/collections/AST;->getText()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v1, v3}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v1

    if-eqz v1, :cond_6

    invoke-virtual {p0}, Lantlr/BaseAST;->getType()I

    move-result p0

    invoke-interface {p1}, Lantlr/collections/AST;->getType()I

    move-result p1

    if-ne p0, p1, :cond_6

    move v0, v2

    :cond_6
    return v0
.end method

.method public equalsList(Lantlr/collections/AST;)Z
    .locals 3

    const/4 v0, 0x0

    if-nez p1, :cond_0

    return v0

    :cond_0
    :goto_0
    if-eqz p0, :cond_4

    if-eqz p1, :cond_4

    invoke-interface {p0, p1}, Lantlr/collections/AST;->equals(Lantlr/collections/AST;)Z

    move-result v1

    if-nez v1, :cond_1

    return v0

    :cond_1
    invoke-interface {p0}, Lantlr/collections/AST;->getFirstChild()Lantlr/collections/AST;

    move-result-object v1

    if-eqz v1, :cond_2

    invoke-interface {p0}, Lantlr/collections/AST;->getFirstChild()Lantlr/collections/AST;

    move-result-object v1

    invoke-interface {p1}, Lantlr/collections/AST;->getFirstChild()Lantlr/collections/AST;

    move-result-object v2

    invoke-interface {v1, v2}, Lantlr/collections/AST;->equalsList(Lantlr/collections/AST;)Z

    move-result v1

    if-nez v1, :cond_3

    return v0

    :cond_2
    invoke-interface {p1}, Lantlr/collections/AST;->getFirstChild()Lantlr/collections/AST;

    move-result-object v1

    if-eqz v1, :cond_3

    return v0

    :cond_3
    invoke-interface {p0}, Lantlr/collections/AST;->getNextSibling()Lantlr/collections/AST;

    move-result-object p0

    invoke-interface {p1}, Lantlr/collections/AST;->getNextSibling()Lantlr/collections/AST;

    move-result-object p1

    goto :goto_0

    :cond_4
    if-nez p0, :cond_5

    if-nez p1, :cond_5

    const/4 p0, 0x1

    return p0

    :cond_5
    return v0
.end method

.method public equalsListPartial(Lantlr/collections/AST;)Z
    .locals 4

    const/4 v0, 0x1

    if-nez p1, :cond_0

    return v0

    :cond_0
    :goto_0
    const/4 v1, 0x0

    if-eqz p0, :cond_3

    if-eqz p1, :cond_3

    invoke-interface {p0, p1}, Lantlr/collections/AST;->equals(Lantlr/collections/AST;)Z

    move-result v2

    if-nez v2, :cond_1

    return v1

    :cond_1
    invoke-interface {p0}, Lantlr/collections/AST;->getFirstChild()Lantlr/collections/AST;

    move-result-object v2

    if-eqz v2, :cond_2

    invoke-interface {p0}, Lantlr/collections/AST;->getFirstChild()Lantlr/collections/AST;

    move-result-object v2

    invoke-interface {p1}, Lantlr/collections/AST;->getFirstChild()Lantlr/collections/AST;

    move-result-object v3

    invoke-interface {v2, v3}, Lantlr/collections/AST;->equalsListPartial(Lantlr/collections/AST;)Z

    move-result v2

    if-nez v2, :cond_2

    return v1

    :cond_2
    invoke-interface {p0}, Lantlr/collections/AST;->getNextSibling()Lantlr/collections/AST;

    move-result-object p0

    invoke-interface {p1}, Lantlr/collections/AST;->getNextSibling()Lantlr/collections/AST;

    move-result-object p1

    goto :goto_0

    :cond_3
    if-nez p0, :cond_4

    if-eqz p1, :cond_4

    return v1

    :cond_4
    return v0
.end method

.method public equalsTree(Lantlr/collections/AST;)Z
    .locals 2

    invoke-virtual {p0, p1}, Lantlr/BaseAST;->equals(Lantlr/collections/AST;)Z

    move-result v0

    const/4 v1, 0x0

    if-nez v0, :cond_0

    return v1

    :cond_0
    invoke-virtual {p0}, Lantlr/BaseAST;->getFirstChild()Lantlr/collections/AST;

    move-result-object v0

    if-eqz v0, :cond_1

    invoke-virtual {p0}, Lantlr/BaseAST;->getFirstChild()Lantlr/collections/AST;

    move-result-object p0

    invoke-interface {p1}, Lantlr/collections/AST;->getFirstChild()Lantlr/collections/AST;

    move-result-object p1

    invoke-interface {p0, p1}, Lantlr/collections/AST;->equalsList(Lantlr/collections/AST;)Z

    move-result p0

    if-nez p0, :cond_2

    return v1

    :cond_1
    invoke-interface {p1}, Lantlr/collections/AST;->getFirstChild()Lantlr/collections/AST;

    move-result-object p0

    if-eqz p0, :cond_2

    return v1

    :cond_2
    const/4 p0, 0x1

    return p0
.end method

.method public equalsTreePartial(Lantlr/collections/AST;)Z
    .locals 3

    const/4 v0, 0x1

    if-nez p1, :cond_0

    return v0

    :cond_0
    invoke-virtual {p0, p1}, Lantlr/BaseAST;->equals(Lantlr/collections/AST;)Z

    move-result v1

    const/4 v2, 0x0

    if-nez v1, :cond_1

    return v2

    :cond_1
    invoke-virtual {p0}, Lantlr/BaseAST;->getFirstChild()Lantlr/collections/AST;

    move-result-object v1

    if-eqz v1, :cond_2

    invoke-virtual {p0}, Lantlr/BaseAST;->getFirstChild()Lantlr/collections/AST;

    move-result-object p0

    invoke-interface {p1}, Lantlr/collections/AST;->getFirstChild()Lantlr/collections/AST;

    move-result-object p1

    invoke-interface {p0, p1}, Lantlr/collections/AST;->equalsListPartial(Lantlr/collections/AST;)Z

    move-result p0

    if-nez p0, :cond_2

    return v2

    :cond_2
    return v0
.end method

.method public findAll(Lantlr/collections/AST;)Lantlr/collections/ASTEnumeration;
    .locals 2

    new-instance v0, Lantlr/collections/impl/Vector;

    const/16 v1, 0xa

    invoke-direct {v0, v1}, Lantlr/collections/impl/Vector;-><init>(I)V

    if-nez p1, :cond_0

    const/4 p0, 0x0

    return-object p0

    :cond_0
    const/4 v1, 0x0

    invoke-static {p0, v0, p1, v1}, Lantlr/BaseAST;->doWorkForFindAll(Lantlr/collections/AST;Lantlr/collections/impl/Vector;Lantlr/collections/AST;Z)V

    new-instance p0, Lantlr/collections/impl/ASTEnumerator;

    invoke-direct {p0, v0}, Lantlr/collections/impl/ASTEnumerator;-><init>(Lantlr/collections/impl/Vector;)V

    return-object p0
.end method

.method public findAllPartial(Lantlr/collections/AST;)Lantlr/collections/ASTEnumeration;
    .locals 2

    new-instance v0, Lantlr/collections/impl/Vector;

    const/16 v1, 0xa

    invoke-direct {v0, v1}, Lantlr/collections/impl/Vector;-><init>(I)V

    if-nez p1, :cond_0

    const/4 p0, 0x0

    return-object p0

    :cond_0
    const/4 v1, 0x1

    invoke-static {p0, v0, p1, v1}, Lantlr/BaseAST;->doWorkForFindAll(Lantlr/collections/AST;Lantlr/collections/impl/Vector;Lantlr/collections/AST;Z)V

    new-instance p0, Lantlr/collections/impl/ASTEnumerator;

    invoke-direct {p0, v0}, Lantlr/collections/impl/ASTEnumerator;-><init>(Lantlr/collections/impl/Vector;)V

    return-object p0
.end method

.method public getColumn()I
    .locals 0

    const/4 p0, 0x0

    return p0
.end method

.method public getFirstChild()Lantlr/collections/AST;
    .locals 0

    iget-object p0, p0, Lantlr/BaseAST;->down:Lantlr/BaseAST;

    return-object p0
.end method

.method public getLine()I
    .locals 0

    const/4 p0, 0x0

    return p0
.end method

.method public getNextSibling()Lantlr/collections/AST;
    .locals 0

    iget-object p0, p0, Lantlr/BaseAST;->right:Lantlr/BaseAST;

    return-object p0
.end method

.method public getNumberOfChildren()I
    .locals 1

    iget-object p0, p0, Lantlr/BaseAST;->down:Lantlr/BaseAST;

    if-eqz p0, :cond_1

    const/4 v0, 0x1

    :goto_0
    iget-object p0, p0, Lantlr/BaseAST;->right:Lantlr/BaseAST;

    if-eqz p0, :cond_0

    add-int/lit8 v0, v0, 0x1

    goto :goto_0

    :cond_0
    return v0

    :cond_1
    const/4 p0, 0x0

    return p0
.end method

.method public getText()Ljava/lang/String;
    .locals 0

    const-string p0, ""

    return-object p0
.end method

.method public getType()I
    .locals 0

    const/4 p0, 0x0

    return p0
.end method

.method public abstract initialize(ILjava/lang/String;)V
.end method

.method public abstract initialize(Lantlr/Token;)V
.end method

.method public abstract initialize(Lantlr/collections/AST;)V
.end method

.method public removeChildren()V
    .locals 1

    const/4 v0, 0x0

    iput-object v0, p0, Lantlr/BaseAST;->down:Lantlr/BaseAST;

    return-void
.end method

.method public setFirstChild(Lantlr/collections/AST;)V
    .locals 0

    check-cast p1, Lantlr/BaseAST;

    iput-object p1, p0, Lantlr/BaseAST;->down:Lantlr/BaseAST;

    return-void
.end method

.method public setNextSibling(Lantlr/collections/AST;)V
    .locals 0

    check-cast p1, Lantlr/BaseAST;

    iput-object p1, p0, Lantlr/BaseAST;->right:Lantlr/BaseAST;

    return-void
.end method

.method public setText(Ljava/lang/String;)V
    .locals 0

    return-void
.end method

.method public setType(I)V
    .locals 0

    return-void
.end method

.method public toString()Ljava/lang/String;
    .locals 4

    new-instance v0, Ljava/lang/StringBuffer;

    invoke-direct {v0}, Ljava/lang/StringBuffer;-><init>()V

    sget-boolean v1, Lantlr/BaseAST;->verboseStringConversion:Z

    if-eqz v1, :cond_0

    invoke-virtual {p0}, Lantlr/BaseAST;->getText()Ljava/lang/String;

    move-result-object v1

    if-eqz v1, :cond_0

    invoke-virtual {p0}, Lantlr/BaseAST;->getText()Ljava/lang/String;

    move-result-object v1

    sget-object v2, Lantlr/BaseAST;->tokenNames:[Ljava/lang/String;

    invoke-virtual {p0}, Lantlr/BaseAST;->getType()I

    move-result v3

    aget-object v2, v2, v3

    invoke-virtual {v1, v2}, Ljava/lang/String;->equalsIgnoreCase(Ljava/lang/String;)Z

    move-result v1

    if-nez v1, :cond_0

    invoke-virtual {p0}, Lantlr/BaseAST;->getText()Ljava/lang/String;

    move-result-object v1

    sget-object v2, Lantlr/BaseAST;->tokenNames:[Ljava/lang/String;

    invoke-virtual {p0}, Lantlr/BaseAST;->getType()I

    move-result v3

    aget-object v2, v2, v3

    const-string v3, "\""

    invoke-static {v2, v3, v3}, Lantlr/StringUtils;->stripFrontBack(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2}, Ljava/lang/String;->equalsIgnoreCase(Ljava/lang/String;)Z

    move-result v1

    if-nez v1, :cond_0

    const/16 v1, 0x5b

    invoke-virtual {v0, v1}, Ljava/lang/StringBuffer;->append(C)Ljava/lang/StringBuffer;

    invoke-virtual {p0}, Lantlr/BaseAST;->getText()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    const-string v1, ",<"

    invoke-virtual {v0, v1}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    sget-object v1, Lantlr/BaseAST;->tokenNames:[Ljava/lang/String;

    invoke-virtual {p0}, Lantlr/BaseAST;->getType()I

    move-result p0

    aget-object p0, v1, p0

    invoke-virtual {v0, p0}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    const-string p0, ">]"

    invoke-virtual {v0, p0}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    invoke-virtual {v0}, Ljava/lang/StringBuffer;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0

    :cond_0
    invoke-virtual {p0}, Lantlr/BaseAST;->getText()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public toStringList()Ljava/lang/String;
    .locals 2

    invoke-interface {p0}, Lantlr/collections/AST;->getFirstChild()Lantlr/collections/AST;

    move-result-object v0

    const-string v1, ""

    if-eqz v0, :cond_0

    const-string v0, " ("

    invoke-static {v1, v0}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    :cond_0
    const-string v0, " "

    invoke-static {v1, v0}, La/a/a/a/a;->b(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/BaseAST;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-interface {p0}, Lantlr/collections/AST;->getFirstChild()Lantlr/collections/AST;

    move-result-object v1

    if-eqz v1, :cond_1

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-interface {p0}, Lantlr/collections/AST;->getFirstChild()Lantlr/collections/AST;

    move-result-object v1

    check-cast v1, Lantlr/BaseAST;

    invoke-virtual {v1}, Lantlr/BaseAST;->toStringList()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    :cond_1
    invoke-interface {p0}, Lantlr/collections/AST;->getFirstChild()Lantlr/collections/AST;

    move-result-object v1

    if-eqz v1, :cond_2

    const-string v1, " )"

    invoke-static {v0, v1}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    :cond_2
    invoke-interface {p0}, Lantlr/collections/AST;->getNextSibling()Lantlr/collections/AST;

    move-result-object v1

    if-eqz v1, :cond_3

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-interface {p0}, Lantlr/collections/AST;->getNextSibling()Lantlr/collections/AST;

    move-result-object p0

    check-cast p0, Lantlr/BaseAST;

    invoke-virtual {p0}, Lantlr/BaseAST;->toStringList()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    :cond_3
    return-object v0
.end method

.method public toStringTree()Ljava/lang/String;
    .locals 2

    invoke-interface {p0}, Lantlr/collections/AST;->getFirstChild()Lantlr/collections/AST;

    move-result-object v0

    const-string v1, ""

    if-eqz v0, :cond_0

    const-string v0, " ("

    invoke-static {v1, v0}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    :cond_0
    const-string v0, " "

    invoke-static {v1, v0}, La/a/a/a/a;->b(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/BaseAST;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-interface {p0}, Lantlr/collections/AST;->getFirstChild()Lantlr/collections/AST;

    move-result-object v1

    if-eqz v1, :cond_1

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-interface {p0}, Lantlr/collections/AST;->getFirstChild()Lantlr/collections/AST;

    move-result-object v1

    check-cast v1, Lantlr/BaseAST;

    invoke-virtual {v1}, Lantlr/BaseAST;->toStringList()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    :cond_1
    invoke-interface {p0}, Lantlr/collections/AST;->getFirstChild()Lantlr/collections/AST;

    move-result-object p0

    if-eqz p0, :cond_2

    const-string p0, " )"

    invoke-static {v0, p0}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    :cond_2
    return-object v0
.end method

.method public xmlSerialize(Ljava/io/Writer;)V
    .locals 2

    :goto_0
    if-eqz p0, :cond_1

    invoke-interface {p0}, Lantlr/collections/AST;->getFirstChild()Lantlr/collections/AST;

    move-result-object v0

    if-nez v0, :cond_0

    move-object v0, p0

    check-cast v0, Lantlr/BaseAST;

    invoke-virtual {v0, p1}, Lantlr/BaseAST;->xmlSerializeNode(Ljava/io/Writer;)V

    goto :goto_1

    :cond_0
    move-object v0, p0

    check-cast v0, Lantlr/BaseAST;

    invoke-virtual {v0, p1}, Lantlr/BaseAST;->xmlSerializeRootOpen(Ljava/io/Writer;)V

    invoke-interface {p0}, Lantlr/collections/AST;->getFirstChild()Lantlr/collections/AST;

    move-result-object v1

    check-cast v1, Lantlr/BaseAST;

    invoke-virtual {v1, p1}, Lantlr/BaseAST;->xmlSerialize(Ljava/io/Writer;)V

    invoke-virtual {v0, p1}, Lantlr/BaseAST;->xmlSerializeRootClose(Ljava/io/Writer;)V

    :goto_1
    invoke-interface {p0}, Lantlr/collections/AST;->getNextSibling()Lantlr/collections/AST;

    move-result-object p0

    goto :goto_0

    :cond_1
    return-void
.end method

.method public xmlSerializeNode(Ljava/io/Writer;)V
    .locals 3

    new-instance v0, Ljava/lang/StringBuffer;

    const/16 v1, 0x64

    invoke-direct {v0, v1}, Ljava/lang/StringBuffer;-><init>(I)V

    const-string v1, "<"

    invoke-virtual {v0, v1}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p0}, Ljava/lang/Object;->getClass()Ljava/lang/Class;

    move-result-object v2

    invoke-virtual {v2}, Ljava/lang/Class;->getName()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, " "

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "text=\""

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0}, Lantlr/BaseAST;->getText()Ljava/lang/String;

    move-result-object v2

    invoke-static {v2}, Lantlr/BaseAST;->encode(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, "\" type=\""

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0}, Lantlr/BaseAST;->getType()I

    move-result p0

    invoke-virtual {v1, p0}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string p0, "\"/>"

    invoke-virtual {v1, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {v0, p0}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    invoke-virtual {v0}, Ljava/lang/StringBuffer;->toString()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p1, p0}, Ljava/io/Writer;->write(Ljava/lang/String;)V

    return-void
.end method

.method public xmlSerializeRootClose(Ljava/io/Writer;)V
    .locals 1

    const-string v0, "</"

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {p0}, Ljava/lang/Object;->getClass()Ljava/lang/Class;

    move-result-object p0

    invoke-virtual {p0}, Ljava/lang/Class;->getName()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p0, ">\n"

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p1, p0}, Ljava/io/Writer;->write(Ljava/lang/String;)V

    return-void
.end method

.method public xmlSerializeRootOpen(Ljava/io/Writer;)V
    .locals 3

    new-instance v0, Ljava/lang/StringBuffer;

    const/16 v1, 0x64

    invoke-direct {v0, v1}, Ljava/lang/StringBuffer;-><init>(I)V

    const-string v1, "<"

    invoke-virtual {v0, v1}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p0}, Ljava/lang/Object;->getClass()Ljava/lang/Class;

    move-result-object v2

    invoke-virtual {v2}, Ljava/lang/Class;->getName()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, " "

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "text=\""

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0}, Lantlr/BaseAST;->getText()Ljava/lang/String;

    move-result-object v2

    invoke-static {v2}, Lantlr/BaseAST;->encode(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, "\" type=\""

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0}, Lantlr/BaseAST;->getType()I

    move-result p0

    invoke-virtual {v1, p0}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string p0, "\">\n"

    invoke-virtual {v1, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {v0, p0}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    invoke-virtual {v0}, Ljava/lang/StringBuffer;->toString()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p1, p0}, Ljava/io/Writer;->write(Ljava/lang/String;)V

    return-void
.end method
