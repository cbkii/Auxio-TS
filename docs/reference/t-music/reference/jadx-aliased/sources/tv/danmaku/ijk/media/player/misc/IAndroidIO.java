package tv.danmaku.ijk.media.player.misc;

/* loaded from: classes4.dex */
public interface IAndroidIO {
    int close();

    int open(String str);

    int read(byte[] bArr, int i);

    long seek(long j, int i);
}
