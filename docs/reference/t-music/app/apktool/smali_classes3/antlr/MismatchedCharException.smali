.class public Lantlr/MismatchedCharException;
.super Lantlr/RecognitionException;
.source ""


# static fields
.field public static final CHAR:I = 0x1

.field public static final NOT_CHAR:I = 0x2

.field public static final NOT_RANGE:I = 0x4

.field public static final NOT_SET:I = 0x6

.field public static final RANGE:I = 0x3

.field public static final SET:I = 0x5


# instance fields
.field public expecting:I

.field public foundChar:I

.field public mismatchType:I

.field public scanner:Lantlr/CharScanner;

.field public set:Lantlr/collections/impl/BitSet;

.field public upper:I


# direct methods
.method public constructor <init>()V
    .locals 1

    const-string v0, "Mismatched char"

    invoke-direct {p0, v0}, Lantlr/RecognitionException;-><init>(Ljava/lang/String;)V

    return-void
.end method

.method public constructor <init>(CCCZLantlr/CharScanner;)V
    .locals 4

    invoke-virtual {p5}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p5}, Lantlr/CharScanner;->getLine()I

    move-result v1

    invoke-virtual {p5}, Lantlr/CharScanner;->getColumn()I

    move-result v2

    const-string v3, "Mismatched char"

    invoke-direct {p0, v3, v0, v1, v2}, Lantlr/RecognitionException;-><init>(Ljava/lang/String;Ljava/lang/String;II)V

    if-eqz p4, :cond_0

    const/4 p4, 0x4

    goto :goto_0

    :cond_0
    const/4 p4, 0x3

    :goto_0
    iput p4, p0, Lantlr/MismatchedCharException;->mismatchType:I

    iput p1, p0, Lantlr/MismatchedCharException;->foundChar:I

    iput p2, p0, Lantlr/MismatchedCharException;->expecting:I

    iput p3, p0, Lantlr/MismatchedCharException;->upper:I

    iput-object p5, p0, Lantlr/MismatchedCharException;->scanner:Lantlr/CharScanner;

    return-void
.end method

.method public constructor <init>(CCZLantlr/CharScanner;)V
    .locals 4

    invoke-virtual {p4}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p4}, Lantlr/CharScanner;->getLine()I

    move-result v1

    invoke-virtual {p4}, Lantlr/CharScanner;->getColumn()I

    move-result v2

    const-string v3, "Mismatched char"

    invoke-direct {p0, v3, v0, v1, v2}, Lantlr/RecognitionException;-><init>(Ljava/lang/String;Ljava/lang/String;II)V

    if-eqz p3, :cond_0

    const/4 p3, 0x2

    goto :goto_0

    :cond_0
    const/4 p3, 0x1

    :goto_0
    iput p3, p0, Lantlr/MismatchedCharException;->mismatchType:I

    iput p1, p0, Lantlr/MismatchedCharException;->foundChar:I

    iput p2, p0, Lantlr/MismatchedCharException;->expecting:I

    iput-object p4, p0, Lantlr/MismatchedCharException;->scanner:Lantlr/CharScanner;

    return-void
.end method

.method public constructor <init>(CLantlr/collections/impl/BitSet;ZLantlr/CharScanner;)V
    .locals 4

    invoke-virtual {p4}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p4}, Lantlr/CharScanner;->getLine()I

    move-result v1

    invoke-virtual {p4}, Lantlr/CharScanner;->getColumn()I

    move-result v2

    const-string v3, "Mismatched char"

    invoke-direct {p0, v3, v0, v1, v2}, Lantlr/RecognitionException;-><init>(Ljava/lang/String;Ljava/lang/String;II)V

    if-eqz p3, :cond_0

    const/4 p3, 0x6

    goto :goto_0

    :cond_0
    const/4 p3, 0x5

    :goto_0
    iput p3, p0, Lantlr/MismatchedCharException;->mismatchType:I

    iput p1, p0, Lantlr/MismatchedCharException;->foundChar:I

    iput-object p2, p0, Lantlr/MismatchedCharException;->set:Lantlr/collections/impl/BitSet;

    iput-object p4, p0, Lantlr/MismatchedCharException;->scanner:Lantlr/CharScanner;

    return-void
.end method

