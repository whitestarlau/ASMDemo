package com.example.asm.effectwidget

import com.sun.org.apache.bcel.internal.generic.*
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.AdviceAdapter


/**
 * 插入代码的方法大量参考 ASM Bytecode Viewer 插件生成
 */
class EffectWidgetClassVisitor(nextVisitor: ClassVisitor, private val className: String) :
    ClassVisitor(Opcodes.ASM5, nextVisitor) {

    val classOwner: String = className.replace('.', '/')
    val classDescriptor: String = "L${classOwner};"
    val effectHelperDescriptor = "Lcom/example/asmdemo/EffectHelper;"
    val getEffectHelperFuncDescriptor = "()Lcom/example/asmdemo/EffectHelper;"
    val getEffectHelperFuncName = "getOSUIEffectHelper\$gen"
    val effectHelperOwner = "com/example/asmdemo/EffectHelper"

    override fun visitSource(source: String?, debug: String?) {
        super.visitSource(source, debug)
    }

    override fun visit(
        version: Int,
        access: Int,
        name: String?,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?,
    ) {
        super.visit(version, access, name, signature, superName, interfaces)
        //判断是否有如下方法，没有的话就要生成一份新的。
        //dispatchTouchEvent
        //onAttachedToWindow
        //onDetachedFromWindow

        println("visit :${access},${name},${signature},${interfaces}")
    }

    private var dispatchGenericMotionEventMethodExist = false
    private var dispatchTouchEventMethodExist = false
    private var onAttachedToWindowMethodExist = false
    private var onDetachedFromWindowMethodExist = false
    private var setPressedMethodExist = false
    private var onHoverChangedMethodExist = false

    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?,
    ): MethodVisitor {
        println("visitMethod :${access},${name},${descriptor},${signature}")
        val methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions)
        val newMethodVisitor =
            object : AdviceAdapter(Opcodes.ASM5, methodVisitor, access, name, descriptor) {

                @Override
                override fun onMethodEnter() {
                    // 方法开始
                    println("onMethodEnter:${access},${methodDesc},${name},${descriptor},${signature}")
                    //我们需要在以下方法中调用effectHelper的调用。
                    //dispatchTouchEvent
                    //onAttachedToWindow
                    //onDetachedFromWindow
                    when (name) {
                        "dispatchGenericMotionEvent" -> {
                            dispatchGenericMotionEventMethodExist = true
                            methodVisitor.visitVarInsn(ALOAD, 0)
                            methodVisitor.visitMethodInsn(INVOKESPECIAL,
                                classOwner,
                                getEffectHelperFuncName,
                                getEffectHelperFuncDescriptor,
                                false);
                            methodVisitor.visitVarInsn(ALOAD, 1)
                            methodVisitor.visitMethodInsn(INVOKEVIRTUAL,
                                effectHelperOwner,
                                "dispatchGenericMotionEvent",
                                "(Landroid/view/MotionEvent;)V",
                                false)
                        }
                        "dispatchTouchEvent" -> {
                            dispatchTouchEventMethodExist = true
                            methodVisitor.visitVarInsn(ALOAD, 0)
                            methodVisitor.visitMethodInsn(INVOKESPECIAL,
                                classOwner,
                                getEffectHelperFuncName,
                                getEffectHelperFuncDescriptor,
                                false);
                            methodVisitor.visitVarInsn(ALOAD, 1)
                            methodVisitor.visitMethodInsn(INVOKEVIRTUAL,
                                effectHelperOwner,
                                "dispatchTouchEvent",
                                "(Landroid/view/MotionEvent;)V",
                                false)
                        }
                        "onAttachedToWindow" -> {
                            onAttachedToWindowMethodExist = true
                            methodVisitor.visitVarInsn(ALOAD, 0)
                            methodVisitor.visitMethodInsn(INVOKESPECIAL,
                                classOwner,
                                getEffectHelperFuncName,
                                getEffectHelperFuncDescriptor,
                                false)
                            methodVisitor.visitMethodInsn(INVOKEVIRTUAL,
                                effectHelperOwner,
                                "onAttachedToWindow",
                                "()V",
                                false)
                        }
                        "onDetachedFromWindow" -> {
                            onDetachedFromWindowMethodExist = true
                            methodVisitor.visitVarInsn(ALOAD, 0)
                            methodVisitor.visitMethodInsn(INVOKESPECIAL,
                                classOwner,
                                getEffectHelperFuncName,
                                getEffectHelperFuncDescriptor,
                                false)
                            methodVisitor.visitMethodInsn(INVOKEVIRTUAL,
                                effectHelperOwner,
                                "onDetachedFromWindow",
                                "()V",
                                false)
                        }
                        "setPressed" -> {
                            setPressedMethodExist = true
                            methodVisitor.visitVarInsn(ALOAD, 0)
                            methodVisitor.visitMethodInsn(INVOKESPECIAL,
                                classOwner,
                                getEffectHelperFuncName,
                                getEffectHelperFuncDescriptor,
                                false);
                            methodVisitor.visitVarInsn(ALOAD, 1)
                            methodVisitor.visitMethodInsn(INVOKEVIRTUAL,
                                effectHelperOwner,
                                "setPressed",
                                "(Z)V",
                                false)
                        }
                        "onHoverChanged" -> {
                            onHoverChangedMethodExist = true
                            methodVisitor.visitVarInsn(ALOAD, 0)
                            methodVisitor.visitMethodInsn(INVOKESPECIAL,
                                classOwner,
                                getEffectHelperFuncName,
                                getEffectHelperFuncDescriptor,
                                false);
                            methodVisitor.visitVarInsn(ALOAD, 1)
                            methodVisitor.visitMethodInsn(INVOKEVIRTUAL,
                                effectHelperOwner,
                                "onHoverChanged",
                                "(Z)V",
                                false)
                        }
                    }
                    super.onMethodEnter()
                }

                @Override
                override fun onMethodExit(opcode: Int) {
                    // 方法结束
                    super.onMethodExit(opcode);
                }


            }
        return newMethodVisitor
    }

    override fun visitEnd() {
        println("visitEnd.")

        //新增mEffectHelper字段
        val fvEffectHelper = cv.visitField(Opcodes.ACC_PRIVATE, "mEffectHelper",
            effectHelperDescriptor, null, null);
        if (fvEffectHelper != null) {
            fvEffectHelper.visitEnd()
        }
        println("added mEffectHelper.")

        //新增getOSUIEffectHelper方法
        makeGetOSUIEffectHelperFunc()

        //判断是否有如下方法，没有的话就要生成一份新的插入。
        if (!dispatchGenericMotionEventMethodExist) {
            makeNewDispatchGenericMotionEvent()
        }
        if (!dispatchTouchEventMethodExist) {
            println("dispatchTouchEventMethodExist false,inject.")
            makeNewDispatchTouchEvent()
        }
        if (!onAttachedToWindowMethodExist) {
            makeNewOnAttachedToWindow()
        }
        if (!onDetachedFromWindowMethodExist) {
            makeNewOnDetachedFromWindow()
        }
        if (!setPressedMethodExist) {
            makeNewSetPress()
        }
        if (!onHoverChangedMethodExist) {
            makeNewOnHoverChanged()
        }
        super.visitEnd()
    }

    /***
     * 生成一个新方法
     *
     * private EffectHelper getOSUIEffectHelper$gen() {
     *  if (mEffectHelper == null) {
     *      mEffectHelper = new EffectHelper(this);
     *  }
     *      return mEffectHelper;
     * }
     */
    private fun makeGetOSUIEffectHelperFunc() {
        println("added getOSUIEffectHelper owner:${classOwner}")
        val methodVisitor = cv.visitMethod(Opcodes.ACC_PRIVATE,
            getEffectHelperFuncName,
            getEffectHelperFuncDescriptor,
            null,
            null)
        methodVisitor.visitCode()
        methodVisitor.visitVarInsn(Opcodes.ALOAD, 0)
        methodVisitor.visitFieldInsn(Opcodes.GETFIELD,
            classOwner,
            "mEffectHelper",
            effectHelperDescriptor)
        val label0 = Label()
        methodVisitor.visitJumpInsn(Opcodes.IFNONNULL, label0)
        methodVisitor.visitVarInsn(Opcodes.ALOAD, 0)
        methodVisitor.visitTypeInsn(Opcodes.NEW, effectHelperOwner)
        methodVisitor.visitInsn(Opcodes.DUP)
        methodVisitor.visitVarInsn(Opcodes.ALOAD, 0)
        methodVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL,
            effectHelperOwner,
            "<init>",
            "(Landroid/view/View;)V",
            false)
        methodVisitor.visitFieldInsn(Opcodes.PUTFIELD,
            classOwner,
            "mEffectHelper",
            effectHelperDescriptor)
        methodVisitor.visitLabel(label0)
        methodVisitor.visitVarInsn(Opcodes.ALOAD, 0)
        methodVisitor.visitFieldInsn(Opcodes.GETFIELD,
            classOwner,
            "mEffectHelper",
            effectHelperDescriptor)
        methodVisitor.visitInsn(Opcodes.ARETURN)
        methodVisitor.visitMaxs(4, 1)
        methodVisitor.visitEnd()
    }

    private fun makeNewDispatchGenericMotionEvent() {
        val methodVisitor = cv.visitMethod(Opcodes.ACC_PUBLIC,
            "dispatchGenericMotionEvent",
            "(Landroid/view/MotionEvent;)Z",
            null,
            null)
        methodVisitor.visitCode()
        val label0 = Label()
        methodVisitor.visitLabel(label0)
//        methodVisitor.visitLineNumber(28, label0)
        methodVisitor.visitVarInsn(Opcodes.ALOAD, 0)
        methodVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL,
            classOwner,
            getEffectHelperFuncName,
            getEffectHelperFuncDescriptor,
            false)
        methodVisitor.visitVarInsn(Opcodes.ALOAD, 1)
        methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
            effectHelperOwner,
            "dispatchGenericMotionEvent",
            "(Landroid/view/MotionEvent;)V",
            false)
        val label1 = Label()
        methodVisitor.visitLabel(label1)
