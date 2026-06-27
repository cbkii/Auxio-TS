package antlr.preprocessor;

import antlr.ANTLRHashString;
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
import p000a.p001a.p002a.p003a.C0000a;
import tv.danmaku.ijk.media.player.IjkMediaMeta;

/* loaded from: classes3.dex */
public class PreprocessorLexer extends CharScanner implements PreprocessorTokenTypes, TokenStream {
    public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
    public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
    public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
    public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
    public static final BitSet _tokenSet_4 = new BitSet(mk_tokenSet_4());
    public static final BitSet _tokenSet_5 = new BitSet(mk_tokenSet_5());
    public static final BitSet _tokenSet_6 = new BitSet(mk_tokenSet_6());
    public static final BitSet _tokenSet_7 = new BitSet(mk_tokenSet_7());
    public static final BitSet _tokenSet_8 = new BitSet(mk_tokenSet_8());
    public static final BitSet _tokenSet_9 = new BitSet(mk_tokenSet_9());
    public static final BitSet _tokenSet_10 = new BitSet(mk_tokenSet_10());

    public PreprocessorLexer(InputBuffer inputBuffer) {
        this(new LexerSharedInputState(inputBuffer));
    }

    public PreprocessorLexer(LexerSharedInputState lexerSharedInputState) {
        super(lexerSharedInputState);
        this.caseSensitiveLiterals = true;
        setCaseSensitive(true);
        this.literals = new Hashtable();
        C0000a.m6a(18, this.literals, new ANTLRHashString("public", this));
        C0000a.m6a(8, this.literals, new ANTLRHashString("class", this));
        C0000a.m6a(23, this.literals, new ANTLRHashString("throws", this));
        C0000a.m6a(26, this.literals, new ANTLRHashString("catch", this));
        C0000a.m6a(17, this.literals, new ANTLRHashString("private", this));
        C0000a.m6a(10, this.literals, new ANTLRHashString("extends", this));
        C0000a.m6a(16, this.literals, new ANTLRHashString("protected", this));
        C0000a.m6a(21, this.literals, new ANTLRHashString("returns", this));
        C0000a.m6a(4, this.literals, new ANTLRHashString("tokens", this));
        C0000a.m6a(25, this.literals, new ANTLRHashString("exception", this));
    }

    public PreprocessorLexer(InputStream inputStream) {
        this(new ByteBuffer(inputStream));
    }

    public PreprocessorLexer(Reader reader) {
        this(new CharBuffer(reader));
    }

    public static final long[] mk_tokenSet_0() {
        long[] jArr = new long[8];
        jArr[0] = -576460752303423496L;
        for (int i = 1; i <= 3; i++) {
            jArr[i] = -1;
        }
        return jArr;
    }

    public static final long[] mk_tokenSet_1() {
        return new long[]{4294977024L, 0, 0, 0, 0};
    }

    public static final long[] mk_tokenSet_10() {
        return new long[]{140741783332352L, 576460752303423488L, 0, 0, 0};
    }

    public static final long[] mk_tokenSet_2() {
        long[] jArr = new long[8];
        jArr[0] = -2199023255560L;
        for (int i = 1; i <= 3; i++) {
            jArr[i] = -1;
        }
        return jArr;
    }

    public static final long[] mk_tokenSet_3() {
        long[] jArr = new long[8];
        jArr[0] = -576462951326679048L;
        for (int i = 1; i <= 3; i++) {
            jArr[i] = -1;
        }
        return jArr;
    }

    public static final long[] mk_tokenSet_4() {
        return new long[]{4294977024L, 1152921504606846976L, 0, 0, 0};
    }

    public static final long[] mk_tokenSet_5() {
        long[] jArr = new long[8];
        jArr[0] = -576605355262354440L;
        jArr[1] = -576460752303423489L;
        for (int i = 2; i <= 3; i++) {
            jArr[i] = -1;
        }
        return jArr;
    }

