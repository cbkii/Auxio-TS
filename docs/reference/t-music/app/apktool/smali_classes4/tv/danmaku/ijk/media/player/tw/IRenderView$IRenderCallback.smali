.class public interface abstract Ltv/danmaku/ijk/media/player/tw/IRenderView$IRenderCallback;
.super Ljava/lang/Object;
.source "IRenderView.java"


# annotations
.annotation system Ldalvik/annotation/EnclosingClass;
    value = Ltv/danmaku/ijk/media/player/tw/IRenderView;
.end annotation

.annotation system Ldalvik/annotation/InnerClass;
    accessFlags = 0x609
    name = "IRenderCallback"
.end annotation


# virtual methods
.method public abstract onSurfaceChanged(Ltv/danmaku/ijk/media/player/tw/IRenderView$ISurfaceHolder;III)V
    .param p1    # Ltv/danmaku/ijk/media/player/tw/IRenderView$ISurfaceHolder;
        .annotation build Landroid/support/annotation/NonNull;
        .end annotation
    .end param
.end method

.method public abstract onSurfaceCreated(Ltv/danmaku/ijk/media/player/tw/IRenderView$ISurfaceHolder;II)V
    .param p1    # Ltv/danmaku/ijk/media/player/tw/IRenderView$ISurfaceHolder;
        .annotation build Landroid/support/annotation/NonNull;
        .end annotation
    .end param
.end method

.method public abstract onSurfaceDestroyed(Ltv/danmaku/ijk/media/player/tw/IRenderView$ISurfaceHolder;)V
    .param p1    # Ltv/danmaku/ijk/media/player/tw/IRenderView$ISurfaceHolder;
        .annotation build Landroid/support/annotation/NonNull;
        .end annotation
    .end param
.end method
