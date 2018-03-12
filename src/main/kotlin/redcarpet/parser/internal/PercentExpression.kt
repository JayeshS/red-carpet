package redcarpet.parser.internal

class PercentExpression : Expression() {
    var varname: String = ""
    var percent: Int = 0

    override fun eval(input: Map<String, String>): Boolean = input.any { entry ->
        entry.key == varname && percentMatches(entry)
    }

    private fun percentMatches(entry: Map.Entry<String, String>): Boolean {
        val hash = Hasher.hash(entry.value)
        return Math.abs(hash % 100) < percent
    }

    companion object Builder {

        fun setVarname(varname: String, percExpr: Expression): Boolean {
            if (percExpr is PercentExpression) {
                percExpr.varname = varname
                return true
            }
            return false
        }

        fun setPerc(perc: String, percExpr: Expression): Boolean {
            if (percExpr is PercentExpression) {
                percExpr.percent = perc.toInt()
                return true
            }
            return false
        }

    }

}
