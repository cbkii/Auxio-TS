package com.p060tw.music.p061a;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.eckom.xtlibrary.p020b.p037f.p039b.C0579f;
import com.eckom.xtlibrary.p020b.p037f.p039b.C0580g;
import com.eckom.xtlibrary.p020b.p037f.p043f.C0654s;
import com.eckom.xtlibrary.p020b.p052i.C0678h;
import com.eckom.xtlibrary.p020b.p052i.C0681k;
import com.p060tw.music.R;
import com.p060tw.music.p062b.C0769a;
import com.p060tw.music.utils.C0792a;

/* compiled from: MusicAdapter.java */
/* renamed from: com.tw.music.a.c */
/* loaded from: classes3.dex */
public class C0767c extends BaseAdapter {

    /* renamed from: Pa */
    private C0769a f1085Pa;
    private Context mContext;
    private b mOnItemClickListener;
    private C0580g mRecord;

    /* renamed from: xf */
    private a f1086xf;

    /* renamed from: yf */
    private int f1087yf;

    /* renamed from: zf */
    private Drawable f1088zf;

    /* compiled from: MusicAdapter.java */
    /* renamed from: com.tw.music.a.c$a */
    public interface a {
        /* renamed from: a */
        void mo1366a(C0579f c0579f, boolean z);
    }

    /* compiled from: MusicAdapter.java */
    /* renamed from: com.tw.music.a.c$b */
    public interface b {
        /* renamed from: U */
        void mo1367U(int i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: MusicAdapter.java */
    /* renamed from: com.tw.music.a.c$c */
    class c {
        ImageView file_ic;
        View itemView;
        ImageView iv_isPlaying;

        /* renamed from: sm */
        TextView f1089sm;

        /* renamed from: tm */
        ImageView f1090tm;

        /* renamed from: um */
        TextView f1091um;

        /* renamed from: vm */
        View f1092vm;

        /* renamed from: wm */
        View f1093wm;

        /* renamed from: xm */
        TextView f1094xm;

        /* renamed from: ym */
        ImageView f1095ym;

        /* renamed from: zm */
        ImageView f1096zm;

        private c() {
        }

        /* synthetic */ c(C0767c c0767c, ViewOnClickListenerC0765a viewOnClickListenerC0765a) {
            this();
        }
    }

    public C0767c(Context context) {
        this.mContext = context;
    }

    /* renamed from: Xa */
    public void m1362Xa() {
        notifyDataSetChanged();
    }

    @Override // android.widget.Adapter
    public int getCount() {
        try {
            if (this.mRecord.mIndex == 4) {
                return this.mRecord.f544jk.length;
            }
            if (this.mRecord == null) {
                return 0;
            }
            return this.mRecord.f543ik == 0 ? this.mRecord.f545kk : this.mRecord.f545kk + 1;
        } catch (Exception e) {
            C0792a.m1513e("An exception occurred at getMusicCount : " + e.getMessage());
            return 0;
        }
    }

    @Override // android.widget.Adapter
    public Object getItem(int i) {
        return Integer.valueOf(i);
    }

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    @Override // android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = m1357a(viewGroup);
        }
        m1359a(view, i, viewGroup);
        return view;
    }

    /* renamed from: a */
    public void m1363a(C0580g c0580g, C0769a c0769a) {
        this.mRecord = c0580g;
        this.f1085Pa = c0769a;
        notifyDataSetChanged();
    }

    /* renamed from: a */
    public void m1365a(b bVar) {
        this.mOnItemClickListener = bVar;
    }

    /* renamed from: a */
    public void m1364a(a aVar) {
        this.f1086xf = aVar;
    }

