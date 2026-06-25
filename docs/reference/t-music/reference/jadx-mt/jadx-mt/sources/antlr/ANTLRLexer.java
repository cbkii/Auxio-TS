package antlr;

import antlr.collections.impl.BitSet;
import java.io.InputStream;
import java.io.Reader;
import java.util.Hashtable;
import p000a.p001a.p002a.p003a.C0000a;
import tv.danmaku.ijk.media.player.IjkMediaMeta;

/* JADX INFO: loaded from: classes3.dex */
public class ANTLRLexer extends CharScanner implements ANTLRTokenTypes, TokenStream {
    public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
    public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
    public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
    public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
    public static final BitSet _tokenSet_4 = new BitSet(mk_tokenSet_4());
    public static final BitSet _tokenSet_5 = new BitSet(mk_tokenSet_5());

    public ANTLRLexer(InputBuffer inputBuffer) {
        this(new LexerSharedInputState(inputBuffer));
    }

    public ANTLRLexer(LexerSharedInputState lexerSharedInputState) {
        super(lexerSharedInputState);
        this.caseSensitiveLiterals = true;
        setCaseSensitive(true);
        this.literals = new Hashtable();
        C0000a.m6a(31, this.literals, new ANTLRHashString("public", this));
        C0000a.m6a(10, this.literals, new ANTLRHashString("class", this));
        C0000a.m6a(5, this.literals, new ANTLRHashString("header", this));
        C0000a.m6a(37, this.literals, new ANTLRHashString("throws", this));
        C0000a.m6a(9, this.literals, new ANTLRHashString("lexclass", this));
        C0000a.m6a(40, this.literals, new ANTLRHashString("catch", this));
        C0000a.m6a(32, this.literals, new ANTLRHashString("private", this));
        C0000a.m6a(51, this.literals, new ANTLRHashString("options", this));
        C0000a.m6a(11, this.literals, new ANTLRHashString("extends", this));
        C0000a.m6a(30, this.literals, new ANTLRHashString("protected", this));
        C0000a.m6a(13, this.literals, new ANTLRHashString("TreeParser", this));
        C0000a.m6a(29, this.literals, new ANTLRHashString("Parser", this));
        C0000a.m6a(12, this.literals, new ANTLRHashString("Lexer", this));
        C0000a.m6a(35, this.literals, new ANTLRHashString("returns", this));
        C0000a.m6a(18, this.literals, new ANTLRHashString("charVocabulary", this));
        C0000a.m6a(4, this.literals, new ANTLRHashString("tokens", this));
        C0000a.m6a(39, this.literals, new ANTLRHashString("exception", this));
    }

    public ANTLRLexer(InputStream inputStream) {
        this(new ByteBuffer(inputStream));
    }

    public ANTLRLexer(Reader reader) {
        this(new CharBuffer(reader));
    }

    public static int escapeCharValue(String str) {
        if (str.charAt(1) != '\\') {
            return 0;
        }
        char cCharAt = str.charAt(2);
        int i = 34;
        if (cCharAt != '\"') {
            i = 39;
            if (cCharAt != '\'') {
                if (cCharAt == '\\') {
                    return 92;
                }
                if (cCharAt == 'b') {
                    return 8;
                }
                if (cCharAt == 'f') {
                    return 12;
                }
                if (cCharAt == 'n') {
                    return 10;
                }
                if (cCharAt == 'r') {
                    return 13;
                }
                if (cCharAt == 't') {
                    return 9;
                }
                if (cCharAt == 'u') {
                    if (str.length() != 8) {
                        return 0;
                    }
                    return Character.digit(str.charAt(6), 16) + (Character.digit(str.charAt(5), 16) * 16) + (Character.digit(str.charAt(4), 16) * 16 * 16) + (Character.digit(str.charAt(3), 16) * 16 * 16 * 16);
                }
                switch (cCharAt) {
                    case '0':
                    case '1':
                    case '2':
                    case '3':
                        if (str.length() <= 5 || !Character.isDigit(str.charAt(4))) {
                            if (str.length() > 4 && Character.isDigit(str.charAt(3))) {
                            }
                        }
                        break;
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                        if (str.length() > 4 && Character.isDigit(str.charAt(3))) {
                        }
                        break;
                }
                return 0;
            }
        }
        return i;
    }

