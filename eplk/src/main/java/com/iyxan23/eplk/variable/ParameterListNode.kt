package com.iyxan23.eplk.nodes.variable

import com.iyxan23.eplk.interpreter.RealtimeResult
import com.iyxan23.eplk.interpreter.Scope
import com.iyxan23.eplk.lexer.models.Position
import com.iyxan23.eplk.nodes.Node
import com.iyxan23.eplk.objects.EplkObject

class ParameterListNode(
    val parameters: List<ParameterNode>,
    override val startPosition: Position,
    override val endPosition: Position
) : Node() {

    override fun toString(): String {
        return parameters.joinToString(", ") { it.toString() }
    }

    override fun visit(scope: Scope): RealtimeResult<EplkObject> {
        TODO("Not yet implemented")
    }
}