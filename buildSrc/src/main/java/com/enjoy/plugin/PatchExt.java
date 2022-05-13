package com.enjoy.plugin;

public class PatchExt {
    private boolean debugOn;
    private String applicationName;
    private String output;

    public PatchExt(){

    }

    public PatchExt(boolean debugOn, String applicationName, String output) {
        this.debugOn = debugOn;
        this.applicationName = applicationName;
        this.output = output;
    }

    public void setDebugOn(boolean debugOn) {
        this.debugOn = debugOn;
    }

    public boolean isDebugOn() {
        return debugOn;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getOutput() {
        return output;
    }
}
