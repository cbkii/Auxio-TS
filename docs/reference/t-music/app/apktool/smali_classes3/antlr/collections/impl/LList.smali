.class public Lantlr/collections/impl/LList;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Lantlr/collections/List;
.implements Lantlr/collections/Stack;


# instance fields
.field public head:Lantlr/collections/impl/LLCell;

.field public length:I

.field public tail:Lantlr/collections/impl/LLCell;


# direct methods
.method public constructor <init>()V
    .locals 1

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    const/4 v0, 0x0

    iput-object v0, p0, Lantlr/collections/impl/LList;->head:Lantlr/collections/impl/LLCell;

    iput-object v0, p0, Lantlr/collections/impl/LList;->tail:Lantlr/collections/impl/LLCell;

    const/4 v0, 0x0

    iput v0, p0, Lantlr/collections/impl/LList;->length:I

    return-void
.end method


# virtual methods
.method public add(Ljava/lang/Object;)V
    .locals 0

    invoke-virtual {p0, p1}, Lantlr/collections/impl/LList;->append(Ljava/lang/Object;)V

    return-void
.end method

.method public append(Ljava/lang/Object;)V
    .locals 3

    new-instance v0, Lantlr/collections/impl/LLCell;

    invoke-direct {v0, p1}, Lantlr/collections/impl/LLCell;-><init>(Ljava/lang/Object;)V

    iget p1, p0, Lantlr/collections/impl/LList;->length:I

    const/4 v1, 0x1

    if-nez p1, :cond_0

    iput-object v0, p0, Lantlr/collections/impl/LList;->tail:Lantlr/collections/impl/LLCell;

    iput-object v0, p0, Lantlr/collections/impl/LList;->head:Lantlr/collections/impl/LLCell;

    iput v1, p0, Lantlr/collections/impl/LList;->length:I

    goto :goto_0

    :cond_0
    iget-object v2, p0, Lantlr/collections/impl/LList;->tail:Lantlr/collections/impl/LLCell;

    iput-object v0, v2, Lantlr/collections/impl/LLCell;->next:Lantlr/collections/impl/LLCell;

    iput-object v0, p0, Lantlr/collections/impl/LList;->tail:Lantlr/collections/impl/LLCell;

    add-int/2addr p1, v1

    iput p1, p0, Lantlr/collections/impl/LList;->length:I

    :goto_0
    return-void
.end method

.method public deleteHead()Ljava/lang/Object;
    .locals 2

    iget-object v0, p0, Lantlr/collections/impl/LList;->head:Lantlr/collections/impl/LLCell;

    if-eqz v0, :cond_0

    iget-object v1, v0, Lantlr/collections/impl/LLCell;->data:Ljava/lang/Object;

    iget-object v0, v0, Lantlr/collections/impl/LLCell;->next:Lantlr/collections/impl/LLCell;

    iput-object v0, p0, Lantlr/collections/impl/LList;->head:Lantlr/collections/impl/LLCell;

    iget v0, p0, Lantlr/collections/impl/LList;->length:I

    add-int/lit8 v0, v0, -0x1

    iput v0, p0, Lantlr/collections/impl/LList;->length:I

    return-object v1

    :cond_0
    new-instance p0, Ljava/util/NoSuchElementException;

    invoke-direct {p0}, Ljava/util/NoSuchElementException;-><init>()V

    throw p0
.end method

.method public elementAt(I)Ljava/lang/Object;
    .locals 1

    iget-object p0, p0, Lantlr/collections/impl/LList;->head:Lantlr/collections/impl/LLCell;

    const/4 v0, 0x0

    :goto_0
    if-eqz p0, :cond_1

    if-ne p1, v0, :cond_0

    iget-object p0, p0, Lantlr/collections/impl/LLCell;->data:Ljava/lang/Object;

    return-object p0

    :cond_0
    add-int/lit8 v0, v0, 0x1

    iget-object p0, p0, Lantlr/collections/impl/LLCell;->next:Lantlr/collections/impl/LLCell;

    goto :goto_0

    :cond_1
    new-instance p0, Ljava/util/NoSuchElementException;

    invoke-direct {p0}, Ljava/util/NoSuchElementException;-><init>()V

    throw p0
