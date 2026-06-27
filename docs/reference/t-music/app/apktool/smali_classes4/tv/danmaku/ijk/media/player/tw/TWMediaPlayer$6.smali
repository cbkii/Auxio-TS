.class Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer$6;
.super Ljava/lang/Object;
.source "TWMediaPlayer.java"

# interfaces
.implements Ltv/danmaku/ijk/media/player/IMediaPlayer$OnSeekCompleteListener;


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
    iput-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer$6;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public onSeekComplete(Ltv/danmaku/ijk/media/player/IMediaPlayer;)V
    .locals 5

    .line 1
    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer$6;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;

    invoke-static {}, Ljava/lang/System;->currentTimeMillis()J

    move-result-wide v0

    invoke-static {p1, v0, v1}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->access$1202(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;J)J

    .line 2
    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer$6;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;

    invoke-static {p1}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->access$100(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;)Ljava/lang/String;

    move-result-object p1

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "SeekCost="

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer$6;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;

    invoke-static {v1}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->access$1200(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;)J

    move-result-wide v1

    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer$6;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;

    invoke-static {p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->access$1300(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;)J

    move-result-wide v3

    sub-long/2addr v1, v3

    invoke-virtual {v0, v1, v2}, Ljava/lang/StringBuilder;->append(J)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    invoke-static {p1, p0}, Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    return-void
.end method