    /* renamed from: a */
    private View m1357a(ViewGroup viewGroup) {
        c cVar = new c(this, null);
        cVar.itemView = LayoutInflater.from(this.mContext).inflate(R.layout.music_list_recycle_item, viewGroup, false);
        cVar.f1089sm = (TextView) cVar.itemView.findViewById(R.id.tv_filename);
        cVar.f1090tm = (ImageView) cVar.itemView.findViewById(R.id.iv_indicator);
        cVar.f1091um = (TextView) cVar.itemView.findViewById(R.id.tv_song);
        cVar.f1092vm = cVar.itemView.findViewById(R.id.item_folder);
        cVar.f1093wm = cVar.itemView.findViewById(R.id.item_music_info);
        cVar.f1094xm = (TextView) cVar.itemView.findViewById(R.id.tv_index);
        cVar.iv_isPlaying = (ImageView) cVar.itemView.findViewById(R.id.iv_isPlaying);
        cVar.f1095ym = (ImageView) cVar.itemView.findViewById(R.id.btn_collection);
        cVar.file_ic = (ImageView) cVar.itemView.findViewById(R.id.file_ic);
        cVar.f1096zm = (ImageView) cVar.itemView.findViewById(R.id.music_icon);
        cVar.itemView.setTag(cVar);
        AnimationDrawable animationDrawable = (AnimationDrawable) cVar.iv_isPlaying.getDrawable();
        if (animationDrawable != null) {
            animationDrawable.start();
        }
        return cVar.itemView;
    }

