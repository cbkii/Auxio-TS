.class public final Lcpdetector/util/FileUtil;
.super Ljava/lang/Object;
.source ""


# static fields
.field public static instance:Lcpdetector/util/FileUtil;


# instance fields
.field public final m_bundle:Ljava/util/ResourceBundle;


# direct methods
.method public constructor <init>()V
    .locals 1

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    const-string v0, "messages"

    invoke-static {v0}, Ljava/util/ResourceBundle;->getBundle(Ljava/lang/String;)Ljava/util/ResourceBundle;

    move-result-object v0

    iput-object v0, p0, Lcpdetector/util/FileUtil;->m_bundle:Ljava/util/ResourceBundle;

    return-void
.end method

.method public static cutDirectoryInformation(Ljava/lang/String;)Ljava/util/Map$Entry;
    .locals 7
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "(",
            "Ljava/lang/String;",
            ")",
            "Ljava/util/Map$Entry<",
            "Ljava/lang/String;",
            "Ljava/lang/String;",
            ">;"
        }
    .end annotation

    new-instance v0, Ljava/lang/StringBuffer;

    invoke-direct {v0}, Ljava/lang/StringBuffer;-><init>()V

    const-string v1, "file.separator"

    invoke-static {v1}, Ljava/lang/System;->getProperty(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    new-instance v2, Ljava/util/StringTokenizer;

    invoke-direct {v2, p0, v1}, Ljava/util/StringTokenizer;-><init>(Ljava/lang/String;Ljava/lang/String;)V

    invoke-virtual {v2}, Ljava/util/StringTokenizer;->countTokens()I

    move-result v3

    const-string v4, "."

    const-string v5, ""

    if-eqz v3, :cond_4

    const/4 v6, 0x1

    if-eq v3, v6, :cond_2

    :goto_0
    invoke-virtual {v2}, Ljava/util/StringTokenizer;->hasMoreElements()Z

    move-result v3

    if-eqz v3, :cond_5

    invoke-virtual {v2}, Ljava/util/StringTokenizer;->nextToken()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v2}, Ljava/util/StringTokenizer;->hasMoreTokens()Z

    move-result v4

    if-eqz v4, :cond_0

    invoke-virtual {v0, v3}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    goto :goto_0

    :cond_0
    new-instance v4, Ljava/io/File;

    invoke-direct {v4, p0}, Ljava/io/File;-><init>(Ljava/lang/String;)V

    invoke-virtual {v4}, Ljava/io/File;->isFile()Z

    move-result v4

    if-eqz v4, :cond_1

    move-object v5, v3

    goto :goto_0

    :cond_1
    invoke-virtual {v0, v3}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    goto :goto_0

    :cond_2
    new-instance v1, Ljava/io/File;

    invoke-virtual {v2}, Ljava/util/StringTokenizer;->nextToken()Ljava/lang/String;

    move-result-object v2

    invoke-direct {v1, v2}, Ljava/io/File;-><init>(Ljava/lang/String;)V

    new-instance v2, Ljava/io/File;

    invoke-direct {v2, p0}, Ljava/io/File;-><init>(Ljava/lang/String;)V

    invoke-virtual {v2}, Ljava/io/File;->isDirectory()Z

    move-result v2

    if-eqz v2, :cond_3

    invoke-virtual {v1}, Ljava/io/File;->getAbsolutePath()Ljava/lang/String;

    move-result-object p0

    goto :goto_1

    :cond_3
    new-instance v1, Ljava/io/File;

    invoke-direct {v1, v4}, Ljava/io/File;-><init>(Ljava/lang/String;)V

    invoke-virtual {v1}, Ljava/io/File;->getAbsolutePath()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    move-object v5, p0

    goto :goto_2

    :cond_4
    new-instance p0, Ljava/io/File;

    invoke-direct {p0, v4}, Ljava/io/File;-><init>(Ljava/lang/String;)V

    invoke-virtual {p0}, Ljava/io/File;->getAbsolutePath()Ljava/lang/String;

    move-result-object p0

    :goto_1
    invoke-virtual {v0, p0}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    :cond_5
    :goto_2
    new-instance p0, Lcpdetector/util/Entry;

    invoke-virtual {v0}, Ljava/lang/StringBuffer;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-direct {p0, v0, v5}, Lcpdetector/util/Entry;-><init>(Ljava/lang/Object;Ljava/lang/Object;)V

    return-object p0
