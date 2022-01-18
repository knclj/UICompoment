package com.test.autocreate

import com.google.auto.service.AutoService
import com.squareup.javapoet.*
import com.test.AutoCreate
import com.test.BaseProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedAnnotationTypes
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

@SupportedAnnotationTypes("com.test.AutoCreate")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor::class)
class AutoCreateProcessor: BaseProcessor() {
    override fun process(
        annotations: MutableSet<out TypeElement>?,
        roundEnv: RoundEnvironment?
    ): Boolean {
        mLogger?.printMessage(Diagnostic.Kind.NOTE,"start process ")
        if(annotations?.isNotEmpty() == true){
           val createAnns =  roundEnv?.getElementsAnnotatedWith(AutoCreate::class.java)
            mLogger?.printMessage(Diagnostic.Kind.NOTE,"AutoCreate size: ${createAnns?.size}")
            //使用Javapoet 生产以下模板代码
//            package com.example.helloworld;
//
//            public final class HelloWorld {
//                public static void main(String[] args) {
//                    System.out.println("Hello, JavaPoet!");
//                }
//            }

            val main = MethodSpec.methodBuilder("main")
                .addModifiers(Modifier.PUBLIC,Modifier.STATIC)
                .returns(TypeName.VOID)
                .addParameter(ArrayTypeName.get(String::class.java),"args")
                .addStatement("\$T.out.println(\$S)",System::class.java,"Hello,JavaPoet!")
                .build()

            val helloJavaPoet = TypeSpec.classBuilder("HelloJavaPoet")
                .addModifiers(Modifier.PUBLIC,Modifier.FINAL)
                .addMethod(main)
                .build()

            val javaFile = JavaFile.builder("com.example.hello",helloJavaPoet)
                .build()
            kotlin.runCatching {
                javaFile.writeTo(filer)
            }.onFailure {
                it.printStackTrace()
            }.onSuccess {
                mLogger?.printMessage(Diagnostic.Kind.NOTE,"onSuccess")
            }

        }
        return true
    }
}