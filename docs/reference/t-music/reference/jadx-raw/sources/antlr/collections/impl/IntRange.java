package antlr.collections.impl;

/* loaded from: classes3.dex */
public class IntRange {
    public int begin;
    public int end;

    public IntRange(int i, int i2) {
        this.begin = i;
        this.end = i2;
    }

    public String toString() {
        return this.begin + ".." + this.end;
    }
}
