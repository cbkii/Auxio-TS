.class Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$4;
.super Ljava/lang/Object;
.source "TWMediaPlayerView.java"

# interfaces
.implements Ltv/danmaku/ijk/media/player/IMediaPlayer$OnInfoListener;


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
    iput-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$4;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public onInfo(Ltv/danmaku/ijk/media/player/IMediaPlayer;II)Z
    .locals 1

    .line 1
    iget-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$4;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {v0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$1600(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)Ltv/danmaku/ijk/media/player/IMediaPlayer$OnInfoListener;

    move-result-object v0

    if-eqz v0, :cond_0

    .line 2
    iget-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$4;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {v0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$1600(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)Ltv/danmaku/ijk/media/player/IMediaPlayer$OnInfoListener;

    move-result-object v0

    invoke-interface {v0, p1, p2, p3}, Ltv/danmaku/ijk/media/player/IMediaPlayer$OnInfoListener;->onInfo(Ltv/danmaku/ijk/media/player/IMediaPlayer;II)Z

    :cond_0
    const/4 p1, 0x3

    if-eq p2, p1, :cond_5

    const/16 p1, 0x385

    if-eq p2, p1, :cond_4

    const/16 p1, 0x386

    if-eq p2, p1, :cond_3

    const/16 p1, 0x2711

    if-eq p2, p1, :cond_2

    const/16 p1, 0x2712

    if-eq p2, p1, :cond_1

    packed-switch p2, :pswitch_data_0

    packed-switch p2, :pswitch_data_1

    goto/16 :goto_0

    .line 3
    :pswitch_0
    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$4;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$600(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)Ljava/lang/String;

    move-result-object p0

    const-string p1, "MEDIA_INFO_METADATA_UPDATE:"

    invoke-static {p0, p1}, Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    goto/16 :goto_0

    .line 4
    :pswitch_1
    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$4;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$600(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)Ljava/lang/String;

    move-result-object p0

    const-string p1, "MEDIA_INFO_NOT_SEEKABLE:"

    invoke-static {p0, p1}, Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    goto/16 :goto_0

    .line 5
    :pswitch_2
    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$4;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$600(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)Ljava/lang/String;

    move-result-object p0

    const-string p1, "MEDIA_INFO_BAD_INTERLEAVING:"

    invoke-static {p0, p1}, Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    goto/16 :goto_0

    .line 6
    :pswitch_3
    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$4;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$600(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)Ljava/lang/String;

    move-result-object p0

    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    const-string p2, "MEDIA_INFO_NETWORK_BANDWIDTH: "

    invoke-virtual {p1, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1, p3}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-static {p0, p1}, Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    goto/16 :goto_0

    .line 7
    :pswitch_4
    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$4;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$600(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)Ljava/lang/String;

    move-result-object p0

    const-string p1, "MEDIA_INFO_BUFFERING_END:"

    invoke-static {p0, p1}, Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    goto/16 :goto_0

    .line 8
    :pswitch_5
    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$4;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$600(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)Ljava/lang/String;

    move-result-object p0

    const-string p1, "MEDIA_INFO_BUFFERING_START:"

    invoke-static {p0, p1}, Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    goto :goto_0

    .line 9
    :pswitch_6
    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$4;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$600(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)Ljava/lang/String;

    move-result-object p0

    const-string p1, "MEDIA_INFO_VIDEO_TRACK_LAGGING:"

    invoke-static {p0, p1}, Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    goto :goto_0

    .line 10
    :cond_1
    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$4;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$600(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)Ljava/lang/String;

    move-result-object p0

    const-string p1, "MEDIA_INFO_AUDIO_RENDERING_START:"

    invoke-static {p0, p1}, Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    goto :goto_0

    .line 11
    :cond_2
    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$4;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {p1, p3}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$1702(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;I)I

    .line 12
    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$4;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {p1}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$600(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)Ljava/lang/String;

    move-result-object p1

    new-instance p2, Ljava/lang/StringBuilder;

    invoke-direct {p2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v0, "MEDIA_INFO_VIDEO_ROTATION_CHANGED: "

    invoke-virtual {p2, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p2, p3}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {p2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p2

    invoke-static {p1, p2}, Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    .line 13
    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$4;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {p1}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$400(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)Ltv/danmaku/ijk/media/player/tw/IRenderView;

    move-result-object p1

    if-eqz p1, :cond_6

    .line 14
    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$4;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$400(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)Ltv/danmaku/ijk/media/player/tw/IRenderView;

    move-result-object p0

    invoke-interface {p0, p3}, Ltv/danmaku/ijk/media/player/tw/IRenderView;->setVideoRotation(I)V

    goto :goto_0

    .line 15
    :cond_3
    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$4;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$600(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)Ljava/lang/String;

    move-result-object p0

    const-string p1, "MEDIA_INFO_SUBTITLE_TIMED_OUT:"

    invoke-static {p0, p1}, Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    goto :goto_0

    .line 16
    :cond_4
    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$4;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$600(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)Ljava/lang/String;

    move-result-object p0

    const-string p1, "MEDIA_INFO_UNSUPPORTED_SUBTITLE:"

    invoke-static {p0, p1}, Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    goto :goto_0

    .line 17
    :cond_5
    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$4;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$600(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)Ljava/lang/String;

    move-result-object p0

    const-string p1, "MEDIA_INFO_VIDEO_RENDERING_START:"

    invoke-static {p0, p1}, Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    :cond_6
    :goto_0
    const/4 p0, 0x1

    return p0

    :pswitch_data_0
    .packed-switch 0x2bc
        :pswitch_6
        :pswitch_5
        :pswitch_4
        :pswitch_3
    .end packed-switch

    :pswitch_data_1
    .packed-switch 0x320
        :pswitch_2
        :pswitch_1
        :pswitch_0
    .end packed-switch
.end method
