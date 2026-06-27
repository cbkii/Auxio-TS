package antlr;

/* loaded from: classes3.dex */
public class PythonBlockFinishingInfo {
    public boolean generatedAnIf;
    public boolean generatedSwitch;
    public boolean needAnErrorClause;
    public String postscript;

    public PythonBlockFinishingInfo() {
        this.postscript = null;
        this.generatedSwitch = false;
        this.generatedSwitch = false;
        this.needAnErrorClause = true;
    }

    public PythonBlockFinishingInfo(String str, boolean z, boolean z2, boolean z3) {
        this.postscript = str;
        this.generatedSwitch = z;
        this.generatedAnIf = z2;
        this.needAnErrorClause = z3;
    }
}
