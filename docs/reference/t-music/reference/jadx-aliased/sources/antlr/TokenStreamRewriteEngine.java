package antlr;

import antlr.ASdebug.ASDebugStream;
import antlr.ASdebug.IASDebugStream;
import antlr.ASdebug.TokenOffsetInfo;
import antlr.collections.impl.BitSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import p054a.p055a.p056a.p003a.C0000a;

/* loaded from: classes3.dex */
public class TokenStreamRewriteEngine implements TokenStream, IASDebugStream {
    public static final String DEFAULT_PROGRAM_NAME = "default";
    public static final int MIN_TOKEN_INDEX = 0;
    public static final int PROGRAM_INIT_SIZE = 100;
    public BitSet discardMask;
    public int index;
    public Map lastRewriteTokenIndexes;
    public Map programs;
    public TokenStream stream;
    public List tokens;

    public static class DeleteOp extends ReplaceOp {
        public DeleteOp(int i, int i2) {
            super(i, i2, null);
        }
    }

    public static class InsertBeforeOp extends RewriteOperation {
        public InsertBeforeOp(int i, String str) {
            super(i, str);
        }

        @Override // antlr.TokenStreamRewriteEngine.RewriteOperation
        public int execute(StringBuffer stringBuffer) {
            stringBuffer.append(this.text);
            return this.index;
        }
    }

    public static class ReplaceOp extends RewriteOperation {
        public int lastIndex;

        public ReplaceOp(int i, int i2, String str) {
            super(i, str);
            this.lastIndex = i2;
        }

        @Override // antlr.TokenStreamRewriteEngine.RewriteOperation
        public int execute(StringBuffer stringBuffer) {
            String str = this.text;
            if (str != null) {
                stringBuffer.append(str);
            }
            return this.lastIndex + 1;
        }
    }

    public static class RewriteOperation {
        public int index;
        public String text;

        public RewriteOperation(int i, String str) {
            this.index = i;
            this.text = str;
        }

        public int execute(StringBuffer stringBuffer) {
            return this.index;
        }

        public String toString() {
            String name = getClass().getName();
            StringBuilder m9b = C0000a.m9b(name.substring(name.indexOf(36) + 1, name.length()), "@");
            m9b.append(this.index);
            m9b.append('\"');
            m9b.append(this.text);
            m9b.append('\"');
            return m9b.toString();
        }
    }

    public TokenStreamRewriteEngine(TokenStream tokenStream) {
        this(tokenStream, 1000);
    }

    public TokenStreamRewriteEngine(TokenStream tokenStream, int i) {
        this.programs = null;
        this.lastRewriteTokenIndexes = null;
        this.index = 0;
        this.discardMask = new BitSet();
        this.stream = tokenStream;
        this.tokens = new ArrayList(i);
        this.programs = new HashMap();
        this.programs.put(DEFAULT_PROGRAM_NAME, new ArrayList(100));
        this.lastRewriteTokenIndexes = new HashMap();
    }

    private List initializeProgram(String str) {
        ArrayList arrayList = new ArrayList(100);
        this.programs.put(str, arrayList);
        return arrayList;
    }

    public void addToSortedRewriteList(RewriteOperation rewriteOperation) {
        addToSortedRewriteList(DEFAULT_PROGRAM_NAME, rewriteOperation);
    }

