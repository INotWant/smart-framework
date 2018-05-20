package top.inotwant.bean;

import java.io.InputStream;

/**
 * 描述上传的文件
 */
public class FileParam {

    private String fieldName;   // 字段名
    private String fileName;    // 上传的文件名
    private long fileSize;
    private String contentType; // 用于判断文件类型
    private InputStream in;

    public FileParam(String fieldName, String fileName, long fileSize, String contentType, InputStream in) {
        this.fieldName = fieldName;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.contentType = contentType;
        this.in = in;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFileName() {
        return fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public String getContentType() {
        return contentType;
    }

    public InputStream getIn() {
        return in;
    }
}
