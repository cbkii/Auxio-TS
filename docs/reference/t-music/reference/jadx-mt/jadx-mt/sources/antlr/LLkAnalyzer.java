package antlr;

import antlr.collections.impl.BitSet;
import java.io.PrintStream;
import p000a.p001a.p002a.p003a.C0000a;

/* JADX INFO: loaded from: classes3.dex */
public class LLkAnalyzer implements LLkGrammarAnalyzer {
    public AlternativeBlock currentBlock;
    public Tool tool;
    public boolean DEBUG_ANALYZER = false;
    public Grammar grammar = null;
    public boolean lexicalAnalysis = false;
    public CharFormatter charFormatter = new JavaCharFormatter();

    public LLkAnalyzer(Tool tool) {
        this.tool = null;
        this.tool = tool;
    }

    private Lookahead getAltLookahead(AlternativeBlock alternativeBlock, int i, int i2) {
        Alternative alternativeAt = alternativeBlock.getAlternativeAt(i);
        AlternativeElement alternativeElement = alternativeAt.head;
        Lookahead[] lookaheadArr = alternativeAt.cache;
        if (lookaheadArr[i2] != null) {
            return lookaheadArr[i2];
        }
        Lookahead lookaheadLook = alternativeElement.look(i2);
        alternativeAt.cache[i2] = lookaheadLook;
        return lookaheadLook;
    }

    public static boolean lookaheadEquivForApproxAndFullAnalysis(Lookahead[] lookaheadArr, int i) {
        for (int i2 = 1; i2 <= i - 1; i2++) {
            if (lookaheadArr[i2].fset.degree() > 1) {
                return false;
            }
        }
        return true;
    }

    private void removeCompetingPredictionSets(BitSet bitSet, AlternativeElement alternativeElement) {
        AlternativeBlock alternativeBlock = this.currentBlock;
        AlternativeElement alternativeElement2 = alternativeBlock.getAlternativeAt(alternativeBlock.analysisAlt).head;
        if (alternativeElement2 instanceof TreeElement) {
            if (((TreeElement) alternativeElement2).root != alternativeElement) {
                return;
            }
        } else if (alternativeElement != alternativeElement2) {
            return;
        }
        int i = 0;
        while (true) {
            AlternativeBlock alternativeBlock2 = this.currentBlock;
            if (i >= alternativeBlock2.analysisAlt) {
                return;
            }
            bitSet.subtractInPlace(alternativeBlock2.getAlternativeAt(i).head.look(1).fset);
            i++;
        }
    }

    private void removeCompetingPredictionSetsFromWildcard(Lookahead[] lookaheadArr, AlternativeElement alternativeElement, int i) {
        for (int i2 = 1; i2 <= i; i2++) {
            int i3 = 0;
            while (true) {
                AlternativeBlock alternativeBlock = this.currentBlock;
                if (i3 < alternativeBlock.analysisAlt) {
                    lookaheadArr[i2].fset.subtractInPlace(alternativeBlock.getAlternativeAt(i3).head.look(i2).fset);
                    i3++;
                }
            }
        }
    }

    private void reset() {
        this.grammar = null;
        this.DEBUG_ANALYZER = false;
        this.currentBlock = null;
        this.lexicalAnalysis = false;
    }

