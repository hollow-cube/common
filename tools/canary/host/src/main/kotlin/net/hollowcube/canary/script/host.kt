package net.hollowcube.canary.script

import java.io.File
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.reflect.KClass
import kotlin.reflect.full.functions
import kotlin.script.experimental.api.*
import kotlin.script.experimental.host.toScriptSource
import kotlin.script.experimental.jvm.dependenciesFromCurrentContext
import kotlin.script.experimental.jvm.jvm
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost
import kotlin.script.experimental.jvmhost.createJvmCompilationConfigurationFromTemplate
import kotlin.script.experimental.jvmhost.createJvmEvaluationConfigurationFromTemplate


fun evalFile(file: Path): ResultWithDiagnostics<EvaluationResult> {
    val compilationConfiguration = createJvmCompilationConfigurationFromTemplate<CanaryScript> {
        jvm {
            defaultImports.put(listOf("net.hollowcube.canary.*"))

            dependenciesFromCurrentContext(wholeClasspath = true)
        }
    }
    val evaluationConfiguration = createJvmEvaluationConfigurationFromTemplate<CanaryScript> {

    }
    return BasicJvmScriptingHost().eval(file.toFile().toScriptSource(), compilationConfiguration, evaluationConfiguration)
}

fun evalToConfiguration(file: Path): Pair<KClass<*>, Any> {
    val result = evalFile(file)
    for (diagnostic in result.reports) {
        println(diagnostic)
    }
    val ret = result.valueOrThrow().returnValue
    return Pair(ret.scriptClass!!, ret.scriptInstance!!)
}
