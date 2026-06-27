.class public Lantlr/PreservingFileWriter;
.super Ljava/io/FileWriter;
.source ""


# instance fields
.field public target_file:Ljava/io/File;

.field public tmp_file:Ljava/io/File;


# direct methods
.method public constructor <init>(Ljava/lang/String;)V
    .locals 4

    const-string v0, ".antlr.tmp"

    invoke-static {p1, v0}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    invoke-direct {p0, v1}, Ljava/io/FileWriter;-><init>(Ljava/lang/String;)V

    new-instance v1, Ljava/io/File;

    invoke-direct {v1, p1}, Ljava/io/File;-><init>(Ljava/lang/String;)V

    iput-object v1, p0, Lantlr/PreservingFileWriter;->target_file:Ljava/io/File;

    iget-object v1, p0, Lantlr/PreservingFileWriter;->target_file:Ljava/io/File;

    invoke-virtual {v1}, Ljava/io/File;->getParent()Ljava/lang/String;

    move-result-object v1

    if-eqz v1, :cond_2

    new-instance v2, Ljava/io/File;

    invoke-direct {v2, v1}, Ljava/io/File;-><init>(Ljava/lang/String;)V

    invoke-virtual {v2}, Ljava/io/File;->exists()Z

    move-result v1

    const-string v3, "destination directory of \'"

    if-eqz v1, :cond_1

    invoke-virtual {v2}, Ljava/io/File;->canWrite()Z

    move-result v1

    if-eqz v1, :cond_0

    goto :goto_0

    :cond_0
    new-instance p0, Ljava/io/IOException;

    const-string v0, "\' isn\'t writeable"

    invoke-static {v3, p1, v0}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p1

    invoke-direct {p0, p1}, Ljava/io/IOException;-><init>(Ljava/lang/String;)V

    throw p0

    :cond_1
    new-instance p0, Ljava/io/IOException;

    const-string v0, "\' doesn\'t exist"

    invoke-static {v3, p1, v0}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p1

    invoke-direct {p0, p1}, Ljava/io/IOException;-><init>(Ljava/lang/String;)V

    throw p0

    :cond_2
    :goto_0
    iget-object v1, p0, Lantlr/PreservingFileWriter;->target_file:Ljava/io/File;

    invoke-virtual {v1}, Ljava/io/File;->exists()Z

    move-result v1

    if-eqz v1, :cond_4

    iget-object v1, p0, Lantlr/PreservingFileWriter;->target_file:Ljava/io/File;

    invoke-virtual {v1}, Ljava/io/File;->canWrite()Z

    move-result v1

    if-eqz v1, :cond_3

    goto :goto_1

    :cond_3
    new-instance p0, Ljava/io/IOException;

    const-string v0, "cannot write to \'"

    const-string v1, "\'"

    invoke-static {v0, p1, v1}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p1

    invoke-direct {p0, p1}, Ljava/io/IOException;-><init>(Ljava/lang/String;)V

    throw p0

    :cond_4
    :goto_1
    new-instance v1, Ljava/io/File;

    invoke-static {p1, v0}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p1

    invoke-direct {v1, p1}, Ljava/io/File;-><init>(Ljava/lang/String;)V

    iput-object v1, p0, Lantlr/PreservingFileWriter;->tmp_file:Ljava/io/File;

    return-void
.end method


