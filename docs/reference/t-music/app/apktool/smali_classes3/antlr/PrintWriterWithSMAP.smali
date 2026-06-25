.class public Lantlr/PrintWriterWithSMAP;
.super Ljava/io/PrintWriter;
.source ""


# instance fields
.field public anythingWrittenSinceMapping:Z

.field public currentOutputLine:I

.field public currentSourceLine:I

.field public lastPrintCharacterWasCR:Z

.field public mapLines:Z

.field public mapSingleSourceLine:Z

.field public sourceMap:Ljava/util/Map;


# direct methods
.method public constructor <init>(Ljava/io/OutputStream;)V
    .locals 1

    invoke-direct {p0, p1}, Ljava/io/PrintWriter;-><init>(Ljava/io/OutputStream;)V

    const/4 p1, 0x1

    iput p1, p0, Lantlr/PrintWriterWithSMAP;->currentOutputLine:I

    const/4 p1, 0x0

    iput p1, p0, Lantlr/PrintWriterWithSMAP;->currentSourceLine:I

    new-instance v0, Ljava/util/HashMap;

    invoke-direct {v0}, Ljava/util/HashMap;-><init>()V

    iput-object v0, p0, Lantlr/PrintWriterWithSMAP;->sourceMap:Ljava/util/Map;

    iput-boolean p1, p0, Lantlr/PrintWriterWithSMAP;->lastPrintCharacterWasCR:Z

    iput-boolean p1, p0, Lantlr/PrintWriterWithSMAP;->mapLines:Z

    iput-boolean p1, p0, Lantlr/PrintWriterWithSMAP;->mapSingleSourceLine:Z

    iput-boolean p1, p0, Lantlr/PrintWriterWithSMAP;->anythingWrittenSinceMapping:Z

    return-void
.end method

.method public constructor <init>(Ljava/io/OutputStream;Z)V
    .locals 0

    invoke-direct {p0, p1, p2}, Ljava/io/PrintWriter;-><init>(Ljava/io/OutputStream;Z)V

    const/4 p1, 0x1

    iput p1, p0, Lantlr/PrintWriterWithSMAP;->currentOutputLine:I

    const/4 p1, 0x0

    iput p1, p0, Lantlr/PrintWriterWithSMAP;->currentSourceLine:I

    new-instance p2, Ljava/util/HashMap;

    invoke-direct {p2}, Ljava/util/HashMap;-><init>()V

    iput-object p2, p0, Lantlr/PrintWriterWithSMAP;->sourceMap:Ljava/util/Map;

    iput-boolean p1, p0, Lantlr/PrintWriterWithSMAP;->lastPrintCharacterWasCR:Z

    iput-boolean p1, p0, Lantlr/PrintWriterWithSMAP;->mapLines:Z

    iput-boolean p1, p0, Lantlr/PrintWriterWithSMAP;->mapSingleSourceLine:Z

    iput-boolean p1, p0, Lantlr/PrintWriterWithSMAP;->anythingWrittenSinceMapping:Z

    return-void
.end method

.method public constructor <init>(Ljava/io/Writer;)V
    .locals 1

    invoke-direct {p0, p1}, Ljava/io/PrintWriter;-><init>(Ljava/io/Writer;)V

    const/4 p1, 0x1

    iput p1, p0, Lantlr/PrintWriterWithSMAP;->currentOutputLine:I

    const/4 p1, 0x0

    iput p1, p0, Lantlr/PrintWriterWithSMAP;->currentSourceLine:I

    new-instance v0, Ljava/util/HashMap;

    invoke-direct {v0}, Ljava/util/HashMap;-><init>()V

    iput-object v0, p0, Lantlr/PrintWriterWithSMAP;->sourceMap:Ljava/util/Map;

    iput-boolean p1, p0, Lantlr/PrintWriterWithSMAP;->lastPrintCharacterWasCR:Z

    iput-boolean p1, p0, Lantlr/PrintWriterWithSMAP;->mapLines:Z

    iput-boolean p1, p0, Lantlr/PrintWriterWithSMAP;->mapSingleSourceLine:Z

    iput-boolean p1, p0, Lantlr/PrintWriterWithSMAP;->anythingWrittenSinceMapping:Z

    return-void
