package com.iyxan23.eplk.nodes.compose

import com.iyxan23.eplk.errors.EplkRuntimeError
import com.iyxan23.eplk.interpreter.RealtimeResult
import com.iyxan23.eplk.interpreter.Scope
import com.iyxan23.eplk.lexer.models.Position
import com.iyxan23.eplk.nodes.StatementsNode
import com.iyxan23.eplk.objects.EplkFunction
import com.iyxan23.eplk.objects.EplkInteger
import com.iyxan23.eplk.objects.EplkObject
import com.iyxan23.eplk.nodes.variable.ParameterListNode

open class ComposeFunction(
    override val parentScope: Scope,
    override val functionName: String,
    override val parameterList: ParameterListNode,
    override val statements: StatementsNode
): EplkFunction(parentScope, functionName, parameterList, statements) {

    override fun call(
        arguments: Map<String, EplkObject>,
        startPosition: Position,
        endPosition: Position
    ): RealtimeResult<EplkObject> {
        val result = RealtimeResult<EplkObject>()

        val composer = parentScope.searchVariable("\$composer") ?: parentScope.searchVariable("\$injectedComposer") ?: return result.failure(
            EplkRuntimeError(
                "ComposerNotFoundError",
                "Composer not found, are you sure you are using this function in a compose block?",
                startPosition,
                endPosition,
                parentScope
            )
        )

        val injectedArguments = arguments.toMutableMap().also {
            it["\$composer"] = composer
            it["\$changed"] = EplkInteger(0, parentScope)
        }

        return super.call(injectedArguments, startPosition, endPosition)
    }
}