# virtual methods
.method public close()V
    .locals 13

    const/4 v0, 0x0

    :try_start_0
    invoke-super {p0}, Ljava/io/FileWriter;->close()V

    const/16 v1, 0x400

    new-array v2, v1, [C

    iget-object v3, p0, Lantlr/PreservingFileWriter;->target_file:Ljava/io/File;

    invoke-virtual {v3}, Ljava/io/File;->length()J

    move-result-wide v3

    iget-object v5, p0, Lantlr/PreservingFileWriter;->tmp_file:Ljava/io/File;

    invoke-virtual {v5}, Ljava/io/File;->length()J

    move-result-wide v5

    cmp-long v3, v3, v5

    const/4 v4, -0x1

    const/4 v5, 0x0

    if-nez v3, :cond_6

    new-array v3, v1, [C

    new-instance v6, Ljava/io/BufferedReader;

    new-instance v7, Ljava/io/FileReader;

    iget-object v8, p0, Lantlr/PreservingFileWriter;->tmp_file:Ljava/io/File;

    invoke-direct {v7, v8}, Ljava/io/FileReader;-><init>(Ljava/io/File;)V

    invoke-direct {v6, v7}, Ljava/io/BufferedReader;-><init>(Ljava/io/Reader;)V
    :try_end_0
    .catchall {:try_start_0 .. :try_end_0} :catchall_4

    :try_start_1
    new-instance v7, Ljava/io/BufferedReader;

    new-instance v8, Ljava/io/FileReader;

    iget-object v9, p0, Lantlr/PreservingFileWriter;->target_file:Ljava/io/File;

    invoke-direct {v8, v9}, Ljava/io/FileReader;-><init>(Ljava/io/File;)V

    invoke-direct {v7, v8}, Ljava/io/BufferedReader;-><init>(Ljava/io/Reader;)V

    const/4 v8, 0x1

    :cond_0
    :goto_0
    if-eqz v8, :cond_4

    invoke-virtual {v6, v2, v5, v1}, Ljava/io/BufferedReader;->read([CII)I

    move-result v9

    invoke-virtual {v7, v3, v5, v1}, Ljava/io/BufferedReader;->read([CII)I

    move-result v10

    if-eq v9, v10, :cond_1

    move v8, v5

    goto :goto_2

    :cond_1
    if-ne v9, v4, :cond_2

    goto :goto_2

    :cond_2
    move v10, v5

    :goto_1
    if-ge v10, v9, :cond_0

    aget-char v11, v2, v10

    aget-char v12, v3, v10

    if-eq v11, v12, :cond_3

    move v8, v5

    goto :goto_0

    :cond_3
    add-int/lit8 v10, v10, 0x1

    goto :goto_1

    :cond_4
    :goto_2
    invoke-virtual {v6}, Ljava/io/BufferedReader;->close()V

    invoke-virtual {v7}, Ljava/io/BufferedReader;->close()V
    :try_end_1
    .catchall {:try_start_1 .. :try_end_1} :catchall_0

    if-eqz v8, :cond_6

    iget-object v1, p0, Lantlr/PreservingFileWriter;->tmp_file:Ljava/io/File;

    if-eqz v1, :cond_5

    invoke-virtual {v1}, Ljava/io/File;->exists()Z

    move-result v1

    if-eqz v1, :cond_5

    iget-object v1, p0, Lantlr/PreservingFileWriter;->tmp_file:Ljava/io/File;

    invoke-virtual {v1}, Ljava/io/File;->delete()Z

    iput-object v0, p0, Lantlr/PreservingFileWriter;->tmp_file:Ljava/io/File;

    :cond_5
    return-void

    :catchall_0
    move-exception v1

    move-object v3, v6

    goto :goto_4

    :cond_6
    :try_start_2
    new-instance v3, Ljava/io/BufferedReader;

    new-instance v6, Ljava/io/FileReader;

    iget-object v7, p0, Lantlr/PreservingFileWriter;->tmp_file:Ljava/io/File;

    invoke-direct {v6, v7}, Ljava/io/FileReader;-><init>(Ljava/io/File;)V

    invoke-direct {v3, v6}, Ljava/io/BufferedReader;-><init>(Ljava/io/Reader;)V
    :try_end_2
    .catchall {:try_start_2 .. :try_end_2} :catchall_3

    :try_start_3
    new-instance v6, Ljava/io/BufferedWriter;

    new-instance v7, Ljava/io/FileWriter;

    iget-object v8, p0, Lantlr/PreservingFileWriter;->target_file:Ljava/io/File;

    invoke-direct {v7, v8}, Ljava/io/FileWriter;-><init>(Ljava/io/File;)V

    invoke-direct {v6, v7}, Ljava/io/BufferedWriter;-><init>(Ljava/io/Writer;)V
    :try_end_3
    .catchall {:try_start_3 .. :try_end_3} :catchall_2

    :goto_3
    :try_start_4
    invoke-virtual {v3, v2, v5, v1}, Ljava/io/BufferedReader;->read([CII)I

    move-result v7
    :try_end_4
    .catchall {:try_start_4 .. :try_end_4} :catchall_1

    if-ne v7, v4, :cond_8

    :try_start_5
    invoke-virtual {v3}, Ljava/io/BufferedReader;->close()V
    :try_end_5
    .catch Ljava/io/IOException; {:try_start_5 .. :try_end_5} :catch_0

    :catch_0
    :try_start_6
    invoke-virtual {v6}, Ljava/io/BufferedWriter;->close()V
    :try_end_6
    .catch Ljava/io/IOException; {:try_start_6 .. :try_end_6} :catch_1

    :catch_1
    iget-object v1, p0, Lantlr/PreservingFileWriter;->tmp_file:Ljava/io/File;

    if-eqz v1, :cond_7

    invoke-virtual {v1}, Ljava/io/File;->exists()Z

    move-result v1

    if-eqz v1, :cond_7

    iget-object v1, p0, Lantlr/PreservingFileWriter;->tmp_file:Ljava/io/File;

    invoke-virtual {v1}, Ljava/io/File;->delete()Z

    iput-object v0, p0, Lantlr/PreservingFileWriter;->tmp_file:Ljava/io/File;

    :cond_7
    return-void

    :cond_8
    :try_start_7
    invoke-virtual {v6, v2, v5, v7}, Ljava/io/BufferedWriter;->write([CII)V
    :try_end_7
    .catchall {:try_start_7 .. :try_end_7} :catchall_1

    goto :goto_3

    :catchall_1
    move-exception v1

    goto :goto_5

    :catchall_2
    move-exception v1

    goto :goto_4

    :catchall_3
    move-exception v1

    move-object v3, v0

    move-object v6, v3

    goto :goto_5

    :catchall_4
    move-exception v1

    move-object v3, v0

    :goto_4
    move-object v6, v0

    :goto_5
    if-eqz v3, :cond_9

    :try_start_8
    invoke-virtual {v3}, Ljava/io/BufferedReader;->close()V
    :try_end_8
    .catch Ljava/io/IOException; {:try_start_8 .. :try_end_8} :catch_2

    :catch_2
    :cond_9
    if-eqz v6, :cond_a

    :try_start_9
    invoke-virtual {v6}, Ljava/io/BufferedWriter;->close()V
    :try_end_9
    .catch Ljava/io/IOException; {:try_start_9 .. :try_end_9} :catch_3

    :catch_3
    :cond_a
    iget-object v2, p0, Lantlr/PreservingFileWriter;->tmp_file:Ljava/io/File;

    if-eqz v2, :cond_b

    invoke-virtual {v2}, Ljava/io/File;->exists()Z

    move-result v2

    if-eqz v2, :cond_b

    iget-object v2, p0, Lantlr/PreservingFileWriter;->tmp_file:Ljava/io/File;

    invoke-virtual {v2}, Ljava/io/File;->delete()Z

    iput-object v0, p0, Lantlr/PreservingFileWriter;->tmp_file:Ljava/io/File;

    :cond_b
    throw v1
.end method
