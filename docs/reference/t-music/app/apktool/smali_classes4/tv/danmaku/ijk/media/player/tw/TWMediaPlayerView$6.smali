.class Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$6;
.super Ljava/lang/Object;
.source "TWMediaPlayerView.java"

# interfaces
.implements Ltv/danmaku/ijk/media/player/IMediaPlayer$OnBufferingUpdateListener;


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
    iput-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$6;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public onBufferingUpdate(Ltv/danmaku/ijk/media/player/IMediaPlayer;I)V
    .locals 0

    .line 1
    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$6;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;

    invoke-static {p0, p2}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->access$1902(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;I)I

    return-void
.end method
