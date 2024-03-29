package com.example.asm.effectwidget

import com.android.build.api.instrumentation.FramesComputationMode
import com.android.build.api.instrumentation.InstrumentationScope
import com.android.build.api.variant.AndroidComponentsExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class EffectWidgetPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val androidComponents = target.extensions.getByType(AndroidComponentsExtension::class.java)
        println("androidComponents:${androidComponents.toString()}")
        androidComponents.onVariants { variant ->
            variant.instrumentation.transformClassesWith(
                EffectWidgetTransform::class.java,
                InstrumentationScope.PROJECT
            ) {}
            variant.instrumentation.setAsmFramesComputationMode(
                FramesComputationMode.COMPUTE_FRAMES_FOR_INSTRUMENTED_METHODS
            )
        }
    }
}