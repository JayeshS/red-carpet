package redcarpet.parser.internal

import org.assertj.core.api.Java6Assertions.assertThat
import org.assertj.core.data.Offset.offset
import org.junit.Before
import org.junit.Test
import java.util.*

class PercentExpressionTest {

    lateinit var p: PercentExpression
    val varName = "foo"
    val rand = Random()

    @Before
    fun `set up`() {
        p = PercentExpression()
        p.varname = varName
    }

    @Test
    fun `percent matches fairly accurately`() {
        val sampleSize = 1000
        p.percent = 10
        val results = IntRange(1, sampleSize).map { p.eval(mapOf(Pair(varName, random()))) }
        assertThat(results.count { it }).isCloseTo(sampleSize / p.percent, offset(25))
    }

    fun random(): String {
        val byte = ByteArray(32)
        rand.nextBytes(byte)
        return Base64.getUrlEncoder().encodeToString(byte)
    }
}
