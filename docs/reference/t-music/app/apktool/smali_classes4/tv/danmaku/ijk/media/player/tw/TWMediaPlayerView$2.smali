.class Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$2;
.super Ljava/lang/Object;
.source "TWMediaPlayerView.java"

# interfaces
.implements Ltv/danmaku/ijk/media/player/IMediaPlayer$OnPreparedListener;


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
    iput-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$2;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public onPrepared(Ltv/danmaku/ijk/media/player/IMediaPlayer;)V
    .locals 6

    .line 1
    iget-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$2;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {}, Ljava/lang/System;->currentTimeMillis()J

    move-result-wide v1

    invoke-static {v0, v1, v2}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$502(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;J)J

    .line 2
    iget-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$2;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {v0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$600(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)Ljava/lang/String;

    move-result-object v0

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "LoadCost="

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v2, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$2;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {v2}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$500(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)J

    move-result-wide v2

    iget-object v4, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$2;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {v4}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$700(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)J

    move-result-wide v4

    sub-long/2addr v2, v4

    invoke-virtual {v1, v2, v3}, Ljava/lang/StringBuilder;->append(J)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-static {v0, v1}, Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    .line 3
    iget-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$2;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    const/4 v1, 0x2

    invoke-static {v0, v1}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$802(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;I)I

    .line 4
    iget-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$2;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {v0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$900(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)Ltv/danmaku/ijk/media/player/IMediaPlayer$OnPreparedListener;

    move-result-object v0

    if-eqz v0, :cond_0

    .line 5
    iget-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$2;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {v0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$900(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)Ltv/danmaku/ijk/media/player/IMediaPlayer$OnPreparedListener;

    move-result-object v0

    iget-object v1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$2;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {v1}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$1000(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)Ltv/danmaku/ijk/media/player/IMediaPlayer;

    move-result-object v1

    invoke-interface {v0, v1}, Ltv/danmaku/ijk/media/player/IMediaPlayer$OnPreparedListener;->onPrepared(Ltv/danmaku/ijk/media/player/IMediaPlayer;)V

    .line 6
    :cond_0
    iget-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$2;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-interface {p1}, Ltv/danmaku/ijk/media/player/IMediaPlayer;->getVideoWidth()I

    move-result v1

    invoke-static {v0, v1}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$002(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;I)I

    .line 7
    iget-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$2;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-interface {p1}, Ltv/danmaku/ijk/media/player/IMediaPlayer;->getVideoHeight()I

    move-result p1

    invoke-static {v0, p1}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$102(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;I)I

    .line 8
    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$2;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {p1}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$1100(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)I

    move-result p1

    if-eqz p1, :cond_1

    .line 9
    iget-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$2;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-virtual {v0, p1}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->seekTo(I)V

    .line 10
    :cond_1
    iget-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$2;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {v0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$000(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)I

    move-result v0

    const/4 v1, 0x3

    if-eqz v0, :cond_4

    iget-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$2;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {v0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$100(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)I

    move-result v0

    if-eqz v0, :cond_4

    .line 11
    iget-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$2;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {v0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$400(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)Ltv/danmaku/ijk/media/player/tw/IRenderView;

    move-result-object v0

    if-eqz v0, :cond_5

    .line 12
    iget-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$2;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {v0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$400(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)Ltv/danmaku/ijk/media/player/tw/IRenderView;

    move-result-object v0

    iget-object v2, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$2;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {v2}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$000(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)I

    move-result v2

    iget-object v3, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$2;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {v3}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$100(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)I

    move-result v3

    invoke-interface {v0, v2, v3}, Ltv/danmaku/ijk/media/player/tw/IRenderView;->setVideoSize(II)V

    .line 13
    iget-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$2;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {v0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$400(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)Ltv/danmaku/ijk/media/player/tw/IRenderView;

    move-result-object v0

    iget-object v2, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$2;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {v2}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$200(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)I

    move-result v2

    iget-object v3, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$2;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {v3}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$300(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)I

    move-result v3

    invoke-interface {v0, v2, v3}, Ltv/danmaku/ijk/media/player/tw/IRenderView;->setVideoSampleAspectRatio(II)V

    .line 14
    iget-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$2;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {v0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$400(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)Ltv/danmaku/ijk/media/player/tw/IRenderView;

    move-result-object v0

    invoke-interface {v0}, Ltv/danmaku/ijk/media/player/tw/IRenderView;->shouldWaitForResize()Z

    move-result v0

    if-eqz v0, :cond_2

    iget-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$2;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {v0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$1200(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)I

    move-result v0

    iget-object v2, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$2;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {v2}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$000(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)I

    move-result v2

    if-ne v0, v2, :cond_5

    iget-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$2;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {v0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$1300(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)I

    move-result v0

    iget-object v2, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$2;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {v2}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$100(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)I

    move-result v2

    if-ne v0, v2, :cond_5

    .line 15
    :cond_2
    iget-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$2;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {v0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$1400(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)I

    move-result v0

    if-ne v0, v1, :cond_3

    .line 16
    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$2;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-virtual {p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->start()V

    goto :goto_0

    .line 17
    :cond_3
    iget-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$2;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-virtual {v0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->isPlaying()Z

    move-result v0

    if-nez v0, :cond_5

    if-nez p1, :cond_5

    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$2;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    .line 18
    invoke-virtual {p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->getCurrentPosition()I

    move-result p0

    goto :goto_0

    .line 19
    :cond_4
    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$2;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {p1}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$1400(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)I

    move-result p1

    if-ne p1, v1, :cond_5

    .line 20
    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$2;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-virtual {p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->start()V

    :cond_5
    :goto_0
    return-void
.end method
