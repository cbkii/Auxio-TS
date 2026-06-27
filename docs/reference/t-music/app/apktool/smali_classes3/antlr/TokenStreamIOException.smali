.class public Lantlr/TokenStreamIOException;
.super Lantlr/TokenStreamException;
.source ""


# instance fields
.field public io:Ljava/io/IOException;


# direct methods
.method public constructor <init>(Ljava/io/IOException;)V
    .locals 1

    invoke-virtual {p1}, Ljava/io/IOException;->getMessage()Ljava/lang/String;

    move-result-object v0

    invoke-direct {p0, v0}, Lantlr/TokenStreamException;-><init>(Ljava/lang/String;)V

    iput-object p1, p0, Lantlr/TokenStreamIOException;->io:Ljava/io/IOException;

    return-void
.end method
