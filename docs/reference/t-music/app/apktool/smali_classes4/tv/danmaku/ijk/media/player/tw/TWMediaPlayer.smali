.class public Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;
.super Ljava/lang/Object;
# vendor-boundary: persist.tw.ijk* system properties gate playback engine/runtime behavior; do not rename/alter keys.

.source "TWMediaPlayer.java"


# static fields
.field public static final PV_PLAYER__AndroidMediaPlayer:I = 0x1

.field public static final PV_PLAYER__Auto:I = 0x0

.field public static final PV_PLAYER__IjkExoMediaPlayer:I = 0x3

.field public static final PV_PLAYER__IjkMediaPlayer:I = 0x2

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

.field private static final mAndroidAudioList:Ljava/util/ArrayList;
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "Ljava/util/ArrayList<",
            "Ljava/lang/String;",
            ">;"
        }
    .end annotation
.end field

.field private static final mAudioList:Ljava/util/ArrayList;
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "Ljava/util/ArrayList<",
            "Ljava/lang/String;",
            ">;"
        }
    .end annotation
.end field

.field private static final mExoAudioList:Ljava/util/ArrayList;
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

.field private mContext:Landroid/content/Context;

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

.field private mPlayerType:I

.field private mPrepareEndTime:J

.field private mPrepareStartTime:J

.field mPreparedListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnPreparedListener;

.field private mSeekCompleteListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnSeekCompleteListener;

.field private mSeekEndTime:J

.field private mSeekStartTime:J

.field private mSeekWhenPrepared:I

.field private mTargetState:I

.field private mUri:Landroid/net/Uri;


