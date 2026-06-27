package com.p060tw.music;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;

/* JADX INFO: loaded from: classes3.dex */
public class AudioPreview extends Activity implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

    /* JADX INFO: renamed from: Ec */
    private TextView f1007Ec;

    /* JADX INFO: renamed from: Fc */
    private TextView f1008Fc;

    /* JADX INFO: renamed from: Gc */
    private TextView f1009Gc;

    /* JADX INFO: renamed from: Hc */
    private SeekBar f1010Hc;

    /* JADX INFO: renamed from: Ic */
    private Handler f1011Ic;

    /* JADX INFO: renamed from: Kc */
    private boolean f1013Kc;
    private AudioManager mAudioManager;
    private int mDuration;
    private C0761a mPlayer;
    private Uri mUri;

    /* JADX INFO: renamed from: Jc */
    private boolean f1012Jc = false;
    private long mMediaId = -1;
    private AudioManager.OnAudioFocusChangeListener mAudioFocusListener = new C0768b(this);

    /* JADX INFO: renamed from: Lc */
    private SeekBar.OnSeekBarChangeListener f1014Lc = new C0770c(this);

    /* JADX INFO: renamed from: com.tw.music.AudioPreview$b */
    class RunnableC0762b implements Runnable {
        RunnableC0762b() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (AudioPreview.this.mPlayer != null && !AudioPreview.this.f1012Jc && AudioPreview.this.mDuration != 0) {
                int currentPosition = AudioPreview.this.mPlayer.getCurrentPosition() / AudioPreview.this.mDuration;
                AudioPreview.this.f1010Hc.setProgress(AudioPreview.this.mPlayer.getCurrentPosition());
            }
            AudioPreview.this.f1011Ic.removeCallbacksAndMessages(null);
            AudioPreview.this.f1011Ic.postDelayed(AudioPreview.this.new RunnableC0762b(), 200L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void start() {
        this.mAudioManager.requestAudioFocus(this.mAudioFocusListener, 3, 1);
        this.mPlayer.start();
        this.f1011Ic.postDelayed(new RunnableC0762b(), 200L);
    }

    private void stopPlayback() {
        Handler handler = this.f1011Ic;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        C0761a c0761a = this.mPlayer;
        if (c0761a != null) {
            c0761a.release();
            this.mPlayer = null;
            this.mAudioManager.abandonAudioFocus(this.mAudioFocusListener);
        }
    }

    /* JADX INFO: renamed from: ye */
    private void m1323ye() {
        ((ProgressBar) findViewById(R.id.spinner)).setVisibility(8);
        this.mDuration = this.mPlayer.getDuration();
        int i = this.mDuration;
        if (i != 0) {
            this.f1010Hc.setMax(i);
            this.f1010Hc.setVisibility(0);
        }
        this.f1010Hc.setOnSeekBarChangeListener(this.f1014Lc);
        this.f1009Gc.setVisibility(8);
        findViewById(R.id.titleandbuttons).setVisibility(0);
        this.mAudioManager.requestAudioFocus(this.mAudioFocusListener, 3, 1);
        this.f1011Ic.postDelayed(new RunnableC0762b(), 200L);
        m1324ze();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: ze */
    public void m1324ze() {
        ImageButton imageButton = (ImageButton) findViewById(R.id.playpause);
        if (imageButton != null) {
            if (this.mPlayer.isPlaying()) {
                imageButton.setImageResource(R.drawable.btn_playback_ic_pause_small);
            } else {
                imageButton.setImageResource(R.drawable.btn_playback_ic_play_small);
                this.f1011Ic.removeCallbacksAndMessages(null);
            }
        }
    }

    /* JADX INFO: renamed from: Oa */
    public void m1325Oa() {
        if (TextUtils.isEmpty(this.f1007Ec.getText())) {
            this.f1007Ec.setText(this.mUri.getLastPathSegment());
        }
        if (TextUtils.isEmpty(this.f1008Fc.getText())) {
            this.f1008Fc.setVisibility(8);
        } else {
            this.f1008Fc.setVisibility(0);
        }
    }

    @Override // android.media.MediaPlayer.OnCompletionListener
    public void onCompletion(MediaPlayer mediaPlayer) {
        this.f1010Hc.setProgress(this.mDuration);
        m1324ze();
    }

    @Override // android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }
        this.mUri = intent.getData();
        Uri uri = this.mUri;
        if (uri == null) {
            finish();
            return;
        }
        String scheme = uri.getScheme();
        setVolumeControlStream(3);
        requestWindowFeature(1);
        setContentView(R.layout.audiopreview);
        this.f1007Ec = (TextView) findViewById(R.id.line1);
        this.f1008Fc = (TextView) findViewById(R.id.line2);
        this.f1009Gc = (TextView) findViewById(R.id.loading);
        if (scheme.equals("http")) {
            this.f1009Gc.setText(getString(R.string.streamloadingtext, new Object[]{this.mUri.getHost()}));
        } else {
            this.f1009Gc.setVisibility(8);
        }
        this.f1010Hc = (SeekBar) findViewById(R.id.progress);
        this.f1011Ic = new Handler();
        this.mAudioManager = (AudioManager) getSystemService("audio");
        C0761a c0761a = (C0761a) getLastNonConfigurationInstance();
        if (c0761a == null) {
            this.mPlayer = new C0761a(null);
            this.mPlayer.m1328l(this);
            try {
                this.mPlayer.m1327a(this.mUri);
            } catch (Exception e) {
                Log.d("AudioPreview", "Failed to open file: " + e);
                Toast.makeText(this, R.string.playback_failed, 0).show();
                finish();
                return;
            }
        } else {
            this.mPlayer = c0761a;
            this.mPlayer.m1328l(this);
            if (this.mPlayer.m1326Qa()) {
                m1323ye();
            }
        }
        C0764a c0764a = new C0764a(this, getContentResolver());
        if (scheme.equals("content")) {
            if (this.mUri.getAuthority() == "media") {
                c0764a.startQuery(0, null, this.mUri, new String[]{"title", "artist"}, null, null, null);
                return;
            } else {
                c0764a.startQuery(0, null, this.mUri, null, null, null, null);
                return;
            }
        }
        if (scheme.equals("file")) {
            c0764a.startQuery(0, null, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, new String[]{"_id", "title", "artist"}, "_data=?", new String[]{this.mUri.getPath()}, null);
        } else if (this.mPlayer.m1326Qa()) {
            m1325Oa();
        }
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, 1, 0, "open in music");
        return true;
    }

    @Override // android.app.Activity
    public void onDestroy() {
        stopPlayback();
        super.onDestroy();
    }

    @Override // android.media.MediaPlayer.OnErrorListener
    public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
        Toast.makeText(this, R.string.playback_failed, 0).show();
        finish();
        return true;
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i != 4) {
            if (i != 79) {
                if (i == 126) {
                    start();
                    m1324ze();
                    return true;
                }
                if (i == 127) {
                    if (this.mPlayer.isPlaying()) {
                        this.mPlayer.pause();
                    }
                    m1324ze();
                    return true;
                }
                switch (i) {
                    case 85:
                        break;
                    case 86:
                        break;
                    case 87:
                    case 88:
                    case 89:
                    case 90:
                        return true;
                    default:
                        return super.onKeyDown(i, keyEvent);
                }
            }
            if (this.mPlayer.isPlaying()) {
                this.mPlayer.pause();
            } else {
                start();
            }
            m1324ze();
            return true;
        }
        stopPlayback();
        finish();
        return true;
    }

    @Override // android.app.Activity
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItemFindItem = menu.findItem(1);
        if (this.mMediaId >= 0) {
            menuItemFindItem.setVisible(true);
            return true;
        }
        menuItemFindItem.setVisible(false);
        return false;
    }

    @Override // android.media.MediaPlayer.OnPreparedListener
    public void onPrepared(MediaPlayer mediaPlayer) {
        if (isFinishing()) {
            return;
        }
        this.mPlayer = (C0761a) mediaPlayer;
        m1325Oa();
        this.mPlayer.start();
        m1323ye();
    }

    @Override // android.app.Activity
    public Object onRetainNonConfigurationInstance() {
        C0761a c0761a = this.mPlayer;
        this.mPlayer = null;
        return c0761a;
    }

    @Override // android.app.Activity
    public void onUserLeaveHint() {
        stopPlayback();
        finish();
        super.onUserLeaveHint();
    }

    public void playPauseClicked(View view) {
        if (this.mPlayer.isPlaying()) {
            this.mPlayer.pause();
        } else {
            start();
        }
        m1324ze();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: com.tw.music.AudioPreview$a */
    static class C0761a extends MediaPlayer implements MediaPlayer.OnPreparedListener {

        /* JADX INFO: renamed from: Rc */
        boolean f1015Rc;
        AudioPreview mActivity;

        private C0761a() {
            this.f1015Rc = false;
        }

        /* JADX INFO: renamed from: Qa */
        boolean m1326Qa() {
            return this.f1015Rc;
        }

        /* JADX INFO: renamed from: a */
        public void m1327a(Uri uri) throws IOException {
            setDataSource(this.mActivity, uri);
            prepareAsync();
        }

        /* JADX INFO: renamed from: l */
        public void m1328l(AudioPreview audioPreview) {
            this.mActivity = audioPreview;
            setOnPreparedListener(this);
            setOnErrorListener(this.mActivity);
            setOnCompletionListener(this.mActivity);
        }

        @Override // android.media.MediaPlayer.OnPreparedListener
        public void onPrepared(MediaPlayer mediaPlayer) {
            this.f1015Rc = true;
            this.mActivity.onPrepared(mediaPlayer);
        }

        /* synthetic */ C0761a(C0764a c0764a) {
            this();
        }
    }
}
