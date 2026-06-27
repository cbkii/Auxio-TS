package antlr;

import p000a.p001a.p002a.p003a.C0000a;

/* loaded from: classes3.dex */
public class ActionTransInfo {
    public boolean assignToRoot = false;
    public String refRuleRoot = null;
    public String followSetName = null;

    public String toString() {
        StringBuilder m5a = C0000a.m5a("assignToRoot:");
        m5a.append(this.assignToRoot);
        m5a.append(", refRuleRoot:");
        m5a.append(this.refRuleRoot);
        m5a.append(", FOLLOW Set:");
        m5a.append(this.followSetName);
        return m5a.toString();
    }
}
