.class public Lantlr/debug/TraceEvent;
.super Lantlr/debug/GuessingEvent;
.source ""


# static fields
.field public static DONE_PARSING:I = 0x2

.field public static ENTER:I = 0x0

.field public static EXIT:I = 0x1


# instance fields
.field public data:I

.field public ruleNum:I


# direct methods
.method public static constructor <clinit>()V
    .locals 0

    return-void
.end method

.method public constructor <init>(Ljava/lang/Object;)V
    .locals 0

    invoke-direct {p0, p1}, Lantlr/debug/GuessingEvent;-><init>(Ljava/lang/Object;)V

    return-void
.end method

.method public constructor <init>(Ljava/lang/Object;IIII)V
    .locals 0

    invoke-direct {p0, p1}, Lantlr/debug/GuessingEvent;-><init>(Ljava/lang/Object;)V

    invoke-virtual {p0, p2, p3, p4, p5}, Lantlr/debug/TraceEvent;->setValues(IIII)V

    return-void
.end method


# virtual methods
.method public getData()I
    .locals 0

    iget p0, p0, Lantlr/debug/TraceEvent;->data:I

    return p0
.end method

.method public getRuleNum()I
    .locals 0

    iget p0, p0, Lantlr/debug/TraceEvent;->ruleNum:I

    return p0
.end method

.method public setData(I)V
    .locals 0

    iput p1, p0, Lantlr/debug/TraceEvent;->data:I

    return-void
.end method

.method public setRuleNum(I)V
    .locals 0

    iput p1, p0, Lantlr/debug/TraceEvent;->ruleNum:I

    return-void
.end method

.method public setValues(IIII)V
    .locals 0

    invoke-super {p0, p1, p3}, Lantlr/debug/GuessingEvent;->setValues(II)V

    invoke-virtual {p0, p2}, Lantlr/debug/TraceEvent;->setRuleNum(I)V

    invoke-virtual {p0, p4}, Lantlr/debug/TraceEvent;->setData(I)V

    return-void
.end method

.method public toString()Ljava/lang/String;
    .locals 3

    const-string v0, "ParserTraceEvent ["

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/debug/Event;->getType()I

    move-result v1

    sget v2, Lantlr/debug/TraceEvent;->ENTER:I

    if-ne v1, v2, :cond_0

    const-string v1, "enter,"

    goto :goto_0

    :cond_0
    const-string v1, "exit,"

    :goto_0
    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0}, Lantlr/debug/TraceEvent;->getRuleNum()I

    move-result v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v1, ","

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0}, Lantlr/debug/GuessingEvent;->getGuessing()I

    move-result p0

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string p0, "]"

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method
