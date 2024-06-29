package com.iyxan23.eplk.objects

import com.iyxan23.eplk.interpreter.Scope
import com.iyxan23.eplk.objects.EplkObject

class NativeEplkObject(
    val `object`: Any,
    override val parentScope: Scope
) : EplkObject(parentScope) {

    override val objectName: String
        get() = `object`.toString()

    override fun toString(): String {
        return `object`.toString()
    }
}