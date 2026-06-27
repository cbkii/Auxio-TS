.class public Lantlr/CSharpBlockFinishingInfo;
.super Ljava/lang/Object;
.source ""


# instance fields
.field public generatedAnIf:Z

.field public generatedSwitch:Z

.field public needAnErrorClause:Z

.field public postscript:Ljava/lang/String;


# direct methods
.method public constructor <init>()V
    .locals 1

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    const/4 v0, 0x0

    iput-object v0, p0, Lantlr/CSharpBlockFinishingInfo;->postscript:Ljava/lang/String;

    const/4 v0, 0x0

    iput-boolean v0, p0, Lantlr/CSharpBlockFinishingInfo;->generatedSwitch:Z

    iput-boolean v0, p0, Lantlr/CSharpBlockFinishingInfo;->generatedSwitch:Z

    const/4 v0, 0x1

    iput-boolean v0, p0, Lantlr/CSharpBlockFinishingInfo;->needAnErrorClause:Z

    return-void
.end method

.method public constructor <init>(Ljava/lang/String;ZZZ)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lantlr/CSharpBlockFinishingInfo;->postscript:Ljava/lang/String;

    iput-boolean p2, p0, Lantlr/CSharpBlockFinishingInfo;->generatedSwitch:Z

    iput-boolean p3, p0, Lantlr/CSharpBlockFinishingInfo;->generatedAnIf:Z

    iput-boolean p4, p0, Lantlr/CSharpBlockFinishingInfo;->needAnErrorClause:Z

    return-void
.end method
