package android.support.v4.app;

import android.app.Notification;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RestrictTo;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.SparseArray;
import android.widget.RemoteViews;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes3.dex */
@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
class NotificationCompatBuilder implements NotificationBuilderWithBuilderAccessor {
    private RemoteViews mBigContentView;
    private final Notification.Builder mBuilder;
    private final NotificationCompat.Builder mBuilderCompat;
    private RemoteViews mContentView;
    private int mGroupAlertBehavior;
    private RemoteViews mHeadsUpContentView;
    private final List<Bundle> mActionExtrasList = new ArrayList();
    private final Bundle mExtras = new Bundle();

    NotificationCompatBuilder(NotificationCompat.Builder builder) {
        ArrayList<String> arrayList;
        this.mBuilderCompat = builder;
        if (Build.VERSION.SDK_INT >= 26) {
            this.mBuilder = new Notification.Builder(builder.mContext, builder.mChannelId);
        } else {
            this.mBuilder = new Notification.Builder(builder.mContext);
        }
        Notification notification = builder.mNotification;
        this.mBuilder.setWhen(notification.when).setSmallIcon(notification.icon, notification.iconLevel).setContent(notification.contentView).setTicker(notification.tickerText, builder.mTickerView).setVibrate(notification.vibrate).setLights(notification.ledARGB, notification.ledOnMS, notification.ledOffMS).setOngoing((notification.flags & 2) != 0).setOnlyAlertOnce((notification.flags & 8) != 0).setAutoCancel((notification.flags & 16) != 0).setDefaults(notification.defaults).setContentTitle(builder.mContentTitle).setContentText(builder.mContentText).setContentInfo(builder.mContentInfo).setContentIntent(builder.mContentIntent).setDeleteIntent(notification.deleteIntent).setFullScreenIntent(builder.mFullScreenIntent, (notification.flags & 128) != 0).setLargeIcon(builder.mLargeIcon).setNumber(builder.mNumber).setProgress(builder.mProgressMax, builder.mProgress, builder.mProgressIndeterminate);
        if (Build.VERSION.SDK_INT < 21) {
            this.mBuilder.setSound(notification.sound, notification.audioStreamType);
        }
        if (Build.VERSION.SDK_INT >= 16) {
            this.mBuilder.setSubText(builder.mSubText).setUsesChronometer(builder.mUseChronometer).setPriority(builder.mPriority);
            Iterator<NotificationCompat.Action> it = builder.mActions.iterator();
            while (it.hasNext()) {
                addAction(it.next());
            }
            Bundle bundle = builder.mExtras;
            if (bundle != null) {
                this.mExtras.putAll(bundle);
            }
            if (Build.VERSION.SDK_INT < 20) {
                if (builder.mLocalOnly) {
                    this.mExtras.putBoolean(NotificationCompatExtras.EXTRA_LOCAL_ONLY, true);
                }
                String str = builder.mGroupKey;
                if (str != null) {
                    this.mExtras.putString(NotificationCompatExtras.EXTRA_GROUP_KEY, str);
                    if (builder.mGroupSummary) {
                        this.mExtras.putBoolean(NotificationCompatExtras.EXTRA_GROUP_SUMMARY, true);
                    } else {
                        this.mExtras.putBoolean(NotificationManagerCompat.EXTRA_USE_SIDE_CHANNEL, true);
                    }
                }
                String str2 = builder.mSortKey;
                if (str2 != null) {
                    this.mExtras.putString(NotificationCompatExtras.EXTRA_SORT_KEY, str2);
                }
            }
            this.mContentView = builder.mContentView;
            this.mBigContentView = builder.mBigContentView;
        }
        if (Build.VERSION.SDK_INT >= 19) {
            this.mBuilder.setShowWhen(builder.mShowWhen);
            if (Build.VERSION.SDK_INT < 21 && (arrayList = builder.mPeople) != null && !arrayList.isEmpty()) {
                Bundle bundle2 = this.mExtras;
                ArrayList<String> arrayList2 = builder.mPeople;
                bundle2.putStringArray(NotificationCompat.EXTRA_PEOPLE, (String[]) arrayList2.toArray(new String[arrayList2.size()]));
            }
        }
        if (Build.VERSION.SDK_INT >= 20) {
            this.mBuilder.setLocalOnly(builder.mLocalOnly).setGroup(builder.mGroupKey).setGroupSummary(builder.mGroupSummary).setSortKey(builder.mSortKey);
            this.mGroupAlertBehavior = builder.mGroupAlertBehavior;
        }
        if (Build.VERSION.SDK_INT >= 21) {
            this.mBuilder.setCategory(builder.mCategory).setColor(builder.mColor).setVisibility(builder.mVisibility).setPublicVersion(builder.mPublicVersion).setSound(notification.sound, notification.audioAttributes);
            Iterator<String> it2 = builder.mPeople.iterator();
            while (it2.hasNext()) {
                this.mBuilder.addPerson(it2.next());
            }
            this.mHeadsUpContentView = builder.mHeadsUpContentView;
            if (builder.mInvisibleActions.size() > 0) {
                Bundle bundle3 = builder.getExtras().getBundle("android.car.EXTENSIONS");
                bundle3 = bundle3 == null ? new Bundle() : bundle3;
                Bundle bundle4 = new Bundle();
                for (int i = 0; i < builder.mInvisibleActions.size(); i++) {
                    bundle4.putBundle(Integer.toString(i), NotificationCompatJellybean.getBundleForAction(builder.mInvisibleActions.get(i)));
                }
                bundle3.putBundle("invisible_actions", bundle4);
                builder.getExtras().putBundle("android.car.EXTENSIONS", bundle3);
                this.mExtras.putBundle("android.car.EXTENSIONS", bundle3);
            }
        }
        if (Build.VERSION.SDK_INT >= 24) {
            this.mBuilder.setExtras(builder.mExtras).setRemoteInputHistory(builder.mRemoteInputHistory);
            RemoteViews remoteViews = builder.mContentView;
            if (remoteViews != null) {
                this.mBuilder.setCustomContentView(remoteViews);
            }
            RemoteViews remoteViews2 = builder.mBigContentView;
            if (remoteViews2 != null) {
                this.mBuilder.setCustomBigContentView(remoteViews2);
            }
            RemoteViews remoteViews3 = builder.mHeadsUpContentView;
            if (remoteViews3 != null) {
                this.mBuilder.setCustomHeadsUpContentView(remoteViews3);
            }
        }
        if (Build.VERSION.SDK_INT >= 26) {
            this.mBuilder.setBadgeIconType(builder.mBadgeIcon).setShortcutId(builder.mShortcutId).setTimeoutAfter(builder.mTimeout).setGroupAlertBehavior(builder.mGroupAlertBehavior);
            if (builder.mColorizedSet) {
                this.mBuilder.setColorized(builder.mColorized);
            }
            if (TextUtils.isEmpty(builder.mChannelId)) {
                return;
            }
            this.mBuilder.setSound(null).setDefaults(0).setLights(0, 0, 0).setVibrate(null);
        }
    }

