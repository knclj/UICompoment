package com.test.bean;

import javax.lang.model.element.Element;

public class RouterBean {

    private String path;
    private String group;
    private Element element;
    private TypeEnum typeEnum;
    private Class<?> target;

    private RouterBean() {

    }
    public enum TypeEnum{
        ACTIVITY
    }
    private RouterBean(String path, String group, Class<?> target, TypeEnum typeEnum){
        this.path = path;
        this.group = group;
        this.typeEnum = typeEnum;
        this.target = target;
    }

    public static  RouterBean create(String path, String group, Class<?> target, TypeEnum typeEnum){
        return new RouterBean(path, group, target, typeEnum);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public TypeEnum getTypeEnum() {
        return typeEnum;
    }

    public void setTypeEnum(TypeEnum typeEnum) {
        this.typeEnum = typeEnum;
    }

    public Class<?> getTarget() {
        return target;
    }

    public void setTarget(Class<?> target) {
        this.target = target;
    }

    public static class Builder{

        private String group;
        private String path;
        private Element element;
        private Class<?> target;

        public Builder addGroup(String group){
            this.group = group;
            return this;
        }
        public Builder addPath(String path){
            this.path = path;
            return this;
        }
        public Builder addElement(Element element){
            this.element = element;
            return this;
        }

        public Builder addTarget(Class<?> target){
            this.target = target;
            return this;
        }

        public RouterBean build(){
            RouterBean routerBean = new RouterBean();
            routerBean.setGroup(group);
            routerBean.setPath(path);
            routerBean.setElement(element);
            routerBean.setTarget(target);
            return routerBean;
        }
    }
}
