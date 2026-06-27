.class public Lantlr/ImportVocabTokenManager;
.super Lantlr/SimpleTokenManager;
.source ""

# interfaces
.implements Ljava/lang/Cloneable;


# instance fields
.field public filename:Ljava/lang/String;

.field public grammar:Lantlr/Grammar;


# direct methods
.method public constructor <init>(Lantlr/Grammar;Ljava/lang/String;Ljava/lang/String;Lantlr/Tool;)V
    .locals 2

    invoke-direct {p0, p3, p4}, Lantlr/SimpleTokenManager;-><init>(Ljava/lang/String;Lantlr/Tool;)V

    iput-object p1, p0, Lantlr/ImportVocabTokenManager;->grammar:Lantlr/Grammar;

    iput-object p2, p0, Lantlr/ImportVocabTokenManager;->filename:Ljava/lang/String;

    new-instance p1, Ljava/io/File;

    iget-object p2, p0, Lantlr/ImportVocabTokenManager;->filename:Ljava/lang/String;

    invoke-direct {p1, p2}, Ljava/io/File;-><init>(Ljava/lang/String;)V

    invoke-virtual {p1}, Ljava/io/File;->exists()Z

    move-result p2

    const-string p3, "Cannot find importVocab file \'"

    const-string p4, "\'"

    if-nez p2, :cond_0

    new-instance p1, Ljava/io/File;

    iget-object p2, p0, Lantlr/SimpleTokenManager;->antlrTool:Lantlr/Tool;

    invoke-virtual {p2}, Lantlr/Tool;->getOutputDirectory()Ljava/lang/String;

    move-result-object p2

    iget-object v0, p0, Lantlr/ImportVocabTokenManager;->filename:Ljava/lang/String;

    invoke-direct {p1, p2, v0}, Ljava/io/File;-><init>(Ljava/lang/String;Ljava/lang/String;)V

    invoke-virtual {p1}, Ljava/io/File;->exists()Z

    move-result p2

    if-nez p2, :cond_0

    iget-object p2, p0, Lantlr/SimpleTokenManager;->antlrTool:Lantlr/Tool;

    invoke-static {p3}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget-object v1, p0, Lantlr/ImportVocabTokenManager;->filename:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, p4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p2, v0}, Lantlr/Tool;->panic(Ljava/lang/String;)V

    :cond_0
    const/4 p2, 0x1

    invoke-virtual {p0, p2}, Lantlr/SimpleTokenManager;->setReadOnly(Z)V

    :try_start_0
    new-instance p2, Ljava/io/BufferedReader;

    new-instance v0, Ljava/io/FileReader;

    invoke-direct {v0, p1}, Ljava/io/FileReader;-><init>(Ljava/io/File;)V

    invoke-direct {p2, v0}, Ljava/io/BufferedReader;-><init>(Ljava/io/Reader;)V

    new-instance p1, Lantlr/ANTLRTokdefLexer;

    invoke-direct {p1, p2}, Lantlr/ANTLRTokdefLexer;-><init>(Ljava/io/Reader;)V

    new-instance p2, Lantlr/ANTLRTokdefParser;

    invoke-direct {p2, p1}, Lantlr/ANTLRTokdefParser;-><init>(Lantlr/TokenStream;)V

    iget-object p1, p0, Lantlr/SimpleTokenManager;->antlrTool:Lantlr/Tool;

    invoke-virtual {p2, p1}, Lantlr/ANTLRTokdefParser;->setTool(Lantlr/Tool;)V

    iget-object p1, p0, Lantlr/ImportVocabTokenManager;->filename:Ljava/lang/String;

    invoke-virtual {p2, p1}, Lantlr/Parser;->setFilename(Ljava/lang/String;)V

    invoke-virtual {p2, p0}, Lantlr/ANTLRTokdefParser;->file(Lantlr/ImportVocabTokenManager;)V
    :try_end_0
    .catch Ljava/io/FileNotFoundException; {:try_start_0 .. :try_end_0} :catch_2
    .catch Lantlr/RecognitionException; {:try_start_0 .. :try_end_0} :catch_1
    .catch Lantlr/TokenStreamException; {:try_start_0 .. :try_end_0} :catch_0

    goto :goto_1

    :catch_0
    iget-object p1, p0, Lantlr/SimpleTokenManager;->antlrTool:Lantlr/Tool;

    const-string p2, "Error reading importVocab file \'"

    invoke-static {p2}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p2

    goto :goto_0

    :catch_1
    move-exception p1

    iget-object p2, p0, Lantlr/SimpleTokenManager;->antlrTool:Lantlr/Tool;

    const-string p3, "Error parsing importVocab file \'"

    invoke-static {p3}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p3

    iget-object p0, p0, Lantlr/ImportVocabTokenManager;->filename:Ljava/lang/String;

    invoke-virtual {p3, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p0, "\': "

    invoke-virtual {p3, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Lantlr/RecognitionException;->toString()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p3, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p2, p0}, Lantlr/Tool;->panic(Ljava/lang/String;)V

    goto :goto_1

    :catch_2
    iget-object p1, p0, Lantlr/SimpleTokenManager;->antlrTool:Lantlr/Tool;

    invoke-static {p3}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p2

    :goto_0
    iget-object p0, p0, Lantlr/ImportVocabTokenManager;->filename:Ljava/lang/String;

    invoke-virtual {p2, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p2, p4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p1, p0}, Lantlr/Tool;->panic(Ljava/lang/String;)V

    :goto_1
    return-void
.end method


# virtual methods
.method public clone()Ljava/lang/Object;
    .locals 2

    invoke-super {p0}, Lantlr/SimpleTokenManager;->clone()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lantlr/ImportVocabTokenManager;

    iget-object v1, p0, Lantlr/ImportVocabTokenManager;->filename:Ljava/lang/String;

    iput-object v1, v0, Lantlr/ImportVocabTokenManager;->filename:Ljava/lang/String;

    iget-object p0, p0, Lantlr/ImportVocabTokenManager;->grammar:Lantlr/Grammar;

    iput-object p0, v0, Lantlr/ImportVocabTokenManager;->grammar:Lantlr/Grammar;

    return-object v0
.end method

.method public define(Lantlr/TokenSymbol;)V
    .locals 0

    invoke-super {p0, p1}, Lantlr/SimpleTokenManager;->define(Lantlr/TokenSymbol;)V

    return-void
.end method

.method public define(Ljava/lang/String;I)V
    .locals 1

    const-string v0, "\""

    invoke-virtual {p1, v0}, Ljava/lang/String;->startsWith(Ljava/lang/String;)Z

    move-result v0

    if-eqz v0, :cond_0

    new-instance v0, Lantlr/StringLiteralSymbol;

    invoke-direct {v0, p1}, Lantlr/StringLiteralSymbol;-><init>(Ljava/lang/String;)V

    goto :goto_0

    :cond_0
    new-instance v0, Lantlr/TokenSymbol;

    invoke-direct {v0, p1}, Lantlr/TokenSymbol;-><init>(Ljava/lang/String;)V

    :goto_0
    invoke-virtual {v0, p2}, Lantlr/TokenSymbol;->setTokenType(I)V

    invoke-super {p0, v0}, Lantlr/SimpleTokenManager;->define(Lantlr/TokenSymbol;)V

    add-int/lit8 p2, p2, 0x1

    iget p1, p0, Lantlr/SimpleTokenManager;->maxToken:I

    if-le p2, p1, :cond_1

    move p1, p2

    :cond_1
    iput p1, p0, Lantlr/SimpleTokenManager;->maxToken:I

    return-void
.end method

.method public isReadOnly()Z
    .locals 0

    iget-boolean p0, p0, Lantlr/SimpleTokenManager;->readOnly:Z

    return p0
.end method

.method public nextTokenType()I
    .locals 0

    invoke-super {p0}, Lantlr/SimpleTokenManager;->nextTokenType()I

    move-result p0

    return p0
.end method
