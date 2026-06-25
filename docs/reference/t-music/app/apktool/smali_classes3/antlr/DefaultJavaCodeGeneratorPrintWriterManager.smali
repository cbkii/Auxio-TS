.class public Lantlr/DefaultJavaCodeGeneratorPrintWriterManager;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Lantlr/JavaCodeGeneratorPrintWriterManager;


# instance fields
.field public currentFileName:Ljava/lang/String;

.field public currentOutput:Ljava/io/PrintWriter;

.field public grammar:Lantlr/Grammar;

.field public smapOutput:Lantlr/PrintWriterWithSMAP;

.field public sourceMaps:Ljava/util/Map;

.field public tool:Lantlr/Tool;


# direct methods
.method public constructor <init>()V
    .locals 1

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    new-instance v0, Ljava/util/HashMap;

    invoke-direct {v0}, Ljava/util/HashMap;-><init>()V

    iput-object v0, p0, Lantlr/DefaultJavaCodeGeneratorPrintWriterManager;->sourceMaps:Ljava/util/Map;

    return-void
.end method


# virtual methods
.method public endMapping()V
    .locals 0

    iget-object p0, p0, Lantlr/DefaultJavaCodeGeneratorPrintWriterManager;->smapOutput:Lantlr/PrintWriterWithSMAP;

    invoke-virtual {p0}, Lantlr/PrintWriterWithSMAP;->endMapping()V

    return-void
.end method

.method public finishOutput()V
    .locals 4

    iget-object v0, p0, Lantlr/DefaultJavaCodeGeneratorPrintWriterManager;->currentOutput:Ljava/io/PrintWriter;

    invoke-virtual {v0}, Ljava/io/PrintWriter;->close()V

    iget-object v0, p0, Lantlr/DefaultJavaCodeGeneratorPrintWriterManager;->grammar:Lantlr/Grammar;

    if-eqz v0, :cond_1

    iget-object v0, p0, Lantlr/DefaultJavaCodeGeneratorPrintWriterManager;->tool:Lantlr/Tool;

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v2, p0, Lantlr/DefaultJavaCodeGeneratorPrintWriterManager;->grammar:Lantlr/Grammar;

    invoke-virtual {v2}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, ".smap"

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Lantlr/Tool;->openOutputFile(Ljava/lang/String;)Ljava/io/PrintWriter;

    move-result-object v0

    iget-object v1, p0, Lantlr/DefaultJavaCodeGeneratorPrintWriterManager;->grammar:Lantlr/Grammar;

    invoke-virtual {v1}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object v1

    const/16 v2, 0x5c

    const/16 v3, 0x2f

    invoke-virtual {v1, v2, v3}, Ljava/lang/String;->replace(CC)Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v1, v3}, Ljava/lang/String;->lastIndexOf(I)I

    move-result v2

    const/4 v3, -0x1

    if-eq v2, v3, :cond_0

    add-int/lit8 v2, v2, 0x1

    invoke-virtual {v1, v2}, Ljava/lang/String;->substring(I)Ljava/lang/String;

    move-result-object v1

    :cond_0
    iget-object v2, p0, Lantlr/DefaultJavaCodeGeneratorPrintWriterManager;->smapOutput:Lantlr/PrintWriterWithSMAP;

    iget-object v3, p0, Lantlr/DefaultJavaCodeGeneratorPrintWriterManager;->grammar:Lantlr/Grammar;

    invoke-virtual {v3}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v2, v0, v3, v1}, Lantlr/PrintWriterWithSMAP;->dump(Ljava/io/PrintWriter;Ljava/lang/String;Ljava/lang/String;)V

    iget-object v0, p0, Lantlr/DefaultJavaCodeGeneratorPrintWriterManager;->sourceMaps:Ljava/util/Map;

    iget-object v1, p0, Lantlr/DefaultJavaCodeGeneratorPrintWriterManager;->currentFileName:Ljava/lang/String;

    iget-object v2, p0, Lantlr/DefaultJavaCodeGeneratorPrintWriterManager;->smapOutput:Lantlr/PrintWriterWithSMAP;

    invoke-virtual {v2}, Lantlr/PrintWriterWithSMAP;->getSourceMap()Ljava/util/Map;

    move-result-object v2

    invoke-interface {v0, v1, v2}, Ljava/util/Map;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    :cond_1
    const/4 v0, 0x0

    iput-object v0, p0, Lantlr/DefaultJavaCodeGeneratorPrintWriterManager;->currentOutput:Ljava/io/PrintWriter;

    return-void
