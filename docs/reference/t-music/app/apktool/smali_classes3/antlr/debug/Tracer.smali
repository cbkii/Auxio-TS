.class public Lantlr/debug/Tracer;
.super Lantlr/debug/TraceAdapter;
.source ""

# interfaces
.implements Lantlr/debug/TraceListener;


# instance fields
.field public indent:Ljava/lang/String;


# direct methods
.method public constructor <init>()V
    .locals 1

    invoke-direct {p0}, Lantlr/debug/TraceAdapter;-><init>()V

    const-string v0, ""

    iput-object v0, p0, Lantlr/debug/Tracer;->indent:Ljava/lang/String;

    return-void
.end method


# virtual methods
.method public dedent()V
    .locals 2

    iget-object v0, p0, Lantlr/debug/Tracer;->indent:Ljava/lang/String;

    invoke-virtual {v0}, Ljava/lang/String;->length()I

    move-result v0

    const/4 v1, 0x2

    if-ge v0, v1, :cond_0

    const-string v0, ""

    goto :goto_0

    :cond_0
    iget-object v0, p0, Lantlr/debug/Tracer;->indent:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/String;->substring(I)Ljava/lang/String;

    move-result-object v0

    :goto_0
    iput-object v0, p0, Lantlr/debug/Tracer;->indent:Ljava/lang/String;

    return-void
.end method

.method public enterRule(Lantlr/debug/TraceEvent;)V
    .locals 3

    sget-object v0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v2, p0, Lantlr/debug/Tracer;->indent:Ljava/lang/String;

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {v0, p1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    invoke-virtual {p0}, Lantlr/debug/Tracer;->indent()V

    return-void
.end method

.method public exitRule(Lantlr/debug/TraceEvent;)V
    .locals 2

    invoke-virtual {p0}, Lantlr/debug/Tracer;->dedent()V

    sget-object v0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    iget-object p0, p0, Lantlr/debug/Tracer;->indent:Ljava/lang/String;

    invoke-virtual {v1, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {v0, p0}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    return-void
.end method

.method public indent()V
    .locals 3

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v1, p0, Lantlr/debug/Tracer;->indent:Ljava/lang/String;

    const-string v2, "  "

    invoke-static {v0, v1, v2}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    iput-object v0, p0, Lantlr/debug/Tracer;->indent:Ljava/lang/String;

    return-void
.end method
