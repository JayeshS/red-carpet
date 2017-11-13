package main.java.org.js

import redcarpet.Expression

class AlwaysFalseExpression : Expression() {
    override fun eval(input: Map<String, String>): Boolean {
        return false
    }
}