    private void addAction(NotificationCompat.Action action) {
        int i = Build.VERSION.SDK_INT;
        if (i < 20) {
            if (i >= 16) {
                this.mActionExtrasList.add(NotificationCompatJellybean.writeActionAndGetExtras(this.mBuilder, action));
                return;
            }
            return;
        }
        Notification.Action.Builder builder = new Notification.Action.Builder(action.getIcon(), action.getTitle(), action.getActionIntent());
        if (action.getRemoteInputs() != null) {
            for (android.app.RemoteInput remoteInput : RemoteInput.fromCompat(action.getRemoteInputs())) {
                builder.addRemoteInput(remoteInput);
            }
        }
        Bundle bundle = action.getExtras() != null ? new Bundle(action.getExtras()) : new Bundle();
        bundle.putBoolean("android.support.allowGeneratedReplies", action.getAllowGeneratedReplies());
        if (Build.VERSION.SDK_INT >= 24) {
            builder.setAllowGeneratedReplies(action.getAllowGeneratedReplies());
        }
        bundle.putInt("android.support.action.semanticAction", action.getSemanticAction());
        if (Build.VERSION.SDK_INT >= 28) {
            builder.setSemanticAction(action.getSemanticAction());
        }
        bundle.putBoolean("android.support.action.showsUserInterface", action.getShowsUserInterface());
        builder.addExtras(bundle);
        this.mBuilder.addAction(builder.build());
    }

