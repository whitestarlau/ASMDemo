package com.example.asm.effectwidget

import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.android.build.api.instrumentation.InstrumentationParameters
import org.objectweb.asm.ClassVisitor

abstract class EffectWidgetTransform : AsmClassVisitorFactory<InstrumentationParameters.None> {
    override fun createClassVisitor(
        classContext: ClassContext,
        nextClassVisitor: ClassVisitor
    ): ClassVisitor {
        val cv =  EffectWidgetClassVisitor(nextClassVisitor, classContext.currentClassData.className)

        return cv
    }

    /**
     * 标记有注解才处理。
     */
    override fun isInstrumentable(classData: ClassData): Boolean {
        val isInstrumentable = (classData.classAnnotations.contains("com.example.MyAnnotation"))
//        val isInstrumentable = (classData.superClasses.contains("android.view.View"))
        if (isInstrumentable) {
            println("isInstrumentable:${isInstrumentable} - ${classData.className}")
            println("isInstrumentable classData.classAnnotations:${classData.classAnnotations}.")
        }
        return isInstrumentable
    }
}
