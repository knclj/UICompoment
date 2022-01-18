package com.test.router;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;
import com.test.ARouter;
import com.test.BaseProcessor;
import com.test.Utils.ProcessorUtils;
import com.test.bean.RouterBean;
import com.test.config.Constant;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

@SupportedOptions(Constant.OPTIONS)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("com.test.ARouter")
@AutoService(Processor.class)
public class ARouterProcessor extends BaseProcessor {


    private Map<String, List<RouterBean>> mAllPathMap = new HashMap<>();
    //路由表
    private Map<String,String> mAllGroupMap = new HashMap<>();
    private String moduleName = "";

    @Override
    public void init(@Nullable ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        moduleName = processingEnv.getOptions().get(Constant.OPTIONS);
        getMLogger().printMessage(Diagnostic.Kind.NOTE,"Options module name:"+moduleName);
    }



    @Override
    public boolean process(@Nullable Set<? extends TypeElement> annotations, @Nullable RoundEnvironment roundEnv) {
        getMLogger().printMessage(Diagnostic.Kind.NOTE,"anntations size:"+annotations.size());
        if(!annotations.isEmpty()){
          Set<? extends Element> routerSets =   roundEnv.getElementsAnnotatedWith(ARouter.class);
          TypeElement activityType = getElementTool().getTypeElement(Constant.ACTIVITY_CLASS);
          if(!routerSets.isEmpty()){

             for (Element element:routerSets){
                 String classSimpleName = element.getSimpleName().toString();
                 getMLogger().printMessage(Diagnostic.Kind.NOTE,"被ARouter 注解的类:"+classSimpleName);
                 ARouter aRouter =   element.getAnnotation(ARouter.class);
                 getMLogger().printMessage(Diagnostic.Kind.NOTE,"Router path"+aRouter.path());
                 RouterBean routerBean =  new RouterBean.Builder()
                         .addGroup(aRouter.group())
                         .addPath(aRouter.path())
                         .addElement(element)
                         .build();
                 getMLogger().printMessage(Diagnostic.Kind.NOTE,"parentElement");

                 if(getMTypesUtils().isSubtype(element.asType(), activityType.asType())){
                     getMLogger().printMessage(Diagnostic.Kind.NOTE,"parent element activity");
                     routerBean.setTypeEnum(RouterBean.TypeEnum.ACTIVITY);
                 }else {
                     getMLogger().printMessage(Diagnostic.Kind.NOTE,"no activity");
                     throw  new RuntimeException("@Router 不在Activity");
                 }
                 if(checkRouterPath(routerBean)){
                     getMLogger().printMessage(Diagnostic.Kind.NOTE," RouterBean path check success");
                     if(!mAllPathMap.containsKey(routerBean.getGroup())){
                         ArrayList<RouterBean> data = new ArrayList<>();
                         data.add(routerBean);
                        mAllPathMap.put(routerBean.getGroup(),data);
                     }else{
                         List<RouterBean> data = mAllPathMap.get(routerBean.getGroup());
                         data.add(routerBean);
                     }
                 }else{
                     getMLogger().printMessage(Diagnostic.Kind.ERROR,"Router 未规范配置");
                 }
             }
             TypeElement pathType = getElementTool().getTypeElement(Constant.ROUTER_API_PATH);
             TypeElement groupType = getElementTool().getTypeElement(Constant.ROUTER_API_GROUP);
             if(!mAllPathMap.isEmpty()){
                 getMLogger().printMessage(Diagnostic.Kind.NOTE,"开始生产 path Router");
                 generatePathRouter(pathType);
             }else{
                 getMLogger().printMessage(Diagnostic.Kind.NOTE," mAllPathMap is null");
             }
             if(!mAllGroupMap.isEmpty()){
                 getMLogger().printMessage(Diagnostic.Kind.NOTE,"开始生产 Group Router");
                generateGroupRouter(pathType,groupType);
             }
          }
        }
        return true;
    }

    private void generatePathRouter(TypeElement pathType)  {
        if(mAllPathMap.isEmpty()){
            return;
        }
        //生产一个返回值 Map<String, RouterBean>
        TypeName methodReturn = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                ClassName.get(String.class),
                ClassName.get(RouterBean.class));


