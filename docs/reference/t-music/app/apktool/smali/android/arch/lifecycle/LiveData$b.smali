.class abstract Landroid/arch/lifecycle/LiveData$b;
.super Ljava/lang/Object;
.source "LiveData.java"


# annotations
.annotation system Ldalvik/annotation/EnclosingClass;
    value = Landroid/arch/lifecycle/LiveData;
.end annotation

.annotation system Ldalvik/annotation/InnerClass;
    accessFlags = 0x402
    name = "b"
.end annotation


# instance fields
.field Ma:I

.field mActive:Z

.field final mObserver:Landroid/arch/lifecycle/m;
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "Landroid/arch/lifecycle/m<",
            "-TT;>;"
        }
    .end annotation
.end field

.field final synthetic this$0:Landroid/arch/lifecycle/LiveData;


# direct methods
.method constructor <init>(Landroid/arch/lifecycle/LiveData;Landroid/arch/lifecycle/m;)V
    .locals 0
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "(",
            "Landroid/arch/lifecycle/m<",
            "-TT;>;)V"
        }
    .end annotation

    .line 1
    iput-object p1, p0, Landroid/arch/lifecycle/LiveData$b;->this$0:Landroid/arch/lifecycle/LiveData;

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    const/4 p1, -0x1

    .line 2
    iput p1, p0, Landroid/arch/lifecycle/LiveData$b;->Ma:I

    .line 3
    iput-object p2, p0, Landroid/arch/lifecycle/LiveData$b;->mObserver:Landroid/arch/lifecycle/m;

    return-void
.end method


# virtual methods
.method g(Landroid/arch/lifecycle/e;)Z
    .locals 0

    const/4 p0, 0x0

    return p0
.end method

.method xa()V
    .locals 0

    return-void
.end method

.method y(Z)V
    .locals 4

    .line 1
    iget-boolean v0, p0, Landroid/arch/lifecycle/LiveData$b;->mActive:Z

    if-ne p1, v0, :cond_0

    return-void

    .line 2
    :cond_0
    iput-boolean p1, p0, Landroid/arch/lifecycle/LiveData$b;->mActive:Z

    .line 3
    iget-object p1, p0, Landroid/arch/lifecycle/LiveData$b;->this$0:Landroid/arch/lifecycle/LiveData;

    invoke-static {p1}, Landroid/arch/lifecycle/LiveData;->access$300(Landroid/arch/lifecycle/LiveData;)I

    move-result p1

    const/4 v0, 0x1

    if-nez p1, :cond_1

    move p1, v0

    goto :goto_0

    :cond_1
    const/4 p1, 0x0

    .line 4
    :goto_0
    iget-object v1, p0, Landroid/arch/lifecycle/LiveData$b;->this$0:Landroid/arch/lifecycle/LiveData;

    invoke-static {v1}, Landroid/arch/lifecycle/LiveData;->access$300(Landroid/arch/lifecycle/LiveData;)I

    move-result v2

    iget-boolean v3, p0, Landroid/arch/lifecycle/LiveData$b;->mActive:Z

    if-eqz v3, :cond_2

    goto :goto_1

    :cond_2
    const/4 v0, -0x1

    :goto_1
    add-int/2addr v2, v0

    invoke-static {v1, v2}, Landroid/arch/lifecycle/LiveData;->access$302(Landroid/arch/lifecycle/LiveData;I)I

    if-eqz p1, :cond_3

    .line 5
    iget-boolean p1, p0, Landroid/arch/lifecycle/LiveData$b;->mActive:Z

    if-eqz p1, :cond_3

    .line 6
    iget-object p1, p0, Landroid/arch/lifecycle/LiveData$b;->this$0:Landroid/arch/lifecycle/LiveData;

    invoke-virtual {p1}, Landroid/arch/lifecycle/LiveData;->onActive()V

    .line 7
    :cond_3
    iget-object p1, p0, Landroid/arch/lifecycle/LiveData$b;->this$0:Landroid/arch/lifecycle/LiveData;

    invoke-static {p1}, Landroid/arch/lifecycle/LiveData;->access$300(Landroid/arch/lifecycle/LiveData;)I

    move-result p1

    if-nez p1, :cond_4

    iget-boolean p1, p0, Landroid/arch/lifecycle/LiveData$b;->mActive:Z

    if-nez p1, :cond_4

    .line 8
    iget-object p1, p0, Landroid/arch/lifecycle/LiveData$b;->this$0:Landroid/arch/lifecycle/LiveData;

    invoke-virtual {p1}, Landroid/arch/lifecycle/LiveData;->onInactive()V

    .line 9
    :cond_4
    iget-boolean p1, p0, Landroid/arch/lifecycle/LiveData$b;->mActive:Z

    if-eqz p1, :cond_5

    .line 10
    iget-object p1, p0, Landroid/arch/lifecycle/LiveData$b;->this$0:Landroid/arch/lifecycle/LiveData;

    invoke-static {p1, p0}, Landroid/arch/lifecycle/LiveData;->access$400(Landroid/arch/lifecycle/LiveData;Landroid/arch/lifecycle/LiveData$b;)V

    :cond_5
    return-void
.end method

.method abstract ya()Z
.end method
