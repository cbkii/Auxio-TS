package antlr.debug;

import antlr.collections.impl.BitSet;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/* JADX INFO: loaded from: classes3.dex */
public class ParserEventSupport {
    public static final int CONSUME = 0;
    public static final int DONE_PARSING = 15;
    public static final int ENTER_RULE = 1;
    public static final int EXIT_RULE = 2;

    /* JADX INFO: renamed from: LA */
    public static final int f324LA = 3;
    public static final int MATCH = 4;
    public static final int MATCH_NOT = 5;
    public static final int MISMATCH = 6;
    public static final int MISMATCH_NOT = 7;
    public static final int NEW_LINE = 14;
    public static final int REPORT_ERROR = 8;
    public static final int REPORT_WARNING = 9;
    public static final int SEMPRED = 10;
    public static final int SYNPRED_FAILED = 11;
    public static final int SYNPRED_STARTED = 12;
    public static final int SYNPRED_SUCCEEDED = 13;
    public ParserController controller;
    public Hashtable doneListeners;
    public ParserMatchEvent matchEvent;
    public Vector matchListeners;
    public MessageEvent messageEvent;
    public Vector messageListeners;
    public NewLineEvent newLineEvent;
    public Vector newLineListeners;
    public int ruleDepth = 0;
    public SemanticPredicateEvent semPredEvent;
    public Vector semPredListeners;
    public Object source;
    public SyntacticPredicateEvent synPredEvent;
    public Vector synPredListeners;
    public ParserTokenEvent tokenEvent;
    public Vector tokenListeners;
    public TraceEvent traceEvent;
    public Vector traceListeners;

    public ParserEventSupport(Object obj) {
        this.matchEvent = new ParserMatchEvent(obj);
        this.messageEvent = new MessageEvent(obj);
        this.tokenEvent = new ParserTokenEvent(obj);
        this.traceEvent = new TraceEvent(obj);
        this.semPredEvent = new SemanticPredicateEvent(obj);
        this.synPredEvent = new SyntacticPredicateEvent(obj);
        this.newLineEvent = new NewLineEvent(obj);
        this.source = obj;
    }

    public void addDoneListener(ListenerBase listenerBase) {
        if (this.doneListeners == null) {
            this.doneListeners = new Hashtable();
        }
        Integer num = (Integer) this.doneListeners.get(listenerBase);
        this.doneListeners.put(listenerBase, new Integer(num != null ? 1 + num.intValue() : 1));
    }

    public void addMessageListener(MessageListener messageListener) {
        if (this.messageListeners == null) {
            this.messageListeners = new Vector();
        }
        this.messageListeners.addElement(messageListener);
        addDoneListener(messageListener);
    }

    public void addNewLineListener(NewLineListener newLineListener) {
        if (this.newLineListeners == null) {
            this.newLineListeners = new Vector();
        }
        this.newLineListeners.addElement(newLineListener);
        addDoneListener(newLineListener);
    }

    public void addParserListener(ParserListener parserListener) {
        if (parserListener instanceof ParserController) {
            ParserController parserController = (ParserController) parserListener;
            parserController.setParserEventSupport(this);
            this.controller = parserController;
        }
        addParserMatchListener(parserListener);
        addParserTokenListener(parserListener);
        addMessageListener(parserListener);
        addTraceListener(parserListener);
        addSemanticPredicateListener(parserListener);
        addSyntacticPredicateListener(parserListener);
    }

    public void addParserMatchListener(ParserMatchListener parserMatchListener) {
        if (this.matchListeners == null) {
            this.matchListeners = new Vector();
        }
        this.matchListeners.addElement(parserMatchListener);
        addDoneListener(parserMatchListener);
    }

    public void addParserTokenListener(ParserTokenListener parserTokenListener) {
        if (this.tokenListeners == null) {
            this.tokenListeners = new Vector();
        }
        this.tokenListeners.addElement(parserTokenListener);
        addDoneListener(parserTokenListener);
    }

    public void addSemanticPredicateListener(SemanticPredicateListener semanticPredicateListener) {
        if (this.semPredListeners == null) {
            this.semPredListeners = new Vector();
        }
        this.semPredListeners.addElement(semanticPredicateListener);
        addDoneListener(semanticPredicateListener);
    }