.end method

.method public static cutDirectoryInformation(Ljava/net/URL;)Ljava/util/Map$Entry;
    .locals 5
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "(",
            "Ljava/net/URL;",
            ")",
            "Ljava/util/Map$Entry<",
            "Ljava/lang/String;",
            "Ljava/lang/String;",
            ">;"
        }
    .end annotation

    new-instance v0, Ljava/lang/StringBuffer;

    invoke-direct {v0}, Ljava/lang/StringBuffer;-><init>()V

    invoke-virtual {p0}, Ljava/net/URL;->toExternalForm()Ljava/lang/String;

    move-result-object v1

    const-string v2, "/"

    invoke-virtual {v1, v2}, Ljava/lang/String;->endsWith(Ljava/lang/String;)Z

    move-result v3

    const-string v4, ""

    if-eqz v3, :cond_0

    goto :goto_1

    :cond_0
    new-instance v1, Ljava/util/StringTokenizer;

    invoke-virtual {p0}, Ljava/net/URL;->getFile()Ljava/lang/String;

    move-result-object v3

    invoke-direct {v1, v3, v2}, Ljava/util/StringTokenizer;-><init>(Ljava/lang/String;Ljava/lang/String;)V

    invoke-virtual {p0}, Ljava/net/URL;->getProtocol()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v0, v3}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    const-string v3, ":"

    invoke-virtual {v0, v3}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    invoke-virtual {p0}, Ljava/net/URL;->getHost()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {v0, p0}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    :goto_0
    invoke-virtual {v1}, Ljava/util/StringTokenizer;->hasMoreElements()Z

    move-result p0

    if-eqz p0, :cond_1

    invoke-virtual {v0, v4}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    invoke-virtual {v1}, Ljava/util/StringTokenizer;->nextToken()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v0, v2}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    goto :goto_0

    :cond_1
    invoke-virtual {v0}, Ljava/lang/StringBuffer;->toString()Ljava/lang/String;

    move-result-object v1

    :goto_1
    new-instance p0, Lcpdetector/util/Entry;

    invoke-direct {p0, v1, v4}, Lcpdetector/util/Entry;-><init>(Ljava/lang/Object;Ljava/lang/Object;)V

    return-object p0
.end method

.method public static cutExtension(Ljava/lang/String;)Ljava/util/Map$Entry;
    .locals 5
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "(",
            "Ljava/lang/String;",
            ")",
            "Ljava/util/Map$Entry<",
            "Ljava/lang/String;",
            "Ljava/lang/String;",
            ">;"
        }
    .end annotation

    new-instance v0, Ljava/util/StringTokenizer;

    const-string v1, "."

    invoke-direct {v0, p0, v1}, Ljava/util/StringTokenizer;-><init>(Ljava/lang/String;Ljava/lang/String;)V

    invoke-virtual {v0}, Ljava/util/StringTokenizer;->countTokens()I

    move-result v2

    const/4 v3, 0x1

    if-le v2, v3, :cond_2

    new-instance p0, Ljava/lang/StringBuffer;

    invoke-direct {p0}, Ljava/lang/StringBuffer;-><init>()V

    :cond_0
    :goto_0
    if-le v2, v3, :cond_1

    add-int/lit8 v2, v2, -0x1

    invoke-virtual {v0}, Ljava/util/StringTokenizer;->nextToken()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {p0, v4}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    if-le v2, v3, :cond_0

    invoke-virtual {p0, v1}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    goto :goto_0

    :cond_1
    invoke-virtual {p0}, Ljava/lang/StringBuffer;->toString()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {v0}, Ljava/util/StringTokenizer;->nextToken()Ljava/lang/String;

    move-result-object v0

    goto :goto_1

    :cond_2
    const-string v0, ""

    :goto_1
    new-instance v1, Lcpdetector/util/Entry;

    invoke-direct {v1, p0, v0}, Lcpdetector/util/Entry;-><init>(Ljava/lang/Object;Ljava/lang/Object;)V

    return-object v1
.end method