.end method

.method public getCurrentOutputLine()I
    .locals 0

    iget-object p0, p0, Lantlr/DefaultJavaCodeGeneratorPrintWriterManager;->smapOutput:Lantlr/PrintWriterWithSMAP;

    invoke-virtual {p0}, Lantlr/PrintWriterWithSMAP;->getCurrentOutputLine()I

    move-result p0

    return p0
.end method

.method public getSourceMaps()Ljava/util/Map;
    .locals 0

    iget-object p0, p0, Lantlr/DefaultJavaCodeGeneratorPrintWriterManager;->sourceMaps:Ljava/util/Map;

    return-object p0
.end method

.method public setupOutput(Lantlr/Tool;Lantlr/Grammar;)Ljava/io/PrintWriter;
    .locals 1

    const/4 v0, 0x0

    invoke-virtual {p0, p1, p2, v0}, Lantlr/DefaultJavaCodeGeneratorPrintWriterManager;->setupOutput(Lantlr/Tool;Lantlr/Grammar;Ljava/lang/String;)Ljava/io/PrintWriter;

    move-result-object p0

    return-object p0
.end method

.method public setupOutput(Lantlr/Tool;Lantlr/Grammar;Ljava/lang/String;)Ljava/io/PrintWriter;
    .locals 2

    iput-object p1, p0, Lantlr/DefaultJavaCodeGeneratorPrintWriterManager;->tool:Lantlr/Tool;

    iput-object p2, p0, Lantlr/DefaultJavaCodeGeneratorPrintWriterManager;->grammar:Lantlr/Grammar;

    if-nez p3, :cond_0

    invoke-virtual {p2}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object p3

    :cond_0
    new-instance p2, Lantlr/PrintWriterWithSMAP;

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v0, p3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, ".java"

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p1, v0}, Lantlr/Tool;->openOutputFile(Ljava/lang/String;)Ljava/io/PrintWriter;

    move-result-object p1

    invoke-direct {p2, p1}, Lantlr/PrintWriterWithSMAP;-><init>(Ljava/io/Writer;)V

    iput-object p2, p0, Lantlr/DefaultJavaCodeGeneratorPrintWriterManager;->smapOutput:Lantlr/PrintWriterWithSMAP;

    invoke-static {p3, v1}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p1

    iput-object p1, p0, Lantlr/DefaultJavaCodeGeneratorPrintWriterManager;->currentFileName:Ljava/lang/String;

    iget-object p1, p0, Lantlr/DefaultJavaCodeGeneratorPrintWriterManager;->smapOutput:Lantlr/PrintWriterWithSMAP;

    iput-object p1, p0, Lantlr/DefaultJavaCodeGeneratorPrintWriterManager;->currentOutput:Ljava/io/PrintWriter;

    iget-object p0, p0, Lantlr/DefaultJavaCodeGeneratorPrintWriterManager;->currentOutput:Ljava/io/PrintWriter;

    return-object p0
.end method

.method public setupOutput(Lantlr/Tool;Ljava/lang/String;)Ljava/io/PrintWriter;
    .locals 1

    const/4 v0, 0x0

    invoke-virtual {p0, p1, v0, p2}, Lantlr/DefaultJavaCodeGeneratorPrintWriterManager;->setupOutput(Lantlr/Tool;Lantlr/Grammar;Ljava/lang/String;)Ljava/io/PrintWriter;

    move-result-object p0

    return-object p0
.end method

.method public startMapping(I)V
    .locals 0

    iget-object p0, p0, Lantlr/DefaultJavaCodeGeneratorPrintWriterManager;->smapOutput:Lantlr/PrintWriterWithSMAP;

    invoke-virtual {p0, p1}, Lantlr/PrintWriterWithSMAP;->startMapping(I)V

    return-void
.end method

.method public startSingleSourceLineMapping(I)V
    .locals 0

    iget-object p0, p0, Lantlr/DefaultJavaCodeGeneratorPrintWriterManager;->smapOutput:Lantlr/PrintWriterWithSMAP;

    invoke-virtual {p0, p1}, Lantlr/PrintWriterWithSMAP;->startSingleSourceLineMapping(I)V

    return-void
.end method
