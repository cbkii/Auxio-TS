.class public Lantlr/debug/ParserEventSupport;
.super Ljava/lang/Object;
.source ""


# static fields
.field public static final CONSUME:I = 0x0

.field public static final DONE_PARSING:I = 0xf

.field public static final ENTER_RULE:I = 0x1

.field public static final EXIT_RULE:I = 0x2

.field public static final LA:I = 0x3

.field public static final MATCH:I = 0x4

.field public static final MATCH_NOT:I = 0x5

.field public static final MISMATCH:I = 0x6

.field public static final MISMATCH_NOT:I = 0x7

.field public static final NEW_LINE:I = 0xe

.field public static final REPORT_ERROR:I = 0x8

.field public static final REPORT_WARNING:I = 0x9

.field public static final SEMPRED:I = 0xa

.field public static final SYNPRED_FAILED:I = 0xb

.field public static final SYNPRED_STARTED:I = 0xc

.field public static final SYNPRED_SUCCEEDED:I = 0xd


# instance fields
.field public controller:Lantlr/debug/ParserController;

.field public doneListeners:Ljava/util/Hashtable;

.field public matchEvent:Lantlr/debug/ParserMatchEvent;

.field public matchListeners:Ljava/util/Vector;

.field public messageEvent:Lantlr/debug/MessageEvent;

.field public messageListeners:Ljava/util/Vector;

.field public newLineEvent:Lantlr/debug/NewLineEvent;

.field public newLineListeners:Ljava/util/Vector;

.field public ruleDepth:I

.field public semPredEvent:Lantlr/debug/SemanticPredicateEvent;

.field public semPredListeners:Ljava/util/Vector;

.field public source:Ljava/lang/Object;

.field public synPredEvent:Lantlr/debug/SyntacticPredicateEvent;

.field public synPredListeners:Ljava/util/Vector;

.field public tokenEvent:Lantlr/debug/ParserTokenEvent;

.field public tokenListeners:Ljava/util/Vector;

.field public traceEvent:Lantlr/debug/TraceEvent;

.field public traceListeners:Ljava/util/Vector;


# direct methods
.method public constructor <init>(Ljava/lang/Object;)V
    .locals 1

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    const/4 v0, 0x0

    iput v0, p0, Lantlr/debug/ParserEventSupport;->ruleDepth:I

    new-instance v0, Lantlr/debug/ParserMatchEvent;

    invoke-direct {v0, p1}, Lantlr/debug/ParserMatchEvent;-><init>(Ljava/lang/Object;)V

    iput-object v0, p0, Lantlr/debug/ParserEventSupport;->matchEvent:Lantlr/debug/ParserMatchEvent;

    new-instance v0, Lantlr/debug/MessageEvent;

    invoke-direct {v0, p1}, Lantlr/debug/MessageEvent;-><init>(Ljava/lang/Object;)V

    iput-object v0, p0, Lantlr/debug/ParserEventSupport;->messageEvent:Lantlr/debug/MessageEvent;

    new-instance v0, Lantlr/debug/ParserTokenEvent;

    invoke-direct {v0, p1}, Lantlr/debug/ParserTokenEvent;-><init>(Ljava/lang/Object;)V

    iput-object v0, p0, Lantlr/debug/ParserEventSupport;->tokenEvent:Lantlr/debug/ParserTokenEvent;

    new-instance v0, Lantlr/debug/TraceEvent;

    invoke-direct {v0, p1}, Lantlr/debug/TraceEvent;-><init>(Ljava/lang/Object;)V

    iput-object v0, p0, Lantlr/debug/ParserEventSupport;->traceEvent:Lantlr/debug/TraceEvent;

    new-instance v0, Lantlr/debug/SemanticPredicateEvent;

    invoke-direct {v0, p1}, Lantlr/debug/SemanticPredicateEvent;-><init>(Ljava/lang/Object;)V

    iput-object v0, p0, Lantlr/debug/ParserEventSupport;->semPredEvent:Lantlr/debug/SemanticPredicateEvent;

    new-instance v0, Lantlr/debug/SyntacticPredicateEvent;

    invoke-direct {v0, p1}, Lantlr/debug/SyntacticPredicateEvent;-><init>(Ljava/lang/Object;)V

    iput-object v0, p0, Lantlr/debug/ParserEventSupport;->synPredEvent:Lantlr/debug/SyntacticPredicateEvent;

    new-instance v0, Lantlr/debug/NewLineEvent;

    invoke-direct {v0, p1}, Lantlr/debug/NewLineEvent;-><init>(Ljava/lang/Object;)V

    iput-object v0, p0, Lantlr/debug/ParserEventSupport;->newLineEvent:Lantlr/debug/NewLineEvent;

    iput-object p1, p0, Lantlr/debug/ParserEventSupport;->source:Ljava/lang/Object;

    return-void
.end method


# virtual methods
.method public addDoneListener(Lantlr/debug/ListenerBase;)V
    .locals 2

    iget-object v0, p0, Lantlr/debug/ParserEventSupport;->doneListeners:Ljava/util/Hashtable;

    if-nez v0, :cond_0

    new-instance v0, Ljava/util/Hashtable;

    invoke-direct {v0}, Ljava/util/Hashtable;-><init>()V

    iput-object v0, p0, Lantlr/debug/ParserEventSupport;->doneListeners:Ljava/util/Hashtable;

    :cond_0
    iget-object v0, p0, Lantlr/debug/ParserEventSupport;->doneListeners:Ljava/util/Hashtable;

    invoke-virtual {v0, p1}, Ljava/util/Hashtable;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Ljava/lang/Integer;

    const/4 v1, 0x1

    if-eqz v0, :cond_1

    invoke-virtual {v0}, Ljava/lang/Integer;->intValue()I

    move-result v0

    add-int/2addr v1, v0

    :cond_1
    iget-object p0, p0, Lantlr/debug/ParserEventSupport;->doneListeners:Ljava/util/Hashtable;

    new-instance v0, Ljava/lang/Integer;

    invoke-direct {v0, v1}, Ljava/lang/Integer;-><init>(I)V

    invoke-virtual {p0, p1, v0}, Ljava/util/Hashtable;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    return-void
.end method

.method public addMessageListener(Lantlr/debug/MessageListener;)V
    .locals 1

    iget-object v0, p0, Lantlr/debug/ParserEventSupport;->messageListeners:Ljava/util/Vector;

    if-nez v0, :cond_0

    new-instance v0, Ljava/util/Vector;

    invoke-direct {v0}, Ljava/util/Vector;-><init>()V

    iput-object v0, p0, Lantlr/debug/ParserEventSupport;->messageListeners:Ljava/util/Vector;

    :cond_0
    iget-object v0, p0, Lantlr/debug/ParserEventSupport;->messageListeners:Ljava/util/Vector;

    invoke-virtual {v0, p1}, Ljava/util/Vector;->addElement(Ljava/lang/Object;)V

    invoke-virtual {p0, p1}, Lantlr/debug/ParserEventSupport;->addDoneListener(Lantlr/debug/ListenerBase;)V

    return-void
.end method

