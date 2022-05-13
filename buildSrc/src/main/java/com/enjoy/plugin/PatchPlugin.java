package com.enjoy.plugin;


import com.android.build.gradle.AppExtension;
import com.android.build.gradle.AppPlugin;
import com.android.build.gradle.api.ApplicationVariant;

import org.codehaus.groovy.runtime.StringGroovyMethods;
import org.gradle.api.Action;
import org.gradle.api.GradleException;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.file.FileCollection;

import java.io.File;
import java.nio.file.Path;
import java.util.function.Consumer;

public class PatchPlugin implements Plugin<Project> {
    private Project project;
    @Override
    public void apply(Project project) {
//        com.android.build.gradle.internal.plugins.AppPlugin
        if(!project.getPlugins().hasPlugin(AppPlugin.class)){
            throw  new GradleException("结合Android application 插件使用");
        }
        this.project = project;
        project.getExtensions().create("patch",PatchExt.class);
        //此处获取不到，因为此处刚开始进入apply 代码，没有进入patch配置，没有赋值
//        System.out.println(patchExt.isDebugOn());
//        System.out.println(patchExt.getApplicationName());
        project.afterEvaluate(project1 -> {
           PatchExt patchExt = project1.getExtensions().findByType(PatchExt.class);
            System.out.println(patchExt.isDebugOn());
            System.out.println(patchExt.getApplicationName());
            AppExtension appExtension = project1.getExtensions().findByType(AppExtension.class);
            appExtension.getApplicationVariants().all(applicationVariant -> {
                if(applicationVariant.getName().contains("debug") && !patchExt.isDebugOn()){
                    return;
                }
                configTask(applicationVariant,patchExt);

            });
        });

    }

    void configTask(ApplicationVariant variant,PatchExt patchExt){
        //debug/release
        String name = variant.getName();
        //Debug/Release
        String capitalizeName = StringGroovyMethods.capitalize(name);

        File outDir;
        if(patchExt.getOutput() == null || patchExt.getOutput().isEmpty()){
            outDir = new File(this.project.getBuildDir(),"patch/"+name);
        }else{
            outDir = new File(this.project.getBuildDir(),patchExt.getOutput()+"/"+name);
        }

        File hexFile = new File(outDir,"hex.txt");
        //dexBuilder
        Task dexTask = this.project.getTasks().findByName("transformClassesWithDexBuildFor"+capitalizeName);
        if(dexTask != null){
            dexTask.doFirst(task -> {
               FileCollection files =  task.getInputs().getFiles();
                for (File file : files) {
                    String filePath = file.getAbsolutePath();
                    if(filePath.endsWith(".jar")){
                        processJar(file);
                    }else if(filePath.endsWith(".class")){
                        processClass(file);
                    }
                }
            });
        }else{
            System.out.println("the transformClassesWithDexBuildFor not found!!!");
        }
    }

    /**
     * 处理jar
     * @param file
     */
    private void processJar(File file){

    }

    /**
     * 处理class
     * @param file
     */
    private void processClass(File file) {

    }
}
