package net.hollowcube.canary.test

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.hollowcube.canary.CanaryDaemon
import net.hollowcube.canary.Test
import net.hollowcube.canary.script.evalToConfiguration
import net.hollowcube.canary.server.block.setBoundingBox
import net.minestom.server.coordinate.Vec
import net.minestom.server.instance.Instance
import net.minestom.server.instance.block.Block
import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Stream
import kotlin.io.path.isDirectory
import kotlin.io.path.name
import kotlin.reflect.KFunction
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.functions

interface TestDescriptor {
    val name: String

    /**
     * Instructs the descriptor to update its internal state, looking for new children and removing old ones
     *
     * todo removal flow
     */
    fun update() {}
}

class ContainerTestDescriptor(
    override val name: String,
    val children: List<TestDescriptor>,
) : TestDescriptor

/**
 * Represents a directory with some child tests of either directories or files
 */
class DirectoryTestDescriptor(
    private val path: Path,
) : TestDescriptor {
    private var children = mapOf<Path, TestDescriptor>()

    override val name: String
        get() = path.fileName.toString()


    override fun update() {
        // Add existing and new entries
        val newChildren = mutableMapOf<Path, TestDescriptor>()
        println("$children")
        for (path in Files.walk(path).filter { path != it }.use(Stream<Path>::toList)) {
            println("Test path: $path")
            val existing = children[path]
            newChildren[path] = when {
                existing != null -> {
                    println("UPDATING ${existing.name}")
                    existing.also(TestDescriptor::update)
                }
                path.isDirectory() -> {
                    println("NEW DIRECTORY $path")
                    DirectoryTestDescriptor(path).also(TestDescriptor::update)
                }
                path.name.endsWith(".test.kts") -> {
                    println("NEW FILE $path")
                    FileTestDescriptor(path).also(TestDescriptor::update)
                }
                else -> continue
            }
        }

        // Remove old entries
        for ((path, child) in children) {
            if (path !in newChildren) {
                println("DELETE $child")
                //todo remove
            }
        }

        children = newChildren
    }
}

private var lastPos = Vec(5.0, 0.0, 0.0)

/**
 * Represents a file. Children are always function descriptors
 */
class FileTestDescriptor(
    private val path: Path,
) : TestDescriptor {
    override val name: String
        get() = path.fileName.toString()

    private var children = mapOf<String, FunctionTestDescriptor2>()

    override fun update() {
        val (scriptClass, scriptInstance) = evalToConfiguration(path)

        val newChildren = mutableMapOf<String, FunctionTestDescriptor2>()
        for (func in scriptClass.functions) {
            val name = func.name

            // Copy over existing but update the TestConfig to reference the new one
            val existing = children[name]
            if (existing != null) {
                println("UPDATE ${existing.name}")
                existing.config = existing.config.copy(instance = scriptInstance, fn = func)
                newChildren[name] = existing
                continue
            }

            // Create a new test
            val annotation = func.findAnnotation<Test>() ?: continue
            if (!func.isSuspend) {
                println("Test function $name is not a suspend function, skipping")
                continue
            }
            //todo check parameters

            println("NEW $name")
            val test = FunctionTestDescriptor2(
                name = name,
                config = TestConfig(
                    timeout = annotation.timeout,
                    instance = scriptInstance,
                    fn = func,
                ),
                lastPos.add(0.0, 40.0, 0.0)
            )
            lastPos = lastPos.add(Vec(0.0, 0.0, 10.0))
            newChildren[name] = test

            test.createStructure(CanaryDaemon.server.testInstance)
        }

        children = newChildren

        //todo diff removals
    }
}

class FunctionTestDescriptor(
    override val name: String,
    val timeout: Long,
    val instance: Any,
    val fn: KFunction<*>,
) : TestDescriptor

data class TestConfig(
    val instance: Any,
    val fn: KFunction<*>,
    val timeout: Long,
)

enum class State {
    UNKNOWN,
    PASSED,
    FAILED,
}

class FunctionTestDescriptor2(
    override val name: String,
    config: TestConfig,
    val pos: Vec
) : TestDescriptor {
    private val boundingBoxPos get() = pos.sub(0.0, 2.0, 0.0)

    var config: TestConfig = config
    val size = Vec(5.0)
    val origin = pos.add(2.0, 0.0, 2.0)

    fun setState(instance: Instance, state: State) {
        val block = when (state) {
            State.UNKNOWN -> Block.LIGHT_GRAY_CONCRETE
            State.PASSED -> Block.LIME_CONCRETE
            State.FAILED -> Block.RED_CONCRETE
        }

        for (x in -1..size.x.toInt()) {
            for (z in -1..size.z.toInt()) {
                if (x == -1 || x == size.x.toInt() || z == -1 || z == size.z.toInt()) {
                    instance.setBlock(pos.add(x.toDouble(), -1.0, z.toDouble()), block)
                }
            }
        }
    }

    fun createStructure(instance: Instance) {
        // Outline on ground
        setState(instance, State.UNKNOWN)

        // Bounding box
        instance.setBoundingBox(boundingBoxPos, pos, size)
    }


}