.method public addNewLineListener(Lantlr/debug/NewLineListener;)V
    .locals 1

    iget-object v0, p0, Lantlr/debug/ParserEventSupport;->newLineListeners:Ljava/util/Vector;

    if-nez v0, :cond_0

    new-instance v0, Ljava/util/Vector;

    invoke-direct {v0}, Ljava/util/Vector;-><init>()V

    iput-object v0, p0, Lantlr/debug/ParserEventSupport;->newLineListeners:Ljava/util/Vector;

    :cond_0
    iget-object v0, p0, Lantlr/debug/ParserEventSupport;->newLineListeners:Ljava/util/Vector;

    invoke-virtual {v0, p1}, Ljava/util/Vector;->addElement(Ljava/lang/Object;)V

    invoke-virtual {p0, p1}, Lantlr/debug/ParserEventSupport;->addDoneListener(Lantlr/debug/ListenerBase;)V

    return-void
.end method

.method public addParserListener(Lantlr/debug/ParserListener;)V
    .locals 1

    instance-of v0, p1, Lantlr/debug/ParserController;

    if-eqz v0, :cond_0

    move-object v0, p1

    check-cast v0, Lantlr/debug/ParserController;

    invoke-interface {v0, p0}, Lantlr/debug/ParserController;->setParserEventSupport(Lantlr/debug/ParserEventSupport;)V

    iput-object v0, p0, Lantlr/debug/ParserEventSupport;->controller:Lantlr/debug/ParserController;

    :cond_0
    invoke-virtual {p0, p1}, Lantlr/debug/ParserEventSupport;->addParserMatchListener(Lantlr/debug/ParserMatchListener;)V

    invoke-virtual {p0, p1}, Lantlr/debug/ParserEventSupport;->addParserTokenListener(Lantlr/debug/ParserTokenListener;)V

    invoke-virtual {p0, p1}, Lantlr/debug/ParserEventSupport;->addMessageListener(Lantlr/debug/MessageListener;)V

    invoke-virtual {p0, p1}, Lantlr/debug/ParserEventSupport;->addTraceListener(Lantlr/debug/TraceListener;)V

    invoke-virtual {p0, p1}, Lantlr/debug/ParserEventSupport;->addSemanticPredicateListener(Lantlr/debug/SemanticPredicateListener;)V

    invoke-virtual {p0, p1}, Lantlr/debug/ParserEventSupport;->addSyntacticPredicateListener(Lantlr/debug/SyntacticPredicateListener;)V

    return-void
.end method

.method public addParserMatchListener(Lantlr/debug/ParserMatchListener;)V
    .locals 1

    iget-object v0, p0, Lantlr/debug/ParserEventSupport;->matchListeners:Ljava/util/Vector;

    if-nez v0, :cond_0

    new-instance v0, Ljava/util/Vector;

    invoke-direct {v0}, Ljava/util/Vector;-><init>()V

    iput-object v0, p0, Lantlr/debug/ParserEventSupport;->matchListeners:Ljava/util/Vector;

    :cond_0
    iget-object v0, p0, Lantlr/debug/ParserEventSupport;->matchListeners:Ljava/util/Vector;

    invoke-virtual {v0, p1}, Ljava/util/Vector;->addElement(Ljava/lang/Object;)V

    invoke-virtual {p0, p1}, Lantlr/debug/ParserEventSupport;->addDoneListener(Lantlr/debug/ListenerBase;)V

    return-void
.end method

.method public addParserTokenListener(Lantlr/debug/ParserTokenListener;)V
    .locals 1

    iget-object v0, p0, Lantlr/debug/ParserEventSupport;->tokenListeners:Ljava/util/Vector;

    if-nez v0, :cond_0

    new-instance v0, Ljava/util/Vector;

    invoke-direct {v0}, Ljava/util/Vector;-><init>()V

    iput-object v0, p0, Lantlr/debug/ParserEventSupport;->tokenListeners:Ljava/util/Vector;

    :cond_0
    iget-object v0, p0, Lantlr/debug/ParserEventSupport;->tokenListeners:Ljava/util/Vector;

    invoke-virtual {v0, p1}, Ljava/util/Vector;->addElement(Ljava/lang/Object;)V

    invoke-virtual {p0, p1}, Lantlr/debug/ParserEventSupport;->addDoneListener(Lantlr/debug/ListenerBase;)V

    return-void
.end method

.method public addSemanticPredicateListener(Lantlr/debug/SemanticPredicateListener;)V
    .locals 1

    iget-object v0, p0, Lantlr/debug/ParserEventSupport;->semPredListeners:Ljava/util/Vector;

    if-nez v0, :cond_0

    new-instance v0, Ljava/util/Vector;

    invoke-direct {v0}, Ljava/util/Vector;-><init>()V

    iput-object v0, p0, Lantlr/debug/ParserEventSupport;->semPredListeners:Ljava/util/Vector;

    :cond_0
    iget-object v0, p0, Lantlr/debug/ParserEventSupport;->semPredListeners:Ljava/util/Vector;

    invoke-virtual {v0, p1}, Ljava/util/Vector;->addElement(Ljava/lang/Object;)V

    invoke-virtual {p0, p1}, Lantlr/debug/ParserEventSupport;->addDoneListener(Lantlr/debug/ListenerBase;)V

    return-void
.end method

.method public addSyntacticPredicateListener(Lantlr/debug/SyntacticPredicateListener;)V
    .locals 1

    iget-object v0, p0, Lantlr/debug/ParserEventSupport;->synPredListeners:Ljava/util/Vector;

    if-nez v0, :cond_0

    new-instance v0, Ljava/util/Vector;

    invoke-direct {v0}, Ljava/util/Vector;-><init>()V

    iput-object v0, p0, Lantlr/debug/ParserEventSupport;->synPredListeners:Ljava/util/Vector;

    :cond_0
    iget-object v0, p0, Lantlr/debug/ParserEventSupport;->synPredListeners:Ljava/util/Vector;

    invoke-virtual {v0, p1}, Ljava/util/Vector;->addElement(Ljava/lang/Object;)V

    invoke-virtual {p0, p1}, Lantlr/debug/ParserEventSupport;->addDoneListener(Lantlr/debug/ListenerBase;)V

    return-void
.end method

.method public addTraceListener(Lantlr/debug/TraceListener;)V
    .locals 1

    iget-object v0, p0, Lantlr/debug/ParserEventSupport;->traceListeners:Ljava/util/Vector;

    if-nez v0, :cond_0

    new-instance v0, Ljava/util/Vector;

    invoke-direct {v0}, Ljava/util/Vector;-><init>()V

    iput-object v0, p0, Lantlr/debug/ParserEventSupport;->traceListeners:Ljava/util/Vector;

    :cond_0
    iget-object v0, p0, Lantlr/debug/ParserEventSupport;->traceListeners:Ljava/util/Vector;

    invoke-virtual {v0, p1}, Ljava/util/Vector;->addElement(Ljava/lang/Object;)V

    invoke-virtual {p0, p1}, Lantlr/debug/ParserEventSupport;->addDoneListener(Lantlr/debug/ListenerBase;)V

    return-void
.end method

.method public fireConsume(I)V
    .locals 3

    iget-object v0, p0, Lantlr/debug/ParserEventSupport;->tokenEvent:Lantlr/debug/ParserTokenEvent;

    sget v1, Lantlr/debug/ParserTokenEvent;->CONSUME:I

    const/4 v2, 0x1

    invoke-virtual {v0, v1, v2, p1}, Lantlr/debug/ParserTokenEvent;->setValues(III)V

    iget-object p1, p0, Lantlr/debug/ParserEventSupport;->tokenListeners:Ljava/util/Vector;

    const/4 v0, 0x0

    invoke-virtual {p0, v0, p1}, Lantlr/debug/ParserEventSupport;->fireEvents(ILjava/util/Vector;)V

    return-void
