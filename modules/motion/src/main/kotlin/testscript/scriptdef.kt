package testscript

import kotlin.script.experimental.annotations.KotlinScript
import kotlin.script.experimental.api.ScriptCompilationConfiguration

@KotlinScript(
    fileExtension = "test.kts",
    compilationConfiguration = MyScriptConfiguration::class,
)
abstract class MyScript

//https://kotlinlang.org/docs/custom-script-deps-tutorial.html#create-a-script-definition
object MyScriptConfiguration : ScriptCompilationConfiguration({

    //todo default imports
})