.end method

.method public constructor <init>(Ljava/io/Writer;Z)V
    .locals 0

    invoke-direct {p0, p1, p2}, Ljava/io/PrintWriter;-><init>(Ljava/io/Writer;Z)V

    const/4 p1, 0x1

    iput p1, p0, Lantlr/PrintWriterWithSMAP;->currentOutputLine:I

    const/4 p1, 0x0

    iput p1, p0, Lantlr/PrintWriterWithSMAP;->currentSourceLine:I

    new-instance p2, Ljava/util/HashMap;

    invoke-direct {p2}, Ljava/util/HashMap;-><init>()V

    iput-object p2, p0, Lantlr/PrintWriterWithSMAP;->sourceMap:Ljava/util/Map;

    iput-boolean p1, p0, Lantlr/PrintWriterWithSMAP;->lastPrintCharacterWasCR:Z

    iput-boolean p1, p0, Lantlr/PrintWriterWithSMAP;->mapLines:Z

    iput-boolean p1, p0, Lantlr/PrintWriterWithSMAP;->mapSingleSourceLine:Z

    iput-boolean p1, p0, Lantlr/PrintWriterWithSMAP;->anythingWrittenSinceMapping:Z

    return-void
.end method


# virtual methods
.method public checkChar(I)V
    .locals 3

    iget-boolean v0, p0, Lantlr/PrintWriterWithSMAP;->lastPrintCharacterWasCR:Z

    const/16 v1, 0xa

    const/4 v2, 0x1

    if-eqz v0, :cond_0

    if-eq p1, v1, :cond_0

    goto :goto_0

    :cond_0
    if-ne p1, v1, :cond_1

    :goto_0
    invoke-virtual {p0, v2}, Lantlr/PrintWriterWithSMAP;->mapLine(Z)V

    goto :goto_1

    :cond_1
    int-to-char v0, p1

    invoke-static {v0}, Ljava/lang/Character;->isWhitespace(C)Z

    move-result v0

    if-nez v0, :cond_2

    iput-boolean v2, p0, Lantlr/PrintWriterWithSMAP;->anythingWrittenSinceMapping:Z

    :cond_2
    :goto_1
    const/16 v0, 0xd

    if-ne p1, v0, :cond_3

    goto :goto_2

    :cond_3
    const/4 v2, 0x0

    :goto_2
    iput-boolean v2, p0, Lantlr/PrintWriterWithSMAP;->lastPrintCharacterWasCR:Z

    return-void
.end method