    public void addToSortedRewriteList(String str, RewriteOperation rewriteOperation) {
        int i;
        List program = getProgram(str);
        int binarySearch = Collections.binarySearch(program, rewriteOperation, new Comparator() { // from class: antlr.TokenStreamRewriteEngine.1
            @Override // java.util.Comparator
            public int compare(Object obj, Object obj2) {
                int i2 = ((RewriteOperation) obj).index;
                int i3 = ((RewriteOperation) obj2).index;
                if (i2 < i3) {
                    return -1;
                }
                return i2 > i3 ? 1 : 0;
            }
        });
        boolean z = true;
        if (binarySearch >= 0) {
            while (binarySearch >= 0 && ((RewriteOperation) program.get(binarySearch)).index >= rewriteOperation.index) {
                binarySearch--;
            }
            i = binarySearch + 1;
            if (rewriteOperation instanceof ReplaceOp) {
                int i2 = i;
                while (i2 < program.size()) {
                    RewriteOperation rewriteOperation2 = (RewriteOperation) program.get(i);
                    if (rewriteOperation2.index != rewriteOperation.index) {
                        break;
                    }
                    if (rewriteOperation2 instanceof ReplaceOp) {
                        program.set(i, rewriteOperation);
                        break;
                    }
                    i2++;
                }
                z = false;
                if (z) {
                    return;
                }
                program.add(i2, rewriteOperation);
                return;
            }
        } else {
            i = (-binarySearch) - 1;
        }
        program.add(i, rewriteOperation);
    }

    public void delete(int i) {
        delete(DEFAULT_PROGRAM_NAME, i, i);
    }

    public void delete(int i, int i2) {
        delete(DEFAULT_PROGRAM_NAME, i, i2);
    }

    public void delete(Token token) {
        delete(DEFAULT_PROGRAM_NAME, token, token);
    }

    public void delete(Token token, Token token2) {
        delete(DEFAULT_PROGRAM_NAME, token, token2);
    }

    public void delete(String str, int i, int i2) {
        replace(str, i, i2, (String) null);
    }

    public void delete(String str, Token token, Token token2) {
        replace(str, token, token2, (String) null);
    }

    public void deleteProgram() {
        deleteProgram(DEFAULT_PROGRAM_NAME);
    }

    public void deleteProgram(String str) {
        rollback(str, 0);
    }

    public void discard(int i) {
        this.discardMask.add(i);
    }

    @Override // antlr.ASdebug.IASDebugStream
    public String getEntireText() {
        return ASDebugStream.getEntireText(this.stream);
    }

    public int getLastRewriteTokenIndex() {
        return getLastRewriteTokenIndex(DEFAULT_PROGRAM_NAME);
    }

    public int getLastRewriteTokenIndex(String str) {
        Integer num = (Integer) this.lastRewriteTokenIndexes.get(str);
        if (num == null) {
            return -1;
        }
        return num.intValue();
    }

    @Override // antlr.ASdebug.IASDebugStream
    public TokenOffsetInfo getOffsetInfo(Token token) {
        return ASDebugStream.getOffsetInfo(this.stream, token);
    }

    public List getProgram(String str) {
        List list = (List) this.programs.get(str);
        return list == null ? initializeProgram(str) : list;
    }

    public TokenWithIndex getToken(int i) {
        return (TokenWithIndex) this.tokens.get(i);
    }

    public int getTokenStreamSize() {
        return this.tokens.size();
    }

    public int index() {
        return this.index;
    }

    public void insertAfter(int i, String str) {
        insertAfter(DEFAULT_PROGRAM_NAME, i, str);
    }

    public void insertAfter(Token token, String str) {
        insertAfter(DEFAULT_PROGRAM_NAME, token, str);
    }

    public void insertAfter(String str, int i, String str2) {
        insertBefore(str, i + 1, str2);
    }

    public void insertAfter(String str, Token token, String str2) {
        insertAfter(str, ((TokenWithIndex) token).getIndex(), str2);
    }

    public void insertBefore(int i, String str) {
        insertBefore(DEFAULT_PROGRAM_NAME, i, str);
    }

    public void insertBefore(Token token, String str) {
        insertBefore(DEFAULT_PROGRAM_NAME, token, str);
    }

    public void insertBefore(String str, int i, String str2) {
        addToSortedRewriteList(str, new InsertBeforeOp(i, str2));
    }

    public void insertBefore(String str, Token token, String str2) {
        insertBefore(str, ((TokenWithIndex) token).getIndex(), str2);
    }

