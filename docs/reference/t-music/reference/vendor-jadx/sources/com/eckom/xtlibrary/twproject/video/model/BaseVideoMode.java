package com.eckom.xtlibrary.twproject.video.model;

import com.eckom.xtlibrary.p020b.p036e.C0570a;

/* loaded from: classes3.dex */
public abstract class BaseVideoMode extends C0570a {

    public enum VIDEO_MODEL_STATE {
        NULL,
        VIDEO_MODEL_CREATE,
        VIDEO_MODEL_RESUME,
        VIDEO_MODEL_PAUSE,
        VIDEO_MODEL_DESTROY
    }

    /* renamed from: P */
    public abstract void mo1154P();

    /* renamed from: Pb */
    public abstract void mo1155Pb();

    /* renamed from: ic */
    public abstract void mo1156ic();

    /* renamed from: jc */
    public abstract void mo1157jc();

    /* renamed from: ma */
    public abstract void mo1158ma();

    /* renamed from: w */
    public abstract void mo1159w(boolean z);
}
