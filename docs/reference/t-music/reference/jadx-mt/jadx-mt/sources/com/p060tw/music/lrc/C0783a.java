package com.p060tw.music.lrc;

import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* JADX INFO: renamed from: com.tw.music.lrc.a */
/* JADX INFO: compiled from: LrcEntry.java */
/* JADX INFO: loaded from: classes3.dex */
class C0783a implements Comparable<C0783a> {

    /* JADX INFO: renamed from: Gm */
    private StaticLayout f1170Gm;
    private float offset = Float.MIN_VALUE;
    private String text;
    private long time;

    private C0783a(long j, String str) {
        this.time = j;
        this.text = str;
    }

    /* JADX INFO: renamed from: Hb */
    private static List<C0783a> m1503Hb(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        Matcher matcher = Pattern.compile("((\\[\\d\\d:\\d\\d\\.\\d{2,3}\\])+)([\\s\\S]*)").matcher(str.trim());
        if (!matcher.matches()) {
            return null;
        }
        String strGroup = matcher.group(1);
        String strGroup2 = matcher.group(3);
        ArrayList arrayList = new ArrayList();
        Matcher matcher2 = Pattern.compile("\\[(\\d\\d):(\\d\\d)\\.(\\d){2,3}\\]").matcher(strGroup);
        while (matcher2.find()) {
            long j = Long.parseLong(matcher2.group(1));
            long j2 = Long.parseLong(matcher2.group(2));
            long j3 = Long.parseLong(matcher2.group(3));
            long j4 = (j * 60000) + (j2 * 1000);
            if (j3 < 100) {
                j3 *= 10;
            }
            arrayList.add(new C0783a(j4 + j3, strGroup2));
        }
        return arrayList;
    }

    /* JADX INFO: renamed from: ob */
    static List<C0783a> m1504ob(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (String str2 : str.split("\\n")) {
            List<C0783a> listM1503Hb = m1503Hb(str2);
            if (listM1503Hb != null && !listM1503Hb.isEmpty()) {
                arrayList.addAll(listM1503Hb);
            }
        }
        Collections.sort(arrayList);
        return arrayList;
    }

    /* JADX INFO: renamed from: a */
    void m1506a(TextPaint textPaint, int i, int i2) {
        this.f1170Gm = new StaticLayout(this.text, textPaint, i, i2 != 1 ? i2 != 3 ? Layout.Alignment.ALIGN_CENTER : Layout.Alignment.ALIGN_OPPOSITE : Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
    }

    int getHeight() {
        StaticLayout staticLayout = this.f1170Gm;
        if (staticLayout == null) {
            return 0;
        }
        return staticLayout.getHeight();
    }

    public float getOffset() {
        return this.offset;
    }

    long getTime() {
        return this.time;
    }

    /* JADX INFO: renamed from: kd */
    StaticLayout m1507kd() {
        return this.f1170Gm;
    }

    public void setOffset(float f) {
        this.offset = f;
    }

    @Override // java.lang.Comparable
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public int compareTo(C0783a c0783a) {
        if (c0783a == null) {
            return -1;
        }
        return (int) (this.time - c0783a.getTime());
    }
}