    @Override // antlr.LLkGrammarAnalyzer
    public Lookahead FOLLOW(int i, RuleEndElement ruleEndElement) {
        BitSet bitSet;
        int i2;
        Object obj;
        RuleBlock ruleBlock = (RuleBlock) ruleEndElement.block;
        boolean z = this.lexicalAnalysis;
        String ruleName = ruleBlock.getRuleName();
        if (z) {
            ruleName = CodeGenerator.encodeLexerRuleName(ruleName);
        }
        if (this.DEBUG_ANALYZER) {
            System.out.println("FOLLOW(" + i + "," + ruleName + ")");
        }
        boolean[] zArr = ruleEndElement.lock;
        if (zArr[i]) {
            if (this.DEBUG_ANALYZER) {
                System.out.println("FOLLOW cycle to " + ruleName);
            }
            return new Lookahead(ruleName);
        }
        if (ruleEndElement.cache[i] != null) {
            if (this.DEBUG_ANALYZER) {
                System.out.println("cache entry FOLLOW(" + i + ") for " + ruleName + ": " + ruleEndElement.cache[i].toString(",", this.charFormatter, this.grammar));
            }
            Lookahead[] lookaheadArr = ruleEndElement.cache;
            if (lookaheadArr[i].cycle == null) {
                return (Lookahead) lookaheadArr[i].clone();
            }
            RuleEndElement ruleEndElement2 = ((RuleSymbol) this.grammar.getSymbol(lookaheadArr[i].cycle)).getBlock().endNode;
            if (ruleEndElement2.cache[i] == null) {
                return (Lookahead) ruleEndElement.cache[i].clone();
            }
            if (this.DEBUG_ANALYZER) {
                System.out.println("combining FOLLOW(" + i + ") for " + ruleName + ": from " + ruleEndElement.cache[i].toString(",", this.charFormatter, this.grammar) + " with FOLLOW for " + ((RuleBlock) ruleEndElement2.block).getRuleName() + ": " + ruleEndElement2.cache[i].toString(",", this.charFormatter, this.grammar));
            }
            Lookahead[] lookaheadArr2 = ruleEndElement2.cache;
            if (lookaheadArr2[i].cycle == null) {
                ruleEndElement.cache[i].combineWith(lookaheadArr2[i]);
                ruleEndElement.cache[i].cycle = null;
            } else {
                Lookahead lookaheadFOLLOW = FOLLOW(i, ruleEndElement2);
                ruleEndElement.cache[i].combineWith(lookaheadFOLLOW);
                ruleEndElement.cache[i].cycle = lookaheadFOLLOW.cycle;
            }
            if (this.DEBUG_ANALYZER) {
                System.out.println("saving FOLLOW(" + i + ") for " + ruleName + ": from " + ruleEndElement.cache[i].toString(",", this.charFormatter, this.grammar));
            }
            return (Lookahead) ruleEndElement.cache[i].clone();
        }
        zArr[i] = true;
        Lookahead lookahead = new Lookahead();
        RuleSymbol ruleSymbol = (RuleSymbol) this.grammar.getSymbol(ruleName);
        for (int i3 = 0; i3 < ruleSymbol.numReferences(); i3++) {
            RuleRefElement reference = ruleSymbol.getReference(i3);
            if (this.DEBUG_ANALYZER) {
                PrintStream printStream = System.out;
                StringBuilder sbM10b = C0000a.m10b("next[", ruleName, "] is ");
                sbM10b.append(reference.next.toString());
                printStream.println(sbM10b.toString());
            }
            Lookahead lookaheadLook = reference.next.look(i);
            if (this.DEBUG_ANALYZER) {
                PrintStream printStream2 = System.out;
                StringBuilder sbM10b2 = C0000a.m10b("FIRST of next[", ruleName, "] ptr is ");
                sbM10b2.append(lookaheadLook.toString());
                printStream2.println(sbM10b2.toString());
            }
            String str = lookaheadLook.cycle;
            if (str == null || !str.equals(ruleName)) {
                obj = null;
            } else {
                obj = null;
                lookaheadLook.cycle = null;
            }
            lookahead.combineWith(lookaheadLook);
            if (this.DEBUG_ANALYZER) {
                PrintStream printStream3 = System.out;
                StringBuilder sbM10b3 = C0000a.m10b("combined FOLLOW[", ruleName, "] is ");
                sbM10b3.append(lookahead.toString());
                printStream3.println(sbM10b3.toString());
            }
        }
        ruleEndElement.lock[i] = false;
        if (lookahead.fset.nil() && lookahead.cycle == null) {
            Grammar grammar = this.grammar;
            if (grammar instanceof TreeWalkerGrammar) {
                bitSet = lookahead.fset;
                i2 = 3;
            } else if (grammar instanceof LexerGrammar) {
                lookahead.setEpsilon();
            } else {
                bitSet = lookahead.fset;
                i2 = 1;
            }
            bitSet.add(i2);
        }
        if (this.DEBUG_ANALYZER) {
            System.out.println("saving FOLLOW(" + i + ") for " + ruleName + ": " + lookahead.toString(",", this.charFormatter, this.grammar));
        }
        ruleEndElement.cache[i] = (Lookahead) lookahead.clone();
        return lookahead;
    }

    public boolean altUsesWildcardDefault(Alternative alternative) {
        AlternativeElement alternativeElement = alternative.head;
        if ((alternativeElement instanceof TreeElement) && (((TreeElement) alternativeElement).root instanceof WildcardElement)) {
            return true;
        }
        return (alternativeElement instanceof WildcardElement) && (alternativeElement.next instanceof BlockEndElement);
    }