.end method

.method public fireDoneParsing()V
    .locals 3

    iget-object v0, p0, Lantlr/debug/ParserEventSupport;->traceEvent:Lantlr/debug/TraceEvent;

    sget v1, Lantlr/debug/TraceEvent;->DONE_PARSING:I

    const/4 v2, 0x0

    invoke-virtual {v0, v1, v2, v2, v2}, Lantlr/debug/TraceEvent;->setValues(IIII)V

    monitor-enter p0

    :try_start_0
    iget-object v0, p0, Lantlr/debug/ParserEventSupport;->doneListeners:Ljava/util/Hashtable;

    if-nez v0, :cond_0

    monitor-exit p0

    return-void

    :cond_0
    iget-object v0, p0, Lantlr/debug/ParserEventSupport;->doneListeners:Ljava/util/Hashtable;

    invoke-virtual {v0}, Ljava/util/Hashtable;->clone()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Ljava/util/Hashtable;

    monitor-exit p0
    :try_end_0
    .catchall {:try_start_0 .. :try_end_0} :catchall_0

    if-eqz v0, :cond_1

    invoke-virtual {v0}, Ljava/util/Hashtable;->keys()Ljava/util/Enumeration;

    move-result-object v0

    :goto_0
    invoke-interface {v0}, Ljava/util/Enumeration;->hasMoreElements()Z

    move-result v1

    if-eqz v1, :cond_1

    invoke-interface {v0}, Ljava/util/Enumeration;->nextElement()Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Lantlr/debug/ListenerBase;

    const/16 v2, 0xf

    invoke-virtual {p0, v2, v1}, Lantlr/debug/ParserEventSupport;->fireEvent(ILantlr/debug/ListenerBase;)V

    goto :goto_0

    :cond_1
    iget-object p0, p0, Lantlr/debug/ParserEventSupport;->controller:Lantlr/debug/ParserController;

    if-eqz p0, :cond_2

    invoke-interface {p0}, Lantlr/debug/ParserController;->checkBreak()V

    :cond_2
    return-void

    :catchall_0
    move-exception v0

    :try_start_1
    monitor-exit p0
    :try_end_1
    .catchall {:try_start_1 .. :try_end_1} :catchall_0

    throw v0
.end method

.method public fireEnterRule(III)V
    .locals 3

    iget v0, p0, Lantlr/debug/ParserEventSupport;->ruleDepth:I

    const/4 v1, 0x1

    add-int/2addr v0, v1

    iput v0, p0, Lantlr/debug/ParserEventSupport;->ruleDepth:I

    iget-object v0, p0, Lantlr/debug/ParserEventSupport;->traceEvent:Lantlr/debug/TraceEvent;

    sget v2, Lantlr/debug/TraceEvent;->ENTER:I

    invoke-virtual {v0, v2, p1, p2, p3}, Lantlr/debug/TraceEvent;->setValues(IIII)V

    iget-object p1, p0, Lantlr/debug/ParserEventSupport;->traceListeners:Ljava/util/Vector;

    invoke-virtual {p0, v1, p1}, Lantlr/debug/ParserEventSupport;->fireEvents(ILjava/util/Vector;)V

    return-void
.end method

