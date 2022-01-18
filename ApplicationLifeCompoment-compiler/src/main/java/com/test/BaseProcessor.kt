package com.test

import javax.annotation.processing.*
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.lang.model.util.Types

open class BaseProcessor: AbstractProcessor() {
    /**
     * 控制台log输出
     */
     var mLogger: Messager? = null

    /**
     * 类型工具
     */
     var mTypesUtils: Types? = null

    /**
     * 用于文件生产输出
     */
     var filer: Filer? = null

    var elementTool: Elements? = null

    override fun init(processingEnv: ProcessingEnvironment?) {
        super.init(processingEnv)
        mLogger = processingEnv?.messager
        mTypesUtils = processingEnv?.typeUtils
        filer = processingEnv?.filer
        elementTool = processingEnv?.elementUtils
    }

   override fun process(
        annotations: MutableSet<out TypeElement>?,
        roundEnv: RoundEnvironment?
    ): Boolean {
        return false
    }

}