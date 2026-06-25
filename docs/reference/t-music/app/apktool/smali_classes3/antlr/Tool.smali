.class public Lantlr/Tool;
.super Ljava/lang/Object;
.source ""


# static fields
.field public static version:Ljava/lang/String; = ""


# instance fields
.field public cmdLineArgValid:Lantlr/collections/impl/BitSet;

.field public errorHandler:Lantlr/ToolErrorHandler;

.field public transient f:Ljava/io/Reader;

.field public genDiagnostics:Z

.field public genDocBook:Z

.field public genHTML:Z

.field public genHashLines:Z

.field public grammarFile:Ljava/lang/String;

.field public hasError:Z

.field public literalsPrefix:Ljava/lang/String;

.field public nameSpace:Lantlr/NameSpace;

.field public namespaceAntlr:Ljava/lang/String;

.field public namespaceStd:Ljava/lang/String;

.field public noConstructors:Z

.field public outputDir:Ljava/lang/String;

.field public upperCaseMangledLiterals:Z


# direct methods
.method public static constructor <clinit>()V
    .locals 0

    return-void
.end method

.method public constructor <init>()V
    .locals 3

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    const/4 v0, 0x0

    iput-boolean v0, p0, Lantlr/Tool;->hasError:Z

    iput-boolean v0, p0, Lantlr/Tool;->genDiagnostics:Z

    iput-boolean v0, p0, Lantlr/Tool;->genDocBook:Z

    iput-boolean v0, p0, Lantlr/Tool;->genHTML:Z

    const-string v1, "."

    iput-object v1, p0, Lantlr/Tool;->outputDir:Ljava/lang/String;

    new-instance v1, Ljava/io/InputStreamReader;

    sget-object v2, Ljava/lang/System;->in:Ljava/io/InputStream;

    invoke-direct {v1, v2}, Ljava/io/InputStreamReader;-><init>(Ljava/io/InputStream;)V

    iput-object v1, p0, Lantlr/Tool;->f:Ljava/io/Reader;

    const-string v1, "LITERAL_"

    iput-object v1, p0, Lantlr/Tool;->literalsPrefix:Ljava/lang/String;

    iput-boolean v0, p0, Lantlr/Tool;->upperCaseMangledLiterals:Z

    const/4 v1, 0x0

    iput-object v1, p0, Lantlr/Tool;->nameSpace:Lantlr/NameSpace;

    iput-object v1, p0, Lantlr/Tool;->namespaceAntlr:Ljava/lang/String;

    iput-object v1, p0, Lantlr/Tool;->namespaceStd:Ljava/lang/String;

    const/4 v1, 0x1

    iput-boolean v1, p0, Lantlr/Tool;->genHashLines:Z

    iput-boolean v0, p0, Lantlr/Tool;->noConstructors:Z

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-direct {v0}, Lantlr/collections/impl/BitSet;-><init>()V

    iput-object v0, p0, Lantlr/Tool;->cmdLineArgValid:Lantlr/collections/impl/BitSet;

    new-instance v0, Lantlr/DefaultToolErrorHandler;

    invoke-direct {v0, p0}, Lantlr/DefaultToolErrorHandler;-><init>(Lantlr/Tool;)V

    iput-object v0, p0, Lantlr/Tool;->errorHandler:Lantlr/ToolErrorHandler;

    return-void
.end method

