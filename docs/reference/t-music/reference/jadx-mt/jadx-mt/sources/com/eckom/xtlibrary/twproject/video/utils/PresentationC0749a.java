package com.eckom.xtlibrary.twproject.video.utils;

import android.app.Presentation;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.widget.FrameLayout;
import com.eckom.xtlibrary.R$id;
import com.eckom.xtlibrary.R$layout;

/* JADX INFO: renamed from: com.eckom.xtlibrary.twproject.video.utils.a */
/* JADX INFO: compiled from: BionPresentation.java */
/* JADX INFO: loaded from: classes3.dex */
public class PresentationC0749a extends Presentation {

    /* JADX INFO: renamed from: ea */
    private static PresentationC0749a f978ea;

    /* JADX INFO: renamed from: da */
    private FrameLayout f979da;
    private boolean flag;

    private PresentationC0749a(Context context, Display display) {
        super(context, display);
        this.flag = false;
        int i = Build.VERSION.SDK_INT;
        if (i >= 33) {
            getWindow().setType(2037);
        } else if (i >= 31) {
            getWindow().setType(2030);
        } else {
            getWindow().setType(2003);
        }
    }

    /* JADX INFO: renamed from: a */
    public static PresentationC0749a m1293a(Context context, Display display) {
        if (f978ea == null) {
            f978ea = new PresentationC0749a(context, display);
        }
        return f978ea;
    }

    @Override // android.app.Dialog
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R$layout.layout_presentation);
    }

    /* JADX INFO: renamed from: pa */
    public boolean m1294pa() {
        return this.flag;
    }

    /* JADX INFO: renamed from: qa */
    public FrameLayout m1295qa() {
        if (this.f979da == null) {
            this.f979da = (FrameLayout) findViewById(R$id.sv_video_sub);
        }
        return this.f979da;
    }

    /* JADX INFO: renamed from: x */
    public void m1296x(boolean z) {
        if (!z) {
            f978ea = null;
        }
        this.flag = z;
    }
}
