.class public Lantlr/ParseTreeToken;
.super Lantlr/ParseTree;
.source ""


# instance fields
.field public token:Lantlr/Token;


# direct methods
.method public constructor <init>(Lantlr/Token;)V
    .locals 0

    invoke-direct {p0}, Lantlr/ParseTree;-><init>()V

    iput-object p1, p0, Lantlr/ParseTreeToken;->token:Lantlr/Token;

    return-void
.end method


# virtual methods
.method public getLeftmostDerivation(Ljava/lang/StringBuffer;I)I
    .locals 1

    const/16 v0, 0x20

    invoke-virtual {p1, v0}, Ljava/lang/StringBuffer;->append(C)Ljava/lang/StringBuffer;

    invoke-virtual {p0}, Lantlr/ParseTreeToken;->toString()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p1, p0}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    return p2
.end method

.method public toString()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lantlr/ParseTreeToken;->token:Lantlr/Token;

    if-eqz p0, :cond_0

    invoke-virtual {p0}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p0

    return-object p0

    :cond_0
    const-string p0, "<missing token>"

    return-object p0
.end method
