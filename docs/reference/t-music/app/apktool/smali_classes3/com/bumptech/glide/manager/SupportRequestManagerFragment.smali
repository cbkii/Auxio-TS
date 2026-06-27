.class public Lcom/bumptech/glide/manager/SupportRequestManagerFragment;
.super Landroid/support/v4/app/Fragment;
.source "SupportRequestManagerFragment.java"


# annotations
.annotation system Ldalvik/annotation/MemberClasses;
    value = {
        Lcom/bumptech/glide/manager/SupportRequestManagerFragment$a;
    }
.end annotation


# instance fields
.field private final ga:Lcom/bumptech/glide/manager/a;

.field private final ha:Lcom/bumptech/glide/manager/f;

.field private ia:Lc/a/a/a;

.field private final ja:Ljava/util/HashSet;
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "Ljava/util/HashSet<",
            "Lcom/bumptech/glide/manager/SupportRequestManagerFragment;",
            ">;"
        }
    .end annotation
.end field

.field private ka:Lcom/bumptech/glide/manager/SupportRequestManagerFragment;


# direct methods
.method public constructor <init>()V
    .locals 1

    .line 1
    new-instance v0, Lcom/bumptech/glide/manager/a;

    invoke-direct {v0}, Lcom/bumptech/glide/manager/a;-><init>()V

    invoke-direct {p0, v0}, Lcom/bumptech/glide/manager/SupportRequestManagerFragment;-><init>(Lcom/bumptech/glide/manager/a;)V

    return-void
.end method

.method public constructor <init>(Lcom/bumptech/glide/manager/a;)V
    .locals 2
    .annotation build Landroid/annotation/SuppressLint;
        value = {
            "ValidFragment"
        }
    .end annotation

    .line 2
    invoke-direct {p0}, Landroid/support/v4/app/Fragment;-><init>()V

    .line 3
    new-instance v0, Lcom/bumptech/glide/manager/SupportRequestManagerFragment$a;

    const/4 v1, 0x0

    invoke-direct {v0, p0, v1}, Lcom/bumptech/glide/manager/SupportRequestManagerFragment$a;-><init>(Lcom/bumptech/glide/manager/SupportRequestManagerFragment;Lcom/bumptech/glide/manager/g;)V

    iput-object v0, p0, Lcom/bumptech/glide/manager/SupportRequestManagerFragment;->ha:Lcom/bumptech/glide/manager/f;

    .line 4
    new-instance v0, Ljava/util/HashSet;

    invoke-direct {v0}, Ljava/util/HashSet;-><init>()V

    iput-object v0, p0, Lcom/bumptech/glide/manager/SupportRequestManagerFragment;->ja:Ljava/util/HashSet;

    .line 5
    iput-object p1, p0, Lcom/bumptech/glide/manager/SupportRequestManagerFragment;->ga:Lcom/bumptech/glide/manager/a;

    return-void
.end method

.method private a(Lcom/bumptech/glide/manager/SupportRequestManagerFragment;)V
    .locals 0

    .line 1
    iget-object p0, p0, Lcom/bumptech/glide/manager/SupportRequestManagerFragment;->ja:Ljava/util/HashSet;

    invoke-virtual {p0, p1}, Ljava/util/HashSet;->add(Ljava/lang/Object;)Z

    return-void
.end method

.method private b(Lcom/bumptech/glide/manager/SupportRequestManagerFragment;)V
    .locals 0

    .line 1
    iget-object p0, p0, Lcom/bumptech/glide/manager/SupportRequestManagerFragment;->ja:Ljava/util/HashSet;

    invoke-virtual {p0, p1}, Ljava/util/HashSet;->remove(Ljava/lang/Object;)Z

    return-void
.end method


