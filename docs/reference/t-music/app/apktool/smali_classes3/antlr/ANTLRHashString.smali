.class public Lantlr/ANTLRHashString;
.super Ljava/lang/Object;
.source ""


# static fields
.field public static final prime:I = 0x97


# instance fields
.field public buf:[C

.field public len:I

.field public lexer:Lantlr/CharScanner;

.field public s:Ljava/lang/String;


# direct methods
.method public constructor <init>(Lantlr/CharScanner;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lantlr/ANTLRHashString;->lexer:Lantlr/CharScanner;

    return-void
.end method

.method public constructor <init>(Ljava/lang/String;Lantlr/CharScanner;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p2, p0, Lantlr/ANTLRHashString;->lexer:Lantlr/CharScanner;

    invoke-virtual {p0, p1}, Lantlr/ANTLRHashString;->setString(Ljava/lang/String;)V

    return-void
.end method

.method public constructor <init>([CILantlr/CharScanner;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p3, p0, Lantlr/ANTLRHashString;->lexer:Lantlr/CharScanner;

    invoke-virtual {p0, p1, p2}, Lantlr/ANTLRHashString;->setBuffer([CI)V

    return-void
.end method

.method private final charAt(I)C
    .locals 1

    iget-object v0, p0, Lantlr/ANTLRHashString;->s:Ljava/lang/String;

    if-eqz v0, :cond_0

    invoke-virtual {v0, p1}, Ljava/lang/String;->charAt(I)C

    move-result p0

    goto :goto_0

    :cond_0
    iget-object p0, p0, Lantlr/ANTLRHashString;->buf:[C

    aget-char p0, p0, p1

    :goto_0
    return p0
.end method

.method private final length()I
    .locals 1

    iget-object v0, p0, Lantlr/ANTLRHashString;->s:Ljava/lang/String;

    if-eqz v0, :cond_0

    invoke-virtual {v0}, Ljava/lang/String;->length()I

    move-result p0

    goto :goto_0

    :cond_0
    iget p0, p0, Lantlr/ANTLRHashString;->len:I

    :goto_0
    return p0
.end method


# virtual methods
.method public equals(Ljava/lang/Object;)Z
    .locals 6

    instance-of v0, p1, Lantlr/ANTLRHashString;

    const/4 v1, 0x0

    if-nez v0, :cond_0

    instance-of v0, p1, Ljava/lang/String;

    if-nez v0, :cond_0

    return v1

    :cond_0
    instance-of v0, p1, Ljava/lang/String;

    if-eqz v0, :cond_1

    new-instance v0, Lantlr/ANTLRHashString;

    check-cast p1, Ljava/lang/String;

    iget-object v2, p0, Lantlr/ANTLRHashString;->lexer:Lantlr/CharScanner;

    invoke-direct {v0, p1, v2}, Lantlr/ANTLRHashString;-><init>(Ljava/lang/String;Lantlr/CharScanner;)V

    goto :goto_0

    :cond_1
    move-object v0, p1

    check-cast v0, Lantlr/ANTLRHashString;

    :goto_0
    invoke-direct {p0}, Lantlr/ANTLRHashString;->length()I

    move-result p1

    invoke-direct {v0}, Lantlr/ANTLRHashString;->length()I

    move-result v2

    if-eq v2, p1, :cond_2

    return v1

    :cond_2
    iget-object v2, p0, Lantlr/ANTLRHashString;->lexer:Lantlr/CharScanner;

    invoke-virtual {v2}, Lantlr/CharScanner;->getCaseSensitiveLiterals()Z

    move-result v2

    if-eqz v2, :cond_4

    move v2, v1

    :goto_1
    if-ge v2, p1, :cond_6

    invoke-direct {p0, v2}, Lantlr/ANTLRHashString;->charAt(I)C

    move-result v3

    invoke-direct {v0, v2}, Lantlr/ANTLRHashString;->charAt(I)C

    move-result v4

    if-eq v3, v4, :cond_3

    return v1

    :cond_3
    add-int/lit8 v2, v2, 0x1

    goto :goto_1

    :cond_4
    move v2, v1

    :goto_2
    if-ge v2, p1, :cond_6

    iget-object v3, p0, Lantlr/ANTLRHashString;->lexer:Lantlr/CharScanner;

    invoke-direct {p0, v2}, Lantlr/ANTLRHashString;->charAt(I)C

    move-result v4

    invoke-virtual {v3, v4}, Lantlr/CharScanner;->toLower(C)C

    move-result v3

    iget-object v4, p0, Lantlr/ANTLRHashString;->lexer:Lantlr/CharScanner;

    invoke-direct {v0, v2}, Lantlr/ANTLRHashString;->charAt(I)C

    move-result v5

    invoke-virtual {v4, v5}, Lantlr/CharScanner;->toLower(C)C

    move-result v4

    if-eq v3, v4, :cond_5

    return v1

    :cond_5
    add-int/lit8 v2, v2, 0x1

    goto :goto_2

    :cond_6
    const/4 p0, 0x1

    return p0
.end method

.method public hashCode()I
    .locals 5

    invoke-direct {p0}, Lantlr/ANTLRHashString;->length()I

    move-result v0

    iget-object v1, p0, Lantlr/ANTLRHashString;->lexer:Lantlr/CharScanner;

    invoke-virtual {v1}, Lantlr/CharScanner;->getCaseSensitiveLiterals()Z

    move-result v1

    const/4 v2, 0x0

    if-eqz v1, :cond_0

    move v1, v2

    :goto_0
    if-ge v2, v0, :cond_1

    mul-int/lit16 v1, v1, 0x97

    invoke-direct {p0, v2}, Lantlr/ANTLRHashString;->charAt(I)C

    move-result v3

    add-int/2addr v1, v3

    add-int/lit8 v2, v2, 0x1

    goto :goto_0

    :cond_0
    move v1, v2

    :goto_1
    if-ge v2, v0, :cond_1

    mul-int/lit16 v1, v1, 0x97

    iget-object v3, p0, Lantlr/ANTLRHashString;->lexer:Lantlr/CharScanner;

    invoke-direct {p0, v2}, Lantlr/ANTLRHashString;->charAt(I)C

    move-result v4

    invoke-virtual {v3, v4}, Lantlr/CharScanner;->toLower(C)C

    move-result v3

    add-int/2addr v1, v3

    add-int/lit8 v2, v2, 0x1

    goto :goto_1

    :cond_1
    return v1
.end method

.method public setBuffer([CI)V
    .locals 0

    iput-object p1, p0, Lantlr/ANTLRHashString;->buf:[C

    iput p2, p0, Lantlr/ANTLRHashString;->len:I

    const/4 p1, 0x0

    iput-object p1, p0, Lantlr/ANTLRHashString;->s:Ljava/lang/String;

    return-void
.end method

.method public setString(Ljava/lang/String;)V
    .locals 0

    iput-object p1, p0, Lantlr/ANTLRHashString;->s:Ljava/lang/String;

    const/4 p1, 0x0

    iput-object p1, p0, Lantlr/ANTLRHashString;->buf:[C

    return-void
.end method
