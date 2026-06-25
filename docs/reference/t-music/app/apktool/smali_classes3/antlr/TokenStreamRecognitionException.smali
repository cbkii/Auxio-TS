.class public Lantlr/TokenStreamRecognitionException;
.super Lantlr/TokenStreamException;
.source ""


# instance fields
.field public recog:Lantlr/RecognitionException;


# direct methods
.method public constructor <init>(Lantlr/RecognitionException;)V
    .locals 1

    invoke-virtual {p1}, Ljava/lang/Exception;->getMessage()Ljava/lang/String;

    move-result-object v0

    invoke-direct {p0, v0}, Lantlr/TokenStreamException;-><init>(Ljava/lang/String;)V

    iput-object p1, p0, Lantlr/TokenStreamRecognitionException;->recog:Lantlr/RecognitionException;

    return-void
.end method


# virtual methods
.method public toString()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lantlr/TokenStreamRecognitionException;->recog:Lantlr/RecognitionException;

    invoke-virtual {p0}, Lantlr/RecognitionException;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method
