.class public interface abstract Landroid/support/v4/media/IMediaController2;
.super Ljava/lang/Object;
.source "IMediaController2.java"

# interfaces
.implements Landroid/os/IInterface;


# annotations
.annotation system Ldalvik/annotation/MemberClasses;
    value = {
        Landroid/support/v4/media/IMediaController2$Stub;
    }
.end annotation


# virtual methods
.method public abstract onAllowedCommandsChanged(Landroid/os/Bundle;)V
.end method

.method public abstract onBufferingStateChanged(Landroid/os/Bundle;IJ)V
.end method

.method public abstract onChildrenChanged(Ljava/lang/String;ILandroid/os/Bundle;)V
.end method

.method public abstract onConnected(Landroid/support/v4/media/IMediaSession2;Landroid/os/Bundle;ILandroid/os/Bundle;JJFJLandroid/os/Bundle;IILjava/util/List;Landroid/app/PendingIntent;)V
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "(",
            "Landroid/support/v4/media/IMediaSession2;",
            "Landroid/os/Bundle;",
            "I",
            "Landroid/os/Bundle;",
            "JJFJ",
            "Landroid/os/Bundle;",
            "II",
            "Ljava/util/List<",
            "Landroid/os/Bundle;",
            ">;",
            "Landroid/app/PendingIntent;",
            ")V"
        }
    .end annotation
.end method

.method public abstract onCurrentMediaItemChanged(Landroid/os/Bundle;)V
.end method

.method public abstract onCustomCommand(Landroid/os/Bundle;Landroid/os/Bundle;Landroid/os/ResultReceiver;)V
.end method

.method public abstract onCustomLayoutChanged(Ljava/util/List;)V
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "(",
            "Ljava/util/List<",
            "Landroid/os/Bundle;",
            ">;)V"
        }
    .end annotation
.end method

.method public abstract onDisconnected()V
.end method

.method public abstract onError(ILandroid/os/Bundle;)V
.end method

.method public abstract onGetChildrenDone(Ljava/lang/String;IILjava/util/List;Landroid/os/Bundle;)V
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "(",
            "Ljava/lang/String;",
            "II",
            "Ljava/util/List<",
            "Landroid/os/Bundle;",
            ">;",
            "Landroid/os/Bundle;",
            ")V"
        }
    .end annotation
.end method

.method public abstract onGetItemDone(Ljava/lang/String;Landroid/os/Bundle;)V
.end method

.method public abstract onGetLibraryRootDone(Landroid/os/Bundle;Ljava/lang/String;Landroid/os/Bundle;)V
.end method

.method public abstract onGetSearchResultDone(Ljava/lang/String;IILjava/util/List;Landroid/os/Bundle;)V
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "(",
            "Ljava/lang/String;",
            "II",
            "Ljava/util/List<",
            "Landroid/os/Bundle;",
            ">;",
            "Landroid/os/Bundle;",
            ")V"
        }
    .end annotation
.end method

.method public abstract onPlaybackInfoChanged(Landroid/os/Bundle;)V
.end method

.method public abstract onPlaybackSpeedChanged(JJF)V
.end method

.method public abstract onPlayerStateChanged(JJI)V
.end method

.method public abstract onPlaylistChanged(Ljava/util/List;Landroid/os/Bundle;)V
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "(",
            "Ljava/util/List<",
            "Landroid/os/Bundle;",
            ">;",
            "Landroid/os/Bundle;",
            ")V"
        }
    .end annotation
.end method

.method public abstract onPlaylistMetadataChanged(Landroid/os/Bundle;)V
.end method

.method public abstract onRepeatModeChanged(I)V
.end method

.method public abstract onRoutesInfoChanged(Ljava/util/List;)V
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "(",
            "Ljava/util/List<",
            "Landroid/os/Bundle;",
            ">;)V"
        }
    .end annotation
.end method

.method public abstract onSearchResultChanged(Ljava/lang/String;ILandroid/os/Bundle;)V
.end method

.method public abstract onSeekCompleted(JJJ)V
.end method

.method public abstract onShuffleModeChanged(I)V
.end method
