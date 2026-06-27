package antlr;

import antlr.collections.impl.BitSet;
import java.io.PrintStream;
import java.util.Hashtable;
import p000a.p001a.p002a.p003a.C0000a;

/* JADX INFO: loaded from: classes3.dex */
public abstract class CharScanner implements TokenStream {
    public static final char EOF_CHAR = 65535;
    public static final char NO_CHAR = 0;
    public Token _returnToken;
    public boolean caseSensitive;
    public boolean caseSensitiveLiterals;
    public boolean commitToPath;
    public ANTLRHashString hashString;
    public LexerSharedInputState inputState;
    public Hashtable literals;
    public boolean saveConsumedInput;
    public int tabsize;
    public ANTLRStringBuffer text;
    public Class tokenObjectClass;
    public int traceDepth;

    public CharScanner() {
        this.saveConsumedInput = true;
        this.caseSensitive = true;
        this.caseSensitiveLiterals = true;
        this.tabsize = 8;
        this._returnToken = null;
        this.commitToPath = false;
        this.traceDepth = 0;
        this.text = new ANTLRStringBuffer();
        this.hashString = new ANTLRHashString(this);
        setTokenObjectClass("antlr.CommonToken");
    }

    public CharScanner(InputBuffer inputBuffer) {
        this();
        this.inputState = new LexerSharedInputState(inputBuffer);
    }

    public CharScanner(LexerSharedInputState lexerSharedInputState) {
        this();
        this.inputState = lexerSharedInputState;
    }

    /* JADX INFO: renamed from: LA */
    public char mo105LA(int i) {
        return this.caseSensitive ? this.inputState.input.mo106LA(i) : toLower(this.inputState.input.mo106LA(i));
    }

    public void append(char c2) {
        if (this.saveConsumedInput) {
            this.text.append(c2);
        }
    }

    public void append(String str) {
        if (this.saveConsumedInput) {
            this.text.append(str);
        }
    }

    public void commit() {
        this.inputState.input.commit();
    }

    public void consume() {
        if (this.inputState.guessing == 0) {
            char cMo105LA = mo105LA(1);
            if (this.caseSensitive) {
                append(cMo105LA);
            } else {
                append(this.inputState.input.mo106LA(1));
            }
            if (cMo105LA == '\t') {
                tab();
            } else {
                this.inputState.column++;
            }
        }
        this.inputState.input.consume();
    }

    public void consumeUntil(int i) {
        while (mo105LA(1) != 65535 && mo105LA(1) != i) {
            consume();
        }
    }

    public void consumeUntil(BitSet bitSet) {
        while (mo105LA(1) != 65535 && !bitSet.member(mo105LA(1))) {
            consume();
        }
    }

    public boolean getCaseSensitive() {
        return this.caseSensitive;
    }

    public final boolean getCaseSensitiveLiterals() {
        return this.caseSensitiveLiterals;
    }

    public int getColumn() {
        return this.inputState.column;
    }

    public boolean getCommitToPath() {
        return this.commitToPath;
    }

    public String getFilename() {
        return this.inputState.filename;
    }

    public InputBuffer getInputBuffer() {
        return this.inputState.input;
    }

    public LexerSharedInputState getInputState() {
        return this.inputState;
    }

    public int getLine() {
        return this.inputState.line;
    }

    public int getTabSize() {
        return this.tabsize;
    }

    public String getText() {
        return this.text.toString();
    }

    public Token getTokenObject() {
        return this._returnToken;
    }

    public Token makeToken(int i) {
        String str;
        try {
            Token token = (Token) this.tokenObjectClass.newInstance();
            token.setType(i);
            token.setColumn(this.inputState.tokenStartColumn);
            token.setLine(this.inputState.tokenStartLine);
            return token;
        } catch (IllegalAccessException unused) {
            str = "Token class is not accessible";
            StringBuilder sbM5a = C0000a.m5a(str);
            sbM5a.append(this.tokenObjectClass);
            panic(sbM5a.toString());
            return Token.badToken;
        } catch (InstantiationException unused2) {
            str = "can't instantiate token: ";
            StringBuilder sbM5a2 = C0000a.m5a(str);
            sbM5a2.append(this.tokenObjectClass);
            panic(sbM5a2.toString());
            return Token.badToken;
        }
    }

    public int mark() {
        return this.inputState.input.mark();
    }

    public void match(char c2) {
        if (mo105LA(1) != c2) {
            throw new MismatchedCharException(mo105LA(1), c2, false, this);
        }
        consume();
    }

