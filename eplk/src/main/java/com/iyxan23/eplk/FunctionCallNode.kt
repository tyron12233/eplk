package com.iyxan23.eplk.nodes

import com.iyxan23.eplk.interpreter.RealtimeResult
import com.iyxan23.eplk.interpreter.Scope
import com.iyxan23.eplk.lexer.models.Position
import com.iyxan23.eplk.nodes.variable.VarAssignNode
import com.iyxan23.eplk.objects.EplkFunction
import com.iyxan23.eplk.objects.EplkObject
import com.iyxan23.eplk.objects.EplkVoid
import com.iyxan23.eplk.objects.EplkNull

class FunctionCallNode(
    val nodeToCall: Node,
    val arguments: Array<Node>,
    override val startPosition: Position,
    override val endPosition: Position
) : Node() {

    override fun visit(scope: Scope): RealtimeResult<EplkObject> {
        val result = RealtimeResult<EplkObject>()

        val nodeToCallResult = result.register(nodeToCall.visit(scope))
        if (result.shouldReturn) return result


        // create temporary variables from the names of the parameters with default values
        // this way, user can assign the arguments to the parameters
        val function = nodeToCallResult as EplkFunction

        val parameterScope = Scope("parameter scope of ${function.functionName}", parent = scope)

        function.parameterList.parameters.filter { it.defaultValue != null }.forEach {
            parameterScope.symbolTable.symbols[it.name] = EplkNull(parameterScope)
        }


        val argumentsMap = mutableMapOf<String, EplkObject>()

        arguments.forEachIndexed { index, argument ->



            // is default value?
            val isDefault = argument is VarAssignNode
            if (isDefault) {
                val varAssignNode = argument as VarAssignNode

                val variableName = varAssignNode.variableName
                val variableValue = varAssignNode.value

                val variableValueResult = result.register(variableValue.visit(parameterScope))
                if (result.shouldReturn) return result

                argumentsMap[variableName] = variableValueResult!!
            } else {
                function.parameterList.parameters[index].name
                val parameterName = function.parameterList.parameters[index].name

                val argumentResult = result.register(argument.visit(scope))
                if (result.shouldReturn) return result

                argumentsMap[parameterName] = argumentResult!!

            }
        }

        // Then call the function
        val functionResult = result.register(
            nodeToCallResult.call(argumentsMap, startPosition, endPosition)
        )

        if (result.isReturning) return result.success(result.returnValue ?: EplkVoid(scope))

        if (result.shouldReturn) return result

        return result.success(functionResult as EplkObject)
    }
}