//        methodVisitor.visitLineNumber(29, label1)
        methodVisitor.visitVarInsn(Opcodes.ALOAD, 0)
        methodVisitor.visitVarInsn(Opcodes.ALOAD, 1)
        methodVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL,
            "androidx/appcompat/widget/AppCompatButton",
            "dispatchGenericMotionEvent",
            "(Landroid/view/MotionEvent;)Z",
            false)
        methodVisitor.visitInsn(Opcodes.IRETURN)
        val label2 = Label()
        methodVisitor.visitLabel(label2)
        methodVisitor.visitLocalVariable("this",
            classDescriptor,
            null,
            label0,
            label2,
            0)
        methodVisitor.visitLocalVariable("event",
            "Landroid/view/MotionEvent;",
            null,
            label0,
            label2,
            1)
        methodVisitor.visitMaxs(2, 2)
        methodVisitor.visitEnd()
    }

    private fun makeNewDispatchTouchEvent() {
        val methodVisitor = cv.visitMethod(Opcodes.ACC_PUBLIC,
            "dispatchTouchEvent",
            "(Landroid/view/MotionEvent;)Z",
            null,
            null)
        methodVisitor.visitCode()
        val label0 = Label()
        methodVisitor.visitLabel(label0)
//        methodVisitor.visitLineNumber(27, label0)
        methodVisitor.visitVarInsn(Opcodes.ALOAD, 0)
        methodVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL,
            classOwner,
            getEffectHelperFuncName,
            getEffectHelperFuncDescriptor,
            false)
        methodVisitor.visitVarInsn(Opcodes.ALOAD, 1)
        methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
            effectHelperOwner,
            "dispatchTouchEvent",
            "(Landroid/view/MotionEvent;)V",
            false)
        val label1 = Label()
        methodVisitor.visitLabel(label1)
