.class public Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView;
.super Landroid/view/SurfaceView;
.source "SurfaceRenderView.java"

# interfaces
.implements Ltv/danmaku/ijk/media/player/tw/IRenderView;


# annotations
.annotation system Ldalvik/annotation/MemberClasses;
    value = {
        Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$SurfaceCallback;,
        Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$InternalSurfaceHolder;
    }
.end annotation


# instance fields
.field private mMeasureHelper:Ltv/danmaku/ijk/media/player/tw/MeasureHelper;

.field private mSurfaceCallback:Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$SurfaceCallback;


# direct methods
.method public constructor <init>(Landroid/content/Context;)V
    .locals 0

    .line 1
    invoke-direct {p0, p1}, Landroid/view/SurfaceView;-><init>(Landroid/content/Context;)V

    .line 2
    invoke-direct {p0, p1}, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView;->initView(Landroid/content/Context;)V

    return-void
.end method

.method public constructor <init>(Landroid/content/Context;Landroid/util/AttributeSet;)V
    .locals 0

    .line 3
    invoke-direct {p0, p1, p2}, Landroid/view/SurfaceView;-><init>(Landroid/content/Context;Landroid/util/AttributeSet;)V

    .line 4
    invoke-direct {p0, p1}, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView;->initView(Landroid/content/Context;)V

    return-void
.end method

.method public constructor <init>(Landroid/content/Context;Landroid/util/AttributeSet;I)V
    .locals 0

    .line 5
    invoke-direct {p0, p1, p2, p3}, Landroid/view/SurfaceView;-><init>(Landroid/content/Context;Landroid/util/AttributeSet;I)V

    .line 6
    invoke-direct {p0, p1}, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView;->initView(Landroid/content/Context;)V

    return-void
.end method

.method public constructor <init>(Landroid/content/Context;Landroid/util/AttributeSet;II)V
    .locals 0
    .annotation build Landroid/annotation/TargetApi;
        value = 0x15
    .end annotation

    .line 7
    invoke-direct {p0, p1, p2, p3, p4}, Landroid/view/SurfaceView;-><init>(Landroid/content/Context;Landroid/util/AttributeSet;II)V

    .line 8
    invoke-direct {p0, p1}, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView;->initView(Landroid/content/Context;)V

    return-void
.end method

.method private initView(Landroid/content/Context;)V
    .locals 1

    .line 1
    new-instance p1, Ltv/danmaku/ijk/media/player/tw/MeasureHelper;

    invoke-direct {p1, p0}, Ltv/danmaku/ijk/media/player/tw/MeasureHelper;-><init>(Landroid/view/View;)V

    iput-object p1, p0, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView;->mMeasureHelper:Ltv/danmaku/ijk/media/player/tw/MeasureHelper;

    .line 2
    new-instance p1, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$SurfaceCallback;

    invoke-direct {p1, p0}, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$SurfaceCallback;-><init>(Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView;)V

    iput-object p1, p0, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView;->mSurfaceCallback:Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$SurfaceCallback;

    .line 3
    invoke-virtual {p0}, Landroid/view/SurfaceView;->getHolder()Landroid/view/SurfaceHolder;

    move-result-object p1

    iget-object v0, p0, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView;->mSurfaceCallback:Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$SurfaceCallback;

    invoke-interface {p1, v0}, Landroid/view/SurfaceHolder;->addCallback(Landroid/view/SurfaceHolder$Callback;)V

    .line 4
    invoke-virtual {p0}, Landroid/view/SurfaceView;->getHolder()Landroid/view/SurfaceHolder;

    move-result-object p0

    const/4 p1, 0x0

    invoke-interface {p0, p1}, Landroid/view/SurfaceHolder;->setType(I)V

    return-void
.end method


# virtual methods
.method public addRenderCallback(Ltv/danmaku/ijk/media/player/tw/IRenderView$IRenderCallback;)V
    .locals 0

    .line 1
    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView;->mSurfaceCallback:Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$SurfaceCallback;

    invoke-virtual {p0, p1}, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$SurfaceCallback;->addRenderCallback(Ltv/danmaku/ijk/media/player/tw/IRenderView$IRenderCallback;)V

    return-void
.end method

.method public getView()Landroid/view/View;
    .locals 0

    return-object p0
.end method

.method public onInitializeAccessibilityEvent(Landroid/view/accessibility/AccessibilityEvent;)V
    .locals 0

    .line 1
    invoke-super {p0, p1}, Landroid/view/SurfaceView;->onInitializeAccessibilityEvent(Landroid/view/accessibility/AccessibilityEvent;)V

    .line 2
    const-class p0, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView;

    invoke-virtual {p0}, Ljava/lang/Class;->getName()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p1, p0}, Landroid/view/accessibility/AccessibilityEvent;->setClassName(Ljava/lang/CharSequence;)V

    return-void
.end method