    @Override // antlr.LLkGrammarAnalyzer
    public boolean deterministic(AlternativeBlock alternativeBlock) {
        boolean z;
        boolean z2;
        int i;
        PrintStream printStream;
        StringBuilder sb;
        String str;
        if (this.DEBUG_ANALYZER) {
            System.out.println("deterministic(" + alternativeBlock + ")");
        }
        int size = alternativeBlock.alternatives.size();
        AlternativeBlock alternativeBlock2 = this.currentBlock;
        this.currentBlock = alternativeBlock;
        if (!alternativeBlock.greedy && !(alternativeBlock instanceof OneOrMoreBlock) && !(alternativeBlock instanceof ZeroOrMoreBlock)) {
            this.tool.warning("Being nongreedy only makes sense for (...)+ and (...)*", this.grammar.getFilename(), alternativeBlock.getLine(), alternativeBlock.getColumn());
        }
        int i2 = 1;
        if (size == 1) {
            AlternativeElement alternativeElement = alternativeBlock.getAlternativeAt(0).head;
            this.currentBlock.alti = 0;
            alternativeBlock.getAlternativeAt(0).cache[1] = alternativeElement.look(1);
            alternativeBlock.getAlternativeAt(0).lookaheadDepth = 1;
            this.currentBlock = alternativeBlock2;
            return true;
        }
        int i3 = 0;
        boolean z3 = true;
        while (i3 < size - 1) {
            AlternativeBlock alternativeBlock3 = this.currentBlock;
            alternativeBlock3.alti = i3;
            alternativeBlock3.analysisAlt = i3;
            int i4 = i3 + 1;
            alternativeBlock3.altj = i4;
            int i5 = i4;
            while (i5 < size) {
                this.currentBlock.altj = i5;
                if (this.DEBUG_ANALYZER) {
                    System.out.println("comparing " + i3 + " against alt " + i5);
                }
                this.currentBlock.analysisAlt = i5;
                Lookahead[] lookaheadArr = new Lookahead[this.grammar.maxk + i2];
                int i6 = i2;
                while (true) {
                    if (this.DEBUG_ANALYZER) {
                        System.out.println("checking depth " + i6 + "<=" + this.grammar.maxk);
                    }
                    Lookahead altLookahead = getAltLookahead(alternativeBlock, i3, i6);
                    Lookahead altLookahead2 = getAltLookahead(alternativeBlock, i5, i6);
                    if (this.DEBUG_ANALYZER) {
                        PrintStream printStream2 = System.out;
                        StringBuilder sbM5a = C0000a.m5a("p is ");
                        z = z3;
                        sbM5a.append(altLookahead.toString(",", this.charFormatter, this.grammar));
                        printStream2.println(sbM5a.toString());
                    } else {
                        z = z3;
                    }
                    if (this.DEBUG_ANALYZER) {
                        PrintStream printStream3 = System.out;
                        StringBuilder sbM5a2 = C0000a.m5a("q is ");
                        sbM5a2.append(altLookahead2.toString(",", this.charFormatter, this.grammar));
                        printStream3.println(sbM5a2.toString());
                    }
                    lookaheadArr[i6] = altLookahead.intersection(altLookahead2);
                    if (this.DEBUG_ANALYZER) {
                        System.out.println("intersection at depth " + i6 + " is " + lookaheadArr[i6].toString());
                    }
                    if (lookaheadArr[i6].nil()) {
                        z2 = false;
                    } else {
                        i6++;
                        z2 = true;
                    }
                    if (!z2 || i6 > this.grammar.maxk) {
                        break;
                    }
                    z3 = z;
                }
                Alternative alternativeAt = alternativeBlock.getAlternativeAt(i3);
                Alternative alternativeAt2 = alternativeBlock.getAlternativeAt(i5);
                if (z2) {
                    alternativeAt.lookaheadDepth = Integer.MAX_VALUE;
                    alternativeAt2.lookaheadDepth = Integer.MAX_VALUE;
                    if (alternativeAt.synPred != null) {
                        if (this.DEBUG_ANALYZER) {
                            printStream = System.out;
                            sb = new StringBuilder();
                            sb.append("alt ");
                            sb.append(i3);
                            str = " has a syn pred";
                            sb.append(str);
                            printStream.println(sb.toString());
                        }
                        i = i5;
                        z3 = false;
                    } else {
                        if (alternativeAt.semPred == null) {
                            if (!altUsesWildcardDefault(alternativeAt2) && ((alternativeBlock.warnWhenFollowAmbig || (!(alternativeAt.head instanceof BlockEndElement) && !(alternativeAt2.head instanceof BlockEndElement))) && alternativeBlock.generateAmbigWarnings && (!alternativeBlock.greedySet || !alternativeBlock.greedy || ((!(alternativeAt.head instanceof BlockEndElement) || (alternativeAt2.head instanceof BlockEndElement)) && (!(alternativeAt2.head instanceof BlockEndElement) || (alternativeAt.head instanceof BlockEndElement)))))) {
                                ToolErrorHandler toolErrorHandler = this.tool.errorHandler;
                                Grammar grammar = this.grammar;
                                i = i5;
                                toolErrorHandler.warnAltAmbiguity(grammar, alternativeBlock, this.lexicalAnalysis, grammar.maxk, lookaheadArr, i3, i5);
                            }
                            z3 = false;
                        } else if (this.DEBUG_ANALYZER) {
                            printStream = System.out;
                            sb = new StringBuilder();
                            sb.append("alt ");
                            sb.append(i3);
                            str = " has a sem pred";
                            sb.append(str);
                            printStream.println(sb.toString());
                        }
                        i = i5;
                        z3 = false;
                    }
                } else {
                    i = i5;
                    alternativeAt.lookaheadDepth = Math.max(alternativeAt.lookaheadDepth, i6);
                    alternativeAt2.lookaheadDepth = Math.max(alternativeAt2.lookaheadDepth, i6);
                    z3 = z;
                }
                i5 = i + 1;
                i2 = 1;
            }
            i3 = i4;
        }
        this.currentBlock = alternativeBlock2;
        return z3;
    }

