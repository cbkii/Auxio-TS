package p057b.p058a.p059a.p010a;

import android.content.res.ColorStateList;
import android.support.annotation.RestrictTo;
import android.support.v4.graphics.drawable.IconCompat;
import androidx.versionedparcelable.AbstractC0505c;

/* compiled from: IconCompatParcelizer.java */
@RestrictTo({RestrictTo.Scope.LIBRARY})
/* renamed from: b.a.a.a.a */
/* loaded from: classes3.dex */
public class C0510a {
    public static IconCompat read(AbstractC0505c abstractC0505c) {
        IconCompat iconCompat = new IconCompat();
        iconCompat.mType = abstractC0505c.readInt(iconCompat.mType, 1);
        iconCompat.mData = abstractC0505c.m96f(iconCompat.mData, 2);
        iconCompat.mParcelable = abstractC0505c.m90a((AbstractC0505c) iconCompat.mParcelable, 3);
        iconCompat.mInt1 = abstractC0505c.readInt(iconCompat.mInt1, 4);
        iconCompat.mInt2 = abstractC0505c.readInt(iconCompat.mInt2, 5);
        iconCompat.mTintList = (ColorStateList) abstractC0505c.m90a((AbstractC0505c) iconCompat.mTintList, 6);
        iconCompat.mTintModeStr = abstractC0505c.m98i(iconCompat.mTintModeStr, 7);
        iconCompat.onPostParceling();
        return iconCompat;
    }

    public static void write(IconCompat iconCompat, AbstractC0505c abstractC0505c) {
        abstractC0505c.m93a(true, true);
        iconCompat.onPreParceling(abstractC0505c.m89_a());
        abstractC0505c.m102k(iconCompat.mType, 1);
        abstractC0505c.m97g(iconCompat.mData, 2);
        abstractC0505c.writeParcelable(iconCompat.mParcelable, 3);
        abstractC0505c.m102k(iconCompat.mInt1, 4);
        abstractC0505c.m102k(iconCompat.mInt2, 5);
        abstractC0505c.writeParcelable(iconCompat.mTintList, 6);
        abstractC0505c.m100j(iconCompat.mTintModeStr, 7);
    }
}
