package cpdetector.p064io.parser;

import android.support.v4.view.InputDeviceCompat;
import antlr.ByteBuffer;
import antlr.CharBuffer;
import antlr.CharScanner;
import antlr.CharStreamException;
import antlr.CharStreamIOException;
import antlr.InputBuffer;
import antlr.LexerSharedInputState;
import antlr.NoViableAltForCharException;
import antlr.RecognitionException;
import antlr.Token;
import antlr.TokenStream;
import antlr.TokenStreamException;
import antlr.TokenStreamIOException;
import antlr.TokenStreamRecognitionException;
import antlr.collections.impl.BitSet;
import java.io.InputStream;
import java.io.Reader;
import java.util.Hashtable;
import tv.danmaku.ijk.media.player.IjkMediaMeta;

/* JADX INFO: loaded from: classes4.dex */
public class EncodingLexer extends CharScanner implements EncodingParserTokenTypes, TokenStream {
    public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());

    public EncodingLexer(InputBuffer inputBuffer) {
        this(new LexerSharedInputState(inputBuffer));
    }

    public EncodingLexer(LexerSharedInputState lexerSharedInputState) {
        super(lexerSharedInputState);
        this.caseSensitiveLiterals = true;
        setCaseSensitive(false);
        this.literals = new Hashtable();
    }

    public EncodingLexer(InputStream inputStream) {
        this(new ByteBuffer(inputStream));
    }

    public EncodingLexer(Reader reader) {
        this(new CharBuffer(reader));
    }

    public static final long[] mk_tokenSet_0() {
        long[] jArr = new long[InputDeviceCompat.SOURCE_GAMEPAD];
        jArr[0] = 4294976512L;
        jArr[1] = 35184372088832L;
        return jArr;
    }

    public final void mDIGIT(boolean z) {
        Token tokenMakeToken;
        int length = this.text.length();
        matchRange('0', '9');
        if (z) {
            tokenMakeToken = makeToken(10);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    public final void mIDENTIFIER(boolean z) {
        Token tokenMakeToken;
        int length = this.text.length();
        while (true) {
            mLETTER(false);
            while (true) {
                char cMo105LA = mo105LA(1);
                char c2 = '-';
                if (cMo105LA != '-') {
                    c2 = '.';
                    if (cMo105LA != '.') {
                        c2 = '_';
                        if (cMo105LA != '_') {
                            switch (cMo105LA) {
                                case '0':
                                case '1':
                                case '2':
                                case '3':
                                case '4':
                                case '5':
                                case '6':
                                case '7':
                                case '8':
                                case '9':
                                    mDIGIT(false);
                                    break;
                                default:
                                    switch (cMo105LA) {
                                        case 'a':
                                        case 'b':
                                        case 'c':
                                        case 'd':
                                        case 'e':
                                        case 'f':
                                        case 'g':
                                        case 'h':
                                        case 'i':
                                        case 'j':
                                        case 'k':
                                        case 'l':
                                        case 'm':
                                        case 'n':
                                        case 'o':
                                        case 'p':
                                        case 'q':
                                        case 'r':
                                        case 's':
                                        case 't':
                                        case 'u':
                                        case 'v':
                                        case 'w':
                                        case 'x':
                                        case 'y':
                                        case IjkMediaMeta.FF_PROFILE_H264_HIGH_422 /* 122 */:
                                            break;
                                        default:
                                            if (z) {
                                                tokenMakeToken = makeToken(6);
                                                tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
                                            } else {
                                                tokenMakeToken = null;
                                            }
                                            this._returnToken = tokenMakeToken;
                                            return;
                                    }
                                    break;
                            }
                        }
                    }
                }
                match(c2);
            }
        }
    }

    public final void mLETTER(boolean z) {
        Token tokenMakeToken;
        int length = this.text.length();
        matchRange('a', 'z');
        if (z) {
            tokenMakeToken = makeToken(11);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:177:0x0477. Please report as an issue. */
    /* JADX WARN: Failed to find 'out' block for switch in B:178:0x047a. Please report as an issue. */
    /* JADX WARN: Path cross not found for [B:172:0x045b, B:168:0x044f], limit reached: 262 */
    /* JADX WARN: Path cross not found for [B:172:0x045b, B:170:0x0455], limit reached: 262 */
    /* JADX WARN: Path cross not found for [B:172:0x045b, B:278:?], limit reached: 262 */
    /* JADX WARN: Path cross not found for [B:278:?, B:172:0x045b], limit reached: 262 */
    /* JADX WARN: Removed duplicated region for block: B:260:0x0513 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:272:0x0473 A[SYNTHETIC] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:117:0x02fe -> B:118:0x030c). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:189:0x04ab -> B:172:0x045b). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:191:0x04b1 -> B:172:0x045b). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:193:0x04b7 -> B:172:0x045b). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:196:0x04c1 -> B:172:0x045b). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:198:0x04c7 -> B:172:0x045b). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:200:0x04cd -> B:172:0x045b). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:208:0x0503 -> B:172:0x045b). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:210:0x0509 -> B:172:0x045b). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:212:0x050f -> B:172:0x045b). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:215:0x051a -> B:172:0x045b). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:217:0x0520 -> B:172:0x045b). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:219:0x0526 -> B:172:0x045b). Please report as a decompilation issue!!! */
    /*  JADX ERROR: JadxOverflowException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxOverflowException: Regions count limit reached
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:59)
        	at jadx.core.utils.ErrorsCounter.error(ErrorsCounter.java:31)
        	at jadx.core.dex.attributes.nodes.NotificationAttrNode.addError(NotificationAttrNode.java:19)
        */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void mMETA_CONTENT_TYPE(boolean r13) throws antlr.NoViableAltForCharException {
        /*
            Method dump skipped, instruction units count: 1976
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: cpdetector.p064io.parser.EncodingLexer.mMETA_CONTENT_TYPE(boolean):void");
    }

    public final void mNEWLINE(boolean z) throws NoViableAltForCharException {
        Token tokenMakeToken;
        int length = this.text.length();
        char cMo105LA = mo105LA(1);
        if (cMo105LA != '\n') {
            if (cMo105LA != '\r') {
                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
            }
            match('\r');
        }
        match('\n');
        newline();
        if (z) {
            tokenMakeToken = makeToken(8);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    public final void mSPACE(boolean z) {
        Token tokenMakeToken;
        int length = this.text.length();
        match(' ');
        if (z) {
            tokenMakeToken = makeToken(9);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    public final void mSPACING(boolean z) throws NoViableAltForCharException {
        Token tokenMakeToken;
        int length = this.text.length();
        char cMo105LA = mo105LA(1);
        if (cMo105LA == '\n' || cMo105LA == '\r') {
            mNEWLINE(false);
        } else {
            if (cMo105LA != ' ') {
                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
            }
            mSPACE(false);
        }
        if (z) {
            tokenMakeToken = makeToken(7);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    /* JADX WARN: Removed duplicated region for block: B:123:0x0366  */
    /* JADX WARN: Removed duplicated region for block: B:197:0x0553  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void mXML_ENCODING_DECL(boolean z) throws NoViableAltForCharException {
        Token tokenMakeToken;
        int length = this.text.length();
        int length2 = this.text.length();
        match("<?xml");
        this.text.setLength(length2);
        char cMo105LA = mo105LA(1);
        if (cMo105LA == '\n' || cMo105LA == '\r' || cMo105LA == ' ') {
            int length3 = this.text.length();
            mSPACING(false);
            this.text.setLength(length3);
        } else if (cMo105LA != 'e' && cMo105LA != 'v') {
            throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
        }
        char cMo105LA2 = mo105LA(1);
        if (cMo105LA2 != 'e') {
            if (cMo105LA2 != 'v') {
                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
            }
            int length4 = this.text.length();
            match("version");
            this.text.setLength(length4);
            char cMo105LA3 = mo105LA(1);
            if (cMo105LA3 == '\n' || cMo105LA3 == '\r' || cMo105LA3 == ' ') {
                int length5 = this.text.length();
                mSPACING(false);
                this.text.setLength(length5);
            } else if (cMo105LA3 != '=') {
                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
            }
            int length6 = this.text.length();
            match("=");
            this.text.setLength(length6);
            char cMo105LA4 = mo105LA(1);
            if (cMo105LA4 == '\n' || cMo105LA4 == '\r' || cMo105LA4 == ' ') {
                int length7 = this.text.length();
                mSPACING(false);
                this.text.setLength(length7);
            } else if (cMo105LA4 != '\"' && cMo105LA4 != '\'') {
                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
            }
            char cMo105LA5 = mo105LA(1);
            if (cMo105LA5 == '\"') {
                int length8 = this.text.length();
                match('\"');
                this.text.setLength(length8);
                char cMo105LA6 = mo105LA(1);
                if (cMo105LA6 == '\n' || cMo105LA6 == '\r' || cMo105LA6 == ' ') {
                    int length9 = this.text.length();
                    mSPACING(false);
                    this.text.setLength(length9);
                } else {
                    switch (cMo105LA6) {
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                            break;
                        default:
                            throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                    }
                }
                int length10 = this.text.length();
                mDIGIT(false);
                this.text.setLength(length10);
                char cMo105LA7 = mo105LA(1);
                if (cMo105LA7 == '\n' || cMo105LA7 == '\r' || cMo105LA7 == ' ') {
                    int length11 = this.text.length();
                    mSPACING(false);
                    this.text.setLength(length11);
                } else if (cMo105LA7 != '.') {
                    throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                }
                int length12 = this.text.length();
                match('.');
                this.text.setLength(length12);
                char cMo105LA8 = mo105LA(1);
                if (cMo105LA8 == '\n' || cMo105LA8 == '\r' || cMo105LA8 == ' ') {
                    int length13 = this.text.length();
                    mSPACING(false);
                    this.text.setLength(length13);
                } else {
                    switch (cMo105LA8) {
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                            break;
                        default:
                            throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                    }
                }
                int length14 = this.text.length();
                mDIGIT(false);
                this.text.setLength(length14);
                char cMo105LA9 = mo105LA(1);
                if (cMo105LA9 == '\n' || cMo105LA9 == '\r' || cMo105LA9 == ' ') {
                    int length15 = this.text.length();
                    mSPACING(false);
                    this.text.setLength(length15);
                } else if (cMo105LA9 != '\"') {
                    throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                }
                int length16 = this.text.length();
                match('\"');
                this.text.setLength(length16);
                char cMo105LA10 = mo105LA(1);
                if (cMo105LA10 != '\n' && cMo105LA10 != '\r' && cMo105LA10 != ' ') {
                    if (cMo105LA10 != 'e') {
                        throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                    }
                }
            } else {
                if (cMo105LA5 != '\'') {
                    throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                }
                int length17 = this.text.length();
                match("'");
                this.text.setLength(length17);
                char cMo105LA11 = mo105LA(1);
                if (cMo105LA11 == '\n' || cMo105LA11 == '\r' || cMo105LA11 == ' ') {
                    int length18 = this.text.length();
                    mSPACING(false);
                    this.text.setLength(length18);
                } else {
                    switch (cMo105LA11) {
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                            break;
                        default:
                            throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                    }
                }
                int length19 = this.text.length();
                mDIGIT(false);
                this.text.setLength(length19);
                char cMo105LA12 = mo105LA(1);
                if (cMo105LA12 == '\n' || cMo105LA12 == '\r' || cMo105LA12 == ' ') {
                    int length20 = this.text.length();
                    mSPACING(false);
                    this.text.setLength(length20);
                } else if (cMo105LA12 != '.') {
                    throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                }
                int length21 = this.text.length();
                match('.');
                this.text.setLength(length21);
                char cMo105LA13 = mo105LA(1);
                if (cMo105LA13 == '\n' || cMo105LA13 == '\r' || cMo105LA13 == ' ') {
                    int length22 = this.text.length();
                    mSPACING(false);
                    this.text.setLength(length22);
                } else {
                    switch (cMo105LA13) {
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                            break;
                        default:
                            throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                    }
                }
                int length23 = this.text.length();
                mDIGIT(false);
                this.text.setLength(length23);
                char cMo105LA14 = mo105LA(1);
                if (cMo105LA14 == '\n' || cMo105LA14 == '\r' || cMo105LA14 == ' ') {
                    int length24 = this.text.length();
                    mSPACING(false);
                    this.text.setLength(length24);
                } else if (cMo105LA14 != '\'') {
                    throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                }
                int length25 = this.text.length();
                match("'");
                this.text.setLength(length25);
                char cMo105LA15 = mo105LA(1);
                if (cMo105LA15 == '\n' || cMo105LA15 == '\r' || cMo105LA15 == ' ') {
                    int length26 = this.text.length();
                    mSPACING(false);
                    this.text.setLength(length26);
                } else if (cMo105LA15 != 'e') {
                    throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                }
            }
        }
        int length27 = this.text.length();
        match("encoding");
        this.text.setLength(length27);
        char cMo105LA16 = mo105LA(1);
        if (cMo105LA16 == '\n' || cMo105LA16 == '\r' || cMo105LA16 == ' ') {
            int length28 = this.text.length();
            mSPACING(false);
            this.text.setLength(length28);
        } else if (cMo105LA16 != '=') {
            throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
        }
        int length29 = this.text.length();
        match("=");
        this.text.setLength(length29);
        char cMo105LA17 = mo105LA(1);
        if (cMo105LA17 == '\n' || cMo105LA17 == '\r' || cMo105LA17 == ' ') {
            int length30 = this.text.length();
            mSPACING(false);
            this.text.setLength(length30);
        } else if (cMo105LA17 != '\"' && cMo105LA17 != '\'') {
            throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
        }
        char cMo105LA18 = mo105LA(1);
        if (cMo105LA18 == '\"') {
            int length31 = this.text.length();
            match('\"');
            this.text.setLength(length31);
            char cMo105LA19 = mo105LA(1);
            if (cMo105LA19 == '\n' || cMo105LA19 == '\r' || cMo105LA19 == ' ') {
                int length32 = this.text.length();
                mSPACING(false);
                this.text.setLength(length32);
            } else {
                switch (cMo105LA19) {
                    case 'a':
                    case 'b':
                    case 'c':
                    case 'd':
                    case 'e':
                    case 'f':
                    case 'g':
                    case 'h':
                    case 'i':
                    case 'j':
                    case 'k':
                    case 'l':
                    case 'm':
                    case 'n':
                    case 'o':
                    case 'p':
                    case 'q':
                    case 'r':
                    case 's':
                    case 't':
                    case 'u':
                    case 'v':
                    case 'w':
                    case 'x':
                    case 'y':
                    case IjkMediaMeta.FF_PROFILE_H264_HIGH_422 /* 122 */:
                        break;
                    default:
                        throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                }
            }
            mIDENTIFIER(false);
            char cMo105LA20 = mo105LA(1);
            if (cMo105LA20 == '\n' || cMo105LA20 == '\r' || cMo105LA20 == ' ') {
                int length33 = this.text.length();
                mSPACING(false);
                this.text.setLength(length33);
            } else if (cMo105LA20 != '\"') {
                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
            }
            int length34 = this.text.length();
            match('\"');
            this.text.setLength(length34);
            if (mo105LA(1) == '\n' || mo105LA(1) == '\r' || mo105LA(1) == ' ') {
            }
        } else {
            if (cMo105LA18 != '\'') {
                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
            }
            int length35 = this.text.length();
            match("'");
            this.text.setLength(length35);
            char cMo105LA21 = mo105LA(1);
            if (cMo105LA21 == '\n' || cMo105LA21 == '\r' || cMo105LA21 == ' ') {
                int length36 = this.text.length();
                mSPACING(false);
                this.text.setLength(length36);
            } else {
                switch (cMo105LA21) {
                    case 'a':
                    case 'b':
                    case 'c':
                    case 'd':
                    case 'e':
                    case 'f':
                    case 'g':
                    case 'h':
                    case 'i':
                    case 'j':
                    case 'k':
                    case 'l':
                    case 'm':
                    case 'n':
                    case 'o':
                    case 'p':
                    case 'q':
                    case 'r':
                    case 's':
                    case 't':
                    case 'u':
                    case 'v':
                    case 'w':
                    case 'x':
                    case 'y':
                    case IjkMediaMeta.FF_PROFILE_H264_HIGH_422 /* 122 */:
                        break;
                    default:
                        throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                }
            }
            mIDENTIFIER(false);
            char cMo105LA22 = mo105LA(1);
            if (cMo105LA22 == '\n' || cMo105LA22 == '\r' || cMo105LA22 == ' ') {
                int length37 = this.text.length();
                mSPACING(false);
                this.text.setLength(length37);
            } else if (cMo105LA22 != '\'') {
                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
            }
            int length38 = this.text.length();
            match("'");
            this.text.setLength(length38);
            if (mo105LA(1) == '\n' || mo105LA(1) == '\r' || mo105LA(1) == ' ') {
                int length39 = this.text.length();
                mSPACING(false);
                this.text.setLength(length39);
            }
        }
        if (z) {
            tokenMakeToken = makeToken(5);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r5v1, types: [antlr.CharScanner, cpdetector.io.parser.EncodingLexer] */
    /* JADX WARN: Type inference failed for: r5v2, types: [antlr.CharScanner] */
    /* JADX WARN: Type inference failed for: r5v8 */
    /* JADX WARN: Type inference failed for: r5v9, types: [antlr.Token] */
    @Override // antlr.TokenStream
    public Token nextToken() throws TokenStreamException {
        while (true) {
            this.setCommitToPath(false);
            this.resetText();
            try {
                try {
                } catch (RecognitionException e) {
                    if (this.getCommitToPath()) {
                        throw new TokenStreamRecognitionException(e);
                    }
                    this.consume();
                }
                if (this.mo105LA(1) == '<' && _tokenSet_0.member(this.mo105LA(2))) {
                    this.mMETA_CONTENT_TYPE(true);
                } else if (this.mo105LA(1) == '<' && this.mo105LA(2) == '?') {
                    this.mXML_ENCODING_DECL(true);
                } else if (this.mo105LA(1) == 65535) {
                    this.uponEOF();
                    this._returnToken = this.makeToken(1);
                } else {
                    this.consume();
                }
                if (this._returnToken != null) {
                    this._returnToken.setType(this.testLiteralsTable(this._returnToken.getType()));
                    this = this._returnToken;
                    return this;
                }
            } catch (CharStreamException e2) {
                if (e2 instanceof CharStreamIOException) {
                    throw new TokenStreamIOException(((CharStreamIOException) e2).f303io);
                }
                throw new TokenStreamException(e2.getMessage());
            }
        }
    }
}
