package android.arch.lifecycle;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.arch.lifecycle.Lifecycle;
import android.content.ComponentCallbacks2;
import android.os.Bundle;
import android.support.annotation.RestrictTo;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
/* loaded from: classes.dex */
public class ReportFragment extends Fragment {

    /* renamed from: fa */
    private InterfaceC0011a f44fa;

    /* renamed from: android.arch.lifecycle.ReportFragment$a */
    interface InterfaceC0011a {
        void onCreate();

        void onResume();

        void onStart();
    }

    /* renamed from: a */
    public static void m34a(Activity activity) {
        FragmentManager fragmentManager = activity.getFragmentManager();
        if (fragmentManager.findFragmentByTag("android.arch.lifecycle.LifecycleDispatcher.report_fragment_tag") == null) {
            fragmentManager.beginTransaction().add(new ReportFragment(), "android.arch.lifecycle.LifecycleDispatcher.report_fragment_tag").commit();
            fragmentManager.executePendingTransactions();
        }
    }

    /* renamed from: b */
    private void m36b(InterfaceC0011a interfaceC0011a) {
        if (interfaceC0011a != null) {
            interfaceC0011a.onResume();
        }
    }

    /* renamed from: c */
    private void m38c(InterfaceC0011a interfaceC0011a) {
        if (interfaceC0011a != null) {
            interfaceC0011a.onStart();
        }
    }

    @Override // android.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        m35a(this.f44fa);
        m37c(Lifecycle.Event.ON_CREATE);
    }

    @Override // android.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        m37c(Lifecycle.Event.ON_DESTROY);
        this.f44fa = null;
    }

    @Override // android.app.Fragment
    public void onPause() {
        super.onPause();
        m37c(Lifecycle.Event.ON_PAUSE);
    }

    @Override // android.app.Fragment
    public void onResume() {
        super.onResume();
        m36b(this.f44fa);
        m37c(Lifecycle.Event.ON_RESUME);
    }

    @Override // android.app.Fragment
    public void onStart() {
        super.onStart();
        m38c(this.f44fa);
        m37c(Lifecycle.Event.ON_START);
    }

    @Override // android.app.Fragment
    public void onStop() {
        super.onStop();
        m37c(Lifecycle.Event.ON_STOP);
    }

    /* renamed from: c */
    private void m37c(Lifecycle.Event event) {
        ComponentCallbacks2 activity = getActivity();
        if (activity instanceof InterfaceC0019h) {
            ((InterfaceC0019h) activity).getLifecycle().m60b(event);
        } else if (activity instanceof InterfaceC0016e) {
            Lifecycle lifecycle = ((InterfaceC0016e) activity).getLifecycle();
            if (lifecycle instanceof C0018g) {
                ((C0018g) lifecycle).m60b(event);
            }
        }
    }

    /* renamed from: a */
    private void m35a(InterfaceC0011a interfaceC0011a) {
        if (interfaceC0011a != null) {
            interfaceC0011a.onCreate();
        }
    }
}
