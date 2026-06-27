package android.support.v4.graphics.drawable;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.annotation.VisibleForTesting;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.os.BuildCompat;
import android.support.v4.util.Preconditions;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.util.Log;
import androidx.versionedparcelable.AbstractC0503a;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;

/* JADX INFO: loaded from: classes3.dex */
public class IconCompat extends AbstractC0503a {
    private static final float ADAPTIVE_ICON_INSET_FACTOR = 0.25f;
    private static final int AMBIENT_SHADOW_ALPHA = 30;
    private static final float BLUR_FACTOR = 0.010416667f;
    static final PorterDuff.Mode DEFAULT_TINT_MODE = PorterDuff.Mode.SRC_IN;
    private static final float DEFAULT_VIEW_PORT_SCALE = 0.6666667f;
    private static final String EXTRA_INT1 = "int1";
    private static final String EXTRA_INT2 = "int2";
    private static final String EXTRA_OBJ = "obj";
    private static final String EXTRA_TINT_LIST = "tint_list";
    private static final String EXTRA_TINT_MODE = "tint_mode";
    private static final String EXTRA_TYPE = "type";
    private static final float ICON_DIAMETER_FACTOR = 0.9166667f;
    private static final int KEY_SHADOW_ALPHA = 61;
    private static final float KEY_SHADOW_OFFSET_FACTOR = 0.020833334f;
    private static final String TAG = "IconCompat";
    public static final int TYPE_UNKNOWN = -1;

    @RestrictTo({RestrictTo.Scope.LIBRARY})
    public byte[] mData;

    @RestrictTo({RestrictTo.Scope.LIBRARY})
    public int mInt1;

    @RestrictTo({RestrictTo.Scope.LIBRARY})
    public int mInt2;
    Object mObj1;

    @RestrictTo({RestrictTo.Scope.LIBRARY})
    public Parcelable mParcelable;

    @RestrictTo({RestrictTo.Scope.LIBRARY})
    public ColorStateList mTintList = null;
    PorterDuff.Mode mTintMode = DEFAULT_TINT_MODE;

    @RestrictTo({RestrictTo.Scope.LIBRARY})
    public String mTintModeStr;

    @RestrictTo({RestrictTo.Scope.LIBRARY})
    public int mType;

    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({RestrictTo.Scope.LIBRARY})
    public @interface IconType {
    }

    @RestrictTo({RestrictTo.Scope.LIBRARY})
    public IconCompat() {
    }

    @Nullable
    public static IconCompat createFromBundle(@NonNull Bundle bundle) {
        int i = bundle.getInt("type");
        IconCompat iconCompat = new IconCompat(i);
        iconCompat.mInt1 = bundle.getInt(EXTRA_INT1);
        iconCompat.mInt2 = bundle.getInt(EXTRA_INT2);
        if (bundle.containsKey(EXTRA_TINT_LIST)) {
            iconCompat.mTintList = (ColorStateList) bundle.getParcelable(EXTRA_TINT_LIST);
        }
        if (bundle.containsKey(EXTRA_TINT_MODE)) {
            iconCompat.mTintMode = PorterDuff.Mode.valueOf(bundle.getString(EXTRA_TINT_MODE));
        }
        if (i == -1 || i == 1) {
            iconCompat.mObj1 = bundle.getParcelable(EXTRA_OBJ);
        } else if (i == 2) {
            iconCompat.mObj1 = bundle.getString(EXTRA_OBJ);
        } else if (i != 3) {
            if (i != 4) {
                if (i != 5) {
                    Log.w(TAG, "Unknown type " + i);
                    return null;
                }
                iconCompat.mObj1 = bundle.getParcelable(EXTRA_OBJ);
            }
            iconCompat.mObj1 = bundle.getString(EXTRA_OBJ);
        } else {
            iconCompat.mObj1 = bundle.getByteArray(EXTRA_OBJ);
        }
        return iconCompat;
    }

