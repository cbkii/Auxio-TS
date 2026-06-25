package com.eckom.xtlibrary.twproject.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.eckom.xtlibrary.p066b.C0556b;

/* loaded from: classes3.dex */
public class ACCReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        char c2;
        String action = intent.getAction();
        Log.d("ACCReceiver", "XTManage ACCReceiver onReceive: " + action);
        int hashCode = action.hashCode();
        if (hashCode != 28079653) {
            if (hashCode == 870469065 && action.equals("com.unisound.intent.action.ACC_OFF")) {
                c2 = 0;
            }
            c2 = 65535;
        } else {
            if (action.equals("com.unisound.intent.action.ACC_ON")) {
                c2 = 1;
            }
            c2 = 65535;
        }
        if (c2 != 0) {
            return;
        }
        C0556b.getInstant().m386db();
    }
}
