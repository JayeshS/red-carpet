package org.js

abstract class Expression {

    abstract fun eval(input: Map<String, String>): Boolean
}