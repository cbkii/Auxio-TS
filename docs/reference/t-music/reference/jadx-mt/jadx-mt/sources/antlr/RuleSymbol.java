package antlr;

import antlr.collections.impl.Vector;

/* JADX INFO: loaded from: classes3.dex */
public class RuleSymbol extends GrammarSymbol {
    public String access;
    public RuleBlock block;
    public String comment;
    public boolean defined;
    public Vector references;

    public RuleSymbol(String str) {
        super(str);
        this.references = new Vector();
    }

    public void addReference(RuleRefElement ruleRefElement) {
        this.references.appendElement(ruleRefElement);
    }

    public RuleBlock getBlock() {
        return this.block;
    }

    public RuleRefElement getReference(int i) {
        return (RuleRefElement) this.references.elementAt(i);
    }

    public boolean isDefined() {
        return this.defined;
    }

    public int numReferences() {
        return this.references.size();
    }

    public void setBlock(RuleBlock ruleBlock) {
        this.block = ruleBlock;
    }

    public void setDefined() {
        this.defined = true;
    }
}