    private void removeSoundAndVibration(Notification notification) {
        notification.sound = null;
        notification.vibrate = null;
        notification.defaults &= -2;
        notification.defaults &= -3;
    }

    public Notification build() {
        Bundle extras;
        RemoteViews remoteViewsMakeHeadsUpContentView;
        RemoteViews remoteViewsMakeBigContentView;
        NotificationCompat.Style style = this.mBuilderCompat.mStyle;
        if (style != null) {
            style.apply(this);
        }
        RemoteViews remoteViewsMakeContentView = style != null ? style.makeContentView(this) : null;
        Notification notificationBuildInternal = buildInternal();
        if (remoteViewsMakeContentView != null) {
            notificationBuildInternal.contentView = remoteViewsMakeContentView;
        } else {
            RemoteViews remoteViews = this.mBuilderCompat.mContentView;
            if (remoteViews != null) {
                notificationBuildInternal.contentView = remoteViews;
            }
        }
        if (Build.VERSION.SDK_INT >= 16 && style != null && (remoteViewsMakeBigContentView = style.makeBigContentView(this)) != null) {
            notificationBuildInternal.bigContentView = remoteViewsMakeBigContentView;
        }
        if (Build.VERSION.SDK_INT >= 21 && style != null && (remoteViewsMakeHeadsUpContentView = this.mBuilderCompat.mStyle.makeHeadsUpContentView(this)) != null) {
            notificationBuildInternal.headsUpContentView = remoteViewsMakeHeadsUpContentView;
        }
        if (Build.VERSION.SDK_INT >= 16 && style != null && (extras = NotificationCompat.getExtras(notificationBuildInternal)) != null) {
            style.addCompatExtras(extras);
        }
        return notificationBuildInternal;
    }