.method public onInitializeAccessibilityNodeInfo(Landroid/view/accessibility/AccessibilityNodeInfo;)V
    .locals 1
    .annotation build Landroid/annotation/TargetApi;
        value = 0xe
    .end annotation

    .line 1
    invoke-super {p0, p1}, Landroid/view/SurfaceView;->onInitializeAccessibilityNodeInfo(Landroid/view/accessibility/AccessibilityNodeInfo;)V

    .line 2
    sget p0, Landroid/os/Build$VERSION;->SDK_INT:I

    const/16 v0, 0xe

    if-lt p0, v0, :cond_0

    .line 3
    const-class p0, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView;

    invoke-virtual {p0}, Ljava/lang/Class;->getName()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p1, p0}, Landroid/view/accessibility/AccessibilityNodeInfo;->setClassName(Ljava/lang/CharSequence;)V

    :cond_0
    return-void
.end method

.method protected onMeasure(II)V
    .locals 1

    .line 1
    iget-object v0, p0, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView;->mMeasureHelper:Ltv/danmaku/ijk/media/player/tw/MeasureHelper;

    invoke-virtual {v0, p1, p2}, Ltv/danmaku/ijk/media/player/tw/MeasureHelper;->doMeasure(II)V

    .line 2
    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView;->mMeasureHelper:Ltv/danmaku/ijk/media/player/tw/MeasureHelper;

    invoke-virtual {p1}, Ltv/danmaku/ijk/media/player/tw/MeasureHelper;->getMeasuredWidth()I

    move-result p1

    iget-object p2, p0, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView;->mMeasureHelper:Ltv/danmaku/ijk/media/player/tw/MeasureHelper;

    invoke-virtual {p2}, Ltv/danmaku/ijk/media/player/tw/MeasureHelper;->getMeasuredHeight()I

    move-result p2

    invoke-virtual {p0, p1, p2}, Landroid/view/SurfaceView;->setMeasuredDimension(II)V

    return-void
.end method

.method public removeRenderCallback(Ltv/danmaku/ijk/media/player/tw/IRenderView$IRenderCallback;)V
    .locals 0

    .line 1
    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView;->mSurfaceCallback:Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$SurfaceCallback;

    invoke-virtual {p0, p1}, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView$SurfaceCallback;->removeRenderCallback(Ltv/danmaku/ijk/media/player/tw/IRenderView$IRenderCallback;)V

    return-void
.end method

.method public setAspectRatio(I)V
    .locals 1

    .line 1
    iget-object v0, p0, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView;->mMeasureHelper:Ltv/danmaku/ijk/media/player/tw/MeasureHelper;

    invoke-virtual {v0, p1}, Ltv/danmaku/ijk/media/player/tw/MeasureHelper;->setAspectRatio(I)V

    .line 2
    invoke-virtual {p0}, Landroid/view/SurfaceView;->requestLayout()V

    return-void
.end method

.method public setVideoRotation(I)V
    .locals 1

    .line 1
    new-instance p0, Ljava/lang/StringBuilder;

    invoke-direct {p0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v0, "SurfaceView doesn\'t support rotation ("

    invoke-virtual {p0, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0, p1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string p1, ")!\n"

    invoke-virtual {p0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    const-string p1, ""

    invoke-static {p1, p0}, Landroid/util/Log;->e(Ljava/lang/String;Ljava/lang/String;)I

    return-void
.end method

.method public setVideoSampleAspectRatio(II)V
    .locals 1

    if-lez p1, :cond_0

    if-lez p2, :cond_0

    .line 1
    iget-object v0, p0, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView;->mMeasureHelper:Ltv/danmaku/ijk/media/player/tw/MeasureHelper;

    invoke-virtual {v0, p1, p2}, Ltv/danmaku/ijk/media/player/tw/MeasureHelper;->setVideoSampleAspectRatio(II)V

    .line 2
    invoke-virtual {p0}, Landroid/view/SurfaceView;->requestLayout()V

    :cond_0
    return-void
.end method

.method public setVideoSize(II)V
    .locals 1

    if-lez p1, :cond_0

    if-lez p2, :cond_0

    .line 1
    iget-object v0, p0, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView;->mMeasureHelper:Ltv/danmaku/ijk/media/player/tw/MeasureHelper;

    invoke-virtual {v0, p1, p2}, Ltv/danmaku/ijk/media/player/tw/MeasureHelper;->setVideoSize(II)V

    .line 2
    invoke-virtual {p0}, Landroid/view/SurfaceView;->getHolder()Landroid/view/SurfaceHolder;

    move-result-object v0

    invoke-interface {v0, p1, p2}, Landroid/view/SurfaceHolder;->setFixedSize(II)V

    .line 3
    invoke-virtual {p0}, Landroid/view/SurfaceView;->requestLayout()V

    :cond_0
    return-void
.end method

.method public shouldWaitForResize()Z
    .locals 0

    const/4 p0, 0x1

    return p0
.end method
