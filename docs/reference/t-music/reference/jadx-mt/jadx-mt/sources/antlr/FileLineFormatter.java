package antlr;

/* JADX INFO: loaded from: classes3.dex */
public abstract class FileLineFormatter {
    public static FileLineFormatter formatter = new DefaultFileLineFormatter();

    public static FileLineFormatter getFormatter() {
        return formatter;
    }

    public static void setFormatter(FileLineFormatter fileLineFormatter) {
        formatter = fileLineFormatter;
    }

    public abstract String getFormatString(String str, int i, int i2);
}
