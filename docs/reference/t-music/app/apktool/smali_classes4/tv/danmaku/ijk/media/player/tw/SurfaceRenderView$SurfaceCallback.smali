.class final Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$SurfaceCallback;
.super Ljava/lang/Object;
.source "SurfaceRenderView.java"

# interfaces
.implements Landroid/view/SurfaceHolder$Callback;


# annotations
.annotation system Ldalvik/annotation/EnclosingClass;
    value = Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView;
.end annotation

.annotation system Ldalvik/annotation/InnerClass;
    accessFlags = 0x1a
    name = "SurfaceCallback"
.end annotation


# instance fields
.field private mFormat:I

.field private mHeight:I

.field private mIsFormatChanged:Z

.field private mRenderCallbackMap:Ljava/util/Map;
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "Ljava/util/Map<",
            "Ltv/danmaku/ijk/media/player/tw/IRenderView$IRenderCallback;",
            "Ljava/lang/Object;",
            ">;"
        }
    .end annotation
.end field

.field private mSurfaceHolder:Landroid/view/SurfaceHolder;

.field private mWeakSurfaceView:Ljava/lang/ref/WeakReference;
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "Ljava/lang/ref/WeakReference<",
            "Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView;",
            ">;"
        }
    .end annotation
.end field

.field private mWidth:I


# direct methods
.method public constructor <init>(Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView;)V
    .locals 1
    .param p1    # Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView;
        .annotation build Landroid/support/annotation/NonNull;
        .end annotation
    .end param

    .line 1
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    .line 2
    new-instance v0, Ljava/util/concurrent/ConcurrentHashMap;

    invoke-direct {v0}, Ljava/util/concurrent/ConcurrentHashMap;-><init>()V

    iput-object v0, p0, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$SurfaceCallback;->mRenderCallbackMap:Ljava/util/Map;

    .line 3
    new-instance v0, Ljava/lang/ref/WeakReference;

    invoke-direct {v0, p1}, Ljava/lang/ref/WeakReference;-><init>(Ljava/lang/Object;)V

    iput-object v0, p0, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$SurfaceCallback;->mWeakSurfaceView:Ljava/lang/ref/WeakReference;

    return-void
.end method


# virtual methods
.method public addRenderCallback(Ltv/danmaku/ijk/media/player/tw/IRenderView$IRenderCallback;)V
    .locals 3
    .param p1    # Ltv/danmaku/ijk/media/player/tw/IRenderView$IRenderCallback;
        .annotation build Landroid/support/annotation/NonNull;
        .end annotation
    .end param

    .line 1
    iget-object v0, p0, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$SurfaceCallback;->mRenderCallbackMap:Ljava/util/Map;

    invoke-interface {v0, p1, p1}, Ljava/util/Map;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    .line 2
    iget-object v0, p0, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$SurfaceCallback;->mSurfaceHolder:Landroid/view/SurfaceHolder;

    if-eqz v0, :cond_0

    .line 3
    new-instance v0, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$InternalSurfaceHolder;

    iget-object v1, p0, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$SurfaceCallback;->mWeakSurfaceView:Ljava/lang/ref/WeakReference;

    invoke-virtual {v1}, Ljava/lang/ref/WeakReference;->get()Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView;

    iget-object v2, p0, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$SurfaceCallback;->mSurfaceHolder:Landroid/view/SurfaceHolder;

    invoke-direct {v0, v1, v2}, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$InternalSurfaceHolder;-><init>(Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView;Landroid/view/SurfaceHolder;)V

    .line 4
    iget v1, p0, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$SurfaceCallback;->mWidth:I

    iget v2, p0, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$SurfaceCallback;->mHeight:I

    invoke-interface {p1, v0, v1, v2}, Ltv/danmaku/ijk/media/player/tw/IRenderView$IRenderCallback;->onSurfaceCreated(Ltv/danmaku/ijk/media/player/tw/IRenderView$ISurfaceHolder;II)V

    goto :goto_0

    :cond_0
    const/4 v0, 0x0

    .line 5
    :goto_0
    iget-boolean v1, p0, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$SurfaceCallback;->mIsFormatChanged:Z

    if-eqz v1, :cond_2

    if-nez v0, :cond_1

    .line 6
    new-instance v0, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$InternalSurfaceHolder;

    iget-object v1, p0, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$SurfaceCallback;->mWeakSurfaceView:Ljava/lang/ref/WeakReference;

    invoke-virtual {v1}, Ljava/lang/ref/WeakReference;->get()Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView;

    iget-object v2, p0, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$SurfaceCallback;->mSurfaceHolder:Landroid/view/SurfaceHolder;

    invoke-direct {v0, v1, v2}, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$InternalSurfaceHolder;-><init>(Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView;Landroid/view/SurfaceHolder;)V

    .line 7
    :cond_1
    iget v1, p0, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$SurfaceCallback;->mFormat:I

    iget v2, p0, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$SurfaceCallback;->mWidth:I

    iget p0, p0, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$SurfaceCallback;->mHeight:I

    invoke-interface {p1, v0, v1, v2, p0}, Ltv/danmaku/ijk/media/player/tw/IRenderView$IRenderCallback;->onSurfaceChanged(Ltv/danmaku/ijk/media/player/tw/IRenderView$ISurfaceHolder;III)V

    :cond_2
    return-void
