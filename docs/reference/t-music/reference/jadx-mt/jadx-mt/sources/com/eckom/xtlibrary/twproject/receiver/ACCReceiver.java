package com.eckom.xtlibrary.twproject.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.eckom.xtlibrary.p020b.C0556b;

/* JADX INFO: loaded from: classes3.dex */
public class ACCReceiver extends BroadcastReceiver {
    /* JADX WARN: Removed duplicated region for block: B:13:0x003d  */
    @Override // android.content.BroadcastReceiver
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onReceive(Context context, Intent intent) {
        byte b2;
        String action = intent.getAction();
        Log.d("ACCReceiver", "XTManage ACCReceiver onReceive: " + action);
        int iHashCode = action.hashCode();
        if (iHashCode != 28079653) {
            b2 = (iHashCode == 870469065 && action.equals("com.unisound.intent.action.ACC_OFF")) ? (byte) 0 : (byte) -1;
        } else if (action.equals("com.unisound.intent.action.ACC_ON")) {
            b2 = 1;
        }
        if (b2 != 0) {
            return;
        }
        C0556b.getInstant().m386db();
    }
}
