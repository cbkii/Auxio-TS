package antlr.preprocessor;

import antlr.CodeGenerator;
import antlr.collections.impl.IndexedVector;
import java.io.IOException;
import java.util.Enumeration;
import p000a.p001a.p002a.p003a.C0000a;

/* JADX INFO: loaded from: classes3.dex */
public class Grammar {
    public antlr.Tool antlrTool;
    public String fileName;
    public Hierarchy hier;
    public String memberAction;
    public String name;
    public IndexedVector options;
    public String preambleAction;
    public IndexedVector rules;
    public String superGrammar;
    public String tokenSection;
    public String type;
    public boolean predefined = false;
    public boolean alreadyExpanded = false;
    public boolean specifiedVocabulary = false;
    public String superClass = null;
    public String importVocab = null;
    public String exportVocab = null;

    public Grammar(antlr.Tool tool, String str, String str2, IndexedVector indexedVector) {
        this.name = str;
        this.superGrammar = str2;
        this.rules = indexedVector;
        this.antlrTool = tool;
    }

    public void addOption(Option option) {
        if (this.options == null) {
            this.options = new IndexedVector();
        }
        this.options.appendElement(option.getName(), option);
    }

    public void addRule(Rule rule) {
        this.rules.appendElement(rule.getName(), rule);
    }

    public void expandInPlace() {
        Grammar superGrammar;
        if (this.alreadyExpanded || (superGrammar = getSuperGrammar()) == null) {
            return;
        }
        if (this.exportVocab == null) {
            this.exportVocab = getName();
        }
        if (superGrammar.isPredefined()) {
            return;
        }
        superGrammar.expandInPlace();
        this.alreadyExpanded = true;
        this.hier.getFile(getFileName()).setExpanded(true);
        Enumeration enumerationElements = superGrammar.getRules().elements();
        while (enumerationElements.hasMoreElements()) {
            inherit((Rule) enumerationElements.nextElement(), superGrammar);
        }
        IndexedVector options = superGrammar.getOptions();
        if (options != null) {
            Enumeration enumerationElements2 = options.elements();
            while (enumerationElements2.hasMoreElements()) {
                inherit((Option) enumerationElements2.nextElement(), superGrammar);
            }
        }
        IndexedVector indexedVector = this.options;
        if ((indexedVector != null && indexedVector.getElement("importVocab") == null) || this.options == null) {
            addOption(new Option("importVocab", C0000a.m3a(new StringBuilder(), superGrammar.exportVocab, ";"), this));
            String strPathToFile = this.antlrTool.pathToFile(superGrammar.getFileName());
            StringBuilder sbM5a = C0000a.m5a(strPathToFile);
            sbM5a.append(superGrammar.exportVocab);
            sbM5a.append(CodeGenerator.TokenTypesFileSuffix);
            sbM5a.append(CodeGenerator.TokenTypesFileExt);
            String string = sbM5a.toString();
            String strFileMinusPath = this.antlrTool.fileMinusPath(string);
            StringBuilder sbM5a2 = C0000a.m5a(".");
            sbM5a2.append(System.getProperty("file.separator"));
            if (!strPathToFile.equals(sbM5a2.toString())) {
                try {
                    this.antlrTool.copyFile(string, strFileMinusPath);
                } catch (IOException unused) {
                    this.antlrTool.toolError("cannot find/copy importVocab file " + string);
                    return;
                }
            }
        }
        inherit(superGrammar.memberAction, superGrammar);
    }

    public String getFileName() {
        return this.fileName;
    }

    public String getName() {
        return this.name;
    }

    public IndexedVector getOptions() {
        return this.options;
    }

    public IndexedVector getRules() {
        return this.rules;
    }

    public Grammar getSuperGrammar() {
        String str = this.superGrammar;
        if (str == null) {
            return null;
        }
        return this.hier.getGrammar(str);
    }

