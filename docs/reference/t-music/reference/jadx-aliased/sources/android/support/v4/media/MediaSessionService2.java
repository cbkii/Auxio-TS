package android.support.v4.media;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/* loaded from: classes3.dex */
public abstract class MediaSessionService2 extends Service {
    public static final String SERVICE_INTERFACE = "android.media.MediaSessionService2";
    public static final String SERVICE_META_DATA = "android.media.session";
    private final SupportLibraryImpl mImpl = createImpl();

    public static class MediaNotification {
        private final Notification mNotification;
        private final int mNotificationId;

        public MediaNotification(int i, @NonNull Notification notification) {
            if (notification == null) {
                throw new IllegalArgumentException("notification shouldn't be null");
            }
            this.mNotificationId = i;
            this.mNotification = notification;
        }

        @NonNull
        public Notification getNotification() {
            return this.mNotification;
        }

        public int getNotificationId() {
            return this.mNotificationId;
        }
    }

    interface SupportLibraryImpl {
        MediaSession2 getSession();

        int getSessionType();

        IBinder onBind(Intent intent);

        void onCreate(MediaSessionService2 mediaSessionService2);

        MediaNotification onUpdateNotification();
    }

    SupportLibraryImpl createImpl() {
        return new MediaSessionService2ImplBase();
    }

    @Nullable
    public final MediaSession2 getSession() {
        return this.mImpl.getSession();
    }

    @Override // android.app.Service
    @CallSuper
    @Nullable
    public IBinder onBind(Intent intent) {
        return this.mImpl.onBind(intent);
    }

    @Override // android.app.Service
    @CallSuper
    public void onCreate() {
        super.onCreate();
        this.mImpl.onCreate(this);
    }

    @NonNull
    public abstract MediaSession2 onCreateSession(String str);

    @Nullable
    public MediaNotification onUpdateNotification() {
        return this.mImpl.onUpdateNotification();
    }
}
