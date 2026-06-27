package antlr.actions.csharp;

import antlr.ActionTransInfo;
import antlr.ByteBuffer;
import antlr.CharBuffer;
import antlr.CharScanner;
import antlr.CharStreamException;
import antlr.CharStreamIOException;
import antlr.CodeGenerator;
import antlr.InputBuffer;
import antlr.LexerSharedInputState;
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
import p054a.p055a.p056a.p003a.C0000a;
import tv.danmaku.ijk.media.player.IjkMediaMeta;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/* loaded from: classes3.dex */
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
    public static final BitSet _tokenSet_26 = new BitSet(mk_tokenSet_26());

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
        return new long[]{0, 576460745995190270L, 0, 0, 0};
    }

    public static final long[] mk_tokenSet_14() {
        return new long[]{2306123388973753856L, 671088640, 0, 0, 0};
    }

    public static final long[] mk_tokenSet_15() {
        return new long[]{287952805300282880L, 576460746129407998L, 0, 0, 0};
    }

    public static final long[] mk_tokenSet_16() {
        return new long[]{2306051920717948416L, IjkMediaMeta.AV_CH_STEREO_LEFT, 0, 0, 0};
    }

    public static final long[] mk_tokenSet_17() {
        return new long[]{2305843013508670976L, 0, 0, 0, 0};
    }

    public static final long[] mk_tokenSet_18() {
        return new long[]{208911504254464L, IjkMediaMeta.AV_CH_STEREO_LEFT, 0, 0, 0};
    }

    public static final long[] mk_tokenSet_19() {
        return new long[]{1151051235328L, 576460746129407998L, 0, 0, 0};
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
        return new long[]{189120294954496L, 0, 0, 0, 0};
    }

    public static final long[] mk_tokenSet_21() {
        return new long[]{288139722277004800L, 576460746129407998L, 0, 0, 0};
    }

    public static final long[] mk_tokenSet_22() {
        return new long[]{288084781055354368L, 576460746666278910L, 0, 0, 0};
    }

    public static final long[] mk_tokenSet_23() {
        return new long[]{287960536241415680L, 576460745995190270L, 0, 0, 0};
    }

    public static final long[] mk_tokenSet_24() {
        return new long[]{287958337218160128L, 576460745995190270L, 0, 0, 0};
    }

    public static final long[] mk_tokenSet_25() {
        return new long[]{288228817078593024L, 576460746532061182L, 0, 0, 0};
    }

    public static final long[] mk_tokenSet_26() {
        return new long[]{288158448334415360L, 576460746532061182L, 0, 0, 0};
    }

    public static final long[] mk_tokenSet_3() {
        return new long[]{4294977024L, 576460745995190270L, 0, 0, 0};
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

    public final void mACTION(boolean z) {
        Token token;
        int length = this.text.length();
        int i = 0;
        while (true) {
            char mo105LA = mo105LA(1);
            if (mo105LA == '#') {
                mAST_ITEM(false);
            } else if (mo105LA == '$') {
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
            token = makeToken(4);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    /* JADX WARN: Code restructure failed: missing block: B:82:0x015d, code lost:
    
        r2 = makeToken(16);
        r2.setText(new java.lang.String(r16.text.getBuffer(), r1, r16.text.length() - r1));
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void mARG(boolean z) {
        int length = this.text.length();
        char mo105LA = mo105LA(1);
        char c2 = 255;
        if (mo105LA != '\'') {
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
                    mINT_OR_FLOAT(false);
                    break;
                default:
                    if (_tokenSet_19.member(mo105LA(1)) && mo105LA(2) >= 3 && mo105LA(2) <= 255 && mo105LA(3) >= 3 && mo105LA(3) <= 255) {
                        mTREE_ELEMENT(false);
                        break;
                    } else {
                        if (mo105LA(1) != '\"' || mo105LA(2) < 3 || mo105LA(2) > 255 || mo105LA(3) < 3 || mo105LA(3) > 255) {
                            throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                        }
                        mSTRING(false);
                        break;
                    }
                    break;
            }
        } else {
            mCHAR(false);
        }
        for (int i = 2; _tokenSet_20.member(mo105LA(1)) && _tokenSet_21.member(mo105LA(i)) && mo105LA(3) >= 3 && mo105LA(3) <= c2; i = 2) {
            char mo105LA2 = mo105LA(1);
            if (mo105LA2 == '\t' || mo105LA2 == '\n' || mo105LA2 == '\r' || mo105LA2 == ' ') {
                mWS(false);
            } else if (mo105LA2 != '-' && mo105LA2 != '/' && mo105LA2 != '*' && mo105LA2 != '+') {
                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
            }
            char mo105LA3 = mo105LA(1);
            if (mo105LA3 == '*') {
                match('*');
            } else if (mo105LA3 == '+') {
                match('+');
            } else if (mo105LA3 == '-') {
                match('-');
            } else {
                if (mo105LA3 != '/') {
                    throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                }
                match('/');
            }
            char mo105LA4 = mo105LA(1);
            if (mo105LA4 == '\t' || mo105LA4 == '\n' || mo105LA4 == '\r' || mo105LA4 == ' ') {
                mWS(false);
            } else if (mo105LA4 != '_' && mo105LA4 != '\"' && mo105LA4 != '#' && mo105LA4 != '\'' && mo105LA4 != '(') {
                switch (mo105LA4) {
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
                        switch (mo105LA4) {
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
                                switch (mo105LA4) {
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
                }
            }
            mARG(false);
            c2 = 255;
        }
        Token token = null;
        this._returnToken = token;
    }

    /* JADX WARN: Removed duplicated region for block: B:47:0x0125 A[FALL_THROUGH] */
    /* JADX WARN: Removed duplicated region for block: B:90:0x0207 A[FALL_THROUGH] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void mAST_CONSTRUCTOR(boolean z) {
        Token token;
        Token token2;
        char mo105LA;
        Token token3;
        char mo105LA2;
        int length = this.text.length();
        int length2 = this.text.length();
        match('[');
        this.text.setLength(length2);
        char mo105LA3 = mo105LA(1);
        if (mo105LA3 == '\t' || mo105LA3 == '\n' || mo105LA3 == '\r' || mo105LA3 == ' ') {
            int length3 = this.text.length();
            mWS(false);
            this.text.setLength(length3);
        } else if (mo105LA3 != '(' && mo105LA3 != '_' && mo105LA3 != '\"' && mo105LA3 != '#') {
            switch (mo105LA3) {
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
                    switch (mo105LA3) {
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
                            switch (mo105LA3) {
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
            }
        }
        int length4 = this.text.length();
        mAST_CTOR_ELEMENT(true);
        this.text.setLength(length4);
        Token token4 = this._returnToken;
        char mo105LA4 = mo105LA(1);
        if (mo105LA4 == '\t' || mo105LA4 == '\n' || mo105LA4 == '\r' || mo105LA4 == ' ') {
            int length5 = this.text.length();
            mWS(false);
            this.text.setLength(length5);
        } else if (mo105LA4 != ',' && mo105LA4 != ']') {
            throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
        }
        if (mo105LA(1) == ',' && _tokenSet_10.member(mo105LA(2)) && mo105LA(3) >= 3 && mo105LA(3) <= 255) {
            int length6 = this.text.length();
            match(',');
            this.text.setLength(length6);
            char mo105LA5 = mo105LA(1);
            if (mo105LA5 != '\t' && mo105LA5 != '\n' && mo105LA5 != '\r' && mo105LA5 != ' ') {
                if (mo105LA5 != '(' && mo105LA5 != '_' && mo105LA5 != '\"' && mo105LA5 != '#') {
                    switch (mo105LA5) {
                        default:
                            switch (mo105LA5) {
                                default:
                                    switch (mo105LA5) {
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
                                    mo105LA2 = mo105LA(1);
                                    if (mo105LA2 != '\t' || mo105LA2 == '\n' || mo105LA2 == '\r' || mo105LA2 == ' ') {
                                        int length8 = this.text.length();
                                        mWS(false);
                                        this.text.setLength(length8);
                                        break;
                                    } else if (mo105LA2 != ',' && mo105LA2 != ']') {
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
            mo105LA2 = mo105LA(1);
            if (mo105LA2 != '\t') {
            }
            int length82 = this.text.length();
            mWS(false);
            this.text.setLength(length82);
        } else {
            if (mo105LA(1) != ',' && mo105LA(1) != ']') {
                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
            }
            token = null;
        }
        char mo105LA6 = mo105LA(1);
        if (mo105LA6 == ',') {
            int length10 = this.text.length();
            match(',');
            this.text.setLength(length10);
            char mo105LA7 = mo105LA(1);
            if (mo105LA7 != '\t' && mo105LA7 != '\n' && mo105LA7 != '\r' && mo105LA7 != ' ') {
                if (mo105LA7 != '(' && mo105LA7 != '_' && mo105LA7 != '\"' && mo105LA7 != '#') {
                    switch (mo105LA7) {
                        default:
                            switch (mo105LA7) {
                                default:
                                    switch (mo105LA7) {
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
                                    mo105LA = mo105LA(1);
                                    if (mo105LA != '\t' || mo105LA == '\n' || mo105LA == '\r' || mo105LA == ' ') {
                                        int length12 = this.text.length();
                                        mWS(false);
                                        this.text.setLength(length12);
                                        break;
                                    } else if (mo105LA != ']') {
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
            mo105LA = mo105LA(1);
            if (mo105LA != '\t') {
            }
            int length122 = this.text.length();
            mWS(false);
            this.text.setLength(length122);
        } else {
            if (mo105LA6 != ']') {
                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
            }
            token2 = null;
        }
        int length14 = this.text.length();
        match(']');
        this.text.setLength(length14);
        String processStringForASTConstructor = this.generator.processStringForASTConstructor(token4.getText());
        if (token != null) {
            processStringForASTConstructor = C0000a.m0a(token, C0000a.m9b(processStringForASTConstructor, ","));
        }
        if (token2 != null) {
            processStringForASTConstructor = C0000a.m0a(token2, C0000a.m9b(processStringForASTConstructor, ","));
        }
        this.text.setLength(length);
        this.text.append(this.generator.getASTCreateString(null, processStringForASTConstructor));
        if (z) {
            token3 = makeToken(10);
            token3.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token3 = null;
        }
        this._returnToken = token3;
    }

    public final void mAST_CTOR_ELEMENT(boolean z) {
        Token token;
        int length = this.text.length();
        if (mo105LA(1) == '\"' && mo105LA(2) >= 3 && mo105LA(2) <= 255 && mo105LA(3) >= 3 && mo105LA(3) <= 255) {
            mSTRING(false);
        } else if (_tokenSet_19.member(mo105LA(1)) && mo105LA(2) >= 3 && mo105LA(2) <= 255) {
            mTREE_ELEMENT(false);
        } else {
            if (mo105LA(1) < '0' || mo105LA(1) > '9') {
                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
            }
            mINT(false);
        }
        if (z) {
            token = makeToken(11);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    /* JADX WARN: Code restructure failed: missing block: B:39:0x0134, code lost:
    
        if (mo105LA(1) != '=') goto L81;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x01b9, code lost:
    
        mVAR_ASSIGN(false);
     */
    /* JADX WARN: Code restructure failed: missing block: B:83:0x01b7, code lost:
    
        if (mo105LA(1) == '=') goto L80;
     */
    /* JADX WARN: Removed duplicated region for block: B:37:0x012d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void mAST_ITEM(boolean z) {
        String text;
        String mapTreeId;
        Token token;
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
            char mo105LA = mo105LA(1);
            if (mo105LA != '\t' && mo105LA != '\n' && mo105LA != '\r' && mo105LA != ' ') {
                if (mo105LA != '_') {
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
                            mID(true);
                            Token token2 = this._returnToken;
                            text = token2.getText();
                            mapTreeId = this.generator.mapTreeId(token2.getText(), this.transInfo);
                            if (mapTreeId == null && !text.equals(mapTreeId)) {
                                this.text.setLength(length);
                                this.text.append(mapTreeId);
                            } else if (!text.equals("define") || text.equals("undef") || text.equals("if") || text.equals("elif") || text.equals("else") || text.equals("endif") || text.equals("line") || text.equals(IjkMediaPlayer.OnNativeInvokeListener.ARG_ERROR) || text.equals("warning") || text.equals("region") || text.equals("endregion")) {
                                this.text.setLength(length);
                                this.text.append("#" + text);
                            }
                            if (_tokenSet_4.member(mo105LA(1))) {
                                mWS(false);
                            }
                            break;
                    }
                }
            } else {
                mWS(false);
            }
            mID(true);
            Token token22 = this._returnToken;
            text = token22.getText();
            mapTreeId = this.generator.mapTreeId(token22.getText(), this.transInfo);
            if (mapTreeId == null) {
            }
            if (!text.equals("define")) {
            }
            this.text.setLength(length);
            this.text.append("#" + text);
            if (_tokenSet_4.member(mo105LA(1))) {
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
            if (this.currentRule != null) {
                String str = this.currentRule.getRuleName() + "_AST";
                this.text.setLength(length);
                this.text.append(str);
                ActionTransInfo actionTransInfo = this.transInfo;
                if (actionTransInfo != null) {
                    actionTransInfo.refRuleRoot = str;
                }
            } else {
                reportWarning("\"##\" not valid in this context");
                this.text.setLength(length);
                this.text.append("##");
            }
            if (_tokenSet_4.member(mo105LA(1))) {
                mWS(false);
            }
        }
        if (z) {
            token = makeToken(6);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    public final void mCHAR(boolean z) {
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
            token = makeToken(22);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    public final void mCOMMENT(boolean z) {
        Token token;
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
            token = makeToken(19);
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
            token = makeToken(25);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    /* JADX WARN: Removed duplicated region for block: B:31:0x011f  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x013b  */
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
                                                } else if (mo105LA(1) < 3 || mo105LA(1) > 255) {
                                                    throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                                                }
                                                if (z) {
                                                    token = makeToken(24);
                                                    token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
                                                } else {
                                                    token = null;
                                                }
                                                this._returnToken = token;
                                            case '4':
                                            case '5':
                                            case '6':
                                            case '7':
                                                matchRange('4', '7');
                                                if (mo105LA(1) < '0' || mo105LA(1) > '9' || mo105LA(2) < 3 || mo105LA(2) > 255) {
                                                    if (mo105LA(1) < 3 || mo105LA(1) > 255) {
                                                        throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                                                    }
                                                    if (z) {
                                                    }
                                                    this._returnToken = token;
                                                }
                                                mDIGIT(false);
                                                if (z) {
                                                }
                                                this._returnToken = token;
                                            default:
                                                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
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

    /* JADX WARN: Code restructure failed: missing block: B:27:0x002a, code lost:
    
        r2 = 'a';
        r4 = 'z';
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x002d, code lost:
    
        match('_');
     */
    /* JADX WARN: Removed duplicated region for block: B:13:0x006b A[LOOP:0: B:9:0x002d->B:13:0x006b, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0066  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:13:0x0045 -> B:6:0x0026). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:17:0x0061 -> B:7:0x002a). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void mID(boolean z) {
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
                    Token token = null;
                    if (!_tokenSet_9.member(mo105LA(1))) {
                        if (z) {
                            token = makeToken(17);
                            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
                        }
                        this._returnToken = token;
                        return;
                    }
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
                                                c2 = 'a';
                                                c3 = 'z';
                                                break;
                                            default:
                                                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                                        }
                                }
                        }
                    } else {
                        match('_');
                    }
                }
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

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:121:? A[FALL_THROUGH, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:124:0x0276  */
    /* JADX WARN: Removed duplicated region for block: B:165:0x02c5 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0302  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x031e  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x0139  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final boolean mID_ELEMENT(boolean z) {
        Token token;
        ActionTransInfo actionTransInfo;
        int length = this.text.length();
        boolean z2 = true;
        mID(true);
        Token token2 = this._returnToken;
        if (_tokenSet_4.member(mo105LA(1)) && _tokenSet_14.member(mo105LA(2))) {
            int length2 = this.text.length();
            mWS(false);
            this.text.setLength(length2);
        } else if (!_tokenSet_14.member(mo105LA(1))) {
            throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
        }
        char mo105LA = mo105LA(1);
        if (mo105LA != '(') {
            if (mo105LA == '.') {
                match('.');
            } else if (mo105LA == '[') {
                int i = 0;
                while (mo105LA(1) == '[') {
                    match('[');
                    char mo105LA2 = mo105LA(1);
                    if (mo105LA2 != '\t' && mo105LA2 != '\n' && mo105LA2 != '\r' && mo105LA2 != ' ') {
                        if (mo105LA2 != '_' && mo105LA2 != '\"' && mo105LA2 != '#' && mo105LA2 != '\'' && mo105LA2 != '(') {
                            switch (mo105LA2) {
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
                                    switch (mo105LA2) {
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
                                            switch (mo105LA2) {
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
                            }
                        }
                    } else {
                        int length3 = this.text.length();
                        mWS(false);
                        this.text.setLength(length3);
                    }
                    mARG(false);
                    char mo105LA3 = mo105LA(1);
                    if (mo105LA3 == '\t' || mo105LA3 == '\n' || mo105LA3 == '\r' || mo105LA3 == ' ') {
                        int length4 = this.text.length();
                        mWS(false);
                        this.text.setLength(length4);
                    } else if (mo105LA3 != ']') {
                        throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                    }
                    match(']');
                    i++;
                }
                if (i < 1) {
                    throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                }
            } else {
                if (mo105LA(1) != '-' || mo105LA(2) != '>' || !_tokenSet_13.member(mo105LA(3))) {
                    if (!_tokenSet_16.member(mo105LA(1))) {
                        throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                    }
                    String mapTreeId = this.generator.mapTreeId(token2.getText(), this.transInfo);
                    if (mapTreeId != null) {
                        this.text.setLength(length);
                        this.text.append(mapTreeId);
                    }
                    if (_tokenSet_17.member(mo105LA(1)) && _tokenSet_16.member(mo105LA(2)) && (actionTransInfo = this.transInfo) != null && actionTransInfo.refRuleRoot != null) {
                        char mo105LA4 = mo105LA(1);
                        if (mo105LA4 == '\t' || mo105LA4 == '\n' || mo105LA4 == '\r' || mo105LA4 == ' ') {
                            mWS(false);
                        } else if (mo105LA4 != '=') {
                            throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                        }
                        mVAR_ASSIGN(false);
                    } else if (!_tokenSet_18.member(mo105LA(1))) {
                        throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                    }
                    if (z) {
                        token = null;
                    } else {
                        token = makeToken(12);
                        token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
                    }
                    this._returnToken = token;
                    return z2;
                }
                match("->");
            }
            mID_ELEMENT(false);
        } else {
            match('(');
            if (_tokenSet_4.member(mo105LA(1)) && _tokenSet_15.member(mo105LA(2)) && mo105LA(3) >= 3 && mo105LA(3) <= 255) {
                int length5 = this.text.length();
                mWS(false);
                this.text.setLength(length5);
            } else if (!_tokenSet_15.member(mo105LA(1)) || mo105LA(2) < 3 || mo105LA(2) > 255) {
                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
            }
            char mo105LA5 = mo105LA(1);
            if (mo105LA5 != '\t' && mo105LA5 != '\n' && mo105LA5 != '\r' && mo105LA5 != ' ') {
                if (mo105LA5 != '_' && mo105LA5 != '\"' && mo105LA5 != '#') {
                    switch (mo105LA5) {
                        case '\'':
                        case '(':
                            break;
                        case ')':
                            break;
                        default:
                            switch (mo105LA5) {
                                default:
                                    switch (mo105LA5) {
                                        default:
                                            switch (mo105LA5) {
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
                                            while (true) {
                                                mARG(false);
                                                if (mo105LA(1) == ',') {
                                                    break;
                                                } else {
                                                    match(',');
                                                    char mo105LA6 = mo105LA(1);
                                                    if (mo105LA6 == '\t' || mo105LA6 == '\n' || mo105LA6 == '\r' || mo105LA6 == ' ') {
                                                        int length6 = this.text.length();
                                                        mWS(false);
                                                        this.text.setLength(length6);
                                                    } else if (mo105LA6 != '_' && mo105LA6 != '\"' && mo105LA6 != '#' && mo105LA6 != '\'' && mo105LA6 != '(') {
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
                                                                continue;
                                                            default:
                                                                switch (mo105LA6) {
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
                                                                        switch (mo105LA6) {
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
                                                        }
                                                    }
                                                }
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
                }
                while (true) {
                    mARG(false);
                    if (mo105LA(1) == ',') {
                    }
                }
            }
            char mo105LA7 = mo105LA(1);
            if (mo105LA7 == '\t' || mo105LA7 == '\n' || mo105LA7 == '\r' || mo105LA7 == ' ') {
                int length7 = this.text.length();
                mWS(false);
                this.text.setLength(length7);
            } else if (mo105LA7 != ')') {
                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
            }
            match(')');
        }
        z2 = false;
        if (z) {
        }
        this._returnToken = token;
        return z2;
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
            token = makeToken(26);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x002e, code lost:
    
        r4 = 'L';
     */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x0034, code lost:
    
        if (mo105LA(1) != 'L') goto L16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x0040, code lost:
    
        if (antlr.actions.csharp.ActionLexer._tokenSet_26.member(mo105LA(2)) == false) goto L16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x0042, code lost:
    
        match(r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x008e, code lost:
    
        if (r10 == false) goto L35;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x0090, code lost:
    
        r10 = makeToken(27);
        r10.setText(new java.lang.String(r9.text.getBuffer(), r0, r9.text.length() - r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x00ad, code lost:
    
        r9._returnToken = r10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x00af, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x00ac, code lost:
    
        r10 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0046, code lost:
    
        r4 = 'l';
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x004c, code lost:
    
        if (mo105LA(1) != 'l') goto L21;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x0058, code lost:
    
        if (antlr.actions.csharp.ActionLexer._tokenSet_26.member(mo105LA(2)) == false) goto L21;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x0061, code lost:
    
        if (mo105LA(1) != '.') goto L31;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x0063, code lost:
    
        match('.');
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x006a, code lost:
    
        if (mo105LA(1) < '0') goto L46;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x0070, code lost:
    
        if (mo105LA(1) > '9') goto L45;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x007c, code lost:
    
        if (antlr.actions.csharp.ActionLexer._tokenSet_26.member(mo105LA(2)) == false) goto L47;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x007e, code lost:
    
        mDIGIT(false);
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x008c, code lost:
    
        if (antlr.actions.csharp.ActionLexer._tokenSet_26.member(mo105LA(1)) == false) goto L38;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x00c5, code lost:
    
        throw new antlr.NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void mINT_OR_FLOAT(boolean z) {
        int length = this.text.length();
        int i = 0;
        while (mo105LA(1) >= '0' && mo105LA(1) <= '9' && _tokenSet_25.member(mo105LA(2))) {
            mDIGIT(false);
            i++;
        }
        throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
    }

    public final void mML_COMMENT(boolean z) {
        Token token;
        int length = this.text.length();
        match("/*");
        while (true) {
            if (mo105LA(1) == '*' && mo105LA(2) == '/') {
                break;
            }
            if (mo105LA(1) == '\r' && mo105LA(2) == '\n' && mo105LA(3) >= 3 && mo105LA(3) <= 255) {
                match('\r');
            } else if (mo105LA(1) == '\r' && mo105LA(2) >= 3 && mo105LA(2) <= 255 && mo105LA(3) >= 3 && mo105LA(3) <= 255) {
                match('\r');
                newline();
            } else if (mo105LA(1) != '\n' || mo105LA(2) < 3 || mo105LA(2) > 255 || mo105LA(3) < 3 || mo105LA(3) > 255) {
                if (mo105LA(1) < 3 || mo105LA(1) > 255 || mo105LA(2) < 3 || mo105LA(2) > 255 || mo105LA(3) < 3 || mo105LA(3) > 255) {
                    break;
                } else {
                    matchNot(CharScanner.EOF_CHAR);
                }
            }
            match('\n');
            newline();
        }
        match("*/");
        if (z) {
            token = makeToken(21);
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
            token = makeToken(20);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    public final void mSTRING(boolean z) {
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
            token = makeToken(23);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x00c6  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x00e1  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void mSTUFF(boolean z) {
        Token token;
        BitSet bitSet;
        int length = this.text.length();
        char mo105LA = mo105LA(1);
        if (mo105LA != '\n') {
            if (mo105LA == '\"') {
                mSTRING(false);
            } else if (mo105LA == '\'') {
                mCHAR(false);
            } else if (mo105LA(1) == '/' && (mo105LA(2) == '*' || mo105LA(2) == '/')) {
                mCOMMENT(false);
            } else if (mo105LA(1) == '\r' && mo105LA(2) == '\n') {
                match("\r\n");
            } else if (mo105LA(1) == '\\' && mo105LA(2) == '#') {
                match('\\');
                match('#');
                this.text.setLength(length);
                this.text.append("#");
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
                token = null;
            } else {
                token = makeToken(5);
                token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
            }
            this._returnToken = token;
        }
        match('\n');
        newline();
        if (z) {
        }
        this._returnToken = token;
    }

    /* JADX WARN: Code restructure failed: missing block: B:46:0x00b5, code lost:
    
        if (r9 == false) goto L45;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x00b7, code lost:
    
        r9 = makeToken(13);
        r9.setText(new java.lang.String(r8.text.getBuffer(), r0, r8.text.length() - r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x00d2, code lost:
    
        r8._returnToken = r9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x00d4, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x00d1, code lost:
    
        r9 = null;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void mTEXT_ARG(boolean z) {
        int length = this.text.length();
        char mo105LA = mo105LA(1);
        if (mo105LA == '\t' || mo105LA == '\n' || mo105LA == '\r' || mo105LA == ' ') {
            mWS(false);
        } else if (mo105LA != '\"' && mo105LA != '$' && mo105LA != '\'' && mo105LA != '+' && mo105LA != '_') {
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
                                    throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                            }
                    }
            }
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
        throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
    }

    public final void mTEXT_ARG_ELEMENT(boolean z) {
        Token token;
        int length = this.text.length();
        char mo105LA = mo105LA(1);
        if (mo105LA == '\"') {
            mSTRING(false);
        } else if (mo105LA == '$') {
            mTEXT_ITEM(false);
        } else if (mo105LA == '\'') {
            mCHAR(false);
        } else if (mo105LA != '+') {
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
                        mINT_OR_FLOAT(false);
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
                }
            }
            mTEXT_ARG_ID_ELEMENT(false);
        } else {
            match('+');
        }
        if (z) {
            token = makeToken(14);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public final void mTEXT_ARG_ID_ELEMENT(boolean z) {
        Token token;
        int length = this.text.length();
        mID(true);
        if (_tokenSet_4.member(mo105LA(1)) && _tokenSet_22.member(mo105LA(2))) {
            int length2 = this.text.length();
            mWS(false);
            this.text.setLength(length2);
        } else if (!_tokenSet_22.member(mo105LA(1))) {
            throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
        }
        char mo105LA = mo105LA(1);
        if (mo105LA != '\t' && mo105LA != '\n' && mo105LA != '\r' && mo105LA != ' ' && mo105LA != '\"' && mo105LA != '$' && mo105LA != ']' && mo105LA != '_') {
            switch (mo105LA) {
                case '\'':
                case ')':
                    break;
                case '(':
                    match('(');
                    if (_tokenSet_4.member(mo105LA(1)) && _tokenSet_23.member(mo105LA(2)) && mo105LA(3) >= 3 && mo105LA(3) <= 255) {
                        int length3 = this.text.length();
                        mWS(false);
                        this.text.setLength(length3);
                    } else if (!_tokenSet_23.member(mo105LA(1)) || mo105LA(2) < 3 || mo105LA(2) > 255) {
                        throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                    }
                    while (_tokenSet_24.member(mo105LA(1)) && mo105LA(2) >= 3 && mo105LA(2) <= 255 && mo105LA(3) >= 3 && mo105LA(3) <= 255) {
                        while (true) {
                            mTEXT_ARG(false);
                            if (mo105LA(1) == ',') {
                                match(',');
                            }
                        }
                    }
                    char mo105LA2 = mo105LA(1);
                    if (mo105LA2 == '\t' || mo105LA2 == '\n' || mo105LA2 == '\r' || mo105LA2 == ' ') {
                        int length4 = this.text.length();
                        mWS(false);
                        this.text.setLength(length4);
                    } else if (mo105LA2 != ')') {
                        throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                    }
                    match(')');
                    break;
                default:
                    switch (mo105LA) {
                        case '+':
                        case ',':
                            break;
                        case '-':
                            match("->");
                            mTEXT_ARG_ID_ELEMENT(false);
                            break;
                        case '.':
                            match('.');
                            mTEXT_ARG_ID_ELEMENT(false);
                            break;
                        default:
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
                                            int i = 0;
                                            while (mo105LA(1) == '[') {
                                                match('[');
                                                if (_tokenSet_4.member(mo105LA(1)) && _tokenSet_24.member(mo105LA(2)) && mo105LA(3) >= 3 && mo105LA(3) <= 255) {
                                                    int length5 = this.text.length();
                                                    mWS(false);
                                                    this.text.setLength(length5);
                                                } else if (!_tokenSet_24.member(mo105LA(1)) || mo105LA(2) < 3 || mo105LA(2) > 255 || mo105LA(3) < 3 || mo105LA(3) > 255) {
                                                    throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                                                }
                                                mTEXT_ARG(false);
                                                char mo105LA3 = mo105LA(1);
                                                if (mo105LA3 == '\t' || mo105LA3 == '\n' || mo105LA3 == '\r' || mo105LA3 == ' ') {
                                                    int length6 = this.text.length();
                                                    mWS(false);
                                                    this.text.setLength(length6);
                                                } else if (mo105LA3 != ']') {
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
                                                    throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                                            }
                                    }
                            }
                    }
            }
        }
        if (z) {
            token = makeToken(15);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    public final void mTEXT_ITEM(boolean z) {
        Token token;
        StringBuilder sb;
        String str;
        String m0a;
        Token token2;
        StringBuilder sb2;
        String str2;
        Token token3;
        String ruleName;
        StringBuilder sb3;
        String str3;
        Token token4;
        Token token5;
        int length = this.text.length();
        if (mo105LA(1) == '$' && mo105LA(2) == 'F' && mo105LA(3) == 'O') {
            match("$FOLLOW");
            if (!_tokenSet_5.member(mo105LA(1)) || !_tokenSet_6.member(mo105LA(2)) || mo105LA(3) < 3 || mo105LA(3) > 255) {
                token5 = null;
            } else {
                char mo105LA = mo105LA(1);
                if (mo105LA == '\t' || mo105LA == '\n' || mo105LA == '\r' || mo105LA == ' ') {
                    mWS(false);
                } else if (mo105LA != '(') {
                    throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                }
                match('(');
                mTEXT_ARG(true);
                token5 = this._returnToken;
                match(')');
            }
            ruleName = this.currentRule.getRuleName();
            if (token5 != null) {
                ruleName = token5.getText();
            }
            m0a = this.generator.getFOLLOWBitSet(ruleName, 1);
            if (m0a == null) {
                sb3 = new StringBuilder();
                str3 = "$FOLLOW(";
                sb3.append(str3);
                sb3.append(ruleName);
                sb3.append("): unknown rule or bad lookahead computation");
                reportError(sb3.toString());
            }
            this.text.setLength(length);
            this.text.append(m0a);
        } else {
            if (mo105LA(1) == '$' && mo105LA(2) == 'F' && mo105LA(3) == 'I') {
                match("$FIRST");
                if (!_tokenSet_5.member(mo105LA(1)) || !_tokenSet_6.member(mo105LA(2)) || mo105LA(3) < 3 || mo105LA(3) > 255) {
                    token3 = null;
                } else {
                    char mo105LA2 = mo105LA(1);
                    if (mo105LA2 == '\t' || mo105LA2 == '\n' || mo105LA2 == '\r' || mo105LA2 == ' ') {
                        mWS(false);
                    } else if (mo105LA2 != '(') {
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
                m0a = this.generator.getFIRSTBitSet(ruleName, 1);
                if (m0a == null) {
                    sb3 = new StringBuilder();
                    str3 = "$FIRST(";
                    sb3.append(str3);
                    sb3.append(ruleName);
                    sb3.append("): unknown rule or bad lookahead computation");
                    reportError(sb3.toString());
                }
            } else {
                if (mo105LA(1) == '$' && mo105LA(2) == 'a') {
                    match("$append");
                    char mo105LA3 = mo105LA(1);
                    if (mo105LA3 == '\t' || mo105LA3 == '\n' || mo105LA3 == '\r' || mo105LA3 == ' ') {
                        mWS(false);
                    } else if (mo105LA3 != '(') {
                        throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                    }
                    match('(');
                    mTEXT_ARG(true);
                    token2 = this._returnToken;
                    match(')');
                    sb2 = new StringBuilder();
                    str2 = "text.Append(";
                } else if (mo105LA(1) == '$' && mo105LA(2) == 's') {
                    match("$set");
                    if (mo105LA(1) == 'T' && mo105LA(2) == 'e') {
                        match("Text");
                        char mo105LA4 = mo105LA(1);
                        if (mo105LA4 == '\t' || mo105LA4 == '\n' || mo105LA4 == '\r' || mo105LA4 == ' ') {
                            mWS(false);
                        } else if (mo105LA4 != '(') {
                            throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                        }
                        match('(');
                        mTEXT_ARG(true);
                        token2 = this._returnToken;
                        match(')');
                        sb2 = new StringBuilder();
                        str2 = "text.Length = _begin; text.Append(";
                    } else {
                        if (mo105LA(1) == 'T' && mo105LA(2) == 'o') {
                            match("Token");
                            char mo105LA5 = mo105LA(1);
                            if (mo105LA5 == '\t' || mo105LA5 == '\n' || mo105LA5 == '\r' || mo105LA5 == ' ') {
                                mWS(false);
                            } else if (mo105LA5 != '(') {
                                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                            }
                            match('(');
                            mTEXT_ARG(true);
                            token = this._returnToken;
                            match(')');
                            sb = new StringBuilder();
                            str = "_token = ";
                        } else {
                            if (mo105LA(1) != 'T' || mo105LA(2) != 'y') {
                                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                            }
                            match("Type");
                            char mo105LA6 = mo105LA(1);
                            if (mo105LA6 == '\t' || mo105LA6 == '\n' || mo105LA6 == '\r' || mo105LA6 == ' ') {
                                mWS(false);
                            } else if (mo105LA6 != '(') {
                                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                            }
                            match('(');
                            mTEXT_ARG(true);
                            token = this._returnToken;
                            match(')');
                            sb = new StringBuilder();
                            str = "_ttype = ";
                        }
                        sb.append(str);
                        m0a = C0000a.m0a(token, sb);
                    }
                } else {
                    if (mo105LA(1) != '$' || mo105LA(2) != 'g') {
                        throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                    }
                    match("$getText");
                    this.text.setLength(length);
                    this.text.append("text.ToString(_begin, text.Length-_begin)");
                }
                sb2.append(str2);
                sb2.append(token2.getText());
                sb2.append(")");
                m0a = sb2.toString();
            }
            this.text.setLength(length);
            this.text.append(m0a);
        }
        if (z) {
            token4 = makeToken(7);
            token4.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token4 = null;
        }
        this._returnToken = token4;
    }

    public final void mTREE(boolean z) {
        int i;
        boolean z2;
        Token token;
        boolean z3;
        int length = this.text.length();
        Vector vector = new Vector(10);
        int length2 = this.text.length();
        char c2 = '(';
        match('(');
        this.text.setLength(length2);
        char mo105LA = mo105LA(1);
        if (mo105LA == '\t' || mo105LA == '\n' || mo105LA == '\r' || mo105LA == ' ') {
            int length3 = this.text.length();
            mWS(false);
            this.text.setLength(length3);
        } else if (mo105LA != '(' && mo105LA != '_' && mo105LA != '\"' && mo105LA != '#') {
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
                case '[':
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
                        default:
                            throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                    }
            }
        }
        int length4 = this.text.length();
        mTREE_ELEMENT(true);
        this.text.setLength(length4);
        vector.appendElement(this.generator.processStringForASTConstructor(this._returnToken.getText()));
        char mo105LA2 = mo105LA(1);
        if (mo105LA2 == '\t' || mo105LA2 == '\n' || mo105LA2 == '\r' || mo105LA2 == ' ') {
            i = length;
            z2 = z;
            int length5 = this.text.length();
            mWS(false);
            this.text.setLength(length5);
        } else {
            if (mo105LA2 != ')' && mo105LA2 != ',') {
                throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
            }
            i = length;
            z2 = z;
        }
        while (mo105LA(1) == ',') {
            int length6 = this.text.length();
            match(',');
            this.text.setLength(length6);
            char mo105LA3 = mo105LA(1);
            if (mo105LA3 == '\t' || mo105LA3 == '\n' || mo105LA3 == '\r' || mo105LA3 == ' ') {
                int length7 = this.text.length();
                z3 = false;
                mWS(false);
                this.text.setLength(length7);
            } else {
                if (mo105LA3 != c2 && mo105LA3 != '_' && mo105LA3 != '\"' && mo105LA3 != '#') {
                    switch (mo105LA3) {
                        default:
                            switch (mo105LA3) {
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
            vector.appendElement(this.generator.processStringForASTConstructor(this._returnToken.getText()));
            char mo105LA4 = mo105LA(1);
            if (mo105LA4 == '\t' || mo105LA4 == '\n' || mo105LA4 == '\r' || mo105LA4 == ' ') {
                boolean z4 = z3;
                c2 = '(';
                int length9 = this.text.length();
                mWS(z4);
                this.text.setLength(length9);
            } else {
                if (mo105LA4 != ')' && mo105LA4 != ',') {
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
            token = makeToken(8);
            token.setText(new String(this.text.getBuffer(), i, this.text.length() - i));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    /* JADX WARN: Code restructure failed: missing block: B:26:0x0092, code lost:
    
        if (r1 != null) goto L38;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void mTREE_ELEMENT(boolean z) {
        String str;
        int length = this.text.length();
        char mo105LA = mo105LA(1);
        Token token = null;
        if (mo105LA != '\"') {
            if (mo105LA != '(') {
                if (mo105LA != '_') {
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
                            mAST_CONSTRUCTOR(false);
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
                                default:
                                    if (mo105LA(1) != '#' || mo105LA(2) != '(') {
                                        if (mo105LA(1) != '#' || mo105LA(2) != '[') {
                                            if (mo105LA(1) == '#' && _tokenSet_13.member(mo105LA(2))) {
                                                int length2 = this.text.length();
                                                match('#');
                                                this.text.setLength(length2);
                                                boolean mID_ELEMENT = mID_ELEMENT(true);
                                                Token token2 = this._returnToken;
                                                if (!mID_ELEMENT) {
                                                    str = this.generator.mapTreeId(token2.getText(), null);
                                                    break;
                                                }
                                            } else {
                                                if (mo105LA(1) != '#' || mo105LA(2) != '#') {
                                                    throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                                                }
                                                str = "##";
                                                match("##");
                                                if (this.currentRule != null) {
                                                    str = this.currentRule.getRuleName() + "_AST";
                                                } else {
                                                    reportError("\"##\" not valid in this context");
                                                }
                                            }
                                            this.text.setLength(length);
                                            this.text.append(str);
                                            break;
                                        } else {
                                            int length3 = this.text.length();
                                            match('#');
                                            this.text.setLength(length3);
                                            mAST_CONSTRUCTOR(false);
                                            break;
                                        }
                                    } else {
                                        int length4 = this.text.length();
                                        match('#');
                                        this.text.setLength(length4);
                                        break;
                                    }
                                    break;
                            }
                    }
                }
                mID_ELEMENT(false);
            }
            mTREE(false);
        } else {
            mSTRING(false);
        }
        if (z) {
            token = makeToken(9);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        }
        this._returnToken = token;
    }

    public final void mVAR_ASSIGN(boolean z) {
        Token token;
        ActionTransInfo actionTransInfo;
        int length = this.text.length();
        match('=');
        if (mo105LA(1) != '=' && (actionTransInfo = this.transInfo) != null && actionTransInfo.refRuleRoot != null) {
            actionTransInfo.assignToRoot = true;
        }
        if (z) {
            token = makeToken(18);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    public final void mWS(boolean z) {
        Token token;
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
            token = makeToken(28);
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