.end method

.method public removeRenderCallback(Ltv/danmaku/ijk/media/player/tw/IRenderView$IRenderCallback;)V
    .locals 0
    .param p1    # Ltv/danmaku/ijk/media/player/tw/IRenderView$IRenderCallback;
        .annotation build Landroid/support/annotation/NonNull;
        .end annotation
    .end param

    .line 1
    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$SurfaceCallback;->mRenderCallbackMap:Ljava/util/Map;

    invoke-interface {p0, p1}, Ljava/util/Map;->remove(Ljava/lang/Object;)Ljava/lang/Object;

    return-void
.end method

.method public surfaceChanged(Landroid/view/SurfaceHolder;III)V
    .locals 2

    .line 1
    iput-object p1, p0, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$SurfaceCallback;->mSurfaceHolder:Landroid/view/SurfaceHolder;

    const/4 p1, 0x1

    .line 2
    iput-boolean p1, p0, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$SurfaceCallback;->mIsFormatChanged:Z

    .line 3
    iput p2, p0, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$SurfaceCallback;->mFormat:I

    .line 4
    iput p3, p0, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$SurfaceCallback;->mWidth:I

    .line 5
    iput p4, p0, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$SurfaceCallback;->mHeight:I

    .line 6
    new-instance p1, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$InternalSurfaceHolder;

    iget-object v0, p0, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$SurfaceCallback;->mWeakSurfaceView:Ljava/lang/ref/WeakReference;

    invoke-virtual {v0}, Ljava/lang/ref/WeakReference;->get()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView;

    iget-object v1, p0, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$SurfaceCallback;->mSurfaceHolder:Landroid/view/SurfaceHolder;

    invoke-direct {p1, v0, v1}, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$InternalSurfaceHolder;-><init>(Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView;Landroid/view/SurfaceHolder;)V

    .line 7
    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$SurfaceCallback;->mRenderCallbackMap:Ljava/util/Map;

    invoke-interface {p0}, Ljava/util/Map;->keySet()Ljava/util/Set;

    move-result-object p0

    invoke-interface {p0}, Ljava/util/Set;->iterator()Ljava/util/Iterator;

    move-result-object p0

    :goto_0
    invoke-interface {p0}, Ljava/util/Iterator;->hasNext()Z

    move-result v0

    if-eqz v0, :cond_0

    invoke-interface {p0}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Ltv/danmaku/ijk/media/player/tw/IRenderView$IRenderCallback;

    .line 8
    invoke-interface {v0, p1, p2, p3, p4}, Ltv/danmaku/ijk/media/player/tw/IRenderView$IRenderCallback;->onSurfaceChanged(Ltv/danmaku/ijk/media/player/tw/IRenderView$ISurfaceHolder;III)V

    goto :goto_0

    :cond_0
    return-void
.end method