    public String getSuperGrammarName() {
        return this.superGrammar;
    }

    public String getType() {
        return this.type;
    }

    public void inherit(Option option, Grammar grammar) {
        if (option.getName().equals("importVocab") || option.getName().equals("exportVocab")) {
            return;
        }
        IndexedVector indexedVector = this.options;
        if ((indexedVector != null ? (Option) indexedVector.getElement(option.getName()) : null) == null) {
            addOption(option);
        }
    }

    public void inherit(Rule rule, Grammar grammar) {
        Rule rule2 = (Rule) this.rules.getElement(rule.getName());
        if (rule2 == null) {
            addRule(rule);
            return;
        }
        if (rule2.sameSignature(rule)) {
            return;
        }
        antlr.Tool tool = this.antlrTool;
        StringBuilder sbM5a = C0000a.m5a("rule ");
        sbM5a.append(getName());
        sbM5a.append(".");
        sbM5a.append(rule2.getName());
        sbM5a.append(" has different signature than ");
        sbM5a.append(grammar.getName());
        sbM5a.append(".");
        sbM5a.append(rule2.getName());
        tool.warning(sbM5a.toString());
    }

    public void inherit(String str, Grammar grammar) {
        if (this.memberAction == null && str != null) {
            this.memberAction = str;
        }
    }

    public boolean isPredefined() {
        return this.predefined;
    }

    public void setFileName(String str) {
        this.fileName = str;
    }

    public void setHierarchy(Hierarchy hierarchy) {
        this.hier = hierarchy;
    }

    public void setMemberAction(String str) {
        this.memberAction = str;
    }

    public void setOptions(IndexedVector indexedVector) {
        this.options = indexedVector;
    }

    public void setPreambleAction(String str) {
        this.preambleAction = str;
    }

    public void setPredefined(boolean z) {
        this.predefined = z;
    }

    public void setTokenSection(String str) {
        this.tokenSection = str;
    }

    public void setType(String str) {
        this.type = str;
    }

    public String toString() {
        StringBuilder sbM5a;
        String str;
        StringBuffer stringBuffer = new StringBuffer(10000);
        String str2 = this.preambleAction;
        if (str2 != null) {
            stringBuffer.append(str2);
        }
        if (this.superGrammar == null) {
            return C0000a.m3a(C0000a.m5a("class "), this.name, ";");
        }
        if (this.superClass != null) {
            sbM5a = C0000a.m5a("class ");
            sbM5a.append(this.name);
            sbM5a.append(" extends ");
            str = this.superClass;
        } else {
            sbM5a = C0000a.m5a("class ");
            sbM5a.append(this.name);
            sbM5a.append(" extends ");
            str = this.type;
        }
        sbM5a.append(str);
        sbM5a.append(";");
        stringBuffer.append(sbM5a.toString());
        stringBuffer.append(System.getProperty("line.separator") + System.getProperty("line.separator"));
        IndexedVector indexedVector = this.options;
        if (indexedVector != null) {
            stringBuffer.append(Hierarchy.optionsToString(indexedVector));
        }
        if (this.tokenSection != null) {
            stringBuffer.append(this.tokenSection + "\n");
        }
        if (this.memberAction != null) {
            stringBuffer.append(this.memberAction + System.getProperty("line.separator"));
        }
        for (int i = 0; i < this.rules.size(); i++) {
            Rule rule = (Rule) this.rules.elementAt(i);
            if (!getName().equals(rule.enclosingGrammar.getName())) {
                StringBuilder sbM5a2 = C0000a.m5a("// inherited from grammar ");
                sbM5a2.append(rule.enclosingGrammar.getName());
                sbM5a2.append(System.getProperty("line.separator"));
                stringBuffer.append(sbM5a2.toString());
            }
            stringBuffer.append(rule + System.getProperty("line.separator") + System.getProperty("line.separator"));
        }
        return stringBuffer.toString();
    }
}
