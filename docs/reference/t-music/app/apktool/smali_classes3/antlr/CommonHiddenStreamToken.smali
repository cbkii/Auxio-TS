.class public Lantlr/CommonHiddenStreamToken;
.super Lantlr/CommonToken;
.source ""


# instance fields
.field public hiddenAfter:Lantlr/CommonHiddenStreamToken;

.field public hiddenBefore:Lantlr/CommonHiddenStreamToken;


# direct methods
.method public constructor <init>()V
    .locals 0

    invoke-direct {p0}, Lantlr/CommonToken;-><init>()V

    return-void
.end method

.method public constructor <init>(ILjava/lang/String;)V
    .locals 0

    invoke-direct {p0, p1, p2}, Lantlr/CommonToken;-><init>(ILjava/lang/String;)V

    return-void
.end method

.method public constructor <init>(Ljava/lang/String;)V
    .locals 0

    invoke-direct {p0, p1}, Lantlr/CommonToken;-><init>(Ljava/lang/String;)V

    return-void
.end method


# virtual methods
.method public getHiddenAfter()Lantlr/CommonHiddenStreamToken;
    .locals 0

    iget-object p0, p0, Lantlr/CommonHiddenStreamToken;->hiddenAfter:Lantlr/CommonHiddenStreamToken;

    return-object p0
.end method

.method public getHiddenBefore()Lantlr/CommonHiddenStreamToken;
    .locals 0

    iget-object p0, p0, Lantlr/CommonHiddenStreamToken;->hiddenBefore:Lantlr/CommonHiddenStreamToken;

    return-object p0
.end method

.method public setHiddenAfter(Lantlr/CommonHiddenStreamToken;)V
    .locals 0

    iput-object p1, p0, Lantlr/CommonHiddenStreamToken;->hiddenAfter:Lantlr/CommonHiddenStreamToken;

    return-void
.end method

.method public setHiddenBefore(Lantlr/CommonHiddenStreamToken;)V
    .locals 0

    iput-object p1, p0, Lantlr/CommonHiddenStreamToken;->hiddenBefore:Lantlr/CommonHiddenStreamToken;

    return-void
.end method