    @Override // antlr.LLkGrammarAnalyzer
    public boolean deterministic(OneOrMoreBlock oneOrMoreBlock) {
        if (this.DEBUG_ANALYZER) {
            System.out.println("deterministic(...)+(" + oneOrMoreBlock + ")");
        }
        AlternativeBlock alternativeBlock = this.currentBlock;
        this.currentBlock = oneOrMoreBlock;
        boolean zDeterministic = deterministic((AlternativeBlock) oneOrMoreBlock);
        boolean zDeterministicImpliedPath = deterministicImpliedPath(oneOrMoreBlock);
        this.currentBlock = alternativeBlock;
        return zDeterministicImpliedPath && zDeterministic;
    }

    @Override // antlr.LLkGrammarAnalyzer
    public boolean deterministic(ZeroOrMoreBlock zeroOrMoreBlock) {
        if (this.DEBUG_ANALYZER) {
            System.out.println("deterministic(...)*(" + zeroOrMoreBlock + ")");
        }
        AlternativeBlock alternativeBlock = this.currentBlock;
        this.currentBlock = zeroOrMoreBlock;
        boolean zDeterministic = deterministic((AlternativeBlock) zeroOrMoreBlock);
        boolean zDeterministicImpliedPath = deterministicImpliedPath(zeroOrMoreBlock);
        this.currentBlock = alternativeBlock;
        return zDeterministicImpliedPath && zDeterministic;
    }

    public boolean deterministicImpliedPath(BlockWithImpliedExitPath blockWithImpliedExitPath) {
        int i;
        boolean z;
        int size = blockWithImpliedExitPath.getAlternatives().size();
        this.currentBlock.altj = -1;
        if (this.DEBUG_ANALYZER) {
            System.out.println("deterministicImpliedPath");
        }
        boolean z2 = true;
        for (int i2 = 0; i2 < size; i2++) {
            Alternative alternativeAt = blockWithImpliedExitPath.getAlternativeAt(i2);
            if (alternativeAt.head instanceof BlockEndElement) {
                this.tool.warning("empty alternative makes no sense in (...)* or (...)+", this.grammar.getFilename(), blockWithImpliedExitPath.getLine(), blockWithImpliedExitPath.getColumn());
            }
            Lookahead[] lookaheadArr = new Lookahead[this.grammar.maxk + 1];
            int i3 = 1;
            while (true) {
                if (this.DEBUG_ANALYZER) {
                    System.out.println("checking depth " + i3 + "<=" + this.grammar.maxk);
                }
                Lookahead lookaheadLook = blockWithImpliedExitPath.next.look(i3);
                blockWithImpliedExitPath.exitCache[i3] = lookaheadLook;
                this.currentBlock.alti = i2;
                Lookahead altLookahead = getAltLookahead(blockWithImpliedExitPath, i2, i3);
                if (this.DEBUG_ANALYZER) {
                    PrintStream printStream = System.out;
                    StringBuilder sbM5a = C0000a.m5a("follow is ");
                    sbM5a.append(lookaheadLook.toString(",", this.charFormatter, this.grammar));
                    printStream.println(sbM5a.toString());
                }
                if (this.DEBUG_ANALYZER) {
                    PrintStream printStream2 = System.out;
                    StringBuilder sbM5a2 = C0000a.m5a("p is ");
                    sbM5a2.append(altLookahead.toString(",", this.charFormatter, this.grammar));
                    printStream2.println(sbM5a2.toString());
                }
                lookaheadArr[i3] = lookaheadLook.intersection(altLookahead);
                if (this.DEBUG_ANALYZER) {
                    System.out.println("intersection at depth " + i3 + " is " + lookaheadArr[i3]);
                }
                if (lookaheadArr[i3].nil()) {
                    i = i3;
                    z = false;
                } else {
                    i = i3 + 1;
                    z = true;
                }
                if (!z || i > this.grammar.maxk) {
                    break;
                }
                i3 = i;
            }
            if (z) {
                alternativeAt.lookaheadDepth = Integer.MAX_VALUE;
                blockWithImpliedExitPath.exitLookaheadDepth = Integer.MAX_VALUE;
                Alternative alternativeAt2 = blockWithImpliedExitPath.getAlternativeAt(this.currentBlock.alti);
                if (blockWithImpliedExitPath.warnWhenFollowAmbig && blockWithImpliedExitPath.generateAmbigWarnings) {
                    if (blockWithImpliedExitPath.greedy && blockWithImpliedExitPath.greedySet && !(alternativeAt2.head instanceof BlockEndElement)) {
                        if (this.DEBUG_ANALYZER) {
                            System.out.println("greedy loop");
                        }
                    } else if (blockWithImpliedExitPath.greedy || (alternativeAt2.head instanceof BlockEndElement)) {
                        ToolErrorHandler toolErrorHandler = this.tool.errorHandler;
                        Grammar grammar = this.grammar;
                        toolErrorHandler.warnAltExitAmbiguity(grammar, blockWithImpliedExitPath, this.lexicalAnalysis, grammar.maxk, lookaheadArr, i2);
                    } else {
                        if (this.DEBUG_ANALYZER) {
                            System.out.println("nongreedy loop");
                        }
                        if (!lookaheadEquivForApproxAndFullAnalysis(blockWithImpliedExitPath.exitCache, this.grammar.maxk)) {
                            this.tool.warning(new String[]{"nongreedy block may exit incorrectly due", "\tto limitations of linear approximate lookahead (first k-1 sets", "\tin lookahead not singleton)."}, this.grammar.getFilename(), blockWithImpliedExitPath.getLine(), blockWithImpliedExitPath.getColumn());
                        }
                    }
                }
                z2 = false;
            } else {
                alternativeAt.lookaheadDepth = Math.max(alternativeAt.lookaheadDepth, i);
                blockWithImpliedExitPath.exitLookaheadDepth = Math.max(blockWithImpliedExitPath.exitLookaheadDepth, i);
            }
        }
        return z2;
    }

