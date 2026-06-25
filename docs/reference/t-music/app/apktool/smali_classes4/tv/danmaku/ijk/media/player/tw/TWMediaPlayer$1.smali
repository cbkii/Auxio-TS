.class Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer$1;
.super Ljava/lang/Object;
.source "TWMediaPlayer.java"

# interfaces
.implements Ltv/danmaku/ijk/media/player/IMediaPlayer$OnPreparedListener;


# annotations
.annotation system Ldalvik/annotation/EnclosingClass;
    value = Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;
.end annotation

.annotation system Ldalvik/annotation/InnerClass;
    accessFlags = 0x0
    name = null
.end annotation


# instance fields
.field final synthetic this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;


# direct methods
.method constructor <init>(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;)V
    .locals 0

    .line 1
    iput-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer$1;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public onPrepared(Ltv/danmaku/ijk/media/player/IMediaPlayer;)V
    .locals 5

    .line 1
    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer$1;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;

    invoke-static {}, Ljava/lang/System;->currentTimeMillis()J

    move-result-wide v0

    invoke-static {p1, v0, v1}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->access$002(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;J)J

    .line 2
    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer$1;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;

    invoke-static {p1}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->access$100(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;)Ljava/lang/String;

    move-result-object p1

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "LoadCost="

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer$1;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;

    invoke-static {v1}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->access$000(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;)J

    move-result-wide v1

    iget-object v3, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer$1;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;

    invoke-static {v3}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->access$200(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;)J

    move-result-wide v3

    sub-long/2addr v1, v3

    invoke-virtual {v0, v1, v2}, Ljava/lang/StringBuilder;->append(J)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-static {p1, v0}, Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    .line 3
    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer$1;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;

    const/4 v0, 0x2

    invoke-static {p1, v0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->access$302(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;I)I

    .line 4
    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer$1;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;

    invoke-static {p1}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->access$400(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;)Ltv/danmaku/ijk/media/player/IMediaPlayer$OnPreparedListener;

    move-result-object p1

    if-eqz p1, :cond_0

    .line 5
    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer$1;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;

    invoke-static {p1}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->access$400(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;)Ltv/danmaku/ijk/media/player/IMediaPlayer$OnPreparedListener;

    move-result-object p1

    iget-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer$1;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;

    invoke-static {v0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->access$500(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;)Ltv/danmaku/ijk/media/player/IMediaPlayer;

    move-result-object v0

    invoke-interface {p1, v0}, Ltv/danmaku/ijk/media/player/IMediaPlayer$OnPreparedListener;->onPrepared(Ltv/danmaku/ijk/media/player/IMediaPlayer;)V

    .line 6
    :cond_0
    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer$1;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;

    invoke-static {p1}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->access$600(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;)I

    move-result p1

    if-eqz p1, :cond_1

    .line 7
    iget-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer$1;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;

    invoke-virtual {v0, p1}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->seekTo(I)V

    .line 8
    :cond_1
    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer$1;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;

    invoke-static {p1}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->access$700(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;)I

    move-result p1

    const/4 v0, 0x3

    if-ne p1, v0, :cond_2

    .line 9
    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer$1;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;

    invoke-virtual {p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->start()V

    :cond_2
    return-void
.end method
