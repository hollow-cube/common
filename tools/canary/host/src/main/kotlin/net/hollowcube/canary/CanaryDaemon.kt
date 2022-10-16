package net.hollowcube.canary

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.hollowcube.canary.script.evalToConfiguration
import net.hollowcube.canary.server.CanaryServer
import net.hollowcube.canary.test.ContainerTestDescriptor
import net.hollowcube.canary.test.FunctionTestDescriptor
import net.hollowcube.canary.test.TestDescriptor
import net.hollowcube.canary.test.WorldTest
import net.minestom.server.command.builder.Command
import net.minestom.server.coordinate.Point
import net.minestom.server.coordinate.Pos
import net.minestom.server.coordinate.Vec
import net.minestom.server.entity.Entity
import net.minestom.server.event.EventListener
import net.minestom.server.event.instance.InstanceTickEvent
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.io.path.isDirectory
import kotlin.reflect.full.callSuspend
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.functions

object CanaryDaemon {
    private lateinit var server: CanaryServer

    private var testRoot: TestDescriptor? = null

    private var lastPos = Vec(5.0, 0.0, 0.0)

    fun start() {
        server = CanaryServer()

        val testDir = Path.of("/Users/matt/dev/projects/mmo/common/tools/canary/host/src/test/java/")
        testRoot = readTestsRecursive(testDir)

        val run = Command("run")
        run.setDefaultExecutor { sender, _ ->
            runAll()
        }
        server.process.command().register(run)
    }

    fun runAll() {
        val root = testRoot
        if (root == null) {
            println("No tests found")
            return
        }

        GlobalScope.launch {
            executeTestRecursive("", root)
        }
    }

    private fun readTestsRecursive(path: Path): TestDescriptor? {
        println(path)
        if (path.isDirectory()) {
            val children = Files.walk(path).use { s -> s.filter { path != it }.map(::readTestsRecursive).filter(Objects::nonNull).map { it!! }.toList() }
            return ContainerTestDescriptor(path.fileName.toString(), children)
        } else {
            val fileName = path.fileName.toString()
            if (!fileName.endsWith(".test.kts")) return null

            val (scriptClass, scriptInstance) = evalToConfiguration(path)

            val tests = mutableListOf<TestDescriptor>()
            for (func in scriptClass.functions) {
                val name = func.name
                val annotation = func.findAnnotation<Test>() ?: continue
                if (!func.isSuspend) {
                    println("Test function $name is not a suspend function, skipping")
                    continue
                }
                //todo check parameters

                val timeout = annotation.timeout
                tests.add(FunctionTestDescriptor(
                    name = name,
                    timeout = timeout,
                    instance = scriptInstance,
                    fn = func,
                ))

                val wt = WorldTest(lastPos.add(0.0, 40.0, 0.0), Vec(5.0, 5.0, 5.0))
                lastPos = lastPos.add(Vec(0.0, 0.0, 10.0))
                GlobalScope.launch {
                    wt.createStructure(server.testInstance)
                }
            }

            return ContainerTestDescriptor(fileName.removeSuffix(".test.kts"), tests)
        }
    }

    private suspend fun executeTestRecursive(path: String, test: TestDescriptor) {
        when (test) {
            is ContainerTestDescriptor -> {
                for (child in test.children) {
                    executeTestRecursive("$path/${test.name}", child)
                }
            }
            is FunctionTestDescriptor -> {
                val entities = mutableListOf<Entity>()
                val env = object : Test.Env {
                    override fun <T : Point> global(point: T): T {
                        TODO("Not yet implemented")
                    }

                    override fun <T : Point> local(point: T): T {
                        TODO("Not yet implemented")
                    }

                    override suspend fun spawnEntity(entity: Entity, pos: Pos) = suspendCoroutine { cont ->
                        entities.add(entity)
                        entity.setInstance(server.testInstance, pos).thenRun {
                            cont.resumeWith(Result.success(Unit))
                        }
                    }

                    override suspend fun expect(condition: () -> Boolean) = suspendCoroutine { cont ->
                        server.testInstance.eventNode().addListener(
                            EventListener.builder(InstanceTickEvent::class.java)
                            .expireWhen {
                                if (condition()) {
                                    cont.resume(Unit)
                                    true
                                } else false
                            }
                            .build())
                    }
                }

                val name = "$path/${test.name}"
                println("RUN $name")
                test.fn.callSuspend(test.instance, env)
                println("DONE $name")
                for (entity in entities) {
                    entity.remove()
                }
            }
        }
    }




    /*
    Startup
    - Start Minestom server
      - Create known test instance
    - Load all tests
      - Reload flow on modify
        - Reset instance completely
        - Place all tests
    - Run all tests, run single test, etc
      - cleanup

     */

}