.class Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$5;
.super Ljava/lang/Object;
.source "TWMediaPlayerView.java"

# interfaces
.implements Ltv/danmaku/ijk/media/player/IMediaPlayer$OnErrorListener;


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
    iput-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$5;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public onError(Ltv/danmaku/ijk/media/player/IMediaPlayer;II)Z
    .locals 2

    .line 1
    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$5;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {p1}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$600(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)Ljava/lang/String;

    move-result-object p1

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "Error: "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, p2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v1, ","

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, p3}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-static {p1, v0}, Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    .line 2
    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$5;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    const/4 v0, -0x1

    invoke-static {p1, v0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$802(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;I)I

    .line 3
    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$5;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {p1, v0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$1402(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;I)I

    .line 4
    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$5;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {p1}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$1800(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)Ltv/danmaku/ijk/media/player/IMediaPlayer$OnErrorListener;

    move-result-object p1

    const/4 v0, 0x1

    if-eqz p1, :cond_0

    .line 5
    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$5;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {p1}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$1800(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)Ltv/danmaku/ijk/media/player/IMediaPlayer$OnErrorListener;

    move-result-object p1

    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$5;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$1000(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)Ltv/danmaku/ijk/media/player/IMediaPlayer;

    move-result-object p0

    invoke-interface {p1, p0, p2, p3}, Ltv/danmaku/ijk/media/player/IMediaPlayer$OnErrorListener;->onError(Ltv/danmaku/ijk/media/player/IMediaPlayer;II)Z

    move-result p0

    if-eqz p0, :cond_0

    :cond_0
    return v0
.end method