    public void addSyntacticPredicateListener(SyntacticPredicateListener syntacticPredicateListener) {
        if (this.synPredListeners == null) {
            this.synPredListeners = new Vector();
        }
        this.synPredListeners.addElement(syntacticPredicateListener);
        addDoneListener(syntacticPredicateListener);
    }

    public void addTraceListener(TraceListener traceListener) {
        if (this.traceListeners == null) {
            this.traceListeners = new Vector();
        }
        this.traceListeners.addElement(traceListener);
        addDoneListener(traceListener);
    }

    public void fireConsume(int i) {
        this.tokenEvent.setValues(ParserTokenEvent.CONSUME, 1, i);
        fireEvents(0, this.tokenListeners);
    }

    public void fireDoneParsing() {
        this.traceEvent.setValues(TraceEvent.DONE_PARSING, 0, 0, 0);
        synchronized (this) {
            if (this.doneListeners == null) {
                return;
            }
            Hashtable hashtable = (Hashtable) this.doneListeners.clone();
            if (hashtable != null) {
                Enumeration enumerationKeys = hashtable.keys();
                while (enumerationKeys.hasMoreElements()) {
                    fireEvent(15, (ListenerBase) enumerationKeys.nextElement());
                }
            }
            ParserController parserController = this.controller;
            if (parserController != null) {
                parserController.checkBreak();
            }
        }
    }

    public void fireEnterRule(int i, int i2, int i3) {
        this.ruleDepth++;
        this.traceEvent.setValues(TraceEvent.ENTER, i, i2, i3);
        fireEvents(1, this.traceListeners);
    }

    public void fireEvent(int i, ListenerBase listenerBase) {
        switch (i) {
            case 0:
                ((ParserTokenListener) listenerBase).parserConsume(this.tokenEvent);
                return;
            case 1:
                ((TraceListener) listenerBase).enterRule(this.traceEvent);
                return;
            case 2:
                ((TraceListener) listenerBase).exitRule(this.traceEvent);
                return;
            case 3:
                ((ParserTokenListener) listenerBase).parserLA(this.tokenEvent);
                return;
            case 4:
                ((ParserMatchListener) listenerBase).parserMatch(this.matchEvent);
                return;
            case 5:
                ((ParserMatchListener) listenerBase).parserMatchNot(this.matchEvent);
                return;
            case 6:
                ((ParserMatchListener) listenerBase).parserMismatch(this.matchEvent);
                return;
            case 7:
                ((ParserMatchListener) listenerBase).parserMismatchNot(this.matchEvent);
                return;
            case 8:
                ((MessageListener) listenerBase).reportError(this.messageEvent);
                return;
            case 9:
                ((MessageListener) listenerBase).reportWarning(this.messageEvent);
                return;
            case 10:
                ((SemanticPredicateListener) listenerBase).semanticPredicateEvaluated(this.semPredEvent);
                return;
            case 11:
                ((SyntacticPredicateListener) listenerBase).syntacticPredicateFailed(this.synPredEvent);
                return;
            case 12:
                ((SyntacticPredicateListener) listenerBase).syntacticPredicateStarted(this.synPredEvent);
                return;
            case 13:
                ((SyntacticPredicateListener) listenerBase).syntacticPredicateSucceeded(this.synPredEvent);
                return;
            case 14:
                ((NewLineListener) listenerBase).hitNewLine(this.newLineEvent);
                return;
            case 15:
                listenerBase.doneParsing(this.traceEvent);
                return;
            default:
                throw new IllegalArgumentException("bad type " + i + " for fireEvent()");
        }
    }

    public void fireEvents(int i, Vector vector) {
        if (vector != null) {
            for (int i2 = 0; i2 < vector.size(); i2++) {
                fireEvent(i, (ListenerBase) vector.elementAt(i2));
            }
        }
        ParserController parserController = this.controller;
        if (parserController != null) {
            parserController.checkBreak();
        }
    }

    public void fireExitRule(int i, int i2, int i3) {
        this.traceEvent.setValues(TraceEvent.EXIT, i, i2, i3);
        fireEvents(2, this.traceListeners);
        this.ruleDepth--;
        if (this.ruleDepth == 0) {
            fireDoneParsing();
        }
    }

