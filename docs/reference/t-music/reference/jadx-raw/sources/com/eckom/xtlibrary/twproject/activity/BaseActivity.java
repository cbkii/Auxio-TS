package com.eckom.xtlibrary.twproject.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

/* loaded from: classes3.dex */
public abstract class BaseActivity extends AppCompatActivity {

    /* renamed from: Wa */
    private float f857Wa = 0.0f;

    /* renamed from: Xa */
    private float f858Xa = 0.0f;

    /* renamed from: ne */
    private void m1095ne() {
        ((WindowManager) getSystemService("window")).getDefaultDisplay().getRealMetrics(new DisplayMetrics());
        this.f857Wa = r0.widthPixels;
        this.f858Xa = r0.heightPixels;
        Log.d("BaseActivity", "getScreenSize: " + this.f857Wa + "x" + this.f858Xa);
    }

    /* renamed from: Aa */
    protected void mo1096Aa() {
    }

    /* renamed from: Ea */
    protected void mo1097Ea() {
    }

    /* renamed from: Fa */
    protected void m1098Fa() {
    }

    protected abstract int getContentView();

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.SupportActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        m1095ne();
        int contentView = getContentView();
        Log.d("BaseActivity", "containViewId:" + contentView);
        if (contentView > 0) {
            setContentView(contentView);
        }
        mo1096Aa();
        mo1097Ea();
        m1098Fa();
    }

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
    }
}
