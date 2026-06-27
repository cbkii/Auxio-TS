package antlr;

/* JADX INFO: loaded from: classes3.dex */
public class TokenSymbol extends GrammarSymbol {
    public String ASTNodeType;
    public String paraphrase;
    public int ttype;

    public TokenSymbol(String str) {
        super(str);
        this.paraphrase = null;
        this.ttype = 0;
    }

    public String getASTNodeType() {
        return this.ASTNodeType;
    }

    public String getParaphrase() {
        return this.paraphrase;
    }

    public int getTokenType() {
        return this.ttype;
    }

    public void setASTNodeType(String str) {
        this.ASTNodeType = str;
    }

    public void setParaphrase(String str) {
        this.paraphrase = str;
    }

    public void setTokenType(int i) {
        this.ttype = i;
    }
}
