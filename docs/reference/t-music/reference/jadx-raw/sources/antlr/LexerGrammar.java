package antlr;

import antlr.collections.impl.BitSet;
import p000a.p001a.p002a.p003a.C0000a;

/* loaded from: classes3.dex */
public class LexerGrammar extends Grammar {
    public boolean caseSensitive;
    public boolean caseSensitiveLiterals;
    public BitSet charVocabulary;
    public boolean filterMode;
    public String filterRule;
    public boolean testLiterals;

    public LexerGrammar(String str, Tool tool, String str2) {
        super(str, tool, str2);
        this.testLiterals = true;
        this.caseSensitiveLiterals = true;
        this.caseSensitive = true;
        this.filterMode = false;
        this.filterRule = null;
        BitSet bitSet = new BitSet();
        for (int i = 0; i <= 127; i++) {
            bitSet.add(i);
        }
        setCharVocabulary(bitSet);
        this.defaultErrorHandler = false;
    }

    @Override // antlr.Grammar
    public void generate() {
        this.generator.gen(this);
    }

    @Override // antlr.Grammar
    public String getSuperClass() {
        return this.debuggingOutput ? "debug.DebuggingCharScanner" : "CharScanner";
    }

    public boolean getTestLiterals() {
        return this.testLiterals;
    }

    @Override // antlr.Grammar
    public void processArguments(String[] strArr) {
        for (int i = 0; i < strArr.length; i++) {
            if (strArr[i].equals("-trace") || strArr[i].equals("-traceLexer")) {
                this.traceRules = true;
            } else if (strArr[i].equals("-debug")) {
                this.debuggingOutput = true;
            }
            this.antlrTool.setArgOK(i);
        }
    }

    public void setCharVocabulary(BitSet bitSet) {
        this.charVocabulary = bitSet;
    }

    @Override // antlr.Grammar
    public boolean setOption(String str, Token token) {
        Tool tool;
        String filename;
        int line;
        int column;
        String str2;
        String text = token.getText();
        if (str.equals("buildAST")) {
            tool = this.antlrTool;
            filename = getFilename();
            line = token.getLine();
            column = token.getColumn();
            str2 = "buildAST option is not valid for lexer";
        } else {
            if (str.equals("testLiterals")) {
                if (text.equals("true")) {
                    this.testLiterals = true;
                } else if (text.equals("false")) {
                    this.testLiterals = false;
                } else {
                    this.antlrTool.warning("testLiterals option must be true or false", getFilename(), token.getLine(), token.getColumn());
                }
                return true;
            }
            if (str.equals("interactive")) {
                if (text.equals("true")) {
                    this.interactive = true;
                } else if (text.equals("false")) {
                    this.interactive = false;
                } else {
                    this.antlrTool.error("interactive option must be true or false", getFilename(), token.getLine(), token.getColumn());
                }
                return true;
            }
            if (str.equals("caseSensitive")) {
                if (text.equals("true")) {
                    this.caseSensitive = true;
                } else if (text.equals("false")) {
                    this.caseSensitive = false;
                } else {
                    this.antlrTool.warning("caseSensitive option must be true or false", getFilename(), token.getLine(), token.getColumn());
                }
                return true;
            }
            if (str.equals("caseSensitiveLiterals")) {
                if (text.equals("true")) {
                    this.caseSensitiveLiterals = true;
                } else if (text.equals("false")) {
                    this.caseSensitiveLiterals = false;
                } else {
                    this.antlrTool.warning("caseSensitiveLiterals option must be true or false", getFilename(), token.getLine(), token.getColumn());
                }
                return true;
            }
            if (str.equals("filter")) {
                if (text.equals("true")) {
                    this.filterMode = true;
                } else if (text.equals("false")) {
                    this.filterMode = false;
                } else if (token.getType() == 24) {
                    this.filterMode = true;
                    this.filterRule = text;
                } else {
                    this.antlrTool.warning("filter option must be true, false, or a lexer rule name", getFilename(), token.getLine(), token.getColumn());
                }
                return true;
            }
            if (!str.equals("longestPossible")) {
                if (str.equals("className")) {
                    super.setOption(str, token);
                    return true;
                }
                if (super.setOption(str, token)) {
                    return true;
                }
                this.antlrTool.error(C0000a.m1a("Invalid option: ", str), getFilename(), token.getLine(), token.getColumn());
                return false;
            }
            tool = this.antlrTool;
            filename = getFilename();
            line = token.getLine();
            column = token.getColumn();
            str2 = "longestPossible option has been deprecated; ignoring it...";
        }
        tool.warning(str2, filename, line, column);
        return true;
    }
}