//        methodVisitor.visitLineNumber(28, label1)
        methodVisitor.visitVarInsn(Opcodes.ALOAD, 0)
        methodVisitor.visitVarInsn(Opcodes.ALOAD, 1)
        methodVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL,
            "androidx/appcompat/widget/AppCompatButton",
            "dispatchTouchEvent",
            "(Landroid/view/MotionEvent;)Z",
            false)
        methodVisitor.visitInsn(Opcodes.IRETURN)
        val label2 = Label()
        methodVisitor.visitLabel(label2)
        methodVisitor.visitLocalVariable("this",
            classDescriptor,
            null,
            label0,
            label2,
            0)
        methodVisitor.visitLocalVariable("event",
            "Landroid/view/MotionEvent;",
            null,
            label0,
            label2,
            1)
        methodVisitor.visitMaxs(2, 2)
        methodVisitor.visitEnd()
    }

    private fun makeNewOnAttachedToWindow() {
        val methodVisitor =
            cv.visitMethod(Opcodes.ACC_PROTECTED, "onAttachedToWindow", "()V", null, null)
        methodVisitor.visitCode()
        val label0 = Label()
        methodVisitor.visitLabel(label0)
//        methodVisitor.visitLineNumber(40, label0)
        methodVisitor.visitVarInsn(Opcodes.ALOAD, 0)
        methodVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL,
            classOwner,
            getEffectHelperFuncName,
            getEffectHelperFuncDescriptor,
            false)
        methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
            effectHelperOwner,
            "onAttachedToWindow",
            "()V",
            false)
        val label1 = Label()
        methodVisitor.visitLabel(label1)
