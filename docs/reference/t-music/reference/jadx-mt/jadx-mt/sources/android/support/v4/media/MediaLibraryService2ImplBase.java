package android.support.v4.media;

import android.content.Intent;
import android.os.IBinder;
import android.support.v4.media.MediaLibraryService2;

/* JADX INFO: loaded from: classes3.dex */
class MediaLibraryService2ImplBase extends MediaSessionService2ImplBase {
    MediaLibraryService2ImplBase() {
    }

    @Override // android.support.v4.media.MediaSessionService2ImplBase, android.support.v4.media.MediaSessionService2.SupportLibraryImpl
    public int getSessionType() {
        return 2;
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0028  */
    @Override // android.support.v4.media.MediaSessionService2ImplBase, android.support.v4.media.MediaSessionService2.SupportLibraryImpl
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public IBinder onBind(Intent intent) {
        byte b2;
        String action = intent.getAction();
        int iHashCode = action.hashCode();
        if (iHashCode != 901933117) {
            b2 = (iHashCode == 1665850838 && action.equals(MediaBrowserServiceCompat.SERVICE_INTERFACE)) ? (byte) 1 : (byte) -1;
        } else if (action.equals(MediaLibraryService2.SERVICE_INTERFACE)) {
            b2 = 0;
        }
        return b2 != 0 ? b2 != 1 ? super.onBind(intent) : getSession().getImpl().getLegacySessionBinder() : getSession().getSessionBinder();
    }

    @Override // android.support.v4.media.MediaSessionService2ImplBase, android.support.v4.media.MediaSessionService2.SupportLibraryImpl
    public MediaLibraryService2.MediaLibrarySession getSession() {
        return (MediaLibraryService2.MediaLibrarySession) super.getSession();
    }
}
