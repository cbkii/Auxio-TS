.class public Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;
.super Landroid/widget/FrameLayout;
# vendor-boundary: persist.tw.ijk* system properties gate playback engine/runtime behavior; do not rename/alter keys.

.source "TWMediaPlayerView.java"


# static fields
.field public static final PV_PLAYER__AndroidMediaPlayer:I = 0x1

.field public static final PV_PLAYER__Auto:I = 0x0

.field public static final PV_PLAYER__IjkExoMediaPlayer:I = 0x3

.field public static final PV_PLAYER__IjkMediaPlayer:I = 0x2

.field public static final RENDER_NONE:I = 0x0

.field public static final RENDER_SURFACE_VIEW:I = 0x1

.field public static final RENDER_TEXTURE_VIEW:I = 0x2

.field private static final STATE_ERROR:I = -0x1

.field private static final STATE_IDLE:I = 0x0

.field private static final STATE_PAUSED:I = 0x4

.field private static final STATE_PLAYBACK_COMPLETED:I = 0x5

.field private static final STATE_PLAYING:I = 0x3

.field private static final STATE_PREPARED:I = 0x2

.field private static final STATE_PREPARING:I = 0x1

.field private static final SYSTEM_ETC:Ljava/lang/String; = "/system/etc/"

.field private static final SYSTEM_TW_ETC:Ljava/lang/String; = "/system_tw/etc/"

.field public static final TWIjkNoError_ERROR:I = 0x1

.field public static final TWIjkNoError_NOERROR:I = 0x0

.field public static final TWIjkOpenSLES_OFF:I = 0x0

.field public static final TWIjkOpenSLES_ON:I = 0x1

.field public static final TWIjk_Android:I = 0x3

.field public static final TWIjk_AndroidIjk:I = 0x0

.field public static final TWIjk_Exo:I = 0x4

.field public static final TWIjk_ExoIjk:I = 0x1

.field public static final TWIjk_Ijk:I = 0x2

.field private static final mAndroidVideoList:Ljava/util/ArrayList;
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "Ljava/util/ArrayList<",
            "Ljava/lang/String;",
            ">;"
        }
    .end annotation
.end field

.field private static final mExoVideoList:Ljava/util/ArrayList;
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "Ljava/util/ArrayList<",
            "Ljava/lang/String;",
            ">;"
        }
    .end annotation
.end field

.field private static final mVideoList:Ljava/util/ArrayList;
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "Ljava/util/ArrayList<",
            "Ljava/lang/String;",
            ">;"
        }
    .end annotation
.end field


# instance fields
.field private TAG:Ljava/lang/String;

.field private mBufferingUpdateListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnBufferingUpdateListener;

.field private mCompletionListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnCompletionListener;

.field private mCurrentBufferPercentage:I

.field private mCurrentState:I

.field private mErrorListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnErrorListener;

.field private mHeaders:Ljava/util/Map;
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "Ljava/util/Map<",
            "Ljava/lang/String;",
            "Ljava/lang/String;",
            ">;"
        }
    .end annotation
.end field

.field private mInfoListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnInfoListener;

.field private mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

.field private mOnCompletionListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnCompletionListener;

.field private mOnErrorListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnErrorListener;

.field private mOnInfoListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnInfoListener;

.field private mOnPreparedListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnPreparedListener;

.field private mOnTimedTextListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnTimedTextListener;

.field private mPlayerType:I

.field private mPrepareEndTime:J

.field private mPrepareStartTime:J

.field mPreparedListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnPreparedListener;

.field private mRenderView:Ltv/danmaku/ijk/media/player/tw/IRenderView;

.field mSHCallback:Ltv/danmaku/ijk/media/player/tw/IRenderView$IRenderCallback;

.field private mSeekCompleteListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnSeekCompleteListener;

.field private mSeekEndTime:J

.field private mSeekStartTime:J

.field private mSeekWhenPrepared:I

.field mSizeChangedListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnVideoSizeChangedListener;

.field private mSurfaceHeight:I

.field private mSurfaceHolder:Ltv/danmaku/ijk/media/player/tw/IRenderView$ISurfaceHolder;

.field private mSurfaceWidth:I

.field private mTargetState:I

.field private mUri:Landroid/net/Uri;

.field private mVideoHeight:I

.field private mVideoRotationDegree:I

.field private mVideoSarDen:I

.field private mVideoSarNum:I

.field private mVideoWidth:I

.field protected subtitleDisplay:Landroid/widget/TextView;