.method public static help()V
    .locals 2

    sget-object v0, Ljava/lang/System;->err:Ljava/io/PrintStream;

    const-string v1, "usage: java antlr.Tool [args] file.g"

    invoke-virtual {v0, v1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    sget-object v0, Ljava/lang/System;->err:Ljava/io/PrintStream;

    const-string v1, "  -o outputDir       specify output directory where all output generated."

    invoke-virtual {v0, v1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    sget-object v0, Ljava/lang/System;->err:Ljava/io/PrintStream;

    const-string v1, "  -glib superGrammar specify location of supergrammar file."

    invoke-virtual {v0, v1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    sget-object v0, Ljava/lang/System;->err:Ljava/io/PrintStream;

    const-string v1, "  -debug             launch the ParseView debugger upon parser invocation."

    invoke-virtual {v0, v1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    sget-object v0, Ljava/lang/System;->err:Ljava/io/PrintStream;

    const-string v1, "  -html              generate a html file from your grammar."

    invoke-virtual {v0, v1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    sget-object v0, Ljava/lang/System;->err:Ljava/io/PrintStream;

    const-string v1, "  -docbook           generate a docbook sgml file from your grammar."

    invoke-virtual {v0, v1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    sget-object v0, Ljava/lang/System;->err:Ljava/io/PrintStream;

    const-string v1, "  -diagnostic        generate a textfile with diagnostics."

    invoke-virtual {v0, v1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    sget-object v0, Ljava/lang/System;->err:Ljava/io/PrintStream;

    const-string v1, "  -trace             have all rules call traceIn/traceOut."

    invoke-virtual {v0, v1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    sget-object v0, Ljava/lang/System;->err:Ljava/io/PrintStream;

    const-string v1, "  -traceLexer        have lexer rules call traceIn/traceOut."

    invoke-virtual {v0, v1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    sget-object v0, Ljava/lang/System;->err:Ljava/io/PrintStream;

    const-string v1, "  -traceParser       have parser rules call traceIn/traceOut."

    invoke-virtual {v0, v1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    sget-object v0, Ljava/lang/System;->err:Ljava/io/PrintStream;

    const-string v1, "  -traceTreeParser   have tree parser rules call traceIn/traceOut."

    invoke-virtual {v0, v1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    sget-object v0, Ljava/lang/System;->err:Ljava/io/PrintStream;

    const-string v1, "  -h|-help|--help    this message"

    invoke-virtual {v0, v1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    return-void
.end method

.method public static main([Ljava/lang/String;)V
    .locals 4

    sget-object v0, Ljava/lang/System;->err:Ljava/io/PrintStream;

    const-string v1, "ANTLR Parser Generator   Version 2.7.7 (2006-11-01)   1989-2005"

    invoke-virtual {v0, v1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    const-string v0, "2.7.7 (2006-11-01)"

    sput-object v0, Lantlr/Tool;->version:Ljava/lang/String;

    :try_start_0
    array-length v0, p0

    const/4 v1, 0x0

    if-nez v0, :cond_0

    goto :goto_1

    :cond_0
    move v0, v1

    :goto_0
    array-length v2, p0

    if-ge v0, v2, :cond_3

    aget-object v2, p0, v0

    const-string v3, "-h"

    invoke-virtual {v2, v3}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v2

    if-nez v2, :cond_2

    aget-object v2, p0, v0

    const-string v3, "-help"

    invoke-virtual {v2, v3}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v2

    if-nez v2, :cond_2

    aget-object v2, p0, v0

    const-string v3, "--help"

    invoke-virtual {v2, v3}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v2

    if-eqz v2, :cond_1

    goto :goto_1

    :cond_1
    add-int/lit8 v0, v0, 0x1

    goto :goto_0

    :cond_2
    :goto_1
    const/4 v1, 0x1

    :cond_3
    if-eqz v1, :cond_4

    invoke-static {}, Lantlr/Tool;->help()V

    goto :goto_2

    :cond_4
    new-instance v0, Lantlr/Tool;

    invoke-direct {v0}, Lantlr/Tool;-><init>()V

    invoke-virtual {v0, p0}, Lantlr/Tool;->doEverything([Ljava/lang/String;)I
    :try_end_0
    .catch Ljava/lang/Exception; {:try_start_0 .. :try_end_0} :catch_0

    goto :goto_2

    :catch_0
    move-exception p0

    sget-object v0, Ljava/lang/System;->err:Ljava/io/PrintStream;

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "line.separator"

    invoke-static {v2}, Ljava/lang/System;->getProperty(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-static {v2}, Ljava/lang/System;->getProperty(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    sget-object v0, Ljava/lang/System;->err:Ljava/io/PrintStream;

    const-string v1, "#$%%*&@# internal error: "

    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    invoke-virtual {p0}, Ljava/lang/Exception;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    sget-object v0, Ljava/lang/System;->err:Ljava/io/PrintStream;

    const-string v1, "[complain to nearest government official"

    invoke-virtual {v0, v1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    sget-object v0, Ljava/lang/System;->err:Ljava/io/PrintStream;

    const-string v1, " or send hate-mail to parrt@antlr.org;"

    invoke-virtual {v0, v1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    sget-object v0, Ljava/lang/System;->err:Ljava/io/PrintStream;

    const-string v1, " please send stack trace with report.]"

    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    invoke-static {v2}, Ljava/lang/System;->getProperty(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    invoke-virtual {p0}, Ljava/lang/Exception;->printStackTrace()V

    :goto_2
    return-void
.end method

.method public static parseSeparatedList(Ljava/lang/String;C)Lantlr/collections/impl/Vector;
    .locals 1

    new-instance v0, Ljava/util/StringTokenizer;

    invoke-static {p1}, Ljava/lang/String;->valueOf(C)Ljava/lang/String;

    move-result-object p1

    invoke-direct {v0, p0, p1}, Ljava/util/StringTokenizer;-><init>(Ljava/lang/String;Ljava/lang/String;)V

    new-instance p0, Lantlr/collections/impl/Vector;

    const/16 p1, 0xa

    invoke-direct {p0, p1}, Lantlr/collections/impl/Vector;-><init>(I)V

    :goto_0
    invoke-virtual {v0}, Ljava/util/StringTokenizer;->hasMoreTokens()Z

    move-result p1

    if-eqz p1, :cond_0

    invoke-virtual {v0}, Ljava/util/StringTokenizer;->nextToken()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/collections/impl/Vector;->appendElement(Ljava/lang/Object;)V

    goto :goto_0

    :cond_0
    invoke-virtual {p0}, Lantlr/collections/impl/Vector;->size()I

    move-result p1

    if-nez p1, :cond_1

    const/4 p0, 0x0

    :cond_1
    return-object p0
.end method


# virtual methods
.method public checkForInvalidArguments([Ljava/lang/String;Lantlr/collections/impl/BitSet;)V
    .locals 3

    const/4 v0, 0x0

    :goto_0
    array-length v1, p1

    if-ge v0, v1, :cond_1

    invoke-virtual {p2, v0}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v1

    if-nez v1, :cond_0

    const-string v1, "invalid command-line argument: "

    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    aget-object v2, p1, v0

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, "; ignored"

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/Tool;->warning(Ljava/lang/String;)V

    :cond_0
    add-int/lit8 v0, v0, 0x1

    goto :goto_0

    :cond_1
    return-void
.end method

.method public copyFile(Ljava/lang/String;Ljava/lang/String;)V
    .locals 4

    new-instance v0, Ljava/io/File;

    invoke-direct {v0, p1}, Ljava/io/File;-><init>(Ljava/lang/String;)V

    new-instance v1, Ljava/io/File;

    invoke-direct {v1, p2}, Ljava/io/File;-><init>(Ljava/lang/String;)V

    const/4 v2, 0x0

    :try_start_0
    invoke-virtual {v0}, Ljava/io/File;->exists()Z

    move-result v3

    if-eqz v3, :cond_7

    invoke-virtual {v0}, Ljava/io/File;->isFile()Z

    move-result v3

    if-eqz v3, :cond_7

    invoke-virtual {v0}, Ljava/io/File;->canRead()Z

    move-result v3

    if-eqz v3, :cond_6

    invoke-virtual {v1}, Ljava/io/File;->exists()Z

    move-result p1

    if-eqz p1, :cond_2

    invoke-virtual {v1}, Ljava/io/File;->isFile()Z

    move-result p0

    if-eqz p0, :cond_1

    new-instance p0, Ljava/io/DataInputStream;

    sget-object p1, Ljava/lang/System;->in:Ljava/io/InputStream;

    invoke-direct {p0, p1}, Ljava/io/DataInputStream;-><init>(Ljava/io/InputStream;)V

    invoke-virtual {v1}, Ljava/io/File;->canWrite()Z

    move-result p0

    if-eqz p0, :cond_0

    goto :goto_0

    :cond_0
    new-instance p0, Lantlr/FileCopyException;

    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v0, "FileCopy: destination file is unwriteable: "

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-direct {p0, p1}, Lantlr/FileCopyException;-><init>(Ljava/lang/String;)V

    throw p0

    :cond_1
    new-instance p0, Lantlr/FileCopyException;

    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v0, "FileCopy: destination is not a file: "

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-direct {p0, p1}, Lantlr/FileCopyException;-><init>(Ljava/lang/String;)V

    throw p0

    :cond_2
    invoke-virtual {p0, v1}, Lantlr/Tool;->parent(Ljava/io/File;)Ljava/io/File;

    move-result-object p0

    invoke-virtual {p0}, Ljava/io/File;->exists()Z

    move-result p1

    if-eqz p1, :cond_5

    invoke-virtual {p0}, Ljava/io/File;->canWrite()Z

    move-result p0

    if-eqz p0, :cond_4

    :goto_0
    new-instance p0, Ljava/io/BufferedReader;

    new-instance p1, Ljava/io/FileReader;

    invoke-direct {p1, v0}, Ljava/io/FileReader;-><init>(Ljava/io/File;)V

    invoke-direct {p0, p1}, Ljava/io/BufferedReader;-><init>(Ljava/io/Reader;)V
    :try_end_0
    .catchall {:try_start_0 .. :try_end_0} :catchall_2

    :try_start_1
    new-instance p1, Ljava/io/BufferedWriter;

    new-instance p2, Ljava/io/FileWriter;

    invoke-direct {p2, v1}, Ljava/io/FileWriter;-><init>(Ljava/io/File;)V

    invoke-direct {p1, p2}, Ljava/io/BufferedWriter;-><init>(Ljava/io/Writer;)V
    :try_end_1
    .catchall {:try_start_1 .. :try_end_1} :catchall_1

    const/16 p2, 0x400

    :try_start_2
    new-array v0, p2, [C

    :goto_1
    const/4 v1, 0x0

    invoke-virtual {p0, v0, v1, p2}, Ljava/io/BufferedReader;->read([CII)I

    move-result v2
    :try_end_2
    .catchall {:try_start_2 .. :try_end_2} :catchall_0

    const/4 v3, -0x1

    if-ne v2, v3, :cond_3

    :try_start_3
    invoke-virtual {p0}, Ljava/io/BufferedReader;->close()V
    :try_end_3
    .catch Ljava/io/IOException; {:try_start_3 .. :try_end_3} :catch_0

    :catch_0
    :try_start_4
    invoke-virtual {p1}, Ljava/io/BufferedWriter;->close()V
    :try_end_4
    .catch Ljava/io/IOException; {:try_start_4 .. :try_end_4} :catch_1

    :catch_1
    return-void

    :cond_3
    :try_start_5
    invoke-virtual {p1, v0, v1, v2}, Ljava/io/BufferedWriter;->write([CII)V
    :try_end_5
    .catchall {:try_start_5 .. :try_end_5} :catchall_0

    goto :goto_1

    :catchall_0
    move-exception p2

    move-object v2, p1

    move-object p1, p2

    goto :goto_2

    :catchall_1
    move-exception p1

    goto :goto_2

    :cond_4
    :try_start_6
    new-instance p0, Lantlr/FileCopyException;

    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v0, "FileCopy: destination directory is unwriteable: "

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-direct {p0, p1}, Lantlr/FileCopyException;-><init>(Ljava/lang/String;)V

    throw p0

    :cond_5
    new-instance p0, Lantlr/FileCopyException;

    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v0, "FileCopy: destination directory doesn\'t exist: "

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-direct {p0, p1}, Lantlr/FileCopyException;-><init>(Ljava/lang/String;)V

    throw p0

    :cond_6
    new-instance p0, Lantlr/FileCopyException;

    new-instance p2, Ljava/lang/StringBuilder;

    invoke-direct {p2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v0, "FileCopy: source file is unreadable: "

    invoke-virtual {p2, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p2, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-direct {p0, p1}, Lantlr/FileCopyException;-><init>(Ljava/lang/String;)V

    throw p0

    :cond_7
    new-instance p0, Lantlr/FileCopyException;

    new-instance p2, Ljava/lang/StringBuilder;

    invoke-direct {p2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v0, "FileCopy: no such source file: "

    invoke-virtual {p2, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p2, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-direct {p0, p1}, Lantlr/FileCopyException;-><init>(Ljava/lang/String;)V

    throw p0
    :try_end_6
    .catchall {:try_start_6 .. :try_end_6} :catchall_2

    :catchall_2
    move-exception p0

    move-object p1, p0

    move-object p0, v2

    :goto_2
    if-eqz p0, :cond_8

    :try_start_7
    invoke-virtual {p0}, Ljava/io/BufferedReader;->close()V
    :try_end_7
    .catch Ljava/io/IOException; {:try_start_7 .. :try_end_7} :catch_2

    :catch_2
    :cond_8
    if-eqz v2, :cond_9

    :try_start_8
    invoke-virtual {v2}, Ljava/io/BufferedWriter;->close()V
    :try_end_8
    .catch Ljava/io/IOException; {:try_start_8 .. :try_end_8} :catch_3

    :catch_3
    :cond_9
    throw p1
.end method

.method public doEverything([Ljava/lang/String;)I
    .locals 5

    const-string v0, "Cannot instantiate code-generator: "

    new-instance v1, Lantlr/preprocessor/Tool;

    invoke-direct {v1, p0, p1}, Lantlr/preprocessor/Tool;-><init>(Lantlr/Tool;[Ljava/lang/String;)V

    invoke-virtual {v1}, Lantlr/preprocessor/Tool;->preprocess()Z

    move-result v2

    invoke-virtual {v1}, Lantlr/preprocessor/Tool;->preprocessedArgList()[Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/Tool;->processArguments([Ljava/lang/String;)V

    if-nez v2, :cond_0

    const/4 p0, 0x1

    return p0

    :cond_0
    invoke-virtual {p0}, Lantlr/Tool;->getGrammarReader()Ljava/io/Reader;

    move-result-object v2

    iput-object v2, p0, Lantlr/Tool;->f:Ljava/io/Reader;

    new-instance v2, Lantlr/ANTLRLexer;

    iget-object v3, p0, Lantlr/Tool;->f:Ljava/io/Reader;

    invoke-direct {v2, v3}, Lantlr/ANTLRLexer;-><init>(Ljava/io/Reader;)V

    new-instance v3, Lantlr/TokenBuffer;

    invoke-direct {v3, v2}, Lantlr/TokenBuffer;-><init>(Lantlr/TokenStream;)V

    new-instance v2, Lantlr/LLkAnalyzer;

    invoke-direct {v2, p0}, Lantlr/LLkAnalyzer;-><init>(Lantlr/Tool;)V

    new-instance v4, Lantlr/MakeGrammar;

    invoke-direct {v4, p0, p1, v2}, Lantlr/MakeGrammar;-><init>(Lantlr/Tool;[Ljava/lang/String;Lantlr/LLkAnalyzer;)V

    :try_start_0
    new-instance p1, Lantlr/ANTLRParser;

    invoke-direct {p1, v3, v4, p0}, Lantlr/ANTLRParser;-><init>(Lantlr/TokenBuffer;Lantlr/ANTLRGrammarParseBehavior;Lantlr/Tool;)V

    iget-object v3, p0, Lantlr/Tool;->grammarFile:Ljava/lang/String;

    invoke-virtual {p1, v3}, Lantlr/Parser;->setFilename(Ljava/lang/String;)V

    invoke-virtual {p1}, Lantlr/ANTLRParser;->grammar()V

    invoke-virtual {p0}, Lantlr/Tool;->hasError()Z

    move-result p1

    if-eqz p1, :cond_1

    const-string p1, "Exiting due to errors."

    invoke-virtual {p0, p1}, Lantlr/Tool;->fatalError(Ljava/lang/String;)V

    :cond_1
    iget-object p1, p0, Lantlr/Tool;->cmdLineArgValid:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v1, p1}, Lantlr/Tool;->checkForInvalidArguments([Ljava/lang/String;Lantlr/collections/impl/BitSet;)V

    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "antlr."

    invoke-virtual {p1, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0, v4}, Lantlr/Tool;->getLanguage(Lantlr/MakeGrammar;)Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p1, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, "CodeGenerator"

    invoke-virtual {p1, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1
    :try_end_0
    .catch Lantlr/RecognitionException; {:try_start_0 .. :try_end_0} :catch_5
    .catch Lantlr/TokenStreamException; {:try_start_0 .. :try_end_0} :catch_4

    :try_start_1
    invoke-static {p1}, Lantlr/Utils;->createInstanceOf(Ljava/lang/String;)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Lantlr/CodeGenerator;

    invoke-virtual {v1, v4}, Lantlr/CodeGenerator;->setBehavior(Lantlr/DefineGrammarSymbols;)V

    invoke-virtual {v1, v2}, Lantlr/CodeGenerator;->setAnalyzer(Lantlr/LLkGrammarAnalyzer;)V

    invoke-virtual {v1, p0}, Lantlr/CodeGenerator;->setTool(Lantlr/Tool;)V

    invoke-virtual {v1}, Lantlr/CodeGenerator;->gen()V
    :try_end_1
    .catch Ljava/lang/ClassNotFoundException; {:try_start_1 .. :try_end_1} :catch_3
    .catch Ljava/lang/InstantiationException; {:try_start_1 .. :try_end_1} :catch_2
    .catch Ljava/lang/IllegalArgumentException; {:try_start_1 .. :try_end_1} :catch_1
    .catch Ljava/lang/IllegalAccessException; {:try_start_1 .. :try_end_1} :catch_0
    .catch Lantlr/RecognitionException; {:try_start_1 .. :try_end_1} :catch_5
    .catch Lantlr/TokenStreamException; {:try_start_1 .. :try_end_1} :catch_4

    goto :goto_3

    :catch_0
    :try_start_2
    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "code-generator class \'"

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p1, "\' is not accessible"

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-object v1, v0

    goto :goto_1

    :catch_1
    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    :goto_0
    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    goto :goto_1

    :catch_2
    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    goto :goto_0

    :catch_3
    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    goto :goto_0

    :goto_1
    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/Tool;->panic(Ljava/lang/String;)V
    :try_end_2
    .catch Lantlr/RecognitionException; {:try_start_2 .. :try_end_2} :catch_5
    .catch Lantlr/TokenStreamException; {:try_start_2 .. :try_end_2} :catch_4

    goto :goto_3

    :catch_4
    move-exception p1

    const-string v0, "TokenStreamException: "

    goto :goto_2

    :catch_5
    move-exception p1

    const-string v0, "Unhandled parser error: "

    :goto_2
    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {p1}, Ljava/lang/Exception;->getMessage()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/Tool;->fatalError(Ljava/lang/String;)V

    :goto_3
    const/4 p0, 0x0

    return p0
.end method

.method public doEverythingWrapper([Ljava/lang/String;)V
    .locals 0

    invoke-virtual {p0, p1}, Lantlr/Tool;->doEverything([Ljava/lang/String;)I

    move-result p0

    invoke-static {p0}, Ljava/lang/System;->exit(I)V

    return-void
.end method

.method public error(Ljava/lang/String;)V
    .locals 2

    const/4 v0, 0x1

    iput-boolean v0, p0, Lantlr/Tool;->hasError:Z

    sget-object p0, Ljava/lang/System;->err:Ljava/io/PrintStream;

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "error: "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    return-void
.end method

.method public error(Ljava/lang/String;Ljava/lang/String;II)V
    .locals 2

    const/4 v0, 0x1

    iput-boolean v0, p0, Lantlr/Tool;->hasError:Z

    sget-object p0, Ljava/lang/System;->err:Ljava/io/PrintStream;

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    invoke-static {}, Lantlr/FileLineFormatter;->getFormatter()Lantlr/FileLineFormatter;

    move-result-object v1

    invoke-virtual {v1, p2, p3, p4}, Lantlr/FileLineFormatter;->getFormatString(Ljava/lang/String;II)Ljava/lang/String;

    move-result-object p2

    invoke-virtual {v0, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    return-void
.end method

.method public fatalError(Ljava/lang/String;)V
    .locals 0

    sget-object p0, Ljava/lang/System;->err:Ljava/io/PrintStream;

    invoke-virtual {p0, p1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    invoke-static {p1}, Lantlr/Utils;->error(Ljava/lang/String;)V

    const/4 p0, 0x0

    throw p0
.end method

.method public fileMinusPath(Ljava/lang/String;)Ljava/lang/String;
    .locals 1

    const-string p0, "file.separator"

    invoke-static {p0}, Ljava/lang/System;->getProperty(Ljava/lang/String;)Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p1, p0}, Ljava/lang/String;->lastIndexOf(Ljava/lang/String;)I

    move-result p0

    const/4 v0, -0x1

    if-ne p0, v0, :cond_0

    return-object p1

    :cond_0
    add-int/lit8 p0, p0, 0x1

    invoke-virtual {p1, p0}, Ljava/lang/String;->substring(I)Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public getGenHashLines()Z
    .locals 0

    iget-boolean p0, p0, Lantlr/Tool;->genHashLines:Z

    return p0
.end method

.method public getGrammarFile()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lantlr/Tool;->grammarFile:Ljava/lang/String;

    return-object p0
.end method

.method public getGrammarReader()Ljava/io/Reader;
    .locals 3

    :try_start_0
    iget-object v0, p0, Lantlr/Tool;->grammarFile:Ljava/lang/String;

    if-eqz v0, :cond_0

    new-instance v0, Ljava/io/BufferedReader;

    new-instance v1, Ljava/io/FileReader;

    iget-object v2, p0, Lantlr/Tool;->grammarFile:Ljava/lang/String;

    invoke-direct {v1, v2}, Ljava/io/FileReader;-><init>(Ljava/lang/String;)V

    invoke-direct {v0, v1}, Ljava/io/BufferedReader;-><init>(Ljava/io/Reader;)V
    :try_end_0
    .catch Ljava/io/IOException; {:try_start_0 .. :try_end_0} :catch_0

    goto :goto_0

    :catch_0
    const-string v0, "cannot open grammar file "

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget-object v1, p0, Lantlr/Tool;->grammarFile:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/Tool;->fatalError(Ljava/lang/String;)V

    :cond_0
    const/4 v0, 0x0

    :goto_0
    return-object v0
.end method

.method public getLanguage(Lantlr/MakeGrammar;)Ljava/lang/String;
    .locals 1

    iget-boolean v0, p0, Lantlr/Tool;->genDiagnostics:Z

    if-eqz v0, :cond_0

    const-string p0, "Diagnostic"

    return-object p0

    :cond_0
    iget-boolean v0, p0, Lantlr/Tool;->genHTML:Z

    if-eqz v0, :cond_1

    const-string p0, "HTML"

    return-object p0

    :cond_1
    iget-boolean p0, p0, Lantlr/Tool;->genDocBook:Z

    if-eqz p0, :cond_2

    const-string p0, "DocBook"

    return-object p0

    :cond_2
    iget-object p0, p1, Lantlr/DefineGrammarSymbols;->language:Ljava/lang/String;

    return-object p0
.end method

.method public getLiteralsPrefix()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lantlr/Tool;->literalsPrefix:Ljava/lang/String;

    return-object p0
.end method

.method public getNameSpace()Lantlr/NameSpace;
    .locals 0

    iget-object p0, p0, Lantlr/Tool;->nameSpace:Lantlr/NameSpace;

    return-object p0
.end method

.method public getNamespaceAntlr()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lantlr/Tool;->namespaceAntlr:Ljava/lang/String;

    return-object p0
.end method

.method public getNamespaceStd()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lantlr/Tool;->namespaceStd:Ljava/lang/String;

    return-object p0
.end method

.method public getOutputDirectory()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lantlr/Tool;->outputDir:Ljava/lang/String;

    return-object p0
.end method

.method public getUpperCaseMangledLiterals()Z
    .locals 0

    iget-boolean p0, p0, Lantlr/Tool;->upperCaseMangledLiterals:Z

    return p0
.end method

.method public hasError()Z
    .locals 0

    iget-boolean p0, p0, Lantlr/Tool;->hasError:Z

    return p0
.end method

.method public openOutputFile(Ljava/lang/String;)Ljava/io/PrintWriter;
    .locals 3

    iget-object v0, p0, Lantlr/Tool;->outputDir:Ljava/lang/String;

    const-string v1, "."

    if-eq v0, v1, :cond_0

    new-instance v1, Ljava/io/File;

    invoke-direct {v1, v0}, Ljava/io/File;-><init>(Ljava/lang/String;)V

    invoke-virtual {v1}, Ljava/io/File;->exists()Z

    move-result v0

    if-nez v0, :cond_0

    invoke-virtual {v1}, Ljava/io/File;->mkdirs()Z

    :cond_0
    new-instance v0, Ljava/io/PrintWriter;

    new-instance v1, Lantlr/PreservingFileWriter;

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    iget-object p0, p0, Lantlr/Tool;->outputDir:Ljava/lang/String;

    invoke-virtual {v2, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p0, "file.separator"

    invoke-static {p0}, Ljava/lang/System;->getProperty(Ljava/lang/String;)Ljava/lang/String;

    move-result-object p0

    invoke-virtual {v2, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    invoke-direct {v1, p0}, Lantlr/PreservingFileWriter;-><init>(Ljava/lang/String;)V

    invoke-direct {v0, v1}, Ljava/io/PrintWriter;-><init>(Ljava/io/Writer;)V

    return-object v0
.end method

.method public panic()V
    .locals 1

    const-string v0, "panic"

    invoke-virtual {p0, v0}, Lantlr/Tool;->fatalError(Ljava/lang/String;)V

    return-void
.end method

.method public panic(Ljava/lang/String;)V
    .locals 2

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "panic: "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/Tool;->fatalError(Ljava/lang/String;)V

    return-void
.end method

.method public parent(Ljava/io/File;)Ljava/io/File;
    .locals 0

    invoke-virtual {p1}, Ljava/io/File;->getParent()Ljava/lang/String;

    move-result-object p0

    if-nez p0, :cond_1

    invoke-virtual {p1}, Ljava/io/File;->isAbsolute()Z

    move-result p0

    if-eqz p0, :cond_0

    new-instance p0, Ljava/io/File;

    sget-object p1, Ljava/io/File;->separator:Ljava/lang/String;

    invoke-direct {p0, p1}, Ljava/io/File;-><init>(Ljava/lang/String;)V

    return-object p0

    :cond_0
    new-instance p0, Ljava/io/File;

    const-string p1, "user.dir"

    invoke-static {p1}, Ljava/lang/System;->getProperty(Ljava/lang/String;)Ljava/lang/String;

    move-result-object p1

    invoke-direct {p0, p1}, Ljava/io/File;-><init>(Ljava/lang/String;)V

    return-object p0

    :cond_1
    new-instance p1, Ljava/io/File;

    invoke-direct {p1, p0}, Ljava/io/File;-><init>(Ljava/lang/String;)V

    return-object p1
.end method

.method public pathToFile(Ljava/lang/String;)Ljava/lang/String;
    .locals 2

    const-string p0, "file.separator"

    invoke-static {p0}, Ljava/lang/System;->getProperty(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p1, v0}, Ljava/lang/String;->lastIndexOf(Ljava/lang/String;)I

    move-result v0

    const/4 v1, -0x1

    if-ne v0, v1, :cond_0

    const-string p1, "."

    invoke-static {p1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p1

    invoke-static {p0}, Ljava/lang/System;->getProperty(Ljava/lang/String;)Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p1, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0

    :cond_0
    add-int/lit8 v0, v0, 0x1

    const/4 p0, 0x0

    invoke-virtual {p1, p0, v0}, Ljava/lang/String;->substring(II)Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public processArguments([Ljava/lang/String;)V
    .locals 5

    const/4 v0, 0x0

    move v1, v0

    :goto_0
    array-length v2, p1

    if-ge v1, v2, :cond_6

    aget-object v2, p1, v1

    const-string v3, "-diagnostic"

    invoke-virtual {v2, v3}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v2

    const/4 v3, 0x1

    if-eqz v2, :cond_0

    iput-boolean v3, p0, Lantlr/Tool;->genDiagnostics:Z

    iput-boolean v0, p0, Lantlr/Tool;->genHTML:Z

    :goto_1
    invoke-virtual {p0, v1}, Lantlr/Tool;->setArgOK(I)V

    goto :goto_3

    :cond_0
    aget-object v2, p1, v1

    const-string v4, "-o"

    invoke-virtual {v2, v4}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v2

    if-eqz v2, :cond_2

    invoke-virtual {p0, v1}, Lantlr/Tool;->setArgOK(I)V

    add-int/lit8 v2, v1, 0x1

    array-length v4, p1

    if-lt v2, v4, :cond_1

    const-string v2, "missing output directory with -o option; ignoring"

    invoke-virtual {p0, v2}, Lantlr/Tool;->error(Ljava/lang/String;)V

    goto :goto_3

    :cond_1
    aget-object v1, p1, v2

    invoke-virtual {p0, v1}, Lantlr/Tool;->setOutputDirectory(Ljava/lang/String;)V

    invoke-virtual {p0, v2}, Lantlr/Tool;->setArgOK(I)V

    move v1, v2

    goto :goto_3

    :cond_2
    aget-object v2, p1, v1

    const-string v4, "-html"

    invoke-virtual {v2, v4}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v2

    if-eqz v2, :cond_3

    iput-boolean v3, p0, Lantlr/Tool;->genHTML:Z

    :goto_2
    iput-boolean v0, p0, Lantlr/Tool;->genDiagnostics:Z

    goto :goto_1

    :cond_3
    aget-object v2, p1, v1

    const-string v4, "-docbook"

    invoke-virtual {v2, v4}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v2

    if-eqz v2, :cond_4

    iput-boolean v3, p0, Lantlr/Tool;->genDocBook:Z

    goto :goto_2

    :cond_4
    aget-object v2, p1, v1

    invoke-virtual {v2, v0}, Ljava/lang/String;->charAt(I)C

    move-result v2

    const/16 v4, 0x2d

    if-eq v2, v4, :cond_5

    aget-object v2, p1, v1

    iput-object v2, p0, Lantlr/Tool;->grammarFile:Ljava/lang/String;

    goto :goto_1

    :cond_5
    :goto_3
    add-int/2addr v1, v3

    goto :goto_0

    :cond_6
    return-void
.end method

.method public reportException(Ljava/lang/Exception;Ljava/lang/String;)V
    .locals 1

    sget-object p0, Ljava/lang/System;->err:Ljava/io/PrintStream;

    if-nez p2, :cond_0

    invoke-virtual {p1}, Ljava/lang/Exception;->getMessage()Ljava/lang/String;

    move-result-object p1

    goto :goto_0

    :cond_0
    const-string v0, ": "

    invoke-static {p2, v0}, La/a/a/a/a;->b(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p2

    invoke-virtual {p1}, Ljava/lang/Exception;->getMessage()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p2, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    :goto_0
    invoke-virtual {p0, p1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    return-void
.end method

.method public reportProgress(Ljava/lang/String;)V
    .locals 0

    sget-object p0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    invoke-virtual {p0, p1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    return-void
.end method

.method public setArgOK(I)V
    .locals 0

    iget-object p0, p0, Lantlr/Tool;->cmdLineArgValid:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, p1}, Lantlr/collections/impl/BitSet;->add(I)V

    return-void
.end method

.method public setFileLineFormatter(Lantlr/FileLineFormatter;)V
    .locals 0

    invoke-static {p1}, Lantlr/FileLineFormatter;->setFormatter(Lantlr/FileLineFormatter;)V

    return-void
.end method

.method public setNameSpace(Ljava/lang/String;)V
    .locals 2

    iget-object v0, p0, Lantlr/Tool;->nameSpace:Lantlr/NameSpace;

    if-nez v0, :cond_0

    new-instance v0, Lantlr/NameSpace;

    const-string v1, "\""

    invoke-static {p1, v1, v1}, Lantlr/StringUtils;->stripFrontBack(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p1

    invoke-direct {v0, p1}, Lantlr/NameSpace;-><init>(Ljava/lang/String;)V

    iput-object v0, p0, Lantlr/Tool;->nameSpace:Lantlr/NameSpace;

    :cond_0
    return-void
.end method

.method public setOutputDirectory(Ljava/lang/String;)V
    .locals 0

    iput-object p1, p0, Lantlr/Tool;->outputDir:Ljava/lang/String;

    return-void
.end method

.method public toolError(Ljava/lang/String;)V
    .locals 2

    sget-object p0, Ljava/lang/System;->err:Ljava/io/PrintStream;

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "error: "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    return-void
.end method

.method public warning(Ljava/lang/String;)V
    .locals 2

    sget-object p0, Ljava/lang/System;->err:Ljava/io/PrintStream;

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "warning: "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    return-void
.end method

.method public warning(Ljava/lang/String;Ljava/lang/String;II)V
    .locals 2

    sget-object p0, Ljava/lang/System;->err:Ljava/io/PrintStream;

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    invoke-static {}, Lantlr/FileLineFormatter;->getFormatter()Lantlr/FileLineFormatter;

    move-result-object v1

    invoke-virtual {v1, p2, p3, p4}, Lantlr/FileLineFormatter;->getFormatString(Ljava/lang/String;II)Ljava/lang/String;

    move-result-object p2

    invoke-virtual {v0, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p2, "warning:"

    invoke-virtual {v0, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    return-void
.end method

.method public warning([Ljava/lang/String;Ljava/lang/String;II)V
    .locals 3

    if-eqz p1, :cond_0

    array-length v0, p1

    if-nez v0, :cond_1

    :cond_0
    const-string v0, "bad multi-line message to Tool.warning"

    invoke-virtual {p0, v0}, Lantlr/Tool;->panic(Ljava/lang/String;)V

    :cond_1
    sget-object p0, Ljava/lang/System;->err:Ljava/io/PrintStream;

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    invoke-static {}, Lantlr/FileLineFormatter;->getFormatter()Lantlr/FileLineFormatter;

    move-result-object v1

    invoke-virtual {v1, p2, p3, p4}, Lantlr/FileLineFormatter;->getFormatString(Ljava/lang/String;II)Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, "warning:"

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const/4 v1, 0x0

    aget-object v1, p1, v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    const/4 p0, 0x1

    :goto_0
    array-length v0, p1

    if-ge p0, v0, :cond_2

    sget-object v0, Ljava/lang/System;->err:Ljava/io/PrintStream;

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    invoke-static {}, Lantlr/FileLineFormatter;->getFormatter()Lantlr/FileLineFormatter;

    move-result-object v2

    invoke-virtual {v2, p2, p3, p4}, Lantlr/FileLineFormatter;->getFormatString(Ljava/lang/String;II)Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, "    "

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    aget-object v2, p1, p0

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    add-int/lit8 p0, p0, 0x1

    goto :goto_0

    :cond_2
    return-void
.end method
