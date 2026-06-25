.class public Lantlr/LLkParser;
.super Lantlr/Parser;
.source ""


# instance fields
.field public k:I


# direct methods
.method public constructor <init>(I)V
    .locals 0

    invoke-direct {p0}, Lantlr/Parser;-><init>()V

    iput p1, p0, Lantlr/LLkParser;->k:I

    return-void
.end method

.method public constructor <init>(Lantlr/ParserSharedInputState;I)V
    .locals 0

    invoke-direct {p0, p1}, Lantlr/Parser;-><init>(Lantlr/ParserSharedInputState;)V

    iput p2, p0, Lantlr/LLkParser;->k:I

    return-void
.end method

.method public constructor <init>(Lantlr/TokenBuffer;I)V
    .locals 0

    invoke-direct {p0}, Lantlr/Parser;-><init>()V

    iput p2, p0, Lantlr/LLkParser;->k:I

    invoke-virtual {p0, p1}, Lantlr/Parser;->setTokenBuffer(Lantlr/TokenBuffer;)V

    return-void
.end method

.method public constructor <init>(Lantlr/TokenStream;I)V
    .locals 0

    invoke-direct {p0}, Lantlr/Parser;-><init>()V

    iput p2, p0, Lantlr/LLkParser;->k:I

    new-instance p2, Lantlr/TokenBuffer;

    invoke-direct {p2, p1}, Lantlr/TokenBuffer;-><init>(Lantlr/TokenStream;)V

    invoke-virtual {p0, p2}, Lantlr/Parser;->setTokenBuffer(Lantlr/TokenBuffer;)V

    return-void
.end method

.method private trace(Ljava/lang/String;Ljava/lang/String;)V
    .locals 3

    invoke-virtual {p0}, Lantlr/Parser;->traceIndent()V

    sget-object v0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    invoke-static {p1, p2}, La/a/a/a/a;->b(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p1

    iget-object p2, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget p2, p2, Lantlr/ParserSharedInputState;->guessing:I

    if-lez p2, :cond_0

    const-string p2, "; [guessing]"

    goto :goto_0

    :cond_0
    const-string p2, "; "

    :goto_0
    invoke-virtual {p1, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {v0, p1}, Ljava/io/PrintStream;->print(Ljava/lang/String;)V

    const/4 p1, 0x1

    move p2, p1

    :goto_1
    iget v0, p0, Lantlr/LLkParser;->k:I

    if-gt p2, v0, :cond_3

    if-eq p2, p1, :cond_1

    sget-object v0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    const-string v1, ", "

    invoke-virtual {v0, v1}, Ljava/io/PrintStream;->print(Ljava/lang/String;)V

    :cond_1
    invoke-virtual {p0, p2}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v0

    const-string v1, "LA("

    if-eqz v0, :cond_2

    sget-object v0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v2, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, p2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v1, ")=="

    invoke-virtual {v2, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0, p2}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v1

    invoke-virtual {v1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v1

    goto :goto_2

    :cond_2
    sget-object v0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v2, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, p2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v1, ")==null"

    :goto_2
    invoke-virtual {v2, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/io/PrintStream;->print(Ljava/lang/String;)V

    add-int/lit8 p2, p2, 0x1

    goto :goto_1

    :cond_3
    sget-object p0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    const-string p1, ""

    invoke-virtual {p0, p1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    return-void
.end method


# virtual methods
.method public LA(I)I
    .locals 0

    iget-object p0, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget-object p0, p0, Lantlr/ParserSharedInputState;->input:Lantlr/TokenBuffer;

    invoke-virtual {p0, p1}, Lantlr/TokenBuffer;->LA(I)I

    move-result p0

    return p0
.end method

.method public LT(I)Lantlr/Token;
    .locals 0

    iget-object p0, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget-object p0, p0, Lantlr/ParserSharedInputState;->input:Lantlr/TokenBuffer;

    invoke-virtual {p0, p1}, Lantlr/TokenBuffer;->LT(I)Lantlr/Token;

    move-result-object p0

    return-object p0
.end method

.method public consume()V
    .locals 0

    iget-object p0, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget-object p0, p0, Lantlr/ParserSharedInputState;->input:Lantlr/TokenBuffer;

    invoke-virtual {p0}, Lantlr/TokenBuffer;->consume()V

    return-void
.end method

.method public traceIn(Ljava/lang/String;)V
    .locals 1

    iget v0, p0, Lantlr/Parser;->traceDepth:I

    add-int/lit8 v0, v0, 0x1

    iput v0, p0, Lantlr/Parser;->traceDepth:I

    const-string v0, "> "

    invoke-direct {p0, v0, p1}, Lantlr/LLkParser;->trace(Ljava/lang/String;Ljava/lang/String;)V

    return-void
.end method

.method public traceOut(Ljava/lang/String;)V
    .locals 1

    const-string v0, "< "

    invoke-direct {p0, v0, p1}, Lantlr/LLkParser;->trace(Ljava/lang/String;Ljava/lang/String;)V

    iget p1, p0, Lantlr/Parser;->traceDepth:I

    add-int/lit8 p1, p1, -0x1

    iput p1, p0, Lantlr/Parser;->traceDepth:I

    return-void
.end method
