package com.test.Utils;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.test.config.ParameterConstant;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Modifier;

public class ParameterFactory {

    private Messager messager;
    private ClassName className;
    private ParameterSpec parameterSpec;
    private MethodSpec.Builder methodSpecBuilder;

    private ParameterFactory(Builder builder){
        this.messager = builder.getMessager();
        this.className = builder.getClassName();
        this.parameterSpec = builder.getParameterSpec();
        methodSpecBuilder = MethodSpec.methodBuilder(ParameterConstant.PARAMETER_METHOD)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(builder.parameterSpec);
    }

    public static class Builder{
        private Messager messager;
        private ClassName className;
        private ParameterSpec parameterSpec;

        public Builder messager(Messager messager){
            this.messager = messager;
            return this;
        }

        public Builder className(ClassName name){
            this.className = name;
            return this;
        }

        public Builder parameterSpec(ParameterSpec spec){
            this.parameterSpec = spec;
            return this;
        }

        public ParameterSpec getParameterSpec() {
            return parameterSpec;
        }

        public ClassName getClassName() {
            return className;
        }

        public Messager getMessager() {
            return messager;
        }

        public ParameterFactory build(){
            return new  ParameterFactory(this);
        }
    }

}
