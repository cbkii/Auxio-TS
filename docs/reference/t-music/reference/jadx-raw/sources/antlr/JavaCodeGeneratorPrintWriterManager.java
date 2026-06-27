package antlr;

import java.io.PrintWriter;
import java.util.Map;

/* loaded from: classes3.dex */
public interface JavaCodeGeneratorPrintWriterManager {
    void endMapping();

    void finishOutput();

    Map getSourceMaps();

    PrintWriter setupOutput(Tool tool, Grammar grammar);

    PrintWriter setupOutput(Tool tool, String str);

    void startMapping(int i);

    void startSingleSourceLineMapping(int i);
}