//        methodVisitor.visitLineNumber(41, label1)
        methodVisitor.visitVarInsn(Opcodes.ALOAD, 0)
        methodVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL,
            "androidx/appcompat/widget/AppCompatButton",
            "onAttachedToWindow",
            "()V",
            false)
        val label2 = Label()
        methodVisitor.visitLabel(label2)
//        methodVisitor.visitLineNumber(42, label2)
        methodVisitor.visitInsn(Opcodes.RETURN)
        val label3 = Label()
        methodVisitor.visitLabel(label3)
        methodVisitor.visitLocalVariable("this",
            classDescriptor,
            null,
            label0,
            label3,
            0)
        methodVisitor.visitMaxs(1, 1)
        methodVisitor.visitEnd()
    }


    private fun makeNewOnDetachedFromWindow() {
        val methodVisitor =
            cv.visitMethod(Opcodes.ACC_PROTECTED, "onDetachedFromWindow", "()V", null, null)
        methodVisitor.visitCode()
        val label0 = Label()
        methodVisitor.visitLabel(label0)
//        methodVisitor.visitLineNumber(46, label0)
        methodVisitor.visitVarInsn(Opcodes.ALOAD, 0)
        methodVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL,
            classOwner,
            getEffectHelperFuncName,
            getEffectHelperFuncDescriptor,
            false)
        methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
            effectHelperOwner,
            "onDetachedFromWindow",
            "()V",
            false)
        val label1 = Label()
        methodVisitor.visitLabel(label1)
//        methodVisitor.visitLineNumber(47, label1)
        methodVisitor.visitVarInsn(Opcodes.ALOAD, 0)
        methodVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL,
            "androidx/appcompat/widget/AppCompatButton",
            "onDetachedFromWindow",
            "()V",
            false)
        val label2 = Label()
        methodVisitor.visitLabel(label2)
