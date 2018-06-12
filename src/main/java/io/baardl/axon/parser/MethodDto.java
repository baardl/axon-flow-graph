package io.baardl.axon.parser;

public class MethodDto {

    private final String methodName;
    private String fileName;
    private String type;
    private String next;
    private String packageName;

    public MethodDto(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public String toString() {
        return "MethodDto{" +
                "methodName='" + methodName + '\'' +
                ", fileName='" + fileName + '\'' +
                ", type='" + type + '\'' +
                ", next='" + next + '\'' +
                ", packageName='" + packageName + '\'' +
                '}';
    }
}