    public void fireLA(int i, int i2) {
        this.tokenEvent.setValues(ParserTokenEvent.f325LA, i, i2);
        fireEvents(3, this.tokenListeners);
    }

    public void fireMatch(char c2, int i) {
        this.matchEvent.setValues(ParserMatchEvent.CHAR, c2, new Character(c2), null, i, false, true);
        fireEvents(4, this.matchListeners);
    }

    public void fireMatch(char c2, BitSet bitSet, int i) {
        this.matchEvent.setValues(ParserMatchEvent.CHAR_BITSET, c2, bitSet, null, i, false, true);
        fireEvents(4, this.matchListeners);
    }

    public void fireMatch(char c2, String str, int i) {
        this.matchEvent.setValues(ParserMatchEvent.CHAR_RANGE, c2, str, null, i, false, true);
        fireEvents(4, this.matchListeners);
    }

    public void fireMatch(int i, BitSet bitSet, String str, int i2) {
        this.matchEvent.setValues(ParserMatchEvent.BITSET, i, bitSet, str, i2, false, true);
        fireEvents(4, this.matchListeners);
    }

    public void fireMatch(int i, String str, int i2) {
        this.matchEvent.setValues(ParserMatchEvent.TOKEN, i, new Integer(i), str, i2, false, true);
        fireEvents(4, this.matchListeners);
    }

    public void fireMatch(String str, int i) {
        this.matchEvent.setValues(ParserMatchEvent.STRING, 0, str, null, i, false, true);
        fireEvents(4, this.matchListeners);
    }

    public void fireMatchNot(char c2, char c3, int i) {
        this.matchEvent.setValues(ParserMatchEvent.CHAR, c2, new Character(c3), null, i, true, true);
        fireEvents(5, this.matchListeners);
    }

    public void fireMatchNot(int i, int i2, String str, int i3) {
        this.matchEvent.setValues(ParserMatchEvent.TOKEN, i, new Integer(i2), str, i3, true, true);
        fireEvents(5, this.matchListeners);
    }

    public void fireMismatch(char c2, char c3, int i) {
        this.matchEvent.setValues(ParserMatchEvent.CHAR, c2, new Character(c3), null, i, false, false);
        fireEvents(6, this.matchListeners);
    }

    public void fireMismatch(char c2, BitSet bitSet, int i) {
        this.matchEvent.setValues(ParserMatchEvent.CHAR_BITSET, c2, bitSet, null, i, false, true);
        fireEvents(6, this.matchListeners);
    }

    public void fireMismatch(char c2, String str, int i) {
        this.matchEvent.setValues(ParserMatchEvent.CHAR_RANGE, c2, str, null, i, false, true);
        fireEvents(6, this.matchListeners);
    }

    public void fireMismatch(int i, int i2, String str, int i3) {
        this.matchEvent.setValues(ParserMatchEvent.TOKEN, i, new Integer(i2), str, i3, false, false);
        fireEvents(6, this.matchListeners);
    }

    public void fireMismatch(int i, BitSet bitSet, String str, int i2) {
        this.matchEvent.setValues(ParserMatchEvent.BITSET, i, bitSet, str, i2, false, true);
        fireEvents(6, this.matchListeners);
    }

    public void fireMismatch(String str, String str2, int i) {
        this.matchEvent.setValues(ParserMatchEvent.STRING, 0, str2, str, i, false, true);
        fireEvents(6, this.matchListeners);
    }

    public void fireMismatchNot(char c2, char c3, int i) {
        this.matchEvent.setValues(ParserMatchEvent.CHAR, c2, new Character(c3), null, i, true, true);
        fireEvents(7, this.matchListeners);
    }

    public void fireMismatchNot(int i, int i2, String str, int i3) {
        this.matchEvent.setValues(ParserMatchEvent.TOKEN, i, new Integer(i2), str, i3, true, true);
        fireEvents(7, this.matchListeners);
    }

    public void fireNewLine(int i) {
        this.newLineEvent.setValues(i);
        fireEvents(14, this.newLineListeners);
    }

    public void fireReportError(Exception exc) {
        this.messageEvent.setValues(MessageEvent.ERROR, exc.toString());
        fireEvents(8, this.messageListeners);
    }

