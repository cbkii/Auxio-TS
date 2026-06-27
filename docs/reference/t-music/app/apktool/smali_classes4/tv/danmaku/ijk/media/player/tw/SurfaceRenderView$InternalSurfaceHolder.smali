.class final Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$InternalSurfaceHolder;
.super Ljava/lang/Object;
.source "SurfaceRenderView.java"

# interfaces
.implements Ltv/danmaku/ijk/media/player/tw/IRenderView$ISurfaceHolder;


# annotations
.annotation system Ldalvik/annotation/EnclosingClass;
    value = Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView;
.end annotation

.annotation system Ldalvik/annotation/InnerClass;
    accessFlags = 0x1a
    name = "InternalSurfaceHolder"
.end annotation


# instance fields
.field private mSurfaceHolder:Landroid/view/SurfaceHolder;

.field private mSurfaceView:Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView;


# direct methods
.method public constructor <init>(Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView;Landroid/view/SurfaceHolder;)V
    .locals 0
    .param p1    # Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView;
        .annotation build Landroid/support/annotation/NonNull;
        .end annotation
    .end param
    .param p2    # Landroid/view/SurfaceHolder;
        .annotation build Landroid/support/annotation/Nullable;
        .end annotation
    .end param

    .line 1
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    .line 2
    iput-object p1, p0, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$InternalSurfaceHolder;->mSurfaceView:Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView;

    .line 3
    iput-object p2, p0, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$InternalSurfaceHolder;->mSurfaceHolder:Landroid/view/SurfaceHolder;

    return-void
.end method


# virtual methods
.method public bindToMediaPlayer(Ltv/danmaku/ijk/media/player/IMediaPlayer;)V
    .locals 2

    if-eqz p1, :cond_1

    .line 1
    sget v0, Landroid/os/Build$VERSION;->SDK_INT:I

    const/16 v1, 0x10

    if-lt v0, v1, :cond_0

    instance-of v0, p1, Ltv/danmaku/ijk/media/player/ISurfaceTextureHolder;

    if-eqz v0, :cond_0

    .line 2
    move-object v0, p1

    check-cast v0, Ltv/danmaku/ijk/media/player/ISurfaceTextureHolder;

    const/4 v1, 0x0

    .line 3
    invoke-interface {v0, v1}, Ltv/danmaku/ijk/media/player/ISurfaceTextureHolder;->setSurfaceTexture(Landroid/graphics/SurfaceTexture;)V

    .line 4
    :cond_0
    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$InternalSurfaceHolder;->mSurfaceHolder:Landroid/view/SurfaceHolder;

    invoke-interface {p1, p0}, Ltv/danmaku/ijk/media/player/IMediaPlayer;->setDisplay(Landroid/view/SurfaceHolder;)V

    :cond_1
    return-void
.end method

.method public getRenderView()Ltv/danmaku/ijk/media/player/tw/IRenderView;
    .locals 0
    .annotation build Landroid/support/annotation/NonNull;
    .end annotation

    .line 1
    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$InternalSurfaceHolder;->mSurfaceView:Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView;

    return-object p0
.end method

.method public getSurfaceHolder()Landroid/view/SurfaceHolder;
    .locals 0
    .annotation build Landroid/support/annotation/Nullable;
    .end annotation

    .line 1
    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$InternalSurfaceHolder;->mSurfaceHolder:Landroid/view/SurfaceHolder;

    return-object p0
.end method

.method public getSurfaceTexture()Landroid/graphics/SurfaceTexture;
    .locals 0
    .annotation build Landroid/support/annotation/Nullable;
    .end annotation

    const/4 p0, 0x0

    return-object p0
.end method

.method public openSurface()Landroid/view/Surface;
    .locals 0
    .annotation build Landroid/support/annotation/Nullable;
    .end annotation

    .line 1
    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$InternalSurfaceHolder;->mSurfaceHolder:Landroid/view/SurfaceHolder;

    if-nez p0, :cond_0

    const/4 p0, 0x0

    return-object p0

    .line 2
    :cond_0
    invoke-interface {p0}, Landroid/view/SurfaceHolder;->getSurface()Landroid/view/Surface;

    move-result-object p0

    return-object p0
.end method