.method public static getDefaultFileName(Ljava/lang/String;)Ljava/lang/String;
    .locals 5

    new-instance v0, Ljava/io/File;

    invoke-direct {v0, p0}, Ljava/io/File;-><init>(Ljava/lang/String;)V

    invoke-virtual {v0}, Ljava/io/File;->exists()Z

    move-result v1

    if-nez v1, :cond_0

    goto :goto_1

    :cond_0
    invoke-static {p0}, Lcpdetector/util/FileUtil;->cutExtension(Ljava/lang/String;)Ljava/util/Map$Entry;

    move-result-object p0

    invoke-interface {p0}, Ljava/util/Map$Entry;->getKey()Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Ljava/lang/String;

    invoke-interface {p0}, Ljava/util/Map$Entry;->getValue()Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Ljava/lang/String;

    const/4 v2, 0x0

    :goto_0
    invoke-virtual {v0}, Ljava/io/File;->exists()Z

    move-result v3

    if-eqz v3, :cond_1

    new-instance v0, Ljava/io/File;

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v3, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const/16 v4, 0x5f

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(C)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const/16 v4, 0x2e

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(C)Ljava/lang/StringBuilder;

    invoke-virtual {v3, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-direct {v0, v3}, Ljava/io/File;-><init>(Ljava/lang/String;)V

    add-int/lit8 v2, v2, 0x1

    goto :goto_0

    :cond_1
    :goto_1
    invoke-virtual {v0}, Ljava/io/File;->getAbsolutePath()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public static getInstance()Lcpdetector/util/FileUtil;
    .locals 1

    sget-object v0, Lcpdetector/util/FileUtil;->instance:Lcpdetector/util/FileUtil;

    if-nez v0, :cond_0

    new-instance v0, Lcpdetector/util/FileUtil;

    invoke-direct {v0}, Lcpdetector/util/FileUtil;-><init>()V

    sput-object v0, Lcpdetector/util/FileUtil;->instance:Lcpdetector/util/FileUtil;

    :cond_0
    sget-object v0, Lcpdetector/util/FileUtil;->instance:Lcpdetector/util/FileUtil;

    return-object v0
.end method

.method public static isAllASCII(Ljava/io/File;)Z
    .locals 1

    new-instance v0, Ljava/io/FileInputStream;

    invoke-direct {v0, p0}, Ljava/io/FileInputStream;-><init>(Ljava/io/File;)V

    invoke-static {v0}, Lcpdetector/util/FileUtil;->isAllASCII(Ljava/io/InputStream;)Z

    move-result p0

    return p0
.end method

.method public static isAllASCII(Ljava/io/InputStream;)Z
    .locals 2

    :cond_0
    invoke-virtual {p0}, Ljava/io/InputStream;->read()I

    move-result v0

    const/16 v1, 0x7f

    if-le v0, v1, :cond_1

    const/4 p0, 0x0

    goto :goto_0

    :cond_1
    const/4 v1, -0x1

    if-ne v0, v1, :cond_0

    const/4 p0, 0x1

    :goto_0
    return p0
.end method

.method public static isEqual(Ljava/io/File;Ljava/nio/charset/Charset;Ljava/nio/charset/Charset;)Z
    .locals 3

    const/4 v0, 0x0

    :try_start_0
    new-instance v1, Ljava/io/FileInputStream;

    invoke-direct {v1, p0}, Ljava/io/FileInputStream;-><init>(Ljava/io/File;)V

    new-instance v2, Ljava/io/FileInputStream;

    invoke-direct {v2, p0}, Ljava/io/FileInputStream;-><init>(Ljava/io/File;)V

    new-instance p0, Ljava/io/InputStreamReader;

    invoke-direct {p0, v1, p1}, Ljava/io/InputStreamReader;-><init>(Ljava/io/InputStream;Ljava/nio/charset/Charset;)V
    :try_end_0
    .catchall {:try_start_0 .. :try_end_0} :catchall_2

    :try_start_1
    new-instance p1, Ljava/io/InputStreamReader;

    invoke-direct {p1, v2, p2}, Ljava/io/InputStreamReader;-><init>(Ljava/io/InputStream;Ljava/nio/charset/Charset;)V
    :try_end_1
    .catchall {:try_start_1 .. :try_end_1} :catchall_1

    :cond_0
    :try_start_2
    invoke-virtual {p0}, Ljava/io/InputStreamReader;->read()I

    move-result p2

    invoke-virtual {p1}, Ljava/io/InputStreamReader;->read()I

    move-result v0
    :try_end_2
    .catchall {:try_start_2 .. :try_end_2} :catchall_0

    if-eq p2, v0, :cond_1

    const/4 p2, 0x0

    goto :goto_0

    :cond_1
    const/4 v1, -0x1

    if-eq p2, v1, :cond_2

    if-ne v0, v1, :cond_0

    :cond_2
    const/4 p2, 0x1

    :goto_0
    invoke-virtual {p0}, Ljava/io/InputStreamReader;->close()V

    invoke-virtual {p1}, Ljava/io/InputStreamReader;->close()V

    return p2

    :catchall_0
    move-exception p2

    move-object v0, p1

    goto :goto_1

    :catchall_1
    move-exception p1

    move-object p2, p1

    goto :goto_1

    :catchall_2
    move-exception p0

    move-object p2, p0

    move-object p0, v0

    :goto_1
    if-eqz p0, :cond_3

    invoke-virtual {p0}, Ljava/io/InputStreamReader;->close()V

    :cond_3
    if-eqz v0, :cond_4

    invoke-virtual {v0}, Ljava/io/InputStreamReader;->close()V

    :cond_4
    throw p2
.end method

.method public static readCache(Ljava/io/File;)Ljava/io/InputStream;
    .locals 1

    new-instance v0, Ljava/io/ByteArrayInputStream;

    invoke-static {p0}, Lcpdetector/util/FileUtil;->readRAM(Ljava/io/File;)[B

    move-result-object p0

    invoke-direct {v0, p0}, Ljava/io/ByteArrayInputStream;-><init>([B)V

    return-object v0
.end method

.method public static readRAM(Ljava/io/File;)[B
    .locals 5

    invoke-virtual {p0}, Ljava/io/File;->length()J

    move-result-wide v0

    long-to-int v0, v0

    new-array v1, v0, [B

    new-instance v2, Ljava/io/FileInputStream;

    invoke-direct {v2, p0}, Ljava/io/FileInputStream;-><init>(Ljava/io/File;)V

    const/4 p0, 0x0

    move v3, p0

    :cond_0
    sub-int v3, v0, v3

    :try_start_0
    invoke-virtual {v2, v1, p0, v3}, Ljava/io/FileInputStream;->read([BII)I

    move-result v3
    :try_end_0
    .catchall {:try_start_0 .. :try_end_0} :catchall_0

    if-lez v3, :cond_1

    add-int/2addr p0, v3

    :cond_1
    const/4 v4, -0x1

    if-eq v3, v4, :cond_2

    if-ne p0, v0, :cond_0

    :cond_2
    invoke-virtual {v2}, Ljava/io/FileInputStream;->close()V

    return-object v1

    :catchall_0
    move-exception p0

    invoke-virtual {v2}, Ljava/io/FileInputStream;->close()V

    throw p0
.end method

.method public static removeDuplicateLineBreaks(Ljava/io/File;)V
    .locals 10

    const-string v0, ""

    invoke-static {}, Lcpdetector/util/StringUtil;->getNewLine()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0}, Ljava/io/File;->exists()Z

    move-result v2

    const-string v3, "FileUtil.removeDuplicateLineBreak(File f): "

    if-nez v2, :cond_0

    sget-object v0, Ljava/lang/System;->err:Ljava/io/PrintStream;

    invoke-static {v3}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    invoke-virtual {p0}, Ljava/io/File;->getAbsolutePath()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {v1, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p0, " does not exist!"

    :goto_0
    invoke-virtual {v1, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {v0, p0}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    goto/16 :goto_c

    :cond_0
    invoke-virtual {p0}, Ljava/io/File;->isDirectory()Z

    move-result v2

    if-eqz v2, :cond_1

    sget-object v0, Ljava/lang/System;->err:Ljava/io/PrintStream;

    invoke-static {v3}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    invoke-virtual {p0}, Ljava/io/File;->getAbsolutePath()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {v1, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p0, " is a directory!"

    goto :goto_0

    :cond_1
    const/4 v2, 0x0

    :try_start_0
    new-instance v3, Ljava/io/FileInputStream;

    invoke-direct {v3, p0}, Ljava/io/FileInputStream;-><init>(Ljava/io/File;)V

    new-instance v4, Ljava/io/BufferedInputStream;

    const/16 v5, 0x400

    invoke-direct {v4, v3, v5}, Ljava/io/BufferedInputStream;-><init>(Ljava/io/InputStream;I)V
    :try_end_0
    .catch Ljava/io/FileNotFoundException; {:try_start_0 .. :try_end_0} :catch_8
    .catch Ljava/io/IOException; {:try_start_0 .. :try_end_0} :catch_4
    .catchall {:try_start_0 .. :try_end_0} :catchall_2

    :try_start_1
    new-instance v3, Ljava/lang/StringBuffer;

    invoke-direct {v3}, Ljava/lang/StringBuffer;-><init>()V

    :goto_1
    invoke-virtual {v4}, Ljava/io/BufferedInputStream;->read()I

    move-result v5

    const/4 v6, -0x1

    if-eq v5, v6, :cond_2

    int-to-char v5, v5

    invoke-virtual {v3, v5}, Ljava/lang/StringBuffer;->append(C)Ljava/lang/StringBuffer;

    goto :goto_1

    :cond_2
    new-instance v5, Ljava/util/StringTokenizer;

    invoke-virtual {v3}, Ljava/lang/StringBuffer;->toString()Ljava/lang/String;

    move-result-object v3

    const/4 v6, 0x1

    invoke-direct {v5, v3, v1, v6}, Ljava/util/StringTokenizer;-><init>(Ljava/lang/String;Ljava/lang/String;Z)V

    new-instance v3, Ljava/lang/StringBuffer;

    invoke-direct {v3}, Ljava/lang/StringBuffer;-><init>()V

    const/4 v6, 0x0

    move v7, v6

    :goto_2
    invoke-virtual {v5}, Ljava/util/StringTokenizer;->hasMoreTokens()Z

    move-result v8

    if-eqz v8, :cond_5

    invoke-virtual {v5}, Ljava/util/StringTokenizer;->nextToken()Ljava/lang/String;

    move-result-object v8

    invoke-virtual {v8}, Ljava/lang/String;->trim()Ljava/lang/String;

    move-result-object v8

    invoke-virtual {v8, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v9

    if-eqz v9, :cond_3

    if-lez v7, :cond_3

    add-int/lit8 v7, v7, 0x1

    goto :goto_2

    :cond_3
    invoke-virtual {v8, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v9

    if-eqz v9, :cond_4

    add-int/lit8 v7, v7, 0x1

    move-object v8, v1

    goto :goto_3

    :cond_4
    move v7, v6

    :goto_3
    invoke-virtual {v3, v8}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    goto :goto_2

    :cond_5
    invoke-virtual {p0}, Ljava/io/File;->delete()Z

    invoke-virtual {p0}, Ljava/io/File;->createNewFile()Z

    new-instance v0, Ljava/io/FileWriter;

    invoke-direct {v0, p0}, Ljava/io/FileWriter;-><init>(Ljava/io/File;)V
    :try_end_1
    .catch Ljava/io/FileNotFoundException; {:try_start_1 .. :try_end_1} :catch_9
    .catch Ljava/io/IOException; {:try_start_1 .. :try_end_1} :catch_3
    .catchall {:try_start_1 .. :try_end_1} :catchall_1

    :try_start_2
    invoke-virtual {v3}, Ljava/lang/StringBuffer;->toString()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {v0, p0}, Ljava/io/FileWriter;->write(Ljava/lang/String;)V
    :try_end_2
    .catch Ljava/io/FileNotFoundException; {:try_start_2 .. :try_end_2} :catch_2
    .catch Ljava/io/IOException; {:try_start_2 .. :try_end_2} :catch_1
    .catchall {:try_start_2 .. :try_end_2} :catchall_0

    :try_start_3
    invoke-virtual {v4}, Ljava/io/BufferedInputStream;->close()V
    :try_end_3
    .catch Ljava/io/IOException; {:try_start_3 .. :try_end_3} :catch_0

    goto :goto_4

    :catch_0
    move-exception p0

    invoke-virtual {p0}, Ljava/io/IOException;->printStackTrace()V

    :goto_4
    :try_start_4
    invoke-virtual {v0}, Ljava/io/FileWriter;->flush()V

    invoke-virtual {v0}, Ljava/io/FileWriter;->close()V
    :try_end_4
    .catch Ljava/io/IOException; {:try_start_4 .. :try_end_4} :catch_b

    goto/16 :goto_c

    :catchall_0
    move-exception p0

    move-object v2, v0

    goto :goto_7

    :catch_1
    move-exception p0

    move-object v2, v0

    goto :goto_5

    :catch_2
    move-object v2, v0

    goto :goto_a

    :catchall_1
    move-exception p0

    goto :goto_7

    :catch_3
    move-exception p0

    goto :goto_5

    :catchall_2
    move-exception p0

    move-object v4, v2

    goto :goto_7

    :catch_4
    move-exception p0

    move-object v4, v2

    :goto_5
    :try_start_5
    sget-object v0, Ljava/lang/System;->err:Ljava/io/PrintStream;

    invoke-virtual {p0, v0}, Ljava/io/IOException;->printStackTrace(Ljava/io/PrintStream;)V
    :try_end_5
    .catchall {:try_start_5 .. :try_end_5} :catchall_1

    if-eqz v4, :cond_6

    :try_start_6
    invoke-virtual {v4}, Ljava/io/BufferedInputStream;->close()V
    :try_end_6
    .catch Ljava/io/IOException; {:try_start_6 .. :try_end_6} :catch_5

    goto :goto_6

    :catch_5
    move-exception p0

    invoke-virtual {p0}, Ljava/io/IOException;->printStackTrace()V

    :cond_6
    :goto_6
    if-eqz v2, :cond_a

    :try_start_7
    invoke-virtual {v2}, Ljava/io/FileWriter;->flush()V

    invoke-virtual {v2}, Ljava/io/FileWriter;->close()V
    :try_end_7
    .catch Ljava/io/IOException; {:try_start_7 .. :try_end_7} :catch_b

    goto :goto_c

    :goto_7
    if-eqz v4, :cond_7

    :try_start_8
    invoke-virtual {v4}, Ljava/io/BufferedInputStream;->close()V
    :try_end_8
    .catch Ljava/io/IOException; {:try_start_8 .. :try_end_8} :catch_6

    goto :goto_8

    :catch_6
    move-exception v0

    invoke-virtual {v0}, Ljava/io/IOException;->printStackTrace()V

    :cond_7
    :goto_8
    if-eqz v2, :cond_8

    :try_start_9
    invoke-virtual {v2}, Ljava/io/FileWriter;->flush()V

    invoke-virtual {v2}, Ljava/io/FileWriter;->close()V
    :try_end_9
    .catch Ljava/io/IOException; {:try_start_9 .. :try_end_9} :catch_7

    goto :goto_9

    :catch_7
    move-exception v0

    invoke-virtual {v0}, Ljava/io/IOException;->printStackTrace()V

    :cond_8
    :goto_9
    throw p0

    :catch_8
    move-object v4, v2

    :catch_9
    :goto_a
    if-eqz v4, :cond_9

    :try_start_a
    invoke-virtual {v4}, Ljava/io/BufferedInputStream;->close()V
    :try_end_a
    .catch Ljava/io/IOException; {:try_start_a .. :try_end_a} :catch_a

    goto :goto_b

    :catch_a
    move-exception p0

    invoke-virtual {p0}, Ljava/io/IOException;->printStackTrace()V

    :cond_9
    :goto_b
    if-eqz v2, :cond_a

    :try_start_b
    invoke-virtual {v2}, Ljava/io/FileWriter;->flush()V

    invoke-virtual {v2}, Ljava/io/FileWriter;->close()V
    :try_end_b
    .catch Ljava/io/IOException; {:try_start_b .. :try_end_b} :catch_b

    goto :goto_c

    :catch_b
    move-exception p0

    invoke-virtual {p0}, Ljava/io/IOException;->printStackTrace()V

    :cond_a
    :goto_c
    return-void
.end method


# virtual methods
.method public equals(Ljava/lang/Object;)Z
    .locals 4

    const/4 v0, 0x1

    if-ne p0, p1, :cond_0

    return v0

    :cond_0
    const/4 v1, 0x0

    if-nez p1, :cond_1

    return v1

    :cond_1
    invoke-virtual {p1}, Ljava/lang/Object;->getClass()Ljava/lang/Class;

    move-result-object v2

    const-class v3, Lcpdetector/util/FileUtil;

    if-eq v3, v2, :cond_2

    return v1

    :cond_2
    check-cast p1, Lcpdetector/util/FileUtil;

    iget-object p0, p0, Lcpdetector/util/FileUtil;->m_bundle:Ljava/util/ResourceBundle;

    if-nez p0, :cond_3

    iget-object p0, p1, Lcpdetector/util/FileUtil;->m_bundle:Ljava/util/ResourceBundle;

    if-eqz p0, :cond_4

    return v1

    :cond_3
    iget-object p1, p1, Lcpdetector/util/FileUtil;->m_bundle:Ljava/util/ResourceBundle;

    invoke-virtual {p0, p1}, Ljava/lang/Object;->equals(Ljava/lang/Object;)Z

    move-result p0

    if-nez p0, :cond_4

    return v1

    :cond_4
    return v0
.end method

.method public formatFilesize(JLjava/util/Locale;)Ljava/lang/String;
    .locals 6

    invoke-static {p1, p2}, Ljava/lang/Math;->abs(J)J

    move-result-wide v0

    invoke-static {p1, p2}, Ljava/lang/Math;->abs(J)J

    move-result-wide v2

    const-wide/16 v4, 0x400

    cmp-long p3, v2, v4

    const/4 v2, 0x0

    const/4 v3, 0x1

    if-gez p3, :cond_0

    iget-object p0, p0, Lcpdetector/util/FileUtil;->m_bundle:Ljava/util/ResourceBundle;

    const-string p1, "GUI_FILEUTIL_FILESIZE_BYTES_1"

    invoke-virtual {p0, p1}, Ljava/util/ResourceBundle;->getString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object p0

    new-array p1, v3, [Ljava/lang/Object;

    new-instance p2, Ljava/lang/Long;

    invoke-direct {p2, v0, v1}, Ljava/lang/Long;-><init>(J)V

    aput-object p2, p1, v2

    :goto_0
    invoke-static {p0, p1}, Ljava/text/MessageFormat;->format(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;

    move-result-object p0

    goto :goto_1

    :cond_0
    const-wide/32 v4, 0x100000

    cmp-long p3, v0, v4

    if-gez p3, :cond_1

    iget-object p0, p0, Lcpdetector/util/FileUtil;->m_bundle:Ljava/util/ResourceBundle;

    const-string p1, "GUI_FILEUTIL_FILESIZE_KBYTES_1"

    invoke-virtual {p0, p1}, Ljava/util/ResourceBundle;->getString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object p0

    new-array p1, v3, [Ljava/lang/Object;

    new-instance p2, Ljava/lang/Double;

    long-to-double v0, v0

    const-wide/high16 v3, 0x4090000000000000L    # 1024.0

    div-double/2addr v0, v3

    invoke-direct {p2, v0, v1}, Ljava/lang/Double;-><init>(D)V

    aput-object p2, p1, v2

    goto :goto_0

    :cond_1
    const-wide/32 v4, 0x40000000

    cmp-long p3, v0, v4

    iget-object p0, p0, Lcpdetector/util/FileUtil;->m_bundle:Ljava/util/ResourceBundle;

    if-gez p3, :cond_2

    const-string p3, "GUI_FILEUTIL_FILESIZE_MBYTES_1"

    invoke-virtual {p0, p3}, Ljava/util/ResourceBundle;->getString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object p0

    new-array p3, v3, [Ljava/lang/Object;

    new-instance v0, Ljava/lang/Double;

    long-to-double p1, p1

    const-wide/high16 v3, 0x4130000000000000L    # 1048576.0

    div-double/2addr p1, v3

    invoke-direct {v0, p1, p2}, Ljava/lang/Double;-><init>(D)V

    aput-object v0, p3, v2

    invoke-static {p0, p3}, Ljava/text/MessageFormat;->format(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;

    move-result-object p0

    goto :goto_1

    :cond_2
    const-string p1, "GUI_FILEUTIL_FILESIZE_GBYTES_1"

    invoke-virtual {p0, p1}, Ljava/util/ResourceBundle;->getString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object p0

    new-array p1, v3, [Ljava/lang/Object;

    new-instance p2, Ljava/lang/Double;

    long-to-double v0, v0

    const-wide/high16 v3, 0x41d0000000000000L    # 1.073741824E9

    div-double/2addr v0, v3

    invoke-direct {p2, v0, v1}, Ljava/lang/Double;-><init>(D)V

    aput-object p2, p1, v2

    goto :goto_0

    :goto_1
    return-object p0
.end method

.method public hashCode()I
    .locals 0

    iget-object p0, p0, Lcpdetector/util/FileUtil;->m_bundle:Ljava/util/ResourceBundle;

    if-nez p0, :cond_0

    const/4 p0, 0x0

    goto :goto_0

    :cond_0
    invoke-virtual {p0}, Ljava/lang/Object;->hashCode()I

    move-result p0

    :goto_0
    add-int/lit8 p0, p0, 0x1f

    return p0
.end method
