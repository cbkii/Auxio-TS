.class Landroid/support/v7/graphics/Palette$Builder$1;
.super Landroid/os/AsyncTask;
.source "Palette.java"


# annotations
.annotation system Ldalvik/annotation/EnclosingMethod;
    value = Landroid/support/v7/graphics/Palette$Builder;->generate(Landroid/support/v7/graphics/Palette$PaletteAsyncListener;)Landroid/os/AsyncTask;
.end annotation

.annotation system Ldalvik/annotation/InnerClass;
    accessFlags = 0x0
    name = null
.end annotation

.annotation system Ldalvik/annotation/Signature;
    value = {
        "Landroid/os/AsyncTask<",
        "Landroid/graphics/Bitmap;",
        "Ljava/lang/Void;",
        "Landroid/support/v7/graphics/Palette;",
        ">;"
    }
.end annotation


# instance fields
.field final synthetic this$0:Landroid/support/v7/graphics/Palette$Builder;

.field final synthetic val$listener:Landroid/support/v7/graphics/Palette$PaletteAsyncListener;


# direct methods
.method constructor <init>(Landroid/support/v7/graphics/Palette$Builder;Landroid/support/v7/graphics/Palette$PaletteAsyncListener;)V
    .locals 0

    .line 1
    iput-object p1, p0, Landroid/support/v7/graphics/Palette$Builder$1;->this$0:Landroid/support/v7/graphics/Palette$Builder;

    iput-object p2, p0, Landroid/support/v7/graphics/Palette$Builder$1;->val$listener:Landroid/support/v7/graphics/Palette$PaletteAsyncListener;

    invoke-direct {p0}, Landroid/os/AsyncTask;-><init>()V

    return-void
.end method


# virtual methods
.method protected varargs doInBackground([Landroid/graphics/Bitmap;)Landroid/support/v7/graphics/Palette;
    .locals 1
    .annotation build Landroid/support/annotation/Nullable;
    .end annotation

    .line 2
    :try_start_0
    iget-object p0, p0, Landroid/support/v7/graphics/Palette$Builder$1;->this$0:Landroid/support/v7/graphics/Palette$Builder;

    invoke-virtual {p0}, Landroid/support/v7/graphics/Palette$Builder;->generate()Landroid/support/v7/graphics/Palette;

    move-result-object p0
    :try_end_0
    .catch Ljava/lang/Exception; {:try_start_0 .. :try_end_0} :catch_0

    return-object p0

    :catch_0
    move-exception p0

    const-string p1, "Palette"

    const-string v0, "Exception thrown during async generate"

    .line 3
    invoke-static {p1, v0, p0}, Landroid/util/Log;->e(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I

    const/4 p0, 0x0

    return-object p0
.end method

.method protected bridge synthetic doInBackground([Ljava/lang/Object;)Ljava/lang/Object;
    .locals 0
    .annotation build Landroid/support/annotation/Nullable;
    .end annotation

    .line 1
    check-cast p1, [Landroid/graphics/Bitmap;

    invoke-virtual {p0, p1}, Landroid/support/v7/graphics/Palette$Builder$1;->doInBackground([Landroid/graphics/Bitmap;)Landroid/support/v7/graphics/Palette;

    move-result-object p0

    return-object p0
.end method

.method protected onPostExecute(Landroid/support/v7/graphics/Palette;)V
    .locals 0
    .param p1    # Landroid/support/v7/graphics/Palette;
        .annotation build Landroid/support/annotation/Nullable;
        .end annotation
    .end param

    .line 2
    iget-object p0, p0, Landroid/support/v7/graphics/Palette$Builder$1;->val$listener:Landroid/support/v7/graphics/Palette$PaletteAsyncListener;

    invoke-interface {p0, p1}, Landroid/support/v7/graphics/Palette$PaletteAsyncListener;->onGenerated(Landroid/support/v7/graphics/Palette;)V

    return-void
.end method

.method protected bridge synthetic onPostExecute(Ljava/lang/Object;)V
    .locals 0
    .param p1    # Ljava/lang/Object;
        .annotation build Landroid/support/annotation/Nullable;
        .end annotation
    .end param

    .line 1
    check-cast p1, Landroid/support/v7/graphics/Palette;

    invoke-virtual {p0, p1}, Landroid/support/v7/graphics/Palette$Builder$1;->onPostExecute(Landroid/support/v7/graphics/Palette;)V

    return-void
.end method
