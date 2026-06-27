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

/* loaded from: classes4.dex */
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
        Token token;
        int length = this.text.length();
        matchRange('0', '9');
        if (z) {
            token = makeToken(10);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    /* JADX WARN: Code restructure failed: missing block: B:18:0x001e, code lost:
    
        switch(r2) {
            case 97: goto L20;
            case 98: goto L20;
            case 99: goto L20;
            case 100: goto L20;
            case 101: goto L20;
            case 102: goto L20;
            case 103: goto L20;
            case 104: goto L20;
            case 105: goto L20;
            case 106: goto L20;
            case 107: goto L20;
            case 108: goto L20;
            case 109: goto L20;
            case 110: goto L20;
            case 111: goto L20;
            case 112: goto L20;
            case 113: goto L20;
            case 114: goto L20;
            case 115: goto L20;
            case 116: goto L20;
            case 117: goto L20;
            case 118: goto L20;
            case 119: goto L20;
            case 120: goto L20;
            case 121: goto L20;
            case 122: goto L20;
            default: goto L19;
        };
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x0021, code lost:
    
        if (r5 == false) goto L15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0023, code lost:
    
        r5 = makeToken(6);
        r5.setText(new java.lang.String(r4.text.getBuffer(), r0, r4.text.length() - r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0043, code lost:
    
        r4._returnToken = r5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0045, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x0042, code lost:
    
        r5 = null;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void mIDENTIFIER(boolean z) {
        int length = this.text.length();
        while (true) {
            mLETTER(false);
            while (true) {
                char mo105LA = mo105LA(1);
                char c2 = '-';
                if (mo105LA != '-') {
                    c2 = '.';
                    if (mo105LA != '.') {
                        c2 = '_';
                        if (mo105LA != '_') {
                            switch (mo105LA) {
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
                            }
                        }
                    }
                }
                match(c2);
            }
        }
    }

    public final void mLETTER(boolean z) {
        Token token;
        int length = this.text.length();
        matchRange('a', 'z');
        if (z) {
            token = makeToken(11);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    /*  JADX ERROR: JadxOverflowException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxOverflowException: Regions count limit reached
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:59)
        	at jadx.core.utils.ErrorsCounter.error(ErrorsCounter.java:31)
        	at jadx.core.dex.attributes.nodes.NotificationAttrNode.addError(NotificationAttrNode.java:19)
        */
    /* JADX WARN: Failed to find 'out' block for switch in B:189:0x0477. Please report as an issue. */
    /* JADX WARN: Failed to find 'out' block for switch in B:202:0x047a. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:162:0x0513 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:174:0x0473 A[SYNTHETIC] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:118:0x04c1 -> B:141:0x045b). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:120:0x04c7 -> B:141:0x045b). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:122:0x04cd -> B:141:0x045b). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:124:0x04ab -> B:141:0x045b). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:126:0x04b1 -> B:141:0x045b). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:128:0x04b7 -> B:141:0x045b). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:130:0x0503 -> B:141:0x045b). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:132:0x0509 -> B:141:0x045b). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:134:0x050f -> B:141:0x045b). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:136:0x051a -> B:141:0x045b). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:138:0x0520 -> B:141:0x045b). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:140:0x0526 -> B:141:0x045b). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:146:0x02fe -> B:75:0x030c). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void mMETA_CONTENT_TYPE(boolean r13) {
        /*
            Method dump skipped, instructions count: 1976
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: cpdetector.p064io.parser.EncodingLexer.mMETA_CONTENT_TYPE(boolean):void");
    }

    public final void mNEWLINE(boolean z) {
        Token token;
        int length = this.text.length();
        char mo105LA = mo105LA(1);
        if (mo105LA != '\n') {
            if (mo105LA != '\r') {
                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
            }
            match('\r');
        }
        match('\n');
        newline();
        if (z) {
            token = makeToken(8);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    public final void mSPACE(boolean z) {
        Token token;
        int length = this.text.length();
        match(' ');
        if (z) {
            token = makeToken(9);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    public final void mSPACING(boolean z) {
        Token token;
        int length = this.text.length();
        char mo105LA = mo105LA(1);
        if (mo105LA == '\n' || mo105LA == '\r') {
            mNEWLINE(false);
        } else {
            if (mo105LA != ' ') {
                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
            }
            mSPACE(false);
        }
        if (z) {
            token = makeToken(7);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    /* JADX WARN: Code restructure failed: missing block: B:153:0x04a6, code lost:
    
        if (mo105LA(1) != ' ') goto L198;
     */
    /* JADX WARN: Code restructure failed: missing block: B:183:0x0551, code lost:
    
        if (mo105LA(1) != ' ') goto L198;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void mXML_ENCODING_DECL(boolean z) {
        Token token;
        int length = this.text.length();
        int length2 = this.text.length();
        match("<?xml");
        this.text.setLength(length2);
        char mo105LA = mo105LA(1);
        if (mo105LA == '\n' || mo105LA == '\r' || mo105LA == ' ') {
            int length3 = this.text.length();
            mSPACING(false);
            this.text.setLength(length3);
        } else if (mo105LA != 'e' && mo105LA != 'v') {
            throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
        }
        char mo105LA2 = mo105LA(1);
        if (mo105LA2 != 'e') {
            if (mo105LA2 != 'v') {
                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
            }
            int length4 = this.text.length();
            match("version");
            this.text.setLength(length4);
            char mo105LA3 = mo105LA(1);
            if (mo105LA3 == '\n' || mo105LA3 == '\r' || mo105LA3 == ' ') {
                int length5 = this.text.length();
                mSPACING(false);
                this.text.setLength(length5);
            } else if (mo105LA3 != '=') {
                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
            }
            int length6 = this.text.length();
            match("=");
            this.text.setLength(length6);
            char mo105LA4 = mo105LA(1);
            if (mo105LA4 == '\n' || mo105LA4 == '\r' || mo105LA4 == ' ') {
                int length7 = this.text.length();
                mSPACING(false);
                this.text.setLength(length7);
            } else if (mo105LA4 != '\"' && mo105LA4 != '\'') {
                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
            }
            char mo105LA5 = mo105LA(1);
            if (mo105LA5 == '\"') {
                int length8 = this.text.length();
                match('\"');
                this.text.setLength(length8);
                char mo105LA6 = mo105LA(1);
                if (mo105LA6 == '\n' || mo105LA6 == '\r' || mo105LA6 == ' ') {
                    int length9 = this.text.length();
                    mSPACING(false);
                    this.text.setLength(length9);
                } else {
                    switch (mo105LA6) {
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
                char mo105LA7 = mo105LA(1);
                if (mo105LA7 == '\n' || mo105LA7 == '\r' || mo105LA7 == ' ') {
                    int length11 = this.text.length();
                    mSPACING(false);
                    this.text.setLength(length11);
                } else if (mo105LA7 != '.') {
                    throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                }
                int length12 = this.text.length();
                match('.');
                this.text.setLength(length12);
                char mo105LA8 = mo105LA(1);
                if (mo105LA8 == '\n' || mo105LA8 == '\r' || mo105LA8 == ' ') {
                    int length13 = this.text.length();
                    mSPACING(false);
                    this.text.setLength(length13);
                } else {
                    switch (mo105LA8) {
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
                char mo105LA9 = mo105LA(1);
                if (mo105LA9 == '\n' || mo105LA9 == '\r' || mo105LA9 == ' ') {
                    int length15 = this.text.length();
                    mSPACING(false);
                    this.text.setLength(length15);
                } else if (mo105LA9 != '\"') {
                    throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                }
                int length16 = this.text.length();
                match('\"');
                this.text.setLength(length16);
                char mo105LA10 = mo105LA(1);
                if (mo105LA10 != '\n' && mo105LA10 != '\r' && mo105LA10 != ' ') {
                    if (mo105LA10 != 'e') {
                        throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                    }
                }
                int length17 = this.text.length();
                mSPACING(false);
                this.text.setLength(length17);
            } else {
                if (mo105LA5 != '\'') {
                    throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                }
                int length18 = this.text.length();
                match("'");
                this.text.setLength(length18);
                char mo105LA11 = mo105LA(1);
                if (mo105LA11 == '\n' || mo105LA11 == '\r' || mo105LA11 == ' ') {
                    int length19 = this.text.length();
                    mSPACING(false);
                    this.text.setLength(length19);
                } else {
                    switch (mo105LA11) {
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
                int length20 = this.text.length();
                mDIGIT(false);
                this.text.setLength(length20);
                char mo105LA12 = mo105LA(1);
                if (mo105LA12 == '\n' || mo105LA12 == '\r' || mo105LA12 == ' ') {
                    int length21 = this.text.length();
                    mSPACING(false);
                    this.text.setLength(length21);
                } else if (mo105LA12 != '.') {
                    throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                }
                int length22 = this.text.length();
                match('.');
                this.text.setLength(length22);
                char mo105LA13 = mo105LA(1);
                if (mo105LA13 == '\n' || mo105LA13 == '\r' || mo105LA13 == ' ') {
                    int length23 = this.text.length();
                    mSPACING(false);
                    this.text.setLength(length23);
                } else {
                    switch (mo105LA13) {
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
                int length24 = this.text.length();
                mDIGIT(false);
                this.text.setLength(length24);
                char mo105LA14 = mo105LA(1);
                if (mo105LA14 == '\n' || mo105LA14 == '\r' || mo105LA14 == ' ') {
                    int length25 = this.text.length();
                    mSPACING(false);
                    this.text.setLength(length25);
                } else if (mo105LA14 != '\'') {
                    throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                }
                int length26 = this.text.length();
                match("'");
                this.text.setLength(length26);
                char mo105LA15 = mo105LA(1);
                if (mo105LA15 != '\n' && mo105LA15 != '\r' && mo105LA15 != ' ') {
                    if (mo105LA15 != 'e') {
                        throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                    }
                }
                int length172 = this.text.length();
                mSPACING(false);
                this.text.setLength(length172);
            }
        }
        int length27 = this.text.length();
        match("encoding");
        this.text.setLength(length27);
        char mo105LA16 = mo105LA(1);
        if (mo105LA16 == '\n' || mo105LA16 == '\r' || mo105LA16 == ' ') {
            int length28 = this.text.length();
            mSPACING(false);
            this.text.setLength(length28);
        } else if (mo105LA16 != '=') {
            throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
        }
        int length29 = this.text.length();
        match("=");
        this.text.setLength(length29);
        char mo105LA17 = mo105LA(1);
        if (mo105LA17 == '\n' || mo105LA17 == '\r' || mo105LA17 == ' ') {
            int length30 = this.text.length();
            mSPACING(false);
            this.text.setLength(length30);
        } else if (mo105LA17 != '\"' && mo105LA17 != '\'') {
            throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
        }
        char mo105LA18 = mo105LA(1);
        if (mo105LA18 == '\"') {
            int length31 = this.text.length();
            match('\"');
            this.text.setLength(length31);
            char mo105LA19 = mo105LA(1);
            if (mo105LA19 == '\n' || mo105LA19 == '\r' || mo105LA19 == ' ') {
                int length32 = this.text.length();
                mSPACING(false);
                this.text.setLength(length32);
            } else {
                switch (mo105LA19) {
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
            char mo105LA20 = mo105LA(1);
            if (mo105LA20 == '\n' || mo105LA20 == '\r' || mo105LA20 == ' ') {
                int length33 = this.text.length();
                mSPACING(false);
                this.text.setLength(length33);
            } else if (mo105LA20 != '\"') {
                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
            }
            int length34 = this.text.length();
            match('\"');
            this.text.setLength(length34);
            if (mo105LA(1) != '\n') {
                if (mo105LA(1) != '\r') {
                }
            }
            int length35 = this.text.length();
            mSPACING(false);
            this.text.setLength(length35);
        } else {
            if (mo105LA18 != '\'') {
                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
            }
            int length36 = this.text.length();
            match("'");
            this.text.setLength(length36);
            char mo105LA21 = mo105LA(1);
            if (mo105LA21 == '\n' || mo105LA21 == '\r' || mo105LA21 == ' ') {
                int length37 = this.text.length();
                mSPACING(false);
                this.text.setLength(length37);
            } else {
                switch (mo105LA21) {
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
            char mo105LA22 = mo105LA(1);
            if (mo105LA22 == '\n' || mo105LA22 == '\r' || mo105LA22 == ' ') {
                int length38 = this.text.length();
                mSPACING(false);
                this.text.setLength(length38);
            } else if (mo105LA22 != '\'') {
                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
            }
            int length39 = this.text.length();
            match("'");
            this.text.setLength(length39);
            if (mo105LA(1) != '\n') {
                if (mo105LA(1) != '\r') {
                }
            }
            int length352 = this.text.length();
            mSPACING(false);
            this.text.setLength(length352);
        }
        if (z) {
            token = makeToken(5);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r5v1, types: [antlr.CharScanner, cpdetector.io.parser.EncodingLexer] */
    /* JADX WARN: Type inference failed for: r5v2, types: [antlr.CharScanner] */
    /* JADX WARN: Type inference failed for: r5v8 */
    /* JADX WARN: Type inference failed for: r5v9, types: [antlr.Token] */
    @Override // antlr.TokenStream
    public Token nextToken() {
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
