.class public interface abstract Ltv/danmaku/ijk/media/player/tw/IRenderView$ISurfaceHolder;
.super Ljava/lang/Object;
.source "IRenderView.java"


# annotations
.annotation system Ldalvik/annotation/EnclosingClass;
    value = Ltv/danmaku/ijk/media/player/tw/IRenderView;
.end annotation

.annotation system Ldalvik/annotation/InnerClass;
    accessFlags = 0x609
    name = "ISurfaceHolder"
.end annotation


# virtual methods
.method public abstract bindToMediaPlayer(Ltv/danmaku/ijk/media/player/IMediaPlayer;)V
.end method

.method public abstract getRenderView()Ltv/danmaku/ijk/media/player/tw/IRenderView;
    .annotation build Landroid/support/annotation/NonNull;
    .end annotation
.end method

.method public abstract getSurfaceHolder()Landroid/view/SurfaceHolder;
    .annotation build Landroid/support/annotation/Nullable;
    .end annotation
.end method

.method public abstract getSurfaceTexture()Landroid/graphics/SurfaceTexture;
    .annotation build Landroid/support/annotation/Nullable;
    .end annotation
.end method

.method public abstract openSurface()Landroid/view/Surface;
    .annotation build Landroid/support/annotation/Nullable;
    .end annotation
.end method
