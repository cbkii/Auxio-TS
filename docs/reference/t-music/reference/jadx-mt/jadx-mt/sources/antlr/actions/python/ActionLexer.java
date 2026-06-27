package antlr.actions.python;

import antlr.ANTLRStringBuffer;
import antlr.ActionTransInfo;
import antlr.ByteBuffer;
import antlr.CharBuffer;
import antlr.CharScanner;
import antlr.CharStreamException;
import antlr.CharStreamIOException;
import antlr.CodeGenerator;
import antlr.InputBuffer;
import antlr.LexerSharedInputState;
import antlr.MismatchedCharException;
import antlr.NoViableAltForCharException;
import antlr.RecognitionException;
import antlr.RuleBlock;
import antlr.Token;
import antlr.TokenStream;
import antlr.TokenStreamException;
import antlr.TokenStreamIOException;
import antlr.TokenStreamRecognitionException;
import antlr.Tool;
import antlr.collections.impl.BitSet;
import antlr.collections.impl.Vector;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.Hashtable;
import p000a.p001a.p002a.p003a.C0000a;
import tv.danmaku.ijk.media.player.IjkMediaMeta;

/* JADX INFO: loaded from: classes3.dex */
public class ActionLexer extends CharScanner implements ActionLexerTokenTypes, TokenStream {
    public Tool antlrTool;
    public RuleBlock currentRule;
    public CodeGenerator generator;
    public int lineOffset;
    public ActionTransInfo transInfo;
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
    public static final BitSet _tokenSet_11 = new BitSet(mk_tokenSet_11());
    public static final BitSet _tokenSet_12 = new BitSet(mk_tokenSet_12());
    public static final BitSet _tokenSet_13 = new BitSet(mk_tokenSet_13());
    public static final BitSet _tokenSet_14 = new BitSet(mk_tokenSet_14());
    public static final BitSet _tokenSet_15 = new BitSet(mk_tokenSet_15());
    public static final BitSet _tokenSet_16 = new BitSet(mk_tokenSet_16());
    public static final BitSet _tokenSet_17 = new BitSet(mk_tokenSet_17());
    public static final BitSet _tokenSet_18 = new BitSet(mk_tokenSet_18());
    public static final BitSet _tokenSet_19 = new BitSet(mk_tokenSet_19());
    public static final BitSet _tokenSet_20 = new BitSet(mk_tokenSet_20());
    public static final BitSet _tokenSet_21 = new BitSet(mk_tokenSet_21());
    public static final BitSet _tokenSet_22 = new BitSet(mk_tokenSet_22());
    public static final BitSet _tokenSet_23 = new BitSet(mk_tokenSet_23());
    public static final BitSet _tokenSet_24 = new BitSet(mk_tokenSet_24());
    public static final BitSet _tokenSet_25 = new BitSet(mk_tokenSet_25());

    public ActionLexer(InputBuffer inputBuffer) {
        this(new LexerSharedInputState(inputBuffer));
    }

    public ActionLexer(LexerSharedInputState lexerSharedInputState) {
        super(lexerSharedInputState);
        this.lineOffset = 0;
        this.caseSensitiveLiterals = true;
        setCaseSensitive(true);
        this.literals = new Hashtable();
    }

    public ActionLexer(InputStream inputStream) {
        this(new ByteBuffer(inputStream));
    }

    public ActionLexer(Reader reader) {
        this(new CharBuffer(reader));
    }

    public ActionLexer(String str, RuleBlock ruleBlock, CodeGenerator codeGenerator, ActionTransInfo actionTransInfo) {
        this(new StringReader(str));
        this.currentRule = ruleBlock;
        this.generator = codeGenerator;
        this.transInfo = actionTransInfo;
    }

    public static final long[] mk_tokenSet_0() {
        long[] jArr = new long[8];
        jArr[0] = -103079215112L;
        for (int i = 1; i <= 3; i++) {
            jArr[i] = -1;
        }
        return jArr;
    }

    public static final long[] mk_tokenSet_1() {
        long[] jArr = new long[8];
        jArr[0] = -145135534866440L;
        for (int i = 1; i <= 3; i++) {
            jArr[i] = -1;
        }
        return jArr;
    }

    public static final long[] mk_tokenSet_10() {
        return new long[]{287950056521213440L, 576460746129407998L, 0, 0, 0};
    }

    public static final long[] mk_tokenSet_11() {
        return new long[]{287958332923183104L, 576460745995190270L, 0, 0, 0};
    }

    public static final long[] mk_tokenSet_12() {
        return new long[]{287978128427460096L, 576460746532061182L, 0, 0, 0};
    }

    public static final long[] mk_tokenSet_13() {
        return new long[]{2306123388973753856L, 671088640, 0, 0, 0};
    }

    public static final long[] mk_tokenSet_14() {
        return new long[]{287952805300282880L, 576460746129407998L, 0, 0, 0};
    }

    public static final long[] mk_tokenSet_15() {
        return new long[]{2305843013508670976L, 0, 0, 0, 0};
    }

    public static final long[] mk_tokenSet_16() {
        return new long[]{2306051920717948416L, IjkMediaMeta.AV_CH_STEREO_LEFT, 0, 0, 0};
    }

    public static final long[] mk_tokenSet_17() {
        return new long[]{208911504254464L, IjkMediaMeta.AV_CH_STEREO_LEFT, 0, 0, 0};
    }

    public static final long[] mk_tokenSet_18() {
        return new long[]{1151051235328L, 576460746129407998L, 0, 0, 0};
    }

    public static final long[] mk_tokenSet_19() {
        return new long[]{189120294954496L, 0, 0, 0, 0};
    }

    public static final long[] mk_tokenSet_2() {
        long[] jArr = new long[8];
        jArr[0] = -141407503262728L;
        for (int i = 1; i <= 3; i++) {
            jArr[i] = -1;
        }
        return jArr;
    }

    public static final long[] mk_tokenSet_20() {
        return new long[]{288139722277004800L, 576460746129407998L, 0, 0, 0};
    }

    public static final long[] mk_tokenSet_21() {
        return new long[]{288049596683265536L, 576460746666278910L, 0, 0, 0};
    }

    public static final long[] mk_tokenSet_22() {
        return new long[]{287960536241415680L, 576460745995190270L, 0, 0, 0};
    }

    public static final long[] mk_tokenSet_23() {
        return new long[]{287958337218160128L, 576460745995190270L, 0, 0, 0};
    }

    public static final long[] mk_tokenSet_24() {
        return new long[]{288228817078593024L, 576460746532061182L, 0, 0, 0};
    }

    public static final long[] mk_tokenSet_25() {
        return new long[]{288158448334415360L, 576460746532061182L, 0, 0, 0};
    }

    public static final long[] mk_tokenSet_3() {
        return new long[]{0, 576460745995190270L, 0, 0, 0};
    }