    /* renamed from: a */
    private void m1359a(View view, int i, ViewGroup viewGroup) {
        String str;
        String str2;
        c cVar = (c) view.getTag();
        try {
            Drawable m964d = C0678h.m964d(C0681k.get().m973Kc(), "selector_item_bg");
            if (m964d != null) {
                cVar.f1092vm.setBackground(m964d);
                cVar.f1093wm.setBackground(m964d);
            }
            Drawable m964d2 = C0678h.m964d(C0681k.get().m973Kc(), "ic_wenjianjia");
            if (m964d2 != null) {
                cVar.file_ic.setImageDrawable(m964d2);
            }
            Drawable m964d3 = C0678h.m964d(C0681k.get().m973Kc(), "selector_btn_icon");
            if (m964d3 != null) {
                cVar.f1096zm.setImageDrawable(m964d3);
            }
            Drawable m964d4 = C0678h.m964d(C0681k.get().m973Kc(), "lev_play_now");
            if (m964d4 != null) {
                cVar.iv_isPlaying.setImageDrawable(m964d4);
                AnimationDrawable animationDrawable = (AnimationDrawable) cVar.iv_isPlaying.getDrawable();
                if (animationDrawable != null) {
                    animationDrawable.start();
                }
            }
            Drawable m964d5 = C0678h.m964d(C0681k.get().m973Kc(), "selector_btn_collect_list");
            if (m964d5 != null) {
                cVar.f1095ym.setImageDrawable(m964d5);
            }
        } catch (Exception unused) {
        }
        Drawable drawable = this.f1088zf;
        if (drawable != null) {
            cVar.f1092vm.setBackground(drawable);
            cVar.f1093wm.setBackground(this.f1088zf);
        }
        cVar.f1095ym.setVisibility(0);
        C0580g c0580g = this.mRecord;
        int i2 = c0580g.mIndex;
        if (i2 == 4) {
            C0579f[] c0579fArr = c0580g.f544jk;
            str2 = c0579fArr[i].mName;
            str = c0579fArr[i].mPath;
            cVar.f1092vm.setVisibility(8);
            cVar.f1093wm.setVisibility(0);
            cVar.f1094xm.setText(String.valueOf(i + 1));
            cVar.f1091um.setText(str2);
            C0769a c0769a = this.f1085Pa;
            if (c0769a != null) {
                if (str.equals(c0769a.m1373gd())) {
                    cVar.iv_isPlaying.setVisibility(0);
                    cVar.f1096zm.setImageLevel(1);
                    cVar.f1094xm.setTextColor(this.mContext.getResources().getColor(R.color.list_text_red3));
                    cVar.f1091um.setTextColor(this.mContext.getResources().getColor(R.color.list_text_red3));
                } else {
                    cVar.iv_isPlaying.setVisibility(4);
                    cVar.f1096zm.setImageLevel(0);
                    cVar.f1094xm.setTextColor(this.mContext.getResources().getColor(R.color.text_white));
                    cVar.f1091um.setTextColor(this.mContext.getResources().getColor(R.color.text_white));
                }
            }
            cVar.f1095ym.getDrawable().setLevel(this.mRecord.f544jk[i].f539ek ? 1 : 0);
        } else {
            if (i2 == 0) {
                cVar.f1095ym.setVisibility(8);
            }
            C0580g c0580g2 = this.mRecord;
            if (c0580g2.f543ik == 0) {
                C0579f[] c0579fArr2 = c0580g2.f544jk;
                str2 = c0579fArr2[i].mName;
                str = c0579fArr2[i].mPath;
            } else if (i == 0) {
                str2 = c0580g2.mName;
                str = null;
            } else {
                C0579f[] c0579fArr3 = c0580g2.f544jk;
                int i3 = i - 1;
                String str3 = c0579fArr3[i3].mName;
                str = c0579fArr3[i3].mPath;
                cVar.f1095ym.getDrawable().setLevel(this.mRecord.f544jk[i3].f539ek ? 1 : 0);
                str2 = str3;
            }
            if (this.mRecord.f543ik != 0 && i == 0) {
                cVar.f1089sm.setText(str2);
                cVar.f1090tm.getDrawable().setLevel(1);
                cVar.f1092vm.setVisibility(0);
                cVar.f1093wm.setVisibility(8);
                cVar.f1092vm.getBackground().setLevel(1);
                if (this.mRecord.f544jk != null) {
                    cVar.f1095ym.getDrawable().setLevel(this.mRecord.f544jk[i].f539ek ? 1 : 0);
                }
            } else {
                C0580g c0580g3 = this.mRecord;
                if (c0580g3.f543ik != 1 && c0580g3.mIndex != 0) {
                    cVar.f1092vm.setVisibility(0);
                    cVar.f1093wm.setVisibility(8);
                    cVar.f1089sm.setText(str2);
                    cVar.f1090tm.getDrawable().setLevel(0);
                    if (this.f1085Pa != null) {
                        if (str.equals(C0654s.f703Cd)) {
                            cVar.f1092vm.getBackground().setLevel(1);
                            cVar.f1089sm.setSelected(true);
                            cVar.f1091um.setSelected(true);
                        } else {
                            cVar.f1092vm.getBackground().setLevel(0);
                            cVar.f1089sm.setSelected(false);
                            cVar.f1091um.setSelected(false);
                        }
                    }
                } else {
                    cVar.f1092vm.setVisibility(8);
                    cVar.f1093wm.setVisibility(0);
                    cVar.f1093wm.getBackground().setLevel(0);
                    cVar.f1091um.setText(str2);
                    if (this.f1085Pa != null) {
                        if (str.equals(C0654s.f702Bd)) {
                            cVar.f1093wm.getBackground().setLevel(1);
                            cVar.f1089sm.setSelected(true);
                            cVar.f1091um.setSelected(true);
                            cVar.iv_isPlaying.setVisibility(0);
                            cVar.f1096zm.setImageLevel(1);
                            try {
                                this.f1087yf = C0678h.m962c(C0681k.get().m973Kc(), "color_song_selector");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            int i4 = this.f1087yf;
                            if (i4 != 0) {
                                cVar.f1094xm.setTextColor(i4);
                                cVar.f1091um.setTextColor(this.f1087yf);
                            } else {
                                cVar.f1094xm.setTextColor(this.mContext.getResources().getColor(R.color.list_text_red3));
                                cVar.f1091um.setTextColor(this.mContext.getResources().getColor(R.color.list_text_red3));
                            }
                        } else {
                            cVar.f1089sm.setSelected(false);
                            cVar.f1091um.setSelected(false);
                            cVar.iv_isPlaying.setVisibility(4);
                            cVar.f1096zm.setImageLevel(0);
                            try {
                                this.f1087yf = C0678h.m962c(C0681k.get().m973Kc(), "color_song_normal");
                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }
                            int i5 = this.f1087yf;
                            if (i5 != 0) {
                                cVar.f1094xm.setTextColor(i5);
                                cVar.f1091um.setTextColor(this.f1087yf);
                            } else {
                                cVar.f1094xm.setTextColor(this.mContext.getResources().getColor(R.color.text_white));
                                cVar.f1091um.setTextColor(this.mContext.getResources().getColor(R.color.text_white));
                            }
                        }
                        if (this.mRecord.f543ik != 0 && i != 0) {
                            cVar.f1094xm.setText(String.valueOf(i));
                        } else {
                            cVar.f1094xm.setText(String.valueOf(i + 1));
                        }
                    }
                }
            }
        }
        cVar.f1095ym.setOnClickListener(new ViewOnClickListenerC0765a(this, i, str2, str, cVar));
        cVar.itemView.setOnClickListener(new ViewOnClickListenerC0766b(this, i));
    }
}
