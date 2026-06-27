.class Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer$2;
.super Ljava/lang/Object;
.source "TWMediaPlayer.java"

# interfaces
.implements Ltv/danmaku/ijk/media/player/IMediaPlayer$OnCompletionListener;


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
    iput-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer$2;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public onCompletion(Ltv/danmaku/ijk/media/player/IMediaPlayer;)V
    .locals 1

    .line 1
    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer$2;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;

    const/4 v0, 0x5

    invoke-static {p1, v0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->access$302(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;I)I

    .line 2
    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer$2;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;

    invoke-static {p1, v0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->access$702(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;I)I

    .line 3
    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer$2;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;

    invoke-static {p1}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->access$800(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;)Ltv/danmaku/ijk/media/player/IMediaPlayer$OnCompletionListener;

    move-result-object p1

    if-eqz p1, :cond_0

    .line 4
    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer$2;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;

    invoke-static {p1}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->access$800(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;)Ltv/danmaku/ijk/media/player/IMediaPlayer$OnCompletionListener;

    move-result-object p1

    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer$2;->this$0:Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;

    invoke-static {p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->access$500(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;)Ltv/danmaku/ijk/media/player/IMediaPlayer;

    move-result-object p0

    invoke-interface {p1, p0}, Ltv/danmaku/ijk/media/player/IMediaPlayer$OnCompletionListener;->onCompletion(Ltv/danmaku/ijk/media/player/IMediaPlayer;)V

    :cond_0
    return-void
.end method
