.class public abstract Lantlr/GrammarElement;
.super Ljava/lang/Object;
.source ""


# static fields
.field public static final AUTO_GEN_BANG:I = 0x3

.field public static final AUTO_GEN_CARET:I = 0x2

.field public static final AUTO_GEN_NONE:I = 0x1


# instance fields
.field public column:I

.field public grammar:Lantlr/Grammar;

.field public line:I


# direct methods
.method public constructor <init>(Lantlr/Grammar;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lantlr/GrammarElement;->grammar:Lantlr/Grammar;

    const/4 p1, -0x1

    iput p1, p0, Lantlr/GrammarElement;->line:I

    iput p1, p0, Lantlr/GrammarElement;->column:I

    return-void
.end method

.method public constructor <init>(Lantlr/Grammar;Lantlr/Token;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lantlr/GrammarElement;->grammar:Lantlr/Grammar;

    invoke-virtual {p2}, Lantlr/Token;->getLine()I

    move-result p1

    iput p1, p0, Lantlr/GrammarElement;->line:I

    invoke-virtual {p2}, Lantlr/Token;->getColumn()I

    move-result p1

    iput p1, p0, Lantlr/GrammarElement;->column:I

    return-void
.end method


# virtual methods
.method public generate()V
    .locals 0

    return-void
.end method

.method public getColumn()I
    .locals 0

    iget p0, p0, Lantlr/GrammarElement;->column:I

    return p0
.end method

.method public getLine()I
    .locals 0

    iget p0, p0, Lantlr/GrammarElement;->line:I

    return p0
.end method

.method public look(I)Lantlr/Lookahead;
    .locals 0

    const/4 p0, 0x0

    return-object p0
.end method

.method public abstract toString()Ljava/lang/String;
.end method
