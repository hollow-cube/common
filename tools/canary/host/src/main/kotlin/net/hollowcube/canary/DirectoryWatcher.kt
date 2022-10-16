package net.hollowcube.canary

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.nio.file.FileSystems
import java.nio.file.Path
import java.nio.file.StandardWatchEventKinds
import kotlin.io.path.isDirectory

private val ALL_EVENTS = arrayOf(StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY)

/**
 * Wrapper around WatchService that watches a set of directories recursively, appropriately handling
 * new directories added or removed, returning events normalized to update actions for files.
 */
class DirectoryWatcher {
    private val watcher = FileSystems.getDefault().newWatchService()

    fun register(directory: Path) {
        assert(directory.isDirectory())

        val wk = directory.register(watcher, *ALL_EVENTS)

    }

    suspend fun poll() = withContext(Dispatchers.IO) {
        val wk = watcher.take() // Blocking
        try {
            val events = mutableListOf<Path>()
            for (event in wk.pollEvents()) {
                val path = event.context() as Path? ?: continue
                println("Event: ${event.kind()}, path: $path")

                /*
                Events
                - edit and save file -> modify
                - create file in directory -> create
                - create directory -> create
                - create file in directory -> modify
                - delete file in directory -> modify
                - create file in inner directory -> nothing
                - delete file -> delete
                 */

                when (event.kind()) {
                    StandardWatchEventKinds.ENTRY_CREATE -> {
                        // New directories need to be watched immediately
                        if (path.isDirectory()) {
                            //todo do i need to watch recursively
                        }
                        // If is file, add to events
                        // If is directory, add watcher to the directory
                    }
                    StandardWatchEventKinds.ENTRY_DELETE -> {
                        // If is directory, delete watcher from the directory
                        // todo how does this trigger with inner events
                    }
                    StandardWatchEventKinds.ENTRY_MODIFY -> {
                        // Directory modification triggers when modifying files inside, but we watch recursively so this is already handled
                        if (path.isDirectory()) continue

                        // Add file to events
                        events.add(path)
                    }
                }
            }
            return@withContext events
        } finally {
            wk.reset()
        }
    }

    fun tick() {

        val wk = watcher.take()
    }

}
