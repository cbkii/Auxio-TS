package antlr;

import antlr.collections.impl.BitSet;
import antlr.collections.impl.Vector;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.StringTokenizer;
import p000a.p001a.p002a.p003a.C0000a;

/* JADX INFO: loaded from: classes3.dex */
public class Tool {
    public static String version = "";
    public String grammarFile;
    public boolean hasError = false;
    public boolean genDiagnostics = false;
    public boolean genDocBook = false;
    public boolean genHTML = false;
    public String outputDir = ".";

    /* JADX INFO: renamed from: f */
    public transient Reader f307f = new InputStreamReader(System.in);
    public String literalsPrefix = "LITERAL_";
    public boolean upperCaseMangledLiterals = false;
    public NameSpace nameSpace = null;
    public String namespaceAntlr = null;
    public String namespaceStd = null;
    public boolean genHashLines = true;
    public boolean noConstructors = false;
    public BitSet cmdLineArgValid = new BitSet();
    public ToolErrorHandler errorHandler = new DefaultToolErrorHandler(this);

    public static void help() {
        System.err.println("usage: java antlr.Tool [args] file.g");
        System.err.println("  -o outputDir       specify output directory where all output generated.");
        System.err.println("  -glib superGrammar specify location of supergrammar file.");
        System.err.println("  -debug             launch the ParseView debugger upon parser invocation.");
        System.err.println("  -html              generate a html file from your grammar.");
        System.err.println("  -docbook           generate a docbook sgml file from your grammar.");
        System.err.println("  -diagnostic        generate a textfile with diagnostics.");
        System.err.println("  -trace             have all rules call traceIn/traceOut.");
        System.err.println("  -traceLexer        have lexer rules call traceIn/traceOut.");
        System.err.println("  -traceParser       have parser rules call traceIn/traceOut.");
        System.err.println("  -traceTreeParser   have tree parser rules call traceIn/traceOut.");
        System.err.println("  -h|-help|--help    this message");
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x0039 A[Catch: Exception -> 0x0046, TryCatch #0 {Exception -> 0x0046, blocks: (B:3:0x000b, B:19:0x0039, B:20:0x003d, B:7:0x0011, B:9:0x0014, B:11:0x001e, B:13:0x0028, B:16:0x0033), top: B:25:0x000b }] */
    /* JADX WARN: Removed duplicated region for block: B:20:0x003d A[Catch: Exception -> 0x0046, TRY_LEAVE, TryCatch #0 {Exception -> 0x0046, blocks: (B:3:0x000b, B:19:0x0039, B:20:0x003d, B:7:0x0011, B:9:0x0014, B:11:0x001e, B:13:0x0028, B:16:0x0033), top: B:25:0x000b }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static void main(String[] strArr) {
        System.err.println("ANTLR Parser Generator   Version 2.7.7 (2006-11-01)   1989-2005");
        version = Version.project_version;
        try {
            boolean z = false;
            if (strArr.length != 0) {
                for (int i = 0; i < strArr.length; i++) {
                    if (!strArr[i].equals("-h") && !strArr[i].equals("-help") && !strArr[i].equals("--help")) {
                    }
                }
                if (z) {
                    new Tool().doEverything(strArr);
                    return;
                } else {
                    help();
                    return;
                }
            }
            z = true;
            if (z) {
            }
        } catch (Exception e) {
            System.err.println(System.getProperty("line.separator") + System.getProperty("line.separator"));
            PrintStream printStream = System.err;
            StringBuilder sbM5a = C0000a.m5a("#$%%*&@# internal error: ");
            sbM5a.append(e.toString());
            printStream.println(sbM5a.toString());
            System.err.println("[complain to nearest government official");
            System.err.println(" or send hate-mail to parrt@antlr.org;");
            PrintStream printStream2 = System.err;
            StringBuilder sbM5a2 = C0000a.m5a(" please send stack trace with report.]");
            sbM5a2.append(System.getProperty("line.separator"));
            printStream2.println(sbM5a2.toString());
            e.printStackTrace();
        }
    }

    public static Vector parseSeparatedList(String str, char c2) {
        StringTokenizer stringTokenizer = new StringTokenizer(str, String.valueOf(c2));
        Vector vector = new Vector(10);
        while (stringTokenizer.hasMoreTokens()) {
            vector.appendElement(stringTokenizer.nextToken());
        }
        if (vector.size() == 0) {
            return null;
        }
        return vector;
    }

    public void checkForInvalidArguments(String[] strArr, BitSet bitSet) {
        for (int i = 0; i < strArr.length; i++) {
            if (!bitSet.member(i)) {
                StringBuilder sbM5a = C0000a.m5a("invalid command-line argument: ");
                sbM5a.append(strArr[i]);
                sbM5a.append("; ignored");
                warning(sbM5a.toString());
            }
        }
    }

    public void copyFile(String str, String str2) throws Throwable {
        Throwable th;
        BufferedReader bufferedReader;
        File file = new File(str);
        File file2 = new File(str2);
        BufferedWriter bufferedWriter = null;
        try {
            if (!file.exists() || !file.isFile()) {
                throw new FileCopyException("FileCopy: no such source file: " + str);
            }
            if (!file.canRead()) {
                throw new FileCopyException("FileCopy: source file is unreadable: " + str);
            }
            if (!file2.exists()) {
                File fileParent = parent(file2);
                if (!fileParent.exists()) {
                    throw new FileCopyException("FileCopy: destination directory doesn't exist: " + str2);
                }
                if (!fileParent.canWrite()) {
                    throw new FileCopyException("FileCopy: destination directory is unwriteable: " + str2);
                }
            } else {
                if (!file2.isFile()) {
                    throw new FileCopyException("FileCopy: destination is not a file: " + str2);
                }
                new DataInputStream(System.in);
                if (!file2.canWrite()) {
                    throw new FileCopyException("FileCopy: destination file is unwriteable: " + str2);
                }
            }
            bufferedReader = new BufferedReader(new FileReader(file));
            try {
                BufferedWriter bufferedWriter2 = new BufferedWriter(new FileWriter(file2));
                try {
                    char[] cArr = new char[1024];
                    while (true) {
                        int i = bufferedReader.read(cArr, 0, 1024);
                        if (i == -1) {
                            try {
                                break;
                            } catch (IOException unused) {
                            }
                        } else {
                            bufferedWriter2.write(cArr, 0, i);
                        }
                    }
                    bufferedReader.close();
                    try {
                        bufferedWriter2.close();
                    } catch (IOException unused2) {
                    }
                } catch (Throwable th2) {
                    bufferedWriter = bufferedWriter2;
                    th = th2;
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException unused3) {
                        }
                    }
                    if (bufferedWriter == null) {
                        throw th;
                    }
                    try {
                        bufferedWriter.close();
                        throw th;
                    } catch (IOException unused4) {
                        throw th;
                    }
                }
            } catch (Throwable th3) {
                th = th3;
            }
        } catch (Throwable th4) {
            th = th4;
            bufferedReader = null;
        }
    }

    public int doEverything(String[] strArr) {
        String str;
        StringBuilder sb;
        antlr.preprocessor.Tool tool = new antlr.preprocessor.Tool(this, strArr);
        boolean zPreprocess = tool.preprocess();
        String[] strArrPreprocessedArgList = tool.preprocessedArgList();
        processArguments(strArrPreprocessedArgList);
        if (!zPreprocess) {
            return 1;
        }
        this.f307f = getGrammarReader();
        TokenBuffer tokenBuffer = new TokenBuffer(new ANTLRLexer(this.f307f));
        LLkAnalyzer lLkAnalyzer = new LLkAnalyzer(this);
        MakeGrammar makeGrammar = new MakeGrammar(this, strArr, lLkAnalyzer);
        try {
            ANTLRParser aNTLRParser = new ANTLRParser(tokenBuffer, makeGrammar, this);
            aNTLRParser.setFilename(this.grammarFile);
            aNTLRParser.grammar();
            if (hasError()) {
                fatalError("Exiting due to errors.");
            }
            checkForInvalidArguments(strArrPreprocessedArgList, this.cmdLineArgValid);
            String str2 = "antlr." + getLanguage(makeGrammar) + "CodeGenerator";
            try {
                CodeGenerator codeGenerator = (CodeGenerator) Utils.createInstanceOf(str2);
                codeGenerator.setBehavior(makeGrammar);
                codeGenerator.setAnalyzer(lLkAnalyzer);
                codeGenerator.setTool(this);
                codeGenerator.gen();
                return 0;
            } catch (ClassNotFoundException unused) {
                sb = new StringBuilder();
                sb.append("Cannot instantiate code-generator: ");
                sb.append(str2);
                panic(sb.toString());
                return 0;
            } catch (IllegalAccessException unused2) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("code-generator class '");
                sb2.append(str2);
                sb2.append("' is not accessible");
                sb = sb2;
                panic(sb.toString());
                return 0;
            } catch (IllegalArgumentException unused3) {
                sb = new StringBuilder();
                sb.append("Cannot instantiate code-generator: ");
                sb.append(str2);
                panic(sb.toString());
                return 0;
            } catch (InstantiationException unused4) {
                sb = new StringBuilder();
                sb.append("Cannot instantiate code-generator: ");
                sb.append(str2);
                panic(sb.toString());
                return 0;
            }
        } catch (RecognitionException e) {
            e = e;
            str = "Unhandled parser error: ";
            StringBuilder sbM5a = C0000a.m5a(str);
            sbM5a.append(e.getMessage());
            fatalError(sbM5a.toString());
            return 0;
        } catch (TokenStreamException e2) {
            e = e2;
            str = "TokenStreamException: ";
            StringBuilder sbM5a2 = C0000a.m5a(str);
            sbM5a2.append(e.getMessage());
            fatalError(sbM5a2.toString());
            return 0;
        }
    }

    public void doEverythingWrapper(String[] strArr) {
        System.exit(doEverything(strArr));
    }

    public void error(String str) {
        this.hasError = true;
        System.err.println("error: " + str);
    }

    public void error(String str, String str2, int i, int i2) {
        this.hasError = true;
        System.err.println(FileLineFormatter.getFormatter().getFormatString(str2, i, i2) + str);
    }

    public void fatalError(String str) {
        System.err.println(str);
        Utils.error(str);
        throw null;
    }

    public String fileMinusPath(String str) {
        int iLastIndexOf = str.lastIndexOf(System.getProperty("file.separator"));
        return iLastIndexOf == -1 ? str : str.substring(iLastIndexOf + 1);
    }

    public boolean getGenHashLines() {
        return this.genHashLines;
    }

    public String getGrammarFile() {
        return this.grammarFile;
    }

    public Reader getGrammarReader() {
        try {
            if (this.grammarFile != null) {
                return new BufferedReader(new FileReader(this.grammarFile));
            }
        } catch (IOException unused) {
            StringBuilder sbM5a = C0000a.m5a("cannot open grammar file ");
            sbM5a.append(this.grammarFile);
            fatalError(sbM5a.toString());
        }
        return null;
    }

    public String getLanguage(MakeGrammar makeGrammar) {
        return this.genDiagnostics ? "Diagnostic" : this.genHTML ? "HTML" : this.genDocBook ? "DocBook" : makeGrammar.language;
    }

    public String getLiteralsPrefix() {
        return this.literalsPrefix;
    }

    public NameSpace getNameSpace() {
        return this.nameSpace;
    }

    public String getNamespaceAntlr() {
        return this.namespaceAntlr;
    }

    public String getNamespaceStd() {
        return this.namespaceStd;
    }

    public String getOutputDirectory() {
        return this.outputDir;
    }

    public boolean getUpperCaseMangledLiterals() {
        return this.upperCaseMangledLiterals;
    }

    public boolean hasError() {
        return this.hasError;
    }

    public PrintWriter openOutputFile(String str) {
        String str2 = this.outputDir;
        if (str2 != ".") {
            File file = new File(str2);
            if (!file.exists()) {
                file.mkdirs();
            }
        }
        return new PrintWriter(new PreservingFileWriter(this.outputDir + System.getProperty("file.separator") + str));
    }

    public void panic() {
        fatalError("panic");
    }

    public void panic(String str) {
        fatalError("panic: " + str);
    }

    public File parent(File file) {
        String parent = file.getParent();
        return parent == null ? file.isAbsolute() ? new File(File.separator) : new File(System.getProperty("user.dir")) : new File(parent);
    }

    public String pathToFile(String str) {
        int iLastIndexOf = str.lastIndexOf(System.getProperty("file.separator"));
        if (iLastIndexOf != -1) {
            return str.substring(0, iLastIndexOf + 1);
        }
        StringBuilder sbM5a = C0000a.m5a(".");
        sbM5a.append(System.getProperty("file.separator"));
        return sbM5a.toString();
    }

    public void processArguments(String[] strArr) {
        int i = 0;
        while (i < strArr.length) {
            if (strArr[i].equals("-diagnostic")) {
                this.genDiagnostics = true;
                this.genHTML = false;
            } else {
                if (strArr[i].equals("-o")) {
                    setArgOK(i);
                    int i2 = i + 1;
                    if (i2 >= strArr.length) {
                        error("missing output directory with -o option; ignoring");
                    } else {
                        setOutputDirectory(strArr[i2]);
                        setArgOK(i2);
                        i = i2;
                    }
                } else {
                    if (strArr[i].equals("-html")) {
                        this.genHTML = true;
                    } else if (strArr[i].equals("-docbook")) {
                        this.genDocBook = true;
                    } else if (strArr[i].charAt(0) != '-') {
                        this.grammarFile = strArr[i];
                    }
                    this.genDiagnostics = false;
                }
                i++;
            }
            setArgOK(i);
            i++;
        }
    }

    public void reportException(Exception exc, String str) {
        String string;
        PrintStream printStream = System.err;
        if (str == null) {
            string = exc.getMessage();
        } else {
            StringBuilder sbM9b = C0000a.m9b(str, ": ");
            sbM9b.append(exc.getMessage());
            string = sbM9b.toString();
        }
        printStream.println(string);
    }

    public void reportProgress(String str) {
        System.out.println(str);
    }

    public void setArgOK(int i) {
        this.cmdLineArgValid.add(i);
    }

    public void setFileLineFormatter(FileLineFormatter fileLineFormatter) {
        FileLineFormatter.setFormatter(fileLineFormatter);
    }

    public void setNameSpace(String str) {
        if (this.nameSpace == null) {
            this.nameSpace = new NameSpace(StringUtils.stripFrontBack(str, "\"", "\""));
        }
    }

    public void setOutputDirectory(String str) {
        this.outputDir = str;
    }

    public void toolError(String str) {
        System.err.println("error: " + str);
    }

    public void warning(String str) {
        System.err.println("warning: " + str);
    }

    public void warning(String str, String str2, int i, int i2) {
        System.err.println(FileLineFormatter.getFormatter().getFormatString(str2, i, i2) + "warning:" + str);
    }

    public void warning(String[] strArr, String str, int i, int i2) {
        if (strArr == null || strArr.length == 0) {
            panic("bad multi-line message to Tool.warning");
        }
        System.err.println(FileLineFormatter.getFormatter().getFormatString(str, i, i2) + "warning:" + strArr[0]);
        for (int i3 = 1; i3 < strArr.length; i3++) {
            System.err.println(FileLineFormatter.getFormatter().getFormatString(str, i, i2) + "    " + strArr[i3]);
        }
    }
}
