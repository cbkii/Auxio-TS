package antlr;

/* JADX INFO: loaded from: classes3.dex */
public abstract class AlternativeElement extends GrammarElement {
    public int autoGenType;
    public String enclosingRuleName;
    public AlternativeElement next;

    public AlternativeElement(Grammar grammar) {
        super(grammar);
        this.autoGenType = 1;
    }

    public AlternativeElement(Grammar grammar, Token token) {
        super(grammar, token);
        this.autoGenType = 1;
    }

    public AlternativeElement(Grammar grammar, Token token, int i) {
        super(grammar, token);
        this.autoGenType = 1;
        this.autoGenType = i;
    }

    public int getAutoGenType() {
        return this.autoGenType;
    }

    public String getLabel() {
        return null;
    }

    public void setAutoGenType(int i) {
        this.autoGenType = i;
    }

    public void setLabel(String str) {
    }
}
