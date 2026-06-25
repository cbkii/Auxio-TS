package antlr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import p054a.p055a.p056a.p003a.C0000a;

/* loaded from: classes3.dex */
public class ImportVocabTokenManager extends SimpleTokenManager implements Cloneable {
    public String filename;
    public Grammar grammar;

    public ImportVocabTokenManager(Grammar grammar, String str, String str2, Tool tool) {
        super(str2, tool);
        Tool tool2;
        StringBuilder m5a;
        this.grammar = grammar;
        this.filename = str;
        File file = new File(this.filename);
        if (!file.exists()) {
            file = new File(this.antlrTool.getOutputDirectory(), this.filename);
            if (!file.exists()) {
                Tool tool3 = this.antlrTool;
                StringBuilder m5a2 = C0000a.m5a("Cannot find importVocab file '");
                m5a2.append(this.filename);
                m5a2.append("'");
                tool3.panic(m5a2.toString());
            }
        }
        setReadOnly(true);
        try {
            ANTLRTokdefParser aNTLRTokdefParser = new ANTLRTokdefParser(new ANTLRTokdefLexer(new BufferedReader(new FileReader(file))));
            aNTLRTokdefParser.setTool(this.antlrTool);
            aNTLRTokdefParser.setFilename(this.filename);
            aNTLRTokdefParser.file(this);
        } catch (RecognitionException e) {
            Tool tool4 = this.antlrTool;
            StringBuilder m5a3 = C0000a.m5a("Error parsing importVocab file '");
            m5a3.append(this.filename);
            m5a3.append("': ");
            m5a3.append(e.toString());
            tool4.panic(m5a3.toString());
        } catch (TokenStreamException unused) {
            tool2 = this.antlrTool;
            m5a = C0000a.m5a("Error reading importVocab file '");
            m5a.append(this.filename);
            m5a.append("'");
            tool2.panic(m5a.toString());
        } catch (FileNotFoundException unused2) {
            tool2 = this.antlrTool;
            m5a = C0000a.m5a("Cannot find importVocab file '");
            m5a.append(this.filename);
            m5a.append("'");
            tool2.panic(m5a.toString());
        }
    }

    @Override // antlr.SimpleTokenManager, antlr.TokenManager
    public Object clone() {
        ImportVocabTokenManager importVocabTokenManager = (ImportVocabTokenManager) super.clone();
        importVocabTokenManager.filename = this.filename;
        importVocabTokenManager.grammar = this.grammar;
        return importVocabTokenManager;
    }

    @Override // antlr.SimpleTokenManager, antlr.TokenManager
    public void define(TokenSymbol tokenSymbol) {
        super.define(tokenSymbol);
    }

    public void define(String str, int i) {
        TokenSymbol stringLiteralSymbol = str.startsWith("\"") ? new StringLiteralSymbol(str) : new TokenSymbol(str);
        stringLiteralSymbol.setTokenType(i);
        super.define(stringLiteralSymbol);
        int i2 = i + 1;
        int i3 = this.maxToken;
        if (i2 > i3) {
            i3 = i2;
        }
        this.maxToken = i3;
    }

    @Override // antlr.SimpleTokenManager, antlr.TokenManager
    public boolean isReadOnly() {
        return this.readOnly;
    }

    @Override // antlr.SimpleTokenManager, antlr.TokenManager
    public int nextTokenType() {
        return super.nextTokenType();
    }
}
