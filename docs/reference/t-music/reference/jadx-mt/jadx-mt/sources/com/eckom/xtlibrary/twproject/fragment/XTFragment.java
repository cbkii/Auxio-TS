package com.eckom.xtlibrary.twproject.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import com.eckom.xtlibrary.p020b.p045g.AbstractC0658a;
import com.eckom.xtlibrary.p020b.p058l.InterfaceC0710a;

/* JADX INFO: loaded from: classes3.dex */
public abstract class XTFragment<P extends AbstractC0658a> extends Fragment implements InterfaceC0710a {
    protected Context mContext;
    public P mPresenter;

    /* JADX INFO: renamed from: a */
    public abstract void m1138a(View view);

    @Override // android.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context.getApplicationContext();
    }

    @Override // android.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override // android.app.Fragment
    public void onDestroy() {
        this.mPresenter.delete();
        this.mPresenter.onDestroy();
        super.onDestroy();
    }

    @Override // android.app.Fragment
    public void onPause() {
        super.onPause();
    }

    @Override // android.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        m1138a(view);
        if (this.mPresenter == null) {
            this.mPresenter = (P) mo1135ra();
            this.mPresenter.m807a(this);
        }
    }

    /* JADX INFO: renamed from: ra */
    public abstract P mo1135ra();
}