.end method

.method public elements()Ljava/util/Enumeration;
    .locals 1

    new-instance v0, Lantlr/collections/impl/LLEnumeration;

    invoke-direct {v0, p0}, Lantlr/collections/impl/LLEnumeration;-><init>(Lantlr/collections/impl/LList;)V

    return-object v0
.end method

.method public height()I
    .locals 0

    iget p0, p0, Lantlr/collections/impl/LList;->length:I

    return p0
.end method

.method public includes(Ljava/lang/Object;)Z
    .locals 1

    iget-object p0, p0, Lantlr/collections/impl/LList;->head:Lantlr/collections/impl/LLCell;

    :goto_0
    if-eqz p0, :cond_1

    iget-object v0, p0, Lantlr/collections/impl/LLCell;->data:Ljava/lang/Object;

    invoke-virtual {v0, p1}, Ljava/lang/Object;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_0

    const/4 p0, 0x1

    return p0

    :cond_0
    iget-object p0, p0, Lantlr/collections/impl/LLCell;->next:Lantlr/collections/impl/LLCell;

    goto :goto_0

    :cond_1
    const/4 p0, 0x0

    return p0
.end method

.method public insertHead(Ljava/lang/Object;)V
    .locals 2

    iget-object v0, p0, Lantlr/collections/impl/LList;->head:Lantlr/collections/impl/LLCell;

    new-instance v1, Lantlr/collections/impl/LLCell;

    invoke-direct {v1, p1}, Lantlr/collections/impl/LLCell;-><init>(Ljava/lang/Object;)V

    iput-object v1, p0, Lantlr/collections/impl/LList;->head:Lantlr/collections/impl/LLCell;

    iget-object p1, p0, Lantlr/collections/impl/LList;->head:Lantlr/collections/impl/LLCell;

    iput-object v0, p1, Lantlr/collections/impl/LLCell;->next:Lantlr/collections/impl/LLCell;

    iget v0, p0, Lantlr/collections/impl/LList;->length:I

    add-int/lit8 v0, v0, 0x1

    iput v0, p0, Lantlr/collections/impl/LList;->length:I

    iget-object v0, p0, Lantlr/collections/impl/LList;->tail:Lantlr/collections/impl/LLCell;

    if-nez v0, :cond_0

    iput-object p1, p0, Lantlr/collections/impl/LList;->tail:Lantlr/collections/impl/LLCell;

    :cond_0
    return-void
.end method

.method public length()I
    .locals 0

    iget p0, p0, Lantlr/collections/impl/LList;->length:I

    return p0
.end method

.method public pop()Ljava/lang/Object;
    .locals 0

    invoke-virtual {p0}, Lantlr/collections/impl/LList;->deleteHead()Ljava/lang/Object;

    move-result-object p0

    return-object p0
.end method

.method public push(Ljava/lang/Object;)V
    .locals 0

    invoke-virtual {p0, p1}, Lantlr/collections/impl/LList;->insertHead(Ljava/lang/Object;)V

    return-void
.end method

.method public top()Ljava/lang/Object;
    .locals 0

    iget-object p0, p0, Lantlr/collections/impl/LList;->head:Lantlr/collections/impl/LLCell;

    if-eqz p0, :cond_0

    iget-object p0, p0, Lantlr/collections/impl/LLCell;->data:Ljava/lang/Object;

    return-object p0

    :cond_0
    new-instance p0, Ljava/util/NoSuchElementException;

    invoke-direct {p0}, Ljava/util/NoSuchElementException;-><init>()V

    throw p0
.end method
