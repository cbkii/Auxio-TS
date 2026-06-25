package antlr;

import antlr.collections.impl.Vector;
import java.util.Enumeration;
import java.util.Hashtable;
import p054a.p055a.p056a.p003a.C0000a;

/* loaded from: classes3.dex */
public abstract class Grammar {
    public Tool antlrTool;
    public String className;
    public CodeGenerator generator;
    public String superClass;
    public LLkGrammarAnalyzer theLLkAnalyzer;
    public TokenManager tokenManager;
    public boolean buildAST = false;
    public boolean analyzerDebug = false;
    public boolean interactive = false;
    public String exportVocab = null;
    public String importVocab = null;
    public Token preambleAction = new CommonToken(0, "");
    public String fileName = null;
    public Token classMemberAction = new CommonToken(0, "");
    public boolean hasSyntacticPredicate = false;
    public boolean hasUserErrorHandling = false;
    public int maxk = 1;
    public boolean traceRules = false;
    public boolean debuggingOutput = false;
    public boolean defaultErrorHandler = true;
    public String comment = null;
    public Hashtable symbols = new Hashtable();
    public Hashtable options = new Hashtable();
    public Vector rules = new Vector(100);

    public Grammar(String str, Tool tool, String str2) {
        this.superClass = null;
        this.className = null;
        this.className = str;
        this.antlrTool = tool;
        this.superClass = str2;
    }

    public void define(RuleSymbol ruleSymbol) {
        this.rules.appendElement(ruleSymbol);
        this.symbols.put(ruleSymbol.getId(), ruleSymbol);
    }

    public abstract void generate();

    public String getClassName() {
        return this.className;
    }

    public boolean getDefaultErrorHandler() {
        return this.defaultErrorHandler;
    }

    public String getFilename() {
        return this.fileName;
    }

    public int getIntegerOption(String str) {
        Token token = (Token) this.options.get(str);
        if (token == null || token.getType() != 20) {
            throw new NumberFormatException();
        }
        return Integer.parseInt(token.getText());
    }

    public Token getOption(String str) {
        return (Token) this.options.get(str);
    }

    public abstract String getSuperClass();

    public GrammarSymbol getSymbol(String str) {
        return (GrammarSymbol) this.symbols.get(str);
    }

    public Enumeration getSymbols() {
        return this.symbols.elements();
    }

    public boolean hasOption(String str) {
        return this.options.containsKey(str);
    }

    public boolean isDefined(String str) {
        return this.symbols.containsKey(str);
    }

    public abstract void processArguments(String[] strArr);

    public void setCodeGenerator(CodeGenerator codeGenerator) {
        this.generator = codeGenerator;
    }

    public void setFilename(String str) {
        this.fileName = str;
    }

    public void setGrammarAnalyzer(LLkGrammarAnalyzer lLkGrammarAnalyzer) {
        this.theLLkAnalyzer = lLkGrammarAnalyzer;
    }

    public boolean setOption(String str, Token token) {
        this.options.put(str, token);
        String text = token.getText();
        if (str.equals("k")) {
            try {
                this.maxk = getIntegerOption("k");
                if (this.maxk <= 0) {
                    this.antlrTool.error("option 'k' must be greater than 0 (was " + token.getText() + ")", getFilename(), token.getLine(), token.getColumn());
                    this.maxk = 1;
                }
            } catch (NumberFormatException unused) {
                Tool tool = this.antlrTool;
                StringBuilder m5a = C0000a.m5a("option 'k' must be an integer (was ");
                m5a.append(token.getText());
                m5a.append(")");
                tool.error(m5a.toString(), getFilename(), token.getLine(), token.getColumn());
            }
            return true;
        }
        if (str.equals("codeGenMakeSwitchThreshold")) {
            try {
                getIntegerOption("codeGenMakeSwitchThreshold");
            } catch (NumberFormatException unused2) {
                this.antlrTool.error("option 'codeGenMakeSwitchThreshold' must be an integer", getFilename(), token.getLine(), token.getColumn());
            }
            return true;
        }
        if (str.equals("codeGenBitsetTestThreshold")) {
            try {
                getIntegerOption("codeGenBitsetTestThreshold");
            } catch (NumberFormatException unused3) {
                this.antlrTool.error("option 'codeGenBitsetTestThreshold' must be an integer", getFilename(), token.getLine(), token.getColumn());
            }
            return true;
        }
        if (str.equals("defaultErrorHandler")) {
            if (text.equals("true")) {
                this.defaultErrorHandler = true;
            } else if (text.equals("false")) {
                this.defaultErrorHandler = false;
            } else {
                this.antlrTool.error("Value for defaultErrorHandler must be true or false", getFilename(), token.getLine(), token.getColumn());
            }
            return true;
        }
        if (str.equals("analyzerDebug")) {
            if (text.equals("true")) {
                this.analyzerDebug = true;
            } else if (text.equals("false")) {
                this.analyzerDebug = false;
            } else {
                this.antlrTool.error("option 'analyzerDebug' must be true or false", getFilename(), token.getLine(), token.getColumn());
            }
            return true;
        }
        if (!str.equals("codeGenDebug")) {
            return str.equals("classHeaderSuffix") || str.equals("classHeaderPrefix") || str.equals("namespaceAntlr") || str.equals("namespaceStd") || str.equals("genHashLines") || str.equals("noConstructors");
        }
        if (text.equals("true")) {
            this.analyzerDebug = true;
        } else if (text.equals("false")) {
            this.analyzerDebug = false;
        } else {
            this.antlrTool.error("option 'codeGenDebug' must be true or false", getFilename(), token.getLine(), token.getColumn());
        }
        return true;
    }

    public void setTokenManager(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer(20000);
        Enumeration elements = this.rules.elements();
        while (elements.hasMoreElements()) {
            RuleSymbol ruleSymbol = (RuleSymbol) elements.nextElement();
            if (!ruleSymbol.f304id.equals("mnextToken")) {
                stringBuffer.append(ruleSymbol.getBlock().toString());
                stringBuffer.append("\n\n");
            }
        }
        return stringBuffer.toString();
    }
}