    public void fireReportError(String str) {
        this.messageEvent.setValues(MessageEvent.ERROR, str);
        fireEvents(8, this.messageListeners);
    }

    public void fireReportWarning(String str) {
        this.messageEvent.setValues(MessageEvent.WARNING, str);
        fireEvents(9, this.messageListeners);
    }

    public boolean fireSemanticPredicateEvaluated(int i, int i2, boolean z, int i3) {
        this.semPredEvent.setValues(i, i2, z, i3);
        fireEvents(10, this.semPredListeners);
        return z;
    }

    public void fireSyntacticPredicateFailed(int i) {
        this.synPredEvent.setValues(0, i);
        fireEvents(11, this.synPredListeners);
    }

    public void fireSyntacticPredicateStarted(int i) {
        this.synPredEvent.setValues(0, i);
        fireEvents(12, this.synPredListeners);
    }

    public void fireSyntacticPredicateSucceeded(int i) {
        this.synPredEvent.setValues(0, i);
        fireEvents(13, this.synPredListeners);
    }

    public void refresh(Vector vector) {
        Vector vector2;
        synchronized (vector) {
            vector2 = (Vector) vector.clone();
        }
        if (vector2 != null) {
            for (int i = 0; i < vector2.size(); i++) {
                ((ListenerBase) vector2.elementAt(i)).refresh();
            }
        }
    }

    public void refreshListeners() {
        refresh(this.matchListeners);
        refresh(this.messageListeners);
        refresh(this.tokenListeners);
        refresh(this.traceListeners);
        refresh(this.semPredListeners);
        refresh(this.synPredListeners);
    }

    public void removeDoneListener(ListenerBase listenerBase) {
        Hashtable hashtable = this.doneListeners;
        if (hashtable == null) {
            return;
        }
        int iIntValue = ((Integer) hashtable.get(listenerBase)) != null ? r0.intValue() - 1 : 0;
        Hashtable hashtable2 = this.doneListeners;
        if (iIntValue == 0) {
            hashtable2.remove(listenerBase);
        } else {
            hashtable2.put(listenerBase, new Integer(iIntValue));
        }
    }

    public void removeMessageListener(MessageListener messageListener) {
        Vector vector = this.messageListeners;
        if (vector != null) {
            vector.removeElement(messageListener);
        }
        removeDoneListener(messageListener);
    }

    public void removeNewLineListener(NewLineListener newLineListener) {
        Vector vector = this.newLineListeners;
        if (vector != null) {
            vector.removeElement(newLineListener);
        }
        removeDoneListener(newLineListener);
    }

    public void removeParserListener(ParserListener parserListener) {
        removeParserMatchListener(parserListener);
        removeMessageListener(parserListener);
        removeParserTokenListener(parserListener);
        removeTraceListener(parserListener);
        removeSemanticPredicateListener(parserListener);
        removeSyntacticPredicateListener(parserListener);
    }

    public void removeParserMatchListener(ParserMatchListener parserMatchListener) {
        Vector vector = this.matchListeners;
        if (vector != null) {
            vector.removeElement(parserMatchListener);
        }
        removeDoneListener(parserMatchListener);
    }

    public void removeParserTokenListener(ParserTokenListener parserTokenListener) {
        Vector vector = this.tokenListeners;
        if (vector != null) {
            vector.removeElement(parserTokenListener);
        }
        removeDoneListener(parserTokenListener);
    }

    public void removeSemanticPredicateListener(SemanticPredicateListener semanticPredicateListener) {
        Vector vector = this.semPredListeners;
        if (vector != null) {
            vector.removeElement(semanticPredicateListener);
        }
        removeDoneListener(semanticPredicateListener);
    }

    public void removeSyntacticPredicateListener(SyntacticPredicateListener syntacticPredicateListener) {
        Vector vector = this.synPredListeners;
        if (vector != null) {
            vector.removeElement(syntacticPredicateListener);
        }
        removeDoneListener(syntacticPredicateListener);
    }

    public void removeTraceListener(TraceListener traceListener) {
        Vector vector = this.traceListeners;
        if (vector != null) {
            vector.removeElement(traceListener);
        }
        removeDoneListener(traceListener);
    }
}
