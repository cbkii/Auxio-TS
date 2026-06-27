.class public Lantlr/CppCharFormatter;
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
.method public escapeChar(IZ)Ljava/lang/String;
    .locals 0

    const/16 p0, 0x9

    if-eq p1, p0, :cond_9

    const/16 p0, 0xa

    if-eq p1, p0, :cond_8

    const/16 p0, 0xd

    if-eq p1, p0, :cond_7

    const/16 p0, 0x22

    if-eq p1, p0, :cond_6

    const/16 p0, 0x27

    if-eq p1, p0, :cond_5

    const/16 p0, 0x5c

    if-eq p1, p0, :cond_4

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
    const/16 p0, 0xff

    if-le p1, p0, :cond_3

    const/16 p0, 0x10

    invoke-static {p1, p0}, Ljava/lang/Integer;->toString(II)Ljava/lang/String;

    move-result-object p0

    :goto_1
    invoke-virtual {p0}, Ljava/lang/String;->length()I

    move-result p1

    const/4 p2, 0x4

    if-ge p1, p2, :cond_2

    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    const/16 p2, 0x30

    invoke-virtual {p1, p2}, Ljava/lang/StringBuilder;->append(C)Ljava/lang/StringBuilder;

    invoke-virtual {p1, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    goto :goto_1

    :cond_2
    const-string p1, "\\u"

    invoke-static {p1, p0}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p0

    return-object p0

    :cond_3
    const-string p0, "\\"

    invoke-static {p0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p0

    const/16 p2, 0x8

    invoke-static {p1, p2}, Ljava/lang/Integer;->toString(II)Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0

    :cond_4
    const-string p0, "\\\\"

    return-object p0

    :cond_5
    const-string p0, "\\\'"

    return-object p0

    :cond_6
    const-string p0, "\\\""

    return-object p0

    :cond_7
    const-string p0, "\\r"

    return-object p0

    :cond_8
    const-string p0, "\\n"

    return-object p0

    :cond_9
    const-string p0, "\\t"

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

    invoke-virtual {p0, v3, v1}, Lantlr/CppCharFormatter;->escapeChar(IZ)Ljava/lang/String;

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
    .locals 2

    const-string v0, "0x"

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    const/16 v1, 0x10

    invoke-static {p1, v1}, Ljava/lang/Integer;->toString(II)Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    if-ltz p1, :cond_0

    const/16 v1, 0x7e

    if-gt p1, v1, :cond_0

    const-string v1, " /* \'"

    invoke-static {v0, v1}, La/a/a/a/a;->b(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    const/4 v1, 0x1

    invoke-virtual {p0, p1, v1}, Lantlr/CppCharFormatter;->escapeChar(IZ)Ljava/lang/String;

    move-result-object p0

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p0, "\' */ "

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    :cond_0
    return-object v0
.end method

.method public literalString(Ljava/lang/String;)Ljava/lang/String;
    .locals 2

    const-string v0, "\""

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    invoke-virtual {p0, p1}, Lantlr/CppCharFormatter;->escapeString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object p0

    invoke-virtual {v1, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method