    public static final long[] mk_tokenSet_6() {
        long[] jArr = new long[8];
        jArr[0] = -549755813896L;
        for (int i = 1; i <= 3; i++) {
            jArr[i] = -1;
        }
        return jArr;
    }

    public static final long[] mk_tokenSet_7() {
        long[] jArr = new long[8];
        jArr[0] = -17179869192L;
        jArr[1] = -268435457;
        for (int i = 2; i <= 3; i++) {
            jArr[i] = -1;
        }
        return jArr;
    }

    public static final long[] mk_tokenSet_8() {
        long[] jArr = new long[8];
        jArr[0] = -549755813896L;
        jArr[1] = -268435457;
        for (int i = 2; i <= 3; i++) {
            jArr[i] = -1;
        }
        return jArr;
    }

    public static final long[] mk_tokenSet_9() {
        return new long[]{140758963201536L, 576460752303423488L, 0, 0, 0};
    }

    public final void mACTION(boolean z) {
        Token token;
        int length = this.text.length();
        match('{');
        while (mo105LA(1) != '}') {
            if ((mo105LA(1) == '\n' || mo105LA(1) == '\r') && mo105LA(2) >= 3 && mo105LA(2) <= 255) {
                mNEWLINE(false);
            } else if (mo105LA(1) == '{' && mo105LA(2) >= 3 && mo105LA(2) <= 255) {
                mACTION(false);
            } else if (mo105LA(1) == '\'' && _tokenSet_6.member(mo105LA(2))) {
                mCHAR_LITERAL(false);
            } else if (mo105LA(1) == '/' && (mo105LA(2) == '*' || mo105LA(2) == '/')) {
                mCOMMENT(false);
            } else if (mo105LA(1) == '\"' && mo105LA(2) >= 3 && mo105LA(2) <= 255) {
                mSTRING_LITERAL(false);
            } else if (mo105LA(1) < 3 || mo105LA(1) > 255 || mo105LA(2) < 3 || mo105LA(2) > 255) {
                break;
            } else {
                matchNot(CharScanner.EOF_CHAR);
            }
        }
        match('}');
        if (z) {
            token = makeToken(7);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x002a, code lost:
    
        r5 = makeToken(27);
        r5.setText(new java.lang.String(r4.text.getBuffer(), r0, r4.text.length() - r0));
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void mALT(boolean z) {
        int length = this.text.length();
        while (_tokenSet_3.member(mo105LA(1)) && mo105LA(2) >= 3 && mo105LA(2) <= 255) {
            mELEMENT(false);
        }
        Token token = null;
        this._returnToken = token;
    }

    public final void mARG_ACTION(boolean z) {
        Token token;
        int length = this.text.length();
        match('[');
        while (mo105LA(1) != ']') {
            if (mo105LA(1) == '[' && mo105LA(2) >= 3 && mo105LA(2) <= 255) {
                mARG_ACTION(false);
            } else if ((mo105LA(1) == '\n' || mo105LA(1) == '\r') && mo105LA(2) >= 3 && mo105LA(2) <= 255) {
                mNEWLINE(false);
            } else if (mo105LA(1) == '\'' && _tokenSet_6.member(mo105LA(2))) {
                mCHAR_LITERAL(false);
            } else if (mo105LA(1) == '\"' && mo105LA(2) >= 3 && mo105LA(2) <= 255) {
                mSTRING_LITERAL(false);
            } else if (mo105LA(1) < 3 || mo105LA(1) > 255 || mo105LA(2) < 3 || mo105LA(2) > 255) {
                break;
            } else {
                matchNot(CharScanner.EOF_CHAR);
            }
        }
        match(']');
        if (z) {
            token = makeToken(20);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    public final void mASSIGN_RHS(boolean z) {
        Token token;
        int length = this.text.length();
        int length2 = this.text.length();
        match('=');
        this.text.setLength(length2);
        while (mo105LA(1) != ';') {
            if (mo105LA(1) == '\"' && mo105LA(2) >= 3 && mo105LA(2) <= 255) {
                mSTRING_LITERAL(false);
            } else if (mo105LA(1) != '\'' || !_tokenSet_6.member(mo105LA(2))) {
                if ((mo105LA(1) != '\n' && mo105LA(1) != '\r') || mo105LA(2) < 3 || mo105LA(2) > 255) {
                    if (mo105LA(1) < 3 || mo105LA(1) > 255 || mo105LA(2) < 3 || mo105LA(2) > 255) {
                        break;
                    } else {
                        matchNot(CharScanner.EOF_CHAR);
                    }
                } else {
                    mNEWLINE(false);
                }
            } else {
                mCHAR_LITERAL(false);
            }
        }
        match(';');
        if (z) {
            token = makeToken(14);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    public final void mBANG(boolean z) {
        Token token;
        int length = this.text.length();
        match('!');
        if (z) {
            token = makeToken(19);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    public final void mCHAR_LITERAL(boolean z) {
        Token token;
        int length = this.text.length();
        match('\'');
        if (mo105LA(1) == '\\') {
            mESC(false);
        } else {
            if (!_tokenSet_8.member(mo105LA(1))) {
                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
            }
            matchNot('\'');
        }
        match('\'');
        if (z) {
            token = makeToken(38);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    public final void mCOMMA(boolean z) {
        Token token;
        int length = this.text.length();
        match(',');
        if (z) {
            token = makeToken(24);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    public final void mCOMMENT(boolean z) {
        this.text.length();
        if (mo105LA(1) == '/' && mo105LA(2) == '/') {
            mSL_COMMENT(false);
        } else {
            if (mo105LA(1) != '/' || mo105LA(2) != '*') {
                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
            }
            mML_COMMENT(false);
        }
        this._returnToken = null;
    }

    public final void mCURLY_BLOCK_SCARF(boolean z) {
        Token token;
        int length = this.text.length();
        match('{');
        while (mo105LA(1) != '}') {
            if ((mo105LA(1) == '\n' || mo105LA(1) == '\r') && mo105LA(2) >= 3 && mo105LA(2) <= 255) {
                mNEWLINE(false);
            } else if (mo105LA(1) == '\"' && mo105LA(2) >= 3 && mo105LA(2) <= 255) {
                mSTRING_LITERAL(false);
            } else if (mo105LA(1) == '\'' && _tokenSet_6.member(mo105LA(2))) {
                mCHAR_LITERAL(false);
            } else if (mo105LA(1) != '/' || (mo105LA(2) != '*' && mo105LA(2) != '/')) {
                if (mo105LA(1) < 3 || mo105LA(1) > 255 || mo105LA(2) < 3 || mo105LA(2) > 255) {
                    break;
                } else {
                    matchNot(CharScanner.EOF_CHAR);
                }
            } else {
                mCOMMENT(false);
            }
        }
        match('}');
        if (z) {
            token = makeToken(32);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    public final void mDIGIT(boolean z) {
        Token token;
        int length = this.text.length();
        matchRange('0', '9');
        if (z) {
            token = makeToken(41);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    public final void mELEMENT(boolean z) {
        Token token;
        int length = this.text.length();
        char mo105LA = mo105LA(1);
        if (mo105LA == '\n' || mo105LA == '\r') {
            mNEWLINE(false);
        } else if (mo105LA == '\"') {
            mSTRING_LITERAL(false);
        } else if (mo105LA == '/') {
            mCOMMENT(false);
        } else if (mo105LA == '{') {
            mACTION(false);
        } else if (mo105LA == '\'') {
            mCHAR_LITERAL(false);
        } else if (mo105LA == '(') {
            mSUBRULE_BLOCK(false);
        } else {
            if (!_tokenSet_5.member(mo105LA(1))) {
                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
            }
            match(_tokenSet_5);
        }
        if (z) {
            token = makeToken(28);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    /* JADX WARN: Removed duplicated region for block: B:37:0x0142  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x015e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void mESC(boolean z) {
        Token token;
        int length = this.text.length();
        char c2 = '\\';
        match('\\');
        char mo105LA = mo105LA(1);
        char c3 = '\"';
        if (mo105LA != '\"') {
            c3 = '\'';
            if (mo105LA != '\'') {
                if (mo105LA != '\\') {
                    c2 = 'f';
                    if (mo105LA != 'f') {
                        c2 = 'n';
                        if (mo105LA != 'n') {
                            c2 = 'r';
                            if (mo105LA != 'r') {
                                c2 = 'w';
                                if (mo105LA != 'w') {
                                    c2 = 'a';
                                    if (mo105LA != 'a') {
                                        if (mo105LA == 'b') {
                                            c2 = 'b';
                                        } else {
                                            if (mo105LA != 't') {
                                                if (mo105LA != 'u') {
                                                    switch (mo105LA) {
                                                        case '0':
                                                        case '1':
                                                        case '2':
                                                        case '3':
                                                            matchRange('0', '3');
                                                            if (mo105LA(1) >= '0' && mo105LA(1) <= '9' && mo105LA(2) >= 3 && mo105LA(2) <= 255) {
                                                                mDIGIT(false);
                                                                if (mo105LA(1) < '0' || mo105LA(1) > '9' || mo105LA(2) < 3 || mo105LA(2) > 255) {
                                                                    if (mo105LA(1) < 3 || mo105LA(1) > 255) {
                                                                        throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                                                                    }
                                                                }
                                                                mDIGIT(false);
                                                                break;
                                                            } else if (mo105LA(1) < 3 || mo105LA(1) > 255) {
                                                                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                                                            }
                                                            break;
                                                        case '4':
                                                        case '5':
                                                        case '6':
                                                        case '7':
                                                            matchRange('4', '7');
                                                            if (mo105LA(1) < '0' || mo105LA(1) > '9' || mo105LA(2) < 3 || mo105LA(2) > 255) {
                                                                if (mo105LA(1) < 3 || mo105LA(1) > 255) {
                                                                    throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                                                                }
                                                            }
                                                            mDIGIT(false);
                                                            break;
                                                        default:
                                                            throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                                                    }
                                                } else {
                                                    match('u');
                                                    mXDIGIT(false);
                                                    mXDIGIT(false);
                                                    mXDIGIT(false);
                                                    mXDIGIT(false);
                                                }
                                                if (z) {
                                                    token = makeToken(40);
                                                    token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
                                                } else {
                                                    token = null;
                                                }
                                                this._returnToken = token;
                                            }
                                            c2 = 't';
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                match(c2);
                if (z) {
                }
                this._returnToken = token;
            }
        }
        match(c3);
        if (z) {
        }
        this._returnToken = token;
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:0x003c, code lost:
    
        switch(r5) {
            case 97: goto L19;
            case 98: goto L19;
            case 99: goto L19;
            case 100: goto L19;
            case 101: goto L19;
            case 102: goto L19;
            case 103: goto L19;
            case 104: goto L19;
            case 105: goto L19;
            case 106: goto L19;
            case 107: goto L19;
            case 108: goto L19;
            case 109: goto L19;
            case 110: goto L19;
            case 111: goto L19;
            case 112: goto L19;
            case 113: goto L19;
            case 114: goto L19;
            case 115: goto L19;
            case 116: goto L19;
            case 117: goto L19;
            case 118: goto L19;
            case 119: goto L19;
            case 120: goto L19;
            case 121: goto L19;
            case 122: goto L19;
            default: goto L13;
        };
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x003f, code lost:
    
        r1 = testLiteralsTable(new java.lang.String(r6.text.getBuffer(), r0, r6.text.length() - r0), 9);
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x0055, code lost:
    
        if (r7 == false) goto L20;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0058, code lost:
    
        if (r1 == (-1)) goto L20;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x005a, code lost:
    
        r2 = makeToken(r1);
        r2.setText(new java.lang.String(r6.text.getBuffer(), r0, r6.text.length() - r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x007e, code lost:
    
        r6._returnToken = r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0080, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x002a, code lost:
    
        r2 = 'a';
        r4 = 'z';
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x002d, code lost:
    
        match('_');
     */
    /* JADX WARN: Failed to find 'out' block for switch in B:13:0x0036. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:11:0x0081 A[LOOP:0: B:9:0x002d->B:11:0x0081, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:12:0x0036 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:6:0x0026  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:11:0x0039 -> B:6:0x0026). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:20:0x0074 -> B:7:0x002a). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void mID(boolean z) {
        char c2;
        char c3;
        char mo105LA;
        int length = this.text.length();
        switch (mo105LA(1)) {
            case 'A':
            case 'B':
            case 'C':
            case 'D':
            case 'E':
            case 'F':
            case 'G':
            case 'H':
            case 'I':
            case 'J':
            case 'K':
            case 'L':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'S':
            case 'T':
            case 'U':
            case 'V':
            case 'W':
            case 'X':
            case 'Y':
            case 'Z':
                c2 = 'A';
                c3 = 'Z';
                matchRange(c2, c3);
                while (true) {
                    Token token = null;
                    mo105LA = mo105LA(1);
                    if (mo105LA == '_') {
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
                                c2 = '0';
                                c3 = '9';
                                matchRange(c2, c3);
                                Token token2 = null;
                                mo105LA = mo105LA(1);
                                if (mo105LA == '_') {
                                    match('_');
                                }
                                break;
                        }
                        switch (mo105LA) {
                        }
                        matchRange(c2, c3);
                        Token token22 = null;
                        mo105LA = mo105LA(1);
                        if (mo105LA == '_') {
                        }
                    }
                }
                break;
            case '[':
            case '\\':
            case ']':
            case '^':
            case '`':
            default:
                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
            case '_':
                break;
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
                c2 = 'a';
                c3 = 'z';
                matchRange(c2, c3);
                while (true) {
                    Token token222 = null;
                    mo105LA = mo105LA(1);
                    if (mo105LA == '_') {
                    }
                    match('_');
                }
                break;
        }
    }

    public final void mID_OR_KEYWORD(boolean z) {
        Token token;
        int length = this.text.length();
        mID(true);
        Token token2 = this._returnToken;
        int type = token2.getType();
        if (_tokenSet_9.member(mo105LA(1)) && mo105LA(2) >= 3 && mo105LA(2) <= 255 && token2.getText().equals("header")) {
            if (_tokenSet_1.member(mo105LA(1)) && _tokenSet_9.member(mo105LA(2))) {
                mWS(false);
            } else if (!_tokenSet_9.member(mo105LA(1)) || mo105LA(2) < 3 || mo105LA(2) > 255) {
                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
            }
            char mo105LA = mo105LA(1);
            if (mo105LA != '\t' && mo105LA != '\n' && mo105LA != '\r' && mo105LA != ' ') {
                if (mo105LA == '\"') {
                    mSTRING_LITERAL(false);
                } else if (mo105LA != '/' && mo105LA != '{') {
                    throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                }
            }
            while (true) {
                char mo105LA2 = mo105LA(1);
                if (mo105LA2 == '\t' || mo105LA2 == '\n' || mo105LA2 == '\r' || mo105LA2 == ' ') {
                    mWS(false);
                } else if (mo105LA2 != '/') {
                    break;
                } else {
                    mCOMMENT(false);
                }
            }
            mACTION(false);
            type = 5;
        } else if (_tokenSet_10.member(mo105LA(1)) && mo105LA(2) >= 3 && mo105LA(2) <= 255 && token2.getText().equals("tokens")) {
            while (true) {
                char mo105LA3 = mo105LA(1);
                if (mo105LA3 == '\t' || mo105LA3 == '\n' || mo105LA3 == '\r' || mo105LA3 == ' ') {
                    mWS(false);
                } else if (mo105LA3 != '/') {
                    break;
                } else {
                    mCOMMENT(false);
                }
            }
            mCURLY_BLOCK_SCARF(false);
            type = 12;
        } else if (_tokenSet_10.member(mo105LA(1)) && token2.getText().equals("options")) {
            while (true) {
                char mo105LA4 = mo105LA(1);
                if (mo105LA4 == '\t' || mo105LA4 == '\n' || mo105LA4 == '\r' || mo105LA4 == ' ') {
                    mWS(false);
                } else if (mo105LA4 != '/') {
                    break;
                } else {
                    mCOMMENT(false);
                }
            }
            match('{');
            type = 13;
        }
        if (!z || type == -1) {
            token = null;
        } else {
            token = makeToken(type);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        }
        this._returnToken = token;
    }

    public final void mLPAREN(boolean z) {
        Token token;
        int length = this.text.length();
        match('(');
        if (z) {
            token = makeToken(29);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    public final void mML_COMMENT(boolean z) {
        Token token;
        int length = this.text.length();
        match("/*");
        while (true) {
            if (mo105LA(1) != '*' || mo105LA(2) != '/') {
                if ((mo105LA(1) != '\n' && mo105LA(1) != '\r') || mo105LA(2) < 3 || mo105LA(2) > 255) {
                    if (mo105LA(1) < 3 || mo105LA(1) > 255 || mo105LA(2) < 3 || mo105LA(2) > 255) {
                        break;
                    } else {
                        matchNot(CharScanner.EOF_CHAR);
                    }
                } else {
                    mNEWLINE(false);
                }
            } else {
                break;
            }
        }
        match("*/");
        if (z) {
            token = makeToken(37);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0051  */
    /* JADX WARN: Removed duplicated region for block: B:9:0x0035  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void mNEWLINE(boolean z) {
        Token token;
        int length = this.text.length();
        if (mo105LA(1) == '\r' && mo105LA(2) == '\n') {
            match('\r');
        } else {
            if (mo105LA(1) == '\r') {
                match('\r');
                newline();
                if (z) {
                    token = null;
                } else {
                    token = makeToken(34);
                    token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
                }
                this._returnToken = token;
            }
            if (mo105LA(1) != '\n') {
                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
            }
        }
        match('\n');
        newline();
        if (z) {
        }
        this._returnToken = token;
    }

    public final void mRCURLY(boolean z) {
        Token token;
        int length = this.text.length();
        match('}');
        if (z) {
            token = makeToken(15);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    public final void mRPAREN(boolean z) {
        Token token;
        int length = this.text.length();
        match(')');
        if (z) {
            token = makeToken(30);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x008e  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0104 A[SYNTHETIC] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:41:0x0077 -> B:16:0x0085). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void mRULE_BLOCK(boolean z) {
        int length = this.text.length();
        match(':');
        if (_tokenSet_1.member(mo105LA(1)) && _tokenSet_2.member(mo105LA(2))) {
            int length2 = this.text.length();
            mWS(false);
            this.text.setLength(length2);
        } else if (!_tokenSet_2.member(mo105LA(1))) {
            throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
        }
        mALT(false);
        char mo105LA = mo105LA(1);
        if (mo105LA != '\t' && mo105LA != '\n' && mo105LA != '\r' && mo105LA != ' ') {
            if (mo105LA != ';' && mo105LA != '|') {
                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
            }
            while (true) {
                Token token = null;
                if (mo105LA(1) == '|') {
                    match(';');
                    if (z) {
                        token = makeToken(22);
                        token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
                    }
                    this._returnToken = token;
                    return;
                }
                match('|');
                if (_tokenSet_1.member(mo105LA(1)) && _tokenSet_2.member(mo105LA(2))) {
                    int length3 = this.text.length();
                    mWS(false);
                    this.text.setLength(length3);
                } else if (!_tokenSet_2.member(mo105LA(1))) {
                    throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                }
                mALT(false);
                char mo105LA2 = mo105LA(1);
                if (mo105LA2 == '\t' || mo105LA2 == '\n' || mo105LA2 == '\r' || mo105LA2 == ' ') {
                    break;
                } else if (mo105LA2 != ';' && mo105LA2 != '|') {
                    throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                }
            }
        }
        int length4 = this.text.length();
        mWS(false);
        this.text.setLength(length4);
        while (true) {
            Token token2 = null;
            if (mo105LA(1) == '|') {
            }
        }
    }

    public final void mSEMI(boolean z) {
        Token token;
        int length = this.text.length();
        match(';');
        if (z) {
            token = makeToken(11);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    public final void mSL_COMMENT(boolean z) {
        Token token;
        int length = this.text.length();
        match("//");
        while (mo105LA(1) != '\n' && mo105LA(1) != '\r' && mo105LA(1) >= 3 && mo105LA(1) <= 255 && mo105LA(2) >= 3 && mo105LA(2) <= 255) {
            matchNot(CharScanner.EOF_CHAR);
        }
        mNEWLINE(false);
        if (z) {
            token = makeToken(36);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    public final void mSTRING_LITERAL(boolean z) {
        Token token;
        int length = this.text.length();
        match('\"');
        while (true) {
            if (mo105LA(1) != '\\') {
                if (!_tokenSet_7.member(mo105LA(1))) {
                    break;
                } else {
                    matchNot('\"');
                }
            } else {
                mESC(false);
            }
        }
        match('\"');
        if (z) {
            token = makeToken(39);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    /* JADX WARN: Removed duplicated region for block: B:48:0x0127  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x0142  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x0111  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:22:0x009c -> B:5:0x0026). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void mSUBRULE_BLOCK(boolean z) {
        char mo105LA;
        Token token;
        int length = this.text.length();
        match('(');
        if (!_tokenSet_1.member(mo105LA(1)) || !_tokenSet_0.member(mo105LA(2))) {
            if (!_tokenSet_0.member(mo105LA(1))) {
                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
            }
            do {
                mALT(false);
                if (_tokenSet_4.member(mo105LA(1)) || !_tokenSet_0.member(mo105LA(2))) {
                    mo105LA = mo105LA(1);
                    if (mo105LA != '\t' || mo105LA == '\n' || mo105LA == '\r' || mo105LA == ' ') {
                        mWS(false);
                    } else if (mo105LA != ')') {
                        throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                    }
                    match(')');
                    if (mo105LA(1) == '=' || mo105LA(2) != '>') {
                        char c2 = '*';
                        if (mo105LA(1) != '*') {
                            c2 = '+';
                            if (mo105LA(1) != '+') {
                                if (mo105LA(1) == '?') {
                                    match('?');
                                }
                            }
                        }
                        match(c2);
                    } else {
                        match("=>");
                    }
                    if (z) {
                        token = null;
                    } else {
                        token = makeToken(6);
                        token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
                    }
                    this._returnToken = token;
                    return;
                }
                char mo105LA2 = mo105LA(1);
                if (mo105LA2 == '\t' || mo105LA2 == '\n' || mo105LA2 == '\r' || mo105LA2 == ' ') {
                    mWS(false);
                } else if (mo105LA2 != '|') {
                    throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                }
                match('|');
                if (!_tokenSet_1.member(mo105LA(1)) || !_tokenSet_0.member(mo105LA(2))) {
                }
            } while (_tokenSet_0.member(mo105LA(1)));
            throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
        }
        mWS(false);
        do {
            mALT(false);
            if (_tokenSet_4.member(mo105LA(1))) {
            }
            mo105LA = mo105LA(1);
            if (mo105LA != '\t') {
            }
            mWS(false);
            match(')');
            if (mo105LA(1) == '=') {
            }
            char c22 = '*';
            if (mo105LA(1) != '*') {
            }
            match(c22);
            if (z) {
            }
            this._returnToken = token;
            return;
        } while (_tokenSet_0.member(mo105LA(1)));
        throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
    }

    public final void mWS(boolean z) {
        this.text.length();
        int i = 0;
        while (true) {
            char c2 = ' ';
            if (mo105LA(1) != ' ') {
                c2 = '\t';
                if (mo105LA(1) != '\t') {
                    if (mo105LA(1) != '\n' && mo105LA(1) != '\r') {
                        break;
                    }
                    mNEWLINE(false);
                    i++;
                }
            }
            match(c2);
            i++;
        }
        if (i < 1) {
            throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
        }
        this._returnToken = null;
    }

    public final void mXDIGIT(boolean z) {
        char c2;
        char c3;
        Token token;
        int length = this.text.length();
        char mo105LA = mo105LA(1);
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
                c2 = '0';
                c3 = '9';
                break;
            default:
                switch (mo105LA) {
                    case 'A':
                    case 'B':
                    case 'C':
                    case 'D':
                    case 'E':
                    case 'F':
                        c2 = 'A';
                        c3 = 'F';
                        break;
                    default:
                        switch (mo105LA) {
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                c2 = 'a';
                                c3 = 'f';
                                break;
                            default:
                                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                        }
                }
        }
        matchRange(c2, c3);
        if (z) {
            token = makeToken(42);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // antlr.TokenStream
    public Token nextToken() {
        do {
            resetText();
            try {
                try {
                    char mo105LA = mo105LA(1);
                    if (mo105LA != '\t' && mo105LA != '\n' && mo105LA != '\r') {
                        if (mo105LA == '\'') {
                            mCHAR_LITERAL(true);
                        } else if (mo105LA == ')') {
                            mRPAREN(true);
                        } else if (mo105LA == ',') {
                            mCOMMA(true);
                        } else if (mo105LA == '/') {
                            mCOMMENT(true);
                        } else if (mo105LA != '=') {
                            if (mo105LA != '_') {
                                if (mo105LA == '}') {
                                    mRCURLY(true);
                                } else if (mo105LA == ':') {
                                    mRULE_BLOCK(true);
                                } else if (mo105LA != ';') {
                                    switch (mo105LA) {
                                        case ' ':
                                            break;
                                        case '!':
                                            mBANG(true);
                                            break;
                                        case '\"':
                                            mSTRING_LITERAL(true);
                                            break;
                                        default:
                                            switch (mo105LA) {
                                                case 'A':
                                                case 'B':
                                                case 'C':
                                                case 'D':
                                                case 'E':
                                                case 'F':
                                                case 'G':
                                                case 'H':
                                                case 'I':
                                                case 'J':
                                                case 'K':
                                                case 'L':
                                                case 'M':
                                                case 'N':
                                                case 'O':
                                                case 'P':
                                                case 'Q':
                                                case 'R':
                                                case 'S':
                                                case 'T':
                                                case 'U':
                                                case 'V':
                                                case 'W':
                                                case 'X':
                                                case 'Y':
                                                case 'Z':
                                                    break;
                                                case '[':
                                                    mARG_ACTION(true);
                                                    break;
                                                default:
                                                    switch (mo105LA) {
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
                                                        case '{':
                                                            mACTION(true);
                                                            break;
                                                        default:
                                                            if (mo105LA(1) == '(' && _tokenSet_0.member(mo105LA(2))) {
                                                                mSUBRULE_BLOCK(true);
                                                                break;
                                                            } else if (mo105LA(1) == '(') {
                                                                mLPAREN(true);
                                                                break;
                                                            } else {
                                                                if (mo105LA(1) != 65535) {
                                                                    throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                                                                }
                                                                uponEOF();
                                                                this._returnToken = makeToken(1);
                                                                break;
                                                            }
                                                    }
                                            }
                                    }
                                } else {
                                    mSEMI(true);
                                }
                            }
                            mID_OR_KEYWORD(true);
                        } else {
                            mASSIGN_RHS(true);
                        }
                    }
                    mWS(true);
                } catch (RecognitionException e) {
                    throw new TokenStreamRecognitionException(e);
                }
            } catch (CharStreamException e2) {
                if (e2 instanceof CharStreamIOException) {
                    throw new TokenStreamIOException(((CharStreamIOException) e2).f303io);
                }
                throw new TokenStreamException(e2.getMessage());
            }
        } while (this._returnToken == null);
        this._returnToken.setType(testLiteralsTable(this._returnToken.getType()));
        return this._returnToken;
    }
}
