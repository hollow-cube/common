package net.hollowcube.canary.server.block

import net.minestom.server.coordinate.Point
import net.minestom.server.instance.block.Block
import net.minestom.server.instance.block.BlockHandler
import net.minestom.server.tag.Tag
import net.minestom.server.utils.NamespaceID

fun Block.Setter.setBoundingBox(blockPos: Point, point: Point, size: Point) {
    val offset = point.sub(blockPos)
    val block = BoundingBoxHandler.BLOCK
        .withTag(BoundingBoxHandler.Tags.PosX, offset.blockX())
        .withTag(BoundingBoxHandler.Tags.PosY, offset.blockY())
        .withTag(BoundingBoxHandler.Tags.PosZ, offset.blockZ())
        .withTag(BoundingBoxHandler.Tags.SizeX, size.blockX())
        .withTag(BoundingBoxHandler.Tags.SizeY, size.blockY())
        .withTag(BoundingBoxHandler.Tags.SizeZ, size.blockZ())
    setBlock(blockPos, block)
}

class BoundingBoxHandler private constructor() : BlockHandler {

    override fun getNamespaceId() = NamespaceID.from("canary:bounding_box")

    override fun getBlockEntityAction() = 7.toByte()

    override fun getBlockEntityTags() = listOf<Tag<*>>(
        Tags.Author, Tags.IgnoreEntities, Tags.Integrity, Tags.Metadata, Tags.Mirror, Tags.Mode, Tags.Name,
        Tags.PosX, Tags.PosY, Tags.PosZ, Tags.SizeX, Tags.SizeY, Tags.SizeZ, Tags.Powered, Tags.Rotation,
        Tags.Seed, Tags.ShowBoundingBox,
    )

    object Tags {
        val Author: Tag<String> = Tag.String("author")
        val IgnoreEntities: Tag<Byte> = Tag.Byte("ignoreEntities")
        val Integrity: Tag<Float> = Tag.Float("integrity")
        val Metadata: Tag<String> = Tag.String("metadata")
        val Mirror: Tag<String> = Tag.String("mirror")
        val Mode: Tag<String> = Tag.String("mode")
        val Name: Tag<String> = Tag.String("name")
        val PosX: Tag<Int> = Tag.Integer("posX")
        val PosY: Tag<Int> = Tag.Integer("posY")
        val PosZ: Tag<Int> = Tag.Integer("posZ")
        val Powered: Tag<Byte> = Tag.Byte("powered")
        val Rotation: Tag<String> = Tag.String("rotation")
        val Seed: Tag<Long> = Tag.Long("seed")
        val ShowBoundingBox: Tag<Byte> = Tag.Byte("showboundingbox")
        val SizeX: Tag<Int> = Tag.Integer("sizeX")
        val SizeY: Tag<Int> = Tag.Integer("sizeY")
        val SizeZ: Tag<Int> = Tag.Integer("sizeZ")
    }

    companion object {
        val BLOCK: Block = Block.STRUCTURE_BLOCK
            .withTag(Tags.Author, "?")
            .withTag(Tags.IgnoreEntities, 0.toByte())
            .withTag(Tags.Integrity, 1f)
            .withTag(Tags.Metadata, "")
            .withTag(Tags.Mirror, "NONE")
            .withTag(Tags.Mode, "SAVE")
            .withTag(Tags.Name, "test123")
            .withTag(Tags.PosX, 0)
            .withTag(Tags.PosY, 1)
            .withTag(Tags.PosZ, 0)
            .withTag(Tags.Powered, 0.toByte())
            .withTag(Tags.Rotation, "NONE")
            .withTag(Tags.Seed, 0L)
            .withTag(Tags.ShowBoundingBox, 1.toByte())
            .withHandler(BoundingBoxHandler())
    }
}