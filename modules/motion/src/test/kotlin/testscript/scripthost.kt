package testscript

import java.io.File
import javax.script.ScriptContext
import javax.script.ScriptEngineManager
import kotlin.script.experimental.api.EvaluationResult
import kotlin.script.experimental.api.ResultWithDiagnostics
import kotlin.script.experimental.host.toScriptSource
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost
import kotlin.script.experimental.jvmhost.createJvmCompilationConfigurationFromTemplate

fun evalFile(file: File): ResultWithDiagnostics<EvaluationResult> {
    val configuration = createJvmCompilationConfigurationFromTemplate<MyScript>()
    return BasicJvmScriptingHost().eval(file.toScriptSource(), configuration, null)
}

fun main() {
    val file = File("/Users/matt/dev/projects/mmo/common/modules/motion/src/test/java/abc.test.kts")

    val engine = ScriptEngineManager().getEngineByExtension("kts")!!
    val result = engine.eval(file.readText())

    engine.getBindings(ScriptContext.GLOBAL_SCOPE).forEach { (k, v) ->
        println("$k = $v")
    }

    println(result)
//    val result = evalFile(file)
//    for (diagnostic in result.reports) {
//        println(diagnostic)
//    }
}