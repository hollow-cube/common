package net.hollowcube.canary.test

import kotlinx.coroutines.coroutineScope
import net.hollowcube.canary.server.block.setBoundingBox
import net.minestom.server.coordinate.Vec
import net.minestom.server.instance.Instance
import net.minestom.server.instance.batch.AbsoluteBlockBatch
import net.minestom.server.instance.batch.BatchOption
import net.minestom.server.instance.block.Block
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/*
What makes up a test
- ID (inc path)
- Test function
- Script instance
- Location in test instance




 */

/**
 * A test placed within a Minecraft world
 */
class WorldTest(
    val pos: Vec,
    val size: Vec,
) {
    private val boundingBoxPos get() = pos.sub(0.0, 2.0, 0.0)
    private lateinit var undoBatch: AbsoluteBlockBatch

    suspend fun createStructure(instance: Instance) = suspendCoroutine { cont ->
//        val batch = AbsoluteBlockBatch(BatchOption().setCalculateInverse(true))

        // Outline on ground
        for (x in -1..size.x.toInt()) {
            for (z in -1..size.z.toInt()) {
                if (x == -1 || x == size.x.toInt() || z == -1 || z == size.z.toInt()) {
                    instance.setBlock(pos.add(x.toDouble(), -1.0, z.toDouble()), Block.YELLOW_CONCRETE)
                }
            }
        }

        // Bounding box
        instance.setBoundingBox(boundingBoxPos, pos, size)
        cont.resume(Unit)

//        undoBatch = batch.apply(instance) { cont.resume(Unit) }!!
    }

    suspend fun removeStructure(instance: Instance) = suspendCoroutine { cont ->
//        undoBatch.apply(instance) { cont.resume(Unit) }
        cont.resume(Unit)
    }


}