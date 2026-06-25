package antlr;

/* JADX INFO: loaded from: classes3.dex */
public class TreeBlockContext extends BlockContext {
    public boolean nextElementIsRoot = true;

    @Override // antlr.BlockContext
    public void addAlternativeElement(AlternativeElement alternativeElement) {
        TreeElement treeElement = (TreeElement) this.block;
        if (!this.nextElementIsRoot) {
            super.addAlternativeElement(alternativeElement);
        } else {
            treeElement.root = (GrammarAtom) alternativeElement;
            this.nextElementIsRoot = false;
        }
    }
}
