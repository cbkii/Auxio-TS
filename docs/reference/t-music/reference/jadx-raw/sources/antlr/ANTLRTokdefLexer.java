package antlr;

import antlr.collections.impl.BitSet;
import java.io.InputStream;
import java.io.Reader;
import java.util.Hashtable;
import tv.danmaku.ijk.media.player.IjkMediaMeta;

/* loaded from: classes3.dex */
public class ANTLRTokdefLexer extends CharScanner implements ANTLRTokdefParserTokenTypes, TokenStream {
    public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
    public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
    public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
    public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());

    public ANTLRTokdefLexer(InputBuffer inputBuffer) {
        this(new LexerSharedInputState(inputBuffer));
    }

    public ANTLRTokdefLexer(LexerSharedInputState lexerSharedInputState) {
        super(lexerSharedInputState);
        this.caseSensitiveLiterals = true;
        setCaseSensitive(true);
        this.literals = new Hashtable();
    }

    public ANTLRTokdefLexer(InputStream inputStream) {
        this(new ByteBuffer(inputStream));
    }

    public ANTLRTokdefLexer(Reader reader) {
        this(new CharBuffer(reader));
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
        jArr[0] = -140737488355336L;
        for (int i = 1; i <= 3; i++) {
            jArr[i] = -1;
        }
        return jArr;
    }

    public static final long[] mk_tokenSet_2() {
        long[] jArr = new long[8];
        jArr[0] = -4398046512136L;
        for (int i = 1; i <= 3; i++) {
            jArr[i] = -1;
        }
        return jArr;
    }

    public static final long[] mk_tokenSet_3() {
        long[] jArr = new long[8];
        jArr[0] = -17179869192L;
        jArr[1] = -268435457;
        for (int i = 2; i <= 3; i++) {
            jArr[i] = -1;
        }
        return jArr;
    }

    public final void mASSIGN(boolean z) {
        Token token;
        int length = this.text.length();
        match('=');
        if (z) {
            token = makeToken(6);
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
            token = makeToken(14);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    /* JADX WARN: Removed duplicated region for block: B:33:0x0135  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x0151  */
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
                    c2 = 'b';
                    if (mo105LA != 'b') {
                        c2 = 'f';
                        if (mo105LA != 'f') {
                            c2 = 'n';
                            if (mo105LA != 'n') {
                                c2 = 'r';
                                if (mo105LA != 'r') {
                                    c2 = 't';
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
                                            token = makeToken(13);
                                            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
                                        } else {
                                            token = null;
                                        }
                                        this._returnToken = token;
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

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Failed to find switch 'out' block (already processed)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.calcSwitchOut(SwitchRegionMaker.java:202)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:61)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:115)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:94)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeEndlessLoop(LoopRegionMaker.java:281)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:64)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:92)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeEndlessLoop(LoopRegionMaker.java:281)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:64)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:92)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.processFallThroughCases(SwitchRegionMaker.java:105)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:64)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:115)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:49)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:13:0x003c -> B:7:0x0027). Please report as a decompilation issue!!! */
    public final void mID(boolean r7) {
        /*
            r6 = this;
            antlr.ANTLRStringBuffer r0 = r6.text
            int r0 = r0.length()
            r1 = 1
            char r2 = r6.mo105LA(r1)
            switch(r2) {
                case 65: goto L60;
                case 66: goto L60;
                case 67: goto L60;
                case 68: goto L60;
                case 69: goto L60;
                case 70: goto L60;
                case 71: goto L60;
                case 72: goto L60;
                case 73: goto L60;
                case 74: goto L60;
                case 75: goto L60;
                case 76: goto L60;
                case 77: goto L60;
                case 78: goto L60;
                case 79: goto L60;
                case 80: goto L60;
                case 81: goto L60;
                case 82: goto L60;
                case 83: goto L60;
                case 84: goto L60;
                case 85: goto L60;
                case 86: goto L60;
                case 87: goto L60;
                case 88: goto L60;
                case 89: goto L60;
                case 90: goto L60;
                default: goto Le;
            }
        Le:
            switch(r2) {
                case 97: goto L27;
                case 98: goto L27;
                case 99: goto L27;
                case 100: goto L27;
                case 101: goto L27;
                case 102: goto L27;
                case 103: goto L27;
                case 104: goto L27;
                case 105: goto L27;
                case 106: goto L27;
                case 107: goto L27;
                case 108: goto L27;
                case 109: goto L27;
                case 110: goto L27;
                case 111: goto L27;
                case 112: goto L27;
                case 113: goto L27;
                case 114: goto L27;
                case 115: goto L27;
                case 116: goto L27;
                case 117: goto L27;
                case 118: goto L27;
                case 119: goto L27;
                case 120: goto L27;
                case 121: goto L27;
                case 122: goto L27;
                default: goto L11;
            }
        L11:
            antlr.NoViableAltForCharException r7 = new antlr.NoViableAltForCharException
            char r0 = r6.mo105LA(r1)
            java.lang.String r1 = r6.getFilename()
            int r2 = r6.getLine()
            int r6 = r6.getColumn()
            r7.<init>(r0, r1, r2, r6)
            throw r7
        L27:
            r2 = 97
            r3 = 122(0x7a, float:1.71E-43)
            goto L64
        L2c:
            r2 = 0
            r3 = 4
            char r4 = r6.mo105LA(r1)
            r5 = 95
            if (r4 == r5) goto L6b
            switch(r4) {
                case 48: goto L5b;
                case 49: goto L5b;
                case 50: goto L5b;
                case 51: goto L5b;
                case 52: goto L5b;
                case 53: goto L5b;
                case 54: goto L5b;
                case 55: goto L5b;
                case 56: goto L5b;
                case 57: goto L5b;
                default: goto L39;
            }
        L39:
            switch(r4) {
                case 65: goto L60;
                case 66: goto L60;
                case 67: goto L60;
                case 68: goto L60;
                case 69: goto L60;
                case 70: goto L60;
                case 71: goto L60;
                case 72: goto L60;
                case 73: goto L60;
                case 74: goto L60;
                case 75: goto L60;
                case 76: goto L60;
                case 77: goto L60;
                case 78: goto L60;
                case 79: goto L60;
                case 80: goto L60;
                case 81: goto L60;
                case 82: goto L60;
                case 83: goto L60;
                case 84: goto L60;
                case 85: goto L60;
                case 86: goto L60;
                case 87: goto L60;
                case 88: goto L60;
                case 89: goto L60;
                case 90: goto L60;
                default: goto L3c;
            }
        L3c:
            switch(r4) {
                case 97: goto L27;
                case 98: goto L27;
                case 99: goto L27;
                case 100: goto L27;
                case 101: goto L27;
                case 102: goto L27;
                case 103: goto L27;
                case 104: goto L27;
                case 105: goto L27;
                case 106: goto L27;
                case 107: goto L27;
                case 108: goto L27;
                case 109: goto L27;
                case 110: goto L27;
                case 111: goto L27;
                case 112: goto L27;
                case 113: goto L27;
                case 114: goto L27;
                case 115: goto L27;
                case 116: goto L27;
                case 117: goto L27;
                case 118: goto L27;
                case 119: goto L27;
                case 120: goto L27;
                case 121: goto L27;
                case 122: goto L27;
                default: goto L3f;
            }
        L3f:
            if (r7 == 0) goto L68
            antlr.Token r2 = r6.makeToken(r3)
            java.lang.String r7 = new java.lang.String
            antlr.ANTLRStringBuffer r1 = r6.text
            char[] r1 = r1.getBuffer()
            antlr.ANTLRStringBuffer r3 = r6.text
            int r3 = r3.length()
            int r3 = r3 - r0
            r7.<init>(r1, r0, r3)
            r2.setText(r7)
            goto L68
        L5b:
            r2 = 48
            r3 = 57
            goto L64
        L60:
            r2 = 65
            r3 = 90
        L64:
            r6.matchRange(r2, r3)
            goto L2c
        L68:
            r6._returnToken = r2
            return
        L6b:
            r6.match(r5)
            goto L2c
        */
        throw new UnsupportedOperationException("Method not decompiled: antlr.ANTLRTokdefLexer.mID(boolean):void");
    }

    public final void mINT(boolean z) {
        Token token;
        int length = this.text.length();
        int i = 0;
        while (mo105LA(1) >= '0' && mo105LA(1) <= '9') {
            mDIGIT(false);
            i++;
        }
        if (i < 1) {
            throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
        }
        if (z) {
            token = makeToken(9);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    public final void mLPAREN(boolean z) {
        Token token;
        int length = this.text.length();
        match('(');
        if (z) {
            token = makeToken(7);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    public final void mML_COMMENT(boolean z) {
        this.text.length();
        match("/*");
        while (true) {
            if (mo105LA(1) == '*' && _tokenSet_1.member(mo105LA(2))) {
                match('*');
                matchNot('/');
            } else if (mo105LA(1) == '\n') {
                match('\n');
                newline();
            } else {
                if (!_tokenSet_2.member(mo105LA(1))) {
                    match("*/");
                    this._returnToken = null;
                    return;
                }
                matchNot('*');
            }
        }
    }

    public final void mRPAREN(boolean z) {
        Token token;
        int length = this.text.length();
        match(')');
        if (z) {
            token = makeToken(8);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x0030, code lost:
    
        if (mo105LA(1) == '\n') goto L15;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void mSL_COMMENT(boolean z) {
        this.text.length();
        match("//");
        while (_tokenSet_0.member(mo105LA(1))) {
            match(_tokenSet_0);
        }
        char mo105LA = mo105LA(1);
        if (mo105LA != '\n') {
            if (mo105LA != '\r') {
                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
            }
            match('\r');
        }
        match('\n');
        newline();
        this._returnToken = null;
    }

    public final void mSTRING(boolean z) {
        Token token;
        int length = this.text.length();
        match('\"');
        while (true) {
            if (mo105LA(1) != '\\') {
                if (!_tokenSet_3.member(mo105LA(1))) {
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
            token = makeToken(5);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x0038, code lost:
    
        if (mo105LA(1) == '\n') goto L15;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void mWS(boolean z) {
        this.text.length();
        char mo105LA = mo105LA(1);
        char c2 = '\t';
        if (mo105LA != '\t') {
            if (mo105LA != '\n') {
                if (mo105LA != '\r') {
                    c2 = ' ';
                    if (mo105LA != ' ') {
                        throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                    }
                } else {
                    match('\r');
                }
            }
            match('\n');
            newline();
            this._returnToken = null;
        }
        match(c2);
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
            token = makeToken(15);
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
                    if (mo105LA == '\t' || mo105LA == '\n' || mo105LA == '\r' || mo105LA == ' ') {
                        mWS(true);
                    } else if (mo105LA == '\"') {
                        mSTRING(true);
                    } else if (mo105LA == '=') {
                        mASSIGN(true);
                    } else if (mo105LA == '(') {
                        mLPAREN(true);
                    } else if (mo105LA != ')') {
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
                                mINT(true);
                                break;
                            default:
                                switch (mo105LA) {
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
                                            default:
                                                if (mo105LA(1) != '/' || mo105LA(2) != '/') {
                                                    if (mo105LA(1) == '/' && mo105LA(2) == '*') {
                                                        mML_COMMENT(true);
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
                                                    mSL_COMMENT(true);
                                                    break;
                                                }
                                                break;
                                        }
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
                                        mID(true);
                                        break;
                                }
                        }
                    } else {
                        mRPAREN(true);
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
