package antlr.preprocessor;

import antlr.ANTLRException;
import antlr.TokenStreamException;
import antlr.collections.impl.IndexedVector;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Enumeration;
import java.util.Hashtable;
import p054a.p055a.p056a.p003a.C0000a;

/* loaded from: classes3.dex */
public class Hierarchy {
    public Grammar LexerRoot;
    public Grammar ParserRoot;
    public Grammar TreeParserRoot;
    public antlr.Tool antlrTool;
    public Hashtable symbols = new Hashtable(10);
    public Hashtable files = new Hashtable(10);

    public Hierarchy(antlr.Tool tool) {
        this.LexerRoot = null;
        this.ParserRoot = null;
        this.TreeParserRoot = null;
        this.antlrTool = tool;
        this.LexerRoot = new Grammar(tool, "Lexer", null, null);
        this.ParserRoot = new Grammar(tool, "Parser", null, null);
        this.TreeParserRoot = new Grammar(tool, "TreeParser", null, null);
        this.LexerRoot.setPredefined(true);
        this.ParserRoot.setPredefined(true);
        this.TreeParserRoot.setPredefined(true);
        this.symbols.put(this.LexerRoot.getName(), this.LexerRoot);
        this.symbols.put(this.ParserRoot.getName(), this.ParserRoot);
        this.symbols.put(this.TreeParserRoot.getName(), this.TreeParserRoot);
    }

    public static String optionsToString(IndexedVector indexedVector) {
        StringBuilder m5a = C0000a.m5a("options {");
        m5a.append(System.getProperty("line.separator"));
        String sb = m5a.toString();
        Enumeration elements = indexedVector.elements();
        while (elements.hasMoreElements()) {
            StringBuilder m5a2 = C0000a.m5a(sb);
            m5a2.append((Option) elements.nextElement());
            m5a2.append(System.getProperty("line.separator"));
            sb = m5a2.toString();
        }
        StringBuilder m9b = C0000a.m9b(sb, "}");
        m9b.append(System.getProperty("line.separator"));
        m9b.append(System.getProperty("line.separator"));
        return m9b.toString();
    }

    public void addGrammar(Grammar grammar) {
        grammar.setHierarchy(this);
        this.symbols.put(grammar.getName(), grammar);
        getFile(grammar.getFileName()).addGrammar(grammar);
    }

    public void addGrammarFile(GrammarFile grammarFile) {
        this.files.put(grammarFile.getName(), grammarFile);
    }

    public void expandGrammarsInFile(String str) {
        Enumeration elements = getFile(str).getGrammars().elements();
        while (elements.hasMoreElements()) {
            ((Grammar) elements.nextElement()).expandInPlace();
        }
    }

    public Grammar findRoot(Grammar grammar) {
        Grammar superGrammar;
        return (grammar.getSuperGrammarName() == null || (superGrammar = grammar.getSuperGrammar()) == null) ? grammar : findRoot(superGrammar);
    }

    public GrammarFile getFile(String str) {
        return (GrammarFile) this.files.get(str);
    }

    public Grammar getGrammar(String str) {
        return (Grammar) this.symbols.get(str);
    }

    public antlr.Tool getTool() {
        return this.antlrTool;
    }

    public void readGrammarFile(String str) {
        antlr.Tool tool;
        StringBuilder sb;
        String str2;
        BufferedReader bufferedReader = new BufferedReader(new FileReader(str));
        addGrammarFile(new GrammarFile(this.antlrTool, str));
        PreprocessorLexer preprocessorLexer = new PreprocessorLexer(bufferedReader);
        preprocessorLexer.setFilename(str);
        Preprocessor preprocessor = new Preprocessor(preprocessorLexer);
        preprocessor.setTool(this.antlrTool);
        preprocessor.setFilename(str);
        try {
            preprocessor.grammarFile(this, str);
        } catch (TokenStreamException e) {
            e = e;
            tool = this.antlrTool;
            sb = new StringBuilder();
            str2 = "Token stream error reading grammar(s):\n";
            sb.append(str2);
            sb.append(e);
            tool.toolError(sb.toString());
        } catch (ANTLRException e2) {
            e = e2;
            tool = this.antlrTool;
            sb = new StringBuilder();
            str2 = "error reading grammar(s):\n";
            sb.append(str2);
            sb.append(e);
            tool.toolError(sb.toString());
        }
    }

    public void setTool(antlr.Tool tool) {
        this.antlrTool = tool;
    }

    public boolean verifyThatHierarchyIsComplete() {
        Enumeration elements = this.symbols.elements();
        boolean z = true;
        while (elements.hasMoreElements()) {
            Grammar grammar = (Grammar) elements.nextElement();
            if (grammar.getSuperGrammarName() != null && grammar.getSuperGrammar() == null) {
                antlr.Tool tool = this.antlrTool;
                StringBuilder m5a = C0000a.m5a("grammar ");
                m5a.append(grammar.getSuperGrammarName());
                m5a.append(" not defined");
                tool.toolError(m5a.toString());
                this.symbols.remove(grammar.getName());
                z = false;
            }
        }
        if (!z) {
            return false;
        }
        Enumeration elements2 = this.symbols.elements();
        while (elements2.hasMoreElements()) {
            Grammar grammar2 = (Grammar) elements2.nextElement();
            if (grammar2.getSuperGrammarName() != null) {
                grammar2.setType(findRoot(grammar2).getName());
            }
        }
        return true;
    }
}
