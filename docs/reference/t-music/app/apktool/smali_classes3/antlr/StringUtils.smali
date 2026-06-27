.class public Lantlr/StringUtils;
.super Ljava/lang/Object;
.source ""


# direct methods
.method public constructor <init>()V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method

.method public static stripBack(Ljava/lang/String;C)Ljava/lang/String;
    .locals 2

    :goto_0
    invoke-virtual {p0}, Ljava/lang/String;->length()I

    move-result v0

    if-lez v0, :cond_0

    invoke-virtual {p0}, Ljava/lang/String;->length()I

    move-result v0

    add-int/lit8 v0, v0, -0x1

    invoke-virtual {p0, v0}, Ljava/lang/String;->charAt(I)C

    move-result v0

    if-ne v0, p1, :cond_0

    invoke-virtual {p0}, Ljava/lang/String;->length()I

    move-result v0

    add-int/lit8 v0, v0, -0x1

    const/4 v1, 0x0

    invoke-virtual {p0, v1, v0}, Ljava/lang/String;->substring(II)Ljava/lang/String;

    move-result-object p0

    goto :goto_0

    :cond_0
    return-object p0
.end method

.method public static stripBack(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    .locals 6

    :goto_0
    const/4 v0, 0x0

    move-object v2, p0

    move p0, v0

    move v1, p0

    :goto_1
    invoke-virtual {p1}, Ljava/lang/String;->length()I

    move-result v3

    if-ge p0, v3, :cond_1

    invoke-virtual {p1, p0}, Ljava/lang/String;->charAt(I)C

    move-result v3

    const/4 v4, 0x1

    :goto_2
    invoke-virtual {v2}, Ljava/lang/String;->length()I

    move-result v5

    if-lez v5, :cond_0

    invoke-virtual {v2}, Ljava/lang/String;->length()I

    move-result v5

    sub-int/2addr v5, v4

    invoke-virtual {v2, v5}, Ljava/lang/String;->charAt(I)C

    move-result v5

    if-ne v5, v3, :cond_0

    invoke-virtual {v2}, Ljava/lang/String;->length()I

    move-result v1

    sub-int/2addr v1, v4

    invoke-virtual {v2, v0, v1}, Ljava/lang/String;->substring(II)Ljava/lang/String;

    move-result-object v2

    move v1, v4

    goto :goto_2

    :cond_0
    add-int/lit8 p0, p0, 0x1

    goto :goto_1

    :cond_1
    if-nez v1, :cond_2

    return-object v2

    :cond_2
    move-object p0, v2

    goto :goto_0
.end method

.method public static stripFront(Ljava/lang/String;C)Ljava/lang/String;
    .locals 1

    :goto_0
    invoke-virtual {p0}, Ljava/lang/String;->length()I

    move-result v0

    if-lez v0, :cond_0

    const/4 v0, 0x0

    invoke-virtual {p0, v0}, Ljava/lang/String;->charAt(I)C

    move-result v0

    if-ne v0, p1, :cond_0

    const/4 v0, 0x1

    invoke-virtual {p0, v0}, Ljava/lang/String;->substring(I)Ljava/lang/String;

    move-result-object p0

    goto :goto_0

    :cond_0
    return-object p0
.end method

.method public static stripFront(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    .locals 6

    :goto_0
    const/4 v0, 0x0

    move-object v2, p0

    move p0, v0

    move v1, p0

    :goto_1
    invoke-virtual {p1}, Ljava/lang/String;->length()I

    move-result v3

    if-ge p0, v3, :cond_1

    invoke-virtual {p1, p0}, Ljava/lang/String;->charAt(I)C

    move-result v3

    const/4 v4, 0x1

    :goto_2
    invoke-virtual {v2}, Ljava/lang/String;->length()I

    move-result v5

    if-lez v5, :cond_0

    invoke-virtual {v2, v0}, Ljava/lang/String;->charAt(I)C

    move-result v5

    if-ne v5, v3, :cond_0

    invoke-virtual {v2, v4}, Ljava/lang/String;->substring(I)Ljava/lang/String;

    move-result-object v2

    move v1, v4

    goto :goto_2

    :cond_0
    add-int/lit8 p0, p0, 0x1

    goto :goto_1

    :cond_1
    if-nez v1, :cond_2

    return-object v2

    :cond_2
    move-object p0, v2

    goto :goto_0
.end method

.method public static stripFrontBack(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    .locals 1

    invoke-virtual {p0, p1}, Ljava/lang/String;->indexOf(Ljava/lang/String;)I

    move-result p1

    invoke-virtual {p0, p2}, Ljava/lang/String;->lastIndexOf(Ljava/lang/String;)I

    move-result p2

    const/4 v0, -0x1

    if-eq p1, v0, :cond_1

    if-ne p2, v0, :cond_0

    goto :goto_0

    :cond_0
    add-int/lit8 p1, p1, 0x1

    invoke-virtual {p0, p1, p2}, Ljava/lang/String;->substring(II)Ljava/lang/String;

    move-result-object p0

    :cond_1
    :goto_0
    return-object p0
.end method
