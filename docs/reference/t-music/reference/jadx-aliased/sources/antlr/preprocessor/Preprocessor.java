package antlr.preprocessor;

import antlr.LLkParser;
import antlr.NoViableAltException;
import antlr.ParserSharedInputState;
import antlr.RecognitionException;
import antlr.SemanticException;
import antlr.Token;
import antlr.TokenBuffer;
import antlr.TokenStream;
import antlr.collections.impl.BitSet;
import antlr.collections.impl.IndexedVector;

/* loaded from: classes3.dex */
public class Preprocessor extends LLkParser implements PreprocessorTokenTypes {
    public static final String[] _tokenNames = {"<0>", "EOF", "<2>", "NULL_TREE_LOOKAHEAD", "\"tokens\"", "HEADER_ACTION", "SUBRULE_BLOCK", "ACTION", "\"class\"", "ID", "\"extends\"", "SEMI", "TOKENS_SPEC", "OPTIONS_START", "ASSIGN_RHS", "RCURLY", "\"protected\"", "\"private\"", "\"public\"", "BANG", "ARG_ACTION", "\"returns\"", "RULE_BLOCK", "\"throws\"", "COMMA", "\"exception\"", "\"catch\"", "ALT", "ELEMENT", "LPAREN", "RPAREN", "ID_OR_KEYWORD", "CURLY_BLOCK_SCARF", "WS", "NEWLINE", "COMMENT", "SL_COMMENT", "ML_COMMENT", "CHAR_LITERAL", "STRING_LITERAL", "ESC", "DIGIT", "XDIGIT"};
    public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
    public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
    public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
    public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
    public static final BitSet _tokenSet_4 = new BitSet(mk_tokenSet_4());
    public static final BitSet _tokenSet_5 = new BitSet(mk_tokenSet_5());
    public static final BitSet _tokenSet_6 = new BitSet(mk_tokenSet_6());
    public static final BitSet _tokenSet_7 = new BitSet(mk_tokenSet_7());
    public static final BitSet _tokenSet_8 = new BitSet(mk_tokenSet_8());
    public antlr.Tool antlrTool;

    public Preprocessor(ParserSharedInputState parserSharedInputState) {
        super(parserSharedInputState, 1);
        this.tokenNames = _tokenNames;
    }

    public Preprocessor(TokenBuffer tokenBuffer) {
        this(tokenBuffer, 1);
    }

    public Preprocessor(TokenBuffer tokenBuffer, int i) {
        super(tokenBuffer, i);
        this.tokenNames = _tokenNames;
    }

    public Preprocessor(TokenStream tokenStream) {
        this(tokenStream, 1);
    }

    public Preprocessor(TokenStream tokenStream, int i) {
        super(tokenStream, i);
        this.tokenNames = _tokenNames;
    }

    public static final long[] mk_tokenSet_0() {
        return new long[]{2, 0};
    }

    public static final long[] mk_tokenSet_1() {
        return new long[]{4658050, 0};
    }

    public static final long[] mk_tokenSet_2() {
        return new long[]{459264, 0};
    }

    public static final long[] mk_tokenSet_3() {
        return new long[]{386, 0};
    }

    public static final long[] mk_tokenSet_4() {
        return new long[]{2048, 0};
    }

    public static final long[] mk_tokenSet_5() {
        return new long[]{459650, 0};
    }

    public static final long[] mk_tokenSet_6() {
        return new long[]{4202624, 0};
    }

    public static final long[] mk_tokenSet_7() {
        return new long[]{34014082, 0};
    }

    public static final long[] mk_tokenSet_8() {
        return new long[]{101122946, 0};
    }

