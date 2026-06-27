package android.support.v4.media;

import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ResultReceiver;
import android.support.v4.media.IMediaController2;
import java.util.List;

/* loaded from: classes3.dex */
public interface IMediaSession2 extends IInterface {

    public static abstract class Stub extends Binder implements IMediaSession2 {
        private static final String DESCRIPTOR = "android.support.v4.media.IMediaSession2";
        static final int TRANSACTION_addPlaylistItem = 23;
        static final int TRANSACTION_adjustVolume = 4;
        static final int TRANSACTION_connect = 1;
        static final int TRANSACTION_fastForward = 9;
        static final int TRANSACTION_getChildren = 36;
        static final int TRANSACTION_getItem = 35;
        static final int TRANSACTION_getLibraryRoot = 34;
        static final int TRANSACTION_getSearchResult = 38;
        static final int TRANSACTION_pause = 6;
        static final int TRANSACTION_play = 5;
        static final int TRANSACTION_playFromMediaId = 18;
        static final int TRANSACTION_playFromSearch = 17;
        static final int TRANSACTION_playFromUri = 16;
        static final int TRANSACTION_prepare = 8;
        static final int TRANSACTION_prepareFromMediaId = 15;
        static final int TRANSACTION_prepareFromSearch = 14;
        static final int TRANSACTION_prepareFromUri = 13;
        static final int TRANSACTION_release = 2;
        static final int TRANSACTION_removePlaylistItem = 24;
        static final int TRANSACTION_replacePlaylistItem = 25;
        static final int TRANSACTION_reset = 7;
        static final int TRANSACTION_rewind = 10;
        static final int TRANSACTION_search = 37;
        static final int TRANSACTION_seekTo = 11;
        static final int TRANSACTION_selectRoute = 33;
        static final int TRANSACTION_sendCustomCommand = 12;
        static final int TRANSACTION_setPlaybackSpeed = 20;
        static final int TRANSACTION_setPlaylist = 21;
        static final int TRANSACTION_setRating = 19;
        static final int TRANSACTION_setRepeatMode = 29;
        static final int TRANSACTION_setShuffleMode = 30;
        static final int TRANSACTION_setVolumeTo = 3;
        static final int TRANSACTION_skipToNextItem = 28;
        static final int TRANSACTION_skipToPlaylistItem = 26;
        static final int TRANSACTION_skipToPreviousItem = 27;
        static final int TRANSACTION_subscribe = 39;
        static final int TRANSACTION_subscribeRoutesInfo = 31;
        static final int TRANSACTION_unsubscribe = 40;
        static final int TRANSACTION_unsubscribeRoutesInfo = 32;
        static final int TRANSACTION_updatePlaylistMetadata = 22;

