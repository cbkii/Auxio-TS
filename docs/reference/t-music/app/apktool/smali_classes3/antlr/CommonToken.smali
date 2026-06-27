.class public Lantlr/CommonToken;
.super Lantlr/Token;
.source ""


# instance fields
.field public col:I

.field public line:I

.field public text:Ljava/lang/String;


# direct methods
.method public constructor <init>()V
    .locals 1

    invoke-direct {p0}, Lantlr/Token;-><init>()V

    const/4 v0, 0x0

    iput-object v0, p0, Lantlr/CommonToken;->text:Ljava/lang/String;

    return-void
.end method

.method public constructor <init>(ILjava/lang/String;)V
    .locals 1

    invoke-direct {p0}, Lantlr/Token;-><init>()V

    const/4 v0, 0x0

    iput-object v0, p0, Lantlr/CommonToken;->text:Ljava/lang/String;

    iput p1, p0, Lantlr/Token;->type:I

    invoke-virtual {p0, p2}, Lantlr/CommonToken;->setText(Ljava/lang/String;)V

    return-void
.end method

.method public constructor <init>(Ljava/lang/String;)V
    .locals 1

    invoke-direct {p0}, Lantlr/Token;-><init>()V

    const/4 v0, 0x0

    iput-object v0, p0, Lantlr/CommonToken;->text:Ljava/lang/String;

    iput-object p1, p0, Lantlr/CommonToken;->text:Ljava/lang/String;

    return-void
.end method


# virtual methods
.method public getColumn()I
    .locals 0

    iget p0, p0, Lantlr/CommonToken;->col:I

    return p0
.end method

.method public getLine()I
    .locals 0

    iget p0, p0, Lantlr/CommonToken;->line:I

    return p0
.end method

.method public getText()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lantlr/CommonToken;->text:Ljava/lang/String;

    return-object p0
.end method

.method public setColumn(I)V
    .locals 0

    iput p1, p0, Lantlr/CommonToken;->col:I

    return-void
.end method

.method public setLine(I)V
    .locals 0

    iput p1, p0, Lantlr/CommonToken;->line:I

    return-void
.end method

.method public setText(Ljava/lang/String;)V
    .locals 0

    iput-object p1, p0, Lantlr/CommonToken;->text:Ljava/lang/String;

    return-void
.end method

.method public toString()Ljava/lang/String;
    .locals 2

    const-string v0, "[\""

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/CommonToken;->getText()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, "\",<"

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget v1, p0, Lantlr/Token;->type:I

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v1, ">,line="

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget v1, p0, Lantlr/CommonToken;->line:I

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v1, ",col="

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget p0, p0, Lantlr/CommonToken;->col:I

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string p0, "]"

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method