# virtual methods
.method public onAttach(Landroid/app/Activity;)V
    .locals 1

    .line 1
    invoke-super {p0, p1}, Landroid/support/v4/app/Fragment;->onAttach(Landroid/app/Activity;)V

    .line 2
    invoke-static {}, Lcom/bumptech/glide/manager/e;->get()Lcom/bumptech/glide/manager/e;

    move-result-object p1

    invoke-virtual {p0}, Landroid/support/v4/app/Fragment;->getActivity()Landroid/support/v4/app/FragmentActivity;

    move-result-object v0

    invoke-virtual {v0}, Landroid/support/v4/app/FragmentActivity;->getSupportFragmentManager()Landroid/support/v4/app/FragmentManager;

    move-result-object v0

    invoke-virtual {p1, v0}, Lcom/bumptech/glide/manager/e;->a(Landroid/support/v4/app/FragmentManager;)Lcom/bumptech/glide/manager/SupportRequestManagerFragment;

    move-result-object p1

    iput-object p1, p0, Lcom/bumptech/glide/manager/SupportRequestManagerFragment;->ka:Lcom/bumptech/glide/manager/SupportRequestManagerFragment;

    .line 3
    iget-object p1, p0, Lcom/bumptech/glide/manager/SupportRequestManagerFragment;->ka:Lcom/bumptech/glide/manager/SupportRequestManagerFragment;

    if-eq p1, p0, :cond_0

    .line 4
    invoke-direct {p1, p0}, Lcom/bumptech/glide/manager/SupportRequestManagerFragment;->a(Lcom/bumptech/glide/manager/SupportRequestManagerFragment;)V

    :cond_0
    return-void
.end method

.method public onDestroy()V
    .locals 0

    .line 1
    invoke-super {p0}, Landroid/support/v4/app/Fragment;->onDestroy()V

    .line 2
    iget-object p0, p0, Lcom/bumptech/glide/manager/SupportRequestManagerFragment;->ga:Lcom/bumptech/glide/manager/a;

    invoke-virtual {p0}, Lcom/bumptech/glide/manager/a;->onDestroy()V

    return-void
.end method

.method public onDetach()V
    .locals 1

    .line 1
    invoke-super {p0}, Landroid/support/v4/app/Fragment;->onDetach()V

    .line 2
    iget-object v0, p0, Lcom/bumptech/glide/manager/SupportRequestManagerFragment;->ka:Lcom/bumptech/glide/manager/SupportRequestManagerFragment;

    if-eqz v0, :cond_0

    .line 3
    invoke-direct {v0, p0}, Lcom/bumptech/glide/manager/SupportRequestManagerFragment;->b(Lcom/bumptech/glide/manager/SupportRequestManagerFragment;)V

    const/4 v0, 0x0

    .line 4
    iput-object v0, p0, Lcom/bumptech/glide/manager/SupportRequestManagerFragment;->ka:Lcom/bumptech/glide/manager/SupportRequestManagerFragment;

    :cond_0
    return-void
.end method

.method public onLowMemory()V
    .locals 0

    .line 1
    invoke-super {p0}, Landroid/support/v4/app/Fragment;->onLowMemory()V

    .line 2
    iget-object p0, p0, Lcom/bumptech/glide/manager/SupportRequestManagerFragment;->ia:Lc/a/a/a;

    if-nez p0, :cond_0

    return-void

    .line 3
    :cond_0
    invoke-virtual {p0}, Lc/a/a/a;->onLowMemory()V

    const/4 p0, 0x0

    throw p0
.end method

.method public onStart()V
    .locals 0

    .line 1
    invoke-super {p0}, Landroid/support/v4/app/Fragment;->onStart()V

    .line 2
    iget-object p0, p0, Lcom/bumptech/glide/manager/SupportRequestManagerFragment;->ga:Lcom/bumptech/glide/manager/a;

    invoke-virtual {p0}, Lcom/bumptech/glide/manager/a;->onStart()V

    return-void
.end method

.method public onStop()V
    .locals 0

    .line 1
    invoke-super {p0}, Landroid/support/v4/app/Fragment;->onStop()V

    .line 2
    iget-object p0, p0, Lcom/bumptech/glide/manager/SupportRequestManagerFragment;->ga:Lcom/bumptech/glide/manager/a;

    invoke-virtual {p0}, Lcom/bumptech/glide/manager/a;->onStop()V

    return-void
.end method
