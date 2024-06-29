package com.iyxan23.eplk.interpreter

import com.iyxan23.eplk.errors.EplkRuntimeError
import com.iyxan23.eplk.lexer.models.Position
import com.iyxan23.eplk.objects.*
import com.iyxan23.eplk.objects.EplkNativeFunction.Companion.createParameters

object StandardDefinitions {
    fun generateScope(name: String, parentScope: Scope? = null): Scope {
        val scope = Scope(name, parent = parentScope)

        scope.symbolTable.symbols["println"] = EplkNativeFunction(scope, "println", createParameters("text"))
        { functionScope: Scope,
          arguments: Map<String, EplkObject>,
          startPosition,
          endPosition, func ->

            val result = RealtimeResult<EplkObject>()
            val `object` = arguments["text"] ?: return@EplkNativeFunction result.failure(
                EplkRuntimeError(
                    "ArgumentNotFound",
                    "Argument text is not found in the arguments provided",
                    startPosition,
                    endPosition,
                    functionScope
                )
            )

            println(`object`.toString())

            return@EplkNativeFunction result.success(EplkVoid(functionScope))
        }

        scope.symbolTable.symbols["print"] = EplkNativeFunction(scope, "print", createParameters("text"))
        { functionScope: Scope,
          arguments: Map<String, EplkObject>,
          startPosition: Position,
          endPosition: Position, func ->

            val result = RealtimeResult<EplkObject>()
            val `object` = arguments["text"] ?: return@EplkNativeFunction result.failure(
                EplkRuntimeError(
                    "ArgumentNotFound",
                    "Argument text is not found in the arguments provided",
                    startPosition,
                    endPosition,
                    functionScope
                )
            )
            print(`object`.toString())

            return@EplkNativeFunction result.success(EplkVoid(functionScope))
        }

//        scope.symbolTable.variables["typeof"] = EplkNativeFunction(scope, "typeof", arrayOf("obj"))
//        { functionScope: Scope,
//          arguments: Array<EplkObject>,
//          _: Position,
//          _: Position ->
//
//            val result = RealtimeResult<EplkObject>()
//            val obj = arguments[0]
//
//            println(obj.objectName)
//
//            return@EplkNativeFunction result.success(EplkVoid(functionScope))
//        }
//
//        scope.symbolTable.variables["random"] = EplkNativeFunction(scope, "random", arrayOf("min", "max"))
//        { functionScope: Scope,
//          arguments: Array<EplkObject>,
//          startPosition: Position,
//          endPosition: Position ->
//            val result = RealtimeResult<EplkObject>()
//
//            val min = arguments[0]
//            val max = arguments[1]
//
//            if (min !is EplkInteger) {
//                return@EplkNativeFunction result.failure(EplkTypeError(
//                    "The argument min provided is expected to be an Integer, got ${min.objectName} instead",
//                    startPosition, endPosition, scope
//                ))
//            }
//
//            if (max !is EplkInteger) {
//                return@EplkNativeFunction result.failure(EplkTypeError(
//                    "The argument max provided is expected to be an Integer, got ${min.objectName} instead",
//                    startPosition, endPosition, scope
//                ))
//            }
//
//            return@EplkNativeFunction result.success(EplkInteger((min.value..max.value).random(), functionScope))
//        }

        return scope
    }
}