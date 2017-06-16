package org.js

import org.parboiled.BaseParser
import org.parboiled.Parboiled
import org.parboiled.Rule
import org.parboiled.annotations.BuildParseTree
import org.parboiled.parserunners.ReportingParseRunner
import org.parboiled.support.ParseTreeUtils

@BuildParseTree
open class Parser : BaseParser<Expression>() {

    open fun Expr() = Sequence(factor(), ZeroOrMore(
            push(ConjunctionExpression()),
            Conjunction(),
            ConjunctionExpression.setConjunction(match(), peek()),
            factor(),
            ConjunctionExpression.setExprs(pop(), peek(), pop(1))))

    open fun Conjunction() = FirstOf(or(), and())

    open fun factor() = FirstOf(Condition(), Parens())

    open fun Condition() = FirstOf(Percentage(), InCondition())

    open fun InCondition() = Sequence(
            push(InListExpression()),
            VarName(),
            InListExpression.setVarname(match(), peek()),
            colon(),
            list())

    open fun Percentage() = Sequence(
            push(PercentExpression()),
            VarName(),
            PercentExpression.setVarname(match(), peek()),
            colon(),
            Digits(),
            PercentExpression.setPerc(match(), peek()),
            '%'
    )

    open fun Parens(): Rule = Sequence('(', Expr(), ')')

    open fun list() = Sequence(
            Digits(),
            InListExpression.addToList(match(), peek()),
            ZeroOrMore(
                    COMMA(),
                    Digits(),
                    InListExpression.addToList(match(), peek())
            )
    )

    open fun Digits() = OneOrMore(CharRange('0', '9'))

    open fun Alnum() = OneOrMore(AnyOf("0123456789abcdefghijklmnopqrstuvwxyz"))

    open fun VarName(): Rule = Sequence(
            alphabets(),
            ZeroOrMore(Alnum())
    )

    open fun or() = fromStringLiteral(" or ")
    open fun and() = fromStringLiteral(" and ")

    open fun alphabets(): Rule = OneOrMore(alphabet())

    open fun alphabet(): Rule = CharRange('a', 'z')

    open fun colon() = Sequence(fromCharLiteral(':'), WhiteSpace())

    open fun COMMA() = Sequence(AnyOf(","), WhiteSpace())

    open fun WhiteSpace(): Rule {
        return ZeroOrMore(AnyOf(" \t"))
    }
}

fun run(): Unit {
    val input: String = "p3:123"
    val input1: String = "userId:123,2123,3111 or (lang:en_US,en_UK and p4:1%)"

    val prsr: Parser = Parboiled.createParser(Parser::class.java)
    val reportingParseRunner = ReportingParseRunner<Expression>(prsr.Expr())
    val result = reportingParseRunner.run(input)
    if (result.hasErrors()) println(result.parseErrors)
    print(ParseTreeUtils.printNodeTree(result))

    val evalResult = result.resultValue.eval(linkedMapOf(Pair("p1", "foo"), Pair("p3", "1asdfadadfadfa")))
    println("Eval result: $evalResult")
}

fun main(args: Array<String>) {
    run()
}