    @RequiresApi(23)
    @Nullable
    public static IconCompat createFromIcon(@NonNull Icon icon) {
        Preconditions.checkNotNull(icon);
        int type = getType(icon);
        if (type == 2) {
            return createWithResource(getResPackage(icon), getResId(icon));
        }
        if (type == 4) {
            return createWithContentUri(getUri(icon));
        }
        IconCompat iconCompat = new IconCompat(-1);
        iconCompat.mObj1 = icon;
        return iconCompat;
    }

    @VisibleForTesting
    static Bitmap createLegacyIconFromAdaptiveIcon(Bitmap bitmap, boolean z) {
        int iMin = (int) (Math.min(bitmap.getWidth(), bitmap.getHeight()) * DEFAULT_VIEW_PORT_SCALE);
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(iMin, iMin, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapCreateBitmap);
        Paint paint = new Paint(3);
        float f = iMin;
        float f2 = 0.5f * f;
        float f3 = ICON_DIAMETER_FACTOR * f2;
        if (z) {
            float f4 = BLUR_FACTOR * f;
            paint.setColor(0);
            paint.setShadowLayer(f4, 0.0f, f * KEY_SHADOW_OFFSET_FACTOR, 1023410176);
            canvas.drawCircle(f2, f2, f3, paint);
            paint.setShadowLayer(f4, 0.0f, 0.0f, 503316480);
            canvas.drawCircle(f2, f2, f3, paint);
            paint.clearShadowLayer();
        }
        paint.setColor(ViewCompat.MEASURED_STATE_MASK);
        Shader.TileMode tileMode = Shader.TileMode.CLAMP;
        BitmapShader bitmapShader = new BitmapShader(bitmap, tileMode, tileMode);
        Matrix matrix = new Matrix();
        matrix.setTranslate((-(bitmap.getWidth() - iMin)) / 2, (-(bitmap.getHeight() - iMin)) / 2);
        bitmapShader.setLocalMatrix(matrix);
        paint.setShader(bitmapShader);
        canvas.drawCircle(f2, f2, f3, paint);
        canvas.setBitmap(null);
        return bitmapCreateBitmap;
    }

