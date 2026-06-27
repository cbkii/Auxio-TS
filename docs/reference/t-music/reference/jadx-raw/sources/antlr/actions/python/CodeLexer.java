package antlr.actions.python;

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
import antlr.Tool;
import antlr.collections.impl.BitSet;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.Hashtable;

/* loaded from: classes3.dex */
public class CodeLexer extends CharScanner implements CodeLexerTokenTypes, TokenStream {
    public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
    public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
    public Tool antlrTool;
    public int lineOffset;

    public CodeLexer(InputBuffer inputBuffer) {
        this(new LexerSharedInputState(inputBuffer));
    }

    public CodeLexer(LexerSharedInputState lexerSharedInputState) {
        super(lexerSharedInputState);
        this.lineOffset = 0;
        this.caseSensitiveLiterals = true;
        setCaseSensitive(true);
        this.literals = new Hashtable();
    }

    public CodeLexer(InputStream inputStream) {
        this(new ByteBuffer(inputStream));
    }

    public CodeLexer(Reader reader) {
        this(new CharBuffer(reader));
    }

    public CodeLexer(String str, String str2, int i, Tool tool) {
        this(new StringReader(str));
        setLine(i);
        setFilename(str2);
        this.antlrTool = tool;
    }

    public static final long[] mk_tokenSet_0() {
        long[] jArr = new long[8];
        jArr[0] = -145135534866440L;
        for (int i = 1; i <= 3; i++) {
            jArr[i] = -1;
        }
        return jArr;
    }

    public static final long[] mk_tokenSet_1() {
        long[] jArr = new long[8];
        jArr[0] = -140737488364552L;
        for (int i = 1; i <= 3; i++) {
            jArr[i] = -1;
        }
        return jArr;
    }

    public final void mACTION(boolean z) {
        Token token;
        int length = this.text.length();
        while (mo105LA(1) >= 3 && mo105LA(1) <= 255) {
            mSTUFF(false);
        }
        if (z) {
            token = makeToken(4);
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
            token = makeToken(6);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    /* JADX WARN: Code restructure failed: missing block: B:20:0x003d, code lost:
    
        r8 = makeToken(8);
        r8.setText(new java.lang.String(r7.text.getBuffer(), r0, r7.text.length() - r0));
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void mIGNWS(boolean z) {
        int length = this.text.length();
        while (true) {
            if (mo105LA(1) == ' ' && mo105LA(2) >= 3 && mo105LA(2) <= 255) {
                match(' ');
            } else if (mo105LA(1) != '\t' || mo105LA(2) < 3 || mo105LA(2) > 255) {
                break;
            } else {
                match('\t');
            }
        }
        Token token = null;
        this._returnToken = token;
    }

    public final void mML_COMMENT(boolean z) {
        Token token;
        int length = this.text.length();
        int length2 = this.text.length();
        match("/*");
        this.text.setLength(length2);
        this.text.append("#");
        while (true) {
            if (mo105LA(1) == '*' && mo105LA(2) == '/') {
                break;
            }
            if (mo105LA(1) == '\r' && mo105LA(2) == '\n') {
                match('\r');
            } else if (mo105LA(1) == '\r' && mo105LA(2) >= 3 && mo105LA(2) <= 255) {
                match('\r');
                int length3 = this.text.length();
                mIGNWS(false);
                this.text.setLength(length3);
                newline();
                this.text.append("# ");
            } else if (mo105LA(1) != '\n' || mo105LA(2) < 3 || mo105LA(2) > 255) {
                if (mo105LA(1) < 3 || mo105LA(1) > 255 || mo105LA(2) < 3 || mo105LA(2) > 255) {
                    break;
                } else {
                    matchNot(CharScanner.EOF_CHAR);
                }
            }
            match('\n');
            int length32 = this.text.length();
            mIGNWS(false);
            this.text.setLength(length32);
            newline();
            this.text.append("# ");
        }
        this.text.append("\n");
        int length4 = this.text.length();
        match("*/");
        this.text.setLength(length4);
        if (z) {
            token = makeToken(9);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    public final void mSL_COMMENT(boolean z) {
        Token token;
        int length = this.text.length();
        int length2 = this.text.length();
        match("//");
        this.text.setLength(length2);
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
            token = makeToken(7);
            token.setText(new String(this.text.getBuffer(), length, this.text.length() - length));
        } else {
            token = null;
        }
        this._returnToken = token;
    }

    public final void mSTUFF(boolean z) {
        BitSet bitSet;
        Token token;
        int length = this.text.length();
        if (mo105LA(1) == '/' && (mo105LA(2) == '*' || mo105LA(2) == '/')) {
            mCOMMENT(false);
        } else {
            if (mo105LA(1) == '\r' && mo105LA(2) == '\n') {
                match("\r\n");
            } else {
                if (mo105LA(1) == '/' && _tokenSet_0.member(mo105LA(2))) {
                    match('/');
                    bitSet = _tokenSet_0;
                } else if (mo105LA(1) == '\r') {
                    match('\r');
                } else if (mo105LA(1) == '\n') {
                    match('\n');
                } else {
                    if (!_tokenSet_1.member(mo105LA(1))) {
                        throw new NoViableAltForCharException(mo105LA(1), getFilename(), getLine(), getColumn());
                    }
                    bitSet = _tokenSet_1;
                }
                match(bitSet);
            }
            newline();
        }
        if (z) {
            token = makeToken(5);
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
                    mACTION(true);
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
}
