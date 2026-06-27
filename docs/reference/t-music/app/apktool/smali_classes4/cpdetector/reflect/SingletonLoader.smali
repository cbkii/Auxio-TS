.class public final Lcpdetector/reflect/SingletonLoader;
.super Ljava/lang/Object;
.source ""


# static fields
.field public static instance:Lcpdetector/reflect/SingletonLoader;


# instance fields
.field public dummyParameters:[Ljava/lang/Object;


# direct methods
.method public static constructor <clinit>()V
    .locals 0

    return-void
.end method

.method public constructor <init>()V
    .locals 1

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    const/4 v0, 0x0

    new-array v0, v0, [Ljava/lang/Object;

    iput-object v0, p0, Lcpdetector/reflect/SingletonLoader;->dummyParameters:[Ljava/lang/Object;

    return-void
.end method

.method public static getInstance()Lcpdetector/reflect/SingletonLoader;
    .locals 1

    sget-object v0, Lcpdetector/reflect/SingletonLoader;->instance:Lcpdetector/reflect/SingletonLoader;

    if-nez v0, :cond_0

    new-instance v0, Lcpdetector/reflect/SingletonLoader;

    invoke-direct {v0}, Lcpdetector/reflect/SingletonLoader;-><init>()V

    sput-object v0, Lcpdetector/reflect/SingletonLoader;->instance:Lcpdetector/reflect/SingletonLoader;

    :cond_0
    sget-object v0, Lcpdetector/reflect/SingletonLoader;->instance:Lcpdetector/reflect/SingletonLoader;

    return-object v0
.end method


# virtual methods
.method public newInstance(Ljava/lang/Class;)Ljava/lang/Object;
    .locals 8

    invoke-virtual {p1}, Ljava/lang/Class;->getDeclaredMethods()[Ljava/lang/reflect/Method;

    move-result-object v0

    const/4 v1, 0x0

    const/4 v2, 0x0

    move v3, v1

    move-object v4, v2

    :goto_0
    array-length v5, v0

    if-ge v3, v5, :cond_1

    aget-object v5, v0, v3

    invoke-virtual {v5}, Ljava/lang/reflect/Method;->getModifiers()I

    move-result v6

    and-int/lit8 v7, v6, 0x8

    if-eqz v7, :cond_0

    and-int/lit8 v6, v6, 0x1

    if-eqz v6, :cond_0

    invoke-virtual {v5}, Ljava/lang/reflect/Method;->getParameterTypes()[Ljava/lang/Class;

    move-result-object v6

    array-length v6, v6

    if-nez v6, :cond_0

    invoke-virtual {v5}, Ljava/lang/reflect/Method;->getReturnType()Ljava/lang/Class;

    move-result-object v6

    if-ne v6, p1, :cond_0

    invoke-virtual {v5}, Ljava/lang/reflect/Method;->getName()Ljava/lang/String;

    move-result-object v6

    invoke-virtual {v6}, Ljava/lang/String;->toLowerCase()Ljava/lang/String;

    move-result-object v6

    const-string v7, "instance"

    invoke-virtual {v6, v7}, Ljava/lang/String;->indexOf(Ljava/lang/String;)I

    move-result v6

    const/4 v7, -0x1

    if-eq v6, v7, :cond_0

    :try_start_0
    iget-object v6, p0, Lcpdetector/reflect/SingletonLoader;->dummyParameters:[Ljava/lang/Object;

    invoke-virtual {v5, v2, v6}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v4
    :try_end_0
    .catch Ljava/lang/IllegalArgumentException; {:try_start_0 .. :try_end_0} :catch_2
    .catch Ljava/lang/IllegalAccessException; {:try_start_0 .. :try_end_0} :catch_1
    .catch Ljava/lang/reflect/InvocationTargetException; {:try_start_0 .. :try_end_0} :catch_0

    goto :goto_1

    :catch_0
    move-exception v5

    invoke-virtual {v5}, Ljava/lang/reflect/InvocationTargetException;->printStackTrace()V

    goto :goto_1

    :catch_1
    move-exception v5

    invoke-virtual {v5}, Ljava/lang/IllegalAccessException;->printStackTrace()V

    goto :goto_1

    :catch_2
    move-exception v5

    invoke-virtual {v5}, Ljava/lang/IllegalArgumentException;->printStackTrace()V

    :cond_0
    :goto_1
    add-int/lit8 v3, v3, 0x1

    goto :goto_0

    :cond_1
    if-nez v4, :cond_4

    invoke-virtual {p1}, Ljava/lang/Class;->getConstructors()[Ljava/lang/reflect/Constructor;

    move-result-object p0

    :goto_2
    array-length v0, p0

    if-ge v1, v0, :cond_4

    aget-object v0, p0, v1

    invoke-virtual {v0}, Ljava/lang/reflect/Constructor;->getParameterTypes()[Ljava/lang/Class;

    move-result-object v2

    array-length v2, v2

    if-nez v2, :cond_3

    invoke-virtual {v0}, Ljava/lang/reflect/Constructor;->getModifiers()I

    move-result v2

    const/4 v3, 0x1

    and-int/2addr v2, v3

    if-nez v2, :cond_2

    :try_start_1
    invoke-virtual {v0, v3}, Ljava/lang/reflect/Constructor;->setAccessible(Z)V

    :cond_2
    invoke-virtual {p1}, Ljava/lang/Class;->newInstance()Ljava/lang/Object;

    move-result-object v4
    :try_end_1
    .catch Ljava/lang/SecurityException; {:try_start_1 .. :try_end_1} :catch_3

    :catch_3
    :cond_3
    add-int/lit8 v1, v1, 0x1

    goto :goto_2

    :cond_4
    if-nez v4, :cond_5

    sget-object p0, Ljava/lang/System;->err:Ljava/io/PrintStream;

    const-string v0, "Unable to instantiate: "

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {p1}, Ljava/lang/Class;->getName()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p1, ": no singleton method, no public default constructor."

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_5
    return-object v4
.end method

.method public newInstance(Ljava/lang/String;)Ljava/lang/Object;
    .locals 0

    invoke-static {p1}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object p1

    invoke-virtual {p0, p1}, Lcpdetector/reflect/SingletonLoader;->newInstance(Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object p0

    return-object p0
.end method
