package antlr;

/* loaded from: classes3.dex */
public class Alternative {
    public Lookahead[] cache;
    public boolean doAutoGen;
    public ExceptionSpec exceptionSpec;
    public AlternativeElement head;
    public int lookaheadDepth;
    public String semPred;
    public SynPredBlock synPred;
    public AlternativeElement tail;
    public Token treeSpecifier = null;

    public Alternative() {
    }

    public Alternative(AlternativeElement alternativeElement) {
        addElement(alternativeElement);
    }

    public void addElement(AlternativeElement alternativeElement) {
        if (this.head == null) {
            this.tail = alternativeElement;
            this.head = alternativeElement;
        } else {
            this.tail.next = alternativeElement;
            this.tail = alternativeElement;
        }
    }

    public boolean atStart() {
        return this.head == null;
    }

    public boolean getAutoGen() {
        return this.doAutoGen && this.treeSpecifier == null;
    }

    public Token getTreeSpecifier() {
        return this.treeSpecifier;
    }

    public void setAutoGen(boolean z) {
        this.doAutoGen = z;
    }
}
