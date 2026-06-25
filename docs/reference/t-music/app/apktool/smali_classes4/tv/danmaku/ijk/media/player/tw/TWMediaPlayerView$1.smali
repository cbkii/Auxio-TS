.class Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$1;
.super Ljava/lang/Object;
.source "TWMediaPlayerView.java"

# interfaces
.implements Ltv/danmaku/ijk/media/player/IMediaPlayer$OnVideoSizeChangedListener;


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
    iput-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$1;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public onVideoSizeChanged(Ltv/danmaku/ijk/media/player/IMediaPlayer;IIII)V
    .locals 0

    .line 1
    iget-object p2, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$1;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-interface {p1}, Ltv/danmaku/ijk/media/player/IMediaPlayer;->getVideoWidth()I

    move-result p3

    invoke-static {p2, p3}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$002(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;I)I

    .line 2
    iget-object p2, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$1;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-interface {p1}, Ltv/danmaku/ijk/media/player/IMediaPlayer;->getVideoHeight()I

    move-result p3

    invoke-static {p2, p3}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$102(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;I)I

    .line 3
    iget-object p2, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$1;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-interface {p1}, Ltv/danmaku/ijk/media/player/IMediaPlayer;->getVideoSarNum()I

    move-result p3

    invoke-static {p2, p3}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$202(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;I)I

    .line 4
    iget-object p2, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$1;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-interface {p1}, Ltv/danmaku/ijk/media/player/IMediaPlayer;->getVideoSarDen()I

    move-result p1

    invoke-static {p2, p1}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$302(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;I)I

    .line 5
    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$1;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {p1}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$000(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)I

    move-result p1

    if-eqz p1, :cond_1

    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$1;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {p1}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$100(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)I

    move-result p1

    if-eqz p1, :cond_1

    .line 6
    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$1;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {p1}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$400(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)Ltv/danmaku/ijk/media/player/tw/IRenderView;

    move-result-object p1

    if-eqz p1, :cond_0

    .line 7
    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$1;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {p1}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$400(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)Ltv/danmaku/ijk/media/player/tw/IRenderView;

    move-result-object p1

    iget-object p2, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$1;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {p2}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$000(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)I

    move-result p2

    iget-object p3, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$1;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {p3}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$100(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)I

    move-result p3

    invoke-interface {p1, p2, p3}, Ltv/danmaku/ijk/media/player/tw/IRenderView;->setVideoSize(II)V

    .line 8
    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$1;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {p1}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$400(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)Ltv/danmaku/ijk/media/player/tw/IRenderView;

    move-result-object p1

    iget-object p2, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$1;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {p2}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$200(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)I

    move-result p2

    iget-object p3, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$1;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {p3}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$300(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)I

    move-result p3

    invoke-interface {p1, p2, p3}, Ltv/danmaku/ijk/media/player/tw/IRenderView;->setVideoSampleAspectRatio(II)V

    .line 9
    :cond_0
    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$1;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-virtual {p0}, Landroid/widget/FrameLayout;->requestLayout()V

    :cond_1
    return-void
.end method
