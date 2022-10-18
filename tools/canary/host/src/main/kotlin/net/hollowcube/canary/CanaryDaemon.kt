package net.hollowcube.canary

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import net.hollowcube.canary.script.evalToConfiguration
import net.hollowcube.canary.server.CanaryServer
import net.hollowcube.canary.test.*
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
    lateinit var server: CanaryServer

    private var testRoot: TestDescriptor? = null

    private var lastPos = Vec(5.0, 0.0, 0.0)

    val lock = Mutex()

    fun start() {
        server = CanaryServer()

        val testDir = Path.of("/Users/matt/dev/projects/mmo/common/tools/canary/host/src/test/java/")
        testRoot = DirectoryTestDescriptor(testDir).also { it.update() }

        val run = Command("run")
        run.setDefaultExecutor { sender, _ ->
            val locked = lock.tryLock()
            if (!locked) {
                sender.sendMessage("Already running tests")
                return@setDefaultExecutor
            }

            try {
                runAll()
            } finally {
                lock.unlock()
            }
        }
        server.process.command().register(run)

        val reload = Command("reload")
        reload.setDefaultExecutor { sender, _ ->
            testRoot!!.update()
        }
        server.process.command().register(reload)
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
            val children = Files.walk(path).use { s ->
                s.filter { path != it }.map(::readTestsRecursive).filter(Objects::nonNull).map { it!! }.toList()
            }
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
                tests.add(test)

                GlobalScope.launch {
                    test.createStructure(server.testInstance)
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

            is FunctionTestDescriptor2 -> {
                val entities = mutableListOf<Entity>()
                val env = object : Test.Env {
                    override fun <T : Point> global(point: T): T {
                        return point.add(test.origin) as T
                    }

                    override fun <T : Point> local(point: T): T {
                        return point.sub(test.origin) as T
                    }

                    override suspend fun spawnEntity(entity: Entity, pos: Pos) = suspendCoroutine { cont ->
                        entities.add(entity)
                        entity.setInstance(server.testInstance, global(pos)).thenRun {
                            cont.resumeWith(Result.success(Unit))
                        }
                    }

                    override suspend fun expect(condition: () -> Boolean) = suspendCoroutine { cont ->
                        server.testInstance.eventNode().addListener(
                            EventListener.builder(InstanceTickEvent::class.java)
                                .expireWhen {
                                    //todo should fail test immediately if an entity exits the test area
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
                test.config.fn.callSuspend(test.config.instance, env)
                test.setState(server.testInstance, State.PASSED)
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