    public static final long[] mk_tokenSet_4() {
        return new long[]{4294977024L, 0, 0, 0, 0};
    }

    public static final long[] mk_tokenSet_5() {
        return new long[]{1103806604800L, 0, 0, 0, 0};
    }

    public static final long[] mk_tokenSet_6() {
        return new long[]{287959436729787904L, 576460745995190270L, 0, 0, 0};
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
        return new long[]{287948901175001088L, 576460745995190270L, 0, 0, 0};
    }

    public final void mACTION(boolean z) throws NoViableAltForCharException, MismatchedCharException {
        Token tokenMakeToken;
        int length = this.text.length();
        int i = 0;
        while (true) {
            char cMo105LA = mo105LA(1);
            if (cMo105LA == '#') {
                mAST_ITEM(false);
            } else if (cMo105LA == '$') {
                mTEXT_ITEM(false);
            } else if (!_tokenSet_0.member(mo105LA(1))) {
                break;
            } else {
                mSTUFF(false);
            }
            i++;
        }
        if (i < 1) {
            throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
        }
        if (z) {
            tokenMakeToken = makeToken(4);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    public final void mARG(boolean z) throws NoViableAltForCharException, MismatchedCharException {
        Token tokenMakeToken;
        int length = this.text.length();
        char cMo105LA = mo105LA(1);
        char c2 = 255;
        if (cMo105LA != '\'') {
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
                    mINT_OR_FLOAT(false);
                    break;
                default:
                    if (_tokenSet_18.member(mo105LA(1)) && mo105LA(2) >= 3 && mo105LA(2) <= 255 && mo105LA(3) >= 3 && mo105LA(3) <= 255) {
                        mTREE_ELEMENT(false);
                    } else {
                        if (mo105LA(1) != '\"' || mo105LA(2) < 3 || mo105LA(2) > 255 || mo105LA(3) < 3 || mo105LA(3) > 255) {
                            throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                        }
                        mSTRING(false);
                    }
                    break;
            }
        } else {
            mCHAR(false);
        }
        for (int i = 2; _tokenSet_19.member(mo105LA(1)) && _tokenSet_20.member(mo105LA(i)) && mo105LA(3) >= 3 && mo105LA(3) <= c2; i = 2) {
            char cMo105LA2 = mo105LA(1);
            if (cMo105LA2 == '\t' || cMo105LA2 == '\n' || cMo105LA2 == '\r' || cMo105LA2 == ' ') {
                mWS(false);
            } else if (cMo105LA2 != '-' && cMo105LA2 != '/' && cMo105LA2 != '*' && cMo105LA2 != '+') {
                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
            }
            char cMo105LA3 = mo105LA(1);
            if (cMo105LA3 == '*') {
                match('*');
            } else if (cMo105LA3 == '+') {
                match('+');
            } else if (cMo105LA3 == '-') {
                match('-');
            } else {
                if (cMo105LA3 != '/') {
                    throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                }
                match('/');
            }
            char cMo105LA4 = mo105LA(1);
            if (cMo105LA4 == '\t' || cMo105LA4 == '\n' || cMo105LA4 == '\r' || cMo105LA4 == ' ') {
                mWS(false);
            } else if (cMo105LA4 != '_' && cMo105LA4 != '\"' && cMo105LA4 != '#' && cMo105LA4 != '\'' && cMo105LA4 != '(') {
                switch (cMo105LA4) {
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
                        continue;
                    default:
                        switch (cMo105LA4) {
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
                            case '[':
                                continue;
                            default:
                                switch (cMo105LA4) {
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
                                break;
                        }
                        break;
                }
            }
            mARG(false);
            c2 = 255;
        }
        if (z) {
            tokenMakeToken = makeToken(16);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    /* JADX WARN: Removed duplicated region for block: B:104:0x023e  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x0125 A[FALL_THROUGH] */
    /* JADX WARN: Removed duplicated region for block: B:62:0x015e  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x0207 A[FALL_THROUGH] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void mAST_CONSTRUCTOR(boolean z) throws NoViableAltForCharException, MismatchedCharException {
        Token token;
        Token token2;
        char cMo105LA;
        Token tokenMakeToken;
        char cMo105LA2;
        int length = this.text.length();
        int length2 = this.text.length();
        match('[');
        this.text.setLength(length2);
        char cMo105LA3 = mo105LA(1);
        if (cMo105LA3 != '\t' && cMo105LA3 != '\n' && cMo105LA3 != '\r' && cMo105LA3 != ' ') {
            if (cMo105LA3 != '(' && cMo105LA3 != '_' && cMo105LA3 != '\"' && cMo105LA3 != '#') {
                switch (cMo105LA3) {
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
                        switch (cMo105LA3) {
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
                            case '[':
                                break;
                            default:
                                switch (cMo105LA3) {
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
                                break;
                        }
                        break;
                }
            }
        } else {
            int length3 = this.text.length();
            mWS(false);
            this.text.setLength(length3);
        }
        int length4 = this.text.length();
        mAST_CTOR_ELEMENT(true);
        this.text.setLength(length4);
        Token token3 = this._returnToken;
        char cMo105LA4 = mo105LA(1);
        if (cMo105LA4 == '\t' || cMo105LA4 == '\n' || cMo105LA4 == '\r' || cMo105LA4 == ' ') {
            int length5 = this.text.length();
            mWS(false);
            this.text.setLength(length5);
        } else if (cMo105LA4 != ',' && cMo105LA4 != ']') {
            throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
        }
        if (mo105LA(1) == ',' && _tokenSet_10.member(mo105LA(2)) && mo105LA(3) >= 3 && mo105LA(3) <= 255) {
            int length6 = this.text.length();
            match(',');
            this.text.setLength(length6);
            char cMo105LA5 = mo105LA(1);
            if (cMo105LA5 != '\t' && cMo105LA5 != '\n' && cMo105LA5 != '\r' && cMo105LA5 != ' ') {
                if (cMo105LA5 != '(' && cMo105LA5 != '_' && cMo105LA5 != '\"' && cMo105LA5 != '#') {
                    switch (cMo105LA5) {
                        default:
                            switch (cMo105LA5) {
                                default:
                                    switch (cMo105LA5) {
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
                                case '[':
                                    int length7 = this.text.length();
                                    mAST_CTOR_ELEMENT(true);
                                    this.text.setLength(length7);
                                    token = this._returnToken;
                                    cMo105LA2 = mo105LA(1);
                                    if (cMo105LA2 != '\t' || cMo105LA2 == '\n' || cMo105LA2 == '\r' || cMo105LA2 == ' ') {
                                        int length8 = this.text.length();
                                        mWS(false);
                                        this.text.setLength(length8);
                                    } else if (cMo105LA2 != ',' && cMo105LA2 != ']') {
                                        throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                                    }
                                    break;
                            }
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
                    }
                }
            } else {
                int length9 = this.text.length();
                mWS(false);
                this.text.setLength(length9);
            }
            int length72 = this.text.length();
            mAST_CTOR_ELEMENT(true);
            this.text.setLength(length72);
            token = this._returnToken;
            cMo105LA2 = mo105LA(1);
            if (cMo105LA2 != '\t') {
                int length82 = this.text.length();
                mWS(false);
                this.text.setLength(length82);
            }
        } else {
            if (mo105LA(1) != ',' && mo105LA(1) != ']') {
                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
            }
            token = null;
        }
        char cMo105LA6 = mo105LA(1);
        if (cMo105LA6 == ',') {
            int length10 = this.text.length();
            match(',');
            this.text.setLength(length10);
            char cMo105LA7 = mo105LA(1);
            if (cMo105LA7 != '\t' && cMo105LA7 != '\n' && cMo105LA7 != '\r' && cMo105LA7 != ' ') {
                if (cMo105LA7 != '(' && cMo105LA7 != '_' && cMo105LA7 != '\"' && cMo105LA7 != '#') {
                    switch (cMo105LA7) {
                        default:
                            switch (cMo105LA7) {
                                default:
                                    switch (cMo105LA7) {
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
                                case '[':
                                    int length11 = this.text.length();
                                    mAST_CTOR_ELEMENT(true);
                                    this.text.setLength(length11);
                                    token2 = this._returnToken;
                                    cMo105LA = mo105LA(1);
                                    if (cMo105LA != '\t' || cMo105LA == '\n' || cMo105LA == '\r' || cMo105LA == ' ') {
                                        int length12 = this.text.length();
                                        mWS(false);
                                        this.text.setLength(length12);
                                    } else if (cMo105LA != ']') {
                                        throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                                    }
                                    break;
                            }
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
                    }
                }
            } else {
                int length13 = this.text.length();
                mWS(false);
                this.text.setLength(length13);
            }
            int length112 = this.text.length();
            mAST_CTOR_ELEMENT(true);
            this.text.setLength(length112);
            token2 = this._returnToken;
            cMo105LA = mo105LA(1);
            if (cMo105LA != '\t') {
                int length122 = this.text.length();
                mWS(false);
                this.text.setLength(length122);
            }
        } else {
            if (cMo105LA6 != ']') {
                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
            }
            token2 = null;
        }
        int length14 = this.text.length();
        match(']');
        this.text.setLength(length14);
        String text = token3.getText();
        if (token != null) {
            text = C0000a.m0a(token, C0000a.m9b(text, ","));
        }
        if (token2 != null) {
            text = C0000a.m0a(token2, C0000a.m9b(text, ","));
        }
        this.text.setLength(length);
        this.text.append(this.generator.getASTCreateString(null, text));
        if (z) {
            tokenMakeToken = makeToken(10);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    public final void mAST_CTOR_ELEMENT(boolean z) throws NoViableAltForCharException, MismatchedCharException {
        Token tokenMakeToken;
        int length = this.text.length();
        if (mo105LA(1) == '\"' && mo105LA(2) >= 3 && mo105LA(2) <= 255 && mo105LA(3) >= 3 && mo105LA(3) <= 255) {
            mSTRING(false);
        } else if (_tokenSet_18.member(mo105LA(1)) && mo105LA(2) >= 3 && mo105LA(2) <= 255) {
            mTREE_ELEMENT(false);
        } else {
            if (mo105LA(1) < '0' || mo105LA(1) > '9') {
                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
            }
            mINT(false);
        }
        if (z) {
            tokenMakeToken = makeToken(11);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    /* JADX WARN: Removed duplicated region for block: B:37:0x00ee  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void mAST_ITEM(boolean z) throws NoViableAltForCharException, MismatchedCharException {
        Token tokenMakeToken;
        int length = this.text.length();
        if (mo105LA(1) == '#' && mo105LA(2) == '(') {
            int length2 = this.text.length();
            match('#');
            this.text.setLength(length2);
            mTREE(true);
        } else if (mo105LA(1) == '#' && _tokenSet_3.member(mo105LA(2))) {
            int length3 = this.text.length();
            match('#');
            this.text.setLength(length3);
            mID(true);
            String strMapTreeId = this.generator.mapTreeId(this._returnToken.getText(), this.transInfo);
            if (strMapTreeId != null) {
                this.text.setLength(length);
                this.text.append(strMapTreeId);
            }
            if (_tokenSet_4.member(mo105LA(1))) {
                mWS(false);
            }
            if (mo105LA(1) == '=') {
            }
        } else if (mo105LA(1) == '#' && mo105LA(2) == '[') {
            int length4 = this.text.length();
            match('#');
            this.text.setLength(length4);
            mAST_CONSTRUCTOR(true);
        } else {
            if (mo105LA(1) != '#' || mo105LA(2) != '#') {
                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
            }
            match("##");
            String str = this.currentRule.getRuleName() + "_AST";
            this.text.setLength(length);
            this.text.append(str);
            ActionTransInfo actionTransInfo = this.transInfo;
            if (actionTransInfo != null) {
                actionTransInfo.refRuleRoot = str;
            }
            if (_tokenSet_4.member(mo105LA(1))) {
                mWS(false);
            }
            if (mo105LA(1) == '=') {
                mVAR_ASSIGN(false);
            }
        }
        if (z) {
            tokenMakeToken = makeToken(6);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    public final void mCHAR(boolean z) throws NoViableAltForCharException, MismatchedCharException {
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
            tokenMakeToken = makeToken(23);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    public final void mCOMMENT(boolean z) throws NoViableAltForCharException, MismatchedCharException {
        Token tokenMakeToken;
        int length = this.text.length();
        if (mo105LA(1) == '/' && mo105LA(2) == '/') {
            mSL_COMMENT(false);
        } else {
            if (mo105LA(1) != '/' || mo105LA(2) != '*') {
                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
            }
            mML_COMMENT(false);
        }
        if (z) {
            tokenMakeToken = makeToken(19);
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
            tokenMakeToken = makeToken(26);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    /* JADX WARN: Removed duplicated region for block: B:53:0x00cc  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x0116 A[PHI: r1
      0x0116: PHI (r1v2 char) = (r1v0 char), (r1v3 char), (r1v4 char), (r1v5 char), (r1v6 char), (r1v7 char) binds: [B:6:0x0018, B:8:0x001c, B:10:0x0020, B:12:0x0024, B:14:0x0028, B:16:0x002c] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:69:0x011a A[PHI: r4
      0x011a: PHI (r4v1 char) = (r4v0 char), (r4v2 char) binds: [B:3:0x0012, B:5:0x0016] A[DONT_GENERATE, DONT_INLINE]] */
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
                                if (cMo105LA != 't') {
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
                                    match(c2);
                                }
                            }
                        }
                    }
                }
            }
        }
        if (z) {
            tokenMakeToken = makeToken(25);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x003c  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x006f A[SYNTHETIC] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:13:0x0045 -> B:6:0x0026). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:17:0x0061 -> B:7:0x002a). Please report as a decompilation issue!!! */
    /*  JADX ERROR: JadxOverflowException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxOverflowException: Regions count limit reached
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:59)
        	at jadx.core.utils.ErrorsCounter.error(ErrorsCounter.java:31)
        	at jadx.core.dex.attributes.nodes.NotificationAttrNode.addError(NotificationAttrNode.java:19)
        */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void mID(boolean r8) throws antlr.NoViableAltForCharException {
        /*
            Method dump skipped, instruction units count: 398
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: antlr.actions.python.ActionLexer.mID(boolean):void");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:142:0x02cc  */
    /* JADX WARN: Removed duplicated region for block: B:149:0x02fa  */
    /* JADX WARN: Removed duplicated region for block: B:177:? A[FALL_THROUGH, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:177:? A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:89:0x01c6  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final boolean mID_ELEMENT(boolean z) throws NoViableAltForCharException, MismatchedCharException {
        ActionTransInfo actionTransInfo;
        Token tokenMakeToken;
        int length = this.text.length();
        boolean z2 = true;
        mID(true);
        Token token = this._returnToken;
        if (_tokenSet_4.member(mo105LA(1)) && _tokenSet_13.member(mo105LA(2))) {
            int length2 = this.text.length();
            mWS(false);
            this.text.setLength(length2);
        } else if (!_tokenSet_13.member(mo105LA(1))) {
            throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
        }
        char cMo105LA = mo105LA(1);
        if (cMo105LA == '\t' || cMo105LA == '\n' || cMo105LA == '\r' || cMo105LA == ' ' || cMo105LA == '=') {
            String strMapTreeId = this.generator.mapTreeId(token.getText(), this.transInfo);
            this.text.setLength(length);
            this.text.append(strMapTreeId);
            if (!_tokenSet_15.member(mo105LA(1)) && _tokenSet_16.member(mo105LA(2)) && (actionTransInfo = this.transInfo) != null && actionTransInfo.refRuleRoot != null) {
                char cMo105LA2 = mo105LA(1);
                if (cMo105LA2 == '\t' || cMo105LA2 == '\n' || cMo105LA2 == '\r' || cMo105LA2 == ' ') {
                    mWS(false);
                } else if (cMo105LA2 != '=') {
                    throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                }
                mVAR_ASSIGN(false);
            } else if (!_tokenSet_17.member(mo105LA(1))) {
                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
            }
        } else {
            if (cMo105LA != '[') {
                if (cMo105LA != ']') {
                    switch (cMo105LA) {
                        case '(':
                            match('(');
                            if (_tokenSet_4.member(mo105LA(1)) && _tokenSet_14.member(mo105LA(2)) && mo105LA(3) >= 3 && mo105LA(3) <= 255) {
                                int length3 = this.text.length();
                                mWS(false);
                                this.text.setLength(length3);
                            } else if (!_tokenSet_14.member(mo105LA(1)) || mo105LA(2) < 3 || mo105LA(2) > 255) {
                                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                            }
                            char cMo105LA3 = mo105LA(1);
                            if (cMo105LA3 != '\t' && cMo105LA3 != '\n' && cMo105LA3 != '\r' && cMo105LA3 != ' ') {
                                if (cMo105LA3 != '_' && cMo105LA3 != '\"' && cMo105LA3 != '#') {
                                    switch (cMo105LA3) {
                                        case '\'':
                                        case '(':
                                            break;
                                        case ')':
                                            break;
                                        default:
                                            switch (cMo105LA3) {
                                                default:
                                                    switch (cMo105LA3) {
                                                        default:
                                                            switch (cMo105LA3) {
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
                                                        case '[':
                                                            break;
                                                    }
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
                                            }
                                            break;
                                    }
                                } else {
                                    while (true) {
                                        mARG(false);
                                        if (mo105LA(1) == ',') {
                                            match(',');
                                            char cMo105LA4 = mo105LA(1);
                                            if (cMo105LA4 != '\t' && cMo105LA4 != '\n' && cMo105LA4 != '\r' && cMo105LA4 != ' ') {
                                                if (cMo105LA4 != '_' && cMo105LA4 != '\"' && cMo105LA4 != '#' && cMo105LA4 != '\'' && cMo105LA4 != '(') {
                                                    switch (cMo105LA4) {
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
                                                            continue;
                                                        default:
                                                            switch (cMo105LA4) {
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
                                                                case '[':
                                                                    continue;
                                                                default:
                                                                    switch (cMo105LA4) {
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
                                                                    break;
                                                            }
                                                            break;
                                                    }
                                                }
                                            } else {
                                                int length4 = this.text.length();
                                                mWS(false);
                                                this.text.setLength(length4);
                                            }
                                        }
                                    }
                                }
                            }
                            char cMo105LA5 = mo105LA(1);
                            if (cMo105LA5 == '\t' || cMo105LA5 == '\n' || cMo105LA5 == '\r' || cMo105LA5 == ' ') {
                                int length5 = this.text.length();
                                mWS(false);
                                this.text.setLength(length5);
                            } else if (cMo105LA5 != ')') {
                                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                            }
                            match(')');
                            break;
                        case ')':
                        case '*':
                        case '+':
                        case ',':
                        case '-':
                        case '/':
                            break;
                        case '.':
                            match('.');
                            mID_ELEMENT(false);
                            break;
                        default:
                            throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                    }
                }
                String strMapTreeId2 = this.generator.mapTreeId(token.getText(), this.transInfo);
                this.text.setLength(length);
                this.text.append(strMapTreeId2);
                if (!_tokenSet_15.member(mo105LA(1))) {
                    if (!_tokenSet_17.member(mo105LA(1))) {
                    }
                }
            } else {
                int i = 0;
                while (mo105LA(1) == '[') {
                    match('[');
                    char cMo105LA6 = mo105LA(1);
                    if (cMo105LA6 != '\t' && cMo105LA6 != '\n' && cMo105LA6 != '\r' && cMo105LA6 != ' ') {
                        if (cMo105LA6 != '_' && cMo105LA6 != '\"' && cMo105LA6 != '#' && cMo105LA6 != '\'' && cMo105LA6 != '(') {
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
                                    switch (cMo105LA6) {
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
                                        case '[':
                                            break;
                                        default:
                                            switch (cMo105LA6) {
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
                                            while (mo105LA(1) == '[') {
                                            }
                                            break;
                                    }
                                    break;
                            }
                        }
                    } else {
                        int length6 = this.text.length();
                        mWS(false);
                        this.text.setLength(length6);
                    }
                    mARG(false);
                    char cMo105LA7 = mo105LA(1);
                    if (cMo105LA7 == '\t' || cMo105LA7 == '\n' || cMo105LA7 == '\r' || cMo105LA7 == ' ') {
                        int length7 = this.text.length();
                        mWS(false);
                        this.text.setLength(length7);
                    } else if (cMo105LA7 != ']') {
                        throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                    }
                    match(']');
                    i++;
                }
                if (i < 1) {
                    throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                }
            }
            z2 = false;
        }
        if (z) {
            tokenMakeToken = makeToken(12);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
        return z2;
    }

    public final void mIGNWS(boolean z) {
        Token tokenMakeToken;
        int length = this.text.length();
        while (true) {
            if (mo105LA(1) == ' ' && mo105LA(2) >= 3 && mo105LA(2) <= 255 && mo105LA(3) >= 3 && mo105LA(3) <= 255) {
                match(' ');
            } else if (mo105LA(1) != '\t' || mo105LA(2) < 3 || mo105LA(2) > 255 || mo105LA(3) < 3 || mo105LA(3) > 255) {
                break;
            } else {
                match('\t');
            }
        }
        if (z) {
            tokenMakeToken = makeToken(21);
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
            mDIGIT(false);
            i++;
        }
        if (i < 1) {
            throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
        }
        if (z) {
            tokenMakeToken = makeToken(27);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x0042 A[PHI: r4
      0x0042: PHI (r4v5 char) = (r4v2 char), (r4v1 char) binds: [B:19:0x0058, B:14:0x0040] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void mINT_OR_FLOAT(boolean z) throws NoViableAltForCharException {
        Token tokenMakeToken;
        int length = this.text.length();
        int i = 0;
        while (mo105LA(1) >= '0' && mo105LA(1) <= '9' && _tokenSet_24.member(mo105LA(2))) {
            mDIGIT(false);
            i++;
        }
        if (i < 1) {
            throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
        }
        char c2 = 'L';
        if (mo105LA(1) != 'L' || !_tokenSet_25.member(mo105LA(2))) {
            c2 = 'l';
            if (mo105LA(1) == 'l' && _tokenSet_25.member(mo105LA(2))) {
                match(c2);
            } else if (mo105LA(1) == '.') {
                match('.');
                while (mo105LA(1) >= '0' && mo105LA(1) <= '9' && _tokenSet_25.member(mo105LA(2))) {
                    mDIGIT(false);
                }
            } else if (!_tokenSet_25.member(mo105LA(1))) {
                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
            }
        }
        if (z) {
            tokenMakeToken = makeToken(28);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    public final void mML_COMMENT(boolean z) throws MismatchedCharException {
        Token tokenMakeToken;
        int length = this.text.length();
        match("/*");
        this.text.setLength(length);
        this.text.append("#");
        while (true) {
            if (mo105LA(1) == '*' && mo105LA(2) == '/') {
                break;
            }
            if (mo105LA(1) == '\r' && mo105LA(2) == '\n' && mo105LA(3) >= 3 && mo105LA(3) <= 255) {
                match('\r');
            } else if (mo105LA(1) == '\r' && mo105LA(2) >= 3 && mo105LA(2) <= 255 && mo105LA(3) >= 3 && mo105LA(3) <= 255) {
                match('\r');
                int length2 = this.text.length();
                mIGNWS(false);
                this.text.setLength(length2);
                newline();
                this.text.append("# ");
            } else if (mo105LA(1) != '\n' || mo105LA(2) < 3 || mo105LA(2) > 255 || mo105LA(3) < 3 || mo105LA(3) > 255) {
                if (mo105LA(1) < 3 || mo105LA(1) > 255 || mo105LA(2) < 3 || mo105LA(2) > 255 || mo105LA(3) < 3 || mo105LA(3) > 255) {
                    break;
                } else {
                    matchNot(CharScanner.EOF_CHAR);
                }
            }
            match('\n');
            int length22 = this.text.length();
            mIGNWS(false);
            this.text.setLength(length22);
            newline();
            this.text.append("# ");
        }
        this.text.append("\n");
        int length3 = this.text.length();
        match("*/");
        this.text.setLength(length3);
        if (z) {
            tokenMakeToken = makeToken(22);
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
        this.text.setLength(length);
        this.text.append("#");
        while (mo105LA(1) != '\n' && mo105LA(1) != '\r' && mo105LA(1) >= 3 && mo105LA(1) <= 255 && mo105LA(2) >= 3 && mo105LA(2) <= 255) {
            matchNot(CharScanner.EOF_CHAR);
        }
        if (mo105LA(1) == '\r' && mo105LA(2) == '\n') {
            match("\r\n");
        } else if (mo105LA(1) == '\n') {
            match('\n');
        } else {
            if (mo105LA(1) != '\r') {
                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
            }
            match('\r');
        }
        newline();
        if (z) {
            tokenMakeToken = makeToken(20);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    public final void mSTRING(boolean z) throws NoViableAltForCharException, MismatchedCharException {
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
            tokenMakeToken = makeToken(24);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    /* JADX WARN: Removed duplicated region for block: B:39:0x00a2  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00bd  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void mSTUFF(boolean z) throws NoViableAltForCharException, MismatchedCharException {
        Token tokenMakeToken;
        BitSet bitSet;
        int length = this.text.length();
        char cMo105LA = mo105LA(1);
        if (cMo105LA != '\n') {
            if (cMo105LA == '\"') {
                mSTRING(false);
            } else if (cMo105LA == '\'') {
                mCHAR(false);
            } else if (mo105LA(1) == '/' && (mo105LA(2) == '*' || mo105LA(2) == '/')) {
                mCOMMENT(false);
            } else if (mo105LA(1) == '\r' && mo105LA(2) == '\n') {
                match("\r\n");
            } else {
                if (mo105LA(1) == '/' && _tokenSet_1.member(mo105LA(2))) {
                    match('/');
                    bitSet = _tokenSet_1;
                } else if (mo105LA(1) == '\r') {
                    match('\r');
                } else {
                    if (!_tokenSet_2.member(mo105LA(1))) {
                        throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                    }
                    bitSet = _tokenSet_2;
                }
                match(bitSet);
            }
            if (z) {
                tokenMakeToken = null;
            } else {
                tokenMakeToken = makeToken(5);
                tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
            }
            this._returnToken = tokenMakeToken;
        }
        match('\n');
        newline();
        if (z) {
        }
        this._returnToken = tokenMakeToken;
    }

    public final void mTEXT_ARG(boolean z) throws NoViableAltForCharException, MismatchedCharException {
        Token tokenMakeToken;
        int length = this.text.length();
        char cMo105LA = mo105LA(1);
        if (cMo105LA != '\t' && cMo105LA != '\n' && cMo105LA != '\r' && cMo105LA != ' ') {
            if (cMo105LA != '\"' && cMo105LA != '$' && cMo105LA != '\'' && cMo105LA != '+' && cMo105LA != '_') {
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
                                        break;
                                    default:
                                        throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                                }
                                break;
                        }
                        break;
                }
            }
        } else {
            mWS(false);
        }
        int i = 0;
        while (_tokenSet_11.member(mo105LA(1)) && mo105LA(2) >= 3 && mo105LA(2) <= 255) {
            mTEXT_ARG_ELEMENT(false);
            if (_tokenSet_4.member(mo105LA(1)) && _tokenSet_12.member(mo105LA(2))) {
                mWS(false);
            } else if (!_tokenSet_12.member(mo105LA(1))) {
                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
            }
            i++;
        }
        if (i < 1) {
            throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
        }
        if (z) {
            tokenMakeToken = makeToken(13);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x0043 A[FALL_THROUGH] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void mTEXT_ARG_ELEMENT(boolean z) throws NoViableAltForCharException, MismatchedCharException {
        Token tokenMakeToken;
        int length = this.text.length();
        char cMo105LA = mo105LA(1);
        if (cMo105LA == '\"') {
            mSTRING(false);
        } else if (cMo105LA == '$') {
            mTEXT_ITEM(false);
        } else if (cMo105LA == '\'') {
            mCHAR(false);
        } else if (cMo105LA == '+') {
            match('+');
        } else if (cMo105LA != '_') {
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
                    mINT_OR_FLOAT(false);
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
                                    throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
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
                            mTEXT_ARG_ID_ELEMENT(false);
                            break;
                    }
                    break;
            }
        }
        if (z) {
            tokenMakeToken = makeToken(14);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    public final void mTEXT_ARG_ID_ELEMENT(boolean z) throws NoViableAltForCharException {
        Token tokenMakeToken;
        int length = this.text.length();
        mID(true);
        if (_tokenSet_4.member(mo105LA(1)) && _tokenSet_21.member(mo105LA(2))) {
            int length2 = this.text.length();
            mWS(false);
            this.text.setLength(length2);
        } else if (!_tokenSet_21.member(mo105LA(1))) {
            throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
        }
        char cMo105LA = mo105LA(1);
        if (cMo105LA != '\t' && cMo105LA != '\n' && cMo105LA != '\r' && cMo105LA != ' ' && cMo105LA != '\"' && cMo105LA != '$') {
            if (cMo105LA == '.') {
                match('.');
                mTEXT_ARG_ID_ELEMENT(false);
            } else if (cMo105LA != ']' && cMo105LA != '_' && cMo105LA != '+' && cMo105LA != ',') {
                switch (cMo105LA) {
                    case '\'':
                    case ')':
                        break;
                    case '(':
                        match('(');
                        if (_tokenSet_4.member(mo105LA(1)) && _tokenSet_22.member(mo105LA(2)) && mo105LA(3) >= 3 && mo105LA(3) <= 255) {
                            int length3 = this.text.length();
                            mWS(false);
                            this.text.setLength(length3);
                        } else if (!_tokenSet_22.member(mo105LA(1)) || mo105LA(2) < 3 || mo105LA(2) > 255) {
                            throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                        }
                        while (_tokenSet_23.member(mo105LA(1)) && mo105LA(2) >= 3 && mo105LA(2) <= 255 && mo105LA(3) >= 3 && mo105LA(3) <= 255) {
                            while (true) {
                                mTEXT_ARG(false);
                                if (mo105LA(1) == ',') {
                                    match(',');
                                }
                            }
                        }
                        char cMo105LA2 = mo105LA(1);
                        if (cMo105LA2 == '\t' || cMo105LA2 == '\n' || cMo105LA2 == '\r' || cMo105LA2 == ' ') {
                            int length4 = this.text.length();
                            mWS(false);
                            this.text.setLength(length4);
                        } else if (cMo105LA2 != ')') {
                            throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                        }
                        match(')');
                        break;
                    default:
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
                                        int i = 0;
                                        while (mo105LA(1) == '[') {
                                            match('[');
                                            if (_tokenSet_4.member(mo105LA(1)) && _tokenSet_23.member(mo105LA(2)) && mo105LA(3) >= 3 && mo105LA(3) <= 255) {
                                                int length5 = this.text.length();
                                                mWS(false);
                                                this.text.setLength(length5);
                                            } else if (!_tokenSet_23.member(mo105LA(1)) || mo105LA(2) < 3 || mo105LA(2) > 255 || mo105LA(3) < 3 || mo105LA(3) > 255) {
                                                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                                            }
                                            mTEXT_ARG(false);
                                            char cMo105LA3 = mo105LA(1);
                                            if (cMo105LA3 == '\t' || cMo105LA3 == '\n' || cMo105LA3 == '\r' || cMo105LA3 == ' ') {
                                                int length6 = this.text.length();
                                                mWS(false);
                                                this.text.setLength(length6);
                                            } else if (cMo105LA3 != ']') {
                                                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                                            }
                                            match(']');
                                            i++;
                                        }
                                        if (i < 1) {
                                            throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                                        }
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
                                                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                                        }
                                        break;
                                }
                                break;
                        }
                        break;
                }
            }
        }
        if (z) {
            tokenMakeToken = makeToken(15);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    public final void mTEXT_ITEM(boolean z) throws NoViableAltForCharException, MismatchedCharException {
        String str;
        ANTLRStringBuffer aNTLRStringBuffer;
        String str2;
        Token token;
        StringBuilder sb;
        String str3;
        Token token2;
        String ruleName;
        String fIRSTBitSet;
        StringBuilder sb2;
        String str4;
        Token token3;
        Token tokenMakeToken;
        Token token4;
        StringBuilder sb3;
        String str5;
        int length = this.text.length();
        if (mo105LA(1) == '$' && mo105LA(2) == 's' && mo105LA(3) == 'e') {
            match("$set");
            if (mo105LA(1) == 'T' && mo105LA(2) == 'e') {
                match("Text");
                char cMo105LA = mo105LA(1);
                if (cMo105LA == '\t' || cMo105LA == '\n' || cMo105LA == '\r' || cMo105LA == ' ') {
                    mWS(false);
                } else if (cMo105LA != '(') {
                    throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                }
                match('(');
                mTEXT_ARG(true);
                token = this._returnToken;
                match(')');
                sb = new StringBuilder();
                str3 = "self.text.setLength(_begin) ; self.text.append(";
                sb.append(str3);
                sb.append(token.getText());
                sb.append(")");
                fIRSTBitSet = sb.toString();
                this.text.setLength(length);
                this.text.append(fIRSTBitSet);
            } else {
                if (mo105LA(1) == 'T' && mo105LA(2) == 'o') {
                    match("Token");
                    char cMo105LA2 = mo105LA(1);
                    if (cMo105LA2 == '\t' || cMo105LA2 == '\n' || cMo105LA2 == '\r' || cMo105LA2 == ' ') {
                        mWS(false);
                    } else if (cMo105LA2 != '(') {
                        throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                    }
                    match('(');
                    mTEXT_ARG(true);
                    token4 = this._returnToken;
                    match(')');
                    sb3 = new StringBuilder();
                    str5 = "_token = ";
                } else {
                    if (mo105LA(1) != 'T' || mo105LA(2) != 'y') {
                        throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                    }
                    match("Type");
                    char cMo105LA3 = mo105LA(1);
                    if (cMo105LA3 == '\t' || cMo105LA3 == '\n' || cMo105LA3 == '\r' || cMo105LA3 == ' ') {
                        mWS(false);
                    } else if (cMo105LA3 != '(') {
                        throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                    }
                    match('(');
                    mTEXT_ARG(true);
                    token4 = this._returnToken;
                    match(')');
                    sb3 = new StringBuilder();
                    str5 = "_ttype = ";
                }
                sb3.append(str5);
                fIRSTBitSet = C0000a.m0a(token4, sb3);
                this.text.setLength(length);
                this.text.append(fIRSTBitSet);
            }
        } else if (mo105LA(1) == '$' && mo105LA(2) == 'F' && mo105LA(3) == 'O') {
            match("$FOLLOW");
            if (!_tokenSet_5.member(mo105LA(1)) || !_tokenSet_6.member(mo105LA(2)) || mo105LA(3) < 3 || mo105LA(3) > 255) {
                token3 = null;
            } else {
                char cMo105LA4 = mo105LA(1);
                if (cMo105LA4 == '\t' || cMo105LA4 == '\n' || cMo105LA4 == '\r' || cMo105LA4 == ' ') {
                    mWS(false);
                } else if (cMo105LA4 != '(') {
                    throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                }
                match('(');
                mTEXT_ARG(true);
                token3 = this._returnToken;
                match(')');
            }
            ruleName = this.currentRule.getRuleName();
            if (token3 != null) {
                ruleName = token3.getText();
            }
            fIRSTBitSet = this.generator.getFOLLOWBitSet(ruleName, 1);
            if (fIRSTBitSet == null) {
                sb2 = new StringBuilder();
                str4 = "$FOLLOW(";
                sb2.append(str4);
                sb2.append(ruleName);
                sb2.append("): unknown rule or bad lookahead computation");
                reportError(sb2.toString());
            }
            this.text.setLength(length);
            this.text.append(fIRSTBitSet);
        } else if (mo105LA(1) == '$' && mo105LA(2) == 'F' && mo105LA(3) == 'I') {
            match("$FIRST");
            if (!_tokenSet_5.member(mo105LA(1)) || !_tokenSet_6.member(mo105LA(2)) || mo105LA(3) < 3 || mo105LA(3) > 255) {
                token2 = null;
            } else {
                char cMo105LA5 = mo105LA(1);
                if (cMo105LA5 == '\t' || cMo105LA5 == '\n' || cMo105LA5 == '\r' || cMo105LA5 == ' ') {
                    mWS(false);
                } else if (cMo105LA5 != '(') {
                    throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                }
                match('(');
                mTEXT_ARG(true);
                token2 = this._returnToken;
                match(')');
            }
            ruleName = this.currentRule.getRuleName();
            if (token2 != null) {
                ruleName = token2.getText();
            }
            fIRSTBitSet = this.generator.getFIRSTBitSet(ruleName, 1);
            if (fIRSTBitSet == null) {
                sb2 = new StringBuilder();
                str4 = "$FIRST(";
                sb2.append(str4);
                sb2.append(ruleName);
                sb2.append("): unknown rule or bad lookahead computation");
                reportError(sb2.toString());
            }
            this.text.setLength(length);
            this.text.append(fIRSTBitSet);
        } else {
            if (mo105LA(1) == '$' && mo105LA(2) == 's' && mo105LA(3) == 'k') {
                match("$skip");
                this.text.setLength(length);
                aNTLRStringBuffer = this.text;
                str2 = "_ttype = SKIP";
            } else if (mo105LA(1) == '$' && mo105LA(2) == 'a') {
                match("$append");
                char cMo105LA6 = mo105LA(1);
                if (cMo105LA6 == '\t' || cMo105LA6 == '\n' || cMo105LA6 == '\r' || cMo105LA6 == ' ') {
                    mWS(false);
                } else if (cMo105LA6 != '(') {
                    throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                }
                match('(');
                mTEXT_ARG(true);
                token = this._returnToken;
                match(')');
                sb = new StringBuilder();
                str3 = "self.text.append(";
                sb.append(str3);
                sb.append(token.getText());
                sb.append(")");
                fIRSTBitSet = sb.toString();
                this.text.setLength(length);
                this.text.append(fIRSTBitSet);
            } else if (mo105LA(1) == '$' && mo105LA(2) == 'g') {
                match("$getText");
                this.text.setLength(length);
                aNTLRStringBuffer = this.text;
                str2 = "self.text.getString(_begin)";
            } else {
                if (mo105LA(1) != '$' || mo105LA(2) != 'n') {
                    throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                }
                if (mo105LA(1) == '$' && mo105LA(2) == 'n' && mo105LA(3) == 'l') {
                    str = "$nl";
                } else {
                    if (mo105LA(1) != '$' || mo105LA(2) != 'n' || mo105LA(3) != 'e') {
                        throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                    }
                    str = "$newline";
                }
                match(str);
                this.text.setLength(length);
                aNTLRStringBuffer = this.text;
                str2 = "self.newline()";
            }
            aNTLRStringBuffer.append(str2);
        }
        if (z) {
            tokenMakeToken = makeToken(7);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    public final void mTREE(boolean z) throws NoViableAltForCharException, MismatchedCharException {
        int i;
        boolean z2;
        Token tokenMakeToken;
        boolean z3;
        int length = this.text.length();
        Vector vector = new Vector(10);
        int length2 = this.text.length();
        char c2 = '(';
        match('(');
        this.text.setLength(length2);
        char cMo105LA = mo105LA(1);
        if (cMo105LA != '\t' && cMo105LA != '\n' && cMo105LA != '\r' && cMo105LA != ' ') {
            if (cMo105LA != '(' && cMo105LA != '_' && cMo105LA != '\"' && cMo105LA != '#') {
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
                    case '[':
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
                                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                        }
                        break;
                }
            }
        } else {
            int length3 = this.text.length();
            mWS(false);
            this.text.setLength(length3);
        }
        int length4 = this.text.length();
        mTREE_ELEMENT(true);
        this.text.setLength(length4);
        vector.appendElement(this._returnToken.getText());
        char cMo105LA2 = mo105LA(1);
        if (cMo105LA2 == '\t' || cMo105LA2 == '\n' || cMo105LA2 == '\r' || cMo105LA2 == ' ') {
            i = length;
            z2 = z;
            int length5 = this.text.length();
            mWS(false);
            this.text.setLength(length5);
        } else {
            if (cMo105LA2 != ')' && cMo105LA2 != ',') {
                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
            }
            i = length;
            z2 = z;
        }
        while (mo105LA(1) == ',') {
            int length6 = this.text.length();
            match(',');
            this.text.setLength(length6);
            char cMo105LA3 = mo105LA(1);
            if (cMo105LA3 == '\t' || cMo105LA3 == '\n' || cMo105LA3 == '\r' || cMo105LA3 == ' ') {
                int length7 = this.text.length();
                z3 = false;
                mWS(false);
                this.text.setLength(length7);
            } else {
                if (cMo105LA3 != c2 && cMo105LA3 != '_' && cMo105LA3 != '\"' && cMo105LA3 != '#') {
                    switch (cMo105LA3) {
                        default:
                            switch (cMo105LA3) {
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
                        case '[':
                            z3 = false;
                            break;
                    }
                }
                z3 = false;
            }
            int length8 = this.text.length();
            mTREE_ELEMENT(true);
            this.text.setLength(length8);
            vector.appendElement(this._returnToken.getText());
            char cMo105LA4 = mo105LA(1);
            if (cMo105LA4 == '\t' || cMo105LA4 == '\n' || cMo105LA4 == '\r' || cMo105LA4 == ' ') {
                boolean z4 = z3;
                c2 = '(';
                int length9 = this.text.length();
                mWS(z4);
                this.text.setLength(length9);
            } else {
                if (cMo105LA4 != ')' && cMo105LA4 != ',') {
                    throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                }
                c2 = '(';
            }
        }
        this.text.setLength(i);
        this.text.append(this.generator.getASTCreateString(vector));
        int length10 = this.text.length();
        match(')');
        this.text.setLength(length10);
        if (z2) {
            tokenMakeToken = makeToken(8);
            tokenMakeToken.setText(new String(this.text.getBuffer(), i, this.text.length() - i));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    /* JADX WARN: Removed duplicated region for block: B:36:0x00dc  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void mTREE_ELEMENT(boolean z) throws NoViableAltForCharException, MismatchedCharException {
        String strMapTreeId;
        int length = this.text.length();
        char cMo105LA = mo105LA(1);
        Token tokenMakeToken = null;
        if (cMo105LA == '\"') {
            mSTRING(false);
        } else if (cMo105LA == '(') {
            mTREE(false);
        } else if (cMo105LA != '_') {
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
                    mID_ELEMENT(false);
                    break;
                case '[':
                    mAST_CONSTRUCTOR(false);
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
                            if (mo105LA(1) == '#' && mo105LA(2) == '(') {
                                int length2 = this.text.length();
                                match('#');
                                this.text.setLength(length2);
                                mTREE(false);
                            } else if (mo105LA(1) == '#' && mo105LA(2) == '[') {
                                int length3 = this.text.length();
                                match('#');
                                this.text.setLength(length3);
                                mAST_CONSTRUCTOR(false);
                            } else {
                                if (mo105LA(1) == '#' && _tokenSet_3.member(mo105LA(2))) {
                                    int length4 = this.text.length();
                                    match('#');
                                    this.text.setLength(length4);
                                    boolean zMID_ELEMENT = mID_ELEMENT(true);
                                    Token token = this._returnToken;
                                    if (!zMID_ELEMENT) {
                                        strMapTreeId = this.generator.mapTreeId(token.getText(), null);
                                    }
                                } else {
                                    if (mo105LA(1) != '#' || mo105LA(2) != '#') {
                                        throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                                    }
                                    match("##");
                                    strMapTreeId = this.currentRule.getRuleName() + "_AST";
                                }
                                this.text.setLength(length);
                                this.text.append(strMapTreeId);
                            }
                            break;
                    }
                    break;
            }
        }
        if (z) {
            tokenMakeToken = makeToken(9);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        }
        this._returnToken = tokenMakeToken;
    }

    public final void mVAR_ASSIGN(boolean z) {
        Token tokenMakeToken;
        ActionTransInfo actionTransInfo;
        int length = this.text.length();
        match('=');
        if (mo105LA(1) != '=' && (actionTransInfo = this.transInfo) != null && actionTransInfo.refRuleRoot != null) {
            actionTransInfo.assignToRoot = true;
        }
        if (z) {
            tokenMakeToken = makeToken(18);
            tokenMakeToken.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            tokenMakeToken = null;
        }
        this._returnToken = tokenMakeToken;
    }

    public final void mWS(boolean z) throws NoViableAltForCharException {
        Token tokenMakeToken;
        int length = this.text.length();
        int i = 0;
        while (true) {
            if (mo105LA(1) == '\r' && mo105LA(2) == '\n') {
                match('\r');
            } else {
                char c2 = ' ';
                if (mo105LA(1) != ' ') {
                    c2 = '\t';
                    if (mo105LA(1) != '\t') {
                        if (mo105LA(1) != '\r') {
                            if (mo105LA(1) != '\n') {
                                break;
                            }
                        } else {
                            match('\r');
                            newline();
                            i++;
                        }
                    }
                }
                match(c2);
                i++;
            }
            match('\n');
            newline();
            i++;
        }
        if (i < 1) {
            throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
        }
        if (z) {
            tokenMakeToken = makeToken(29);
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
                    if (mo105LA(1) >= 3 && mo105LA(1) <= 255) {
                        mACTION(true);
                    } else {
                        if (mo105LA(1) != 65535) {
                            throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                        }
                        uponEOF();
                        this._returnToken = makeToken(1);
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

    @Override // antlr.CharScanner
    public void reportError(RecognitionException recognitionException) {
        this.antlrTool.error("Syntax error in action: " + recognitionException, getFilename(), getLine(), getColumn());
    }

    @Override // antlr.CharScanner
    public void reportError(String str) {
        this.antlrTool.error(str, getFilename(), getLine(), getColumn());
    }

    @Override // antlr.CharScanner
    public void reportWarning(String str) {
        if (getFilename() == null) {
            this.antlrTool.warning(str);
        } else {
            this.antlrTool.warning(str, getFilename(), getLine(), getColumn());
        }
    }

    public void setLineOffset(int i) {
        setLine(i);
    }

    public void setTool(Tool tool) {
        this.antlrTool = tool;
    }
}
