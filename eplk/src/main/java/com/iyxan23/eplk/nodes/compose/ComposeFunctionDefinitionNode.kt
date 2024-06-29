package com.iyxan23.eplk.nodes.compose

import androidx.compose.runtime.Composer
import com.iyxan23.eplk.interpreter.RealtimeResult
import com.iyxan23.eplk.interpreter.Scope
import com.iyxan23.eplk.lexer.models.Position
import com.iyxan23.eplk.nodes.FunctionDefinitionNode
import com.iyxan23.eplk.nodes.StatementsNode
import com.iyxan23.eplk.objects.EplkObject
import com.iyxan23.eplk.objects.EplkVoid
import com.iyxan23.eplk.nodes.variable.ParameterListNode
import com.iyxan23.eplk.objects.NativeEplkObject

class ComposeFunctionDefinitionNode(
    override val functionName: String,
    override val parameters: ParameterListNode,
    override val statements: StatementsNode,
    override val startPosition: Position,
    override val endPosition: Position,
) : FunctionDefinitionNode(functionName, parameters, statements, startPosition, endPosition) {

    override fun visit(scope: Scope): RealtimeResult<EplkObject> {
        scope.symbolTable.symbols[functionName] =
            ComposeFunction(scope, functionName, parameters, statements)

        return RealtimeResult<EplkObject>().success(EplkVoid(scope))
    }
}

fun Scope.getComposer(): Composer {
   val composer = symbolTable.symbols["\$composer"] ?: symbolTable.symbols["\$injectedComposer"]

    if (composer !is NativeEplkObject) {
        throw Exception("Composer is not defined in this scope")
    }

    return composer.`object` as Composer
}