    public static final long[] mk_tokenSet_0() {
        long[] jArr = new long[8];
        jArr[0] = -9224;
        for (int i = 1; i <= 3; i++) {
            jArr[i] = -1;
        }
        return jArr;
    }

    public static final long[] mk_tokenSet_1() {
        long[] jArr = new long[8];
        jArr[0] = -549755813896L;
        jArr[1] = -268435457;
        for (int i = 2; i <= 3; i++) {
            jArr[i] = -1;
        }
        return jArr;
    }

    public static final long[] mk_tokenSet_2() {
        long[] jArr = new long[8];
        jArr[0] = -17179869192L;
        jArr[1] = -268435457;
        for (int i = 2; i <= 3; i++) {
            jArr[i] = -1;
        }
        return jArr;
    }

    public static final long[] mk_tokenSet_3() {
        long[] jArr = new long[8];
        jArr[0] = -566935692296L;
        jArr[1] = -671088641;
        for (int i = 2; i <= 3; i++) {
            jArr[i] = -1;
        }
        return jArr;
    }

    public static final long[] mk_tokenSet_4() {
        long[] jArr = new long[8];
        jArr[0] = -549755813896L;
        for (int i = 1; i <= 3; i++) {
            jArr[i] = -1;
        }
        return jArr;
    }

    public static final long[] mk_tokenSet_5() {
        return new long[]{4294977024L, 0, 0, 0, 0};
    }

    public static int tokenTypeForCharLiteral(String str) {
        return str.length() > 3 ? escapeCharValue(str) : str.charAt(1);
    }

    public final void mACTION(boolean z) throws NoViableAltForCharException, MismatchedCharException {
        int i;
        String text;
        String str;
        int length = this.text.length();
        int line = getLine();
        int column = getColumn();
        mNESTED_ACTION(false);
        if (mo105LA(1) == '?') {
            match('?');
            i = 43;
        } else {
            i = 7;
        }
        if (i == 7) {
            text = getText();
            str = "}";
        } else {
            text = getText();
            str = "}?";
        }
        setText(StringUtils.stripFrontBack(text, "{", str));
        CommonToken commonToken = new CommonToken(i, new String(this.text.getBuffer(), length, this.text.length() - length));
        commonToken.setLine(line);
        commonToken.setColumn(column);
        this._returnToken = commonToken;
    }

