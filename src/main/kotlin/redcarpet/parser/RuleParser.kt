package redcarpet.parser

import org.parboiled.Parboiled
import org.parboiled.parserunners.ReportingParseRunner
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import redcarpet.parser.internal.Expression
import redcarpet.parser.internal.Parser

class RuleParser {
    val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    fun createRule(input: String): Expression {
        val parser = ReportingParseRunner<Expression>(Parboiled.createParser(Parser::class.java).Expr())
        if (parser.run(input).hasErrors()) {
            logger.info("Could not parse {}", input)
            throw InvalidRuleException(parser.run(input).parseErrors.map { it.errorMessage }.joinToString(","))
        }
        return parser.run(input).resultValue
    }

}

class InvalidRuleException(message: String) : RuntimeException()
