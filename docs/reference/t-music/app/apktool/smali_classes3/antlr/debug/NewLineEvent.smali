.class public Lantlr/debug/NewLineEvent;
.super Lantlr/debug/Event;
.source ""


# instance fields
.field public line:I


# direct methods
.method public constructor <init>(Ljava/lang/Object;)V
    .locals 0

    invoke-direct {p0, p1}, Lantlr/debug/Event;-><init>(Ljava/lang/Object;)V

    return-void
.end method

.method public constructor <init>(Ljava/lang/Object;I)V
    .locals 0

    invoke-direct {p0, p1}, Lantlr/debug/Event;-><init>(Ljava/lang/Object;)V

    invoke-virtual {p0, p2}, Lantlr/debug/NewLineEvent;->setValues(I)V

    return-void
.end method


# virtual methods
.method public getLine()I
    .locals 0

    iget p0, p0, Lantlr/debug/NewLineEvent;->line:I

    return p0
.end method

.method public setLine(I)V
    .locals 0

    iput p1, p0, Lantlr/debug/NewLineEvent;->line:I

    return-void
.end method

.method public setValues(I)V
    .locals 0

    invoke-virtual {p0, p1}, Lantlr/debug/NewLineEvent;->setLine(I)V

    return-void
.end method

.method public toString()Ljava/lang/String;
    .locals 1

    const-string v0, "NewLineEvent ["

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget p0, p0, Lantlr/debug/NewLineEvent;->line:I

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string p0, "]"

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method
