package com.eckom.xtlibrary.p020b.p053j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/* compiled from: DateTimeUtils.java */
/* renamed from: com.eckom.xtlibrary.b.j.a */
/* loaded from: classes3.dex */
public class C0685a {
    /* renamed from: g */
    public static boolean m995g(String str, String str2, String str3) {
        Calendar calendar = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        try {
            calendar.setTime(new SimpleDateFormat(str).parse(str2));
            calendar2.setTime(new SimpleDateFormat(str).parse(str3));
            return calendar.after(calendar2);
        } catch (ParseException unused) {
            return false;
        }
    }
}
