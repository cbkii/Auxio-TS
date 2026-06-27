package com.eckom.xtlibrary.p020b.p053j;

import android.text.TextUtils;
import android.util.Base64;
import java.io.ByteArrayOutputStream;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.json.JSONObject;

/* JADX INFO: renamed from: com.eckom.xtlibrary.b.j.n */
/* JADX INFO: compiled from: RsaUtil.java */
/* JADX INFO: loaded from: classes3.dex */
public class C0698n {
    /* JADX INFO: renamed from: a */
    public static void m1022a(int i, Cipher cipher, byte[] bArr, int i2, ByteArrayOutputStream byteArrayOutputStream) throws BadPaddingException, IllegalBlockSizeException {
        byte[] bArrDoFinal;
        int i3 = 0;
        while (true) {
            int i4 = i2 - i3;
            if (i4 <= 0) {
                return;
            }
            if (i4 >= i) {
                bArrDoFinal = cipher.doFinal(bArr, i3, i);
                i3 += i;
            } else {
                bArrDoFinal = cipher.doFinal(bArr, i3, i4);
                i3 = i2;
            }
            byteArrayOutputStream.write(bArrDoFinal);
        }
    }

    public static String decrypt(String str) throws BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(2, m1024z("RSA", "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCmyWXcwD/iJ4mJyWgKTEoIhDCPnFRV4wTNssNnd1RYbJbJ9264Jua9GgqWe3hC2qz3K3xk6CrqMBiwphIBkOLzVAmFGpskKQgndqZviXNJ2tNEYH0MoO9jcQo1DsIQHoYFyYoBdvh4WqECdghLTBBoXU6SOnSMIlVk+xIw1uKpXQIDAQAB"));
        byte[] bArrDecode = Base64.decode(str, 2);
        int length = bArrDecode.length;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        m1022a(1024, cipher, bArrDecode, length, byteArrayOutputStream);
        return byteArrayOutputStream.toString();
    }

    /* JADX INFO: renamed from: h */
    public static boolean m1023h(String str, String str2, String str3) {
        JSONObject jSONObject = new JSONObject(str);
        return !TextUtils.isEmpty(str2) && str2.contains(jSONObject.optString("softwareType")) && TextUtils.equals(jSONObject.optString("cid"), str3);
    }

    /* JADX INFO: renamed from: z */
    public static PublicKey m1024z(String str, String str2) {
        return KeyFactory.getInstance(str).generatePublic(new X509EncodedKeySpec(Base64.decode(str2, 2)));
    }
}
