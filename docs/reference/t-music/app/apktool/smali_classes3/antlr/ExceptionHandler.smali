.class public Lantlr/ExceptionHandler;
.super Ljava/lang/Object;
.source ""


# instance fields
.field public action:Lantlr/Token;

.field public exceptionTypeAndName:Lantlr/Token;


# direct methods
.method public constructor <init>(Lantlr/Token;Lantlr/Token;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lantlr/ExceptionHandler;->exceptionTypeAndName:Lantlr/Token;

    iput-object p2, p0, Lantlr/ExceptionHandler;->action:Lantlr/Token;

    return-void
.end method