    public final void mARG_ACTION(boolean z) throws NoViableAltForCharException, MismatchedCharException {
        Token tokenMakeToken;
        int length = this.text.length();
        mNESTED_ARG_ACTION(false);
        setText(StringUtils.stripFrontBack(getText(), "[", "]"));
        if (z) {
            tokenMakeToken = makeToken(34);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    public final void mASSIGN(boolean z) {
        Token tokenMakeToken;
        int length = this.text.length();
        match('=');
        if (z) {
            tokenMakeToken = makeToken(15);
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
            tokenMakeToken = makeToken(33);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    public final void mCARET(boolean z) {
        Token tokenMakeToken;
        int length = this.text.length();
        match('^');
        if (z) {
            tokenMakeToken = makeToken(49);
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
            if (!_tokenSet_1.member(mo105LA(1))) {
                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
            }
            matchNot('\'');
        }
        match('\'');
        if (z) {
            tokenMakeToken = makeToken(19);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    public final void mCLOSE_ELEMENT_OPTION(boolean z) {
        Token tokenMakeToken;
        int length = this.text.length();
        match('>');
        if (z) {
            tokenMakeToken = makeToken(26);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    public final void mCOLON(boolean z) {
        Token tokenMakeToken;
        int length = this.text.length();
        match(':');
        if (z) {
            tokenMakeToken = makeToken(36);
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
            tokenMakeToken = makeToken(38);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    public final void mCOMMENT(boolean z) throws NoViableAltForCharException, MismatchedCharException {
        int type;
        Token tokenMakeToken;
        int length = this.text.length();
        if (mo105LA(1) == '/' && mo105LA(2) == '/') {
            mSL_COMMENT(false);
            type = 53;
        } else {
            if (mo105LA(1) != '/' || mo105LA(2) != '*') {
                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
            }
            mML_COMMENT(true);
            type = this._returnToken.getType();
        }
        if (type != 8) {
            type = -1;
        }
        if (!z || type == -1) {
            tokenMakeToken = null;
        } else {
            tokenMakeToken = makeToken(type);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        }
        this._returnToken = tokenMakeToken;
    }

    public final void mDIGIT(boolean z) {
        Token tokenMakeToken;
        int length = this.text.length();
        matchRange('0', '9');
        if (z) {
            tokenMakeToken = makeToken(57);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    /* JADX WARN: Removed duplicated region for block: B:59:0x00d5  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x0137 A[PHI: r1
      0x0137: PHI (r1v2 char) = (r1v0 char), (r1v3 char), (r1v4 char), (r1v5 char), (r1v6 char), (r1v7 char), (r1v9 char), (r1v11 char) binds: [B:6:0x0018, B:8:0x001c, B:10:0x0020, B:12:0x0024, B:14:0x0028, B:16:0x002c, B:76:0x0135, B:75:0x0132] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:78:0x013b A[PHI: r4
      0x013b: PHI (r4v1 char) = (r4v0 char), (r4v2 char) binds: [B:3:0x0012, B:5:0x0016] A[DONT_GENERATE, DONT_INLINE]] */
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
                                                if (mo105LA(1) >= '0' && mo105LA(1) <= '7' && mo105LA(2) >= 3 && mo105LA(2) <= 255) {
                                                    matchRange('0', '7');
                                                    if (mo105LA(1) >= '0' && mo105LA(1) <= '7' && mo105LA(2) >= 3 && mo105LA(2) <= 255) {
                                                        matchRange('0', '7');
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
                                                if (mo105LA(1) < '0' || mo105LA(1) > '7' || mo105LA(2) < 3 || mo105LA(2) > 255) {
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
            tokenMakeToken = makeToken(56);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    public final void mIMPLIES(boolean z) {
        Token tokenMakeToken;
        int length = this.text.length();
        match("=>");
        if (z) {
            tokenMakeToken = makeToken(48);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    public final void mINT(boolean z) throws NoViableAltForCharException {
        Token tokenMakeToken;
        int length = this.text.length();
        int i = 0;
        while (mo105LA(1) >= '0' && mo105LA(1) <= '9') {
            matchRange('0', '9');
            i++;
        }
        if (i < 1) {
            throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
        }
        if (z) {
            tokenMakeToken = makeToken(20);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    public final int mINTERNAL_RULE_REF(boolean z) {
        char c2;
        char c3;
        Token tokenMakeToken;
        int length = this.text.length();
        while (true) {
            matchRange('a', 'z');
            while (true) {
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
                                    c2 = 'A';
                                    c3 = 'Z';
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
                                            int iTestLiteralsTable = testLiteralsTable(41);
                                            if (z) {
                                                tokenMakeToken = makeToken(62);
                                                tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
                                            } else {
                                                tokenMakeToken = null;
                                            }
                                            this._returnToken = tokenMakeToken;
                                            return iTestLiteralsTable;
                                    }
                                    break;
                            }
                            break;
                    }
                    matchRange(c2, c3);
                } else {
                    match('_');
                }
            }
        }
    }

    public final void mLPAREN(boolean z) {
        Token tokenMakeToken;
        int length = this.text.length();
        match('(');
        if (z) {
            tokenMakeToken = makeToken(27);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    public final void mML_COMMENT(boolean z) throws NoViableAltForCharException, MismatchedCharException {
        int i;
        Token tokenMakeToken;
        int length = this.text.length();
        match("/*");
        if (mo105LA(1) == '*' && mo105LA(2) >= 3 && mo105LA(2) <= 255 && mo105LA(2) != '/') {
            match('*');
            i = 8;
        } else {
            if (mo105LA(1) < 3 || mo105LA(1) > 255 || mo105LA(2) < 3 || mo105LA(2) > 255) {
                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
            }
            i = 55;
        }
        while (true) {
            if (mo105LA(1) == '*' && mo105LA(2) == '/') {
                break;
            }
            if (mo105LA(1) == '\r' && mo105LA(2) == '\n') {
                match('\r');
            } else if (mo105LA(1) == '\r' && mo105LA(2) >= 3 && mo105LA(2) <= 255) {
                match('\r');
                newline();
            } else if (_tokenSet_0.member(mo105LA(1)) && mo105LA(2) >= 3 && mo105LA(2) <= 255) {
                match(_tokenSet_0);
            } else if (mo105LA(1) != '\n') {
                break;
            }
            match('\n');
            newline();
        }
        match("*/");
        if (!z || i == -1) {
            tokenMakeToken = null;
        } else {
            tokenMakeToken = makeToken(i);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        }
        this._returnToken = tokenMakeToken;
    }

    public final void mNESTED_ACTION(boolean z) throws NoViableAltForCharException, MismatchedCharException {
        Token tokenMakeToken;
        int length = this.text.length();
        match('{');
        while (mo105LA(1) != '}') {
            if ((mo105LA(1) == '\n' || mo105LA(1) == '\r') && mo105LA(2) >= 3 && mo105LA(2) <= 255) {
                if (mo105LA(1) == '\r' && mo105LA(2) == '\n') {
                    match('\r');
                } else if (mo105LA(1) == '\r' && mo105LA(2) >= 3 && mo105LA(2) <= 255) {
                    match('\r');
                    newline();
                } else if (mo105LA(1) != '\n') {
                    throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                }
                match('\n');
                newline();
            } else if (mo105LA(1) == '{' && mo105LA(2) >= 3 && mo105LA(2) <= 255) {
                mNESTED_ACTION(false);
            } else if (mo105LA(1) == '\'' && _tokenSet_4.member(mo105LA(2))) {
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
            tokenMakeToken = makeToken(60);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    public final void mNESTED_ARG_ACTION(boolean z) throws NoViableAltForCharException, MismatchedCharException {
        Token tokenMakeToken;
        int length = this.text.length();
        match('[');
        while (true) {
            char cMo105LA = mo105LA(1);
            if (cMo105LA != '\n') {
                if (cMo105LA == '\"') {
                    mSTRING_LITERAL(false);
                } else if (cMo105LA == '\'') {
                    mCHAR_LITERAL(false);
                } else if (cMo105LA == '[') {
                    mNESTED_ARG_ACTION(false);
                } else if (mo105LA(1) == '\r' && mo105LA(2) == '\n') {
                    match('\r');
                } else if (mo105LA(1) == '\r' && mo105LA(2) >= 3 && mo105LA(2) <= 255) {
                    match('\r');
                    newline();
                } else if (!_tokenSet_3.member(mo105LA(1))) {
                    break;
                } else {
                    matchNot(']');
                }
            }
            match('\n');
            newline();
        }
        match(']');
        if (z) {
            tokenMakeToken = makeToken(59);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    public final void mNOT_OP(boolean z) {
        Token tokenMakeToken;
        int length = this.text.length();
        match('~');
        if (z) {
            tokenMakeToken = makeToken(42);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    public final void mOPEN_ELEMENT_OPTION(boolean z) {
        Token tokenMakeToken;
        int length = this.text.length();
        match('<');
        if (z) {
            tokenMakeToken = makeToken(25);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    public final void mOR(boolean z) {
        Token tokenMakeToken;
        int length = this.text.length();
        match('|');
        if (z) {
            tokenMakeToken = makeToken(21);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    public final void mPLUS(boolean z) {
        Token tokenMakeToken;
        int length = this.text.length();
        match('+');
        if (z) {
            tokenMakeToken = makeToken(47);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    public final void mQUESTION(boolean z) {
        Token tokenMakeToken;
        int length = this.text.length();
        match('?');
        if (z) {
            tokenMakeToken = makeToken(45);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    public final void mRANGE(boolean z) {
        Token tokenMakeToken;
        int length = this.text.length();
        match("..");
        if (z) {
            tokenMakeToken = makeToken(22);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    public final void mRCURLY(boolean z) {
        Token tokenMakeToken;
        int length = this.text.length();
        match('}');
        if (z) {
            tokenMakeToken = makeToken(17);
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
            tokenMakeToken = makeToken(28);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    public final void mRULE_REF(boolean z) throws NoViableAltForCharException, MismatchedCharException {
        Token tokenMakeToken;
        int length = this.text.length();
        int iMINTERNAL_RULE_REF = mINTERNAL_RULE_REF(false);
        if (iMINTERNAL_RULE_REF == 51) {
            mWS_LOOP(false);
            if (mo105LA(1) == '{') {
                match('{');
                iMINTERNAL_RULE_REF = 14;
            }
        } else if (iMINTERNAL_RULE_REF == 4) {
            mWS_LOOP(false);
            if (mo105LA(1) == '{') {
                match('{');
                iMINTERNAL_RULE_REF = 23;
            }
        }
        if (!z || iMINTERNAL_RULE_REF == -1) {
            tokenMakeToken = null;
        } else {
            tokenMakeToken = makeToken(iMINTERNAL_RULE_REF);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        }
        this._returnToken = tokenMakeToken;
    }

    public final void mSEMI(boolean z) {
        Token tokenMakeToken;
        int length = this.text.length();
        match(';');
        if (z) {
            tokenMakeToken = makeToken(16);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x004c  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0068  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void mSL_COMMENT(boolean z) throws NoViableAltForCharException, MismatchedCharException {
        Token tokenMakeToken;
        int length = this.text.length();
        match("//");
        while (_tokenSet_0.member(mo105LA(1))) {
            match(_tokenSet_0);
        }
        if (mo105LA(1) == '\r' && mo105LA(2) == '\n') {
            match('\r');
        } else {
            if (mo105LA(1) == '\r') {
                match('\r');
                newline();
                if (z) {
                    tokenMakeToken = null;
                } else {
                    tokenMakeToken = makeToken(54);
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

    public final void mSTAR(boolean z) {
        Token tokenMakeToken;
        int length = this.text.length();
        match('*');
        if (z) {
            tokenMakeToken = makeToken(46);
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
                if (!_tokenSet_2.member(mo105LA(1))) {
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
            tokenMakeToken = makeToken(6);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    public final void mTOKEN_REF(boolean z) {
        char c2;
        char c3;
        Token tokenMakeToken;
        int length = this.text.length();
        while (true) {
            matchRange('A', 'Z');
            while (true) {
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
                                            c2 = 'a';
                                            c3 = 'z';
                                            break;
                                        default:
                                            int iTestLiteralsTable = testLiteralsTable(24);
                                            if (!z || iTestLiteralsTable == -1) {
                                                tokenMakeToken = null;
                                            } else {
                                                tokenMakeToken = makeToken(iTestLiteralsTable);
                                                tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
                                            }
                                            this._returnToken = tokenMakeToken;
                                            return;
                                    }
                                    break;
                            }
                            break;
                    }
                    matchRange(c2, c3);
                } else {
                    match('_');
                }
            }
        }
    }

    public final void mTREE_BEGIN(boolean z) {
        Token tokenMakeToken;
        int length = this.text.length();
        match("#(");
        if (z) {
            tokenMakeToken = makeToken(44);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    public final void mWILDCARD(boolean z) {
        Token tokenMakeToken;
        int length = this.text.length();
        match('.');
        if (z) {
            tokenMakeToken = makeToken(50);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    public final void mWS(boolean z) throws NoViableAltForCharException {
        this.text.length();
        char cMo105LA = mo105LA(1);
        if (cMo105LA == '\t') {
            match('\t');
        } else if (cMo105LA == '\n') {
            match('\n');
            newline();
        } else if (cMo105LA == ' ') {
            match(' ');
        } else if (mo105LA(1) == '\r' && mo105LA(2) == '\n') {
            match('\r');
            match('\n');
            newline();
        } else {
            if (mo105LA(1) != '\r') {
                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
            }
            match('\r');
            newline();
        }
        this._returnToken = null;
    }

    public final void mWS_LOOP(boolean z) throws NoViableAltForCharException, MismatchedCharException {
        Token tokenMakeToken;
        int length = this.text.length();
        while (true) {
            char cMo105LA = mo105LA(1);
            if (cMo105LA == '\t' || cMo105LA == '\n' || cMo105LA == '\r' || cMo105LA == ' ') {
                mWS(false);
            } else if (cMo105LA != '/') {
                break;
            } else {
                mCOMMENT(false);
            }
        }
        if (z) {
            tokenMakeToken = makeToken(61);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    public final void mWS_OPT(boolean z) throws NoViableAltForCharException {
        Token tokenMakeToken;
        int length = this.text.length();
        if (_tokenSet_5.member(mo105LA(1))) {
            mWS(false);
        }
        if (z) {
            tokenMakeToken = makeToken(63);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
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
            tokenMakeToken = makeToken(58);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    /* JADX WARN: Removed duplicated region for block: B:66:0x0108 A[Catch: CharStreamException -> 0x011f, RecognitionException -> 0x0121, TryCatch #1 {RecognitionException -> 0x0121, blocks: (B:3:0x0004, B:15:0x0020, B:16:0x0023, B:17:0x0026, B:18:0x0029, B:19:0x002c, B:20:0x002f, B:44:0x00a3, B:46:0x00a9, B:67:0x010b, B:70:0x0111, B:47:0x00ad, B:49:0x00b5, B:51:0x00bb, B:52:0x00bf, B:54:0x00c5, B:55:0x00c9, B:57:0x00cf, B:58:0x00d3, B:60:0x00dc, B:61:0x00e6, B:62:0x00fb, B:37:0x0085, B:38:0x008a, B:39:0x008f, B:40:0x0094, B:41:0x0099, B:35:0x007b, B:36:0x0080, B:30:0x0062, B:31:0x0067, B:32:0x006c, B:33:0x0071, B:34:0x0076, B:24:0x0044, B:25:0x0049, B:26:0x004e, B:27:0x0053, B:28:0x0058, B:29:0x005d, B:21:0x0035, B:22:0x003a, B:23:0x003f, B:63:0x00fc, B:64:0x0100, B:65:0x0104, B:66:0x0108), top: B:83:0x0004, outer: #0 }] */
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
                    } else if (cMo105LA == '^') {
                        mCARET(true);
                    } else if (cMo105LA == '>') {
                        mCLOSE_ELEMENT_OPTION(true);
                    } else if (cMo105LA != '?') {
                        switch (cMo105LA) {
                            case ' ':
                                break;
                            case '!':
                                mBANG(true);
                                break;
                            case '\"':
                                mSTRING_LITERAL(true);
                                break;
                            case '#':
                                mTREE_BEGIN(true);
                                break;
                            default:
                                switch (cMo105LA) {
                                    case '\'':
                                        mCHAR_LITERAL(true);
                                        break;
                                    case '(':
                                        mLPAREN(true);
                                        break;
                                    case ')':
                                        mRPAREN(true);
                                        break;
                                    case '*':
                                        mSTAR(true);
                                        break;
                                    case '+':
                                        mPLUS(true);
                                        break;
                                    case ',':
                                        mCOMMA(true);
                                        break;
                                    default:
                                        switch (cMo105LA) {
                                            case '/':
                                                mCOMMENT(true);
                                                break;
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
                                                mINT(true);
                                                break;
                                            case ':':
                                                mCOLON(true);
                                                break;
                                            case ';':
                                                mSEMI(true);
                                                break;
                                            case '<':
                                                mOPEN_ELEMENT_OPTION(true);
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
                                                        mTOKEN_REF(true);
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
                                                                mRULE_REF(true);
                                                                break;
                                                            case '{':
                                                                mACTION(true);
                                                                break;
                                                            case '|':
                                                                mOR(true);
                                                                break;
                                                            case '}':
                                                                mRCURLY(true);
                                                                break;
                                                            case '~':
                                                                mNOT_OP(true);
                                                                break;
                                                            default:
                                                                if (mo105LA(1) == '=' && mo105LA(2) == '>') {
                                                                    mIMPLIES(true);
                                                                } else if (mo105LA(1) == '.' && mo105LA(2) == '.') {
                                                                    mRANGE(true);
                                                                } else if (mo105LA(1) == '=') {
                                                                    mASSIGN(true);
                                                                } else if (mo105LA(1) == '.') {
                                                                    mWILDCARD(true);
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
                                        break;
                                }
                                break;
                        }
                    } else {
                        mQUESTION(true);
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
        this._returnToken.setType(this._returnToken.getType());
        return this._returnToken;
    }
}