    @Override // antlr.TokenStream
    public Token nextToken() {
        TokenWithIndex tokenWithIndex;
        do {
            tokenWithIndex = (TokenWithIndex) this.stream.nextToken();
            if (tokenWithIndex != null) {
                tokenWithIndex.setIndex(this.index);
                if (tokenWithIndex.getType() != 1) {
                    this.tokens.add(tokenWithIndex);
                }
                this.index++;
            }
            if (tokenWithIndex == null) {
                break;
            }
        } while (this.discardMask.member(tokenWithIndex.getType()));
        return tokenWithIndex;
    }

    public void replace(int i, int i2, String str) {
        replace(DEFAULT_PROGRAM_NAME, i, i2, str);
    }

    public void replace(int i, String str) {
        replace(DEFAULT_PROGRAM_NAME, i, i, str);
    }

    public void replace(Token token, Token token2, String str) {
        replace(DEFAULT_PROGRAM_NAME, token, token2, str);
    }

    public void replace(Token token, String str) {
        replace(DEFAULT_PROGRAM_NAME, token, token, str);
    }

    public void replace(String str, int i, int i2, String str2) {
        addToSortedRewriteList(new ReplaceOp(i, i2, str2));
    }

    public void replace(String str, Token token, Token token2, String str2) {
        replace(str, ((TokenWithIndex) token).getIndex(), ((TokenWithIndex) token2).getIndex(), str2);
    }

    public void rollback(int i) {
        rollback(DEFAULT_PROGRAM_NAME, i);
    }

    public void rollback(String str, int i) {
        List list = (List) this.programs.get(str);
        if (list != null) {
            this.programs.put(str, list.subList(0, i));
        }
    }

    public void setLastRewriteTokenIndex(String str, int i) {
        this.lastRewriteTokenIndexes.put(str, new Integer(i));
    }

    public int size() {
        return this.tokens.size();
    }

    public String toDebugString() {
        return toDebugString(0, getTokenStreamSize() - 1);
    }

    public String toDebugString(int i, int i2) {
        StringBuffer stringBuffer = new StringBuffer();
        while (i >= 0 && i <= i2 && i < this.tokens.size()) {
            stringBuffer.append(getToken(i));
            i++;
        }
        return stringBuffer.toString();
    }

    public String toOriginalString() {
        return toOriginalString(0, getTokenStreamSize() - 1);
    }

    public String toOriginalString(int i, int i2) {
        StringBuffer stringBuffer = new StringBuffer();
        while (i >= 0 && i <= i2 && i < this.tokens.size()) {
            stringBuffer.append(getToken(i).getText());
            i++;
        }
        return stringBuffer.toString();
    }

    public String toString() {
        return toString(0, getTokenStreamSize() - 1);
    }

    public String toString(int i, int i2) {
        return toString(DEFAULT_PROGRAM_NAME, i, i2);
    }

    public String toString(String str) {
        return toString(str, 0, getTokenStreamSize() - 1);
    }

    public String toString(String str, int i, int i2) {
        RewriteOperation rewriteOperation;
        List list = (List) this.programs.get(str);
        if (list == null || list.size() == 0) {
            return toOriginalString(i, i2);
        }
        StringBuffer stringBuffer = new StringBuffer();
        int i3 = 0;
        while (i >= 0 && i <= i2 && i < this.tokens.size()) {
            if (i3 < list.size()) {
                while (true) {
                    rewriteOperation = (RewriteOperation) list.get(i3);
                    while (rewriteOperation.index < i && i3 < list.size()) {
                        i3++;
                        if (i3 < list.size()) {
                            break;
                        }
                    }
                }
                while (i == rewriteOperation.index && i3 < list.size()) {
                    i = rewriteOperation.execute(stringBuffer);
                    i3++;
                    if (i3 < list.size()) {
                        rewriteOperation = (RewriteOperation) list.get(i3);
                    }
                }
            }
            if (i <= i2) {
                stringBuffer.append(getToken(i).getText());
                i++;
            }
        }
        while (i3 < list.size()) {
            RewriteOperation rewriteOperation2 = (RewriteOperation) list.get(i3);
            if (rewriteOperation2.index >= size()) {
                rewriteOperation2.execute(stringBuffer);
            }
            i3++;
        }
        return stringBuffer.toString();
    }
}