//        methodVisitor.visitLineNumber(48, label2)
        methodVisitor.visitInsn(Opcodes.RETURN)
        val label3 = Label()
        methodVisitor.visitLabel(label3)
        methodVisitor.visitLocalVariable("this",
            classDescriptor,
            null,
            label0,
            label3,
            0)
        methodVisitor.visitMaxs(1, 1)
        methodVisitor.visitEnd()
    }

    private fun makeNewSetPress() {
        val methodVisitor = cv.visitMethod(Opcodes.ACC_PUBLIC,
            "setPressed", "(Z)V", null, null)
        methodVisitor.visitCode()
        val label0 = Label()
        methodVisitor.visitLabel(label0)
//        methodVisitor.visitLineNumber(52, label0)
        methodVisitor.visitVarInsn(Opcodes.ALOAD, 0)
        methodVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL,
            classOwner,
            getEffectHelperFuncName,
            getEffectHelperFuncDescriptor,
            false)
        methodVisitor.visitVarInsn(Opcodes.ILOAD, 1)
        methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
            effectHelperOwner,
            "setPressed",
            "(Z)V",
            false)
        val label1 = Label()
        methodVisitor.visitLabel(label1)
//        methodVisitor.visitLineNumber(53, label1)
        methodVisitor.visitVarInsn(Opcodes.ALOAD, 0)
        methodVisitor.visitVarInsn(Opcodes.ILOAD, 1)
        methodVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL,
            "androidx/appcompat/widget/AppCompatButton",
            "setPressed",
            "(Z)V",
            false)
        val label2 = Label()
        methodVisitor.visitLabel(label2)
//        methodVisitor.visitLineNumber(54, label2)
        methodVisitor.visitInsn(Opcodes.RETURN)
        val label3 = Label()
        methodVisitor.visitLabel(label3)
        methodVisitor.visitLocalVariable("this",
            classDescriptor,
            null,
            label0,
            label3,
            0)
        methodVisitor.visitLocalVariable("pressed", "Z", null, label0, label3, 1)
        methodVisitor.visitMaxs(2, 2)
        methodVisitor.visitEnd()
    }

    private fun makeNewOnHoverChanged() {
        val methodVisitor = cv.visitMethod(Opcodes.ACC_PUBLIC, "onHoverChanged", "(Z)V", null, null)
        methodVisitor.visitCode()
        val label0 = Label()
        methodVisitor.visitLabel(label0)
//        methodVisitor.visitLineNumber(58, label0)
        methodVisitor.visitVarInsn(Opcodes.ALOAD, 0)
        methodVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL,
            classOwner,
            getEffectHelperFuncName,
            getEffectHelperFuncDescriptor,
            false)
        methodVisitor.visitVarInsn(Opcodes.ILOAD, 1)
        methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
            effectHelperOwner,
            "onHoverChanged",
            "(Z)V",
            false)
        val label1 = Label()
        methodVisitor.visitLabel(label1)
//        methodVisitor.visitLineNumber(59, label1)
        methodVisitor.visitVarInsn(Opcodes.ALOAD, 0)
        methodVisitor.visitVarInsn(Opcodes.ILOAD, 1)
        methodVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL,
            "androidx/appcompat/widget/AppCompatButton",
            "onHoverChanged",
            "(Z)V",
            false)
        val label2 = Label()
        methodVisitor.visitLabel(label2)
//        methodVisitor.visitLineNumber(60, label2)
        methodVisitor.visitInsn(Opcodes.RETURN)
        val label3 = Label()
        methodVisitor.visitLabel(label3)
        methodVisitor.visitLocalVariable("this",
            classDescriptor,
            null,
            label0,
            label3,
            0)
        methodVisitor.visitLocalVariable("hovered", "Z", null, label0, label3, 1)
        methodVisitor.visitMaxs(2, 2)
        methodVisitor.visitEnd()
    }
}