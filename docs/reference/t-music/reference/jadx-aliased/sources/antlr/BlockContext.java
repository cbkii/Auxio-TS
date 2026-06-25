package antlr;

/* loaded from: classes3.dex */
public class BlockContext {
    public int altNum;
    public AlternativeBlock block;
    public BlockEndElement blockEnd;

    public void addAlternativeElement(AlternativeElement alternativeElement) {
        currentAlt().addElement(alternativeElement);
    }

    public Alternative currentAlt() {
        return (Alternative) this.block.alternatives.elementAt(this.altNum);
    }

    public AlternativeElement currentElement() {
        return currentAlt().tail;
    }
}
