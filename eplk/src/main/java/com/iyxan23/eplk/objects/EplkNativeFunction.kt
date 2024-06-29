package com.iyxan23.eplk.objects

import com.iyxan23.eplk.errors.EplkRuntimeError
import com.iyxan23.eplk.interpreter.RealtimeResult
import com.iyxan23.eplk.interpreter.Scope
import com.iyxan23.eplk.lexer.models.Position
import com.iyxan23.eplk.nodes.StatementsNode
import com.iyxan23.eplk.nodes.types.NULL_NODE
import com.iyxan23.eplk.nodes.variable.ParameterListNode
import com.iyxan23.eplk.nodes.variable.ParameterNode



// Note: What I meant by native is that this function is made in the java/kotlin language
class EplkNativeFunction(
    scope: Scope,
    override val functionName: String,
    override val parameterList: ParameterListNode,
    val functionCallback:
   (
        scope: Scope,
        arguments: Map<String, EplkObject>,
        startPosition: Position,
        endPosition: Position,
        function: EplkNativeFunction,
    ) -> RealtimeResult<EplkObject>

) : EplkFunction(scope, functionName, parameterList, StatementsNode(arrayOf())) {
    override val objectName: String = "Native function"
    override fun toString(): String = "Native function $functionName(${parameterList.parameters.joinToString(", ")})"

    override fun call(
        arguments: Map<String, EplkObject>,
        startPosition: Position,
        endPosition: Position
    ): RealtimeResult<EplkObject> = functionCallback(parentScope, arguments, startPosition, endPosition, this)


    companion object {
        fun createParameters(vararg parameters: String): ParameterListNode {
            val position = Position(0, 0, 0, "", "")
            val parameterNodes = parameters.map { ParameterNode(it, NULL_NODE, position, position) }
            return ParameterListNode(parameterNodes, position, position)
        }
    }
}