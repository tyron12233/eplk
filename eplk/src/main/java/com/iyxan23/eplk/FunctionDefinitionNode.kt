package com.iyxan23.eplk.nodes

import com.iyxan23.eplk.interpreter.RealtimeResult
import com.iyxan23.eplk.interpreter.Scope
import com.iyxan23.eplk.lexer.models.Position
import com.iyxan23.eplk.objects.EplkFunction
import com.iyxan23.eplk.objects.EplkObject
import com.iyxan23.eplk.objects.EplkVoid
import com.iyxan23.eplk.nodes.variable.ParameterListNode

open class FunctionDefinitionNode(
    open val functionName: String,
    open val parameters: ParameterListNode,
    open val statements: StatementsNode,
    override val startPosition: Position,
    override val endPosition: Position = statements.endPosition
) : Node() {

    override fun visit(scope: Scope): RealtimeResult<EplkObject> {
        scope.symbolTable.symbols[functionName] =
            EplkFunction(scope, functionName, parameters, statements)

        return RealtimeResult<EplkObject>().success(EplkVoid(scope))
    }
}