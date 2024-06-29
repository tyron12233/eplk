package com.iyxan23.eplk.objects

import com.iyxan23.eplk.interpreter.Scope
import com.iyxan23.eplk.objects.EplkObject

class EplkNull(
    override val parentScope: Scope
) : EplkObject(parentScope) {

    override val objectName: String
        get() = "NULL"

    override fun toString(): String {
        return "NULL"
    }
}