.class public final Lantlr/collections/impl/LLEnumeration;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Ljava/util/Enumeration;


# instance fields
.field public cursor:Lantlr/collections/impl/LLCell;

.field public list:Lantlr/collections/impl/LList;


# direct methods
.method public constructor <init>(Lantlr/collections/impl/LList;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lantlr/collections/impl/LLEnumeration;->list:Lantlr/collections/impl/LList;

    iget-object p1, p0, Lantlr/collections/impl/LLEnumeration;->list:Lantlr/collections/impl/LList;

    iget-object p1, p1, Lantlr/collections/impl/LList;->head:Lantlr/collections/impl/LLCell;

    iput-object p1, p0, Lantlr/collections/impl/LLEnumeration;->cursor:Lantlr/collections/impl/LLCell;

    return-void
.end method


# virtual methods
.method public hasMoreElements()Z
    .locals 0

    iget-object p0, p0, Lantlr/collections/impl/LLEnumeration;->cursor:Lantlr/collections/impl/LLCell;

    if-eqz p0, :cond_0

    const/4 p0, 0x1

    return p0

    :cond_0
    const/4 p0, 0x0

    return p0
.end method

.method public nextElement()Ljava/lang/Object;
    .locals 2

    invoke-virtual {p0}, Lantlr/collections/impl/LLEnumeration;->hasMoreElements()Z

    move-result v0

    if-eqz v0, :cond_0

    iget-object v0, p0, Lantlr/collections/impl/LLEnumeration;->cursor:Lantlr/collections/impl/LLCell;

    iget-object v1, v0, Lantlr/collections/impl/LLCell;->next:Lantlr/collections/impl/LLCell;

    iput-object v1, p0, Lantlr/collections/impl/LLEnumeration;->cursor:Lantlr/collections/impl/LLCell;

    iget-object p0, v0, Lantlr/collections/impl/LLCell;->data:Ljava/lang/Object;

    return-object p0

    :cond_0
    new-instance p0, Ljava/util/NoSuchElementException;

    invoke-direct {p0}, Ljava/util/NoSuchElementException;-><init>()V

    throw p0
.end method
