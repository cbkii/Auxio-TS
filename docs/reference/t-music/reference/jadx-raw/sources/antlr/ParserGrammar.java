package antlr;

import p000a.p001a.p002a.p003a.C0000a;

/* loaded from: classes3.dex */
public class ParserGrammar extends Grammar {
    public ParserGrammar(String str, Tool tool, String str2) {
        super(str, tool, str2);
    }

    @Override // antlr.Grammar
    public void generate() {
        this.generator.gen(this);
    }

    @Override // antlr.Grammar
    public String getSuperClass() {
        return this.debuggingOutput ? "debug.LLkDebuggingParser" : "LLkParser";
    }

    @Override // antlr.Grammar
    public void processArguments(String[] strArr) {
        for (int i = 0; i < strArr.length; i++) {
            if (strArr[i].equals("-trace") || strArr[i].equals("-traceParser")) {
                this.traceRules = true;
            } else if (strArr[i].equals("-debug")) {
                this.debuggingOutput = true;
            }
            this.antlrTool.setArgOK(i);
        }
    }

    @Override // antlr.Grammar
    public boolean setOption(String str, Token token) {
        String text = token.getText();
        if (str.equals("buildAST")) {
            if (text.equals("true")) {
                this.buildAST = true;
            } else if (text.equals("false")) {
                this.buildAST = false;
            } else {
                this.antlrTool.error("buildAST option must be true or false", getFilename(), token.getLine(), token.getColumn());
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
        if (str.equals("ASTLabelType")) {
            super.setOption(str, token);
            return true;
        }
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
}
