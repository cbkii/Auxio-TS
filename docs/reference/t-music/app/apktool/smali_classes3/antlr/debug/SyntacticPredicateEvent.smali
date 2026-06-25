.class public Lantlr/debug/SyntacticPredicateEvent;
.super Lantlr/debug/GuessingEvent;
.source ""


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
.method public setValues(II)V
    .locals 0

    invoke-super {p0, p1, p2}, Lantlr/debug/GuessingEvent;->setValues(II)V

    return-void
.end method

.method public toString()Ljava/lang/String;
    .locals 1

    const-string v0, "SyntacticPredicateEvent ["

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/debug/GuessingEvent;->getGuessing()I

    move-result p0

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string p0, "]"

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method
