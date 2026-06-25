package antlr;

/* JADX INFO: loaded from: classes3.dex */
public class DefaultFileLineFormatter extends FileLineFormatter {
    @Override // antlr.FileLineFormatter
    public String getFormatString(String str, int i, int i2) {
        StringBuffer stringBuffer = new StringBuffer();
        if (str != null) {
            stringBuffer.append(str + ":");
        }
        if (i != -1) {
            if (str == null) {
                stringBuffer.append("line ");
            }
            stringBuffer.append(i);
            if (i2 != -1) {
                stringBuffer.append(":" + i2);
            }
            stringBuffer.append(":");
        }
        stringBuffer.append(" ");
        return stringBuffer.toString();
    }
}
