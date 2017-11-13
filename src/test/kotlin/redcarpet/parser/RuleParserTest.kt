package redcarpet.parser

import org.amshove.kluent.shouldEqual
import org.junit.Test
import org.parboiled.Parboiled
import org.parboiled.parserunners.ReportingParseRunner
import redcarpet.parser.internal.Expression
import redcarpet.parser.internal.Parser
import java.util.*

class RuleParserTest {

    @Test
    fun percent() {
        val input = "p3:100%"

        val evalResult = evaluate(input, linkedMapOf(Pair("p3", random())))
        evalResult shouldEqual true
    }

    @Test
    fun inListFalse() {
        val input = "p3:\"123\""

        val evalResult = evaluate(input, linkedMapOf(Pair("p1", "foo"), Pair("p3", "foobar")))
        evalResult shouldEqual false
    }

    @Test
    fun inListTrue() {
        val input = "(p3:\"123\")"

        val evalResult = evaluate(input, linkedMapOf(Pair("p1", "foo"), Pair("p3", "123")))
        evalResult shouldEqual true
    }

    @Test
    fun orConjunctionFirstTrue() {
        val input = "p1:\"1\" or p2:\"2\""
        val evalResult = evaluate(input, linkedMapOf(Pair("p1", "1"), Pair("p3", "123")))
        evalResult shouldEqual true
    }

    @Test
    fun orConjunctionSecondTrue() {
        val input = "p1:\"1\" or p2:\"2\""
        val evalResult = evaluate(input, linkedMapOf(Pair("p1", "foo"), Pair("p2", "2")))
        evalResult shouldEqual true
    }

    @Test
    fun orConjunctionLong() {
        val input = "p1:\"1\" or p2:\"2\" or p3:\"3\""
        val evalResult = evaluate(input, linkedMapOf(Pair("p1", "foo"), Pair("p3", "3")))
        evalResult shouldEqual true
    }

    @Test
    fun andConjunctionPartialMatch() {
        val input = "p1:\"1\" and p2:\"2\""
        val evalResult = evaluate(input, linkedMapOf(Pair("p1", "1"), Pair("p2", "foo")))
        evalResult shouldEqual false
    }

    @Test
    fun andConjunction() {
        val input = "p1:\"1\" and p2:2"
        val evalResult = evaluate(input, linkedMapOf(Pair("p1", "1"), Pair("p2", "2")))
        evalResult shouldEqual true
    }

    @Test
    fun orAndAndConjunction() {
        val input = "p1:\"1\" or (p2:2 and p3:3)"
        val evalResult = evaluate(input, linkedMapOf(Pair("p1", "1")))
        evalResult shouldEqual true
    }

    @Test
    fun orAndAndConjunctionParenthesisMatch() {
        val input = "p1:\"1\" or (p2:\"2\" and p3:\"3\")"
        val evalResult = evaluate(input, linkedMapOf(Pair("p2", "2"), Pair("p3", "3")))
        evalResult shouldEqual true
    }

    @Test
    fun parensMismatch() {
        val input = "p1:\"1\" or (p2:2 and p3:3)"
        val evalResult = evaluate(input, linkedMapOf(Pair("p1", "2"), Pair("p2", "2"), Pair("p3", "4")))
        evalResult shouldEqual false
    }

    @Test
    fun listAndPercentPercentMatch() {
        val input = "p1: \"1\" or p2: 100%"
        val evalResult = evaluate(input, linkedMapOf(Pair("p2", "2"), Pair("p3", "3")))
        evalResult shouldEqual true
    }

    @Test
    fun multiListAndPercentListMatch() {
        val input = "p1:\"1\", \"2\", \"3\" or p2:0%"
        val evalResult = evaluate(input, linkedMapOf(Pair("p1", "3"), Pair("p3", "3")))
        evalResult shouldEqual true
    }

    @Test
    fun multiList() {
        val input = "p1:\"1\", \"2\", \"3\""
        val evalResult = evaluate(input, linkedMapOf(Pair("p1", "3")))
        evalResult shouldEqual true
    }

    @Test
    fun multiListWithSpaces() {
        val input = "p1:\"foo 1\", \"foo 2\", \"3\""
        val evalResult = evaluate(input, linkedMapOf(Pair("p1", "foo 2")))
        evalResult shouldEqual true
    }

    private fun evaluate(input: String, inputMap: LinkedHashMap<String, String>): Boolean {
        val prsr: Parser = Parboiled.createParser(Parser::class.java)
        val reportingParseRunner = ReportingParseRunner<Expression>(prsr.Expr())
        val result = reportingParseRunner.run(input)
        if (result.hasErrors()) println(result.parseErrors)
        return result.resultValue.eval(inputMap)
    }

    private fun random(): String = UUID.randomUUID().toString()
}