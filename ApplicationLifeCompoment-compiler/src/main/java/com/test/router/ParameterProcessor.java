package com.test.router;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.test.BaseProcessor;
import com.test.Parameter;
import com.test.config.Constant;
import com.test.config.ParameterConstant;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

@SupportedAnnotationTypes(ParameterConstant.PARAMETER_PACKAGE_INTER)
@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class ParameterProcessor extends BaseProcessor {
    private Map<TypeElement, List<Element>> tempParameterMap = new HashMap<>();
    @Override
    public boolean process(@Nullable Set<? extends TypeElement> annotations, @Nullable RoundEnvironment roundEnv) {

        if (annotations == null || annotations.isEmpty()){
            return false;
        }

       Set<? extends Element> elements =   roundEnv.getElementsAnnotatedWith(Parameter.class);

        elements.forEach((Consumer<Element>) element -> {
            TypeElement  parentType = (TypeElement) element.getEnclosingElement();
            if(tempParameterMap.containsKey(parentType)){
                tempParameterMap.get(parentType).add(element);
            }else{
                List<Element> fields = new ArrayList<>();
                fields.add(element);
                tempParameterMap.put(parentType,fields);
            }
        });
        if(tempParameterMap.isEmpty()){
            return true;
        }
        generaParameterImplClass();
        return false;
    }

    private void generaParameterImplClass(){
        TypeElement activityType = getElementTool().getTypeElement(Constant.ACTIVITY_CLASS);
        TypeElement parameterType = getElementTool().getTypeElement(ParameterConstant.PARAMETER_PACKAGE_INTER);

        ParameterSpec parameterSpecBuilder = ParameterSpec.builder(TypeName.OBJECT,ParameterConstant.PARAMETER_NAME)
                .build();
        tempParameterMap.forEach((typeElement, elements) -> {
            if(getMTypesUtils().isSubtype(typeElement.asType(), activityType.asType())){
              throw  new RuntimeException("需要注释在Activity");
            }
            
        });

    }
}
