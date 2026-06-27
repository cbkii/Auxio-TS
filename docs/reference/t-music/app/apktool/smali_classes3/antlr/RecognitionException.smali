.class public Lantlr/RecognitionException;
.super Lantlr/ANTLRException;
.source ""


# instance fields
.field public column:I

.field public fileName:Ljava/lang/String;

.field public line:I


# direct methods
.method public constructor <init>()V
    .locals 1

    const-string v0, "parsing error"

    invoke-direct {p0, v0}, Lantlr/ANTLRException;-><init>(Ljava/lang/String;)V

    const/4 v0, 0x0

    iput-object v0, p0, Lantlr/RecognitionException;->fileName:Ljava/lang/String;

    const/4 v0, -0x1

    iput v0, p0, Lantlr/RecognitionException;->line:I

    iput v0, p0, Lantlr/RecognitionException;->column:I

    return-void
.end method

.method public constructor <init>(Ljava/lang/String;)V
    .locals 0

    invoke-direct {p0, p1}, Lantlr/ANTLRException;-><init>(Ljava/lang/String;)V

    const/4 p1, 0x0

    iput-object p1, p0, Lantlr/RecognitionException;->fileName:Ljava/lang/String;

    const/4 p1, -0x1

    iput p1, p0, Lantlr/RecognitionException;->line:I

    iput p1, p0, Lantlr/RecognitionException;->column:I

    return-void
.end method

.method public constructor <init>(Ljava/lang/String;Ljava/lang/String;I)V
    .locals 1

    const/4 v0, -0x1

    invoke-direct {p0, p1, p2, p3, v0}, Lantlr/RecognitionException;-><init>(Ljava/lang/String;Ljava/lang/String;II)V

    return-void
.end method

.method public constructor <init>(Ljava/lang/String;Ljava/lang/String;II)V
    .locals 0

    invoke-direct {p0, p1}, Lantlr/ANTLRException;-><init>(Ljava/lang/String;)V

    iput-object p2, p0, Lantlr/RecognitionException;->fileName:Ljava/lang/String;

    iput p3, p0, Lantlr/RecognitionException;->line:I

    iput p4, p0, Lantlr/RecognitionException;->column:I

    return-void
.end method


# virtual methods
.method public getColumn()I
    .locals 0

    iget p0, p0, Lantlr/RecognitionException;->column:I

    return p0
.end method

.method public getErrorMessage()Ljava/lang/String;
    .locals 0

    invoke-virtual {p0}, Ljava/lang/Exception;->getMessage()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public getFilename()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lantlr/RecognitionException;->fileName:Ljava/lang/String;

    return-object p0
.end method

.method public getLine()I
    .locals 0

    iget p0, p0, Lantlr/RecognitionException;->line:I

    return p0
.end method

.method public toString()Ljava/lang/String;
    .locals 5

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    invoke-static {}, Lantlr/FileLineFormatter;->getFormatter()Lantlr/FileLineFormatter;

    move-result-object v1

    iget-object v2, p0, Lantlr/RecognitionException;->fileName:Ljava/lang/String;

    iget v3, p0, Lantlr/RecognitionException;->line:I

    iget v4, p0, Lantlr/RecognitionException;->column:I

    invoke-virtual {v1, v2, v3, v4}, Lantlr/FileLineFormatter;->getFormatString(Ljava/lang/String;II)Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0}, Ljava/lang/Exception;->getMessage()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method
