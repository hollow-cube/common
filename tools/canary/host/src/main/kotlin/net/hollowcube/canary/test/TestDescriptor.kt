package net.hollowcube.canary.test

import kotlin.reflect.KFunction

interface TestDescriptor {
    val name: String
}

class ContainerTestDescriptor(
    override val name: String,
    val children: List<TestDescriptor>,
) : TestDescriptor

class FunctionTestDescriptor(
    override val name: String,
    val timeout: Long,
    val instance: Any,
    val fn: KFunction<*>,
) : TestDescriptor
