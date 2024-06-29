package com.iyxan23.eplk.objects

import com.iyxan23.eplk.errors.runtime.EplkCallError
import com.iyxan23.eplk.interpreter.RealtimeResult
import com.iyxan23.eplk.interpreter.Scope
import com.iyxan23.eplk.interpreter.SymbolTable
import com.iyxan23.eplk.lexer.models.Position
import com.iyxan23.eplk.nodes.StatementsNode
import com.iyxan23.eplk.nodes.variable.ParameterListNode
import com.iyxan23.eplk.nodes.variable.ParameterNode

open class EplkFunction(
    scope: Scope,
    open val functionName: String,
    open val parameterList: ParameterListNode,
    open val statements: StatementsNode,
) : EplkObject(scope) {

    override val objectName: String = "Function"
    override fun toString(): String = "Function $functionName(${parameterList.parameters.joinToString(", ")})"

    override fun call(
        arguments: Map<String, EplkObject>,
        startPosition: Position,
        endPosition: Position
    ): RealtimeResult<EplkObject> {
        val result = RealtimeResult<EplkObject>()

        val defaultParameterSize = parameterList.parameters.filter { it.defaultValue != null }.size

        if (arguments.size < parameterList.parameters.size - defaultParameterSize) {
            return result.failure(EplkCallError(
                "Too little arguments provided, expected ${parameterList.parameters.size - defaultParameterSize}, got ${arguments.size}",
                startPosition,
                endPosition,
                parentScope
            ))
        }


        if (arguments.size > parameterList.parameters.size) {
            return result.failure(EplkCallError(
                "Too many arguments provided, expected ${parameterList.parameters.size}, got ${arguments.size}",
                startPosition,
                endPosition,
                parentScope
            ))
        }

        val functionScope = Scope(
            "function $functionName(${parameterList.parameters.joinToString(", ")})",
            SymbolTable(),
            parentScope,
            startPosition
        )

        parameterList.parameters.forEachIndexed { index: Int, parameter: ParameterNode ->

            if (parameter.defaultValue != null) {
                // If the argument is not provided, use the default value
                if (arguments[parameter.name] == null) {
                    val defaultValueResult = result.register(parameter.defaultValue.visit(functionScope))
                    if (result.shouldReturn) return result

                    functionScope.symbolTable.symbols[parameter.name] = defaultValueResult!!
                    return@forEachIndexed
                }

                // If the argument is provided, use the argument
                functionScope.symbolTable.symbols[parameter.name] = arguments[parameter.name]!!
                return@forEachIndexed
            }

            functionScope.symbolTable.symbols[parameter.name] = arguments[parameter.name]!!;
        }

        // Alright let's execute the expression
        val expressionResult = result.register(statements.visit(functionScope))
        if (result.shouldReturn) return result

        return result.success(expressionResult!!)
    }
}