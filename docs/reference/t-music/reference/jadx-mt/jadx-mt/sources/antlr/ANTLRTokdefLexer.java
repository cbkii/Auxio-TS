package antlr;

import antlr.collections.impl.BitSet;
import java.io.InputStream;
import java.io.Reader;
import java.util.Hashtable;
import tv.danmaku.ijk.media.player.IjkMediaMeta;

/* JADX INFO: loaded from: classes3.dex */
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
        Token tokenMakeToken;
        int length = this.text.length();
        match('=');
        if (z) {
            tokenMakeToken = makeToken(6);
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
            tokenMakeToken = makeToken(14);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    /* JADX WARN: Removed duplicated region for block: B:55:0x00d0  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x012c A[PHI: r1
      0x012c: PHI (r1v2 char) = (r1v0 char), (r1v3 char), (r1v4 char), (r1v5 char), (r1v6 char), (r1v7 char) binds: [B:6:0x0018, B:8:0x001c, B:10:0x0020, B:12:0x0024, B:14:0x0028, B:16:0x002c] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:72:0x0130 A[PHI: r4
      0x0130: PHI (r4v1 char) = (r4v0 char), (r4v2 char) binds: [B:3:0x0012, B:5:0x0016] A[DONT_GENERATE, DONT_INLINE]] */
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
                c2 = 'b';
                if (cMo105LA != 'b') {
                    c2 = 'f';
                    if (cMo105LA != 'f') {
                        c2 = 'n';
                        if (cMo105LA != 'n') {
                            c2 = 'r';
                            if (cMo105LA != 'r') {
                                c2 = 't';
                                if (cMo105LA == 't') {
                                    match(c2);
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
                            }
                        }
                    }
                }
            }
        }
        if (z) {
            tokenMakeToken = makeToken(13);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:12:0x003c -> B:7:0x0027). Please report as a decompilation issue!!! */
    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Failed to find switch 'out' block (already processed)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.calcSwitchOut(SwitchRegionMaker.java:217)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:68)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:112)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeEndlessLoop(LoopRegionMaker.java:282)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:65)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeEndlessLoop(LoopRegionMaker.java:282)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:65)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.addCases(SwitchRegionMaker.java:123)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:71)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:112)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    public final void mID(boolean r7) throws antlr.NoViableAltForCharException {
        /*
            Method dump skipped, instruction units count: 360
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: antlr.ANTLRTokdefLexer.mID(boolean):void");
    }

    public final void mINT(boolean z) throws NoViableAltForCharException {
        Token tokenMakeToken;
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
            tokenMakeToken = makeToken(9);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    public final void mLPAREN(boolean z) {
        Token tokenMakeToken;
        int length = this.text.length();
        match('(');
        if (z) {
            tokenMakeToken = makeToken(7);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    public final void mML_COMMENT(boolean z) throws MismatchedCharException {
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
        Token tokenMakeToken;
        int length = this.text.length();
        match(')');
        if (z) {
            tokenMakeToken = makeToken(8);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x0049  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void mSL_COMMENT(boolean z) throws NoViableAltForCharException, MismatchedCharException {
        this.text.length();
        match("//");
        while (_tokenSet_0.member(mo105LA(1))) {
            match(_tokenSet_0);
        }
        char cMo105LA = mo105LA(1);
        if (cMo105LA == '\n') {
            match('\n');
        } else {
            if (cMo105LA != '\r') {
                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
            }
            match('\r');
            if (mo105LA(1) == '\n') {
            }
        }
        newline();
        this._returnToken = null;
    }

    public final void mSTRING(boolean z) throws NoViableAltForCharException, MismatchedCharException {
        Token tokenMakeToken;
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
            tokenMakeToken = makeToken(5);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x003a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void mWS(boolean z) throws NoViableAltForCharException {
        this.text.length();
        char cMo105LA = mo105LA(1);
        char c2 = '\t';
        if (cMo105LA == '\t') {
            match(c2);
        } else if (cMo105LA == '\n') {
            match('\n');
            newline();
        } else if (cMo105LA != '\r') {
            c2 = ' ';
            if (cMo105LA != ' ') {
                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
            }
            match(c2);
        } else {
            match('\r');
            if (mo105LA(1) == '\n') {
            }
            newline();
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
            tokenMakeToken = makeToken(15);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    @Override // antlr.TokenStream
    public Token nextToken() throws TokenStreamException {
        do {
            resetText();
            try {
                try {
                    char cMo105LA = mo105LA(1);
                    if (cMo105LA == '\t' || cMo105LA == '\n' || cMo105LA == '\r' || cMo105LA == ' ') {
                        mWS(true);
                    } else if (cMo105LA == '\"') {
                        mSTRING(true);
                    } else if (cMo105LA == '=') {
                        mASSIGN(true);
                    } else if (cMo105LA == '(') {
                        mLPAREN(true);
                    } else if (cMo105LA != ')') {
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
                                mINT(true);
                                break;
                            default:
                                switch (cMo105LA) {
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
                                                if (mo105LA(1) == '/' && mo105LA(2) == '/') {
                                                    mSL_COMMENT(true);
                                                } else if (mo105LA(1) == '/' && mo105LA(2) == '*') {
                                                    mML_COMMENT(true);
                                                } else {
                                                    if (mo105LA(1) != 65535) {
                                                        throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                                                    }
                                                    uponEOF();
                                                    this._returnToken = makeToken(1);
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
                                break;
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