    public void match(BitSet bitSet) throws MismatchedCharException {
        if (!bitSet.member(mo105LA(1))) {
            throw new MismatchedCharException(mo105LA(1), bitSet, false, this);
        }
        consume();
    }

    public void match(String str) {
        int length = str.length();
        for (int i = 0; i < length; i++) {
            if (mo105LA(1) != str.charAt(i)) {
                throw new MismatchedCharException(mo105LA(1), str.charAt(i), false, this);
            }
            consume();
        }
    }

    public void matchNot(char c2) throws MismatchedCharException {
        if (mo105LA(1) == c2) {
            throw new MismatchedCharException(mo105LA(1), c2, true, this);
        }
        consume();
    }

    public void matchRange(char c2, char c3) {
        if (mo105LA(1) < c2 || mo105LA(1) > c3) {
            throw new MismatchedCharException(mo105LA(1), c2, c3, false, this);
        }
        consume();
    }

    public void newline() {
        LexerSharedInputState lexerSharedInputState = this.inputState;
        lexerSharedInputState.line++;
        lexerSharedInputState.column = 1;
    }

    public void panic() {
        System.err.println("CharScanner: panic");
        Utils.error("");
        throw null;
    }

    public void panic(String str) {
        System.err.println("CharScanner; panic: " + str);
        Utils.error(str);
        throw null;
    }

    public void reportError(RecognitionException recognitionException) {
        System.err.println(recognitionException);
    }

    public void reportError(String str) {
        if (getFilename() == null) {
            System.err.println("error: " + str);
            return;
        }
        System.err.println(getFilename() + ": error: " + str);
    }

    public void reportWarning(String str) {
        if (getFilename() == null) {
            System.err.println("warning: " + str);
            return;
        }
        System.err.println(getFilename() + ": warning: " + str);
    }

    public void resetText() {
        this.text.setLength(0);
        LexerSharedInputState lexerSharedInputState = this.inputState;
        lexerSharedInputState.tokenStartColumn = lexerSharedInputState.column;
        lexerSharedInputState.tokenStartLine = lexerSharedInputState.line;
    }

    public void rewind(int i) {
        this.inputState.input.rewind(i);
    }

    public void setCaseSensitive(boolean z) {
        this.caseSensitive = z;
    }

    public void setColumn(int i) {
        this.inputState.column = i;
    }

    public void setCommitToPath(boolean z) {
        this.commitToPath = z;
    }

    public void setFilename(String str) {
        this.inputState.filename = str;
    }

    public void setInputState(LexerSharedInputState lexerSharedInputState) {
        this.inputState = lexerSharedInputState;
    }

    public void setLine(int i) {
        this.inputState.line = i;
    }

    public void setTabSize(int i) {
        this.tabsize = i;
    }

    public void setText(String str) {
        resetText();
        this.text.append(str);
    }

    public void setTokenObjectClass(String str) {
        try {
            this.tokenObjectClass = Utils.loadClass(str);
        } catch (ClassNotFoundException unused) {
            panic("ClassNotFoundException: " + str);
        }
    }

    public void tab() {
        int column = getColumn() - 1;
        int i = this.tabsize;
        setColumn((((column / i) + 1) * i) + 1);
    }

    public int testLiteralsTable(int i) {
        this.hashString.setBuffer(this.text.getBuffer(), this.text.length());
        Integer num = (Integer) this.literals.get(this.hashString);
        return num != null ? num.intValue() : i;
    }

    public int testLiteralsTable(String str, int i) {
        Integer num = (Integer) this.literals.get(new ANTLRHashString(str, this));
        return num != null ? num.intValue() : i;
    }

    public char toLower(char c2) {
        return Character.toLowerCase(c2);
    }

    public void traceIn(String str) {
        this.traceDepth++;
        traceIndent();
        PrintStream printStream = System.out;
        StringBuilder sbM10b = C0000a.m10b("> lexer ", str, "; c==");
        sbM10b.append(mo105LA(1));
        printStream.println(sbM10b.toString());
    }

    public void traceIndent() {
        for (int i = 0; i < this.traceDepth; i++) {
            System.out.print(" ");
        }
    }

    public void traceOut(String str) {
        traceIndent();
        PrintStream printStream = System.out;
        StringBuilder sbM10b = C0000a.m10b("< lexer ", str, "; c==");
        sbM10b.append(mo105LA(1));
        printStream.println(sbM10b.toString());
        this.traceDepth--;
    }

    public void uponEOF() {
    }
}
