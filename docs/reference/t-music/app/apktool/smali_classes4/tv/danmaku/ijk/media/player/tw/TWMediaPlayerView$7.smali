.class Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$7;
.super Ljava/lang/Object;
.source "TWMediaPlayerView.java"

# interfaces
.implements Ltv/danmaku/ijk/media/player/IMediaPlayer$OnSeekCompleteListener;


# annotations
.annotation system Ldalvik/annotation/EnclosingClass;
    value = Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;
.end annotation

.annotation system Ldalvik/annotation/InnerClass;
    accessFlags = 0x0
    name = null
.end annotation


# instance fields
.field final synthetic this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;


# direct methods
.method constructor <init>(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)V
    .locals 0

    .line 1
    iput-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$7;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public onSeekComplete(Ltv/danmaku/ijk/media/player/IMediaPlayer;)V
    .locals 5

    .line 1
    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$7;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {}, Ljava/lang/System;->currentTimeMillis()J

    move-result-wide v0

    invoke-static {p1, v0, v1}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$2002(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;J)J

    .line 2
    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$7;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {p1}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$600(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)Ljava/lang/String;

    move-result-object p1

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "SeekCost="

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$7;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {v1}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$2000(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)J

    move-result-wide v1

    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$7;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$2100(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)J

    move-result-wide v3

    sub-long/2addr v1, v3

    invoke-virtual {v0, v1, v2}, Ljava/lang/StringBuilder;->append(J)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    invoke-static {p1, p0}, Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    return-void
.end method