    @Override // antlr.LLkGrammarAnalyzer
    public Lookahead look(int i, ActionElement actionElement) {
        if (this.DEBUG_ANALYZER) {
            System.out.println("lookAction(" + i + "," + actionElement + ")");
        }
        return actionElement.next.look(i);
    }

    @Override // antlr.LLkGrammarAnalyzer
    public Lookahead look(int i, AlternativeBlock alternativeBlock) {
        if (this.DEBUG_ANALYZER) {
            System.out.println("lookAltBlk(" + i + "," + alternativeBlock + ")");
        }
        AlternativeBlock alternativeBlock2 = this.currentBlock;
        this.currentBlock = alternativeBlock;
        Lookahead lookahead = new Lookahead();
        for (int i2 = 0; i2 < alternativeBlock.alternatives.size(); i2++) {
            if (this.DEBUG_ANALYZER) {
                System.out.println("alt " + i2 + " of " + alternativeBlock);
            }
            this.currentBlock.analysisAlt = i2;
            Alternative alternativeAt = alternativeBlock.getAlternativeAt(i2);
            AlternativeElement alternativeElement = alternativeAt.head;
            if (this.DEBUG_ANALYZER && alternativeElement == alternativeAt.tail) {
                System.out.println("alt " + i2 + " is empty");
            }
            lookahead.combineWith(alternativeElement.look(i));
        }
        if (i == 1 && alternativeBlock.not && subruleCanBeInverted(alternativeBlock, this.lexicalAnalysis)) {
            if (this.lexicalAnalysis) {
                BitSet bitSet = (BitSet) ((LexerGrammar) this.grammar).charVocabulary.clone();
                for (int i3 : lookahead.fset.toArray()) {
                    bitSet.remove(i3);
                }
                lookahead.fset = bitSet;
            } else {
                lookahead.fset.notInPlace(4, this.grammar.tokenManager.maxTokenType());
            }
        }
        this.currentBlock = alternativeBlock2;
        return lookahead;
    }

    @Override // antlr.LLkGrammarAnalyzer
    public Lookahead look(int i, BlockEndElement blockEndElement) {
        Lookahead lookaheadLook;
        Lookahead lookaheadLook2;
        if (this.DEBUG_ANALYZER) {
            System.out.println("lookBlockEnd(" + i + ", " + blockEndElement.block + "); lock is " + blockEndElement.lock[i]);
        }
        if (blockEndElement.lock[i]) {
            return new Lookahead();
        }
        AlternativeBlock alternativeBlock = blockEndElement.block;
        if ((alternativeBlock instanceof ZeroOrMoreBlock) || (alternativeBlock instanceof OneOrMoreBlock)) {
            blockEndElement.lock[i] = true;
            lookaheadLook = look(i, blockEndElement.block);
            blockEndElement.lock[i] = false;
        } else {
            lookaheadLook = new Lookahead();
        }
        AlternativeBlock alternativeBlock2 = blockEndElement.block;
        if (alternativeBlock2 instanceof TreeElement) {
            lookaheadLook2 = Lookahead.m109of(3);
        } else {
            if (alternativeBlock2 instanceof SynPredBlock) {
                lookaheadLook.setEpsilon();
                return lookaheadLook;
            }
            lookaheadLook2 = alternativeBlock2.next.look(i);
        }
        lookaheadLook.combineWith(lookaheadLook2);
        return lookaheadLook;
    }

