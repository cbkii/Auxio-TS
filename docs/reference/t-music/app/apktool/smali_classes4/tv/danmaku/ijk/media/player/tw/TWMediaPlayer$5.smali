.class Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer$5;
.super Ljava/lang/Object;
.source "TWMediaPlayer.java"

# interfaces
.implements Ltv/danmaku/ijk/media/player/IMediaPlayer$OnBufferingUpdateListener;


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
    iput-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer$5;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public onBufferingUpdate(Ltv/danmaku/ijk/media/player/IMediaPlayer;I)V
    .locals 0

    .line 1
    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer$5;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;

    invoke-static {p0, p2}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->access$1102(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;I)I

    return-void
.end method
