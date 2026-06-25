.class public abstract Lantlr/AlternativeElement;
.super Lantlr/GrammarElement;
.source ""


# instance fields
.field public autoGenType:I

.field public enclosingRuleName:Ljava/lang/String;

.field public next:Lantlr/AlternativeElement;


# direct methods
.method public constructor <init>(Lantlr/Grammar;)V
    .locals 0

    invoke-direct {p0, p1}, Lantlr/GrammarElement;-><init>(Lantlr/Grammar;)V

    const/4 p1, 0x1

    iput p1, p0, Lantlr/AlternativeElement;->autoGenType:I

    return-void
.end method

.method public constructor <init>(Lantlr/Grammar;Lantlr/Token;)V
    .locals 0

    invoke-direct {p0, p1, p2}, Lantlr/GrammarElement;-><init>(Lantlr/Grammar;Lantlr/Token;)V

    const/4 p1, 0x1

    iput p1, p0, Lantlr/AlternativeElement;->autoGenType:I

    return-void
.end method

.method public constructor <init>(Lantlr/Grammar;Lantlr/Token;I)V
    .locals 0

    invoke-direct {p0, p1, p2}, Lantlr/GrammarElement;-><init>(Lantlr/Grammar;Lantlr/Token;)V

    const/4 p1, 0x1

    iput p1, p0, Lantlr/AlternativeElement;->autoGenType:I

    iput p3, p0, Lantlr/AlternativeElement;->autoGenType:I

    return-void
.end method


# virtual methods
.method public getAutoGenType()I
    .locals 0

    iget p0, p0, Lantlr/AlternativeElement;->autoGenType:I

    return p0
.end method

.method public getLabel()Ljava/lang/String;
    .locals 0

    const/4 p0, 0x0

    return-object p0
.end method

.method public setAutoGenType(I)V
    .locals 0

    iput p1, p0, Lantlr/AlternativeElement;->autoGenType:I

    return-void
.end method

.method public setLabel(Ljava/lang/String;)V
    .locals 0

    return-void
.end method