# direct methods
.method static constructor <clinit>()V
    .locals 1

    const-string v0, "ijk.video"

    .line 1
    invoke-static {v0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->readMediaList2(Ljava/lang/String;)Ljava/util/ArrayList;

    move-result-object v0

    sput-object v0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mVideoList:Ljava/util/ArrayList;

    const-string v0, "android.video"

    .line 2
    invoke-static {v0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->readMediaList2(Ljava/lang/String;)Ljava/util/ArrayList;

    move-result-object v0

    sput-object v0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mAndroidVideoList:Ljava/util/ArrayList;

    const-string v0, "exo.video"

    .line 3
    invoke-static {v0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->readMediaList2(Ljava/lang/String;)Ljava/util/ArrayList;

    move-result-object v0

    sput-object v0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mExoVideoList:Ljava/util/ArrayList;

    return-void
.end method

.method public constructor <init>(Landroid/content/Context;)V
    .locals 3

    .line 1
    invoke-direct {p0, p1}, Landroid/widget/FrameLayout;-><init>(Landroid/content/Context;)V

    const-string p1, "TWMediaPlayerView"

    .line 2
    iput-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->TAG:Ljava/lang/String;

    const/4 p1, 0x0

    .line 3
    iput p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mCurrentState:I

    .line 4
    iput p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mTargetState:I

    const/4 v0, 0x0

    .line 5
    iput-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mSurfaceHolder:Ltv/danmaku/ijk/media/player/tw/IRenderView$ISurfaceHolder;

    .line 6
    iput-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    .line 7
    iput-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mRenderView:Ltv/danmaku/ijk/media/player/tw/IRenderView;

    const-wide/16 v1, 0x0

    .line 8
    iput-wide v1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mPrepareStartTime:J

    .line 9
    iput-wide v1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mPrepareEndTime:J

    .line 10
    iput-wide v1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mSeekStartTime:J

    .line 11
    iput-wide v1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mSeekEndTime:J

    .line 12
    iput-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->subtitleDisplay:Landroid/widget/TextView;

    .line 13
    new-instance v0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$1;

    invoke-direct {v0, p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$1;-><init>(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)V

    iput-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mSizeChangedListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnVideoSizeChangedListener;

    .line 14
    new-instance v0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$2;

    invoke-direct {v0, p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$2;-><init>(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)V

    iput-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mPreparedListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnPreparedListener;

    .line 15
    new-instance v0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$3;

    invoke-direct {v0, p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$3;-><init>(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)V

    iput-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mCompletionListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnCompletionListener;

    .line 16
    new-instance v0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$4;

    invoke-direct {v0, p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$4;-><init>(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)V

    iput-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mInfoListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnInfoListener;

    .line 17
    new-instance v0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$5;

    invoke-direct {v0, p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$5;-><init>(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)V

    iput-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mErrorListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnErrorListener;

    .line 18
    new-instance v0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$6;

    invoke-direct {v0, p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$6;-><init>(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)V

    iput-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mBufferingUpdateListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnBufferingUpdateListener;

    .line 19
    new-instance v0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$7;

    invoke-direct {v0, p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$7;-><init>(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)V

    iput-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mSeekCompleteListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnSeekCompleteListener;

    .line 20
    new-instance v0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$8;

    invoke-direct {v0, p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$8;-><init>(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)V

    iput-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mOnTimedTextListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnTimedTextListener;

    .line 21
    new-instance v0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$9;

    invoke-direct {v0, p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$9;-><init>(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)V

    iput-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mSHCallback:Ltv/danmaku/ijk/media/player/tw/IRenderView$IRenderCallback;

    .line 22
    iput p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mPlayerType:I

    .line 23
    invoke-direct {p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->initMP()V

    return-void
.end method

.method public constructor <init>(Landroid/content/Context;Landroid/util/AttributeSet;)V
    .locals 2

    .line 24
    invoke-direct {p0, p1, p2}, Landroid/widget/FrameLayout;-><init>(Landroid/content/Context;Landroid/util/AttributeSet;)V

    const-string p1, "TWMediaPlayerView"

    .line 25
    iput-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->TAG:Ljava/lang/String;

    const/4 p1, 0x0

    .line 26
    iput p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mCurrentState:I

    .line 27
    iput p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mTargetState:I

    const/4 p2, 0x0

    .line 28
    iput-object p2, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mSurfaceHolder:Ltv/danmaku/ijk/media/player/tw/IRenderView$ISurfaceHolder;

    .line 29
    iput-object p2, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    .line 30
    iput-object p2, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mRenderView:Ltv/danmaku/ijk/media/player/tw/IRenderView;

    const-wide/16 v0, 0x0

    .line 31
    iput-wide v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mPrepareStartTime:J

    .line 32
    iput-wide v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mPrepareEndTime:J

    .line 33
    iput-wide v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mSeekStartTime:J

    .line 34
    iput-wide v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mSeekEndTime:J

    .line 35
    iput-object p2, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->subtitleDisplay:Landroid/widget/TextView;

    .line 36
    new-instance p2, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$1;

    invoke-direct {p2, p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$1;-><init>(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)V

    iput-object p2, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mSizeChangedListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnVideoSizeChangedListener;

    .line 37
    new-instance p2, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$2;

    invoke-direct {p2, p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$2;-><init>(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)V

    iput-object p2, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mPreparedListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnPreparedListener;

    .line 38
    new-instance p2, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$3;

    invoke-direct {p2, p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$3;-><init>(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)V

    iput-object p2, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mCompletionListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnCompletionListener;

    .line 39
    new-instance p2, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$4;

    invoke-direct {p2, p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$4;-><init>(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)V

    iput-object p2, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mInfoListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnInfoListener;

    .line 40
    new-instance p2, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$5;

    invoke-direct {p2, p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$5;-><init>(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)V

    iput-object p2, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mErrorListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnErrorListener;

    .line 41
    new-instance p2, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$6;

    invoke-direct {p2, p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$6;-><init>(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)V

    iput-object p2, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mBufferingUpdateListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnBufferingUpdateListener;

    .line 42
    new-instance p2, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$7;

    invoke-direct {p2, p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$7;-><init>(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)V

    iput-object p2, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mSeekCompleteListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnSeekCompleteListener;

    .line 43
    new-instance p2, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$8;

    invoke-direct {p2, p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$8;-><init>(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)V

    iput-object p2, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mOnTimedTextListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnTimedTextListener;

    .line 44
    new-instance p2, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$9;

    invoke-direct {p2, p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$9;-><init>(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)V

    iput-object p2, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mSHCallback:Ltv/danmaku/ijk/media/player/tw/IRenderView$IRenderCallback;

    .line 45
    iput p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mPlayerType:I

    .line 46
    invoke-direct {p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->initMP()V

    return-void
.end method

.method public constructor <init>(Landroid/content/Context;Landroid/util/AttributeSet;I)V
    .locals 2

    .line 47
    invoke-direct {p0, p1, p2, p3}, Landroid/widget/FrameLayout;-><init>(Landroid/content/Context;Landroid/util/AttributeSet;I)V

    const-string p1, "TWMediaPlayerView"

    .line 48
    iput-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->TAG:Ljava/lang/String;

    const/4 p1, 0x0

    .line 49
    iput p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mCurrentState:I

    .line 50
    iput p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mTargetState:I

    const/4 p2, 0x0

    .line 51
    iput-object p2, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mSurfaceHolder:Ltv/danmaku/ijk/media/player/tw/IRenderView$ISurfaceHolder;

    .line 52
    iput-object p2, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    .line 53
    iput-object p2, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mRenderView:Ltv/danmaku/ijk/media/player/tw/IRenderView;

    const-wide/16 v0, 0x0

    .line 54
    iput-wide v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mPrepareStartTime:J

    .line 55
    iput-wide v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mPrepareEndTime:J

    .line 56
    iput-wide v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mSeekStartTime:J

    .line 57
    iput-wide v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mSeekEndTime:J

    .line 58
    iput-object p2, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->subtitleDisplay:Landroid/widget/TextView;

    .line 59
    new-instance p2, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$1;

    invoke-direct {p2, p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$1;-><init>(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)V

    iput-object p2, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mSizeChangedListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnVideoSizeChangedListener;

    .line 60
    new-instance p2, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$2;

    invoke-direct {p2, p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$2;-><init>(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)V

    iput-object p2, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mPreparedListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnPreparedListener;

    .line 61
    new-instance p2, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$3;

    invoke-direct {p2, p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$3;-><init>(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)V

    iput-object p2, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mCompletionListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnCompletionListener;

    .line 62
    new-instance p2, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$4;

    invoke-direct {p2, p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$4;-><init>(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)V

    iput-object p2, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mInfoListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnInfoListener;

    .line 63
    new-instance p2, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$5;

    invoke-direct {p2, p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$5;-><init>(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)V

    iput-object p2, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mErrorListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnErrorListener;

    .line 64
    new-instance p2, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$6;

    invoke-direct {p2, p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$6;-><init>(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)V

    iput-object p2, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mBufferingUpdateListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnBufferingUpdateListener;

    .line 65
    new-instance p2, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$7;

    invoke-direct {p2, p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$7;-><init>(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)V

    iput-object p2, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mSeekCompleteListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnSeekCompleteListener;

    .line 66
    new-instance p2, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$8;

    invoke-direct {p2, p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$8;-><init>(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)V

    iput-object p2, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mOnTimedTextListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnTimedTextListener;

    .line 67
    new-instance p2, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$9;

    invoke-direct {p2, p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$9;-><init>(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)V

    iput-object p2, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mSHCallback:Ltv/danmaku/ijk/media/player/tw/IRenderView$IRenderCallback;

    .line 68
    iput p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mPlayerType:I

    .line 69
    invoke-direct {p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->initMP()V

    return-void
.end method

.method public constructor <init>(Landroid/content/Context;Landroid/util/AttributeSet;II)V
    .locals 0
    .annotation build Landroid/annotation/TargetApi;
        value = 0x15
    .end annotation

    .line 70
    invoke-direct {p0, p1, p2, p3, p4}, Landroid/widget/FrameLayout;-><init>(Landroid/content/Context;Landroid/util/AttributeSet;II)V

    const-string p1, "TWMediaPlayerView"

    .line 71
    iput-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->TAG:Ljava/lang/String;

    const/4 p1, 0x0

    .line 72
    iput p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mCurrentState:I

    .line 73
    iput p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mTargetState:I

    const/4 p2, 0x0

    .line 74
    iput-object p2, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mSurfaceHolder:Ltv/danmaku/ijk/media/player/tw/IRenderView$ISurfaceHolder;

    .line 75
    iput-object p2, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    .line 76
    iput-object p2, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mRenderView:Ltv/danmaku/ijk/media/player/tw/IRenderView;

    const-wide/16 p3, 0x0

    .line 77
    iput-wide p3, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mPrepareStartTime:J

    .line 78
    iput-wide p3, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mPrepareEndTime:J

    .line 79
    iput-wide p3, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mSeekStartTime:J

    .line 80
    iput-wide p3, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mSeekEndTime:J

    .line 81
    iput-object p2, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->subtitleDisplay:Landroid/widget/TextView;

    .line 82
    new-instance p2, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$1;

    invoke-direct {p2, p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$1;-><init>(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)V

    iput-object p2, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mSizeChangedListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnVideoSizeChangedListener;

    .line 83
    new-instance p2, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$2;

    invoke-direct {p2, p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$2;-><init>(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)V

    iput-object p2, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mPreparedListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnPreparedListener;

    .line 84
    new-instance p2, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$3;

    invoke-direct {p2, p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$3;-><init>(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)V

    iput-object p2, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mCompletionListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnCompletionListener;

    .line 85
    new-instance p2, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$4;

    invoke-direct {p2, p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$4;-><init>(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)V

    iput-object p2, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mInfoListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnInfoListener;

    .line 86
    new-instance p2, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$5;

    invoke-direct {p2, p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$5;-><init>(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)V

    iput-object p2, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mErrorListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnErrorListener;

    .line 87
    new-instance p2, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$6;

    invoke-direct {p2, p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$6;-><init>(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)V

    iput-object p2, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mBufferingUpdateListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnBufferingUpdateListener;

    .line 88
    new-instance p2, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$7;

    invoke-direct {p2, p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$7;-><init>(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)V

    iput-object p2, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mSeekCompleteListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnSeekCompleteListener;

    .line 89
    new-instance p2, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$8;

    invoke-direct {p2, p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$8;-><init>(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)V

    iput-object p2, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mOnTimedTextListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnTimedTextListener;

    .line 90
    new-instance p2, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$9;

    invoke-direct {p2, p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView$9;-><init>(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)V

    iput-object p2, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mSHCallback:Ltv/danmaku/ijk/media/player/tw/IRenderView$IRenderCallback;

    .line 91
    iput p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mPlayerType:I

    .line 92
    invoke-direct {p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->initMP()V

    return-void
.end method

.method static synthetic access$000(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)I
    .locals 0

    .line 1
    iget p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mVideoWidth:I

    return p0
.end method

.method static synthetic access$002(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;I)I
    .locals 0

    .line 1
    iput p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mVideoWidth:I

    return p1
.end method

.method static synthetic access$100(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)I
    .locals 0

    .line 1
    iget p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mVideoHeight:I

    return p0
.end method

.method static synthetic access$1000(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)Ltv/danmaku/ijk/media/player/IMediaPlayer;
    .locals 0

    .line 1
    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    return-object p0
.end method

.method static synthetic access$102(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;I)I
    .locals 0

    .line 1
    iput p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mVideoHeight:I

    return p1
.end method

.method static synthetic access$1100(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)I
    .locals 0

    .line 1
    iget p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mSeekWhenPrepared:I

    return p0
.end method

.method static synthetic access$1200(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)I
    .locals 0

    .line 1
    iget p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mSurfaceWidth:I

    return p0
.end method

.method static synthetic access$1202(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;I)I
    .locals 0

    .line 1
    iput p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mSurfaceWidth:I

    return p1
.end method

.method static synthetic access$1300(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)I
    .locals 0

    .line 1
    iget p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mSurfaceHeight:I

    return p0
.end method

.method static synthetic access$1302(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;I)I
    .locals 0

    .line 1
    iput p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mSurfaceHeight:I

    return p1
.end method

.method static synthetic access$1400(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)I
    .locals 0

    .line 1
    iget p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mTargetState:I

    return p0
.end method

.method static synthetic access$1402(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;I)I
    .locals 0

    .line 1
    iput p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mTargetState:I

    return p1
.end method

.method static synthetic access$1500(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)Ltv/danmaku/ijk/media/player/IMediaPlayer$OnCompletionListener;
    .locals 0

    .line 1
    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mOnCompletionListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnCompletionListener;

    return-object p0
.end method

.method static synthetic access$1600(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)Ltv/danmaku/ijk/media/player/IMediaPlayer$OnInfoListener;
    .locals 0

    .line 1
    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mOnInfoListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnInfoListener;

    return-object p0
.end method

.method static synthetic access$1702(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;I)I
    .locals 0

    .line 1
    iput p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mVideoRotationDegree:I

    return p1
.end method

.method static synthetic access$1800(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)Ltv/danmaku/ijk/media/player/IMediaPlayer$OnErrorListener;
    .locals 0

    .line 1
    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mOnErrorListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnErrorListener;

    return-object p0
.end method

.method static synthetic access$1902(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;I)I
    .locals 0

    .line 1
    iput p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mCurrentBufferPercentage:I

    return p1
.end method

.method static synthetic access$200(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)I
    .locals 0

    .line 1
    iget p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mVideoSarNum:I

    return p0
.end method

.method static synthetic access$2000(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)J
    .locals 2

    .line 1
    iget-wide v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mSeekEndTime:J

    return-wide v0
.end method

.method static synthetic access$2002(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;J)J
    .locals 0

    .line 1
    iput-wide p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mSeekEndTime:J

    return-wide p1
.end method

.method static synthetic access$202(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;I)I
    .locals 0

    .line 1
    iput p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mVideoSarNum:I

    return p1
.end method

.method static synthetic access$2100(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)J
    .locals 2

    .line 1
    iget-wide v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mSeekStartTime:J

    return-wide v0
.end method

.method static synthetic access$2202(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;Ltv/danmaku/ijk/media/player/tw/IRenderView$ISurfaceHolder;)Ltv/danmaku/ijk/media/player/tw/IRenderView$ISurfaceHolder;
    .locals 0

    .line 1
    iput-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mSurfaceHolder:Ltv/danmaku/ijk/media/player/tw/IRenderView$ISurfaceHolder;

    return-object p1
.end method

.method static synthetic access$2300(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;Ltv/danmaku/ijk/media/player/IMediaPlayer;Ltv/danmaku/ijk/media/player/tw/IRenderView$ISurfaceHolder;)V
    .locals 0

    .line 1
    invoke-direct {p0, p1, p2}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->bindSurfaceHolder(Ltv/danmaku/ijk/media/player/IMediaPlayer;Ltv/danmaku/ijk/media/player/tw/IRenderView$ISurfaceHolder;)V

    return-void
.end method

.method static synthetic access$300(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)I
    .locals 0

    .line 1
    iget p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mVideoSarDen:I

    return p0
.end method

.method static synthetic access$302(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;I)I
    .locals 0

    .line 1
    iput p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mVideoSarDen:I

    return p1
.end method

.method static synthetic access$400(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)Ltv/danmaku/ijk/media/player/tw/IRenderView;
    .locals 0

    .line 1
    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mRenderView:Ltv/danmaku/ijk/media/player/tw/IRenderView;

    return-object p0
.end method

.method static synthetic access$500(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)J
    .locals 2

    .line 1
    iget-wide v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mPrepareEndTime:J

    return-wide v0
.end method

.method static synthetic access$502(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;J)J
    .locals 0

    .line 1
    iput-wide p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mPrepareEndTime:J

    return-wide p1
.end method

.method static synthetic access$600(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)Ljava/lang/String;
    .locals 0

    .line 1
    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->TAG:Ljava/lang/String;

    return-object p0
.end method

.method static synthetic access$700(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)J
    .locals 2

    .line 1
    iget-wide v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mPrepareStartTime:J

    return-wide v0
.end method

.method static synthetic access$802(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;I)I
    .locals 0

    .line 1
    iput p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mCurrentState:I

    return p1
.end method

.method static synthetic access$900(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;)Ltv/danmaku/ijk/media/player/IMediaPlayer$OnPreparedListener;
    .locals 0

    .line 1
    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mOnPreparedListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnPreparedListener;

    return-object p0
.end method

.method private bindSurfaceHolder(Ltv/danmaku/ijk/media/player/IMediaPlayer;Ltv/danmaku/ijk/media/player/tw/IRenderView$ISurfaceHolder;)V
    .locals 0

    if-nez p1, :cond_0

    return-void

    :cond_0
    if-nez p2, :cond_1

    const/4 p0, 0x0

    .line 1
    :try_start_0
    invoke-interface {p1, p0}, Ltv/danmaku/ijk/media/player/IMediaPlayer;->setDisplay(Landroid/view/SurfaceHolder;)V

    return-void

    .line 2
    :cond_1
    invoke-interface {p2, p1}, Ltv/danmaku/ijk/media/player/tw/IRenderView$ISurfaceHolder;->bindToMediaPlayer(Ltv/danmaku/ijk/media/player/IMediaPlayer;)V
    :try_end_0
    .catch Ljava/lang/Exception; {:try_start_0 .. :try_end_0} :catch_0

    :catch_0
    return-void
.end method

.method private createPlayer()Ltv/danmaku/ijk/media/player/IMediaPlayer;
    .locals 9

    .line 1
    iget v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mPlayerType:I

    const/4 v1, 0x1

    if-eq v0, v1, :cond_1

    const/4 v0, 0x0

    .line 2
    iget-object v2, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mUri:Landroid/net/Uri;

    if-eqz v2, :cond_2

    .line 3
    new-instance v0, Ltv/danmaku/ijk/media/player/IjkMediaPlayer;

    invoke-direct {v0}, Ltv/danmaku/ijk/media/player/IjkMediaPlayer;-><init>()V

    const/16 v2, 0x8

    .line 4
    invoke-static {v2}, Ltv/danmaku/ijk/media/player/IjkMediaPlayer;->native_setLogLevel(I)V

    const-wide/16 v2, 0x0

    const/4 v4, 0x4

    const-string v5, "mediacodec"

    .line 5
    invoke-virtual {v0, v4, v5, v2, v3}, Ltv/danmaku/ijk/media/player/IjkMediaPlayer;->setOption(ILjava/lang/String;J)V

    .line 6
    invoke-virtual {p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->getTWIjkOpenSLES()I

    move-result p0

    const-wide/16 v5, 0x1

    if-nez p0, :cond_0

    move-wide v7, v2

    goto :goto_0

    :cond_0
    move-wide v7, v5

    :goto_0
    const-string p0, "opensles"

    invoke-virtual {v0, v4, p0, v7, v8}, Ltv/danmaku/ijk/media/player/IjkMediaPlayer;->setOption(ILjava/lang/String;J)V

    const-wide/32 v7, 0x32335652

    const-string p0, "overlay-format"

    .line 7
    invoke-virtual {v0, v4, p0, v7, v8}, Ltv/danmaku/ijk/media/player/IjkMediaPlayer;->setOption(ILjava/lang/String;J)V

    const-string p0, "framedrop"

    .line 8
    invoke-virtual {v0, v4, p0, v5, v6}, Ltv/danmaku/ijk/media/player/IjkMediaPlayer;->setOption(ILjava/lang/String;J)V

    const-string p0, "start-on-prepared"

    .line 9
    invoke-virtual {v0, v4, p0, v2, v3}, Ltv/danmaku/ijk/media/player/IjkMediaPlayer;->setOption(ILjava/lang/String;J)V

    const-string p0, "http-detect-range-support"

    .line 10
    invoke-virtual {v0, v1, p0, v2, v3}, Ltv/danmaku/ijk/media/player/IjkMediaPlayer;->setOption(ILjava/lang/String;J)V

    const/4 p0, 0x2

    const-wide/16 v1, 0x30

    const-string v3, "skip_loop_filter"

    .line 11
    invoke-virtual {v0, p0, v3, v1, v2}, Ltv/danmaku/ijk/media/player/IjkMediaPlayer;->setOption(ILjava/lang/String;J)V

    goto :goto_1

    .line 12
    :cond_1
    new-instance v0, Ltv/danmaku/ijk/media/player/AndroidMediaPlayer;

    invoke-direct {v0}, Ltv/danmaku/ijk/media/player/AndroidMediaPlayer;-><init>()V

    :cond_2
    :goto_1
    return-object v0
.end method

.method private initMP()V
    .locals 2

    const/4 v0, 0x0

    .line 1
    iput v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mVideoWidth:I

    .line 2
    iput v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mVideoHeight:I

    const/4 v1, 0x1

    .line 3
    invoke-virtual {p0, v1}, Landroid/widget/FrameLayout;->setFocusable(Z)V

    .line 4
    invoke-virtual {p0, v1}, Landroid/widget/FrameLayout;->setFocusableInTouchMode(Z)V

    .line 5
    invoke-virtual {p0}, Landroid/widget/FrameLayout;->requestFocus()Z

    .line 6
    iput v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mCurrentState:I

    .line 7
    iput v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mTargetState:I

    .line 8
    invoke-virtual {p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->initSubtitleDisplay()V

    return-void
.end method

.method private isInPlaybackState()Z
    .locals 2

    .line 1
    iget-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    const/4 v1, 0x1

    if-eqz v0, :cond_0

    iget p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mCurrentState:I

    const/4 v0, -0x1

    if-eq p0, v0, :cond_0

    if-eqz p0, :cond_0

    if-eq p0, v1, :cond_0

    goto :goto_0

    :cond_0
    const/4 v1, 0x0

    :goto_0
    return v1
.end method

.method public static isVideo(Ljava/lang/String;)Z
    .locals 2

    .line 1
    sget-object v0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mVideoList:Ljava/util/ArrayList;

    if-eqz v0, :cond_1

    .line 2
    invoke-virtual {v0}, Ljava/util/ArrayList;->iterator()Ljava/util/Iterator;

    move-result-object v0

    :cond_0
    invoke-interface {v0}, Ljava/util/Iterator;->hasNext()Z

    move-result v1

    if-eqz v1, :cond_1

    invoke-interface {v0}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Ljava/lang/String;

    .line 3
    invoke-virtual {p0, v1}, Ljava/lang/String;->endsWith(Ljava/lang/String;)Z

    move-result v1

    if-eqz v1, :cond_0

    const/4 p0, 0x1

    return p0

    :cond_1
    const/4 p0, 0x0

    return p0
.end method

.method private makePlayerType(I)V
    .locals 3

    if-nez p1, :cond_3

    .line 1
    invoke-virtual {p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->getTWIjk()I

    move-result p1

    const/4 v0, 0x1

    const/4 v1, 0x2

    if-eqz p1, :cond_1

    const/4 v2, 0x3

    if-eq p1, v2, :cond_0

    .line 2
    iput v1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mPlayerType:I

    goto :goto_0

    .line 3
    :cond_0
    iput v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mPlayerType:I

    goto :goto_0

    .line 4
    :cond_1
    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mUri:Landroid/net/Uri;

    invoke-virtual {p1}, Landroid/net/Uri;->toString()Ljava/lang/String;

    move-result-object p1

    sget-object v2, Ljava/util/Locale;->ENGLISH:Ljava/util/Locale;

    invoke-virtual {p1, v2}, Ljava/lang/String;->toUpperCase(Ljava/util/Locale;)Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->isAndroidVideo(Ljava/lang/String;)Z

    move-result p1

    if-eqz p1, :cond_2

    .line 5
    iput v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mPlayerType:I

    goto :goto_0

    .line 6
    :cond_2
    iput v1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mPlayerType:I

    goto :goto_0

    .line 7
    :cond_3
    iput p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mPlayerType:I

    :goto_0
    return-void
.end method

.method private static readMediaList(Ljava/lang/String;)Ljava/util/ArrayList;
    .locals 3
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "(",
            "Ljava/lang/String;",
            ")",
            "Ljava/util/ArrayList<",
            "Ljava/lang/String;",
            ">;"
        }
    .end annotation

    const/4 v0, 0x0

    .line 1
    :try_start_0
    new-instance v1, Ljava/io/BufferedReader;

    new-instance v2, Ljava/io/FileReader;

    invoke-direct {v2, p0}, Ljava/io/FileReader;-><init>(Ljava/lang/String;)V

    invoke-direct {v1, v2}, Ljava/io/BufferedReader;-><init>(Ljava/io/Reader;)V
    :try_end_0
    .catch Ljava/lang/Exception; {:try_start_0 .. :try_end_0} :catch_0
    .catchall {:try_start_0 .. :try_end_0} :catchall_1

    .line 2
    :try_start_1
    new-instance p0, Ljava/util/ArrayList;

    invoke-direct {p0}, Ljava/util/ArrayList;-><init>()V

    .line 3
    :goto_0
    invoke-virtual {v1}, Ljava/io/BufferedReader;->readLine()Ljava/lang/String;

    move-result-object v2

    if-eqz v2, :cond_0

    .line 4
    invoke-virtual {p0, v2}, Ljava/util/ArrayList;->add(Ljava/lang/Object;)Z
    :try_end_1
    .catch Ljava/lang/Exception; {:try_start_1 .. :try_end_1} :catch_1
    .catchall {:try_start_1 .. :try_end_1} :catchall_0

    goto :goto_0

    .line 5
    :cond_0
    :try_start_2
    invoke-virtual {v1}, Ljava/io/BufferedReader;->close()V

    return-object p0

    :catchall_0
    move-exception p0

    goto :goto_1

    :catchall_1
    move-exception p0

    move-object v1, v0

    :goto_1
    if-eqz v1, :cond_1

    invoke-virtual {v1}, Ljava/io/BufferedReader;->close()V

    .line 6
    :cond_1
    throw p0

    :catch_0
    move-object v1, v0

    :catch_1
    if-eqz v1, :cond_2

    .line 7
    invoke-virtual {v1}, Ljava/io/BufferedReader;->close()V
    :try_end_2
    .catch Ljava/lang/Exception; {:try_start_2 .. :try_end_2} :catch_2

    :catch_2
    :cond_2
    return-object v0
.end method

.method private static readMediaList2(Ljava/lang/String;)Ljava/util/ArrayList;
    .locals 3
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "(",
            "Ljava/lang/String;",
            ")",
            "Ljava/util/ArrayList<",
            "Ljava/lang/String;",
            ">;"
        }
    .end annotation

    .line 1
    new-instance v0, Ljava/io/File;

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "/system/etc/"

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-direct {v0, v1}, Ljava/io/File;-><init>(Ljava/lang/String;)V

    invoke-virtual {v0}, Ljava/io/File;->canRead()Z

    move-result v0

    if-eqz v0, :cond_0

    .line 2
    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    invoke-static {p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->readMediaList(Ljava/lang/String;)Ljava/util/ArrayList;

    move-result-object p0

    return-object p0

    .line 3
    :cond_0
    new-instance v0, Ljava/io/File;

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "/system_tw/etc/"

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-direct {v0, v1}, Ljava/io/File;-><init>(Ljava/lang/String;)V

    invoke-virtual {v0}, Ljava/io/File;->canRead()Z

    move-result v0

    if-eqz v0, :cond_1

    .line 4
    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    invoke-static {p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->readMediaList(Ljava/lang/String;)Ljava/util/ArrayList;

    move-result-object p0

    return-object p0

    :cond_1
    const/4 p0, 0x0

    return-object p0
.end method

.method private setMPURI(Landroid/net/Uri;Ljava/util/Map;)V
    .locals 0
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "(",
            "Landroid/net/Uri;",
            "Ljava/util/Map<",
            "Ljava/lang/String;",
            "Ljava/lang/String;",
            ">;)V"
        }
    .end annotation

    .line 2
    iput-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mUri:Landroid/net/Uri;

    .line 3
    iput-object p2, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mHeaders:Ljava/util/Map;

    const/4 p1, 0x0

    .line 4
    iput p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mSeekWhenPrepared:I

    .line 5
    invoke-virtual {p0, p1}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->openMP(I)V

    .line 6
    invoke-virtual {p0}, Landroid/widget/FrameLayout;->requestLayout()V

    .line 7
    invoke-virtual {p0}, Landroid/widget/FrameLayout;->invalidate()V

    return-void
.end method


# virtual methods
.method public canPause()Z
    .locals 0

    const/4 p0, 0x1

    return p0
.end method

.method public canSeekBackward()Z
    .locals 0

    const/4 p0, 0x1

    return p0
.end method

.method public canSeekForward()Z
    .locals 0

    const/4 p0, 0x1

    return p0
.end method

.method public currentAspectRatio()I
    .locals 0

    const/4 p0, 0x0

    return p0
.end method

.method public currentRender()I
    .locals 0

    const/4 p0, 0x1

    return p0
.end method

.method public getAudioSessionId()I
    .locals 0

    .line 1
    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    if-eqz p0, :cond_0

    .line 2
    invoke-interface {p0}, Ltv/danmaku/ijk/media/player/IMediaPlayer;->getAudioSessionId()I

    move-result p0

    return p0

    :cond_0
    const/4 p0, 0x0

    return p0
.end method

.method public getBufferPercentage()I
    .locals 1

    .line 1
    iget-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    if-eqz v0, :cond_0

    .line 2
    iget p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mCurrentBufferPercentage:I

    return p0

    :cond_0
    const/4 p0, 0x0

    return p0
.end method

.method public getCurrentPosition()I
    .locals 2

    .line 1
    invoke-direct {p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->isInPlaybackState()Z

    move-result v0

    if-eqz v0, :cond_0

    .line 2
    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    invoke-interface {p0}, Ltv/danmaku/ijk/media/player/IMediaPlayer;->getCurrentPosition()J

    move-result-wide v0

    long-to-int p0, v0

    return p0

    :cond_0
    const/4 p0, 0x0

    return p0
.end method

.method public getDuration()I
    .locals 2

    .line 1
    invoke-direct {p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->isInPlaybackState()Z

    move-result v0

    if-eqz v0, :cond_0

    .line 2
    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    invoke-interface {p0}, Ltv/danmaku/ijk/media/player/IMediaPlayer;->getDuration()J

    move-result-wide v0

    long-to-int p0, v0

    return p0

    :cond_0
    const/4 p0, -0x1

    return p0
.end method

.method public getPlayerType()I
    .locals 0

    .line 1
    iget p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mPlayerType:I

    return p0
.end method

.method public getTWIjk()I
    .locals 1

    const-string p0, "persist.tw.ijk"

    const/4 v0, 0x0

    .line 1
    invoke-static {p0, v0}, Landroid/os/SystemProperties;->getInt(Ljava/lang/String;I)I

    move-result p0

    return p0
.end method

.method public getTWIjkNoError()I
    .locals 1

    const-string p0, "persist.tw.ijk.noerror"

    const/4 v0, 0x0

    .line 1
    invoke-static {p0, v0}, Landroid/os/SystemProperties;->getInt(Ljava/lang/String;I)I

    move-result p0

    return p0
.end method

.method public getTWIjkOpenSLES()I
    .locals 1

    const-string p0, "persist.tw.ijk.opensles"

    const/4 v0, 0x0

    .line 1
    invoke-static {p0, v0}, Landroid/os/SystemProperties;->getInt(Ljava/lang/String;I)I

    move-result p0

    return p0
.end method

.method protected initSubtitleDisplay()V
    .locals 4

    .line 1
    new-instance v0, Landroid/widget/TextView;

    invoke-virtual {p0}, Landroid/widget/FrameLayout;->getContext()Landroid/content/Context;

    move-result-object v1

    invoke-direct {v0, v1}, Landroid/widget/TextView;-><init>(Landroid/content/Context;)V

    iput-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->subtitleDisplay:Landroid/widget/TextView;

    .line 2
    iget-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->subtitleDisplay:Landroid/widget/TextView;

    const/high16 v1, 0x41c00000    # 24.0f

    invoke-virtual {v0, v1}, Landroid/widget/TextView;->setTextSize(F)V

    .line 3
    iget-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->subtitleDisplay:Landroid/widget/TextView;

    const/16 v1, 0x11

    invoke-virtual {v0, v1}, Landroid/widget/TextView;->setGravity(I)V

    .line 4
    new-instance v0, Landroid/widget/FrameLayout$LayoutParams;

    const/4 v1, -0x1

    const/4 v2, -0x2

    const/16 v3, 0x50

    invoke-direct {v0, v1, v2, v3}, Landroid/widget/FrameLayout$LayoutParams;-><init>(III)V

    .line 5
    iget-object v1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->subtitleDisplay:Landroid/widget/TextView;

    invoke-virtual {p0, v1, v0}, Landroid/widget/FrameLayout;->addView(Landroid/view/View;Landroid/view/ViewGroup$LayoutParams;)V

    return-void
.end method

.method public isAndroidVideo(Ljava/lang/String;)Z
    .locals 1

    .line 1
    sget-object p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mAndroidVideoList:Ljava/util/ArrayList;

    if-eqz p0, :cond_1

    .line 2
    invoke-virtual {p0}, Ljava/util/ArrayList;->iterator()Ljava/util/Iterator;

    move-result-object p0

    :cond_0
    invoke-interface {p0}, Ljava/util/Iterator;->hasNext()Z

    move-result v0

    if-eqz v0, :cond_1

    invoke-interface {p0}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Ljava/lang/String;

    .line 3
    invoke-virtual {p1, v0}, Ljava/lang/String;->endsWith(Ljava/lang/String;)Z

    move-result v0

    if-eqz v0, :cond_0

    const/4 p0, 0x1

    return p0

    :cond_1
    const/4 p0, 0x0

    return p0
.end method

.method public isExoVideo(Ljava/lang/String;)Z
    .locals 1

    .line 1
    sget-object p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mExoVideoList:Ljava/util/ArrayList;

    if-eqz p0, :cond_1

    .line 2
    invoke-virtual {p0}, Ljava/util/ArrayList;->iterator()Ljava/util/Iterator;

    move-result-object p0

    :cond_0
    invoke-interface {p0}, Ljava/util/Iterator;->hasNext()Z

    move-result v0

    if-eqz v0, :cond_1

    invoke-interface {p0}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Ljava/lang/String;

    .line 3
    invoke-virtual {p1, v0}, Ljava/lang/String;->endsWith(Ljava/lang/String;)Z

    move-result v0

    if-eqz v0, :cond_0

    const/4 p0, 0x1

    return p0

    :cond_1
    const/4 p0, 0x0

    return p0
.end method

.method public isPlaying()Z
    .locals 1

    .line 1
    invoke-direct {p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->isInPlaybackState()Z

    move-result v0

    if-eqz v0, :cond_0

    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    invoke-interface {p0}, Ltv/danmaku/ijk/media/player/IMediaPlayer;->isPlaying()Z

    move-result p0

    if-eqz p0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method public noError(Ltv/danmaku/ijk/media/player/IMediaPlayer;II)Z
    .locals 0

    const/16 p1, -0x3ec

    if-ne p2, p1, :cond_0

    if-eqz p3, :cond_2

    .line 1
    :cond_0
    invoke-virtual {p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->getTWIjk()I

    move-result p1

    const/4 p2, 0x1

    if-eqz p1, :cond_1

    if-ne p1, p2, :cond_2

    .line 2
    :cond_1
    invoke-virtual {p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->getTWIjkNoError()I

    move-result p1

    if-nez p1, :cond_2

    invoke-virtual {p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->getPlayerType()I

    move-result p1

    const/4 p3, 0x2

    if-eq p1, p3, :cond_2

    .line 3
    invoke-virtual {p0, p3}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->openMP(I)V

    return p2

    :cond_2
    const/4 p0, 0x0

    return p0
.end method

.method public openMP(I)V
    .locals 7

    const-string v0, "Unable to open content: "

    .line 1
    iget-object v1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mUri:Landroid/net/Uri;

    if-nez v1, :cond_0

    return-void

    :cond_0
    const/4 v1, 0x0

    .line 2
    invoke-virtual {p0, v1}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->release(Z)V

    const/16 v2, -0x3ec

    const/4 v3, -0x1

    .line 3
    :try_start_0
    invoke-virtual {p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->currentRender()I

    move-result v4

    invoke-virtual {p0, v4}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->setRender(I)V

    .line 4
    invoke-direct {p0, p1}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->makePlayerType(I)V

    .line 5
    invoke-direct {p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->createPlayer()Ltv/danmaku/ijk/media/player/IMediaPlayer;

    move-result-object p1

    iput-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    .line 6
    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    iget-object v4, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mPreparedListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnPreparedListener;

    invoke-interface {p1, v4}, Ltv/danmaku/ijk/media/player/IMediaPlayer;->setOnPreparedListener(Ltv/danmaku/ijk/media/player/IMediaPlayer$OnPreparedListener;)V

    .line 7
    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    iget-object v4, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mSizeChangedListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnVideoSizeChangedListener;

    invoke-interface {p1, v4}, Ltv/danmaku/ijk/media/player/IMediaPlayer;->setOnVideoSizeChangedListener(Ltv/danmaku/ijk/media/player/IMediaPlayer$OnVideoSizeChangedListener;)V

    .line 8
    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    iget-object v4, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mCompletionListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnCompletionListener;

    invoke-interface {p1, v4}, Ltv/danmaku/ijk/media/player/IMediaPlayer;->setOnCompletionListener(Ltv/danmaku/ijk/media/player/IMediaPlayer$OnCompletionListener;)V

    .line 9
    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    iget-object v4, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mErrorListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnErrorListener;

    invoke-interface {p1, v4}, Ltv/danmaku/ijk/media/player/IMediaPlayer;->setOnErrorListener(Ltv/danmaku/ijk/media/player/IMediaPlayer$OnErrorListener;)V

    .line 10
    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    iget-object v4, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mInfoListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnInfoListener;

    invoke-interface {p1, v4}, Ltv/danmaku/ijk/media/player/IMediaPlayer;->setOnInfoListener(Ltv/danmaku/ijk/media/player/IMediaPlayer$OnInfoListener;)V

    .line 11
    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    iget-object v4, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mBufferingUpdateListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnBufferingUpdateListener;

    invoke-interface {p1, v4}, Ltv/danmaku/ijk/media/player/IMediaPlayer;->setOnBufferingUpdateListener(Ltv/danmaku/ijk/media/player/IMediaPlayer$OnBufferingUpdateListener;)V

    .line 12
    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    iget-object v4, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mSeekCompleteListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnSeekCompleteListener;

    invoke-interface {p1, v4}, Ltv/danmaku/ijk/media/player/IMediaPlayer;->setOnSeekCompleteListener(Ltv/danmaku/ijk/media/player/IMediaPlayer$OnSeekCompleteListener;)V

    .line 13
    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    iget-object v4, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mOnTimedTextListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnTimedTextListener;

    invoke-interface {p1, v4}, Ltv/danmaku/ijk/media/player/IMediaPlayer;->setOnTimedTextListener(Ltv/danmaku/ijk/media/player/IMediaPlayer$OnTimedTextListener;)V

    .line 14
    iput v1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mCurrentBufferPercentage:I

    .line 15
    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    invoke-virtual {p0}, Landroid/widget/FrameLayout;->getContext()Landroid/content/Context;

    move-result-object v4

    invoke-virtual {v4}, Landroid/content/Context;->getApplicationContext()Landroid/content/Context;

    move-result-object v4

    iget-object v5, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mUri:Landroid/net/Uri;

    iget-object v6, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mHeaders:Ljava/util/Map;

    invoke-interface {p1, v4, v5, v6}, Ltv/danmaku/ijk/media/player/IMediaPlayer;->setDataSource(Landroid/content/Context;Landroid/net/Uri;Ljava/util/Map;)V

    .line 16
    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mSurfaceHolder:Ltv/danmaku/ijk/media/player/tw/IRenderView$ISurfaceHolder;

    if-eqz p1, :cond_1

    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    iget-object v4, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mSurfaceHolder:Ltv/danmaku/ijk/media/player/tw/IRenderView$ISurfaceHolder;

    invoke-direct {p0, p1, v4}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->bindSurfaceHolder(Ltv/danmaku/ijk/media/player/IMediaPlayer;Ltv/danmaku/ijk/media/player/tw/IRenderView$ISurfaceHolder;)V

    .line 17
    :cond_1
    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    const/4 v4, 0x3

    invoke-interface {p1, v4}, Ltv/danmaku/ijk/media/player/IMediaPlayer;->setAudioStreamType(I)V

    .line 18
    invoke-static {}, Ljava/lang/System;->currentTimeMillis()J

    move-result-wide v4

    iput-wide v4, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mPrepareStartTime:J

    .line 19
    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    invoke-interface {p1}, Ltv/danmaku/ijk/media/player/IMediaPlayer;->prepareAsync()V

    const/4 p1, 0x1

    .line 20
    iput p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mCurrentState:I
    :try_end_0
    .catch Ljava/io/IOException; {:try_start_0 .. :try_end_0} :catch_1
    .catch Ljava/lang/IllegalArgumentException; {:try_start_0 .. :try_end_0} :catch_0
    .catchall {:try_start_0 .. :try_end_0} :catchall_0

    goto :goto_0

    :catchall_0
    move-exception p0

    goto :goto_1

    :catch_0
    move-exception p1

    .line 21
    :try_start_1
    iget-object v4, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->TAG:Ljava/lang/String;

    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v5, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mUri:Landroid/net/Uri;

    invoke-virtual {v5, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-static {v4, v0, p1}, Landroid/util/Log;->w(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I

    .line 22
    iput v3, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mCurrentState:I

    .line 23
    iput v3, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mTargetState:I

    .line 24
    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mErrorListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnErrorListener;

    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    invoke-interface {p1, p0, v2, v1}, Ltv/danmaku/ijk/media/player/IMediaPlayer$OnErrorListener;->onError(Ltv/danmaku/ijk/media/player/IMediaPlayer;II)Z

    goto :goto_0

    :catch_1
    move-exception p1

    .line 25
    iget-object v4, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->TAG:Ljava/lang/String;

    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v5, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mUri:Landroid/net/Uri;

    invoke-virtual {v5, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-static {v4, v0, p1}, Landroid/util/Log;->w(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I

    .line 26
    iput v3, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mCurrentState:I

    .line 27
    iput v3, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mTargetState:I

    .line 28
    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mErrorListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnErrorListener;

    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    invoke-interface {p1, p0, v2, v1}, Ltv/danmaku/ijk/media/player/IMediaPlayer$OnErrorListener;->onError(Ltv/danmaku/ijk/media/player/IMediaPlayer;II)Z
    :try_end_1
    .catchall {:try_start_1 .. :try_end_1} :catchall_0

    :goto_0
    return-void

    .line 29
    :goto_1
    throw p0
.end method

.method public pause()V
    .locals 2

    .line 1
    invoke-direct {p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->isInPlaybackState()Z

    move-result v0

    const/4 v1, 0x4

    if-eqz v0, :cond_0

    .line 2
    iget-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    invoke-interface {v0}, Ltv/danmaku/ijk/media/player/IMediaPlayer;->isPlaying()Z

    move-result v0

    if-eqz v0, :cond_0

    .line 3
    iget-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    invoke-interface {v0}, Ltv/danmaku/ijk/media/player/IMediaPlayer;->pause()V

    .line 4
    iput v1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mCurrentState:I

    .line 5
    :cond_0
    iput v1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mTargetState:I

    return-void
.end method

.method public release(Z)V
    .locals 2

    .line 1
    iget-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    if-eqz v0, :cond_0

    const/4 v0, 0x0

    .line 2
    invoke-virtual {p0, v0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->setRenderView(Ltv/danmaku/ijk/media/player/tw/IRenderView;)V

    .line 3
    iget-object v1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    invoke-interface {v1}, Ltv/danmaku/ijk/media/player/IMediaPlayer;->reset()V

    .line 4
    iget-object v1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    invoke-interface {v1}, Ltv/danmaku/ijk/media/player/IMediaPlayer;->release()V

    .line 5
    iput-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    const/4 v0, 0x0

    .line 6
    iput v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mCurrentState:I

    if-eqz p1, :cond_0

    .line 7
    iput v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mTargetState:I

    :cond_0
    return-void
.end method

.method public releaseWithoutStop()V
    .locals 1

    .line 1
    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    if-eqz p0, :cond_0

    const/4 v0, 0x0

    .line 2
    invoke-interface {p0, v0}, Ltv/danmaku/ijk/media/player/IMediaPlayer;->setDisplay(Landroid/view/SurfaceHolder;)V

    :cond_0
    return-void
.end method

.method public resume()V
    .locals 1

    .line 1
    iget v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mPlayerType:I

    invoke-virtual {p0, v0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->openMP(I)V

    return-void
.end method

.method public seekTo(I)V
    .locals 3

    .line 1
    invoke-direct {p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->isInPlaybackState()Z

    move-result v0

    if-eqz v0, :cond_0

    .line 2
    invoke-static {}, Ljava/lang/System;->currentTimeMillis()J

    move-result-wide v0

    iput-wide v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mSeekStartTime:J

    .line 3
    iget-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    int-to-long v1, p1

    invoke-interface {v0, v1, v2}, Ltv/danmaku/ijk/media/player/IMediaPlayer;->seekTo(J)V

    const/4 p1, 0x0

    .line 4
    iput p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mSeekWhenPrepared:I

    goto :goto_0

    .line 5
    :cond_0
    iput p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mSeekWhenPrepared:I

    :goto_0
    return-void
.end method

.method public setMPPath(Ljava/lang/String;)V
    .locals 0

    .line 1
    invoke-static {p1}, Landroid/net/Uri;->parse(Ljava/lang/String;)Landroid/net/Uri;

    move-result-object p1

    invoke-virtual {p0, p1}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->setMPURI(Landroid/net/Uri;)V

    return-void
.end method

.method public setMPURI(Landroid/net/Uri;)V
    .locals 1

    const/4 v0, 0x0

    .line 1
    invoke-direct {p0, p1, v0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->setMPURI(Landroid/net/Uri;Ljava/util/Map;)V

    return-void
.end method

.method public setOnCompletionListener(Ltv/danmaku/ijk/media/player/IMediaPlayer$OnCompletionListener;)V
    .locals 0

    .line 1
    iput-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mOnCompletionListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnCompletionListener;

    return-void
.end method

.method public setOnErrorListener(Ltv/danmaku/ijk/media/player/IMediaPlayer$OnErrorListener;)V
    .locals 0

    .line 1
    iput-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mOnErrorListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnErrorListener;

    return-void
.end method

.method public setOnInfoListener(Ltv/danmaku/ijk/media/player/IMediaPlayer$OnInfoListener;)V
    .locals 0

    .line 1
    iput-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mOnInfoListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnInfoListener;

    return-void
.end method

.method public setOnPreparedListener(Ltv/danmaku/ijk/media/player/IMediaPlayer$OnPreparedListener;)V
    .locals 0

    .line 1
    iput-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mOnPreparedListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnPreparedListener;

    return-void
.end method

.method public setRender(I)V
    .locals 3

    if-eqz p1, :cond_3

    const/4 v0, 0x1

    if-eq p1, v0, :cond_2

    const/4 v1, 0x2

    if-eq p1, v1, :cond_0

    .line 1
    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->TAG:Ljava/lang/String;

    invoke-static {}, Ljava/util/Locale;->getDefault()Ljava/util/Locale;

    move-result-object v1

    new-array v0, v0, [Ljava/lang/Object;

    const/4 v2, 0x0

    invoke-static {p1}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object p1

    aput-object p1, v0, v2

    const-string p1, "invalid render %d\n"

    invoke-static {v1, p1, v0}, Ljava/lang/String;->format(Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;

    move-result-object p1

    invoke-static {p0, p1}, Landroid/util/Log;->e(Ljava/lang/String;Ljava/lang/String;)I

    goto :goto_0

    .line 2
    :cond_0
    new-instance p1, Ltv/danmaku/ijk/media/player/tw/TextureRenderView;

    invoke-virtual {p0}, Landroid/widget/FrameLayout;->getContext()Landroid/content/Context;

    move-result-object v0

    invoke-direct {p1, v0}, Ltv/danmaku/ijk/media/player/tw/TextureRenderView;-><init>(Landroid/content/Context;)V

    .line 3
    iget-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    if-eqz v0, :cond_1

    .line 4
    invoke-virtual {p1}, Ltv/danmaku/ijk/media/player/tw/TextureRenderView;->getSurfaceHolder()Ltv/danmaku/ijk/media/player/tw/IRenderView$ISurfaceHolder;

    move-result-object v0

    iget-object v1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    invoke-interface {v0, v1}, Ltv/danmaku/ijk/media/player/tw/IRenderView$ISurfaceHolder;->bindToMediaPlayer(Ltv/danmaku/ijk/media/player/IMediaPlayer;)V

    .line 5
    iget-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    invoke-interface {v0}, Ltv/danmaku/ijk/media/player/IMediaPlayer;->getVideoWidth()I

    move-result v0

    iget-object v1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    invoke-interface {v1}, Ltv/danmaku/ijk/media/player/IMediaPlayer;->getVideoHeight()I

    move-result v1

    invoke-virtual {p1, v0, v1}, Ltv/danmaku/ijk/media/player/tw/TextureRenderView;->setVideoSize(II)V

    .line 6
    iget-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    invoke-interface {v0}, Ltv/danmaku/ijk/media/player/IMediaPlayer;->getVideoSarNum()I

    move-result v0

    iget-object v1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    invoke-interface {v1}, Ltv/danmaku/ijk/media/player/IMediaPlayer;->getVideoSarDen()I

    move-result v1

    invoke-virtual {p1, v0, v1}, Ltv/danmaku/ijk/media/player/tw/TextureRenderView;->setVideoSampleAspectRatio(II)V

    .line 7
    invoke-virtual {p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->currentAspectRatio()I

    move-result v0

    invoke-virtual {p1, v0}, Ltv/danmaku/ijk/media/player/tw/TextureRenderView;->setAspectRatio(I)V

    .line 8
    :cond_1
    invoke-virtual {p0, p1}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->setRenderView(Ltv/danmaku/ijk/media/player/tw/IRenderView;)V

    goto :goto_0

    .line 9
    :cond_2
    new-instance p1, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView;

    invoke-virtual {p0}, Landroid/widget/FrameLayout;->getContext()Landroid/content/Context;

    move-result-object v0

    invoke-direct {p1, v0}, Ltv/danmaku/ijk/media/player/tw/SurfaceRenderView;-><init>(Landroid/content/Context;)V

    .line 10
    invoke-virtual {p0, p1}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->setRenderView(Ltv/danmaku/ijk/media/player/tw/IRenderView;)V

    goto :goto_0

    :cond_3
    const/4 p1, 0x0

    .line 11
    invoke-virtual {p0, p1}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->setRenderView(Ltv/danmaku/ijk/media/player/tw/IRenderView;)V

    :goto_0
    return-void
.end method

.method public setRenderView(Ltv/danmaku/ijk/media/player/tw/IRenderView;)V
    .locals 4

    .line 1
    iget-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mRenderView:Ltv/danmaku/ijk/media/player/tw/IRenderView;

    if-eqz v0, :cond_1

    .line 2
    iget-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    const/4 v1, 0x0

    if-eqz v0, :cond_0

    .line 3
    invoke-interface {v0, v1}, Ltv/danmaku/ijk/media/player/IMediaPlayer;->setDisplay(Landroid/view/SurfaceHolder;)V

    .line 4
    :cond_0
    iget-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mRenderView:Ltv/danmaku/ijk/media/player/tw/IRenderView;

    invoke-interface {v0}, Ltv/danmaku/ijk/media/player/tw/IRenderView;->getView()Landroid/view/View;

    move-result-object v0

    .line 5
    iget-object v2, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mRenderView:Ltv/danmaku/ijk/media/player/tw/IRenderView;

    iget-object v3, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mSHCallback:Ltv/danmaku/ijk/media/player/tw/IRenderView$IRenderCallback;

    invoke-interface {v2, v3}, Ltv/danmaku/ijk/media/player/tw/IRenderView;->removeRenderCallback(Ltv/danmaku/ijk/media/player/tw/IRenderView$IRenderCallback;)V

    .line 6
    iput-object v1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mRenderView:Ltv/danmaku/ijk/media/player/tw/IRenderView;

    .line 7
    invoke-virtual {p0, v0}, Landroid/widget/FrameLayout;->removeView(Landroid/view/View;)V

    :cond_1
    if-nez p1, :cond_2

    return-void

    .line 8
    :cond_2
    iput-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mRenderView:Ltv/danmaku/ijk/media/player/tw/IRenderView;

    .line 9
    invoke-virtual {p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->currentAspectRatio()I

    move-result v0

    invoke-interface {p1, v0}, Ltv/danmaku/ijk/media/player/tw/IRenderView;->setAspectRatio(I)V

    .line 10
    iget v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mVideoWidth:I

    if-lez v0, :cond_3

    iget v1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mVideoHeight:I

    if-lez v1, :cond_3

    .line 11
    invoke-interface {p1, v0, v1}, Ltv/danmaku/ijk/media/player/tw/IRenderView;->setVideoSize(II)V

    .line 12
    :cond_3
    iget v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mVideoSarNum:I

    if-lez v0, :cond_4

    iget v1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mVideoSarDen:I

    if-lez v1, :cond_4

    .line 13
    invoke-interface {p1, v0, v1}, Ltv/danmaku/ijk/media/player/tw/IRenderView;->setVideoSampleAspectRatio(II)V

    .line 14
    :cond_4
    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mRenderView:Ltv/danmaku/ijk/media/player/tw/IRenderView;

    invoke-interface {p1}, Ltv/danmaku/ijk/media/player/tw/IRenderView;->getView()Landroid/view/View;

    move-result-object p1

    .line 15
    new-instance v0, Landroid/widget/FrameLayout$LayoutParams;

    const/16 v1, 0x11

    const/4 v2, -0x2

    invoke-direct {v0, v2, v2, v1}, Landroid/widget/FrameLayout$LayoutParams;-><init>(III)V

    .line 16
    invoke-virtual {p1, v0}, Landroid/view/View;->setLayoutParams(Landroid/view/ViewGroup$LayoutParams;)V

    .line 17
    invoke-virtual {p0, p1}, Landroid/widget/FrameLayout;->addView(Landroid/view/View;)V

    .line 18
    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mRenderView:Ltv/danmaku/ijk/media/player/tw/IRenderView;

    iget-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mSHCallback:Ltv/danmaku/ijk/media/player/tw/IRenderView$IRenderCallback;

    invoke-interface {p1, v0}, Ltv/danmaku/ijk/media/player/tw/IRenderView;->addRenderCallback(Ltv/danmaku/ijk/media/player/tw/IRenderView$IRenderCallback;)V

    .line 19
    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mRenderView:Ltv/danmaku/ijk/media/player/tw/IRenderView;

    iget p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mVideoRotationDegree:I

    invoke-interface {p1, p0}, Ltv/danmaku/ijk/media/player/tw/IRenderView;->setVideoRotation(I)V

    return-void
.end method

.method public setVolume(FF)V
    .locals 1

    .line 1
    invoke-direct {p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->isInPlaybackState()Z

    move-result v0

    if-eqz v0, :cond_0

    .line 2
    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    invoke-interface {p0, p1, p2}, Ltv/danmaku/ijk/media/player/IMediaPlayer;->setVolume(FF)V

    :cond_0
    return-void
.end method

.method public start()V
    .locals 2

    .line 1
    invoke-direct {p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->isInPlaybackState()Z

    move-result v0

    const/4 v1, 0x3

    if-eqz v0, :cond_0

    .line 2
    iget-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    invoke-interface {v0}, Ltv/danmaku/ijk/media/player/IMediaPlayer;->start()V

    .line 3
    iput v1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mCurrentState:I

    .line 4
    :cond_0
    iput v1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mTargetState:I

    return-void
.end method

.method public stopPlayback()V
    .locals 2

    .line 1
    iget-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    if-eqz v0, :cond_0

    const/4 v0, 0x0

    .line 2
    invoke-virtual {p0, v0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->setRenderView(Ltv/danmaku/ijk/media/player/tw/IRenderView;)V

    .line 3
    iget-object v1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    invoke-interface {v1}, Ltv/danmaku/ijk/media/player/IMediaPlayer;->stop()V

    .line 4
    iget-object v1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    invoke-interface {v1}, Ltv/danmaku/ijk/media/player/IMediaPlayer;->release()V

    .line 5
    iput-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    const/4 v0, 0x0

    .line 6
    iput v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mCurrentState:I

    .line 7
    iput v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->mTargetState:I

    :cond_0
    return-void
.end method

.method public suspend()V
    .locals 1

    const/4 v0, 0x0

    .line 1
    invoke-virtual {p0, v0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayerView;->release(Z)V

    return-void
.end method
