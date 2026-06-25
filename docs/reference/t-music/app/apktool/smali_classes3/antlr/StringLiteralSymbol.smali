.class public Lantlr/StringLiteralSymbol;
.super Lantlr/TokenSymbol;
.source ""


# instance fields
.field public label:Ljava/lang/String;


# direct methods
.method public constructor <init>(Ljava/lang/String;)V
    .locals 0

    invoke-direct {p0, p1}, Lantlr/TokenSymbol;-><init>(Ljava/lang/String;)V

    return-void
.end method


# virtual methods
.method public getLabel()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lantlr/StringLiteralSymbol;->label:Ljava/lang/String;

    return-object p0
.end method

.method public setLabel(Ljava/lang/String;)V
    .locals 0

    iput-object p1, p0, Lantlr/StringLiteralSymbol;->label:Ljava/lang/String;

    return-void
.end method
