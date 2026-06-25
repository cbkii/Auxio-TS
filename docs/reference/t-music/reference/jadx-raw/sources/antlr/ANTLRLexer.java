package antlr;

import antlr.collections.impl.BitSet;
import java.io.InputStream;
import java.io.Reader;
import java.util.Hashtable;
import p000a.p001a.p002a.p003a.C0000a;
import tv.danmaku.ijk.media.player.IjkMediaMeta;

/* loaded from: classes3.dex */
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
        char charAt = str.charAt(2);
        int i = 34;
        if (charAt != '\"') {
            i = 39;
            if (charAt != '\'') {
                if (charAt == '\\') {
                    return 92;
                }
                if (charAt == 'b') {
                    return 8;
                }
                if (charAt == 'f') {
                    return 12;
                }
                if (charAt == 'n') {
                    return 10;
                }
                if (charAt == 'r') {
                    return 13;
                }
                if (charAt == 't') {
                    return 9;
                }
                if (charAt == 'u') {
                    if (str.length() != 8) {
                        return 0;
                    }
                    return Character.digit(str.charAt(6), 16) + (Character.digit(str.charAt(5), 16) * 16) + (Character.digit(str.charAt(4), 16) * 16 * 16) + (Character.digit(str.charAt(3), 16) * 16 * 16 * 16);
                }
                switch (charAt) {
                    case '0':
                    case '1':
                    case '2':
                    case '3':
                        if (str.length() > 5 && Character.isDigit(str.charAt(4))) {
                            break;
                        } else if (str.length() > 4 && Character.isDigit(str.charAt(3))) {
                            break;
                        } else {
                            break;
                        }
                        break;
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                        if (str.length() > 4 && Character.isDigit(str.charAt(3))) {
                            break;
                        } else {
                            break;
                        }
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

    public final void mACTION(boolean z) {
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

    public final void mARG_ACTION(boolean z) {
        Token token;
        int length = this.text.length();
        mNESTED_ARG_ACTION(false);
        setText(StringUtils.stripFrontBack(getText(), "[", "]"));
        if (z) {
            token = makeToken(34);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    public final void mASSIGN(boolean z) {
        Token token;
        int length = this.text.length();
        match('=');
        if (z) {
            token = makeToken(15);
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
            token = makeToken(33);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    public final void mCARET(boolean z) {
        Token token;
        int length = this.text.length();
        match('^');
        if (z) {
            token = makeToken(49);
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
            if (!_tokenSet_1.member(mo105LA(1))) {
                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
            }
            matchNot('\'');
        }
        match('\'');
        if (z) {
            token = makeToken(19);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    public final void mCLOSE_ELEMENT_OPTION(boolean z) {
        Token token;
        int length = this.text.length();
        match('>');
        if (z) {
            token = makeToken(26);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    public final void mCOLON(boolean z) {
        Token token;
        int length = this.text.length();
        match(':');
        if (z) {
            token = makeToken(36);
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
            token = makeToken(38);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    public final void mCOMMENT(boolean z) {
        int type;
        Token token;
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
            token = null;
        } else {
            token = makeToken(type);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        }
        this._returnToken = token;
    }

    public final void mDIGIT(boolean z) {
        Token token;
        int length = this.text.length();
        matchRange('0', '9');
        if (z) {
            token = makeToken(57);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    /* JADX WARN: Removed duplicated region for block: B:37:0x0140  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x015c  */
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
                                                            if (mo105LA(1) >= '0' && mo105LA(1) <= '7' && mo105LA(2) >= 3 && mo105LA(2) <= 255) {
                                                                matchRange('0', '7');
                                                                if (mo105LA(1) < '0' || mo105LA(1) > '7' || mo105LA(2) < 3 || mo105LA(2) > 255) {
                                                                    if (mo105LA(1) < 3 || mo105LA(1) > 255) {
                                                                        throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                                                                    }
                                                                }
                                                                matchRange('0', '7');
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
                                                            if (mo105LA(1) < '0' || mo105LA(1) > '7' || mo105LA(2) < 3 || mo105LA(2) > 255) {
                                                                if (mo105LA(1) < 3 || mo105LA(1) > 255) {
                                                                    throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                                                                }
                                                            }
                                                            matchRange('0', '7');
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
                                                    token = makeToken(56);
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

    public final void mIMPLIES(boolean z) {
        Token token;
        int length = this.text.length();
        match("=>");
        if (z) {
            token = makeToken(48);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    public final void mINT(boolean z) {
        Token token;
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
            token = makeToken(20);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x0019, code lost:
    
        switch(r3) {
            case 65: goto L14;
            case 66: goto L14;
            case 67: goto L14;
            case 68: goto L14;
            case 69: goto L14;
            case 70: goto L14;
            case 71: goto L14;
            case 72: goto L14;
            case 73: goto L14;
            case 74: goto L14;
            case 75: goto L14;
            case 76: goto L14;
            case 77: goto L14;
            case 78: goto L14;
            case 79: goto L14;
            case 80: goto L14;
            case 81: goto L14;
            case 82: goto L14;
            case 83: goto L14;
            case 84: goto L14;
            case 85: goto L14;
            case 86: goto L14;
            case 87: goto L14;
            case 88: goto L14;
            case 89: goto L14;
            case 90: goto L14;
            default: goto L22;
        };
     */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x004b, code lost:
    
        r3 = 'A';
        r4 = 'Z';
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x001c, code lost:
    
        switch(r3) {
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
    /* JADX WARN: Code restructure failed: missing block: B:18:0x001f, code lost:
    
        r1 = testLiteralsTable(41);
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0025, code lost:
    
        if (r6 == false) goto L15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x0027, code lost:
    
        r6 = makeToken(62);
        r6.setText(new java.lang.String(r5.text.getBuffer(), r0, r5.text.length() - r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0051, code lost:
    
        r5._returnToken = r6;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0053, code lost:
    
        return r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0050, code lost:
    
        r6 = null;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final int mINTERNAL_RULE_REF(boolean z) {
        char c2;
        char c3;
        int length = this.text.length();
        while (true) {
            matchRange('a', 'z');
            while (true) {
                char mo105LA = mo105LA(1);
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
                            c2 = '0';
                            c3 = '9';
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
        Token token;
        int length = this.text.length();
        match('(');
        if (z) {
            token = makeToken(27);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    public final void mML_COMMENT(boolean z) {
        int i;
        Token token;
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
            token = null;
        } else {
            token = makeToken(i);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        }
        this._returnToken = token;
    }

    public final void mNESTED_ACTION(boolean z) {
        Token token;
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
            token = makeToken(60);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    public final void mNESTED_ARG_ACTION(boolean z) {
        Token token;
        int length = this.text.length();
        match('[');
        while (true) {
            char mo105LA = mo105LA(1);
            if (mo105LA != '\n') {
                if (mo105LA == '\"') {
                    mSTRING_LITERAL(false);
                } else if (mo105LA == '\'') {
                    mCHAR_LITERAL(false);
                } else if (mo105LA == '[') {
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
            token = makeToken(59);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    public final void mNOT_OP(boolean z) {
        Token token;
        int length = this.text.length();
        match('~');
        if (z) {
            token = makeToken(42);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    public final void mOPEN_ELEMENT_OPTION(boolean z) {
        Token token;
        int length = this.text.length();
        match('<');
        if (z) {
            token = makeToken(25);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    public final void mOR(boolean z) {
        Token token;
        int length = this.text.length();
        match('|');
        if (z) {
            token = makeToken(21);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    public final void mPLUS(boolean z) {
        Token token;
        int length = this.text.length();
        match('+');
        if (z) {
            token = makeToken(47);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    public final void mQUESTION(boolean z) {
        Token token;
        int length = this.text.length();
        match('?');
        if (z) {
            token = makeToken(45);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    public final void mRANGE(boolean z) {
        Token token;
        int length = this.text.length();
        match("..");
        if (z) {
            token = makeToken(22);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    public final void mRCURLY(boolean z) {
        Token token;
        int length = this.text.length();
        match('}');
        if (z) {
            token = makeToken(17);
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
            token = makeToken(28);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    public final void mRULE_REF(boolean z) {
        Token token;
        int length = this.text.length();
        int mINTERNAL_RULE_REF = mINTERNAL_RULE_REF(false);
        if (mINTERNAL_RULE_REF == 51) {
            mWS_LOOP(false);
            if (mo105LA(1) == '{') {
                match('{');
                mINTERNAL_RULE_REF = 14;
            }
        } else if (mINTERNAL_RULE_REF == 4) {
            mWS_LOOP(false);
            if (mo105LA(1) == '{') {
                match('{');
                mINTERNAL_RULE_REF = 23;
            }
        }
        if (!z || mINTERNAL_RULE_REF == -1) {
            token = null;
        } else {
            token = makeToken(mINTERNAL_RULE_REF);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        }
        this._returnToken = token;
    }

    public final void mSEMI(boolean z) {
        Token token;
        int length = this.text.length();
        match(';');
        if (z) {
            token = makeToken(16);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x004c  */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0068  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void mSL_COMMENT(boolean z) {
        Token token;
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
                    token = null;
                } else {
                    token = makeToken(54);
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

    public final void mSTAR(boolean z) {
        Token token;
        int length = this.text.length();
        match('*');
        if (z) {
            token = makeToken(46);
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
            token = makeToken(6);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x0019, code lost:
    
        switch(r3) {
            case 65: goto L22;
            case 66: goto L22;
            case 67: goto L22;
            case 68: goto L22;
            case 69: goto L22;
            case 70: goto L22;
            case 71: goto L22;
            case 72: goto L22;
            case 73: goto L22;
            case 74: goto L22;
            case 75: goto L22;
            case 76: goto L22;
            case 77: goto L22;
            case 78: goto L22;
            case 79: goto L22;
            case 80: goto L22;
            case 81: goto L22;
            case 82: goto L22;
            case 83: goto L22;
            case 84: goto L22;
            case 85: goto L22;
            case 86: goto L22;
            case 87: goto L22;
            case 88: goto L22;
            case 89: goto L22;
            case 90: goto L22;
            default: goto L8;
        };
     */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x001c, code lost:
    
        switch(r3) {
            case 97: goto L16;
            case 98: goto L16;
            case 99: goto L16;
            case 100: goto L16;
            case 101: goto L16;
            case 102: goto L16;
            case 103: goto L16;
            case 104: goto L16;
            case 105: goto L16;
            case 106: goto L16;
            case 107: goto L16;
            case 108: goto L16;
            case 109: goto L16;
            case 110: goto L16;
            case 111: goto L16;
            case 112: goto L16;
            case 113: goto L16;
            case 114: goto L16;
            case 115: goto L16;
            case 116: goto L16;
            case 117: goto L16;
            case 118: goto L16;
            case 119: goto L16;
            case 120: goto L16;
            case 121: goto L16;
            case 122: goto L16;
            default: goto L21;
        };
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x004c, code lost:
    
        r3 = 'a';
        r4 = 'z';
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x001f, code lost:
    
        r1 = testLiteralsTable(24);
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0025, code lost:
    
        if (r6 == false) goto L17;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x0028, code lost:
    
        if (r1 == (-1)) goto L17;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x002a, code lost:
    
        r6 = makeToken(r1);
        r6.setText(new java.lang.String(r5.text.getBuffer(), r0, r5.text.length() - r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0052, code lost:
    
        r5._returnToken = r6;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0054, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x0051, code lost:
    
        r6 = null;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void mTOKEN_REF(boolean z) {
        char c2;
        char c3;
        int length = this.text.length();
        while (true) {
            matchRange('A', 'Z');
            while (true) {
                char mo105LA = mo105LA(1);
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
                            c2 = '0';
                            c3 = '9';
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
        Token token;
        int length = this.text.length();
        match("#(");
        if (z) {
            token = makeToken(44);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    public final void mWILDCARD(boolean z) {
        Token token;
        int length = this.text.length();
        match('.');
        if (z) {
            token = makeToken(50);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    public final void mWS(boolean z) {
        this.text.length();
        char mo105LA = mo105LA(1);
        if (mo105LA != '\t') {
            if (mo105LA != '\n') {
                if (mo105LA == ' ') {
                    match(' ');
                } else if (mo105LA(1) == '\r' && mo105LA(2) == '\n') {
                    match('\r');
                } else {
                    if (mo105LA(1) != '\r') {
                        throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                    }
                    match('\r');
                    newline();
                }
            }
            match('\n');
            newline();
        } else {
            match('\t');
        }
        this._returnToken = null;
    }

    public final void mWS_LOOP(boolean z) {
        Token token;
        int length = this.text.length();
        while (true) {
            char mo105LA = mo105LA(1);
            if (mo105LA == '\t' || mo105LA == '\n' || mo105LA == '\r' || mo105LA == ' ') {
                mWS(false);
            } else if (mo105LA != '/') {
                break;
            } else {
                mCOMMENT(false);
            }
        }
        if (z) {
            token = makeToken(61);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    public final void mWS_OPT(boolean z) {
        Token token;
        int length = this.text.length();
        if (_tokenSet_5.member(mo105LA(1))) {
            mWS(false);
        }
        if (z) {
            token = makeToken(63);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
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
            token = makeToken(58);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    @Override // antlr.TokenStream
    public Token nextToken() {
        do {
            resetText();
            try {
                try {
                    char mo105LA = mo105LA(1);
                    if (mo105LA != '\t' && mo105LA != '\n' && mo105LA != '\r') {
                        if (mo105LA == '^') {
                            mCARET(true);
                        } else if (mo105LA == '>') {
                            mCLOSE_ELEMENT_OPTION(true);
                        } else if (mo105LA != '?') {
                            switch (mo105LA) {
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
                                    switch (mo105LA) {
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
                                            switch (mo105LA) {
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
                                                            mTOKEN_REF(true);
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
                                                                    if (mo105LA(1) != '=' || mo105LA(2) != '>') {
                                                                        if (mo105LA(1) == '.' && mo105LA(2) == '.') {
                                                                            mRANGE(true);
                                                                            break;
                                                                        } else if (mo105LA(1) == '=') {
                                                                            mASSIGN(true);
                                                                            break;
                                                                        } else if (mo105LA(1) == '.') {
                                                                            mWILDCARD(true);
                                                                            break;
                                                                        } else {
                                                                            if (mo105LA(1) != 65535) {
                                                                                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                                                                            }
                                                                            uponEOF();
                                                                            this._returnToken = makeToken(1);
                                                                            break;
                                                                        }
                                                                    } else {
                                                                        mIMPLIES(true);
                                                                        break;
                                                                    }
                                                                    break;
                                                            }
                                                    }
                                            }
                                    }
                            }
                        } else {
                            mQUESTION(true);
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
        this._returnToken.setType(this._returnToken.getType());
        return this._returnToken;
    }
}
