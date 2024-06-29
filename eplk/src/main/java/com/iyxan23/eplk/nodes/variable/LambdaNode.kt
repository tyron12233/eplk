package com.iyxan23.eplk.nodes.variable

import com.iyxan23.eplk.interpreter.RealtimeResult
import com.iyxan23.eplk.interpreter.Scope
import com.iyxan23.eplk.lexer.models.Position
import com.iyxan23.eplk.nodes.Node
import com.iyxan23.eplk.nodes.StatementsNode
import com.iyxan23.eplk.objects.EplkFunction
import com.iyxan23.eplk.objects.EplkObject

class LambdaNode(
    val parameterList: ParameterListNode,
    val statements: StatementsNode,
    override val startPosition: Position,
    override val endPosition: Position
): Node() {
    override fun visit(scope: Scope): RealtimeResult<EplkObject> {
        val anonymousName = "lambda$${scope.symbolTable.symbols.size}"
        val function = EplkFunction(scope, anonymousName , parameterList, statements)

        return RealtimeResult<EplkObject>().success(function)
    }
}