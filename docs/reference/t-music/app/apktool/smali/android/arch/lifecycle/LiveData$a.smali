.class Landroid/arch/lifecycle/LiveData$a;
.super Landroid/arch/lifecycle/LiveData$b;
.source "LiveData.java"


# annotations
.annotation system Ldalvik/annotation/EnclosingClass;
    value = Landroid/arch/lifecycle/LiveData;
.end annotation

.annotation system Ldalvik/annotation/InnerClass;
    accessFlags = 0x2
    name = "a"
.end annotation

.annotation system Ldalvik/annotation/Signature;
    value = {
        "Landroid/arch/lifecycle/LiveData<",
        "TT;>.b;"
    }
.end annotation


# instance fields
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
    iput-object p1, p0, Landroid/arch/lifecycle/LiveData$a;->this$0:Landroid/arch/lifecycle/LiveData;

    .line 2
    invoke-direct {p0, p1, p2}, Landroid/arch/lifecycle/LiveData$b;-><init>(Landroid/arch/lifecycle/LiveData;Landroid/arch/lifecycle/m;)V

    return-void
.end method


# virtual methods
.method ya()Z
    .locals 0

    const/4 p0, 0x1

    return p0
.end method
