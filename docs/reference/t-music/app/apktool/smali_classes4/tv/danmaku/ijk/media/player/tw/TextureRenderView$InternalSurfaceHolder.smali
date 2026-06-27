.class final Ltv/danmaku/ijk/media/player/tw/TextureRenderView$InternalSurfaceHolder;
.super Ljava/lang/Object;
.source "TextureRenderView.java"

# interfaces
.implements Ltv/danmaku/ijk/media/player/tw/IRenderView$ISurfaceHolder;


# annotations
.annotation system Ldalvik/annotation/EnclosingClass;
    value = Ltv/danmaku/ijk/media/player/tw/TextureRenderView;
.end annotation

.annotation system Ldalvik/annotation/InnerClass;
    accessFlags = 0x1a
    name = "InternalSurfaceHolder"
.end annotation


# instance fields
.field private mSurfaceTexture:Landroid/graphics/SurfaceTexture;

.field private mSurfaceTextureHost:Ltv/danmaku/ijk/media/player/ISurfaceTextureHost;

.field private mTextureView:Ltv/danmaku/ijk/media/player/tw/TextureRenderView;


# direct methods
.method public constructor <init>(Ltv/danmaku/ijk/media/player/tw/TextureRenderView;Landroid/graphics/SurfaceTexture;Ltv/danmaku/ijk/media/player/ISurfaceTextureHost;)V
    .locals 0
    .param p1    # Ltv/danmaku/ijk/media/player/tw/TextureRenderView;
        .annotation build Landroid/support/annotation/NonNull;
        .end annotation
    .end param
    .param p2    # Landroid/graphics/SurfaceTexture;
        .annotation build Landroid/support/annotation/Nullable;
        .end annotation
    .end param
    .param p3    # Ltv/danmaku/ijk/media/player/ISurfaceTextureHost;
        .annotation build Landroid/support/annotation/NonNull;
        .end annotation
    .end param

    .line 1
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    .line 2
    iput-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TextureRenderView$InternalSurfaceHolder;->mTextureView:Ltv/danmaku/ijk/media/player/tw/TextureRenderView;

    .line 3
    iput-object p2, p0, Ltv/danmaku/ijk/media/player/tw/TextureRenderView$InternalSurfaceHolder;->mSurfaceTexture:Landroid/graphics/SurfaceTexture;

    .line 4
    iput-object p3, p0, Ltv/danmaku/ijk/media/player/tw/TextureRenderView$InternalSurfaceHolder;->mSurfaceTextureHost:Ltv/danmaku/ijk/media/player/ISurfaceTextureHost;

    return-void
.end method


