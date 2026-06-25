.class public Lantlr/NoViableAltForCharException;
.super Lantlr/RecognitionException;
.source ""


# instance fields
.field public foundChar:C


# direct methods
.method public constructor <init>(CLantlr/CharScanner;)V
    .locals 3

    invoke-virtual {p2}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p2}, Lantlr/CharScanner;->getLine()I

    move-result v1

    invoke-virtual {p2}, Lantlr/CharScanner;->getColumn()I

    move-result p2

    const-string v2, "NoViableAlt"

    invoke-direct {p0, v2, v0, v1, p2}, Lantlr/RecognitionException;-><init>(Ljava/lang/String;Ljava/lang/String;II)V

    iput-char p1, p0, Lantlr/NoViableAltForCharException;->foundChar:C

    return-void
.end method

.method public constructor <init>(CLjava/lang/String;I)V
    .locals 1

    const/4 v0, -0x1

    invoke-direct {p0, p1, p2, p3, v0}, Lantlr/NoViableAltForCharException;-><init>(CLjava/lang/String;II)V

    return-void
.end method

.method public constructor <init>(CLjava/lang/String;II)V
    .locals 1

    const-string v0, "NoViableAlt"

    invoke-direct {p0, v0, p2, p3, p4}, Lantlr/RecognitionException;-><init>(Ljava/lang/String;Ljava/lang/String;II)V

    iput-char p1, p0, Lantlr/NoViableAltForCharException;->foundChar:C

    return-void
.end method


# virtual methods
.method public getMessage()Ljava/lang/String;
    .locals 3

    iget-char v0, p0, Lantlr/NoViableAltForCharException;->foundChar:C

    const-string v1, "unexpected char: "

    const/16 v2, 0x20

    if-lt v0, v2, :cond_0

    const/16 v2, 0x7e

    if-gt v0, v2, :cond_0

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const/16 v1, 0x27

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(C)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget-char p0, p0, Lantlr/NoViableAltForCharException;->foundChar:C

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(C)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(C)Ljava/lang/StringBuilder;

    goto :goto_0

    :cond_0
    const-string v0, "0x"

    invoke-static {v1, v0}, La/a/a/a/a;->b(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget-char p0, p0, Lantlr/NoViableAltForCharException;->foundChar:C

    invoke-static {p0}, Ljava/lang/Integer;->toHexString(I)Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p0}, Ljava/lang/String;->toUpperCase()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    :goto_0
    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method
