package antlr;

/* loaded from: classes3.dex */
public class StringLiteralSymbol extends TokenSymbol {
    public String label;

    public StringLiteralSymbol(String str) {
        super(str);
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String str) {
        this.label = str;
    }
}
