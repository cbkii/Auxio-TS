package antlr;

/* JADX INFO: loaded from: classes3.dex */
public class CppBlockFinishingInfo {
    public boolean generatedAnIf;
    public boolean generatedSwitch;
    public boolean needAnErrorClause;
    public String postscript;

    public CppBlockFinishingInfo() {
        this.postscript = null;
        this.generatedSwitch = false;
        this.needAnErrorClause = true;
    }

    public CppBlockFinishingInfo(String str, boolean z, boolean z2, boolean z3) {
        this.postscript = str;
        this.generatedSwitch = z;
        this.generatedAnIf = z2;
        this.needAnErrorClause = z3;
    }
}