    public final Grammar class_def(String str, Hierarchy hierarchy) {
        RecognitionException e;
        Grammar grammar;
        Token mo108LT;
        Token mo108LT2;
        Token mo108LT3;
        String superClass;
        Grammar grammar2;
        int i;
        IndexedVector indexedVector = new IndexedVector(100);
        IndexedVector indexedVector2 = null;
        try {
            int mo107LA = mo107LA(1);
            if (mo107LA == 7) {
                mo108LT = mo108LT(1);
                match(7);
            } else {
                if (mo107LA != 8) {
                    throw new NoViableAltException(mo108LT(1), getFilename());
                }
                mo108LT = null;
            }
            match(8);
            mo108LT2 = mo108LT(1);
            match(9);
            match(10);
            mo108LT3 = mo108LT(1);
            match(9);
            int mo107LA2 = mo107LA(1);
            if (mo107LA2 == 6) {
                superClass = superClass();
            } else {
                if (mo107LA2 != 11) {
                    throw new NoViableAltException(mo108LT(1), getFilename());
                }
                superClass = null;
            }
            match(11);
            grammar2 = hierarchy.getGrammar(mo108LT2.getText());
        } catch (RecognitionException e2) {
            e = e2;
            grammar = null;
        }
        if (grammar2 != null) {
            throw new SemanticException("redefinition of grammar " + mo108LT2.getText(), str, mo108LT2.getLine(), mo108LT2.getColumn());
        }
        try {
            grammar = new Grammar(hierarchy.getTool(), mo108LT2.getText(), mo108LT3.getText(), indexedVector);
        } catch (RecognitionException e3) {
            e = e3;
            grammar = grammar2;
        }
        try {
            grammar.superClass = superClass;
            if (mo108LT != null) {
                grammar.setPreambleAction(mo108LT.getText());
            }
            int mo107LA3 = mo107LA(1);
            if (mo107LA3 != 7 && mo107LA3 != 9 && mo107LA3 != 12) {
                if (mo107LA3 != 13) {
                    switch (mo107LA3) {
                        case 16:
                        case 17:
                        case 18:
                            break;
                        default:
                            throw new NoViableAltException(mo108LT(1), getFilename());
                    }
                } else {
                    indexedVector2 = optionSpec(grammar);
                }
            }
            grammar.setOptions(indexedVector2);
            int mo107LA4 = mo107LA(1);
            if (mo107LA4 != 7 && mo107LA4 != 9) {
                if (mo107LA4 != 12) {
                    switch (mo107LA4) {
                        case 16:
                        case 17:
                        case 18:
                            break;
                        default:
                            throw new NoViableAltException(mo108LT(1), getFilename());
                    }
                } else {
                    Token mo108LT4 = mo108LT(1);
                    match(12);
                    grammar.setTokenSection(mo108LT4.getText());
                }
            }
            int mo107LA5 = mo107LA(1);
            if (mo107LA5 == 7) {
                Token mo108LT5 = mo108LT(1);
                match(7);
                grammar.setMemberAction(mo108LT5.getText());
            } else if (mo107LA5 != 9) {
                switch (mo107LA5) {
                    case 16:
                    case 17:
                    case 18:
                        break;
                    default:
                        throw new NoViableAltException(mo108LT(1), getFilename());
                }
            }
            i = 0;
            while (_tokenSet_2.member(mo107LA(1))) {
                rule(grammar);
                i++;
            }
        } catch (RecognitionException e4) {
            e = e4;
            reportError(e);
            consume();
            consumeUntil(_tokenSet_3);
            return grammar;
        }
        if (i >= 1) {
            return grammar;
        }
        throw new NoViableAltException(mo108LT(1), getFilename());
    }

    public final String exceptionGroup() {
        String str = "";
        while (mo107LA(1) == 25) {
            try {
                str = str + exceptionSpec();
            } catch (RecognitionException e) {
                reportError(e);
                consume();
                consumeUntil(_tokenSet_5);
            }
        }
        return str;
    }