        //Java
        mAllPathMap.forEach((s, routerBeans) -> {
            MethodSpec.Builder methodSpecBuilder = MethodSpec.methodBuilder(Constant.ROUTER_API_PATH_METHOD)
                    .returns(methodReturn)
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .addStatement("$T<$T,$T> $N = new $T<>()",
                            ClassName.get(Map.class),
                            ClassName.get(String.class),
                            ClassName.get(RouterBean.class),
                            Constant.ROUTER_PATH_VAR1,
                            ClassName.get(HashMap.class));//Map<String,RouterBean>  pathMap = new HashMap<>();

            routerBeans.forEach(bean -> {
                //        pathMap.put("",RouterBean.create("","",))
                methodSpecBuilder.addStatement("$N.put($S,$T.create($S,$S,$T.class,$T.$L))",
                        Constant.ROUTER_PATH_VAR1,
                        bean.getPath(),
                        ClassName.get(RouterBean.class),
                        bean.getPath(),
                        bean.getGroup(),
                        ClassName.get((TypeElement) bean.getElement()),                        ClassName.get(RouterBean.TypeEnum.class),
                        bean.getTypeEnum()
                        );
            });
            methodSpecBuilder.addStatement("return $N",Constant.ROUTER_PATH_VAR1);
            //类实现接口从写方法
            String finalClassName = Constant.PATH_FILE_NAME+s;
            getMLogger().printMessage(Diagnostic.Kind.NOTE,"APT 生产的path类文件"+Constant.AROUTER_PACKAGE+"."+finalClassName);
            try {
                JavaFile.builder(Constant.AROUTER_PACKAGE, TypeSpec.classBuilder(finalClassName)
                .addSuperinterface(ClassName.get(pathType))
                        .addModifiers(Modifier.PUBLIC)
                        .addMethod(methodSpecBuilder.build())
                        .build())
                .build().writeTo(getFiler());
                mAllGroupMap.put(s,finalClassName);
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }

    private void generateGroupRouter(TypeElement pathType,TypeElement groupType){
        if(mAllGroupMap.isEmpty()){
            return;
        }
        //Map<String, Class<? extends IRouterPath>>
        TypeName methodReturn = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                ClassName.get(String.class),
                ParameterizedTypeName.get(ClassName.get(Class.class),
                        WildcardTypeName.subtypeOf(ClassName.get(pathType))));
        MethodSpec.Builder  methodBuilder = MethodSpec.methodBuilder(Constant.ROUTER_API_GROUP_METHOD)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(methodReturn)
                .addStatement("$T<$T,$T> $N = new $T<>()",
                        ClassName.get(Map.class),
                        ClassName.get(String.class),
                        ParameterizedTypeName.get(ClassName.get(Class.class), WildcardTypeName.subtypeOf(ClassName.get(pathType))),
                        Constant.ROUTER_GROUP_VAR1,
                        HashMap.class);//Map<String,Class<? extends IRouterPath>> groupMap = new HashMap<>();

        mAllGroupMap.forEach((groupKey, clzName) -> {
            //        groupMap.put("personal",Router$$Path$$personal.class);
            methodBuilder.addStatement("$N.put($S,$T.class)",
                    Constant.ROUTER_GROUP_VAR1,
                    groupKey,
                    ClassName.get(Constant.AROUTER_PACKAGE,clzName)
                    );
        });
        methodBuilder.addStatement("return $N",Constant.ROUTER_GROUP_VAR1);
        //类实现接口从写方法
        String finalClassName = Constant.GROUP_FILE_NAME+moduleName;
        getMLogger().printMessage(Diagnostic.Kind.NOTE,"APT 生产的path类文件"+Constant.AROUTER_PACKAGE+"."+finalClassName);
        try {
            JavaFile.builder(Constant.AROUTER_PACKAGE, TypeSpec.classBuilder(finalClassName)
                    .addSuperinterface(ClassName.get(groupType))
                    .addModifiers(Modifier.PUBLIC)
                    .addMethod(methodBuilder.build())
                    .build())
                    .build().writeTo(getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private final boolean checkRouterPath(RouterBean bean){
        String group = bean.getGroup();
        String path = bean.getPath();
        if(ProcessorUtils.isEmpty(path) || !path.startsWith("/")){
            getMLogger().printMessage(Diagnostic.Kind.ERROR,"Router 注解path必须有值，且开头用/开头");
            return false;
        }

        if(path.lastIndexOf("/") == 0){
            getMLogger().printMessage(Diagnostic.Kind.ERROR,"Router 未规范配置，且开头用/开头");
            return false;
        }
        String finalGroup = path.substring(1,path.indexOf("/",1));

        if(ProcessorUtils.isEmpty(finalGroup) ){
            getMLogger().printMessage(Diagnostic.Kind.ERROR,"Router 未规范配置，且开头用/开头");
            return false;
        }else{
            bean.setGroup(finalGroup);
        }
        return true;
    }
}