# virtual methods
.method public bindToMediaPlayer(Ltv/danmaku/ijk/media/player/IMediaPlayer;)V
    .locals 2
    .annotation build Landroid/annotation/TargetApi;
        value = 0x10
    .end annotation

    if-nez p1, :cond_0

    return-void

    .line 1
    :cond_0
    sget v0, Landroid/os/Build$VERSION;->SDK_INT:I

    const/16 v1, 0x10

    if-lt v0, v1, :cond_2

    instance-of v0, p1, Ltv/danmaku/ijk/media/player/ISurfaceTextureHolder;

    if-eqz v0, :cond_2

    .line 2
    check-cast p1, Ltv/danmaku/ijk/media/player/ISurfaceTextureHolder;

    .line 3
    iget-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TextureRenderView$InternalSurfaceHolder;->mTextureView:Ltv/danmaku/ijk/media/player/tw/TextureRenderView;

    invoke-static {v0}, Ltv/danmaku/ijk/media/player/tw/TextureRenderView;->access$100(Ltv/danmaku/ijk/media/player/tw/TextureRenderView;)Ltv/danmaku/ijk/media/player/tw/TextureRenderView$SurfaceCallback;

    move-result-object v0

    const/4 v1, 0x0

    invoke-virtual {v0, v1}, Ltv/danmaku/ijk/media/player/tw/TextureRenderView$SurfaceCallback;->setOwnSurfaceTexture(Z)V

    .line 4
    invoke-interface {p1}, Ltv/danmaku/ijk/media/player/ISurfaceTextureHolder;->getSurfaceTexture()Landroid/graphics/SurfaceTexture;

    move-result-object v0

    if-eqz v0, :cond_1

    .line 5
    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/TextureRenderView$InternalSurfaceHolder;->mTextureView:Ltv/danmaku/ijk/media/player/tw/TextureRenderView;

    invoke-virtual {p0, v0}, Landroid/view/TextureView;->setSurfaceTexture(Landroid/graphics/SurfaceTexture;)V

    goto :goto_0

    .line 6
    :cond_1
    iget-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TextureRenderView$InternalSurfaceHolder;->mSurfaceTexture:Landroid/graphics/SurfaceTexture;

    invoke-interface {p1, v0}, Ltv/danmaku/ijk/media/player/ISurfaceTextureHolder;->setSurfaceTexture(Landroid/graphics/SurfaceTexture;)V

    .line 7
    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/TextureRenderView$InternalSurfaceHolder;->mTextureView:Ltv/danmaku/ijk/media/player/tw/TextureRenderView;

    invoke-static {p0}, Ltv/danmaku/ijk/media/player/tw/TextureRenderView;->access$100(Ltv/danmaku/ijk/media/player/tw/TextureRenderView;)Ltv/danmaku/ijk/media/player/tw/TextureRenderView$SurfaceCallback;

    move-result-object p0

    invoke-interface {p1, p0}, Ltv/danmaku/ijk/media/player/ISurfaceTextureHolder;->setSurfaceTextureHost(Ltv/danmaku/ijk/media/player/ISurfaceTextureHost;)V

    goto :goto_0

    .line 8
    :cond_2
    invoke-virtual {p0}, Ltv/danmaku/ijk/media/player/tw/TextureRenderView$InternalSurfaceHolder;->openSurface()Landroid/view/Surface;

    move-result-object p0

    invoke-interface {p1, p0}, Ltv/danmaku/ijk/media/player/IMediaPlayer;->setSurface(Landroid/view/Surface;)V

    :goto_0
    return-void
.end method

.method public getRenderView()Ltv/danmaku/ijk/media/player/tw/IRenderView;
    .locals 0
    .annotation build Landroid/support/annotation/NonNull;
    .end annotation

    .line 1
    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/TextureRenderView$InternalSurfaceHolder;->mTextureView:Ltv/danmaku/ijk/media/player/tw/TextureRenderView;

    return-object p0
.end method

.method public getSurfaceHolder()Landroid/view/SurfaceHolder;
    .locals 0
    .annotation build Landroid/support/annotation/Nullable;
    .end annotation

    const/4 p0, 0x0

    return-object p0
.end method

.method public getSurfaceTexture()Landroid/graphics/SurfaceTexture;
    .locals 0
    .annotation build Landroid/support/annotation/Nullable;
    .end annotation

    .line 1
    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/TextureRenderView$InternalSurfaceHolder;->mSurfaceTexture:Landroid/graphics/SurfaceTexture;

    return-object p0
.end method

.method public openSurface()Landroid/view/Surface;
    .locals 1
    .annotation build Landroid/support/annotation/Nullable;
    .end annotation

    .line 1
    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/TextureRenderView$InternalSurfaceHolder;->mSurfaceTexture:Landroid/graphics/SurfaceTexture;

    if-nez p0, :cond_0

    const/4 p0, 0x0

    return-object p0

    .line 2
    :cond_0
    new-instance v0, Landroid/view/Surface;

    invoke-direct {v0, p0}, Landroid/view/Surface;-><init>(Landroid/graphics/SurfaceTexture;)V

    return-object v0
.end method
