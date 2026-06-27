package antlr;

import p000a.p001a.p002a.p003a.C0000a;

/* JADX INFO: loaded from: classes3.dex */
public class ActionTransInfo {
    public boolean assignToRoot = false;
    public String refRuleRoot = null;
    public String followSetName = null;

    public String toString() {
        StringBuilder sbM5a = C0000a.m5a("assignToRoot:");
        sbM5a.append(this.assignToRoot);
        sbM5a.append(", refRuleRoot:");
        sbM5a.append(this.refRuleRoot);
        sbM5a.append(", FOLLOW Set:");
        sbM5a.append(this.followSetName);
        return sbM5a.toString();
    }
}
