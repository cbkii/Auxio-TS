package antlr.preprocessor;

import antlr.ANTLRHashString;
import antlr.ByteBuffer;
import antlr.CharBuffer;
import antlr.CharScanner;
import antlr.CharStreamException;
import antlr.CharStreamIOException;
import antlr.InputBuffer;
import antlr.LexerSharedInputState;
import antlr.MismatchedCharException;
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

/* JADX INFO: loaded from: classes3.dex */
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

    public final void mACTION(boolean z) throws NoViableAltForCharException, MismatchedCharException {
        Token tokenMakeToken;
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
            tokenMakeToken = makeToken(7);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    public final void mALT(boolean z) throws NoViableAltForCharException, MismatchedCharException {
        Token tokenMakeToken;
        int length = this.text.length();
        while (_tokenSet_3.member(mo105LA(1)) && mo105LA(2) >= 3 && mo105LA(2) <= 255) {
            mELEMENT(false);
        }
        if (z) {
            tokenMakeToken = makeToken(27);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    public final void mARG_ACTION(boolean z) throws NoViableAltForCharException, MismatchedCharException {
        Token tokenMakeToken;
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
            tokenMakeToken = makeToken(20);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    public final void mASSIGN_RHS(boolean z) throws NoViableAltForCharException, MismatchedCharException {
        Token tokenMakeToken;
        int length = this.text.length();
        int length2 = this.text.length();
        match('=');
        this.text.setLength(length2);
        while (mo105LA(1) != ';') {
            if (mo105LA(1) == '\"' && mo105LA(2) >= 3 && mo105LA(2) <= 255) {
                mSTRING_LITERAL(false);
            } else if (mo105LA(1) == '\'' && _tokenSet_6.member(mo105LA(2))) {
                mCHAR_LITERAL(false);
            } else if ((mo105LA(1) == '\n' || mo105LA(1) == '\r') && mo105LA(2) >= 3 && mo105LA(2) <= 255) {
                mNEWLINE(false);
            } else if (mo105LA(1) < 3 || mo105LA(1) > 255 || mo105LA(2) < 3 || mo105LA(2) > 255) {
                break;
            } else {
                matchNot(CharScanner.EOF_CHAR);
            }
        }
        match(';');
        if (z) {
            tokenMakeToken = makeToken(14);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    public final void mBANG(boolean z) {
        Token tokenMakeToken;
        int length = this.text.length();
        match('!');
        if (z) {
            tokenMakeToken = makeToken(19);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    public final void mCHAR_LITERAL(boolean z) throws NoViableAltForCharException, MismatchedCharException {
        Token tokenMakeToken;
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
            tokenMakeToken = makeToken(38);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    public final void mCOMMA(boolean z) {
        Token tokenMakeToken;
        int length = this.text.length();
        match(',');
        if (z) {
            tokenMakeToken = makeToken(24);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    public final void mCOMMENT(boolean z) throws NoViableAltForCharException, MismatchedCharException {
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

    public final void mCURLY_BLOCK_SCARF(boolean z) throws NoViableAltForCharException, MismatchedCharException {
        Token tokenMakeToken;
        int length = this.text.length();
        match('{');
        while (mo105LA(1) != '}') {
            if ((mo105LA(1) == '\n' || mo105LA(1) == '\r') && mo105LA(2) >= 3 && mo105LA(2) <= 255) {
                mNEWLINE(false);
            } else if (mo105LA(1) == '\"' && mo105LA(2) >= 3 && mo105LA(2) <= 255) {
                mSTRING_LITERAL(false);
            } else if (mo105LA(1) == '\'' && _tokenSet_6.member(mo105LA(2))) {
                mCHAR_LITERAL(false);
            } else if (mo105LA(1) == '/' && (mo105LA(2) == '*' || mo105LA(2) == '/')) {
                mCOMMENT(false);
            } else if (mo105LA(1) < 3 || mo105LA(1) > 255 || mo105LA(2) < 3 || mo105LA(2) > 255) {
                break;
            } else {
                matchNot(CharScanner.EOF_CHAR);
            }
        }
        match('}');
        if (z) {
            tokenMakeToken = makeToken(32);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    public final void mDIGIT(boolean z) {
        Token tokenMakeToken;
        int length = this.text.length();
        matchRange('0', '9');
        if (z) {
            tokenMakeToken = makeToken(41);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    public final void mELEMENT(boolean z) throws NoViableAltForCharException, MismatchedCharException {
        Token tokenMakeToken;
        int length = this.text.length();
        char cMo105LA = mo105LA(1);
        if (cMo105LA == '\n' || cMo105LA == '\r') {
            mNEWLINE(false);
        } else if (cMo105LA == '\"') {
            mSTRING_LITERAL(false);
        } else if (cMo105LA == '/') {
            mCOMMENT(false);
        } else if (cMo105LA == '{') {
            mACTION(false);
        } else if (cMo105LA == '\'') {
            mCHAR_LITERAL(false);
        } else if (cMo105LA == '(') {
            mSUBRULE_BLOCK(false);
        } else {
            if (!_tokenSet_5.member(mo105LA(1))) {
                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
            }
            match(_tokenSet_5);
        }
        if (z) {
            tokenMakeToken = makeToken(28);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    /* JADX WARN: Removed duplicated region for block: B:59:0x00d8  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x0139 A[PHI: r1
      0x0139: PHI (r1v2 char) = (r1v0 char), (r1v3 char), (r1v4 char), (r1v5 char), (r1v6 char), (r1v7 char), (r1v9 char), (r1v11 char) binds: [B:6:0x0018, B:8:0x001c, B:10:0x0020, B:12:0x0024, B:14:0x0028, B:16:0x002c, B:76:0x0137, B:75:0x0134] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:78:0x013d A[PHI: r4
      0x013d: PHI (r4v1 char) = (r4v0 char), (r4v2 char) binds: [B:3:0x0012, B:5:0x0016] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void mESC(boolean z) throws NoViableAltForCharException {
        Token tokenMakeToken;
        int length = this.text.length();
        char c2 = '\\';
        match('\\');
        char cMo105LA = mo105LA(1);
        char c3 = '\"';
        if (cMo105LA != '\"') {
            c3 = '\'';
            if (cMo105LA == '\'') {
                match(c3);
            } else if (cMo105LA != '\\') {
                c2 = 'f';
                if (cMo105LA != 'f') {
                    c2 = 'n';
                    if (cMo105LA != 'n') {
                        c2 = 'r';
                        if (cMo105LA != 'r') {
                            c2 = 'w';
                            if (cMo105LA != 'w') {
                                c2 = 'a';
                                if (cMo105LA == 'a') {
                                    match(c2);
                                } else {
                                    if (cMo105LA == 'b') {
                                        c2 = 'b';
                                    } else if (cMo105LA == 't') {
                                        c2 = 't';
                                    } else if (cMo105LA != 'u') {
                                        switch (cMo105LA) {
                                            case '0':
                                            case '1':
                                            case '2':
                                            case '3':
                                                matchRange('0', '3');
                                                if (mo105LA(1) >= '0' && mo105LA(1) <= '9' && mo105LA(2) >= 3 && mo105LA(2) <= 255) {
                                                    mDIGIT(false);
                                                    if (mo105LA(1) >= '0' && mo105LA(1) <= '9' && mo105LA(2) >= 3 && mo105LA(2) <= 255) {
                                                        mDIGIT(false);
                                                    } else if (mo105LA(1) < 3 || mo105LA(1) > 255) {
                                                        throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                                                    }
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
                                    match(c2);
                                }
                            }
                        }
                    }
                }
            }
        }
        if (z) {
            tokenMakeToken = makeToken(40);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    /* JADX WARN: Code restructure failed: missing block: B:23:0x002a, code lost:
    
        r2 = 'a';
        r4 = 'z';
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x002d, code lost:
    
        match('_');
     */
    /* JADX WARN: Failed to find 'out' block for switch in B:10:0x0036. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0079  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0081 A[LOOP:0: B:8:0x002d->B:22:0x0081, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0036 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:6:0x0026  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:11:0x0039 -> B:6:0x0026). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:18:0x0074 -> B:7:0x002a). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void mID(boolean z) throws NoViableAltForCharException {
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
                char c2 = 'A';
                char c3 = 'Z';
                matchRange(c2, c3);
                while (true) {
                    Token tokenMakeToken = null;
                    char cMo105LA = mo105LA(1);
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
                                c2 = '0';
                                c3 = '9';
                                matchRange(c2, c3);
                                Token tokenMakeToken2 = null;
                                char cMo105LA2 = mo105LA(1);
                                if (cMo105LA2 != '_') {
                                    match('_');
                                }
                                break;
                        }
                        switch (cMo105LA2) {
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
                            default:
                                switch (cMo105LA2) {
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
                                        break;
                                    default:
                                        int iTestLiteralsTable = testLiteralsTable(new String(this.text.getBuffer(), length, this.text.length() - length), 9);
                                        if (z && iTestLiteralsTable != -1) {
                                            tokenMakeToken2 = makeToken(iTestLiteralsTable);
                                            tokenMakeToken2.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
                                        }
                                        this._returnToken = tokenMakeToken2;
                                        return;
                                }
                                break;
                        }
                        matchRange(c2, c3);
                        Token tokenMakeToken22 = null;
                        char cMo105LA22 = mo105LA(1);
                        if (cMo105LA22 != '_') {
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
                break;
        }
    }

    public final void mID_OR_KEYWORD(boolean z) throws NoViableAltForCharException, MismatchedCharException {
        Token tokenMakeToken;
        int length = this.text.length();
        mID(true);
        Token token = this._returnToken;
        int type = token.getType();
        if (_tokenSet_9.member(mo105LA(1)) && mo105LA(2) >= 3 && mo105LA(2) <= 255 && token.getText().equals("header")) {
            if (_tokenSet_1.member(mo105LA(1)) && _tokenSet_9.member(mo105LA(2))) {
                mWS(false);
            } else if (!_tokenSet_9.member(mo105LA(1)) || mo105LA(2) < 3 || mo105LA(2) > 255) {
                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
            }
            char cMo105LA = mo105LA(1);
            if (cMo105LA != '\t' && cMo105LA != '\n' && cMo105LA != '\r' && cMo105LA != ' ') {
                if (cMo105LA == '\"') {
                    mSTRING_LITERAL(false);
                } else if (cMo105LA != '/' && cMo105LA != '{') {
                    throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                }
            }
            while (true) {
                char cMo105LA2 = mo105LA(1);
                if (cMo105LA2 == '\t' || cMo105LA2 == '\n' || cMo105LA2 == '\r' || cMo105LA2 == ' ') {
                    mWS(false);
                } else if (cMo105LA2 != '/') {
                    break;
                } else {
                    mCOMMENT(false);
                }
            }
            mACTION(false);
            type = 5;
        } else if (_tokenSet_10.member(mo105LA(1)) && mo105LA(2) >= 3 && mo105LA(2) <= 255 && token.getText().equals("tokens")) {
            while (true) {
                char cMo105LA3 = mo105LA(1);
                if (cMo105LA3 == '\t' || cMo105LA3 == '\n' || cMo105LA3 == '\r' || cMo105LA3 == ' ') {
                    mWS(false);
                } else if (cMo105LA3 != '/') {
                    break;
                } else {
                    mCOMMENT(false);
                }
            }
            mCURLY_BLOCK_SCARF(false);
            type = 12;
        } else if (_tokenSet_10.member(mo105LA(1)) && token.getText().equals("options")) {
            while (true) {
                char cMo105LA4 = mo105LA(1);
                if (cMo105LA4 == '\t' || cMo105LA4 == '\n' || cMo105LA4 == '\r' || cMo105LA4 == ' ') {
                    mWS(false);
                } else if (cMo105LA4 != '/') {
                    break;
                } else {
                    mCOMMENT(false);
                }
            }
            match('{');
            type = 13;
        }
        if (!z || type == -1) {
            tokenMakeToken = null;
        } else {
            tokenMakeToken = makeToken(type);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        }
        this._returnToken = tokenMakeToken;
    }

    public final void mLPAREN(boolean z) {
        Token tokenMakeToken;
        int length = this.text.length();
        match('(');
        if (z) {
            tokenMakeToken = makeToken(29);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    public final void mML_COMMENT(boolean z) throws NoViableAltForCharException, MismatchedCharException {
        Token tokenMakeToken;
        int length = this.text.length();
        match("/*");
        while (true) {
            if (mo105LA(1) == '*' && mo105LA(2) == '/') {
                break;
            }
            if ((mo105LA(1) == '\n' || mo105LA(1) == '\r') && mo105LA(2) >= 3 && mo105LA(2) <= 255) {
                mNEWLINE(false);
            } else if (mo105LA(1) < 3 || mo105LA(1) > 255 || mo105LA(2) < 3 || mo105LA(2) > 255) {
                break;
            } else {
                matchNot(CharScanner.EOF_CHAR);
            }
        }
        match("*/");
        if (z) {
            tokenMakeToken = makeToken(37);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x0035  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0051  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void mNEWLINE(boolean z) throws NoViableAltForCharException {
        Token tokenMakeToken;
        int length = this.text.length();
        if (mo105LA(1) == '\r' && mo105LA(2) == '\n') {
            match('\r');
        } else {
            if (mo105LA(1) == '\r') {
                match('\r');
                newline();
                if (z) {
                    tokenMakeToken = null;
                } else {
                    tokenMakeToken = makeToken(34);
                    tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
                }
                this._returnToken = tokenMakeToken;
            }
            if (mo105LA(1) != '\n') {
                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
            }
        }
        match('\n');
        newline();
        if (z) {
        }
        this._returnToken = tokenMakeToken;
    }

    public final void mRCURLY(boolean z) {
        Token tokenMakeToken;
        int length = this.text.length();
        match('}');
        if (z) {
            tokenMakeToken = makeToken(15);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    public final void mRPAREN(boolean z) {
        Token tokenMakeToken;
        int length = this.text.length();
        match(')');
        if (z) {
            tokenMakeToken = makeToken(30);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x008e  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x0104 A[SYNTHETIC] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:19:0x0077 -> B:20:0x0085). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void mRULE_BLOCK(boolean z) throws NoViableAltForCharException, MismatchedCharException {
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
        char cMo105LA = mo105LA(1);
        if (cMo105LA == '\t' || cMo105LA == '\n' || cMo105LA == '\r' || cMo105LA == ' ') {
            int length3 = this.text.length();
            mWS(false);
            this.text.setLength(length3);
        } else if (cMo105LA != ';' && cMo105LA != '|') {
            throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
        }
        while (true) {
            Token tokenMakeToken = null;
            if (mo105LA(1) == '|') {
                match(';');
                if (z) {
                    tokenMakeToken = makeToken(22);
                    tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
                }
                this._returnToken = tokenMakeToken;
                return;
            }
            match('|');
            if (_tokenSet_1.member(mo105LA(1)) && _tokenSet_2.member(mo105LA(2))) {
                int length4 = this.text.length();
                mWS(false);
                this.text.setLength(length4);
            } else if (!_tokenSet_2.member(mo105LA(1))) {
                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
            }
            mALT(false);
            char cMo105LA2 = mo105LA(1);
            if (cMo105LA2 == '\t' || cMo105LA2 == '\n' || cMo105LA2 == '\r' || cMo105LA2 == ' ') {
                break;
            } else if (cMo105LA2 != ';' && cMo105LA2 != '|') {
                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
            }
        }
        int length32 = this.text.length();
        mWS(false);
        this.text.setLength(length32);
        while (true) {
            Token tokenMakeToken2 = null;
            if (mo105LA(1) == '|') {
            }
        }
    }

    public final void mSEMI(boolean z) {
        Token tokenMakeToken;
        int length = this.text.length();
        match(';');
        if (z) {
            tokenMakeToken = makeToken(11);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    public final void mSL_COMMENT(boolean z) throws NoViableAltForCharException, MismatchedCharException {
        Token tokenMakeToken;
        int length = this.text.length();
        match("//");
        while (mo105LA(1) != '\n' && mo105LA(1) != '\r' && mo105LA(1) >= 3 && mo105LA(1) <= 255 && mo105LA(2) >= 3 && mo105LA(2) <= 255) {
            matchNot(CharScanner.EOF_CHAR);
        }
        mNEWLINE(false);
        if (z) {
            tokenMakeToken = makeToken(36);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    public final void mSTRING_LITERAL(boolean z) throws NoViableAltForCharException, MismatchedCharException {
        Token tokenMakeToken;
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
            tokenMakeToken = makeToken(39);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    /* JADX WARN: Removed duplicated region for block: B:42:0x00e9  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x0105  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x010d A[PHI: r3
      0x010d: PHI (r3v4 char) = (r3v2 char), (r3v3 char) binds: [B:49:0x010b, B:52:0x0117] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:51:0x0111  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0127  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x0142  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:26:0x009c -> B:6:0x0026). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void mSUBRULE_BLOCK(boolean z) throws NoViableAltForCharException {
        char cMo105LA;
        Token tokenMakeToken;
        int length = this.text.length();
        match('(');
        if (_tokenSet_1.member(mo105LA(1)) && _tokenSet_0.member(mo105LA(2))) {
            mWS(false);
        } else if (!_tokenSet_0.member(mo105LA(1))) {
            throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
        }
        do {
            mALT(false);
            if (_tokenSet_4.member(mo105LA(1)) || !_tokenSet_0.member(mo105LA(2))) {
                cMo105LA = mo105LA(1);
                if (cMo105LA != '\t' || cMo105LA == '\n' || cMo105LA == '\r' || cMo105LA == ' ') {
                    mWS(false);
                } else if (cMo105LA != ')') {
                    throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                }
                match(')');
                if (mo105LA(1) == '=' || mo105LA(2) != '>') {
                    char c2 = '*';
                    if (mo105LA(1) != '*') {
                        match(c2);
                    } else {
                        c2 = '+';
                        if (mo105LA(1) != '+') {
                            if (mo105LA(1) == '?') {
                                match('?');
                            }
                        }
                    }
                } else {
                    match("=>");
                }
                if (z) {
                    tokenMakeToken = null;
                } else {
                    tokenMakeToken = makeToken(6);
                    tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
                }
                this._returnToken = tokenMakeToken;
                return;
            }
            char cMo105LA2 = mo105LA(1);
            if (cMo105LA2 == '\t' || cMo105LA2 == '\n' || cMo105LA2 == '\r' || cMo105LA2 == ' ') {
                mWS(false);
            } else if (cMo105LA2 != '|') {
                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
            }
            match('|');
            if (_tokenSet_1.member(mo105LA(1)) && _tokenSet_0.member(mo105LA(2))) {
                mWS(false);
                mALT(false);
                if (_tokenSet_4.member(mo105LA(1))) {
                }
                cMo105LA = mo105LA(1);
                if (cMo105LA != '\t') {
                    mWS(false);
                }
                match(')');
                if (mo105LA(1) == '=') {
                    char c22 = '*';
                    if (mo105LA(1) != '*') {
                    }
                }
                if (z) {
                }
                this._returnToken = tokenMakeToken;
                return;
            }
        } while (_tokenSet_0.member(mo105LA(1)));
        throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
    }

    /* JADX WARN: Removed duplicated region for block: B:5:0x0010 A[PHI: r3
      0x0010: PHI (r3v4 char) = (r3v0 char), (r3v1 char) binds: [B:4:0x000e, B:7:0x001a] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void mWS(boolean z) throws NoViableAltForCharException {
        this.text.length();
        int i = 0;
        while (true) {
            char c2 = ' ';
            if (mo105LA(1) != ' ') {
                c2 = '\t';
                if (mo105LA(1) != '\t') {
                    if (mo105LA(1) != '\n' && mo105LA(1) != '\r') {
                        break;
                    } else {
                        mNEWLINE(false);
                    }
                }
            } else {
                match(c2);
            }
            i++;
        }
        if (i < 1) {
            throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
        }
        this._returnToken = null;
    }

    public final void mXDIGIT(boolean z) throws NoViableAltForCharException {
        char c2;
        char c3;
        Token tokenMakeToken;
        int length = this.text.length();
        char cMo105LA = mo105LA(1);
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
                c2 = '0';
                c3 = '9';
                break;
            default:
                switch (cMo105LA) {
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
                        switch (cMo105LA) {
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
                        break;
                }
                break;
        }
        matchRange(c2, c3);
        if (z) {
            tokenMakeToken = makeToken(42);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:51:0x00ae A[Catch: CharStreamException -> 0x00e1, RecognitionException -> 0x00e3, TryCatch #1 {RecognitionException -> 0x00e3, blocks: (B:3:0x0004, B:27:0x0038, B:28:0x003b, B:29:0x003e, B:30:0x0041, B:37:0x005e, B:39:0x006b, B:58:0x00c9, B:61:0x00cf, B:40:0x006f, B:42:0x0075, B:43:0x0079, B:45:0x0082, B:46:0x008c, B:47:0x00a1, B:34:0x0055, B:33:0x0050, B:31:0x0046, B:32:0x004b, B:48:0x00a2, B:49:0x00a6, B:50:0x00aa, B:51:0x00ae, B:52:0x00b2, B:53:0x00b6, B:54:0x00ba, B:55:0x00be, B:56:0x00c2, B:57:0x00c6), top: B:74:0x0004, outer: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:57:0x00c6 A[Catch: CharStreamException -> 0x00e1, RecognitionException -> 0x00e3, TryCatch #1 {RecognitionException -> 0x00e3, blocks: (B:3:0x0004, B:27:0x0038, B:28:0x003b, B:29:0x003e, B:30:0x0041, B:37:0x005e, B:39:0x006b, B:58:0x00c9, B:61:0x00cf, B:40:0x006f, B:42:0x0075, B:43:0x0079, B:45:0x0082, B:46:0x008c, B:47:0x00a1, B:34:0x0055, B:33:0x0050, B:31:0x0046, B:32:0x004b, B:48:0x00a2, B:49:0x00a6, B:50:0x00aa, B:51:0x00ae, B:52:0x00b2, B:53:0x00b6, B:54:0x00ba, B:55:0x00be, B:56:0x00c2, B:57:0x00c6), top: B:74:0x0004, outer: #0 }] */
    @Override // antlr.TokenStream
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public Token nextToken() throws TokenStreamException {
        do {
            resetText();
            try {
                try {
                    char cMo105LA = mo105LA(1);
                    if (cMo105LA == '\t' || cMo105LA == '\n' || cMo105LA == '\r') {
                        mWS(true);
                    } else if (cMo105LA == '\'') {
                        mCHAR_LITERAL(true);
                    } else if (cMo105LA == ')') {
                        mRPAREN(true);
                    } else if (cMo105LA == ',') {
                        mCOMMA(true);
                    } else if (cMo105LA == '/') {
                        mCOMMENT(true);
                    } else if (cMo105LA == '=') {
                        mASSIGN_RHS(true);
                    } else if (cMo105LA == '_') {
                        mID_OR_KEYWORD(true);
                    } else if (cMo105LA == '}') {
                        mRCURLY(true);
                    } else if (cMo105LA == ':') {
                        mRULE_BLOCK(true);
                    } else if (cMo105LA != ';') {
                        switch (cMo105LA) {
                            case ' ':
                                break;
                            case '!':
                                mBANG(true);
                                break;
                            case '\"':
                                mSTRING_LITERAL(true);
                                break;
                            default:
                                switch (cMo105LA) {
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
                                            case '{':
                                                mACTION(true);
                                                break;
                                            default:
                                                if (mo105LA(1) == '(' && _tokenSet_0.member(mo105LA(2))) {
                                                    mSUBRULE_BLOCK(true);
                                                } else if (mo105LA(1) == '(') {
                                                    mLPAREN(true);
                                                } else {
                                                    if (mo105LA(1) != 65535) {
                                                        throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                                                    }
                                                    uponEOF();
                                                    this._returnToken = makeToken(1);
                                                }
                                                break;
                                        }
                                        break;
                                }
                                break;
                        }
                    } else {
                        mSEMI(true);
                    }
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