    @Override // antlr.LLkGrammarAnalyzer
    public Lookahead look(int i, CharLiteralElement charLiteralElement) {
        if (this.DEBUG_ANALYZER) {
            System.out.println("lookCharLiteral(" + i + "," + charLiteralElement + ")");
        }
        if (i > 1) {
            return charLiteralElement.next.look(i - 1);
        }
        if (!this.lexicalAnalysis) {
            this.tool.panic("Character literal reference found in parser");
            return Lookahead.m109of(charLiteralElement.getType());
        }
        if (!charLiteralElement.not) {
            return Lookahead.m109of(charLiteralElement.getType());
        }
        BitSet bitSet = (BitSet) ((LexerGrammar) this.grammar).charVocabulary.clone();
        if (this.DEBUG_ANALYZER) {
            PrintStream printStream = System.out;
            StringBuilder sbM5a = C0000a.m5a("charVocab is ");
            sbM5a.append(bitSet.toString());
            printStream.println(sbM5a.toString());
        }
        removeCompetingPredictionSets(bitSet, charLiteralElement);
        if (this.DEBUG_ANALYZER) {
            PrintStream printStream2 = System.out;
            StringBuilder sbM5a2 = C0000a.m5a("charVocab after removal of prior alt lookahead ");
            sbM5a2.append(bitSet.toString());
            printStream2.println(sbM5a2.toString());
        }
        bitSet.clear(charLiteralElement.getType());
        return new Lookahead(bitSet);
    }

    @Override // antlr.LLkGrammarAnalyzer
    public Lookahead look(int i, CharRangeElement charRangeElement) {
        if (this.DEBUG_ANALYZER) {
            System.out.println("lookCharRange(" + i + "," + charRangeElement + ")");
        }
        if (i > 1) {
            return charRangeElement.next.look(i - 1);
        }
        BitSet bitSetM114of = BitSet.m114of(charRangeElement.begin);
        for (int i2 = charRangeElement.begin + 1; i2 <= charRangeElement.end; i2++) {
            bitSetM114of.add(i2);
        }
        return new Lookahead(bitSetM114of);
    }

    @Override // antlr.LLkGrammarAnalyzer
    public Lookahead look(int i, GrammarAtom grammarAtom) {
        if (this.DEBUG_ANALYZER) {
            System.out.println("look(" + i + "," + grammarAtom + "[" + grammarAtom.getType() + "])");
        }
        if (this.lexicalAnalysis) {
            this.tool.panic("token reference found in lexer");
        }
        if (i > 1) {
            return grammarAtom.next.look(i - 1);
        }
        Lookahead lookaheadM109of = Lookahead.m109of(grammarAtom.getType());
        if (grammarAtom.not) {
            lookaheadM109of.fset.notInPlace(4, this.grammar.tokenManager.maxTokenType());
            removeCompetingPredictionSets(lookaheadM109of.fset, grammarAtom);
        }
        return lookaheadM109of;
    }

    @Override // antlr.LLkGrammarAnalyzer
    public Lookahead look(int i, OneOrMoreBlock oneOrMoreBlock) {
        if (this.DEBUG_ANALYZER) {
            System.out.println("look+" + i + "," + oneOrMoreBlock + ")");
        }
        return look(i, (AlternativeBlock) oneOrMoreBlock);
    }

    @Override // antlr.LLkGrammarAnalyzer
    public Lookahead look(int i, RuleBlock ruleBlock) {
        if (this.DEBUG_ANALYZER) {
            System.out.println("lookRuleBlk(" + i + "," + ruleBlock + ")");
        }
        return look(i, (AlternativeBlock) ruleBlock);
    }

    @Override // antlr.LLkGrammarAnalyzer
    public Lookahead look(int i, RuleEndElement ruleEndElement) {
        if (this.DEBUG_ANALYZER) {
            System.out.println("lookRuleBlockEnd(" + i + "); noFOLLOW=" + ruleEndElement.noFOLLOW + "; lock is " + ruleEndElement.lock[i]);
        }
        if (!ruleEndElement.noFOLLOW) {
            return FOLLOW(i, ruleEndElement);
        }
        Lookahead lookahead = new Lookahead();
        lookahead.setEpsilon();
        lookahead.epsilonDepth = BitSet.m114of(i);
        return lookahead;
    }