.method private appendCharName(Ljava/lang/StringBuffer;I)V
    .locals 0

    const/16 p0, 0x9

    if-eq p2, p0, :cond_3

    const/16 p0, 0xa

    if-eq p2, p0, :cond_2

    const/16 p0, 0xd

    if-eq p2, p0, :cond_1

    const p0, 0xffff

    if-eq p2, p0, :cond_0

    const/16 p0, 0x27

    invoke-virtual {p1, p0}, Ljava/lang/StringBuffer;->append(C)Ljava/lang/StringBuffer;

    int-to-char p2, p2

    invoke-virtual {p1, p2}, Ljava/lang/StringBuffer;->append(C)Ljava/lang/StringBuffer;

    invoke-virtual {p1, p0}, Ljava/lang/StringBuffer;->append(C)Ljava/lang/StringBuffer;

    goto :goto_1

    :cond_0
    const-string p0, "\'<EOF>\'"

    goto :goto_0

    :cond_1
    const-string p0, "\'\\r\'"

    goto :goto_0

    :cond_2
    const-string p0, "\'\\n\'"

    goto :goto_0

    :cond_3
    const-string p0, "\'\\t\'"

    :goto_0
    invoke-virtual {p1, p0}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    :goto_1
    return-void
.end method


# virtual methods
.method public getMessage()Ljava/lang/String;
    .locals 5

    new-instance v0, Ljava/lang/StringBuffer;

    invoke-direct {v0}, Ljava/lang/StringBuffer;-><init>()V

    iget v1, p0, Lantlr/MismatchedCharException;->mismatchType:I

    const-string v2, "NOT "

    const-string v3, ", found "

    const-string v4, "expecting "

    packed-switch v1, :pswitch_data_0

    invoke-super {p0}, Ljava/lang/Exception;->getMessage()Ljava/lang/String;

    move-result-object p0

    :goto_0
    invoke-virtual {v0, p0}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    goto :goto_5

    :pswitch_0
    invoke-static {v4}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    iget v3, p0, Lantlr/MismatchedCharException;->mismatchType:I

    const/4 v4, 0x6

    if-ne v3, v4, :cond_0

    goto :goto_1

    :cond_0
    const-string v2, ""

    :goto_1
    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, "one of ("

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    iget-object v1, p0, Lantlr/MismatchedCharException;->set:Lantlr/collections/impl/BitSet;

    invoke-virtual {v1}, Lantlr/collections/impl/BitSet;->toArray()[I

    move-result-object v1

    const/4 v2, 0x0

    :goto_2
    array-length v3, v1

    if-ge v2, v3, :cond_1

    aget v3, v1, v2

    invoke-direct {p0, v0, v3}, Lantlr/MismatchedCharException;->appendCharName(Ljava/lang/StringBuffer;I)V

    add-int/lit8 v2, v2, 0x1

    goto :goto_2

    :cond_1
    const-string v1, "), found "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    goto :goto_4

    :pswitch_1
    const-string v1, "expecting token "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    iget v1, p0, Lantlr/MismatchedCharException;->mismatchType:I

    const/4 v4, 0x4

    if-ne v1, v4, :cond_2

    invoke-virtual {v0, v2}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    :cond_2
    const-string v1, "in range: "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    iget v1, p0, Lantlr/MismatchedCharException;->expecting:I

    invoke-direct {p0, v0, v1}, Lantlr/MismatchedCharException;->appendCharName(Ljava/lang/StringBuffer;I)V

    const-string v1, ".."

    invoke-virtual {v0, v1}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    iget v1, p0, Lantlr/MismatchedCharException;->upper:I

    goto :goto_3

    :pswitch_2
    const-string v1, "expecting anything but \'"

    invoke-virtual {v0, v1}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    iget v1, p0, Lantlr/MismatchedCharException;->expecting:I

    invoke-direct {p0, v0, v1}, Lantlr/MismatchedCharException;->appendCharName(Ljava/lang/StringBuffer;I)V

    const-string p0, "\'; got it anyway"

    goto :goto_0

    :pswitch_3
    invoke-virtual {v0, v4}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    iget v1, p0, Lantlr/MismatchedCharException;->expecting:I

    :goto_3
    invoke-direct {p0, v0, v1}, Lantlr/MismatchedCharException;->appendCharName(Ljava/lang/StringBuffer;I)V

    invoke-virtual {v0, v3}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    :goto_4
    iget v1, p0, Lantlr/MismatchedCharException;->foundChar:I

    invoke-direct {p0, v0, v1}, Lantlr/MismatchedCharException;->appendCharName(Ljava/lang/StringBuffer;I)V

    :goto_5
    invoke-virtual {v0}, Ljava/lang/StringBuffer;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0

    :pswitch_data_0
    .packed-switch 0x1
        :pswitch_3
        :pswitch_2
        :pswitch_1
        :pswitch_1
        :pswitch_0
        :pswitch_0
    .end packed-switch
.end method