    protected Notification buildInternal() {
        int i = Build.VERSION.SDK_INT;
        if (i >= 26) {
            return this.mBuilder.build();
        }
        if (i >= 24) {
            Notification notificationBuild = this.mBuilder.build();
            if (this.mGroupAlertBehavior != 0) {
                if (notificationBuild.getGroup() != null && (notificationBuild.flags & 512) != 0 && this.mGroupAlertBehavior == 2) {
                    removeSoundAndVibration(notificationBuild);
                }
                if (notificationBuild.getGroup() != null && (notificationBuild.flags & 512) == 0 && this.mGroupAlertBehavior == 1) {
                    removeSoundAndVibration(notificationBuild);
                }
            }
            return notificationBuild;
        }
        if (i >= 21) {
            this.mBuilder.setExtras(this.mExtras);
            Notification notificationBuild2 = this.mBuilder.build();
            RemoteViews remoteViews = this.mContentView;
            if (remoteViews != null) {
                notificationBuild2.contentView = remoteViews;
            }
            RemoteViews remoteViews2 = this.mBigContentView;
            if (remoteViews2 != null) {
                notificationBuild2.bigContentView = remoteViews2;
            }
            RemoteViews remoteViews3 = this.mHeadsUpContentView;
            if (remoteViews3 != null) {
                notificationBuild2.headsUpContentView = remoteViews3;
            }
            if (this.mGroupAlertBehavior != 0) {
                if (notificationBuild2.getGroup() != null && (notificationBuild2.flags & 512) != 0 && this.mGroupAlertBehavior == 2) {
                    removeSoundAndVibration(notificationBuild2);
                }
                if (notificationBuild2.getGroup() != null && (notificationBuild2.flags & 512) == 0 && this.mGroupAlertBehavior == 1) {
                    removeSoundAndVibration(notificationBuild2);
                }
            }
            return notificationBuild2;
        }
        if (i >= 20) {
            this.mBuilder.setExtras(this.mExtras);
            Notification notificationBuild3 = this.mBuilder.build();
            RemoteViews remoteViews4 = this.mContentView;
            if (remoteViews4 != null) {
                notificationBuild3.contentView = remoteViews4;
            }
            RemoteViews remoteViews5 = this.mBigContentView;
            if (remoteViews5 != null) {
                notificationBuild3.bigContentView = remoteViews5;
            }
            if (this.mGroupAlertBehavior != 0) {
                if (notificationBuild3.getGroup() != null && (notificationBuild3.flags & 512) != 0 && this.mGroupAlertBehavior == 2) {
                    removeSoundAndVibration(notificationBuild3);
                }
                if (notificationBuild3.getGroup() != null && (notificationBuild3.flags & 512) == 0 && this.mGroupAlertBehavior == 1) {
                    removeSoundAndVibration(notificationBuild3);
                }
            }
            return notificationBuild3;
        }
        if (i >= 19) {
            SparseArray<Bundle> sparseArrayBuildActionExtrasMap = NotificationCompatJellybean.buildActionExtrasMap(this.mActionExtrasList);
            if (sparseArrayBuildActionExtrasMap != null) {
                this.mExtras.putSparseParcelableArray(NotificationCompatExtras.EXTRA_ACTION_EXTRAS, sparseArrayBuildActionExtrasMap);
            }
            this.mBuilder.setExtras(this.mExtras);
            Notification notificationBuild4 = this.mBuilder.build();
            RemoteViews remoteViews6 = this.mContentView;
            if (remoteViews6 != null) {
                notificationBuild4.contentView = remoteViews6;
            }
            RemoteViews remoteViews7 = this.mBigContentView;
            if (remoteViews7 != null) {
                notificationBuild4.bigContentView = remoteViews7;
            }
            return notificationBuild4;
        }
        if (i < 16) {
            return this.mBuilder.getNotification();
        }
        Notification notificationBuild5 = this.mBuilder.build();
        Bundle extras = NotificationCompat.getExtras(notificationBuild5);
        Bundle bundle = new Bundle(this.mExtras);
        for (String str : this.mExtras.keySet()) {
            if (extras.containsKey(str)) {
                bundle.remove(str);
            }
        }
        extras.putAll(bundle);
        SparseArray<Bundle> sparseArrayBuildActionExtrasMap2 = NotificationCompatJellybean.buildActionExtrasMap(this.mActionExtrasList);
        if (sparseArrayBuildActionExtrasMap2 != null) {
            NotificationCompat.getExtras(notificationBuild5).putSparseParcelableArray(NotificationCompatExtras.EXTRA_ACTION_EXTRAS, sparseArrayBuildActionExtrasMap2);
        }
        RemoteViews remoteViews8 = this.mContentView;
        if (remoteViews8 != null) {
            notificationBuild5.contentView = remoteViews8;
        }
        RemoteViews remoteViews9 = this.mBigContentView;
        if (remoteViews9 != null) {
            notificationBuild5.bigContentView = remoteViews9;
        }
        return notificationBuild5;
    }

    @Override // android.support.v4.app.NotificationBuilderWithBuilderAccessor
    public Notification.Builder getBuilder() {
        return this.mBuilder;
    }
}