.method public dump(Ljava/io/PrintWriter;Ljava/lang/String;Ljava/lang/String;)V
    .locals 4

    const-string v0, "SMAP"

    invoke-virtual {p1, v0}, Ljava/io/PrintWriter;->println(Ljava/lang/String;)V

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v0, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p2, ".java"

    invoke-virtual {v0, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p2

    invoke-virtual {p1, p2}, Ljava/io/PrintWriter;->println(Ljava/lang/String;)V

    const-string p2, "G"

    invoke-virtual {p1, p2}, Ljava/io/PrintWriter;->println(Ljava/lang/String;)V

    const-string p2, "*S G"

    invoke-virtual {p1, p2}, Ljava/io/PrintWriter;->println(Ljava/lang/String;)V

    const-string p2, "*F"

    invoke-virtual {p1, p2}, Ljava/io/PrintWriter;->println(Ljava/lang/String;)V

    new-instance p2, Ljava/lang/StringBuilder;

    invoke-direct {p2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v0, "+ 0 "

    invoke-virtual {p2, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p2, p3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p2

    invoke-virtual {p1, p2}, Ljava/io/PrintWriter;->println(Ljava/lang/String;)V

    invoke-virtual {p1, p3}, Ljava/io/PrintWriter;->println(Ljava/lang/String;)V

    const-string p2, "*L"

    invoke-virtual {p1, p2}, Ljava/io/PrintWriter;->println(Ljava/lang/String;)V

    new-instance p2, Ljava/util/ArrayList;

    iget-object p3, p0, Lantlr/PrintWriterWithSMAP;->sourceMap:Ljava/util/Map;

    invoke-interface {p3}, Ljava/util/Map;->keySet()Ljava/util/Set;

    move-result-object p3

    invoke-direct {p2, p3}, Ljava/util/ArrayList;-><init>(Ljava/util/Collection;)V

    invoke-static {p2}, Ljava/util/Collections;->sort(Ljava/util/List;)V

    invoke-interface {p2}, Ljava/util/List;->iterator()Ljava/util/Iterator;

    move-result-object p2

    :cond_0
    invoke-interface {p2}, Ljava/util/Iterator;->hasNext()Z

    move-result p3

    if-eqz p3, :cond_1

    invoke-interface {p2}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object p3

    check-cast p3, Ljava/lang/Integer;

    iget-object v0, p0, Lantlr/PrintWriterWithSMAP;->sourceMap:Ljava/util/Map;

    invoke-interface {v0, p3}, Ljava/util/Map;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Ljava/util/List;

    invoke-interface {v0}, Ljava/util/List;->iterator()Ljava/util/Iterator;

    move-result-object v0

    :goto_0
    invoke-interface {v0}, Ljava/util/Iterator;->hasNext()Z

    move-result v1

    if-eqz v1, :cond_0

    invoke-interface {v0}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Ljava/lang/Integer;

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v2, p3}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    const-string v3, ":"

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p1, v1}, Ljava/io/PrintWriter;->println(Ljava/lang/String;)V

    goto :goto_0

    :cond_1
    const-string p0, "*E"

    invoke-virtual {p1, p0}, Ljava/io/PrintWriter;->println(Ljava/lang/String;)V

    invoke-virtual {p1}, Ljava/io/PrintWriter;->close()V

    return-void
.end method

.method public endMapping()V
    .locals 1

    const/4 v0, 0x0

    invoke-virtual {p0, v0}, Lantlr/PrintWriterWithSMAP;->mapLine(Z)V

    iput-boolean v0, p0, Lantlr/PrintWriterWithSMAP;->mapLines:Z

    iput-boolean v0, p0, Lantlr/PrintWriterWithSMAP;->mapSingleSourceLine:Z

    return-void
.end method

.method public getCurrentOutputLine()I
    .locals 0

    iget p0, p0, Lantlr/PrintWriterWithSMAP;->currentOutputLine:I

    return p0
.end method

.method public getSourceMap()Ljava/util/Map;
    .locals 0

    iget-object p0, p0, Lantlr/PrintWriterWithSMAP;->sourceMap:Ljava/util/Map;

    return-object p0
.end method

.method public mapLine(Z)V
    .locals 4

    iget-boolean v0, p0, Lantlr/PrintWriterWithSMAP;->mapLines:Z

    if-eqz v0, :cond_1

    iget-boolean v0, p0, Lantlr/PrintWriterWithSMAP;->anythingWrittenSinceMapping:Z

    if-eqz v0, :cond_1

    new-instance v0, Ljava/lang/Integer;

    iget v1, p0, Lantlr/PrintWriterWithSMAP;->currentSourceLine:I

    invoke-direct {v0, v1}, Ljava/lang/Integer;-><init>(I)V

    new-instance v1, Ljava/lang/Integer;

    iget v2, p0, Lantlr/PrintWriterWithSMAP;->currentOutputLine:I

    invoke-direct {v1, v2}, Ljava/lang/Integer;-><init>(I)V

    iget-object v2, p0, Lantlr/PrintWriterWithSMAP;->sourceMap:Ljava/util/Map;

    invoke-interface {v2, v0}, Ljava/util/Map;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Ljava/util/List;

    if-nez v2, :cond_0

    new-instance v2, Ljava/util/ArrayList;

    invoke-direct {v2}, Ljava/util/ArrayList;-><init>()V

    iget-object v3, p0, Lantlr/PrintWriterWithSMAP;->sourceMap:Ljava/util/Map;

    invoke-interface {v3, v0, v2}, Ljava/util/Map;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    :cond_0
    invoke-interface {v2, v1}, Ljava/util/List;->contains(Ljava/lang/Object;)Z

    move-result v0

    if-nez v0, :cond_1

    invoke-interface {v2, v1}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    :cond_1
    if-eqz p1, :cond_2

    iget p1, p0, Lantlr/PrintWriterWithSMAP;->currentOutputLine:I

    add-int/lit8 p1, p1, 0x1

    iput p1, p0, Lantlr/PrintWriterWithSMAP;->currentOutputLine:I

    :cond_2
    iget-boolean p1, p0, Lantlr/PrintWriterWithSMAP;->mapSingleSourceLine:Z

    if-nez p1, :cond_3

    iget p1, p0, Lantlr/PrintWriterWithSMAP;->currentSourceLine:I

    add-int/lit8 p1, p1, 0x1

    iput p1, p0, Lantlr/PrintWriterWithSMAP;->currentSourceLine:I

    :cond_3
    const/4 p1, 0x0

    iput-boolean p1, p0, Lantlr/PrintWriterWithSMAP;->anythingWrittenSinceMapping:Z

    return-void
.end method

.method public println()V
    .locals 1

    const/4 v0, 0x1

    invoke-virtual {p0, v0}, Lantlr/PrintWriterWithSMAP;->mapLine(Z)V

    invoke-super {p0}, Ljava/io/PrintWriter;->println()V

    const/4 v0, 0x0

    iput-boolean v0, p0, Lantlr/PrintWriterWithSMAP;->lastPrintCharacterWasCR:Z

    return-void
.end method

.method public startMapping(I)V
    .locals 1

    const/4 v0, 0x1

    iput-boolean v0, p0, Lantlr/PrintWriterWithSMAP;->mapLines:Z

    const/16 v0, -0x378

    if-eq p1, v0, :cond_0

    iput p1, p0, Lantlr/PrintWriterWithSMAP;->currentSourceLine:I

    :cond_0
    return-void
.end method

.method public startSingleSourceLineMapping(I)V
    .locals 1

    const/4 v0, 0x1

    iput-boolean v0, p0, Lantlr/PrintWriterWithSMAP;->mapSingleSourceLine:Z

    iput-boolean v0, p0, Lantlr/PrintWriterWithSMAP;->mapLines:Z

    const/16 v0, -0x378

    if-eq p1, v0, :cond_0

    iput p1, p0, Lantlr/PrintWriterWithSMAP;->currentSourceLine:I

    :cond_0
    return-void
.end method

.method public write(I)V
    .locals 0

    invoke-virtual {p0, p1}, Lantlr/PrintWriterWithSMAP;->checkChar(I)V

    invoke-super {p0, p1}, Ljava/io/PrintWriter;->write(I)V

    return-void
.end method

.method public write(Ljava/lang/String;II)V
    .locals 3

    add-int v0, p2, p3

    move v1, p2

    :goto_0
    if-ge v1, v0, :cond_0

    invoke-virtual {p1, v1}, Ljava/lang/String;->charAt(I)C

    move-result v2

    invoke-virtual {p0, v2}, Lantlr/PrintWriterWithSMAP;->checkChar(I)V

    add-int/lit8 v1, v1, 0x1

    goto :goto_0

    :cond_0
    invoke-super {p0, p1, p2, p3}, Ljava/io/PrintWriter;->write(Ljava/lang/String;II)V

    return-void
.end method

.method public write([CII)V
    .locals 3

    add-int v0, p2, p3

    move v1, p2

    :goto_0
    if-ge v1, v0, :cond_0

    aget-char v2, p1, v1

    invoke-virtual {p0, v2}, Lantlr/PrintWriterWithSMAP;->checkChar(I)V

    add-int/lit8 v1, v1, 0x1

    goto :goto_0

    :cond_0
    invoke-super {p0, p1, p2, p3}, Ljava/io/PrintWriter;->write([CII)V

    return-void
.end method
