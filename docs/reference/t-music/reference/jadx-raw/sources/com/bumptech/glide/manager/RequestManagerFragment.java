package com.bumptech.glide.manager;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import java.util.HashSet;
import p011c.p012a.p013a.C0511a;

@TargetApi(11)
/* loaded from: classes3.dex */
public class RequestManagerFragment extends Fragment {

    /* renamed from: ga */
    private final C0521a f331ga;

    /* renamed from: ha */
    private final InterfaceC0526f f332ha;

    /* renamed from: ia */
    private C0511a f333ia;

    /* renamed from: ja */
    private final HashSet<RequestManagerFragment> f334ja;

    /* renamed from: ka */
    private RequestManagerFragment f335ka;

    /* renamed from: com.bumptech.glide.manager.RequestManagerFragment$a */
    private class C0519a implements InterfaceC0526f {
        private C0519a() {
        }
    }

    public RequestManagerFragment() {
        this(new C0521a());
    }

    /* renamed from: a */
    private void m168a(RequestManagerFragment requestManagerFragment) {
        this.f334ja.add(requestManagerFragment);
    }

    /* renamed from: b */
    private void m169b(RequestManagerFragment requestManagerFragment) {
        this.f334ja.remove(requestManagerFragment);
    }

    @Override // android.app.Fragment
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.f335ka = C0525e.get().m172a(getActivity().getFragmentManager());
        RequestManagerFragment requestManagerFragment = this.f335ka;
        if (requestManagerFragment != this) {
            requestManagerFragment.m168a(this);
        }
    }

    @Override // android.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        this.f331ga.onDestroy();
    }

    @Override // android.app.Fragment
    public void onDetach() {
        super.onDetach();
        RequestManagerFragment requestManagerFragment = this.f335ka;
        if (requestManagerFragment != null) {
            requestManagerFragment.m169b(this);
            this.f335ka = null;
        }
    }

    @Override // android.app.Fragment, android.content.ComponentCallbacks
    public void onLowMemory() {
        C0511a c0511a = this.f333ia;
        if (c0511a == null) {
            return;
        }
        c0511a.onLowMemory();
        throw null;
    }

    @Override // android.app.Fragment
    public void onStart() {
        super.onStart();
        this.f331ga.onStart();
    }

    @Override // android.app.Fragment
    public void onStop() {
        super.onStop();
        this.f331ga.onStop();
    }

    @Override // android.app.Fragment, android.content.ComponentCallbacks2
    public void onTrimMemory(int i) {
        C0511a c0511a = this.f333ia;
        if (c0511a == null) {
            return;
        }
        c0511a.onTrimMemory(i);
        throw null;
    }

    @SuppressLint({"ValidFragment"})
    RequestManagerFragment(C0521a c0521a) {
        this.f332ha = new C0519a();
        this.f334ja = new HashSet<>();
        this.f331ga = c0521a;
    }
}