.method public surfaceCreated(Landroid/view/SurfaceHolder;)V
    .locals 3

    .line 1
    iput-object p1, p0, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$SurfaceCallback;->mSurfaceHolder:Landroid/view/SurfaceHolder;

    const/4 p1, 0x0

    .line 2
    iput-boolean p1, p0, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$SurfaceCallback;->mIsFormatChanged:Z

    .line 3
    iput p1, p0, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$SurfaceCallback;->mFormat:I

    .line 4
    iput p1, p0, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$SurfaceCallback;->mWidth:I

    .line 5
    iput p1, p0, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$SurfaceCallback;->mHeight:I

    .line 6
    new-instance v0, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$InternalSurfaceHolder;

    iget-object v1, p0, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$SurfaceCallback;->mWeakSurfaceView:Ljava/lang/ref/WeakReference;

    invoke-virtual {v1}, Ljava/lang/ref/WeakReference;->get()Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView;

    iget-object v2, p0, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$SurfaceCallback;->mSurfaceHolder:Landroid/view/SurfaceHolder;

    invoke-direct {v0, v1, v2}, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$InternalSurfaceHolder;-><init>(Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView;Landroid/view/SurfaceHolder;)V

    .line 7
    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$SurfaceCallback;->mRenderCallbackMap:Ljava/util/Map;

    invoke-interface {p0}, Ljava/util/Map;->keySet()Ljava/util/Set;

    move-result-object p0

    invoke-interface {p0}, Ljava/util/Set;->iterator()Ljava/util/Iterator;

    move-result-object p0

    :goto_0
    invoke-interface {p0}, Ljava/util/Iterator;->hasNext()Z

    move-result v1

    if-eqz v1, :cond_0

    invoke-interface {p0}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Ltv/danmaku/ijk/media/player/tw/IRenderView$IRenderCallback;

    .line 8
    invoke-interface {v1, v0, p1, p1}, Ltv/danmaku/ijk/media/player/tw/IRenderView$IRenderCallback;->onSurfaceCreated(Ltv/danmaku/ijk/media/player/tw/IRenderView$ISurfaceHolder;II)V

    goto :goto_0

    :cond_0
    return-void
.end method

.method public surfaceDestroyed(Landroid/view/SurfaceHolder;)V
    .locals 2

    const/4 p1, 0x0

    .line 1
    iput-object p1, p0, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$SurfaceCallback;->mSurfaceHolder:Landroid/view/SurfaceHolder;

    const/4 p1, 0x0

    .line 2
    iput-boolean p1, p0, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$SurfaceCallback;->mIsFormatChanged:Z

    .line 3
    iput p1, p0, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$SurfaceCallback;->mFormat:I

    .line 4
    iput p1, p0, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$SurfaceCallback;->mWidth:I

    .line 5
    iput p1, p0, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$SurfaceCallback;->mHeight:I

    .line 6
    new-instance p1, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$InternalSurfaceHolder;

    iget-object v0, p0, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$SurfaceCallback;->mWeakSurfaceView:Ljava/lang/ref/WeakReference;

    invoke-virtual {v0}, Ljava/lang/ref/WeakReference;->get()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView;

    iget-object v1, p0, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$SurfaceCallback;->mSurfaceHolder:Landroid/view/SurfaceHolder;

    invoke-direct {p1, v0, v1}, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$InternalSurfaceHolder;-><init>(Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView;Landroid/view/SurfaceHolder;)V

    .line 7
    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$SurfaceCallback;->mRenderCallbackMap:Ljava/util/Map;

    invoke-interface {p0}, Ljava/util/Map;->keySet()Ljava/util/Set;

    move-result-object p0

    invoke-interface {p0}, Ljava/util/Set;->iterator()Ljava/util/Iterator;

    move-result-object p0

    :goto_0
    invoke-interface {p0}, Ljava/util/Iterator;->hasNext()Z

    move-result v0

    if-eqz v0, :cond_0

    invoke-interface {p0}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Ltv/danmaku/ijk/media/player/tw/IRenderView$IRenderCallback;

    .line 8
    invoke-interface {v0, p1}, Ltv/danmaku/ijk/media/player/tw/IRenderView$IRenderCallback;->onSurfaceDestroyed(Ltv/danmaku/ijk/media/player/tw/IRenderView$ISurfaceHolder;)V

    goto :goto_0

    :cond_0
    return-void
.end method
