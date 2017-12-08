package redcarpet.parser.internal

class AlwaysTrueExpression : Expression() {
    override fun eval(input: Map<String, String>): Boolean {
        return true
    }
}