    @Override // antlr.LLkGrammarAnalyzer
    public Lookahead look(int i, RuleRefElement ruleRefElement) {
        if (this.DEBUG_ANALYZER) {
            System.out.println("lookRuleRef(" + i + "," + ruleRefElement + ")");
        }
        RuleSymbol ruleSymbol = (RuleSymbol) this.grammar.getSymbol(ruleRefElement.targetRule);
        if (ruleSymbol == null || !ruleSymbol.defined) {
            Tool tool = this.tool;
            StringBuilder sbM5a = C0000a.m5a("no definition of rule ");
            sbM5a.append(ruleRefElement.targetRule);
            tool.error(sbM5a.toString(), this.grammar.getFilename(), ruleRefElement.getLine(), ruleRefElement.getColumn());
            return new Lookahead();
        }
        RuleEndElement ruleEndElement = ruleSymbol.getBlock().endNode;
        boolean z = ruleEndElement.noFOLLOW;
        ruleEndElement.noFOLLOW = true;
        Lookahead lookaheadLook = look(i, ruleRefElement.targetRule);
        if (this.DEBUG_ANALYZER) {
            PrintStream printStream = System.out;
            StringBuilder sbM5a2 = C0000a.m5a("back from rule ref to ");
            sbM5a2.append(ruleRefElement.targetRule);
            printStream.println(sbM5a2.toString());
        }
        ruleEndElement.noFOLLOW = z;
        if (lookaheadLook.cycle != null) {
            Tool tool2 = this.tool;
            StringBuilder sbM5a3 = C0000a.m5a("infinite recursion to rule ");
            sbM5a3.append(lookaheadLook.cycle);
            sbM5a3.append(" from rule ");
            sbM5a3.append(ruleRefElement.enclosingRuleName);
            tool2.error(sbM5a3.toString(), this.grammar.getFilename(), ruleRefElement.getLine(), ruleRefElement.getColumn());
        }
        if (lookaheadLook.containsEpsilon()) {
            if (this.DEBUG_ANALYZER) {
                PrintStream printStream2 = System.out;
                StringBuilder sbM5a4 = C0000a.m5a("rule ref to ");
                sbM5a4.append(ruleRefElement.targetRule);
                sbM5a4.append(" has eps, depth: ");
                sbM5a4.append(lookaheadLook.epsilonDepth);
                printStream2.println(sbM5a4.toString());
            }
            lookaheadLook.resetEpsilon();
            int[] array = lookaheadLook.epsilonDepth.toArray();
            lookaheadLook.epsilonDepth = null;
            for (int i2 : array) {
                lookaheadLook.combineWith(ruleRefElement.next.look(i - (i - i2)));
            }
        }
        return lookaheadLook;
    }

    @Override // antlr.LLkGrammarAnalyzer
    public Lookahead look(int i, StringLiteralElement stringLiteralElement) {
        if (this.DEBUG_ANALYZER) {
            System.out.println("lookStringLiteral(" + i + "," + stringLiteralElement + ")");
        }
        if (this.lexicalAnalysis) {
            return i > stringLiteralElement.processedAtomText.length() ? stringLiteralElement.next.look(i - stringLiteralElement.processedAtomText.length()) : Lookahead.m109of(stringLiteralElement.processedAtomText.charAt(i - 1));
        }
        if (i > 1) {
            return stringLiteralElement.next.look(i - 1);
        }
        Lookahead lookaheadM109of = Lookahead.m109of(stringLiteralElement.getType());
        if (stringLiteralElement.not) {
            lookaheadM109of.fset.notInPlace(4, this.grammar.tokenManager.maxTokenType());
        }
        return lookaheadM109of;
    }

    @Override // antlr.LLkGrammarAnalyzer
    public Lookahead look(int i, SynPredBlock synPredBlock) {
        if (this.DEBUG_ANALYZER) {
            System.out.println("look=>(" + i + "," + synPredBlock + ")");
        }
        return synPredBlock.next.look(i);
    }

    @Override // antlr.LLkGrammarAnalyzer
    public Lookahead look(int i, TokenRangeElement tokenRangeElement) {
        if (this.DEBUG_ANALYZER) {
            System.out.println("lookTokenRange(" + i + "," + tokenRangeElement + ")");
        }
        if (i > 1) {
            return tokenRangeElement.next.look(i - 1);
        }
        BitSet bitSetM114of = BitSet.m114of(tokenRangeElement.begin);
        for (int i2 = tokenRangeElement.begin + 1; i2 <= tokenRangeElement.end; i2++) {
            bitSetM114of.add(i2);
        }
        return new Lookahead(bitSetM114of);
    }

    @Override // antlr.LLkGrammarAnalyzer
    public Lookahead look(int i, TreeElement treeElement) {
        if (this.DEBUG_ANALYZER) {
            System.out.println("look(" + i + "," + treeElement.root + "[" + treeElement.root.getType() + "])");
        }
        if (i > 1) {
            return treeElement.next.look(i - 1);
        }
        GrammarAtom grammarAtom = treeElement.root;
        if (grammarAtom instanceof WildcardElement) {
            return grammarAtom.look(1);
        }
        Lookahead lookaheadM109of = Lookahead.m109of(grammarAtom.getType());
        if (treeElement.root.not) {
            lookaheadM109of.fset.notInPlace(4, this.grammar.tokenManager.maxTokenType());
        }
        return lookaheadM109of;
    }

