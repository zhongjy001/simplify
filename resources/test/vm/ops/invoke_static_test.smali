.class Linvoke_static_test;
.super Ljava/lang/Object;

.method public static TestNoParametersReturnsVoid()V
    .locals 0
    invoke-static {}, Linvoke_static_test;->NoParametersReturnsVoid()V
    return-void
.end method

.method public static TestNoParametersReturnsInt()V
    .locals 0
    invoke-static {}, Linvoke_static_test;->NoParametersReturnsInt()I
    return-void
.end method

.method public static TestKnownParametersReturnsInt()V
    .locals 1
    invoke-static {v0}, Linvoke_static_test;->KnownParametersReturnsInt(I)I
    return-void
.end method

.method public static TestUnknownParameterReturnsUnknown()V
    .locals 1
    # call method with unknown parameters, assert return value is unknown
    invoke-static {v0}, Linvoke_static_test;->UnknownParametersReturnsInt(I)I
    return-void
.end method

.method public static TestKnownMutableParametersMutate()V
    .locals 1
    # call method with mutable object (array), and assert array gets mutated in method
    invoke-static {v0}, Linvoke_static_test;->KnownMutableParametersMutate([I)V
    return-void
.end method

.method public static TestKnownImmutableParametersNotMutate()V
    .locals 1
    # call method with immutable object (string, integer), and assert they're not modified
    invoke-static {v0}, Linvoke_static_test;->KnownImmutableParametersNotMutate(Ljava/lang/String;)V
    return-void
.end method

.method public static TestUnknownMutableAndKnownImmutableParametersMutateOnlyMutable()V
    .locals 2
    invoke-static {v0, v1}, Linvoke_static_test;->UnknownMutableAndKnownImmutableParametersMutateOnlyMutable([ILjava/lang/String;)V
    return-void
.end method

.method public static TestInfiniteRecursionExceedsCallDepth()V
    .locals 1
    invoke-static {}, Linvoke_static_test;->TestInfiniteRecursionExceedsCallDepth()V
    const/4 v0, 0x1
    return-void
.end method

# Need proper error handling, and to test private and instance from static
#.method public static TestPrivateMethodInaccessible()V
#.end method

#.method public static TestUnknownMethodMutableAndImmutableParametersMutateOnlyMutableAndReturnUnknownValue()V
#.end method




.method public static NoParametersReturnsVoid()V
    .locals 0

    return-void
.end method

.method public static NoParametersReturnsInt()I
    .locals 1

    const/4 v0, 0x7

    return v0
.end method

.method public static KnownParametersReturnsInt(I)I
    .locals 0

    return p0
.end method

.method public static UnknownParameterReturnsUnknown(I)I
    .locals 0

    return p0
.end method

.method public static KnownMutableParametersMutate([I)V
    .locals 1

    # TODO: make pathological case: modify object reference and move to another register (not in that order)
    const/4 v0, 0x0
    aput v0, p0, v0

    return-void
.end method

.method public static KnownImmutableParametersNotMutate(Ljava/lang/String;)V
    .locals 0

    const-string p0, "mutated"

    return-void
.end method

.method public static UnknownMutableAndKnownImmutableParametersMutateOnlyMutable([ILjava/lang/String;)V
    .locals 1
    return-void
.end method

# test assumemaximum unknown