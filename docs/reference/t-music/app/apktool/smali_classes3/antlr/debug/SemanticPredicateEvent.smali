.class public Lantlr/debug/SemanticPredicateEvent;
.super Lantlr/debug/GuessingEvent;
.source ""


# static fields
.field public static final PREDICTING:I = 0x1

.field public static final VALIDATING:I


# instance fields
.field public condition:I

.field public result:Z


# direct methods
.method public constructor <init>(Ljava/lang/Object;)V
    .locals 0

    invoke-direct {p0, p1}, Lantlr/debug/GuessingEvent;-><init>(Ljava/lang/Object;)V

    return-void
.end method

.method public constructor <init>(Ljava/lang/Object;I)V
    .locals 0

    invoke-direct {p0, p1, p2}, Lantlr/debug/GuessingEvent;-><init>(Ljava/lang/Object;I)V

    return-void
.end method


# virtual methods
.method public getCondition()I
    .locals 0

    iget p0, p0, Lantlr/debug/SemanticPredicateEvent;->condition:I

    return p0
.end method

.method public getResult()Z
    .locals 0

    iget-boolean p0, p0, Lantlr/debug/SemanticPredicateEvent;->result:Z

    return p0
.end method

.method public setCondition(I)V
    .locals 0

    iput p1, p0, Lantlr/debug/SemanticPredicateEvent;->condition:I

    return-void
.end method

.method public setResult(Z)V
    .locals 0

    iput-boolean p1, p0, Lantlr/debug/SemanticPredicateEvent;->result:Z

    return-void
.end method

.method public setValues(IIZI)V
    .locals 0

    invoke-super {p0, p1, p4}, Lantlr/debug/GuessingEvent;->setValues(II)V

    invoke-virtual {p0, p2}, Lantlr/debug/SemanticPredicateEvent;->setCondition(I)V

    invoke-virtual {p0, p3}, Lantlr/debug/SemanticPredicateEvent;->setResult(Z)V

    return-void
.end method

.method public toString()Ljava/lang/String;
    .locals 3

    const-string v0, "SemanticPredicateEvent ["

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/debug/SemanticPredicateEvent;->getCondition()I

    move-result v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v1, ","

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0}, Lantlr/debug/SemanticPredicateEvent;->getResult()Z

    move-result v2

    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(Z)Ljava/lang/StringBuilder;

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
