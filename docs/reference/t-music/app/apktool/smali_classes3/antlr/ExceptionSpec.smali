.class public Lantlr/ExceptionSpec;
.super Ljava/lang/Object;
.source ""


# instance fields
.field public handlers:Lantlr/collections/impl/Vector;

.field public label:Lantlr/Token;


# direct methods
.method public constructor <init>(Lantlr/Token;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lantlr/ExceptionSpec;->label:Lantlr/Token;

    new-instance p1, Lantlr/collections/impl/Vector;

    invoke-direct {p1}, Lantlr/collections/impl/Vector;-><init>()V

    iput-object p1, p0, Lantlr/ExceptionSpec;->handlers:Lantlr/collections/impl/Vector;

    return-void
.end method


# virtual methods
.method public addHandler(Lantlr/ExceptionHandler;)V
    .locals 0

    iget-object p0, p0, Lantlr/ExceptionSpec;->handlers:Lantlr/collections/impl/Vector;

    invoke-virtual {p0, p1}, Lantlr/collections/impl/Vector;->appendElement(Ljava/lang/Object;)V

    return-void
.end method
