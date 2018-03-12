package redcarpet.parser.internal

import net.jpountz.xxhash.XXHashFactory

object Hasher {
    val h: XXHashFactory = XXHashFactory.fastestInstance()
    val seed = 0x97a12e2f0a2175

    fun hash(input: String): Long {
        val inputBytes = input.toByteArray()
        return h.hash64().hash(inputBytes, 0, inputBytes.size, seed)
    }
}
