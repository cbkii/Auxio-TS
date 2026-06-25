package com.eckom.xtlibrary.p020b.p037f.p043f;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.eckom.xtlibrary.p020b.p037f.p039b.C0579f;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONObject;

/* compiled from: CollectionUtils.java */
/* renamed from: com.eckom.xtlibrary.b.f.f.a */
/* loaded from: classes3.dex */
public class C0636a {

    /* renamed from: vk */
    private static SharedPreferences f672vk;

    /* renamed from: a */
    public static ArrayList<C0579f> m740a(Context context, ArrayList<C0579f> arrayList) {
        try {
            arrayList.clear();
            f672vk = context.getSharedPreferences("TAG", 0);
            JSONArray jSONArray = new JSONArray(f672vk.getString("TAG", ""));
            for (int i = 0; i < jSONArray.length(); i++) {
                JSONObject jSONObject = jSONArray.getJSONObject(i);
                String optString = jSONObject.optString("music_name", "");
                String optString2 = jSONObject.optString("music_path", "");
                if (new File(optString2).exists()) {
                    arrayList.add(new C0579f(optString, optString2));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    /* renamed from: b */
    public static void m744b(Context context, ArrayList<C0579f> arrayList) {
        try {
            f672vk = context.getSharedPreferences("TAG", 0);
            JSONArray jSONArray = new JSONArray();
            Iterator<C0579f> it = arrayList.iterator();
            while (it.hasNext()) {
                C0579f next = it.next();
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("music_name", next.mName);
                jSONObject.put("music_path", next.mPath);
                jSONArray.put(jSONObject);
            }
            f672vk.edit().putString("TAG", jSONArray.toString()).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* renamed from: a */
    public static boolean m743a(Context context, String str, ArrayList<C0579f> arrayList) {
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (arrayList.size() < 1) {
            return false;
        }
        Iterator<C0579f> it = arrayList.iterator();
        while (it.hasNext()) {
            if (TextUtils.equals(it.next().mPath, str)) {
                return true;
            }
        }
        return false;
    }

    /* renamed from: a */
    public static ArrayList<C0579f> m741a(C0579f c0579f, ArrayList<C0579f> arrayList) {
        try {
            arrayList.add(c0579f);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    /* renamed from: a */
    public static synchronized ArrayList<C0579f> m742a(String str, ArrayList<C0579f> arrayList) {
        synchronized (C0636a.class) {
            Iterator<C0579f> it = arrayList.iterator();
            while (it.hasNext()) {
                if (it.next().mPath.equals(str)) {
                    it.remove();
                }
            }
        }
        return arrayList;
    }
}
