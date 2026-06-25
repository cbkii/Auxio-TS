.class public Lantlr/PythonCharFormatter;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Lantlr/CharFormatter;


# direct methods
.method public constructor <init>()V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public _escapeChar(IZ)Ljava/lang/String;
    .locals 0

    const/16 p0, 0x9

    if-eq p1, p0, :cond_c

    const/16 p0, 0xa

    if-eq p1, p0, :cond_b

    const/16 p0, 0xd

    if-eq p1, p0, :cond_a

    const/16 p0, 0x22

    if-eq p1, p0, :cond_8

    const/16 p0, 0x27

    if-eq p1, p0, :cond_6

    const/16 p0, 0x5c

    if-eq p1, p0, :cond_5

    const/16 p0, 0x20

    if-lt p1, p0, :cond_1

    const/16 p0, 0x7e

    if-le p1, p0, :cond_0

    goto :goto_0

    :cond_0
    int-to-char p0, p1

    invoke-static {p0}, Ljava/lang/String;->valueOf(C)Ljava/lang/String;

    move-result-object p0

    return-object p0

    :cond_1
    :goto_0
    const/16 p0, 0x10

    if-ltz p1, :cond_2

    const/16 p2, 0xf

    if-gt p1, p2, :cond_2

    const-string p2, "\\u000"

    invoke-static {p2}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p2

    invoke-static {p1, p0}, Ljava/lang/Integer;->toString(II)Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p2, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0

    :cond_2
    if-gt p0, p1, :cond_3

    const/16 p2, 0xff

    if-gt p1, p2, :cond_3

    const-string p2, "\\u00"

    invoke-static {p2}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p2

    invoke-static {p1, p0}, Ljava/lang/Integer;->toString(II)Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p2, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0

    :cond_3
    const/16 p2, 0x100

    if-gt p2, p1, :cond_4

    const/16 p2, 0xfff

    if-gt p1, p2, :cond_4

    const-string p2, "\\u0"

    invoke-static {p2}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p2

    invoke-static {p1, p0}, Ljava/lang/Integer;->toString(II)Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p2, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0

    :cond_4
    const-string p2, "\\u"

    invoke-static {p2}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p2

    invoke-static {p1, p0}, Ljava/lang/Integer;->toString(II)Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p2, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0

    :cond_5
    const-string p0, "\\\\"

    return-object p0

    :cond_6
    if-eqz p2, :cond_7

    const-string p0, "\\\'"

    goto :goto_1

    :cond_7
    const-string p0, "\'"

    :goto_1
    return-object p0

    :cond_8
    if-eqz p2, :cond_9

    const-string p0, "\""

    goto :goto_2

    :cond_9
    const-string p0, "\\\""

    :goto_2
    return-object p0

    :cond_a
    const-string p0, "\\r"

    return-object p0

    :cond_b
    const-string p0, "\\n"

    return-object p0

    :cond_c
    const-string p0, "\\t"

    return-object p0
.end method

.method public escapeChar(IZ)Ljava/lang/String;
    .locals 0

    invoke-virtual {p0, p1, p2}, Lantlr/PythonCharFormatter;->_escapeChar(IZ)Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public escapeString(Ljava/lang/String;)Ljava/lang/String;
    .locals 4

    new-instance v0, Ljava/lang/String;

    invoke-direct {v0}, Ljava/lang/String;-><init>()V

    const/4 v1, 0x0

    move-object v2, v0

    move v0, v1

    :goto_0
    invoke-virtual {p1}, Ljava/lang/String;->length()I

    move-result v3

    if-ge v0, v3, :cond_0

    invoke-static {v2}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v2

    invoke-virtual {p1, v0}, Ljava/lang/String;->charAt(I)C

    move-result v3

    invoke-virtual {p0, v3, v1}, Lantlr/PythonCharFormatter;->escapeChar(IZ)Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    add-int/lit8 v0, v0, 0x1

    goto :goto_0

    :cond_0
    return-object v2
.end method

.method public literalChar(I)Ljava/lang/String;
    .locals 3

    const-string v0, ""

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    const/4 v2, 0x1

    invoke-virtual {p0, p1, v2}, Lantlr/PythonCharFormatter;->escapeChar(IZ)Ljava/lang/String;

    move-result-object p0

    invoke-virtual {v1, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public literalString(Ljava/lang/String;)Ljava/lang/String;
    .locals 2

    const-string v0, "\""

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    invoke-virtual {p0, p1}, Lantlr/PythonCharFormatter;->escapeString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object p0

    invoke-virtual {v1, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method