        private static class Proxy implements IMediaSession2 {
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.support.v4.media.IMediaSession2
            public void addPlaylistItem(IMediaController2 iMediaController2, int i, Bundle bundle) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iMediaController2 != null ? iMediaController2.asBinder() : null);
                    obtain.writeInt(i);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(23, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // android.support.v4.media.IMediaSession2
            public void adjustVolume(IMediaController2 iMediaController2, int i, int i2) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iMediaController2 != null ? iMediaController2.asBinder() : null);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(4, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            @Override // android.support.v4.media.IMediaSession2
            public void connect(IMediaController2 iMediaController2, String str) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iMediaController2 != null ? iMediaController2.asBinder() : null);
                    obtain.writeString(str);
                    this.mRemote.transact(1, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // android.support.v4.media.IMediaSession2
            public void fastForward(IMediaController2 iMediaController2) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iMediaController2 != null ? iMediaController2.asBinder() : null);
                    this.mRemote.transact(9, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // android.support.v4.media.IMediaSession2
            public void getChildren(IMediaController2 iMediaController2, String str, int i, int i2, Bundle bundle) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iMediaController2 != null ? iMediaController2.asBinder() : null);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(36, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            @Override // android.support.v4.media.IMediaSession2
            public void getItem(IMediaController2 iMediaController2, String str) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iMediaController2 != null ? iMediaController2.asBinder() : null);
                    obtain.writeString(str);
                    this.mRemote.transact(35, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // android.support.v4.media.IMediaSession2
            public void getLibraryRoot(IMediaController2 iMediaController2, Bundle bundle) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iMediaController2 != null ? iMediaController2.asBinder() : null);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(34, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // android.support.v4.media.IMediaSession2
            public void getSearchResult(IMediaController2 iMediaController2, String str, int i, int i2, Bundle bundle) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iMediaController2 != null ? iMediaController2.asBinder() : null);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(38, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // android.support.v4.media.IMediaSession2
            public void pause(IMediaController2 iMediaController2) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iMediaController2 != null ? iMediaController2.asBinder() : null);
                    this.mRemote.transact(6, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // android.support.v4.media.IMediaSession2
            public void play(IMediaController2 iMediaController2) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iMediaController2 != null ? iMediaController2.asBinder() : null);
                    this.mRemote.transact(5, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // android.support.v4.media.IMediaSession2
            public void playFromMediaId(IMediaController2 iMediaController2, String str, Bundle bundle) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iMediaController2 != null ? iMediaController2.asBinder() : null);
                    obtain.writeString(str);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(18, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // android.support.v4.media.IMediaSession2
            public void playFromSearch(IMediaController2 iMediaController2, String str, Bundle bundle) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iMediaController2 != null ? iMediaController2.asBinder() : null);
                    obtain.writeString(str);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(17, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // android.support.v4.media.IMediaSession2
            public void playFromUri(IMediaController2 iMediaController2, Uri uri, Bundle bundle) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iMediaController2 != null ? iMediaController2.asBinder() : null);
                    if (uri != null) {
                        obtain.writeInt(1);
                        uri.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(16, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // android.support.v4.media.IMediaSession2
            public void prepare(IMediaController2 iMediaController2) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iMediaController2 != null ? iMediaController2.asBinder() : null);
                    this.mRemote.transact(8, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // android.support.v4.media.IMediaSession2
            public void prepareFromMediaId(IMediaController2 iMediaController2, String str, Bundle bundle) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iMediaController2 != null ? iMediaController2.asBinder() : null);
                    obtain.writeString(str);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(15, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // android.support.v4.media.IMediaSession2
            public void prepareFromSearch(IMediaController2 iMediaController2, String str, Bundle bundle) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iMediaController2 != null ? iMediaController2.asBinder() : null);
                    obtain.writeString(str);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(14, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // android.support.v4.media.IMediaSession2
            public void prepareFromUri(IMediaController2 iMediaController2, Uri uri, Bundle bundle) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iMediaController2 != null ? iMediaController2.asBinder() : null);
                    if (uri != null) {
                        obtain.writeInt(1);
                        uri.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(13, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // android.support.v4.media.IMediaSession2
            public void release(IMediaController2 iMediaController2) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iMediaController2 != null ? iMediaController2.asBinder() : null);
                    this.mRemote.transact(2, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // android.support.v4.media.IMediaSession2
            public void removePlaylistItem(IMediaController2 iMediaController2, Bundle bundle) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iMediaController2 != null ? iMediaController2.asBinder() : null);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(24, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // android.support.v4.media.IMediaSession2
            public void replacePlaylistItem(IMediaController2 iMediaController2, int i, Bundle bundle) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iMediaController2 != null ? iMediaController2.asBinder() : null);
                    obtain.writeInt(i);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(25, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // android.support.v4.media.IMediaSession2
            public void reset(IMediaController2 iMediaController2) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iMediaController2 != null ? iMediaController2.asBinder() : null);
                    this.mRemote.transact(7, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // android.support.v4.media.IMediaSession2
            public void rewind(IMediaController2 iMediaController2) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iMediaController2 != null ? iMediaController2.asBinder() : null);
                    this.mRemote.transact(10, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // android.support.v4.media.IMediaSession2
            public void search(IMediaController2 iMediaController2, String str, Bundle bundle) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iMediaController2 != null ? iMediaController2.asBinder() : null);
                    obtain.writeString(str);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(37, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // android.support.v4.media.IMediaSession2
            public void seekTo(IMediaController2 iMediaController2, long j) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iMediaController2 != null ? iMediaController2.asBinder() : null);
                    obtain.writeLong(j);
                    this.mRemote.transact(11, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // android.support.v4.media.IMediaSession2
            public void selectRoute(IMediaController2 iMediaController2, Bundle bundle) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iMediaController2 != null ? iMediaController2.asBinder() : null);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(33, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // android.support.v4.media.IMediaSession2
            public void sendCustomCommand(IMediaController2 iMediaController2, Bundle bundle, Bundle bundle2, ResultReceiver resultReceiver) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iMediaController2 != null ? iMediaController2.asBinder() : null);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (bundle2 != null) {
                        obtain.writeInt(1);
                        bundle2.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (resultReceiver != null) {
                        obtain.writeInt(1);
                        resultReceiver.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(12, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // android.support.v4.media.IMediaSession2
            public void setPlaybackSpeed(IMediaController2 iMediaController2, float f) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iMediaController2 != null ? iMediaController2.asBinder() : null);
                    obtain.writeFloat(f);
                    this.mRemote.transact(20, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // android.support.v4.media.IMediaSession2
            public void setPlaylist(IMediaController2 iMediaController2, List<Bundle> list, Bundle bundle) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iMediaController2 != null ? iMediaController2.asBinder() : null);
                    obtain.writeTypedList(list);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(21, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // android.support.v4.media.IMediaSession2
            public void setRating(IMediaController2 iMediaController2, String str, Bundle bundle) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iMediaController2 != null ? iMediaController2.asBinder() : null);
                    obtain.writeString(str);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(19, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // android.support.v4.media.IMediaSession2
            public void setRepeatMode(IMediaController2 iMediaController2, int i) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iMediaController2 != null ? iMediaController2.asBinder() : null);
                    obtain.writeInt(i);
                    this.mRemote.transact(29, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // android.support.v4.media.IMediaSession2
            public void setShuffleMode(IMediaController2 iMediaController2, int i) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iMediaController2 != null ? iMediaController2.asBinder() : null);
                    obtain.writeInt(i);
                    this.mRemote.transact(30, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // android.support.v4.media.IMediaSession2
            public void setVolumeTo(IMediaController2 iMediaController2, int i, int i2) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iMediaController2 != null ? iMediaController2.asBinder() : null);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(3, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // android.support.v4.media.IMediaSession2
            public void skipToNextItem(IMediaController2 iMediaController2) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iMediaController2 != null ? iMediaController2.asBinder() : null);
                    this.mRemote.transact(28, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // android.support.v4.media.IMediaSession2
            public void skipToPlaylistItem(IMediaController2 iMediaController2, Bundle bundle) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iMediaController2 != null ? iMediaController2.asBinder() : null);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(26, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // android.support.v4.media.IMediaSession2
            public void skipToPreviousItem(IMediaController2 iMediaController2) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iMediaController2 != null ? iMediaController2.asBinder() : null);
                    this.mRemote.transact(27, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // android.support.v4.media.IMediaSession2
            public void subscribe(IMediaController2 iMediaController2, String str, Bundle bundle) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iMediaController2 != null ? iMediaController2.asBinder() : null);
                    obtain.writeString(str);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(39, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // android.support.v4.media.IMediaSession2
            public void subscribeRoutesInfo(IMediaController2 iMediaController2) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iMediaController2 != null ? iMediaController2.asBinder() : null);
                    this.mRemote.transact(31, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // android.support.v4.media.IMediaSession2
            public void unsubscribe(IMediaController2 iMediaController2, String str) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iMediaController2 != null ? iMediaController2.asBinder() : null);
                    obtain.writeString(str);
                    this.mRemote.transact(40, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // android.support.v4.media.IMediaSession2
            public void unsubscribeRoutesInfo(IMediaController2 iMediaController2) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iMediaController2 != null ? iMediaController2.asBinder() : null);
                    this.mRemote.transact(32, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // android.support.v4.media.IMediaSession2
            public void updatePlaylistMetadata(IMediaController2 iMediaController2, Bundle bundle) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iMediaController2 != null ? iMediaController2.asBinder() : null);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(22, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IMediaSession2 asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (queryLocalInterface == null || !(queryLocalInterface instanceof IMediaSession2)) ? new Proxy(iBinder) : (IMediaSession2) queryLocalInterface;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            if (i == 1598968902) {
                parcel2.writeString(DESCRIPTOR);
                return true;
            }
            switch (i) {
                case 1:
                    parcel.enforceInterface(DESCRIPTOR);
                    connect(IMediaController2.Stub.asInterface(parcel.readStrongBinder()), parcel.readString());
                    return true;
                case 2:
                    parcel.enforceInterface(DESCRIPTOR);
                    release(IMediaController2.Stub.asInterface(parcel.readStrongBinder()));
                    return true;
                case 3:
                    parcel.enforceInterface(DESCRIPTOR);
                    setVolumeTo(IMediaController2.Stub.asInterface(parcel.readStrongBinder()), parcel.readInt(), parcel.readInt());
                    return true;
                case 4:
                    parcel.enforceInterface(DESCRIPTOR);
                    adjustVolume(IMediaController2.Stub.asInterface(parcel.readStrongBinder()), parcel.readInt(), parcel.readInt());
                    return true;
                case 5:
                    parcel.enforceInterface(DESCRIPTOR);
                    play(IMediaController2.Stub.asInterface(parcel.readStrongBinder()));
                    return true;
                case 6:
                    parcel.enforceInterface(DESCRIPTOR);
                    pause(IMediaController2.Stub.asInterface(parcel.readStrongBinder()));
                    return true;
                case 7:
                    parcel.enforceInterface(DESCRIPTOR);
                    reset(IMediaController2.Stub.asInterface(parcel.readStrongBinder()));
                    return true;
                case 8:
                    parcel.enforceInterface(DESCRIPTOR);
                    prepare(IMediaController2.Stub.asInterface(parcel.readStrongBinder()));
                    return true;
                case 9:
                    parcel.enforceInterface(DESCRIPTOR);
                    fastForward(IMediaController2.Stub.asInterface(parcel.readStrongBinder()));
                    return true;
                case 10:
                    parcel.enforceInterface(DESCRIPTOR);
                    rewind(IMediaController2.Stub.asInterface(parcel.readStrongBinder()));
                    return true;
                case 11:
                    parcel.enforceInterface(DESCRIPTOR);
                    seekTo(IMediaController2.Stub.asInterface(parcel.readStrongBinder()), parcel.readLong());
                    return true;
                case 12:
                    parcel.enforceInterface(DESCRIPTOR);
                    sendCustomCommand(IMediaController2.Stub.asInterface(parcel.readStrongBinder()), parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel) : null, parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel) : null, parcel.readInt() != 0 ? (ResultReceiver) ResultReceiver.CREATOR.createFromParcel(parcel) : null);
                    return true;
                case 13:
                    parcel.enforceInterface(DESCRIPTOR);
                    prepareFromUri(IMediaController2.Stub.asInterface(parcel.readStrongBinder()), parcel.readInt() != 0 ? (Uri) Uri.CREATOR.createFromParcel(parcel) : null, parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel) : null);
                    return true;
                case 14:
                    parcel.enforceInterface(DESCRIPTOR);
                    prepareFromSearch(IMediaController2.Stub.asInterface(parcel.readStrongBinder()), parcel.readString(), parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel) : null);
                    return true;
                case 15:
                    parcel.enforceInterface(DESCRIPTOR);
                    prepareFromMediaId(IMediaController2.Stub.asInterface(parcel.readStrongBinder()), parcel.readString(), parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel) : null);
                    return true;
                case 16:
                    parcel.enforceInterface(DESCRIPTOR);
                    playFromUri(IMediaController2.Stub.asInterface(parcel.readStrongBinder()), parcel.readInt() != 0 ? (Uri) Uri.CREATOR.createFromParcel(parcel) : null, parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel) : null);
                    return true;
                case 17:
                    parcel.enforceInterface(DESCRIPTOR);
                    playFromSearch(IMediaController2.Stub.asInterface(parcel.readStrongBinder()), parcel.readString(), parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel) : null);
                    return true;
                case 18:
                    parcel.enforceInterface(DESCRIPTOR);
                    playFromMediaId(IMediaController2.Stub.asInterface(parcel.readStrongBinder()), parcel.readString(), parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel) : null);
                    return true;
                case 19:
                    parcel.enforceInterface(DESCRIPTOR);
                    setRating(IMediaController2.Stub.asInterface(parcel.readStrongBinder()), parcel.readString(), parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel) : null);
                    return true;
                case 20:
                    parcel.enforceInterface(DESCRIPTOR);
                    setPlaybackSpeed(IMediaController2.Stub.asInterface(parcel.readStrongBinder()), parcel.readFloat());
                    return true;
                case 21:
                    parcel.enforceInterface(DESCRIPTOR);
                    setPlaylist(IMediaController2.Stub.asInterface(parcel.readStrongBinder()), parcel.createTypedArrayList(Bundle.CREATOR), parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel) : null);
                    return true;
                case 22:
                    parcel.enforceInterface(DESCRIPTOR);
                    updatePlaylistMetadata(IMediaController2.Stub.asInterface(parcel.readStrongBinder()), parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel) : null);
                    return true;
                case 23:
                    parcel.enforceInterface(DESCRIPTOR);
                    addPlaylistItem(IMediaController2.Stub.asInterface(parcel.readStrongBinder()), parcel.readInt(), parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel) : null);
                    return true;
                case 24:
                    parcel.enforceInterface(DESCRIPTOR);
                    removePlaylistItem(IMediaController2.Stub.asInterface(parcel.readStrongBinder()), parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel) : null);
                    return true;
                case 25:
                    parcel.enforceInterface(DESCRIPTOR);
                    replacePlaylistItem(IMediaController2.Stub.asInterface(parcel.readStrongBinder()), parcel.readInt(), parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel) : null);
                    return true;
                case 26:
                    parcel.enforceInterface(DESCRIPTOR);
                    skipToPlaylistItem(IMediaController2.Stub.asInterface(parcel.readStrongBinder()), parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel) : null);
                    return true;
                case 27:
                    parcel.enforceInterface(DESCRIPTOR);
                    skipToPreviousItem(IMediaController2.Stub.asInterface(parcel.readStrongBinder()));
                    return true;
                case 28:
                    parcel.enforceInterface(DESCRIPTOR);
                    skipToNextItem(IMediaController2.Stub.asInterface(parcel.readStrongBinder()));
                    return true;
                case 29:
                    parcel.enforceInterface(DESCRIPTOR);
                    setRepeatMode(IMediaController2.Stub.asInterface(parcel.readStrongBinder()), parcel.readInt());
                    return true;
                case 30:
                    parcel.enforceInterface(DESCRIPTOR);
                    setShuffleMode(IMediaController2.Stub.asInterface(parcel.readStrongBinder()), parcel.readInt());
                    return true;
                case 31:
                    parcel.enforceInterface(DESCRIPTOR);
                    subscribeRoutesInfo(IMediaController2.Stub.asInterface(parcel.readStrongBinder()));
                    return true;
                case 32:
                    parcel.enforceInterface(DESCRIPTOR);
                    unsubscribeRoutesInfo(IMediaController2.Stub.asInterface(parcel.readStrongBinder()));
                    return true;
                case 33:
                    parcel.enforceInterface(DESCRIPTOR);
                    selectRoute(IMediaController2.Stub.asInterface(parcel.readStrongBinder()), parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel) : null);
                    return true;
                case 34:
                    parcel.enforceInterface(DESCRIPTOR);
                    getLibraryRoot(IMediaController2.Stub.asInterface(parcel.readStrongBinder()), parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel) : null);
                    return true;
                case 35:
                    parcel.enforceInterface(DESCRIPTOR);
                    getItem(IMediaController2.Stub.asInterface(parcel.readStrongBinder()), parcel.readString());
                    return true;
                case 36:
                    parcel.enforceInterface(DESCRIPTOR);
                    getChildren(IMediaController2.Stub.asInterface(parcel.readStrongBinder()), parcel.readString(), parcel.readInt(), parcel.readInt(), parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel) : null);
                    return true;
                case 37:
                    parcel.enforceInterface(DESCRIPTOR);
                    search(IMediaController2.Stub.asInterface(parcel.readStrongBinder()), parcel.readString(), parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel) : null);
                    return true;
                case 38:
                    parcel.enforceInterface(DESCRIPTOR);
                    getSearchResult(IMediaController2.Stub.asInterface(parcel.readStrongBinder()), parcel.readString(), parcel.readInt(), parcel.readInt(), parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel) : null);
                    return true;
                case 39:
                    parcel.enforceInterface(DESCRIPTOR);
                    subscribe(IMediaController2.Stub.asInterface(parcel.readStrongBinder()), parcel.readString(), parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel) : null);
                    return true;
                case 40:
                    parcel.enforceInterface(DESCRIPTOR);
                    unsubscribe(IMediaController2.Stub.asInterface(parcel.readStrongBinder()), parcel.readString());
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    void addPlaylistItem(IMediaController2 iMediaController2, int i, Bundle bundle);

    void adjustVolume(IMediaController2 iMediaController2, int i, int i2);

    void connect(IMediaController2 iMediaController2, String str);

    void fastForward(IMediaController2 iMediaController2);

    void getChildren(IMediaController2 iMediaController2, String str, int i, int i2, Bundle bundle);

    void getItem(IMediaController2 iMediaController2, String str);

    void getLibraryRoot(IMediaController2 iMediaController2, Bundle bundle);

    void getSearchResult(IMediaController2 iMediaController2, String str, int i, int i2, Bundle bundle);

    void pause(IMediaController2 iMediaController2);

    void play(IMediaController2 iMediaController2);

    void playFromMediaId(IMediaController2 iMediaController2, String str, Bundle bundle);

    void playFromSearch(IMediaController2 iMediaController2, String str, Bundle bundle);

    void playFromUri(IMediaController2 iMediaController2, Uri uri, Bundle bundle);

    void prepare(IMediaController2 iMediaController2);

    void prepareFromMediaId(IMediaController2 iMediaController2, String str, Bundle bundle);

    void prepareFromSearch(IMediaController2 iMediaController2, String str, Bundle bundle);

    void prepareFromUri(IMediaController2 iMediaController2, Uri uri, Bundle bundle);

    void release(IMediaController2 iMediaController2);

    void removePlaylistItem(IMediaController2 iMediaController2, Bundle bundle);

    void replacePlaylistItem(IMediaController2 iMediaController2, int i, Bundle bundle);

    void reset(IMediaController2 iMediaController2);

    void rewind(IMediaController2 iMediaController2);

    void search(IMediaController2 iMediaController2, String str, Bundle bundle);

    void seekTo(IMediaController2 iMediaController2, long j);

    void selectRoute(IMediaController2 iMediaController2, Bundle bundle);

    void sendCustomCommand(IMediaController2 iMediaController2, Bundle bundle, Bundle bundle2, ResultReceiver resultReceiver);

    void setPlaybackSpeed(IMediaController2 iMediaController2, float f);

    void setPlaylist(IMediaController2 iMediaController2, List<Bundle> list, Bundle bundle);

    void setRating(IMediaController2 iMediaController2, String str, Bundle bundle);

    void setRepeatMode(IMediaController2 iMediaController2, int i);

    void setShuffleMode(IMediaController2 iMediaController2, int i);

    void setVolumeTo(IMediaController2 iMediaController2, int i, int i2);

    void skipToNextItem(IMediaController2 iMediaController2);

    void skipToPlaylistItem(IMediaController2 iMediaController2, Bundle bundle);

    void skipToPreviousItem(IMediaController2 iMediaController2);

    void subscribe(IMediaController2 iMediaController2, String str, Bundle bundle);

    void subscribeRoutesInfo(IMediaController2 iMediaController2);

    void unsubscribe(IMediaController2 iMediaController2, String str);

    void unsubscribeRoutesInfo(IMediaController2 iMediaController2);

    void updatePlaylistMetadata(IMediaController2 iMediaController2, Bundle bundle);
}
