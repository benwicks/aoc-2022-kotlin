fun main() {
    fun buildFilesystemTree(input: List<String>): FilesystemNode.Directory {
        val filesystemTree: FilesystemNode.Directory = FilesystemNode.Directory(null, "/")
        var currentDirectory = filesystemTree

        for (line in input) {
            if (line.startsWith("$ cd ")) {
                val nextDirectoryName = line.split("$ cd ")[1]
                if (nextDirectoryName == "..") {
                    currentDirectory = currentDirectory.parentDirectory!!
                } else if (nextDirectoryName != "/") {
                    currentDirectory = currentDirectory.findSubDirectory(nextDirectoryName)
                }
            } else if (!line.startsWith("$ ls")) {
                // add contents to current directory
                currentDirectory.addChild(FilesystemNode.from(line, currentDirectory))
            }
        }
        return filesystemTree
    }

    fun part1(input: List<String>): Int {
        val filesystemTree = buildFilesystemTree(input)

        // Find all the directories with a total size of at most 100_000.
        // What is the sum of the total sizes of those directories?
        var sum = 0
        val maxSize = 100_000
        if (filesystemTree.size <= maxSize) {
            sum = filesystemTree.size
        }
        return sum + filesystemTree.childDirectoriesMatching { it.size <= maxSize }.sumOf { it.size }
    }

    fun part2(input: List<String>): Int {
        val filesystemTree = buildFilesystemTree(input)
        val needToFreeAtLeast = 30_000_000 + filesystemTree.size - 70_000_000

        // Find the smallest directory that, if deleted, would free up enough space on the filesystem to run the update.
        // What is the total size of that directory?
        return filesystemTree.childDirectoriesMatching {
            it.size >= needToFreeAtLeast
        }.minBy { it.size }.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 95_437)
    check(part2(testInput) == 24_933_642)

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}

sealed class FilesystemNode(open val name: String) {
    abstract val size: Int

    data class Directory(
            val parentDirectory: Directory?,
            override val name: String,
            private var children: MutableList<FilesystemNode> = mutableListOf(),
    ) : FilesystemNode(name) {
        fun addChild(child: FilesystemNode) {
            children += child
        }

        fun findSubDirectory(subDirectoryName: String): Directory {
            return children
                    .filterIsInstance<Directory>()
                    .first { it.name == subDirectoryName }
        }

        override val size
            get() = children.sumOf { it.size }

        override fun toString(): String {
            return "Name: $name, Size: $size"
        }

        fun childDirectoriesMatching(predicate: (Directory) -> Boolean): List<Directory> {
            val childDirectories = children.filterIsInstance<Directory>()
            val filtered = childDirectories.filter(predicate).toMutableList()
            for (index in 0 until filtered.size) {
                val matchingDirectory = filtered[index]
                filtered += matchingDirectory.childDirectoriesMatching(predicate)
            }
            // also recursively check children who didn't meet predicate (since they may have children that do)
            val childDirectoriesThatDoNotMeetPredicate = childDirectories.filterNot(predicate).toMutableList()
            for (child in childDirectoriesThatDoNotMeetPredicate) {
                filtered += child.childDirectoriesMatching(predicate)
            }
            return filtered
        }
    }

    data class File(
            override val name: String,
            override val size: Int,
    ) : FilesystemNode(name)

    companion object {
        fun from(line: String, currentDirectory: Directory): FilesystemNode {
            return if (line.startsWith("dir ")) {
                Directory(currentDirectory, line.split("dir ")[1])
            } else {
                val parts = line.split(" ")
                File(parts[1], parts[0].toInt())
            }
        }
    }
}