    public final String exceptionHandler() {
        try {
            match(26);
            Token mo108LT = mo108LT(1);
            match(20);
            Token mo108LT2 = mo108LT(1);
            match(7);
            return System.getProperty("line.separator") + "catch " + mo108LT.getText() + " " + mo108LT2.getText();
        } catch (RecognitionException e) {
            this.reportError(e);
            this.consume();
            this.consumeUntil(_tokenSet_8);
            return null;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x0066 A[Catch: RecognitionException -> 0x0078, TryCatch #0 {RecognitionException -> 0x0078, blocks: (B:3:0x0019, B:15:0x0038, B:16:0x003b, B:17:0x0048, B:19:0x0049, B:20:0x005c, B:21:0x0073, B:22:0x0060, B:24:0x0066), top: B:2:0x0019 }] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:21:0x0073 -> B:22:0x0060). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final String exceptionSpec() {
        String exceptionHandler;
        StringBuilder sb;
        String str = System.getProperty("line.separator") + "exception ";
        try {
            match(25);
            int mo107LA = mo107LA(1);
            if (mo107LA != 1) {
                if (mo107LA == 20) {
                    Token mo108LT = mo108LT(1);
                    match(20);
                    sb = new StringBuilder();
                    sb.append(str);
                    exceptionHandler = mo108LT.getText();
                    sb.append(exceptionHandler);
                    str = sb.toString();
                } else if (mo107LA != 7 && mo107LA != 8 && mo107LA != 9 && mo107LA != 25 && mo107LA != 26) {
                    switch (mo107LA) {
                        case 16:
                        case 17:
                        case 18:
                            break;
                        default:
                            throw new NoViableAltException(mo108LT(1), getFilename());
                    }
                }
            }
            if (mo107LA(1) == 26) {
                exceptionHandler = exceptionHandler();
                sb = new StringBuilder();
                sb.append(str);
                sb.append(exceptionHandler);
                str = sb.toString();
                if (mo107LA(1) == 26) {
                }
            }
        } catch (RecognitionException e) {
            reportError(e);
            consume();
            consumeUntil(_tokenSet_7);
        }
        return str;
    }

    public antlr.Tool getTool() {
        return this.antlrTool;
    }

    public final void grammarFile(Hierarchy hierarchy, String str) {
        while (mo107LA(1) == 5) {
            try {
                Token mo108LT = mo108LT(1);
                match(5);
                hierarchy.getFile(str).addHeaderAction(mo108LT.getText());
            } catch (RecognitionException e) {
                reportError(e);
                consume();
                consumeUntil(_tokenSet_0);
                return;
            }
        }
        int mo107LA = mo107LA(1);
        IndexedVector indexedVector = null;
        if (mo107LA != 1) {
            if (mo107LA == 13) {
                indexedVector = optionSpec(null);
            } else if (mo107LA != 7 && mo107LA != 8) {
                throw new NoViableAltException(mo108LT(1), getFilename());
            }
        }
        while (true) {
            if (mo107LA(1) != 7 && mo107LA(1) != 8) {
                match(1);
                return;
            }
            Grammar class_def = class_def(str, hierarchy);
            if (class_def != null && indexedVector != null) {
                hierarchy.getFile(str).setOptions(indexedVector);
            }
            if (class_def != null) {
                class_def.setFileName(str);
                hierarchy.addGrammar(class_def);
            }
        }
    }

    public final IndexedVector optionSpec(Grammar grammar) {
        IndexedVector indexedVector = new IndexedVector();
        try {
            match(13);
            while (mo107LA(1) == 9) {
                Token mo108LT = mo108LT(1);
                match(9);
                Token mo108LT2 = mo108LT(1);
                match(14);
                Option option = new Option(mo108LT.getText(), mo108LT2.getText(), grammar);
                indexedVector.appendElement(option.getName(), option);
                if (grammar != null && mo108LT.getText().equals("importVocab")) {
                    grammar.specifiedVocabulary = true;
                    grammar.importVocab = mo108LT2.getText();
                } else if (grammar != null && mo108LT.getText().equals("exportVocab")) {
                    grammar.exportVocab = mo108LT2.getText().substring(0, mo108LT2.getText().length() - 1);
                    grammar.exportVocab = grammar.exportVocab.trim();
                }
            }
            match(15);
        } catch (RecognitionException e) {
            reportError(e);
            consume();
            consumeUntil(_tokenSet_1);
        }
        return indexedVector;
    }

    @Override // antlr.Parser
    public void reportError(RecognitionException recognitionException) {
        if (getTool() != null) {
            getTool().error(recognitionException.getErrorMessage(), recognitionException.getFilename(), recognitionException.getLine(), recognitionException.getColumn());
        } else {
            super.reportError(recognitionException);
        }
    }

    @Override // antlr.Parser
    public void reportError(String str) {
        if (getTool() != null) {
            getTool().error(str, getFilename(), -1, -1);
        } else {
            super.reportError(str);
        }
    }

    @Override // antlr.Parser
    public void reportWarning(String str) {
        if (getTool() != null) {
            getTool().warning(str, getFilename(), -1, -1);
        } else {
            super.reportWarning(str);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x006e A[Catch: RecognitionException -> 0x0163, TryCatch #0 {RecognitionException -> 0x0163, blocks: (B:3:0x0001, B:5:0x000a, B:6:0x000d, B:7:0x0028, B:8:0x0033, B:10:0x0010, B:12:0x0035, B:15:0x0047, B:16:0x004a, B:17:0x0054, B:18:0x005f, B:19:0x004d, B:20:0x0061, B:23:0x006b, B:24:0x006e, B:25:0x0079, B:26:0x0084, B:27:0x0071, B:28:0x0086, B:31:0x008e, B:32:0x0091, B:33:0x00a1, B:34:0x00ac, B:35:0x0094, B:36:0x00ae, B:42:0x00be, B:43:0x00d3, B:48:0x00de, B:49:0x00eb, B:50:0x00ec, B:51:0x00f2, B:55:0x0110, B:57:0x013c, B:59:0x0145, B:61:0x014e, B:63:0x0157, B:64:0x015a, B:66:0x015f, B:69:0x00fb, B:70:0x0108, B:71:0x0109, B:73:0x00c3, B:74:0x00d0, B:80:0x001a, B:83:0x0022), top: B:2:0x0001 }] */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0071 A[Catch: RecognitionException -> 0x0163, TryCatch #0 {RecognitionException -> 0x0163, blocks: (B:3:0x0001, B:5:0x000a, B:6:0x000d, B:7:0x0028, B:8:0x0033, B:10:0x0010, B:12:0x0035, B:15:0x0047, B:16:0x004a, B:17:0x0054, B:18:0x005f, B:19:0x004d, B:20:0x0061, B:23:0x006b, B:24:0x006e, B:25:0x0079, B:26:0x0084, B:27:0x0071, B:28:0x0086, B:31:0x008e, B:32:0x0091, B:33:0x00a1, B:34:0x00ac, B:35:0x0094, B:36:0x00ae, B:42:0x00be, B:43:0x00d3, B:48:0x00de, B:49:0x00eb, B:50:0x00ec, B:51:0x00f2, B:55:0x0110, B:57:0x013c, B:59:0x0145, B:61:0x014e, B:63:0x0157, B:64:0x015a, B:66:0x015f, B:69:0x00fb, B:70:0x0108, B:71:0x0109, B:73:0x00c3, B:74:0x00d0, B:80:0x001a, B:83:0x0022), top: B:2:0x0001 }] */
    /* JADX WARN: Removed duplicated region for block: B:30:0x008c A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0091 A[Catch: RecognitionException -> 0x0163, TryCatch #0 {RecognitionException -> 0x0163, blocks: (B:3:0x0001, B:5:0x000a, B:6:0x000d, B:7:0x0028, B:8:0x0033, B:10:0x0010, B:12:0x0035, B:15:0x0047, B:16:0x004a, B:17:0x0054, B:18:0x005f, B:19:0x004d, B:20:0x0061, B:23:0x006b, B:24:0x006e, B:25:0x0079, B:26:0x0084, B:27:0x0071, B:28:0x0086, B:31:0x008e, B:32:0x0091, B:33:0x00a1, B:34:0x00ac, B:35:0x0094, B:36:0x00ae, B:42:0x00be, B:43:0x00d3, B:48:0x00de, B:49:0x00eb, B:50:0x00ec, B:51:0x00f2, B:55:0x0110, B:57:0x013c, B:59:0x0145, B:61:0x014e, B:63:0x0157, B:64:0x015a, B:66:0x015f, B:69:0x00fb, B:70:0x0108, B:71:0x0109, B:73:0x00c3, B:74:0x00d0, B:80:0x001a, B:83:0x0022), top: B:2:0x0001 }] */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0094 A[Catch: RecognitionException -> 0x0163, TryCatch #0 {RecognitionException -> 0x0163, blocks: (B:3:0x0001, B:5:0x000a, B:6:0x000d, B:7:0x0028, B:8:0x0033, B:10:0x0010, B:12:0x0035, B:15:0x0047, B:16:0x004a, B:17:0x0054, B:18:0x005f, B:19:0x004d, B:20:0x0061, B:23:0x006b, B:24:0x006e, B:25:0x0079, B:26:0x0084, B:27:0x0071, B:28:0x0086, B:31:0x008e, B:32:0x0091, B:33:0x00a1, B:34:0x00ac, B:35:0x0094, B:36:0x00ae, B:42:0x00be, B:43:0x00d3, B:48:0x00de, B:49:0x00eb, B:50:0x00ec, B:51:0x00f2, B:55:0x0110, B:57:0x013c, B:59:0x0145, B:61:0x014e, B:63:0x0157, B:64:0x015a, B:66:0x015f, B:69:0x00fb, B:70:0x0108, B:71:0x0109, B:73:0x00c3, B:74:0x00d0, B:80:0x001a, B:83:0x0022), top: B:2:0x0001 }] */
    /* JADX WARN: Removed duplicated region for block: B:38:0x00b6 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:45:0x00d9  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x00f8  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x013c A[Catch: RecognitionException -> 0x0163, TryCatch #0 {RecognitionException -> 0x0163, blocks: (B:3:0x0001, B:5:0x000a, B:6:0x000d, B:7:0x0028, B:8:0x0033, B:10:0x0010, B:12:0x0035, B:15:0x0047, B:16:0x004a, B:17:0x0054, B:18:0x005f, B:19:0x004d, B:20:0x0061, B:23:0x006b, B:24:0x006e, B:25:0x0079, B:26:0x0084, B:27:0x0071, B:28:0x0086, B:31:0x008e, B:32:0x0091, B:33:0x00a1, B:34:0x00ac, B:35:0x0094, B:36:0x00ae, B:42:0x00be, B:43:0x00d3, B:48:0x00de, B:49:0x00eb, B:50:0x00ec, B:51:0x00f2, B:55:0x0110, B:57:0x013c, B:59:0x0145, B:61:0x014e, B:63:0x0157, B:64:0x015a, B:66:0x015f, B:69:0x00fb, B:70:0x0108, B:71:0x0109, B:73:0x00c3, B:74:0x00d0, B:80:0x001a, B:83:0x0022), top: B:2:0x0001 }] */
    /* JADX WARN: Removed duplicated region for block: B:59:0x0145 A[Catch: RecognitionException -> 0x0163, TryCatch #0 {RecognitionException -> 0x0163, blocks: (B:3:0x0001, B:5:0x000a, B:6:0x000d, B:7:0x0028, B:8:0x0033, B:10:0x0010, B:12:0x0035, B:15:0x0047, B:16:0x004a, B:17:0x0054, B:18:0x005f, B:19:0x004d, B:20:0x0061, B:23:0x006b, B:24:0x006e, B:25:0x0079, B:26:0x0084, B:27:0x0071, B:28:0x0086, B:31:0x008e, B:32:0x0091, B:33:0x00a1, B:34:0x00ac, B:35:0x0094, B:36:0x00ae, B:42:0x00be, B:43:0x00d3, B:48:0x00de, B:49:0x00eb, B:50:0x00ec, B:51:0x00f2, B:55:0x0110, B:57:0x013c, B:59:0x0145, B:61:0x014e, B:63:0x0157, B:64:0x015a, B:66:0x015f, B:69:0x00fb, B:70:0x0108, B:71:0x0109, B:73:0x00c3, B:74:0x00d0, B:80:0x001a, B:83:0x0022), top: B:2:0x0001 }] */
    /* JADX WARN: Removed duplicated region for block: B:61:0x014e A[Catch: RecognitionException -> 0x0163, TryCatch #0 {RecognitionException -> 0x0163, blocks: (B:3:0x0001, B:5:0x000a, B:6:0x000d, B:7:0x0028, B:8:0x0033, B:10:0x0010, B:12:0x0035, B:15:0x0047, B:16:0x004a, B:17:0x0054, B:18:0x005f, B:19:0x004d, B:20:0x0061, B:23:0x006b, B:24:0x006e, B:25:0x0079, B:26:0x0084, B:27:0x0071, B:28:0x0086, B:31:0x008e, B:32:0x0091, B:33:0x00a1, B:34:0x00ac, B:35:0x0094, B:36:0x00ae, B:42:0x00be, B:43:0x00d3, B:48:0x00de, B:49:0x00eb, B:50:0x00ec, B:51:0x00f2, B:55:0x0110, B:57:0x013c, B:59:0x0145, B:61:0x014e, B:63:0x0157, B:64:0x015a, B:66:0x015f, B:69:0x00fb, B:70:0x0108, B:71:0x0109, B:73:0x00c3, B:74:0x00d0, B:80:0x001a, B:83:0x0022), top: B:2:0x0001 }] */
    /* JADX WARN: Removed duplicated region for block: B:63:0x0157 A[Catch: RecognitionException -> 0x0163, TryCatch #0 {RecognitionException -> 0x0163, blocks: (B:3:0x0001, B:5:0x000a, B:6:0x000d, B:7:0x0028, B:8:0x0033, B:10:0x0010, B:12:0x0035, B:15:0x0047, B:16:0x004a, B:17:0x0054, B:18:0x005f, B:19:0x004d, B:20:0x0061, B:23:0x006b, B:24:0x006e, B:25:0x0079, B:26:0x0084, B:27:0x0071, B:28:0x0086, B:31:0x008e, B:32:0x0091, B:33:0x00a1, B:34:0x00ac, B:35:0x0094, B:36:0x00ae, B:42:0x00be, B:43:0x00d3, B:48:0x00de, B:49:0x00eb, B:50:0x00ec, B:51:0x00f2, B:55:0x0110, B:57:0x013c, B:59:0x0145, B:61:0x014e, B:63:0x0157, B:64:0x015a, B:66:0x015f, B:69:0x00fb, B:70:0x0108, B:71:0x0109, B:73:0x00c3, B:74:0x00d0, B:80:0x001a, B:83:0x0022), top: B:2:0x0001 }] */
    /* JADX WARN: Removed duplicated region for block: B:66:0x015f A[Catch: RecognitionException -> 0x0163, TRY_LEAVE, TryCatch #0 {RecognitionException -> 0x0163, blocks: (B:3:0x0001, B:5:0x000a, B:6:0x000d, B:7:0x0028, B:8:0x0033, B:10:0x0010, B:12:0x0035, B:15:0x0047, B:16:0x004a, B:17:0x0054, B:18:0x005f, B:19:0x004d, B:20:0x0061, B:23:0x006b, B:24:0x006e, B:25:0x0079, B:26:0x0084, B:27:0x0071, B:28:0x0086, B:31:0x008e, B:32:0x0091, B:33:0x00a1, B:34:0x00ac, B:35:0x0094, B:36:0x00ae, B:42:0x00be, B:43:0x00d3, B:48:0x00de, B:49:0x00eb, B:50:0x00ec, B:51:0x00f2, B:55:0x0110, B:57:0x013c, B:59:0x0145, B:61:0x014e, B:63:0x0157, B:64:0x015a, B:66:0x015f, B:69:0x00fb, B:70:0x0108, B:71:0x0109, B:73:0x00c3, B:74:0x00d0, B:80:0x001a, B:83:0x0022), top: B:2:0x0001 }] */
    /* JADX WARN: Removed duplicated region for block: B:68:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:71:0x0109 A[Catch: RecognitionException -> 0x0163, TryCatch #0 {RecognitionException -> 0x0163, blocks: (B:3:0x0001, B:5:0x000a, B:6:0x000d, B:7:0x0028, B:8:0x0033, B:10:0x0010, B:12:0x0035, B:15:0x0047, B:16:0x004a, B:17:0x0054, B:18:0x005f, B:19:0x004d, B:20:0x0061, B:23:0x006b, B:24:0x006e, B:25:0x0079, B:26:0x0084, B:27:0x0071, B:28:0x0086, B:31:0x008e, B:32:0x0091, B:33:0x00a1, B:34:0x00ac, B:35:0x0094, B:36:0x00ae, B:42:0x00be, B:43:0x00d3, B:48:0x00de, B:49:0x00eb, B:50:0x00ec, B:51:0x00f2, B:55:0x0110, B:57:0x013c, B:59:0x0145, B:61:0x014e, B:63:0x0157, B:64:0x015a, B:66:0x015f, B:69:0x00fb, B:70:0x0108, B:71:0x0109, B:73:0x00c3, B:74:0x00d0, B:80:0x001a, B:83:0x0022), top: B:2:0x0001 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void rule(Grammar grammar) {
        String str;
        boolean z;
        int mo107LA;
        Token token;
        int mo107LA2;
        Token token2;
        int mo107LA3;
        String str2;
        int mo107LA4;
        IndexedVector indexedVector;
        int mo107LA5;
        try {
            int mo107LA6 = mo107LA(1);
            Token token3 = null;
            if (mo107LA6 != 9) {
                switch (mo107LA6) {
                    case 16:
                        match(16);
                        str = "protected";
                        break;
                    case 17:
                        match(17);
                        str = "private";
                        break;
                    case 18:
                        match(18);
                        str = "public";
                        break;
                    default:
                        throw new NoViableAltException(mo108LT(1), getFilename());
                }
            } else {
                str = null;
            }
            Token mo108LT = mo108LT(1);
            match(9);
            int mo107LA7 = mo107LA(1);
            if (mo107LA7 != 7 && mo107LA7 != 13) {
                switch (mo107LA7) {
                    case 19:
                        match(19);
                        z = true;
                        break;
                    case 20:
                    case 21:
                    case 22:
                    case 23:
                        break;
                    default:
                        throw new NoViableAltException(mo108LT(1), getFilename());
                }
                mo107LA = mo107LA(1);
                if (mo107LA != 7 && mo107LA != 13) {
                    switch (mo107LA) {
                        case 20:
                            token = mo108LT(1);
                            match(20);
                            break;
                        case 21:
                        case 22:
                        case 23:
                            break;
                        default:
                            throw new NoViableAltException(mo108LT(1), getFilename());
                    }
                    mo107LA2 = mo107LA(1);
                    if (mo107LA2 != 7 && mo107LA2 != 13) {
                        switch (mo107LA2) {
                            case 21:
                                match(21);
                                token2 = mo108LT(1);
                                match(20);
                                break;
                            case 22:
                            case 23:
                                break;
                            default:
                                throw new NoViableAltException(mo108LT(1), getFilename());
                        }
                        mo107LA3 = mo107LA(1);
                        if (mo107LA3 != 7 || mo107LA3 == 13 || mo107LA3 == 22) {
                            str2 = "";
                        } else {
                            if (mo107LA3 != 23) {
                                throw new NoViableAltException(mo108LT(1), getFilename());
                            }
                            str2 = throwsSpec();
                        }
                        mo107LA4 = mo107LA(1);
                        if (mo107LA4 != 7) {
                            if (mo107LA4 == 13) {
                                indexedVector = optionSpec(null);
                                mo107LA5 = mo107LA(1);
                                if (mo107LA5 == 7) {
                                    token3 = mo108LT(1);
                                    match(7);
                                } else if (mo107LA5 != 22) {
                                    throw new NoViableAltException(mo108LT(1), getFilename());
                                }
                                Token mo108LT2 = mo108LT(1);
                                match(22);
                                Rule rule = new Rule(mo108LT.getText(), mo108LT2.getText() + exceptionGroup(), indexedVector, grammar);
                                rule.setThrowsSpec(str2);
                                if (token != null) {
                                    rule.setArgs(token.getText());
                                }
                                if (token2 != null) {
                                    rule.setReturnValue(token2.getText());
                                }
                                if (token3 != null) {
                                    rule.setInitAction(token3.getText());
                                }
                                if (z) {
                                    rule.setBang();
                                }
                                rule.setVisibility(str);
                                if (grammar != null) {
                                    grammar.addRule(rule);
                                    return;
                                }
                                return;
                            }
                            if (mo107LA4 != 22) {
                                throw new NoViableAltException(mo108LT(1), getFilename());
                            }
                        }
                        indexedVector = null;
                        mo107LA5 = mo107LA(1);
                        if (mo107LA5 == 7) {
                        }
                        Token mo108LT22 = mo108LT(1);
                        match(22);
                        Rule rule2 = new Rule(mo108LT.getText(), mo108LT22.getText() + exceptionGroup(), indexedVector, grammar);
                        rule2.setThrowsSpec(str2);
                        if (token != null) {
                        }
                        if (token2 != null) {
                        }
                        if (token3 != null) {
                        }
                        if (z) {
                        }
                        rule2.setVisibility(str);
                        if (grammar != null) {
                        }
                    }
                    token2 = null;
                    mo107LA3 = mo107LA(1);
                    if (mo107LA3 != 7) {
                    }
                    str2 = "";
                    mo107LA4 = mo107LA(1);
                    if (mo107LA4 != 7) {
                    }
                    indexedVector = null;
                    mo107LA5 = mo107LA(1);
                    if (mo107LA5 == 7) {
                    }
                    Token mo108LT222 = mo108LT(1);
                    match(22);
                    Rule rule22 = new Rule(mo108LT.getText(), mo108LT222.getText() + exceptionGroup(), indexedVector, grammar);
                    rule22.setThrowsSpec(str2);
                    if (token != null) {
                    }
                    if (token2 != null) {
                    }
                    if (token3 != null) {
                    }
                    if (z) {
                    }
                    rule22.setVisibility(str);
                    if (grammar != null) {
                    }
                }
                token = null;
                mo107LA2 = mo107LA(1);
                if (mo107LA2 != 7) {
                    switch (mo107LA2) {
                    }
                    mo107LA3 = mo107LA(1);
                    if (mo107LA3 != 7) {
                    }
                    str2 = "";
                    mo107LA4 = mo107LA(1);
                    if (mo107LA4 != 7) {
                    }
                    indexedVector = null;
                    mo107LA5 = mo107LA(1);
                    if (mo107LA5 == 7) {
                    }
                    Token mo108LT2222 = mo108LT(1);
                    match(22);
                    Rule rule222 = new Rule(mo108LT.getText(), mo108LT2222.getText() + exceptionGroup(), indexedVector, grammar);
                    rule222.setThrowsSpec(str2);
                    if (token != null) {
                    }
                    if (token2 != null) {
                    }
                    if (token3 != null) {
                    }
                    if (z) {
                    }
                    rule222.setVisibility(str);
                    if (grammar != null) {
                    }
                }
                token2 = null;
                mo107LA3 = mo107LA(1);
                if (mo107LA3 != 7) {
                }
                str2 = "";
                mo107LA4 = mo107LA(1);
                if (mo107LA4 != 7) {
                }
                indexedVector = null;
                mo107LA5 = mo107LA(1);
                if (mo107LA5 == 7) {
                }
                Token mo108LT22222 = mo108LT(1);
                match(22);
                Rule rule2222 = new Rule(mo108LT.getText(), mo108LT22222.getText() + exceptionGroup(), indexedVector, grammar);
                rule2222.setThrowsSpec(str2);
                if (token != null) {
                }
                if (token2 != null) {
                }
                if (token3 != null) {
                }
                if (z) {
                }
                rule2222.setVisibility(str);
                if (grammar != null) {
                }
            }
            z = false;
            mo107LA = mo107LA(1);
            if (mo107LA != 7) {
                switch (mo107LA) {
                }
                mo107LA2 = mo107LA(1);
                if (mo107LA2 != 7) {
                }
                token2 = null;
                mo107LA3 = mo107LA(1);
                if (mo107LA3 != 7) {
                }
                str2 = "";
                mo107LA4 = mo107LA(1);
                if (mo107LA4 != 7) {
                }
                indexedVector = null;
                mo107LA5 = mo107LA(1);
                if (mo107LA5 == 7) {
                }
                Token mo108LT222222 = mo108LT(1);
                match(22);
                Rule rule22222 = new Rule(mo108LT.getText(), mo108LT222222.getText() + exceptionGroup(), indexedVector, grammar);
                rule22222.setThrowsSpec(str2);
                if (token != null) {
                }
                if (token2 != null) {
                }
                if (token3 != null) {
                }
                if (z) {
                }
                rule22222.setVisibility(str);
                if (grammar != null) {
                }
            }
            token = null;
            mo107LA2 = mo107LA(1);
            if (mo107LA2 != 7) {
            }
            token2 = null;
            mo107LA3 = mo107LA(1);
            if (mo107LA3 != 7) {
            }
            str2 = "";
            mo107LA4 = mo107LA(1);
            if (mo107LA4 != 7) {
            }
            indexedVector = null;
            mo107LA5 = mo107LA(1);
            if (mo107LA5 == 7) {
            }
            Token mo108LT2222222 = mo108LT(1);
            match(22);
            Rule rule222222 = new Rule(mo108LT.getText(), mo108LT2222222.getText() + exceptionGroup(), indexedVector, grammar);
            rule222222.setThrowsSpec(str2);
            if (token != null) {
            }
            if (token2 != null) {
            }
            if (token3 != null) {
            }
            if (z) {
            }
            rule222222.setVisibility(str);
            if (grammar != null) {
            }
        } catch (RecognitionException e) {
            reportError(e);
            consume();
            consumeUntil(_tokenSet_5);
        }
    }

    public void setTool(antlr.Tool tool) {
        if (this.antlrTool != null) {
            throw new IllegalStateException("antlr.Tool already registered");
        }
        this.antlrTool = tool;
    }

    public final String superClass() {
        String text = mo108LT(1).getText();
        try {
            match(6);
        } catch (RecognitionException e) {
            reportError(e);
            consume();
            consumeUntil(_tokenSet_4);
        }
        return text;
    }

    public final String throwsSpec() {
        String str = "throws ";
        try {
            match(23);
            Token mo108LT = mo108LT(1);
            match(9);
            StringBuilder sb = new StringBuilder();
            sb.append("throws ");
            String text = mo108LT.getText();
            while (true) {
                sb.append(text);
                str = sb.toString();
                if (mo107LA(1) != 24) {
                    break;
                }
                match(24);
                Token mo108LT2 = mo108LT(1);
                match(9);
                sb = new StringBuilder();
                sb.append(str);
                sb.append(",");
                text = mo108LT2.getText();
            }
        } catch (RecognitionException e) {
            reportError(e);
            consume();
            consumeUntil(_tokenSet_6);
        }
        return str;
    }
}
