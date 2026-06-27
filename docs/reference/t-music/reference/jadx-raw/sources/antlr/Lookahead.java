package antlr;

import antlr.collections.impl.BitSet;
import antlr.collections.impl.Vector;
import p000a.p001a.p002a.p003a.C0000a;

/* loaded from: classes3.dex */
public class Lookahead implements Cloneable {
    public String cycle;
    public BitSet epsilonDepth;
    public BitSet fset;
    public boolean hasEpsilon;

    public Lookahead() {
        this.hasEpsilon = false;
        this.fset = new BitSet();
    }

    public Lookahead(BitSet bitSet) {
        this.hasEpsilon = false;
        this.fset = bitSet;
    }

    public Lookahead(String str) {
        this();
        this.cycle = str;
    }

    /* renamed from: of */
    public static Lookahead m109of(int i) {
        Lookahead lookahead = new Lookahead();
        lookahead.fset.add(i);
        return lookahead;
    }

    public Object clone() {
        try {
            Lookahead lookahead = (Lookahead) super.clone();
            lookahead.fset = (BitSet) this.fset.clone();
            lookahead.cycle = this.cycle;
            if (this.epsilonDepth != null) {
                lookahead.epsilonDepth = (BitSet) this.epsilonDepth.clone();
            }
            return lookahead;
        } catch (CloneNotSupportedException unused) {
            throw new InternalError();
        }
    }

    public void combineWith(Lookahead lookahead) {
        if (this.cycle == null) {
            this.cycle = lookahead.cycle;
        }
        if (lookahead.containsEpsilon()) {
            this.hasEpsilon = true;
        }
        BitSet bitSet = this.epsilonDepth;
        if (bitSet != null) {
            BitSet bitSet2 = lookahead.epsilonDepth;
            if (bitSet2 != null) {
                bitSet.orInPlace(bitSet2);
            }
        } else {
            BitSet bitSet3 = lookahead.epsilonDepth;
            if (bitSet3 != null) {
                this.epsilonDepth = (BitSet) bitSet3.clone();
            }
        }
        this.fset.orInPlace(lookahead.fset);
    }

    public boolean containsEpsilon() {
        return this.hasEpsilon;
    }

    public Lookahead intersection(Lookahead lookahead) {
        Lookahead lookahead2 = new Lookahead(this.fset.and(lookahead.fset));
        if (this.hasEpsilon && lookahead.hasEpsilon) {
            lookahead2.setEpsilon();
        }
        return lookahead2;
    }

    public boolean nil() {
        return this.fset.nil() && !this.hasEpsilon;
    }

    public void resetEpsilon() {
        this.hasEpsilon = false;
    }

    public void setEpsilon() {
        this.hasEpsilon = true;
    }

    public String toString() {
        String bitSet = this.fset.toString(",");
        String str = "";
        String str2 = containsEpsilon() ? "+<epsilon>" : "";
        String m3a = this.cycle != null ? C0000a.m3a(C0000a.m5a("; FOLLOW("), this.cycle, ")") : "";
        if (this.epsilonDepth != null) {
            StringBuilder m5a = C0000a.m5a("; depths=");
            m5a.append(this.epsilonDepth.toString(","));
            str = m5a.toString();
        }
        return bitSet + str2 + m3a + str;
    }

    public String toString(String str, CharFormatter charFormatter) {
        String bitSet = this.fset.toString(str, charFormatter);
        String str2 = "";
        String str3 = containsEpsilon() ? "+<epsilon>" : "";
        String m3a = this.cycle != null ? C0000a.m3a(C0000a.m5a("; FOLLOW("), this.cycle, ")") : "";
        if (this.epsilonDepth != null) {
            StringBuilder m5a = C0000a.m5a("; depths=");
            m5a.append(this.epsilonDepth.toString(","));
            str2 = m5a.toString();
        }
        return bitSet + str3 + m3a + str2;
    }

    public String toString(String str, CharFormatter charFormatter, Grammar grammar) {
        return grammar instanceof LexerGrammar ? toString(str, charFormatter) : toString(str, grammar.tokenManager.getVocabulary());
    }

    public String toString(String str, Vector vector) {
        String bitSet = this.fset.toString(str, vector);
        String str2 = "";
        String m3a = this.cycle != null ? C0000a.m3a(C0000a.m5a("; FOLLOW("), this.cycle, ")") : "";
        if (this.epsilonDepth != null) {
            StringBuilder m5a = C0000a.m5a("; depths=");
            m5a.append(this.epsilonDepth.toString(","));
            str2 = m5a.toString();
        }
        return C0000a.m2a(bitSet, m3a, str2);
    }
}