# direct methods
.method static constructor <clinit>()V
    .locals 1

    const-string v0, "ijk.audio"

    .line 1
    invoke-static {v0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->readMediaList2(Ljava/lang/String;)Ljava/util/ArrayList;

    move-result-object v0

    sput-object v0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mAudioList:Ljava/util/ArrayList;

    const-string v0, "android.audio"

    .line 2
    invoke-static {v0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->readMediaList2(Ljava/lang/String;)Ljava/util/ArrayList;

    move-result-object v0

    sput-object v0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mAndroidAudioList:Ljava/util/ArrayList;

    const-string v0, "exo.audio"

    .line 3
    invoke-static {v0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->readMediaList2(Ljava/lang/String;)Ljava/util/ArrayList;

    move-result-object v0

    sput-object v0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mExoAudioList:Ljava/util/ArrayList;

    return-void
.end method

.method public constructor <init>(Landroid/content/Context;)V
    .locals 3

    .line 1
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    const-string v0, "TWMediaPlayer"

    .line 2
    iput-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->TAG:Ljava/lang/String;

    const/4 v0, 0x0

    .line 3
    iput v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mCurrentState:I

    .line 4
    iput v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mTargetState:I

    const/4 v1, 0x0

    .line 5
    iput-object v1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    const-wide/16 v1, 0x0

    .line 6
    iput-wide v1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mPrepareStartTime:J

    .line 7
    iput-wide v1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mPrepareEndTime:J

    .line 8
    iput-wide v1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mSeekStartTime:J

    .line 9
    iput-wide v1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mSeekEndTime:J

    .line 10
    new-instance v1, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer$1;

    invoke-direct {v1, p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer$1;-><init>(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;)V

    iput-object v1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mPreparedListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnPreparedListener;

    .line 11
    new-instance v1, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer$2;

    invoke-direct {v1, p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer$2;-><init>(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;)V

    iput-object v1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mCompletionListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnCompletionListener;

    .line 12
    new-instance v1, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer$3;

    invoke-direct {v1, p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer$3;-><init>(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;)V

    iput-object v1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mInfoListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnInfoListener;

    .line 13
    new-instance v1, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer$4;

    invoke-direct {v1, p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer$4;-><init>(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;)V

    iput-object v1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mErrorListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnErrorListener;

    .line 14
    new-instance v1, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer$5;

    invoke-direct {v1, p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer$5;-><init>(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;)V

    iput-object v1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mBufferingUpdateListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnBufferingUpdateListener;

    .line 15
    new-instance v1, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer$6;

    invoke-direct {v1, p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer$6;-><init>(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;)V

    iput-object v1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mSeekCompleteListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnSeekCompleteListener;

    .line 16
    iput v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mPlayerType:I

    .line 17
    iput-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mContext:Landroid/content/Context;

    .line 18
    invoke-direct {p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->initMP()V

    return-void
.end method

.method static synthetic access$000(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;)J
    .locals 2

    .line 1
    iget-wide v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mPrepareEndTime:J

    return-wide v0
.end method

.method static synthetic access$002(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;J)J
    .locals 0

    .line 1
    iput-wide p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mPrepareEndTime:J

    return-wide p1
.end method

.method static synthetic access$100(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;)Ljava/lang/String;
    .locals 0

    .line 1
    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->TAG:Ljava/lang/String;

    return-object p0
.end method

.method static synthetic access$1000(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;)Ltv/danmaku/ijk/media/player/IMediaPlayer$OnErrorListener;
    .locals 0

    .line 1
    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mOnErrorListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnErrorListener;

    return-object p0
.end method

.method static synthetic access$1102(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;I)I
    .locals 0

    .line 1
    iput p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mCurrentBufferPercentage:I

    return p1
.end method

.method static synthetic access$1200(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;)J
    .locals 2

    .line 1
    iget-wide v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mSeekEndTime:J

    return-wide v0
.end method

.method static synthetic access$1202(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;J)J
    .locals 0

    .line 1
    iput-wide p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mSeekEndTime:J

    return-wide p1
.end method

.method static synthetic access$1300(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;)J
    .locals 2

    .line 1
    iget-wide v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mSeekStartTime:J

    return-wide v0
.end method

.method static synthetic access$200(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;)J
    .locals 2

    .line 1
    iget-wide v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mPrepareStartTime:J

    return-wide v0
.end method

.method static synthetic access$302(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;I)I
    .locals 0

    .line 1
    iput p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mCurrentState:I

    return p1
.end method

.method static synthetic access$400(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;)Ltv/danmaku/ijk/media/player/IMediaPlayer$OnPreparedListener;
    .locals 0

    .line 1
    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mOnPreparedListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnPreparedListener;

    return-object p0
.end method

.method static synthetic access$500(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;)Ltv/danmaku/ijk/media/player/IMediaPlayer;
    .locals 0

    .line 1
    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    return-object p0
.end method

.method static synthetic access$600(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;)I
    .locals 0

    .line 1
    iget p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mSeekWhenPrepared:I

    return p0
.end method

.method static synthetic access$700(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;)I
    .locals 0

    .line 1
    iget p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mTargetState:I

    return p0
.end method

.method static synthetic access$702(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;I)I
    .locals 0

    .line 1
    iput p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mTargetState:I

    return p1
.end method

.method static synthetic access$800(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;)Ltv/danmaku/ijk/media/player/IMediaPlayer$OnCompletionListener;
    .locals 0

    .line 1
    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mOnCompletionListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnCompletionListener;

    return-object p0
.end method

.method static synthetic access$900(Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;)Ltv/danmaku/ijk/media/player/IMediaPlayer$OnInfoListener;
    .locals 0

    .line 1
    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mOnInfoListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnInfoListener;

    return-object p0
.end method

.method private createPlayer()Ltv/danmaku/ijk/media/player/IMediaPlayer;
    .locals 9

    .line 1
    iget v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mPlayerType:I

    const/4 v1, 0x1

    if-eq v0, v1, :cond_1

    const/4 v0, 0x0

    .line 2
    iget-object v2, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mUri:Landroid/net/Uri;

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
    invoke-virtual {p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->getTWIjkOpenSLES()I

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
    .locals 1

    const/4 v0, 0x0

    .line 1
    iput v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mCurrentState:I

    .line 2
    iput v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mTargetState:I

    return-void
.end method

.method public static isAudio(Ljava/lang/String;)Z
    .locals 2

    .line 1
    sget-object v0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mAudioList:Ljava/util/ArrayList;

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

.method private isInPlaybackState()Z
    .locals 2

    .line 1
    iget-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    const/4 v1, 0x1

    if-eqz v0, :cond_0

    iget p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mCurrentState:I

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

.method private makePlayerType(I)V
    .locals 3

    if-nez p1, :cond_3

    .line 1
    invoke-virtual {p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->getTWIjk()I

    move-result p1

    const/4 v0, 0x1

    const/4 v1, 0x2

    if-eqz p1, :cond_1

    const/4 v2, 0x3

    if-eq p1, v2, :cond_0

    .line 2
    iput v1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mPlayerType:I

    goto :goto_0

    .line 3
    :cond_0
    iput v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mPlayerType:I

    goto :goto_0

    .line 4
    :cond_1
    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mUri:Landroid/net/Uri;

    invoke-virtual {p1}, Landroid/net/Uri;->toString()Ljava/lang/String;

    move-result-object p1

    sget-object v2, Ljava/util/Locale;->ENGLISH:Ljava/util/Locale;

    invoke-virtual {p1, v2}, Ljava/lang/String;->toUpperCase(Ljava/util/Locale;)Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->isAndroidAudio(Ljava/lang/String;)Z

    move-result p1

    if-eqz p1, :cond_2

    .line 5
    iput v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mPlayerType:I

    goto :goto_0

    .line 6
    :cond_2
    iput v1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mPlayerType:I

    goto :goto_0

    .line 7
    :cond_3
    iput p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mPlayerType:I

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

    invoke-static {p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->readMediaList(Ljava/lang/String;)Ljava/util/ArrayList;

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

    invoke-static {p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->readMediaList(Ljava/lang/String;)Ljava/util/ArrayList;

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
    iput-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mUri:Landroid/net/Uri;

    .line 3
    iput-object p2, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mHeaders:Ljava/util/Map;

    const/4 p1, 0x0

    .line 4
    iput p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mSeekWhenPrepared:I

    .line 5
    invoke-virtual {p0, p1}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->openMP(I)V

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

.method public getAudioSessionId()I
    .locals 0

    .line 1
    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

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
    iget-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    if-eqz v0, :cond_0

    .line 2
    iget p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mCurrentBufferPercentage:I

    return p0

    :cond_0
    const/4 p0, 0x0

    return p0
.end method

.method public getCurrentPosition()I
    .locals 2

    .line 1
    invoke-direct {p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->isInPlaybackState()Z

    move-result v0

    if-eqz v0, :cond_0

    .line 2
    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

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
    invoke-direct {p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->isInPlaybackState()Z

    move-result v0

    if-eqz v0, :cond_0

    .line 2
    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

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
    iget p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mPlayerType:I

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

.method public isAndroidAudio(Ljava/lang/String;)Z
    .locals 1

    .line 1
    sget-object p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mAndroidAudioList:Ljava/util/ArrayList;

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

.method public isExoAudio(Ljava/lang/String;)Z
    .locals 1

    .line 1
    sget-object p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mExoAudioList:Ljava/util/ArrayList;

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
    invoke-direct {p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->isInPlaybackState()Z

    move-result v0

    if-eqz v0, :cond_0

    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

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
    invoke-virtual {p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->getTWIjk()I

    move-result p1

    const/4 p2, 0x1

    if-eqz p1, :cond_1

    if-ne p1, p2, :cond_2

    .line 2
    :cond_1
    invoke-virtual {p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->getTWIjkNoError()I

    move-result p1

    if-nez p1, :cond_2

    invoke-virtual {p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->getPlayerType()I

    move-result p1

    const/4 p3, 0x2

    if-eq p1, p3, :cond_2

    .line 3
    invoke-virtual {p0, p3}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->openMP(I)V

    return p2

    :cond_2
    const/4 p0, 0x0

    return p0
.end method

.method public openMP(I)V
    .locals 7

    const-string v0, "Unable to open content: "

    .line 1
    iget-object v1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mUri:Landroid/net/Uri;

    if-nez v1, :cond_0

    return-void

    :cond_0
    const/4 v1, 0x0

    .line 2
    invoke-virtual {p0, v1}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->release(Z)V

    const/16 v2, -0x3ec

    const/4 v3, -0x1

    .line 3
    :try_start_0
    invoke-direct {p0, p1}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->makePlayerType(I)V

    .line 4
    invoke-direct {p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->createPlayer()Ltv/danmaku/ijk/media/player/IMediaPlayer;

    move-result-object p1

    iput-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    .line 5
    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    iget-object v4, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mPreparedListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnPreparedListener;

    invoke-interface {p1, v4}, Ltv/danmaku/ijk/media/player/IMediaPlayer;->setOnPreparedListener(Ltv/danmaku/ijk/media/player/IMediaPlayer$OnPreparedListener;)V

    .line 6
    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    iget-object v4, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mCompletionListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnCompletionListener;

    invoke-interface {p1, v4}, Ltv/danmaku/ijk/media/player/IMediaPlayer;->setOnCompletionListener(Ltv/danmaku/ijk/media/player/IMediaPlayer$OnCompletionListener;)V

    .line 7
    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    iget-object v4, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mErrorListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnErrorListener;

    invoke-interface {p1, v4}, Ltv/danmaku/ijk/media/player/IMediaPlayer;->setOnErrorListener(Ltv/danmaku/ijk/media/player/IMediaPlayer$OnErrorListener;)V

    .line 8
    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    iget-object v4, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mInfoListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnInfoListener;

    invoke-interface {p1, v4}, Ltv/danmaku/ijk/media/player/IMediaPlayer;->setOnInfoListener(Ltv/danmaku/ijk/media/player/IMediaPlayer$OnInfoListener;)V

    .line 9
    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    iget-object v4, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mBufferingUpdateListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnBufferingUpdateListener;

    invoke-interface {p1, v4}, Ltv/danmaku/ijk/media/player/IMediaPlayer;->setOnBufferingUpdateListener(Ltv/danmaku/ijk/media/player/IMediaPlayer$OnBufferingUpdateListener;)V

    .line 10
    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    iget-object v4, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mSeekCompleteListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnSeekCompleteListener;

    invoke-interface {p1, v4}, Ltv/danmaku/ijk/media/player/IMediaPlayer;->setOnSeekCompleteListener(Ltv/danmaku/ijk/media/player/IMediaPlayer$OnSeekCompleteListener;)V

    .line 11
    iput v1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mCurrentBufferPercentage:I

    .line 12
    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    iget-object v4, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mContext:Landroid/content/Context;

    invoke-virtual {v4}, Landroid/content/Context;->getApplicationContext()Landroid/content/Context;

    move-result-object v4

    iget-object v5, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mUri:Landroid/net/Uri;

    iget-object v6, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mHeaders:Ljava/util/Map;

    invoke-interface {p1, v4, v5, v6}, Ltv/danmaku/ijk/media/player/IMediaPlayer;->setDataSource(Landroid/content/Context;Landroid/net/Uri;Ljava/util/Map;)V

    .line 13
    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    const/4 v4, 0x3

    invoke-interface {p1, v4}, Ltv/danmaku/ijk/media/player/IMediaPlayer;->setAudioStreamType(I)V

    .line 14
    invoke-static {}, Ljava/lang/System;->currentTimeMillis()J

    move-result-wide v4

    iput-wide v4, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mPrepareStartTime:J

    .line 15
    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    invoke-interface {p1}, Ltv/danmaku/ijk/media/player/IMediaPlayer;->prepareAsync()V

    const/4 p1, 0x1

    .line 16
    iput p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mCurrentState:I
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

    .line 17
    :try_start_1
    iget-object v4, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->TAG:Ljava/lang/String;

    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v5, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mUri:Landroid/net/Uri;

    invoke-virtual {v5, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-static {v4, v0, p1}, Landroid/util/Log;->w(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I

    .line 18
    iput v3, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mCurrentState:I

    .line 19
    iput v3, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mTargetState:I

    .line 20
    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mErrorListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnErrorListener;

    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    invoke-interface {p1, p0, v2, v1}, Ltv/danmaku/ijk/media/player/IMediaPlayer$OnErrorListener;->onError(Ltv/danmaku/ijk/media/player/IMediaPlayer;II)Z

    goto :goto_0

    :catch_1
    move-exception p1

    .line 21
    iget-object v4, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->TAG:Ljava/lang/String;

    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v5, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mUri:Landroid/net/Uri;

    invoke-virtual {v5, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-static {v4, v0, p1}, Landroid/util/Log;->w(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I

    .line 22
    iput v3, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mCurrentState:I

    .line 23
    iput v3, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mTargetState:I

    .line 24
    iget-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mErrorListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnErrorListener;

    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    invoke-interface {p1, p0, v2, v1}, Ltv/danmaku/ijk/media/player/IMediaPlayer$OnErrorListener;->onError(Ltv/danmaku/ijk/media/player/IMediaPlayer;II)Z
    :try_end_1
    .catchall {:try_start_1 .. :try_end_1} :catchall_0

    :goto_0
    return-void

    .line 25
    :goto_1
    throw p0
.end method

.method public pause()V
    .locals 2

    .line 1
    invoke-direct {p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->isInPlaybackState()Z

    move-result v0

    const/4 v1, 0x4

    if-eqz v0, :cond_0

    .line 2
    iget-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    invoke-interface {v0}, Ltv/danmaku/ijk/media/player/IMediaPlayer;->isPlaying()Z

    move-result v0

    if-eqz v0, :cond_0

    .line 3
    iget-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    invoke-interface {v0}, Ltv/danmaku/ijk/media/player/IMediaPlayer;->pause()V

    .line 4
    iput v1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mCurrentState:I

    .line 5
    :cond_0
    iput v1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mTargetState:I

    return-void
.end method

.method public release(Z)V
    .locals 1

    .line 1
    iget-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    if-eqz v0, :cond_0

    .line 2
    invoke-interface {v0}, Ltv/danmaku/ijk/media/player/IMediaPlayer;->reset()V

    .line 3
    iget-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    invoke-interface {v0}, Ltv/danmaku/ijk/media/player/IMediaPlayer;->release()V

    const/4 v0, 0x0

    .line 4
    iput-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    const/4 v0, 0x0

    .line 5
    iput v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mCurrentState:I

    if-eqz p1, :cond_0

    .line 6
    iput v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mTargetState:I

    :cond_0
    return-void
.end method

.method public resume()V
    .locals 1

    .line 1
    iget v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mPlayerType:I

    invoke-virtual {p0, v0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->openMP(I)V

    return-void
.end method

.method public seekTo(I)V
    .locals 3

    .line 1
    invoke-direct {p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->isInPlaybackState()Z

    move-result v0

    if-eqz v0, :cond_0

    .line 2
    invoke-static {}, Ljava/lang/System;->currentTimeMillis()J

    move-result-wide v0

    iput-wide v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mSeekStartTime:J

    .line 3
    iget-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    int-to-long v1, p1

    invoke-interface {v0, v1, v2}, Ltv/danmaku/ijk/media/player/IMediaPlayer;->seekTo(J)V

    const/4 p1, 0x0

    .line 4
    iput p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mSeekWhenPrepared:I

    goto :goto_0

    .line 5
    :cond_0
    iput p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mSeekWhenPrepared:I

    :goto_0
    return-void
.end method

.method public setMPPath(Ljava/lang/String;)V
    .locals 0

    .line 1
    invoke-static {p1}, Landroid/net/Uri;->parse(Ljava/lang/String;)Landroid/net/Uri;

    move-result-object p1

    invoke-virtual {p0, p1}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->setMPURI(Landroid/net/Uri;)V

    return-void
.end method

.method public setMPURI(Landroid/net/Uri;)V
    .locals 1

    const/4 v0, 0x0

    .line 1
    invoke-direct {p0, p1, v0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->setMPURI(Landroid/net/Uri;Ljava/util/Map;)V

    return-void
.end method

.method public setOnCompletionListener(Ltv/danmaku/ijk/media/player/IMediaPlayer$OnCompletionListener;)V
    .locals 0

    .line 1
    iput-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mOnCompletionListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnCompletionListener;

    return-void
.end method

.method public setOnErrorListener(Ltv/danmaku/ijk/media/player/IMediaPlayer$OnErrorListener;)V
    .locals 0

    .line 1
    iput-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mOnErrorListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnErrorListener;

    return-void
.end method

.method public setOnInfoListener(Ltv/danmaku/ijk/media/player/IMediaPlayer$OnInfoListener;)V
    .locals 0

    .line 1
    iput-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mOnInfoListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnInfoListener;

    return-void
.end method

.method public setOnPreparedListener(Ltv/danmaku/ijk/media/player/IMediaPlayer$OnPreparedListener;)V
    .locals 0

    .line 1
    iput-object p1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mOnPreparedListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnPreparedListener;

    return-void
.end method

.method public setVolume(FF)V
    .locals 1

    .line 1
    invoke-direct {p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->isInPlaybackState()Z

    move-result v0

    if-eqz v0, :cond_0

    .line 2
    iget-object p0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    invoke-interface {p0, p1, p2}, Ltv/danmaku/ijk/media/player/IMediaPlayer;->setVolume(FF)V

    :cond_0
    return-void
.end method

.method public start()V
    .locals 2

    .line 1
    invoke-direct {p0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->isInPlaybackState()Z

    move-result v0

    const/4 v1, 0x3

    if-eqz v0, :cond_0

    .line 2
    iget-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    invoke-interface {v0}, Ltv/danmaku/ijk/media/player/IMediaPlayer;->start()V

    .line 3
    iput v1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mCurrentState:I

    .line 4
    :cond_0
    iput v1, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mTargetState:I

    return-void
.end method

.method public stopPlayback()V
    .locals 1

    .line 1
    iget-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    if-eqz v0, :cond_0

    .line 2
    invoke-interface {v0}, Ltv/danmaku/ijk/media/player/IMediaPlayer;->stop()V

    .line 3
    iget-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    invoke-interface {v0}, Ltv/danmaku/ijk/media/player/IMediaPlayer;->release()V

    const/4 v0, 0x0

    .line 4
    iput-object v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mMediaPlayer:Ltv/danmaku/ijk/media/player/IMediaPlayer;

    const/4 v0, 0x0

    .line 5
    iput v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mCurrentState:I

    .line 6
    iput v0, p0, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->mTargetState:I

    :cond_0
    return-void
.end method

.method public suspend()V
    .locals 1

    const/4 v0, 0x0

    .line 1
    invoke-virtual {p0, v0}, Ltv/danmaku/ijk/media/player/tw/TWMediaPlayer;->release(Z)V

    return-void
.end method
