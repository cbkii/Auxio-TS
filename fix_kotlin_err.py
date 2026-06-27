with open("app/src/main/java/org/oxycblt/auxio/headunit/root/RootStateHolder.kt", "r") as f:
    content = f.read()

# Fix `@Synchronized` on `runRootCommandSync`. It might be that the compiler doesn't like it on properties or something, but `runRootCommandSync` is a function.
# Wait, look at the error: `RootStateHolder.kt:81:5 This annotation is not applicable to target 'member property with backing field'`
# Let's see what is at line 81.
