.class public Lantlr/debug/MessageEvent;
.super Lantlr/debug/Event;
.source ""


# static fields
.field public static ERROR:I = 0x1

.field public static WARNING:I


# instance fields
.field public text:Ljava/lang/String;


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

.method public constructor <init>(Ljava/lang/Object;ILjava/lang/String;)V
    .locals 0

    invoke-direct {p0, p1}, Lantlr/debug/Event;-><init>(Ljava/lang/Object;)V

    invoke-virtual {p0, p2, p3}, Lantlr/debug/MessageEvent;->setValues(ILjava/lang/String;)V

    return-void
.end method


# virtual methods
.method public getText()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lantlr/debug/MessageEvent;->text:Ljava/lang/String;

    return-object p0
.end method

.method public setText(Ljava/lang/String;)V
    .locals 0

    iput-object p1, p0, Lantlr/debug/MessageEvent;->text:Ljava/lang/String;

    return-void
.end method

.method public setValues(ILjava/lang/String;)V
    .locals 0

    invoke-super {p0, p1}, Lantlr/debug/Event;->setValues(I)V

    invoke-virtual {p0, p2}, Lantlr/debug/MessageEvent;->setText(Ljava/lang/String;)V

    return-void
.end method

.method public toString()Ljava/lang/String;
    .locals 3

    const-string v0, "ParserMessageEvent ["

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/debug/Event;->getType()I

    move-result v1

    sget v2, Lantlr/debug/MessageEvent;->WARNING:I

    if-ne v1, v2, :cond_0

    const-string v1, "warning,"

    goto :goto_0

    :cond_0
    const-string v1, "error,"

    :goto_0
    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0}, Lantlr/debug/MessageEvent;->getText()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p0, "]"

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method
