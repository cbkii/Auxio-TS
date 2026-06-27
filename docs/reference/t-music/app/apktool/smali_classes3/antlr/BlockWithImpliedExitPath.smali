.class public abstract Lantlr/BlockWithImpliedExitPath;
.super Lantlr/AlternativeBlock;
.source ""


# instance fields
.field public exitCache:[Lantlr/Lookahead;

.field public exitLookaheadDepth:I


# direct methods
.method public constructor <init>(Lantlr/Grammar;)V
    .locals 0

    invoke-direct {p0, p1}, Lantlr/AlternativeBlock;-><init>(Lantlr/Grammar;)V

    iget-object p1, p0, Lantlr/GrammarElement;->grammar:Lantlr/Grammar;

    iget p1, p1, Lantlr/Grammar;->maxk:I

    add-int/lit8 p1, p1, 0x1

    new-array p1, p1, [Lantlr/Lookahead;

    iput-object p1, p0, Lantlr/BlockWithImpliedExitPath;->exitCache:[Lantlr/Lookahead;

    return-void
.end method

.method public constructor <init>(Lantlr/Grammar;Lantlr/Token;)V
    .locals 1

    const/4 v0, 0x0

    invoke-direct {p0, p1, p2, v0}, Lantlr/AlternativeBlock;-><init>(Lantlr/Grammar;Lantlr/Token;Z)V

    iget-object p1, p0, Lantlr/GrammarElement;->grammar:Lantlr/Grammar;

    iget p1, p1, Lantlr/Grammar;->maxk:I

    add-int/lit8 p1, p1, 0x1

    new-array p1, p1, [Lantlr/Lookahead;

    iput-object p1, p0, Lantlr/BlockWithImpliedExitPath;->exitCache:[Lantlr/Lookahead;

    return-void
.end method
