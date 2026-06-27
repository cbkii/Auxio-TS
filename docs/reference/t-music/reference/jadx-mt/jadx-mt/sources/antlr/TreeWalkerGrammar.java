package antlr;

import p000a.p001a.p002a.p003a.C0000a;

/* JADX INFO: loaded from: classes3.dex */
public class TreeWalkerGrammar extends Grammar {
    public boolean transform;

    public TreeWalkerGrammar(String str, Tool tool, String str2) {
        super(str, tool, str2);
        this.transform = false;
    }

    @Override // antlr.Grammar
    public void generate() {
        this.generator.gen(this);
    }

    @Override // antlr.Grammar
    public String getSuperClass() {
        return "TreeParser";
    }

    @Override // antlr.Grammar
    public void processArguments(String[] strArr) {
        for (int i = 0; i < strArr.length; i++) {
            if (strArr[i].equals("-trace") || strArr[i].equals("-traceTreeParser")) {
                this.traceRules = true;
                this.antlrTool.setArgOK(i);
            }
        }
    }

    @Override // antlr.Grammar
    public boolean setOption(String str, Token token) {
        if (str.equals("buildAST")) {
            if (token.getText().equals("true")) {
                this.buildAST = true;
            } else if (token.getText().equals("false")) {
                this.buildAST = false;
            } else {
                this.antlrTool.error("buildAST option must be true or false", getFilename(), token.getLine(), token.getColumn());
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