    @Override // antlr.LLkGrammarAnalyzer
    public Lookahead look(int i, WildcardElement wildcardElement) {
        BitSet bitSet;
        if (this.DEBUG_ANALYZER) {
            System.out.println("look(" + i + "," + wildcardElement + ")");
        }
        if (i > 1) {
            return wildcardElement.next.look(i - 1);
        }
        if (this.lexicalAnalysis) {
            bitSet = (BitSet) ((LexerGrammar) this.grammar).charVocabulary.clone();
        } else {
            BitSet bitSet2 = new BitSet(1);
            bitSet2.notInPlace(4, this.grammar.tokenManager.maxTokenType());
            if (this.DEBUG_ANALYZER) {
                System.out.println("look(" + i + "," + wildcardElement + ") after not: " + bitSet2);
            }
            bitSet = bitSet2;
        }
        return new Lookahead(bitSet);
    }

    @Override // antlr.LLkGrammarAnalyzer
    public Lookahead look(int i, ZeroOrMoreBlock zeroOrMoreBlock) {
        if (this.DEBUG_ANALYZER) {
            System.out.println("look*(" + i + "," + zeroOrMoreBlock + ")");
        }
        Lookahead lookaheadLook = look(i, (AlternativeBlock) zeroOrMoreBlock);
        lookaheadLook.combineWith(zeroOrMoreBlock.next.look(i));
        return lookaheadLook;
    }

    @Override // antlr.LLkGrammarAnalyzer
    public Lookahead look(int i, String str) {
        if (this.DEBUG_ANALYZER) {
            System.out.println("lookRuleName(" + i + "," + str + ")");
        }
        RuleBlock block = ((RuleSymbol) this.grammar.getSymbol(str)).getBlock();
        boolean[] zArr = block.lock;
        if (zArr[i]) {
            if (this.DEBUG_ANALYZER) {
                PrintStream printStream = System.out;
                StringBuilder sbM5a = C0000a.m5a("infinite recursion to rule ");
                sbM5a.append(block.getRuleName());
                printStream.println(sbM5a.toString());
            }
            return new Lookahead(str);
        }
        if (block.cache[i] != null) {
            if (this.DEBUG_ANALYZER) {
                System.out.println("found depth " + i + " result in FIRST " + str + " cache: " + block.cache[i].toString(",", this.charFormatter, this.grammar));
            }
            return (Lookahead) block.cache[i].clone();
        }
        zArr[i] = true;
        Lookahead lookaheadLook = look(i, block);
        block.lock[i] = false;
        block.cache[i] = (Lookahead) lookaheadLook.clone();
        if (this.DEBUG_ANALYZER) {
            System.out.println("saving depth " + i + " result in FIRST " + str + " cache: " + block.cache[i].toString(",", this.charFormatter, this.grammar));
        }
        return lookaheadLook;
    }

    @Override // antlr.LLkGrammarAnalyzer
    public void setGrammar(Grammar grammar) {
        if (this.grammar != null) {
            reset();
        }
        this.grammar = grammar;
        Grammar grammar2 = this.grammar;
        this.lexicalAnalysis = grammar2 instanceof LexerGrammar;
        this.DEBUG_ANALYZER = grammar2.analyzerDebug;
    }

    @Override // antlr.LLkGrammarAnalyzer
    public boolean subruleCanBeInverted(AlternativeBlock alternativeBlock, boolean z) {
        if ((alternativeBlock instanceof ZeroOrMoreBlock) || (alternativeBlock instanceof OneOrMoreBlock) || (alternativeBlock instanceof SynPredBlock) || alternativeBlock.alternatives.size() == 0) {
            return false;
        }
        for (int i = 0; i < alternativeBlock.alternatives.size(); i++) {
            Alternative alternativeAt = alternativeBlock.getAlternativeAt(i);
            if (alternativeAt.synPred == null && alternativeAt.semPred == null && alternativeAt.exceptionSpec == null) {
                AlternativeElement alternativeElement = alternativeAt.head;
                if (((alternativeElement instanceof CharLiteralElement) || (alternativeElement instanceof TokenRefElement) || (alternativeElement instanceof CharRangeElement) || (alternativeElement instanceof TokenRangeElement) || ((alternativeElement instanceof StringLiteralElement) && !z)) && (alternativeElement.next instanceof BlockEndElement) && alternativeElement.getAutoGenType() == 1) {
                }
            }
            return false;
        }
        return true;
    }
}
