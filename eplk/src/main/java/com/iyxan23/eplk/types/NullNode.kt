package com.iyxan23.eplk.nodes.types

import com.iyxan23.eplk.interpreter.RealtimeResult
import com.iyxan23.eplk.interpreter.Scope
import com.iyxan23.eplk.lexer.models.Position
import com.iyxan23.eplk.lexer.models.Token
import com.iyxan23.eplk.nodes.Node
import com.iyxan23.eplk.objects.EplkObject
import com.iyxan23.eplk.objects.EplkNull

val NULL_NODE = NullNode(Position(0, 0, 0, "", ""), Position(0, 0, 0, "", ""))

data class NullNode(
    override val startPosition: Position,
    override val endPosition: Position
) : Node() {

    override fun visit(scope: Scope): RealtimeResult<EplkObject> {
        return RealtimeResult<EplkObject>().success(EplkNull(scope))
    }
}