.method public fireEvent(ILantlr/debug/ListenerBase;)V
    .locals 1

    packed-switch p1, :pswitch_data_0

    new-instance p0, Ljava/lang/IllegalArgumentException;

    new-instance p2, Ljava/lang/StringBuilder;

    invoke-direct {p2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v0, "bad type "

    invoke-virtual {p2, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p2, p1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string p1, " for fireEvent()"

    invoke-virtual {p2, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-direct {p0, p1}, Ljava/lang/IllegalArgumentException;-><init>(Ljava/lang/String;)V

    throw p0

    :pswitch_0
    iget-object p0, p0, Lantlr/debug/ParserEventSupport;->traceEvent:Lantlr/debug/TraceEvent;

    invoke-interface {p2, p0}, Lantlr/debug/ListenerBase;->doneParsing(Lantlr/debug/TraceEvent;)V

    goto/16 :goto_0

    :pswitch_1
    check-cast p2, Lantlr/debug/NewLineListener;

    iget-object p0, p0, Lantlr/debug/ParserEventSupport;->newLineEvent:Lantlr/debug/NewLineEvent;

    invoke-interface {p2, p0}, Lantlr/debug/NewLineListener;->hitNewLine(Lantlr/debug/NewLineEvent;)V

    goto/16 :goto_0

    :pswitch_2
    check-cast p2, Lantlr/debug/SyntacticPredicateListener;

    iget-object p0, p0, Lantlr/debug/ParserEventSupport;->synPredEvent:Lantlr/debug/SyntacticPredicateEvent;

    invoke-interface {p2, p0}, Lantlr/debug/SyntacticPredicateListener;->syntacticPredicateSucceeded(Lantlr/debug/SyntacticPredicateEvent;)V

    goto/16 :goto_0

    :pswitch_3
    check-cast p2, Lantlr/debug/SyntacticPredicateListener;

    iget-object p0, p0, Lantlr/debug/ParserEventSupport;->synPredEvent:Lantlr/debug/SyntacticPredicateEvent;

    invoke-interface {p2, p0}, Lantlr/debug/SyntacticPredicateListener;->syntacticPredicateStarted(Lantlr/debug/SyntacticPredicateEvent;)V

    goto :goto_0

    :pswitch_4
    check-cast p2, Lantlr/debug/SyntacticPredicateListener;

    iget-object p0, p0, Lantlr/debug/ParserEventSupport;->synPredEvent:Lantlr/debug/SyntacticPredicateEvent;

    invoke-interface {p2, p0}, Lantlr/debug/SyntacticPredicateListener;->syntacticPredicateFailed(Lantlr/debug/SyntacticPredicateEvent;)V

    goto :goto_0

    :pswitch_5
    check-cast p2, Lantlr/debug/SemanticPredicateListener;

    iget-object p0, p0, Lantlr/debug/ParserEventSupport;->semPredEvent:Lantlr/debug/SemanticPredicateEvent;

    invoke-interface {p2, p0}, Lantlr/debug/SemanticPredicateListener;->semanticPredicateEvaluated(Lantlr/debug/SemanticPredicateEvent;)V

    goto :goto_0

    :pswitch_6
    check-cast p2, Lantlr/debug/MessageListener;

    iget-object p0, p0, Lantlr/debug/ParserEventSupport;->messageEvent:Lantlr/debug/MessageEvent;

    invoke-interface {p2, p0}, Lantlr/debug/MessageListener;->reportWarning(Lantlr/debug/MessageEvent;)V

    goto :goto_0

    :pswitch_7
    check-cast p2, Lantlr/debug/MessageListener;

    iget-object p0, p0, Lantlr/debug/ParserEventSupport;->messageEvent:Lantlr/debug/MessageEvent;

    invoke-interface {p2, p0}, Lantlr/debug/MessageListener;->reportError(Lantlr/debug/MessageEvent;)V

    goto :goto_0

    :pswitch_8
    check-cast p2, Lantlr/debug/ParserMatchListener;

    iget-object p0, p0, Lantlr/debug/ParserEventSupport;->matchEvent:Lantlr/debug/ParserMatchEvent;

    invoke-interface {p2, p0}, Lantlr/debug/ParserMatchListener;->parserMismatchNot(Lantlr/debug/ParserMatchEvent;)V

    goto :goto_0

    :pswitch_9
    check-cast p2, Lantlr/debug/ParserMatchListener;

    iget-object p0, p0, Lantlr/debug/ParserEventSupport;->matchEvent:Lantlr/debug/ParserMatchEvent;

    invoke-interface {p2, p0}, Lantlr/debug/ParserMatchListener;->parserMismatch(Lantlr/debug/ParserMatchEvent;)V

    goto :goto_0

    :pswitch_a
    check-cast p2, Lantlr/debug/ParserMatchListener;

    iget-object p0, p0, Lantlr/debug/ParserEventSupport;->matchEvent:Lantlr/debug/ParserMatchEvent;

    invoke-interface {p2, p0}, Lantlr/debug/ParserMatchListener;->parserMatchNot(Lantlr/debug/ParserMatchEvent;)V

    goto :goto_0

    :pswitch_b
    check-cast p2, Lantlr/debug/ParserMatchListener;

    iget-object p0, p0, Lantlr/debug/ParserEventSupport;->matchEvent:Lantlr/debug/ParserMatchEvent;

    invoke-interface {p2, p0}, Lantlr/debug/ParserMatchListener;->parserMatch(Lantlr/debug/ParserMatchEvent;)V

    goto :goto_0

    :pswitch_c
    check-cast p2, Lantlr/debug/ParserTokenListener;

    iget-object p0, p0, Lantlr/debug/ParserEventSupport;->tokenEvent:Lantlr/debug/ParserTokenEvent;

    invoke-interface {p2, p0}, Lantlr/debug/ParserTokenListener;->parserLA(Lantlr/debug/ParserTokenEvent;)V

    goto :goto_0

    :pswitch_d
    check-cast p2, Lantlr/debug/TraceListener;

    iget-object p0, p0, Lantlr/debug/ParserEventSupport;->traceEvent:Lantlr/debug/TraceEvent;

    invoke-interface {p2, p0}, Lantlr/debug/TraceListener;->exitRule(Lantlr/debug/TraceEvent;)V

    goto :goto_0

    :pswitch_e
    check-cast p2, Lantlr/debug/TraceListener;

    iget-object p0, p0, Lantlr/debug/ParserEventSupport;->traceEvent:Lantlr/debug/TraceEvent;

    invoke-interface {p2, p0}, Lantlr/debug/TraceListener;->enterRule(Lantlr/debug/TraceEvent;)V

    goto :goto_0

    :pswitch_f
    check-cast p2, Lantlr/debug/ParserTokenListener;

    iget-object p0, p0, Lantlr/debug/ParserEventSupport;->tokenEvent:Lantlr/debug/ParserTokenEvent;

    invoke-interface {p2, p0}, Lantlr/debug/ParserTokenListener;->parserConsume(Lantlr/debug/ParserTokenEvent;)V

    :goto_0
    return-void

    :pswitch_data_0
    .packed-switch 0x0
        :pswitch_f
        :pswitch_e
        :pswitch_d
        :pswitch_c
        :pswitch_b
        :pswitch_a
        :pswitch_9
        :pswitch_8
        :pswitch_7
        :pswitch_6
        :pswitch_5
        :pswitch_4
        :pswitch_3
        :pswitch_2
        :pswitch_1
        :pswitch_0
    .end packed-switch
.end method

.method public fireEvents(ILjava/util/Vector;)V
    .locals 2

    if-eqz p2, :cond_0

    const/4 v0, 0x0

    :goto_0
    invoke-virtual {p2}, Ljava/util/Vector;->size()I

    move-result v1

    if-ge v0, v1, :cond_0

    invoke-virtual {p2, v0}, Ljava/util/Vector;->elementAt(I)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Lantlr/debug/ListenerBase;

    invoke-virtual {p0, p1, v1}, Lantlr/debug/ParserEventSupport;->fireEvent(ILantlr/debug/ListenerBase;)V

    add-int/lit8 v0, v0, 0x1

    goto :goto_0

    :cond_0
    iget-object p0, p0, Lantlr/debug/ParserEventSupport;->controller:Lantlr/debug/ParserController;

    if-eqz p0, :cond_1

    invoke-interface {p0}, Lantlr/debug/ParserController;->checkBreak()V

    :cond_1
    return-void
.end method

.method public fireExitRule(III)V
    .locals 2

    iget-object v0, p0, Lantlr/debug/ParserEventSupport;->traceEvent:Lantlr/debug/TraceEvent;

    sget v1, Lantlr/debug/TraceEvent;->EXIT:I

    invoke-virtual {v0, v1, p1, p2, p3}, Lantlr/debug/TraceEvent;->setValues(IIII)V

    iget-object p1, p0, Lantlr/debug/ParserEventSupport;->traceListeners:Ljava/util/Vector;

    const/4 p2, 0x2

    invoke-virtual {p0, p2, p1}, Lantlr/debug/ParserEventSupport;->fireEvents(ILjava/util/Vector;)V

    iget p1, p0, Lantlr/debug/ParserEventSupport;->ruleDepth:I

    add-int/lit8 p1, p1, -0x1

    iput p1, p0, Lantlr/debug/ParserEventSupport;->ruleDepth:I

    iget p1, p0, Lantlr/debug/ParserEventSupport;->ruleDepth:I

    if-nez p1, :cond_0

    invoke-virtual {p0}, Lantlr/debug/ParserEventSupport;->fireDoneParsing()V

    :cond_0
    return-void
.end method

.method public fireLA(II)V
    .locals 2

    iget-object v0, p0, Lantlr/debug/ParserEventSupport;->tokenEvent:Lantlr/debug/ParserTokenEvent;

    sget v1, Lantlr/debug/ParserTokenEvent;->LA:I

    invoke-virtual {v0, v1, p1, p2}, Lantlr/debug/ParserTokenEvent;->setValues(III)V

    iget-object p1, p0, Lantlr/debug/ParserEventSupport;->tokenListeners:Ljava/util/Vector;

    const/4 p2, 0x3

    invoke-virtual {p0, p2, p1}, Lantlr/debug/ParserEventSupport;->fireEvents(ILjava/util/Vector;)V

    return-void
.end method

.method public fireMatch(CI)V
    .locals 8

    iget-object v0, p0, Lantlr/debug/ParserEventSupport;->matchEvent:Lantlr/debug/ParserMatchEvent;

    sget v1, Lantlr/debug/ParserMatchEvent;->CHAR:I

    new-instance v3, Ljava/lang/Character;

    invoke-direct {v3, p1}, Ljava/lang/Character;-><init>(C)V

    const/4 v4, 0x0

    const/4 v6, 0x0

    const/4 v7, 0x1

    move v2, p1

    move v5, p2

    invoke-virtual/range {v0 .. v7}, Lantlr/debug/ParserMatchEvent;->setValues(IILjava/lang/Object;Ljava/lang/String;IZZ)V

    iget-object p1, p0, Lantlr/debug/ParserEventSupport;->matchListeners:Ljava/util/Vector;

    const/4 p2, 0x4

    invoke-virtual {p0, p2, p1}, Lantlr/debug/ParserEventSupport;->fireEvents(ILjava/util/Vector;)V

    return-void
.end method

.method public fireMatch(CLantlr/collections/impl/BitSet;I)V
    .locals 8

    iget-object v0, p0, Lantlr/debug/ParserEventSupport;->matchEvent:Lantlr/debug/ParserMatchEvent;

    sget v1, Lantlr/debug/ParserMatchEvent;->CHAR_BITSET:I

    const/4 v4, 0x0

    const/4 v6, 0x0

    const/4 v7, 0x1

    move v2, p1

    move-object v3, p2

    move v5, p3

    invoke-virtual/range {v0 .. v7}, Lantlr/debug/ParserMatchEvent;->setValues(IILjava/lang/Object;Ljava/lang/String;IZZ)V

    iget-object p1, p0, Lantlr/debug/ParserEventSupport;->matchListeners:Ljava/util/Vector;

    const/4 p2, 0x4

    invoke-virtual {p0, p2, p1}, Lantlr/debug/ParserEventSupport;->fireEvents(ILjava/util/Vector;)V

    return-void
.end method

.method public fireMatch(CLjava/lang/String;I)V
    .locals 8

    iget-object v0, p0, Lantlr/debug/ParserEventSupport;->matchEvent:Lantlr/debug/ParserMatchEvent;

    sget v1, Lantlr/debug/ParserMatchEvent;->CHAR_RANGE:I

    const/4 v4, 0x0

    const/4 v6, 0x0

    const/4 v7, 0x1

    move v2, p1

    move-object v3, p2

    move v5, p3

    invoke-virtual/range {v0 .. v7}, Lantlr/debug/ParserMatchEvent;->setValues(IILjava/lang/Object;Ljava/lang/String;IZZ)V

    iget-object p1, p0, Lantlr/debug/ParserEventSupport;->matchListeners:Ljava/util/Vector;

    const/4 p2, 0x4

    invoke-virtual {p0, p2, p1}, Lantlr/debug/ParserEventSupport;->fireEvents(ILjava/util/Vector;)V

    return-void
.end method

.method public fireMatch(ILantlr/collections/impl/BitSet;Ljava/lang/String;I)V
    .locals 8

    iget-object v0, p0, Lantlr/debug/ParserEventSupport;->matchEvent:Lantlr/debug/ParserMatchEvent;

    sget v1, Lantlr/debug/ParserMatchEvent;->BITSET:I

    const/4 v6, 0x0

    const/4 v7, 0x1

    move v2, p1

    move-object v3, p2

    move-object v4, p3

    move v5, p4

    invoke-virtual/range {v0 .. v7}, Lantlr/debug/ParserMatchEvent;->setValues(IILjava/lang/Object;Ljava/lang/String;IZZ)V

    iget-object p1, p0, Lantlr/debug/ParserEventSupport;->matchListeners:Ljava/util/Vector;

    const/4 p2, 0x4

    invoke-virtual {p0, p2, p1}, Lantlr/debug/ParserEventSupport;->fireEvents(ILjava/util/Vector;)V

    return-void
.end method

.method public fireMatch(ILjava/lang/String;I)V
    .locals 8

    iget-object v0, p0, Lantlr/debug/ParserEventSupport;->matchEvent:Lantlr/debug/ParserMatchEvent;

    sget v1, Lantlr/debug/ParserMatchEvent;->TOKEN:I

    new-instance v3, Ljava/lang/Integer;

    invoke-direct {v3, p1}, Ljava/lang/Integer;-><init>(I)V

    const/4 v6, 0x0

    const/4 v7, 0x1

    move v2, p1

    move-object v4, p2

    move v5, p3

    invoke-virtual/range {v0 .. v7}, Lantlr/debug/ParserMatchEvent;->setValues(IILjava/lang/Object;Ljava/lang/String;IZZ)V

    iget-object p1, p0, Lantlr/debug/ParserEventSupport;->matchListeners:Ljava/util/Vector;

    const/4 p2, 0x4

    invoke-virtual {p0, p2, p1}, Lantlr/debug/ParserEventSupport;->fireEvents(ILjava/util/Vector;)V

    return-void
.end method

.method public fireMatch(Ljava/lang/String;I)V
    .locals 8

    iget-object v0, p0, Lantlr/debug/ParserEventSupport;->matchEvent:Lantlr/debug/ParserMatchEvent;

    sget v1, Lantlr/debug/ParserMatchEvent;->STRING:I

    const/4 v2, 0x0

    const/4 v4, 0x0

    const/4 v6, 0x0

    const/4 v7, 0x1

    move-object v3, p1

    move v5, p2

    invoke-virtual/range {v0 .. v7}, Lantlr/debug/ParserMatchEvent;->setValues(IILjava/lang/Object;Ljava/lang/String;IZZ)V

    iget-object p1, p0, Lantlr/debug/ParserEventSupport;->matchListeners:Ljava/util/Vector;

    const/4 p2, 0x4

    invoke-virtual {p0, p2, p1}, Lantlr/debug/ParserEventSupport;->fireEvents(ILjava/util/Vector;)V

    return-void
.end method

.method public fireMatchNot(CCI)V
    .locals 8

    iget-object v0, p0, Lantlr/debug/ParserEventSupport;->matchEvent:Lantlr/debug/ParserMatchEvent;

    sget v1, Lantlr/debug/ParserMatchEvent;->CHAR:I

    new-instance v3, Ljava/lang/Character;

    invoke-direct {v3, p2}, Ljava/lang/Character;-><init>(C)V

    const/4 v4, 0x0

    const/4 v6, 0x1

    const/4 v7, 0x1

    move v2, p1

    move v5, p3

    invoke-virtual/range {v0 .. v7}, Lantlr/debug/ParserMatchEvent;->setValues(IILjava/lang/Object;Ljava/lang/String;IZZ)V

    iget-object p1, p0, Lantlr/debug/ParserEventSupport;->matchListeners:Ljava/util/Vector;

    const/4 p2, 0x5

    invoke-virtual {p0, p2, p1}, Lantlr/debug/ParserEventSupport;->fireEvents(ILjava/util/Vector;)V

    return-void
.end method

.method public fireMatchNot(IILjava/lang/String;I)V
    .locals 8

    iget-object v0, p0, Lantlr/debug/ParserEventSupport;->matchEvent:Lantlr/debug/ParserMatchEvent;

    sget v1, Lantlr/debug/ParserMatchEvent;->TOKEN:I

    new-instance v3, Ljava/lang/Integer;

    invoke-direct {v3, p2}, Ljava/lang/Integer;-><init>(I)V

    const/4 v6, 0x1

    const/4 v7, 0x1

    move v2, p1

    move-object v4, p3

    move v5, p4

    invoke-virtual/range {v0 .. v7}, Lantlr/debug/ParserMatchEvent;->setValues(IILjava/lang/Object;Ljava/lang/String;IZZ)V

    iget-object p1, p0, Lantlr/debug/ParserEventSupport;->matchListeners:Ljava/util/Vector;

    const/4 p2, 0x5

    invoke-virtual {p0, p2, p1}, Lantlr/debug/ParserEventSupport;->fireEvents(ILjava/util/Vector;)V

    return-void
.end method

.method public fireMismatch(CCI)V
    .locals 8

    iget-object v0, p0, Lantlr/debug/ParserEventSupport;->matchEvent:Lantlr/debug/ParserMatchEvent;

    sget v1, Lantlr/debug/ParserMatchEvent;->CHAR:I

    new-instance v3, Ljava/lang/Character;

    invoke-direct {v3, p2}, Ljava/lang/Character;-><init>(C)V

    const/4 v4, 0x0

    const/4 v6, 0x0

    const/4 v7, 0x0

    move v2, p1

    move v5, p3

    invoke-virtual/range {v0 .. v7}, Lantlr/debug/ParserMatchEvent;->setValues(IILjava/lang/Object;Ljava/lang/String;IZZ)V

    iget-object p1, p0, Lantlr/debug/ParserEventSupport;->matchListeners:Ljava/util/Vector;

    const/4 p2, 0x6

    invoke-virtual {p0, p2, p1}, Lantlr/debug/ParserEventSupport;->fireEvents(ILjava/util/Vector;)V

    return-void
.end method

.method public fireMismatch(CLantlr/collections/impl/BitSet;I)V
    .locals 8

    iget-object v0, p0, Lantlr/debug/ParserEventSupport;->matchEvent:Lantlr/debug/ParserMatchEvent;

    sget v1, Lantlr/debug/ParserMatchEvent;->CHAR_BITSET:I

    const/4 v4, 0x0

    const/4 v6, 0x0

    const/4 v7, 0x1

    move v2, p1

    move-object v3, p2

    move v5, p3

    invoke-virtual/range {v0 .. v7}, Lantlr/debug/ParserMatchEvent;->setValues(IILjava/lang/Object;Ljava/lang/String;IZZ)V

    iget-object p1, p0, Lantlr/debug/ParserEventSupport;->matchListeners:Ljava/util/Vector;

    const/4 p2, 0x6

    invoke-virtual {p0, p2, p1}, Lantlr/debug/ParserEventSupport;->fireEvents(ILjava/util/Vector;)V

    return-void
.end method

.method public fireMismatch(CLjava/lang/String;I)V
    .locals 8

    iget-object v0, p0, Lantlr/debug/ParserEventSupport;->matchEvent:Lantlr/debug/ParserMatchEvent;

    sget v1, Lantlr/debug/ParserMatchEvent;->CHAR_RANGE:I

    const/4 v4, 0x0

    const/4 v6, 0x0

    const/4 v7, 0x1

    move v2, p1

    move-object v3, p2

    move v5, p3

    invoke-virtual/range {v0 .. v7}, Lantlr/debug/ParserMatchEvent;->setValues(IILjava/lang/Object;Ljava/lang/String;IZZ)V

    iget-object p1, p0, Lantlr/debug/ParserEventSupport;->matchListeners:Ljava/util/Vector;

    const/4 p2, 0x6

    invoke-virtual {p0, p2, p1}, Lantlr/debug/ParserEventSupport;->fireEvents(ILjava/util/Vector;)V

    return-void
.end method

.method public fireMismatch(IILjava/lang/String;I)V
    .locals 8

    iget-object v0, p0, Lantlr/debug/ParserEventSupport;->matchEvent:Lantlr/debug/ParserMatchEvent;

    sget v1, Lantlr/debug/ParserMatchEvent;->TOKEN:I

    new-instance v3, Ljava/lang/Integer;

    invoke-direct {v3, p2}, Ljava/lang/Integer;-><init>(I)V

    const/4 v6, 0x0

    const/4 v7, 0x0

    move v2, p1

    move-object v4, p3

    move v5, p4

    invoke-virtual/range {v0 .. v7}, Lantlr/debug/ParserMatchEvent;->setValues(IILjava/lang/Object;Ljava/lang/String;IZZ)V

    iget-object p1, p0, Lantlr/debug/ParserEventSupport;->matchListeners:Ljava/util/Vector;

    const/4 p2, 0x6

    invoke-virtual {p0, p2, p1}, Lantlr/debug/ParserEventSupport;->fireEvents(ILjava/util/Vector;)V

    return-void
.end method

.method public fireMismatch(ILantlr/collections/impl/BitSet;Ljava/lang/String;I)V
    .locals 8

    iget-object v0, p0, Lantlr/debug/ParserEventSupport;->matchEvent:Lantlr/debug/ParserMatchEvent;

    sget v1, Lantlr/debug/ParserMatchEvent;->BITSET:I

    const/4 v6, 0x0

    const/4 v7, 0x1

    move v2, p1

    move-object v3, p2

    move-object v4, p3

    move v5, p4

    invoke-virtual/range {v0 .. v7}, Lantlr/debug/ParserMatchEvent;->setValues(IILjava/lang/Object;Ljava/lang/String;IZZ)V

    iget-object p1, p0, Lantlr/debug/ParserEventSupport;->matchListeners:Ljava/util/Vector;

    const/4 p2, 0x6

    invoke-virtual {p0, p2, p1}, Lantlr/debug/ParserEventSupport;->fireEvents(ILjava/util/Vector;)V

    return-void
.end method

.method public fireMismatch(Ljava/lang/String;Ljava/lang/String;I)V
    .locals 8

    iget-object v0, p0, Lantlr/debug/ParserEventSupport;->matchEvent:Lantlr/debug/ParserMatchEvent;

    sget v1, Lantlr/debug/ParserMatchEvent;->STRING:I

    const/4 v2, 0x0

    const/4 v6, 0x0

    const/4 v7, 0x1

    move-object v3, p2

    move-object v4, p1

    move v5, p3

    invoke-virtual/range {v0 .. v7}, Lantlr/debug/ParserMatchEvent;->setValues(IILjava/lang/Object;Ljava/lang/String;IZZ)V

    iget-object p1, p0, Lantlr/debug/ParserEventSupport;->matchListeners:Ljava/util/Vector;

    const/4 p2, 0x6

    invoke-virtual {p0, p2, p1}, Lantlr/debug/ParserEventSupport;->fireEvents(ILjava/util/Vector;)V

    return-void
.end method

.method public fireMismatchNot(CCI)V
    .locals 8

    iget-object v0, p0, Lantlr/debug/ParserEventSupport;->matchEvent:Lantlr/debug/ParserMatchEvent;

    sget v1, Lantlr/debug/ParserMatchEvent;->CHAR:I

    new-instance v3, Ljava/lang/Character;

    invoke-direct {v3, p2}, Ljava/lang/Character;-><init>(C)V

    const/4 v4, 0x0

    const/4 v6, 0x1

    const/4 v7, 0x1

    move v2, p1

    move v5, p3

    invoke-virtual/range {v0 .. v7}, Lantlr/debug/ParserMatchEvent;->setValues(IILjava/lang/Object;Ljava/lang/String;IZZ)V

    iget-object p1, p0, Lantlr/debug/ParserEventSupport;->matchListeners:Ljava/util/Vector;

    const/4 p2, 0x7

    invoke-virtual {p0, p2, p1}, Lantlr/debug/ParserEventSupport;->fireEvents(ILjava/util/Vector;)V

    return-void
.end method

.method public fireMismatchNot(IILjava/lang/String;I)V
    .locals 8

    iget-object v0, p0, Lantlr/debug/ParserEventSupport;->matchEvent:Lantlr/debug/ParserMatchEvent;

    sget v1, Lantlr/debug/ParserMatchEvent;->TOKEN:I

    new-instance v3, Ljava/lang/Integer;

    invoke-direct {v3, p2}, Ljava/lang/Integer;-><init>(I)V

    const/4 v6, 0x1

    const/4 v7, 0x1

    move v2, p1

    move-object v4, p3

    move v5, p4

    invoke-virtual/range {v0 .. v7}, Lantlr/debug/ParserMatchEvent;->setValues(IILjava/lang/Object;Ljava/lang/String;IZZ)V

    iget-object p1, p0, Lantlr/debug/ParserEventSupport;->matchListeners:Ljava/util/Vector;

    const/4 p2, 0x7

    invoke-virtual {p0, p2, p1}, Lantlr/debug/ParserEventSupport;->fireEvents(ILjava/util/Vector;)V

    return-void
.end method

.method public fireNewLine(I)V
    .locals 1

    iget-object v0, p0, Lantlr/debug/ParserEventSupport;->newLineEvent:Lantlr/debug/NewLineEvent;

    invoke-virtual {v0, p1}, Lantlr/debug/NewLineEvent;->setValues(I)V

    iget-object p1, p0, Lantlr/debug/ParserEventSupport;->newLineListeners:Ljava/util/Vector;

    const/16 v0, 0xe

    invoke-virtual {p0, v0, p1}, Lantlr/debug/ParserEventSupport;->fireEvents(ILjava/util/Vector;)V

    return-void
.end method

.method public fireReportError(Ljava/lang/Exception;)V
    .locals 2

    iget-object v0, p0, Lantlr/debug/ParserEventSupport;->messageEvent:Lantlr/debug/MessageEvent;

    sget v1, Lantlr/debug/MessageEvent;->ERROR:I

    invoke-virtual {p1}, Ljava/lang/Exception;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {v0, v1, p1}, Lantlr/debug/MessageEvent;->setValues(ILjava/lang/String;)V

    iget-object p1, p0, Lantlr/debug/ParserEventSupport;->messageListeners:Ljava/util/Vector;

    const/16 v0, 0x8

    invoke-virtual {p0, v0, p1}, Lantlr/debug/ParserEventSupport;->fireEvents(ILjava/util/Vector;)V

    return-void
.end method

.method public fireReportError(Ljava/lang/String;)V
    .locals 2

    iget-object v0, p0, Lantlr/debug/ParserEventSupport;->messageEvent:Lantlr/debug/MessageEvent;

    sget v1, Lantlr/debug/MessageEvent;->ERROR:I

    invoke-virtual {v0, v1, p1}, Lantlr/debug/MessageEvent;->setValues(ILjava/lang/String;)V

    iget-object p1, p0, Lantlr/debug/ParserEventSupport;->messageListeners:Ljava/util/Vector;

    const/16 v0, 0x8

    invoke-virtual {p0, v0, p1}, Lantlr/debug/ParserEventSupport;->fireEvents(ILjava/util/Vector;)V

    return-void
.end method

.method public fireReportWarning(Ljava/lang/String;)V
    .locals 2

    iget-object v0, p0, Lantlr/debug/ParserEventSupport;->messageEvent:Lantlr/debug/MessageEvent;

    sget v1, Lantlr/debug/MessageEvent;->WARNING:I

    invoke-virtual {v0, v1, p1}, Lantlr/debug/MessageEvent;->setValues(ILjava/lang/String;)V

    iget-object p1, p0, Lantlr/debug/ParserEventSupport;->messageListeners:Ljava/util/Vector;

    const/16 v0, 0x9

    invoke-virtual {p0, v0, p1}, Lantlr/debug/ParserEventSupport;->fireEvents(ILjava/util/Vector;)V

    return-void
.end method

.method public fireSemanticPredicateEvaluated(IIZI)Z
    .locals 1

    iget-object v0, p0, Lantlr/debug/ParserEventSupport;->semPredEvent:Lantlr/debug/SemanticPredicateEvent;

    invoke-virtual {v0, p1, p2, p3, p4}, Lantlr/debug/SemanticPredicateEvent;->setValues(IIZI)V

    iget-object p1, p0, Lantlr/debug/ParserEventSupport;->semPredListeners:Ljava/util/Vector;

    const/16 p2, 0xa

    invoke-virtual {p0, p2, p1}, Lantlr/debug/ParserEventSupport;->fireEvents(ILjava/util/Vector;)V

    return p3
.end method

.method public fireSyntacticPredicateFailed(I)V
    .locals 2

    iget-object v0, p0, Lantlr/debug/ParserEventSupport;->synPredEvent:Lantlr/debug/SyntacticPredicateEvent;

    const/4 v1, 0x0

    invoke-virtual {v0, v1, p1}, Lantlr/debug/SyntacticPredicateEvent;->setValues(II)V

    iget-object p1, p0, Lantlr/debug/ParserEventSupport;->synPredListeners:Ljava/util/Vector;

    const/16 v0, 0xb

    invoke-virtual {p0, v0, p1}, Lantlr/debug/ParserEventSupport;->fireEvents(ILjava/util/Vector;)V

    return-void
.end method

.method public fireSyntacticPredicateStarted(I)V
    .locals 2

    iget-object v0, p0, Lantlr/debug/ParserEventSupport;->synPredEvent:Lantlr/debug/SyntacticPredicateEvent;

    const/4 v1, 0x0

    invoke-virtual {v0, v1, p1}, Lantlr/debug/SyntacticPredicateEvent;->setValues(II)V

    iget-object p1, p0, Lantlr/debug/ParserEventSupport;->synPredListeners:Ljava/util/Vector;

    const/16 v0, 0xc

    invoke-virtual {p0, v0, p1}, Lantlr/debug/ParserEventSupport;->fireEvents(ILjava/util/Vector;)V

    return-void
.end method

.method public fireSyntacticPredicateSucceeded(I)V
    .locals 2

    iget-object v0, p0, Lantlr/debug/ParserEventSupport;->synPredEvent:Lantlr/debug/SyntacticPredicateEvent;

    const/4 v1, 0x0

    invoke-virtual {v0, v1, p1}, Lantlr/debug/SyntacticPredicateEvent;->setValues(II)V

    iget-object p1, p0, Lantlr/debug/ParserEventSupport;->synPredListeners:Ljava/util/Vector;

    const/16 v0, 0xd

    invoke-virtual {p0, v0, p1}, Lantlr/debug/ParserEventSupport;->fireEvents(ILjava/util/Vector;)V

    return-void
.end method

.method public refresh(Ljava/util/Vector;)V
    .locals 1

    monitor-enter p1

    :try_start_0
    invoke-virtual {p1}, Ljava/util/Vector;->clone()Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Ljava/util/Vector;

    monitor-exit p1
    :try_end_0
    .catchall {:try_start_0 .. :try_end_0} :catchall_0

    if-eqz p0, :cond_0

    const/4 p1, 0x0

    :goto_0
    invoke-virtual {p0}, Ljava/util/Vector;->size()I

    move-result v0

    if-ge p1, v0, :cond_0

    invoke-virtual {p0, p1}, Ljava/util/Vector;->elementAt(I)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lantlr/debug/ListenerBase;

    invoke-interface {v0}, Lantlr/debug/ListenerBase;->refresh()V

    add-int/lit8 p1, p1, 0x1

    goto :goto_0

    :cond_0
    return-void

    :catchall_0
    move-exception p0

    :try_start_1
    monitor-exit p1
    :try_end_1
    .catchall {:try_start_1 .. :try_end_1} :catchall_0

    throw p0
.end method

.method public refreshListeners()V
    .locals 1

    iget-object v0, p0, Lantlr/debug/ParserEventSupport;->matchListeners:Ljava/util/Vector;

    invoke-virtual {p0, v0}, Lantlr/debug/ParserEventSupport;->refresh(Ljava/util/Vector;)V

    iget-object v0, p0, Lantlr/debug/ParserEventSupport;->messageListeners:Ljava/util/Vector;

    invoke-virtual {p0, v0}, Lantlr/debug/ParserEventSupport;->refresh(Ljava/util/Vector;)V

    iget-object v0, p0, Lantlr/debug/ParserEventSupport;->tokenListeners:Ljava/util/Vector;

    invoke-virtual {p0, v0}, Lantlr/debug/ParserEventSupport;->refresh(Ljava/util/Vector;)V

    iget-object v0, p0, Lantlr/debug/ParserEventSupport;->traceListeners:Ljava/util/Vector;

    invoke-virtual {p0, v0}, Lantlr/debug/ParserEventSupport;->refresh(Ljava/util/Vector;)V

    iget-object v0, p0, Lantlr/debug/ParserEventSupport;->semPredListeners:Ljava/util/Vector;

    invoke-virtual {p0, v0}, Lantlr/debug/ParserEventSupport;->refresh(Ljava/util/Vector;)V

    iget-object v0, p0, Lantlr/debug/ParserEventSupport;->synPredListeners:Ljava/util/Vector;

    invoke-virtual {p0, v0}, Lantlr/debug/ParserEventSupport;->refresh(Ljava/util/Vector;)V

    return-void
.end method

.method public removeDoneListener(Lantlr/debug/ListenerBase;)V
    .locals 2

    iget-object v0, p0, Lantlr/debug/ParserEventSupport;->doneListeners:Ljava/util/Hashtable;

    if-nez v0, :cond_0

    return-void

    :cond_0
    invoke-virtual {v0, p1}, Ljava/util/Hashtable;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Ljava/lang/Integer;

    const/4 v1, 0x0

    if-eqz v0, :cond_1

    invoke-virtual {v0}, Ljava/lang/Integer;->intValue()I

    move-result v0

    add-int/lit8 v1, v0, -0x1

    :cond_1
    iget-object p0, p0, Lantlr/debug/ParserEventSupport;->doneListeners:Ljava/util/Hashtable;

    if-nez v1, :cond_2

    invoke-virtual {p0, p1}, Ljava/util/Hashtable;->remove(Ljava/lang/Object;)Ljava/lang/Object;

    goto :goto_0

    :cond_2
    new-instance v0, Ljava/lang/Integer;

    invoke-direct {v0, v1}, Ljava/lang/Integer;-><init>(I)V

    invoke-virtual {p0, p1, v0}, Ljava/util/Hashtable;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    :goto_0
    return-void
.end method

.method public removeMessageListener(Lantlr/debug/MessageListener;)V
    .locals 1

    iget-object v0, p0, Lantlr/debug/ParserEventSupport;->messageListeners:Ljava/util/Vector;

    if-eqz v0, :cond_0

    invoke-virtual {v0, p1}, Ljava/util/Vector;->removeElement(Ljava/lang/Object;)Z

    :cond_0
    invoke-virtual {p0, p1}, Lantlr/debug/ParserEventSupport;->removeDoneListener(Lantlr/debug/ListenerBase;)V

    return-void
.end method

.method public removeNewLineListener(Lantlr/debug/NewLineListener;)V
    .locals 1

    iget-object v0, p0, Lantlr/debug/ParserEventSupport;->newLineListeners:Ljava/util/Vector;

    if-eqz v0, :cond_0

    invoke-virtual {v0, p1}, Ljava/util/Vector;->removeElement(Ljava/lang/Object;)Z

    :cond_0
    invoke-virtual {p0, p1}, Lantlr/debug/ParserEventSupport;->removeDoneListener(Lantlr/debug/ListenerBase;)V

    return-void
.end method

.method public removeParserListener(Lantlr/debug/ParserListener;)V
    .locals 0

    invoke-virtual {p0, p1}, Lantlr/debug/ParserEventSupport;->removeParserMatchListener(Lantlr/debug/ParserMatchListener;)V

    invoke-virtual {p0, p1}, Lantlr/debug/ParserEventSupport;->removeMessageListener(Lantlr/debug/MessageListener;)V

    invoke-virtual {p0, p1}, Lantlr/debug/ParserEventSupport;->removeParserTokenListener(Lantlr/debug/ParserTokenListener;)V

    invoke-virtual {p0, p1}, Lantlr/debug/ParserEventSupport;->removeTraceListener(Lantlr/debug/TraceListener;)V

    invoke-virtual {p0, p1}, Lantlr/debug/ParserEventSupport;->removeSemanticPredicateListener(Lantlr/debug/SemanticPredicateListener;)V

    invoke-virtual {p0, p1}, Lantlr/debug/ParserEventSupport;->removeSyntacticPredicateListener(Lantlr/debug/SyntacticPredicateListener;)V

    return-void
.end method

.method public removeParserMatchListener(Lantlr/debug/ParserMatchListener;)V
    .locals 1

    iget-object v0, p0, Lantlr/debug/ParserEventSupport;->matchListeners:Ljava/util/Vector;

    if-eqz v0, :cond_0

    invoke-virtual {v0, p1}, Ljava/util/Vector;->removeElement(Ljava/lang/Object;)Z

    :cond_0
    invoke-virtual {p0, p1}, Lantlr/debug/ParserEventSupport;->removeDoneListener(Lantlr/debug/ListenerBase;)V

    return-void
.end method

.method public removeParserTokenListener(Lantlr/debug/ParserTokenListener;)V
    .locals 1

    iget-object v0, p0, Lantlr/debug/ParserEventSupport;->tokenListeners:Ljava/util/Vector;

    if-eqz v0, :cond_0

    invoke-virtual {v0, p1}, Ljava/util/Vector;->removeElement(Ljava/lang/Object;)Z

    :cond_0
    invoke-virtual {p0, p1}, Lantlr/debug/ParserEventSupport;->removeDoneListener(Lantlr/debug/ListenerBase;)V

    return-void
.end method

.method public removeSemanticPredicateListener(Lantlr/debug/SemanticPredicateListener;)V
    .locals 1

    iget-object v0, p0, Lantlr/debug/ParserEventSupport;->semPredListeners:Ljava/util/Vector;

    if-eqz v0, :cond_0

    invoke-virtual {v0, p1}, Ljava/util/Vector;->removeElement(Ljava/lang/Object;)Z

    :cond_0
    invoke-virtual {p0, p1}, Lantlr/debug/ParserEventSupport;->removeDoneListener(Lantlr/debug/ListenerBase;)V

    return-void
.end method

.method public removeSyntacticPredicateListener(Lantlr/debug/SyntacticPredicateListener;)V
    .locals 1

    iget-object v0, p0, Lantlr/debug/ParserEventSupport;->synPredListeners:Ljava/util/Vector;

    if-eqz v0, :cond_0

    invoke-virtual {v0, p1}, Ljava/util/Vector;->removeElement(Ljava/lang/Object;)Z

    :cond_0
    invoke-virtual {p0, p1}, Lantlr/debug/ParserEventSupport;->removeDoneListener(Lantlr/debug/ListenerBase;)V

    return-void
.end method

.method public removeTraceListener(Lantlr/debug/TraceListener;)V
    .locals 1

    iget-object v0, p0, Lantlr/debug/ParserEventSupport;->traceListeners:Ljava/util/Vector;

    if-eqz v0, :cond_0

    invoke-virtual {v0, p1}, Ljava/util/Vector;->removeElement(Ljava/lang/Object;)Z

    :cond_0
    invoke-virtual {p0, p1}, Lantlr/debug/ParserEventSupport;->removeDoneListener(Lantlr/debug/ListenerBase;)V

    return-void
.end method
