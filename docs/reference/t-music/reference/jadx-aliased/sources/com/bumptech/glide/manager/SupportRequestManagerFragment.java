package com.bumptech.glide.manager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v4.app.Fragment;
import java.util.HashSet;
import p060c.p061a.p062a.C0511a;

/* loaded from: classes3.dex */
public class SupportRequestManagerFragment extends Fragment {

    /* renamed from: ga */
    private final C0521a f336ga;

    /* renamed from: ha */
    private final InterfaceC0526f f337ha;

    /* renamed from: ia */
    private C0511a f338ia;

    /* renamed from: ja */
    private final HashSet<SupportRequestManagerFragment> f339ja;

    /* renamed from: ka */
    private SupportRequestManagerFragment f340ka;

    /* renamed from: com.bumptech.glide.manager.SupportRequestManagerFragment$a */
    private class C0520a implements InterfaceC0526f {
        private C0520a() {
        }
    }

    public SupportRequestManagerFragment() {
        this(new C0521a());
    }

    /* renamed from: a */
    private void m170a(SupportRequestManagerFragment supportRequestManagerFragment) {
        this.f339ja.add(supportRequestManagerFragment);
    }

    /* renamed from: b */
    private void m171b(SupportRequestManagerFragment supportRequestManagerFragment) {
        this.f339ja.remove(supportRequestManagerFragment);
    }

    @Override // android.support.v4.app.Fragment
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.f340ka = C0525e.get().m173a(getActivity().getSupportFragmentManager());
        SupportRequestManagerFragment supportRequestManagerFragment = this.f340ka;
        if (supportRequestManagerFragment != this) {
            supportRequestManagerFragment.m170a(this);
        }
    }

    @Override // android.support.v4.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        this.f336ga.onDestroy();
    }

    @Override // android.support.v4.app.Fragment
    public void onDetach() {
        super.onDetach();
        SupportRequestManagerFragment supportRequestManagerFragment = this.f340ka;
        if (supportRequestManagerFragment != null) {
            supportRequestManagerFragment.m171b(this);
            this.f340ka = null;
        }
    }

    @Override // android.support.v4.app.Fragment, android.content.ComponentCallbacks
    public void onLowMemory() {
        super.onLowMemory();
        C0511a c0511a = this.f338ia;
        if (c0511a == null) {
            return;
        }
        c0511a.onLowMemory();
        throw null;
    }

    @Override // android.support.v4.app.Fragment
    public void onStart() {
        super.onStart();
        this.f336ga.onStart();
    }

    @Override // android.support.v4.app.Fragment
    public void onStop() {
        super.onStop();
        this.f336ga.onStop();
    }

    @SuppressLint({"ValidFragment"})
    public SupportRequestManagerFragment(C0521a c0521a) {
        this.f337ha = new C0520a();
        this.f339ja = new HashSet<>();
        this.f336ga = c0521a;
    }
}
