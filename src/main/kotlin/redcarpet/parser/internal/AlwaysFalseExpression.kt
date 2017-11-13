package redcarpet.parser.internal

class AlwaysFalseExpression : Expression() {
    override fun eval(input: Map<String, String>): Boolean {
        return false
    }
}