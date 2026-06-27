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

/* JADX INFO: renamed from: com.eckom.xtlibrary.b.f.f.a */
/* JADX INFO: compiled from: CollectionUtils.java */
/* JADX INFO: loaded from: classes3.dex */
public class C0636a {

    /* JADX INFO: renamed from: vk */
    private static SharedPreferences f672vk;

    /* JADX INFO: renamed from: a */
    public static ArrayList<C0579f> m740a(Context context, ArrayList<C0579f> arrayList) {
        try {
            arrayList.clear();
            f672vk = context.getSharedPreferences("TAG", 0);
            JSONArray jSONArray = new JSONArray(f672vk.getString("TAG", ""));
            for (int i = 0; i < jSONArray.length(); i++) {
                JSONObject jSONObject = jSONArray.getJSONObject(i);
                String strOptString = jSONObject.optString("music_name", "");
                String strOptString2 = jSONObject.optString("music_path", "");
                if (new File(strOptString2).exists()) {
                    arrayList.add(new C0579f(strOptString, strOptString2));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    /* JADX INFO: renamed from: b */
    public static void m744b(Context context, ArrayList<C0579f> arrayList) {
        try {
            f672vk = context.getSharedPreferences("TAG", 0);
            JSONArray jSONArray = new JSONArray();
            for (C0579f c0579f : arrayList) {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("music_name", c0579f.mName);
                jSONObject.put("music_path", c0579f.mPath);
                jSONArray.put(jSONObject);
            }
            f672vk.edit().putString("TAG", jSONArray.toString()).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: renamed from: a */
    public static boolean m743a(Context context, String str, ArrayList<C0579f> arrayList) {
        try {
            if (arrayList.size() < 1) {
                return false;
            }
            Iterator<C0579f> it = arrayList.iterator();
            while (it.hasNext()) {
                if (TextUtils.equals(it.next().mPath, str)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /* JADX INFO: renamed from: a */
    public static ArrayList<C0579f> m741a(C0579f c0579f, ArrayList<C0579f> arrayList) {
        try {
            arrayList.add(c0579f);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    /* JADX INFO: renamed from: a */
    public static synchronized ArrayList<C0579f> m742a(String str, ArrayList<C0579f> arrayList) {
        Iterator<C0579f> it = arrayList.iterator();
        while (it.hasNext()) {
            if (it.next().mPath.equals(str)) {
                it.remove();
            }
        }
        return arrayList;
    }
}
