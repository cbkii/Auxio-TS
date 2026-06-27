.class Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$9;
.super Ljava/lang/Object;
.source "TWMediaPlayerView.java"

# interfaces
.implements Ltv/danmaku/ijk/media/player/tw/IRenderView$IRenderCallback;


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
    iput-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$9;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public onSurfaceChanged(Ltv/danmaku/ijk/media/player/tw/IRenderView$ISurfaceHolder;III)V
    .locals 2
    .param p1    # Ltv/danmaku/ijk/media/player/tw/IRenderView$ISurfaceHolder;
        .annotation build Landroid/support/annotation/NonNull;
        .end annotation
    .end param

    .line 1
    invoke-interface {p1}, Ltv/danmaku/ijk/media/player/tw/IRenderView$ISurfaceHolder;->getRenderView()Ltv/danmaku/ijk/media/player/tw/IRenderView;

    move-result-object p1

    iget-object p2, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$9;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {p2}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$400(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)Ltv/danmaku/ijk/media/player/tw/IRenderView;

    move-result-object p2

    if-eq p1, p2, :cond_0

    .line 2
    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$9;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$600(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)Ljava/lang/String;

    move-result-object p0

    const-string p1, "onSurfaceChanged: unmatched render callback\n"

    invoke-static {p0, p1}, Landroid/util/Log;->e(Ljava/lang/String;Ljava/lang/String;)I

    return-void

    .line 3
    :cond_0
    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$9;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {p1, p3}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$1202(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;I)I

    .line 4
    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$9;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {p1, p4}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$1302(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;I)I

    .line 5
    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$9;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {p1}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$1400(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)I

    move-result p1

    const/4 p2, 0x3

    const/4 v0, 0x1

    const/4 v1, 0x0

    if-ne p1, p2, :cond_1

    move p1, v0

    goto :goto_0

    :cond_1
    move p1, v1

    .line 6
    :goto_0
    iget-object p2, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$9;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {p2}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$400(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)Ltv/danmaku/ijk/media/player/tw/IRenderView;

    move-result-object p2

    invoke-interface {p2}, Ltv/danmaku/ijk/media/player/tw/IRenderView;->shouldWaitForResize()Z

    move-result p2

    if-eqz p2, :cond_3

    iget-object p2, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$9;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {p2}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$000(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)I

    move-result p2

    if-ne p2, p3, :cond_2

    iget-object p2, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$9;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {p2}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$100(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)I

    move-result p2

    if-ne p2, p4, :cond_2

    goto :goto_1

    :cond_2
    move v0, v1

    .line 7
    :cond_3
    :goto_1
    iget-object p2, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$9;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {p2}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$1000(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)Ltv/danmaku/ijk/media/player/IMediaPlayer;

    move-result-object p2

    if-eqz p2, :cond_5

    if-eqz p1, :cond_5

    if-eqz v0, :cond_5

    .line 8
    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$9;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {p1}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$1100(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)I

    move-result p1

    if-eqz p1, :cond_4

    .line 9
    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$9;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {p1}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$1100(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)I

    move-result p2

    invoke-virtual {p1, p2}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->seekTo(I)V

    .line 10
    :cond_4
    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$9;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-virtual {p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->start()V

    :cond_5
    return-void
.end method

.method public onSurfaceCreated(Ltv/danmaku/ijk/media/player/tw/IRenderView$ISurfaceHolder;II)V
    .locals 0
    .param p1    # Ltv/danmaku/ijk/media/player/tw/IRenderView$ISurfaceHolder;
        .annotation build Landroid/support/annotation/NonNull;
        .end annotation
    .end param

    .line 1
    invoke-interface {p1}, Ltv/danmaku/ijk/media/player/tw/IRenderView$ISurfaceHolder;->getRenderView()Ltv/danmaku/ijk/media/player/tw/IRenderView;

    move-result-object p2

    iget-object p3, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$9;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {p3}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$400(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)Ltv/danmaku/ijk/media/player/tw/IRenderView;

    move-result-object p3

    if-eq p2, p3, :cond_0

    .line 2
    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$9;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$600(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)Ljava/lang/String;

    move-result-object p0

    const-string p1, "onSurfaceCreated: unmatched render callback\n"

    invoke-static {p0, p1}, Landroid/util/Log;->e(Ljava/lang/String;Ljava/lang/String;)I

    return-void

    .line 3
    :cond_0
    iget-object p2, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$9;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {p2, p1}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$2202(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;Ltv/danmaku/ijk/media/player/tw/IRenderView$ISurfaceHolder;)Ltv/danmaku/ijk/media/player/tw/IRenderView$ISurfaceHolder;

    .line 4
    iget-object p2, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$9;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {p2}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$1000(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)Ltv/danmaku/ijk/media/player/IMediaPlayer;

    move-result-object p2

    if-eqz p2, :cond_1

    .line 5
    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$9;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$1000(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)Ltv/danmaku/ijk/media/player/IMediaPlayer;

    move-result-object p2

    invoke-static {p0, p2, p1}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$2300(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;Ltv/danmaku/ijk/media/player/IMediaPlayer;Ltv/danmaku/ijk/media/player/tw/IRenderView$ISurfaceHolder;)V

    :cond_1
    return-void
.end method

.method public onSurfaceDestroyed(Ltv/danmaku/ijk/media/player/tw/IRenderView$ISurfaceHolder;)V
    .locals 1
    .param p1    # Ltv/danmaku/ijk/media/player/tw/IRenderView$ISurfaceHolder;
        .annotation build Landroid/support/annotation/NonNull;
        .end annotation
    .end param

    .line 1
    invoke-interface {p1}, Ltv/danmaku/ijk/media/player/tw/IRenderView$ISurfaceHolder;->getRenderView()Ltv/danmaku/ijk/media/player/tw/IRenderView;

    move-result-object p1

    iget-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$9;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {v0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$400(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)Ltv/danmaku/ijk/media/player/tw/IRenderView;

    move-result-object v0

    if-eq p1, v0, :cond_0

    .line 2
    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$9;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$600(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)Ljava/lang/String;

    move-result-object p0

    const-string p1, "onSurfaceDestroyed: unmatched render callback\n"

    invoke-static {p0, p1}, Landroid/util/Log;->e(Ljava/lang/String;Ljava/lang/String;)I

    return-void

    .line 3
    :cond_0
    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$9;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    const/4 v0, 0x0

    invoke-static {p1, v0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$2202(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;Ltv/danmaku/ijk/media/player/tw/IRenderView$ISurfaceHolder;)Ltv/danmaku/ijk/media/player/tw/IRenderView$ISurfaceHolder;

    .line 4
    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$9;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-virtual {p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->releaseWithoutStop()V

    return-void
.end method