    public static IconCompat createWithAdaptiveBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            throw new IllegalArgumentException("Bitmap must not be null.");
        }
        IconCompat iconCompat = new IconCompat(5);
        iconCompat.mObj1 = bitmap;
        return iconCompat;
    }

    public static IconCompat createWithBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            throw new IllegalArgumentException("Bitmap must not be null.");
        }
        IconCompat iconCompat = new IconCompat(1);
        iconCompat.mObj1 = bitmap;
        return iconCompat;
    }

    public static IconCompat createWithContentUri(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Uri must not be null.");
        }
        IconCompat iconCompat = new IconCompat(4);
        iconCompat.mObj1 = str;
        return iconCompat;
    }

    public static IconCompat createWithData(byte[] bArr, int i, int i2) {
        if (bArr == null) {
            throw new IllegalArgumentException("Data must not be null.");
        }
        IconCompat iconCompat = new IconCompat(3);
        iconCompat.mObj1 = bArr;
        iconCompat.mInt1 = i;
        iconCompat.mInt2 = i2;
        return iconCompat;
    }

    public static IconCompat createWithResource(Context context, @DrawableRes int i) {
        if (context == null) {
            throw new IllegalArgumentException("Context must not be null.");
        }
        IconCompat iconCompat = new IconCompat(2);
        iconCompat.mInt1 = i;
        iconCompat.mObj1 = context.getPackageName();
        return iconCompat;
    }

    /* JADX WARN: Removed duplicated region for block: B:30:0x008d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private Drawable loadDrawableInner(Context context) {
        Resources resourcesForApplication;
        InputStream inputStreamOpenInputStream;
        int i = this.mType;
        if (i == 1) {
            return new BitmapDrawable(context.getResources(), (Bitmap) this.mObj1);
        }
        if (i == 2) {
            String packageName = (String) this.mObj1;
            if (TextUtils.isEmpty(packageName)) {
                packageName = context.getPackageName();
            }
            if ("android".equals(packageName)) {
                resourcesForApplication = Resources.getSystem();
            } else {
                PackageManager packageManager = context.getPackageManager();
                try {
                    ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, 8192);
                    if (applicationInfo != null) {
                        resourcesForApplication = packageManager.getResourcesForApplication(applicationInfo);
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    Log.e(TAG, String.format("Unable to find pkg=%s for icon %s", packageName, this), e);
                }
            }
            try {
                return ResourcesCompat.getDrawable(resourcesForApplication, this.mInt1, context.getTheme());
            } catch (RuntimeException e2) {
                Log.e(TAG, String.format("Unable to load resource 0x%08x from pkg=%s", Integer.valueOf(this.mInt1), this.mObj1), e2);
            }
        } else {
            if (i == 3) {
                return new BitmapDrawable(context.getResources(), BitmapFactory.decodeByteArray((byte[]) this.mObj1, this.mInt1, this.mInt2));
            }
            if (i == 4) {
                Uri uri = Uri.parse((String) this.mObj1);
                String scheme = uri.getScheme();
                if ("content".equals(scheme) || "file".equals(scheme)) {
                    try {
                        inputStreamOpenInputStream = context.getContentResolver().openInputStream(uri);
                    } catch (Exception e3) {
                        Log.w(TAG, "Unable to load image from URI: " + uri, e3);
                        inputStreamOpenInputStream = null;
                    }
                    if (inputStreamOpenInputStream != null) {
                        return new BitmapDrawable(context.getResources(), BitmapFactory.decodeStream(inputStreamOpenInputStream));
                    }
                } else {
                    try {
                        inputStreamOpenInputStream = new FileInputStream(new File((String) this.mObj1));
                    } catch (FileNotFoundException e4) {
                        Log.w(TAG, "Unable to load image from path: " + uri, e4);
                        inputStreamOpenInputStream = null;
                    }
                    if (inputStreamOpenInputStream != null) {
                    }
                }
            } else if (i == 5) {
                return new BitmapDrawable(context.getResources(), createLegacyIconFromAdaptiveIcon((Bitmap) this.mObj1, false));
            }
        }
        return null;
    }

    private static String typeToString(int i) {
        return i != 1 ? i != 2 ? i != 3 ? i != 4 ? i != 5 ? "UNKNOWN" : "BITMAP_MASKABLE" : "URI" : "DATA" : "RESOURCE" : "BITMAP";
    }

    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public void addToShortcutIntent(@NonNull Intent intent, @Nullable Drawable drawable, @NonNull Context context) {
        Bitmap bitmapCopy;
        Bitmap bitmapCreateBitmap;
        int i = this.mType;
        if (i == 1) {
            bitmapCopy = (Bitmap) this.mObj1;
            if (drawable != null) {
                bitmapCopy = bitmapCopy.copy(bitmapCopy.getConfig(), true);
            }
        } else if (i == 2) {
            try {
                Context contextCreatePackageContext = context.createPackageContext((String) this.mObj1, 0);
                if (drawable == null) {
                    intent.putExtra("android.intent.extra.shortcut.ICON_RESOURCE", Intent.ShortcutIconResource.fromContext(contextCreatePackageContext, this.mInt1));
                    return;
                }
                Drawable drawable2 = ContextCompat.getDrawable(contextCreatePackageContext, this.mInt1);
                if (drawable2.getIntrinsicWidth() <= 0 || drawable2.getIntrinsicHeight() <= 0) {
                    int launcherLargeIconSize = ((ActivityManager) contextCreatePackageContext.getSystemService("activity")).getLauncherLargeIconSize();
                    bitmapCreateBitmap = Bitmap.createBitmap(launcherLargeIconSize, launcherLargeIconSize, Bitmap.Config.ARGB_8888);
                } else {
                    bitmapCreateBitmap = Bitmap.createBitmap(drawable2.getIntrinsicWidth(), drawable2.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                }
                drawable2.setBounds(0, 0, bitmapCreateBitmap.getWidth(), bitmapCreateBitmap.getHeight());
                drawable2.draw(new Canvas(bitmapCreateBitmap));
                bitmapCopy = bitmapCreateBitmap;
            } catch (PackageManager.NameNotFoundException e) {
                throw new IllegalArgumentException("Can't find package " + this.mObj1, e);
            }
        } else {
            if (i != 5) {
                throw new IllegalArgumentException("Icon type not supported for intent shortcuts");
            }
            bitmapCopy = createLegacyIconFromAdaptiveIcon((Bitmap) this.mObj1, true);
        }
        if (drawable != null) {
            int width = bitmapCopy.getWidth();
            int height = bitmapCopy.getHeight();
            drawable.setBounds(width / 2, height / 2, width, height);
            drawable.draw(new Canvas(bitmapCopy));
        }
        intent.putExtra("android.intent.extra.shortcut.ICON", bitmapCopy);
    }

    @IdRes
    public int getResId() {
        if (this.mType == -1 && Build.VERSION.SDK_INT >= 23) {
            return getResId((Icon) this.mObj1);
        }
        if (this.mType == 2) {
            return this.mInt1;
        }
        throw new IllegalStateException("called getResId() on " + this);
    }

    @NonNull
    public String getResPackage() {
        if (this.mType == -1 && Build.VERSION.SDK_INT >= 23) {
            return getResPackage((Icon) this.mObj1);
        }
        if (this.mType == 2) {
            return (String) this.mObj1;
        }
        throw new IllegalStateException("called getResPackage() on " + this);
    }

    public int getType() {
        return (this.mType != -1 || Build.VERSION.SDK_INT < 23) ? this.mType : getType((Icon) this.mObj1);
    }

    @NonNull
    public Uri getUri() {
        return (this.mType != -1 || Build.VERSION.SDK_INT < 23) ? Uri.parse((String) this.mObj1) : getUri((Icon) this.mObj1);
    }

    public Drawable loadDrawable(Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            return toIcon().loadDrawable(context);
        }
        Drawable drawableLoadDrawableInner = loadDrawableInner(context);
        if (drawableLoadDrawableInner != null && (this.mTintList != null || this.mTintMode != DEFAULT_TINT_MODE)) {
            drawableLoadDrawableInner.mutate();
            DrawableCompat.setTintList(drawableLoadDrawableInner, this.mTintList);
            DrawableCompat.setTintMode(drawableLoadDrawableInner, this.mTintMode);
        }
        return drawableLoadDrawableInner;
    }

    @Override // androidx.versionedparcelable.AbstractC0503a
    public void onPostParceling() {
        this.mTintMode = PorterDuff.Mode.valueOf(this.mTintModeStr);
        int i = this.mType;
        if (i == -1) {
            Parcelable parcelable = this.mParcelable;
            if (parcelable == null) {
                throw new IllegalArgumentException("Invalid icon");
            }
            this.mObj1 = parcelable;
            return;
        }
        if (i != 1) {
            if (i == 2) {
                this.mObj1 = new String(this.mData, Charset.forName("UTF-16"));
                return;
            }
            if (i == 3) {
                this.mObj1 = this.mData;
                return;
            } else if (i == 4) {
                this.mObj1 = Uri.parse(new String(this.mData, Charset.forName("UTF-16")));
                return;
            } else if (i != 5) {
                return;
            }
        }
        Parcelable parcelable2 = this.mParcelable;
        if (parcelable2 != null) {
            this.mObj1 = parcelable2;
            return;
        }
        byte[] bArr = this.mData;
        this.mObj1 = bArr;
        this.mType = 3;
        this.mInt1 = 0;
        this.mInt2 = bArr.length;
    }

    @Override // androidx.versionedparcelable.AbstractC0503a
    public void onPreParceling(boolean z) {
        this.mTintModeStr = this.mTintMode.name();
        int i = this.mType;
        if (i == -1) {
            if (z) {
                throw new IllegalArgumentException("Can't serialize Icon created with IconCompat#createFromIcon");
            }
            this.mParcelable = (Parcelable) this.mObj1;
            return;
        }
        if (i != 1) {
            if (i == 2) {
                this.mData = ((String) this.mObj1).getBytes(Charset.forName("UTF-16"));
                return;
            }
            if (i == 3) {
                this.mData = (byte[]) this.mObj1;
                return;
            } else if (i == 4) {
                this.mData = this.mObj1.toString().getBytes(Charset.forName("UTF-16"));
                return;
            } else if (i != 5) {
                return;
            }
        }
        if (!z) {
            this.mParcelable = (Parcelable) this.mObj1;
            return;
        }
        Bitmap bitmap = (Bitmap) this.mObj1;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, byteArrayOutputStream);
        this.mData = byteArrayOutputStream.toByteArray();
    }

    public IconCompat setTint(@ColorInt int i) {
        return setTintList(ColorStateList.valueOf(i));
    }

    public IconCompat setTintList(ColorStateList colorStateList) {
        this.mTintList = colorStateList;
        return this;
    }

    public IconCompat setTintMode(PorterDuff.Mode mode) {
        this.mTintMode = mode;
        return this;
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        int i = this.mType;
        if (i == -1) {
            bundle.putParcelable(EXTRA_OBJ, (Parcelable) this.mObj1);
        } else if (i == 1) {
            bundle.putParcelable(EXTRA_OBJ, (Bitmap) this.mObj1);
        } else if (i == 2) {
            bundle.putString(EXTRA_OBJ, (String) this.mObj1);
        } else if (i != 3) {
            if (i != 4) {
                if (i != 5) {
                    throw new IllegalArgumentException("Invalid icon");
                }
                bundle.putParcelable(EXTRA_OBJ, (Bitmap) this.mObj1);
            }
            bundle.putString(EXTRA_OBJ, (String) this.mObj1);
        } else {
            bundle.putByteArray(EXTRA_OBJ, (byte[]) this.mObj1);
        }
        bundle.putInt("type", this.mType);
        bundle.putInt(EXTRA_INT1, this.mInt1);
        bundle.putInt(EXTRA_INT2, this.mInt2);
        ColorStateList colorStateList = this.mTintList;
        if (colorStateList != null) {
            bundle.putParcelable(EXTRA_TINT_LIST, colorStateList);
        }
        PorterDuff.Mode mode = this.mTintMode;
        if (mode != DEFAULT_TINT_MODE) {
            bundle.putString(EXTRA_TINT_MODE, mode.name());
        }
        return bundle;
    }

    @RequiresApi(23)
    public Icon toIcon() {
        Icon iconCreateWithBitmap;
        int i = this.mType;
        if (i == -1) {
            return (Icon) this.mObj1;
        }
        if (i == 1) {
            iconCreateWithBitmap = Icon.createWithBitmap((Bitmap) this.mObj1);
        } else if (i == 2) {
            iconCreateWithBitmap = Icon.createWithResource((String) this.mObj1, this.mInt1);
        } else if (i == 3) {
            iconCreateWithBitmap = Icon.createWithData((byte[]) this.mObj1, this.mInt1, this.mInt2);
        } else if (i == 4) {
            iconCreateWithBitmap = Icon.createWithContentUri((String) this.mObj1);
        } else {
            if (i != 5) {
                throw new IllegalArgumentException("Unknown type");
            }
            iconCreateWithBitmap = Build.VERSION.SDK_INT >= 26 ? Icon.createWithAdaptiveBitmap((Bitmap) this.mObj1) : Icon.createWithBitmap(createLegacyIconFromAdaptiveIcon((Bitmap) this.mObj1, false));
        }
        ColorStateList colorStateList = this.mTintList;
        if (colorStateList != null) {
            iconCreateWithBitmap.setTintList(colorStateList);
        }
        PorterDuff.Mode mode = this.mTintMode;
        if (mode != DEFAULT_TINT_MODE) {
            iconCreateWithBitmap.setTintMode(mode);
        }
        return iconCreateWithBitmap;
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x007a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public String toString() {
        if (this.mType == -1) {
            return String.valueOf(this.mObj1);
        }
        StringBuilder sb = new StringBuilder("Icon(typ=");
        sb.append(typeToString(this.mType));
        int i = this.mType;
        if (i == 1) {
            sb.append(" size=");
            sb.append(((Bitmap) this.mObj1).getWidth());
            sb.append("x");
            sb.append(((Bitmap) this.mObj1).getHeight());
        } else if (i == 2) {
            sb.append(" pkg=");
            sb.append(getResPackage());
            sb.append(" id=");
            sb.append(String.format("0x%08x", Integer.valueOf(getResId())));
        } else if (i == 3) {
            sb.append(" len=");
            sb.append(this.mInt1);
            if (this.mInt2 != 0) {
                sb.append(" off=");
                sb.append(this.mInt2);
            }
        } else if (i == 4) {
            sb.append(" uri=");
            sb.append(this.mObj1);
        } else if (i == 5) {
        }
        if (this.mTintList != null) {
            sb.append(" tint=");
            sb.append(this.mTintList);
        }
        if (this.mTintMode != DEFAULT_TINT_MODE) {
            sb.append(" mode=");
            sb.append(this.mTintMode);
        }
        sb.append(")");
        return sb.toString();
    }

    private IconCompat(int i) {
        this.mType = i;
    }

    public static IconCompat createWithContentUri(Uri uri) {
        if (uri != null) {
            return createWithContentUri(uri.toString());
        }
        throw new IllegalArgumentException("Uri must not be null.");
    }

    @RequiresApi(23)
    public static int getType(@NonNull Icon icon) {
        if (BuildCompat.isAtLeastP()) {
            return icon.getType();
        }
        try {
            return ((Integer) icon.getClass().getMethod("getType", new Class[0]).invoke(icon, new Object[0])).intValue();
        } catch (IllegalAccessException e) {
            Log.e(TAG, "Unable to get icon type " + icon, e);
            return -1;
        } catch (NoSuchMethodException e2) {
            Log.e(TAG, "Unable to get icon type " + icon, e2);
            return -1;
        } catch (InvocationTargetException e3) {
            Log.e(TAG, "Unable to get icon type " + icon, e3);
            return -1;
        }
    }

    @RequiresApi(23)
    @Nullable
    public static Uri getUri(@NonNull Icon icon) {
        if (BuildCompat.isAtLeastP()) {
            return icon.getUri();
        }
        try {
            return (Uri) icon.getClass().getMethod("getUri", new Class[0]).invoke(icon, new Object[0]);
        } catch (IllegalAccessException e) {
            Log.e(TAG, "Unable to get icon uri", e);
            return null;
        } catch (NoSuchMethodException e2) {
            Log.e(TAG, "Unable to get icon uri", e2);
            return null;
        } catch (InvocationTargetException e3) {
            Log.e(TAG, "Unable to get icon uri", e3);
            return null;
        }
    }

    @RestrictTo({RestrictTo.Scope.LIBRARY})
    public static IconCompat createWithResource(String str, @DrawableRes int i) {
        if (str != null) {
            IconCompat iconCompat = new IconCompat(2);
            iconCompat.mInt1 = i;
            iconCompat.mObj1 = str;
            return iconCompat;
        }
        throw new IllegalArgumentException("Package must not be null.");
    }

    @DrawableRes
    @RequiresApi(23)
    @IdRes
    public static int getResId(@NonNull Icon icon) {
        if (BuildCompat.isAtLeastP()) {
            return icon.getResId();
        }
        try {
            return ((Integer) icon.getClass().getMethod("getResId", new Class[0]).invoke(icon, new Object[0])).intValue();
        } catch (IllegalAccessException e) {
            Log.e(TAG, "Unable to get icon resource", e);
            return 0;
        } catch (NoSuchMethodException e2) {
            Log.e(TAG, "Unable to get icon resource", e2);
            return 0;
        } catch (InvocationTargetException e3) {
            Log.e(TAG, "Unable to get icon resource", e3);
            return 0;
        }
    }

    @RequiresApi(23)
    @Nullable
    public static String getResPackage(@NonNull Icon icon) {
        if (BuildCompat.isAtLeastP()) {
            return icon.getResPackage();
        }
        try {
            return (String) icon.getClass().getMethod("getResPackage", new Class[0]).invoke(icon, new Object[0]);
        } catch (IllegalAccessException e) {
            Log.e(TAG, "Unable to get icon package", e);
            return null;
        } catch (NoSuchMethodException e2) {
            Log.e(TAG, "Unable to get icon package", e2);
            return null;
        } catch (InvocationTargetException e3) {
            Log.e(TAG, "Unable to get icon package", e3);
            return null;
        }
    }
}
