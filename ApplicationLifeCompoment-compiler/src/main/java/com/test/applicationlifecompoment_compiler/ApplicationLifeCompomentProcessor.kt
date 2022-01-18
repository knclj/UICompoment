package com.test.applicationlifecompoment_compiler

import com.google.auto.service.AutoService
import com.test.BaseProcessor
import com.test.applicationlifecompoment_annotions.ApplicationLifeComponent
import java.io.BufferedReader
import java.io.PrintWriter
import java.util.*
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.Types
import javax.tools.Diagnostic
import javax.tools.FileObject
import javax.tools.JavaFileObject
import javax.tools.StandardLocation
import kotlin.collections.ArrayList

@AutoService(Processor::class)
class ApplicationLifeCompomentProcessor : BaseProcessor() {

    private var mModuleAbilityType:TypeMirror? = null

    private val interfaceMapResult = hashMapOf<String,List<String>>()


    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return Collections.singleton(ApplicationLifeComponent::class.java.canonicalName)
    }

    override fun init(processingEnv: ProcessingEnvironment?) {
        super.init(processingEnv)
        mModuleAbilityType = processingEnv?.elementUtils?.getTypeElement(SERVICE_NAME)?.asType()
    }

    override fun process(
        annotations: MutableSet<out TypeElement>?,
        roundEnv: RoundEnvironment?
    ): Boolean {
        if (annotations == null || annotations.isEmpty()) {
           return false
        }
        val  elements =  roundEnv?.getElementsAnnotatedWith(ApplicationLifeComponent::class.java)
        if(elements == null || elements.isEmpty()){
            return false
        }
        mLogger?.printMessage(Diagnostic.Kind.NOTE, "${TAG} 开始处理")
        elements.forEach{
            if(it.kind.isClass && it is TypeElement && it.interfaces?.isNotEmpty() == true){
                mLogger?.printMessage(Diagnostic.Kind.NOTE,"${TAG} ${it}找到实现接口类")
                it.interfaces.forEach{ typeMirror ->
                    var collections:ArrayList<String>? = null
                    if(interfaceMapResult.containsKey(typeMirror.toString())){
                        collections  = interfaceMapResult.get(typeMirror.toString()) as ArrayList<String>?
                    }else{
                        collections = arrayListOf<String>()
                    }
                    collections?.add(it.qualifiedName.toString())
                    interfaceMapResult.put(typeMirror.toString(),collections!!)

                    if(mTypesUtils?.isSameType(typeMirror,mModuleAbilityType) == true){
                        mLogger?.printMessage(Diagnostic.Kind.NOTE,"${TAG}${typeMirror} isSameType 不能直接实现接口")
                    }else if(mTypesUtils?.isSubtype(typeMirror,mModuleAbilityType) == true){
                        mLogger?.printMessage(Diagnostic.Kind.NOTE,"${TAG}${typeMirror} isSubtype 子类")

                    }
                }
            }else{
                mLogger?.printMessage(Diagnostic.Kind.NOTE,"${TAG} 需要实现${SERVICE_NAME}")
            }
        }

        mLogger?.printMessage(Diagnostic.Kind.NOTE,"writeSPI ${interfaceMapResult.size}")
        if(interfaceMapResult.isEmpty()){
            return false
        }
        writeSPI()
        return true
    }

    companion object {
        private const val TAG = "ApplicationLifeCompomen"
        const val SERVICE_NAME = "com.test.applicationlifecompoment_api.IApplicationLifeComponent";
    }

    private fun writeSPI(){
        mLogger?.printMessage(Diagnostic.Kind.NOTE,"writeSPI ${interfaceMapResult.size}")
        val resouceFilePrefix = "META-INF/services/"
        val services = TreeSet<String>()
        interfaceMapResult.forEach { key, value ->
            //读文
            var reader:BufferedReader? = null
            try {
                services.addAll(value)
                val readFile = filer?.getResource(StandardLocation.CLASS_OUTPUT,"","${resouceFilePrefix}${key}")
                reader = BufferedReader(readFile?.openReader(true))
                var line:String? = null
                while ((reader?.readLine()).also { line = it } != null){
                    line?.let { services.add(it) }
                }
            }catch (e:Exception){
//                e.printStackTrace()
            }finally {
                reader?.close()
            }
            //write
            var writer:PrintWriter? = null
            try {
                val fileObject =  filer?.createResource(StandardLocation.CLASS_OUTPUT,"","${resouceFilePrefix}${key}");
                writer = PrintWriter(fileObject?.openWriter())
                services.forEach {
                    writer.println(it)
                }
                writer.flush()
                writer.close()
            }catch (e:Exception){
//                e.printStackTrace()
            }finally {
                writer?.close()
            }
        }
    }
}