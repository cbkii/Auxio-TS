.class public Lantlr/Token;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Ljava/lang/Cloneable;


# static fields
.field public static final EOF_TYPE:I = 0x1

.field public static final INVALID_TYPE:I = 0x0

.field public static final MIN_USER_TYPE:I = 0x4

.field public static final NULL_TREE_LOOKAHEAD:I = 0x3

.field public static final SKIP:I = -0x1

.field public static badToken:Lantlr/Token;


# instance fields
.field public type:I


# direct methods
.method public static constructor <clinit>()V
    .locals 3

    new-instance v0, Lantlr/Token;

    const/4 v1, 0x0

    const-string v2, "<no text>"

    invoke-direct {v0, v1, v2}, Lantlr/Token;-><init>(ILjava/lang/String;)V

    sput-object v0, Lantlr/Token;->badToken:Lantlr/Token;

    return-void
.end method

.method public constructor <init>()V
    .locals 1

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    const/4 v0, 0x0

    iput v0, p0, Lantlr/Token;->type:I

    return-void
.end method

.method public constructor <init>(I)V
    .locals 1

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    const/4 v0, 0x0

    iput v0, p0, Lantlr/Token;->type:I

    iput p1, p0, Lantlr/Token;->type:I

    return-void
.end method

.method public constructor <init>(ILjava/lang/String;)V
    .locals 1

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    const/4 v0, 0x0

    iput v0, p0, Lantlr/Token;->type:I

    iput p1, p0, Lantlr/Token;->type:I

    invoke-virtual {p0, p2}, Lantlr/Token;->setText(Ljava/lang/String;)V

    return-void
.end method


# virtual methods
.method public getColumn()I
    .locals 0

    const/4 p0, 0x0

    return p0
.end method

.method public getFilename()Ljava/lang/String;
    .locals 0

    const/4 p0, 0x0

    return-object p0
.end method

.method public getLine()I
    .locals 0

    const/4 p0, 0x0

    return p0
.end method

.method public getText()Ljava/lang/String;
    .locals 0

    const-string p0, "<no text>"

    return-object p0
.end method

.method public getType()I
    .locals 0

    iget p0, p0, Lantlr/Token;->type:I

    return p0
.end method

.method public setColumn(I)V
    .locals 0

    return-void
.end method

.method public setFilename(Ljava/lang/String;)V
    .locals 0

    return-void
.end method

.method public setLine(I)V
    .locals 0

    return-void
.end method

.method public setText(Ljava/lang/String;)V
    .locals 0

    return-void
.end method

.method public setType(I)V
    .locals 0

    iput p1, p0, Lantlr/Token;->type:I

    return-void
.end method

.method public toString()Ljava/lang/String;
    .locals 2

    const-string v0, "[\""

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, "\",<"

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0}, Lantlr/Token;->getType()I

    move-result p0

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string p0, ">]"

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method
