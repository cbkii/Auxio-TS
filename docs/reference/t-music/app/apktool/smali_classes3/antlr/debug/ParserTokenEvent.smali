.class public Lantlr/debug/ParserTokenEvent;
.super Lantlr/debug/Event;
.source ""


# static fields
.field public static CONSUME:I = 0x1

.field public static LA:I


# instance fields
.field public amount:I

.field public value:I


# direct methods
.method public static constructor <clinit>()V
    .locals 0

    return-void
.end method

.method public constructor <init>(Ljava/lang/Object;)V
    .locals 0

    invoke-direct {p0, p1}, Lantlr/debug/Event;-><init>(Ljava/lang/Object;)V

    return-void
.end method

.method public constructor <init>(Ljava/lang/Object;III)V
    .locals 0

    invoke-direct {p0, p1}, Lantlr/debug/Event;-><init>(Ljava/lang/Object;)V

    invoke-virtual {p0, p2, p3, p4}, Lantlr/debug/ParserTokenEvent;->setValues(III)V

    return-void
.end method


# virtual methods
.method public getAmount()I
    .locals 0

    iget p0, p0, Lantlr/debug/ParserTokenEvent;->amount:I

    return p0
.end method

.method public getValue()I
    .locals 0

    iget p0, p0, Lantlr/debug/ParserTokenEvent;->value:I

    return p0
.end method

.method public setAmount(I)V
    .locals 0

    iput p1, p0, Lantlr/debug/ParserTokenEvent;->amount:I

    return-void
.end method

.method public setValue(I)V
    .locals 0

    iput p1, p0, Lantlr/debug/ParserTokenEvent;->value:I

    return-void
.end method

.method public setValues(III)V
    .locals 0

    invoke-super {p0, p1}, Lantlr/debug/Event;->setValues(I)V

    invoke-virtual {p0, p2}, Lantlr/debug/ParserTokenEvent;->setAmount(I)V

    invoke-virtual {p0, p3}, Lantlr/debug/ParserTokenEvent;->setValue(I)V

    return-void
.end method

.method public toString()Ljava/lang/String;
    .locals 3

    invoke-virtual {p0}, Lantlr/debug/Event;->getType()I

    move-result v0

    sget v1, Lantlr/debug/ParserTokenEvent;->LA:I

    const-string v2, "]"

    if-ne v0, v1, :cond_0

    const-string v0, "ParserTokenEvent [LA,"

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/debug/ParserTokenEvent;->getAmount()I

    move-result v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v1, ","

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    :goto_0
    invoke-virtual {p0}, Lantlr/debug/ParserTokenEvent;->getValue()I

    move-result p0

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0

    :cond_0
    const-string v0, "ParserTokenEvent [consume,1,"

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    goto :goto_0
.end method
