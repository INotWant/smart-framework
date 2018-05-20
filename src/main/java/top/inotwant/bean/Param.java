package top.inotwant.bean;

import top.inotwant.utils.CastUtil;
import top.inotwant.utils.CollectionUtil;
import top.inotwant.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * action method 的统一参数
 */
public class Param {

    private List<FileParam> fileParamList;
    private List<FormParam> formParamList;

    public Param(List<FormParam> formParamList) {
        this.formParamList = formParamList;
    }

    public Param(List<FileParam> fileParamList, List<FormParam> formParamList) {
        this.fileParamList = fileParamList;
        this.formParamList = formParamList;
    }

    /**
     * 获取请求参数映射
     */
    public Map<String, Object> getFieldMap() {
        Map<String, Object> fieldMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(this.formParamList)) {
            for (FormParam formParam : this.formParamList) {
                String fieldName = formParam.getFieldName();
                Object fieldValue = formParam.getFieldValue();
                if (fieldMap.containsKey(fieldName)) {
                    fieldMap.put(fieldName, fieldMap.get(fieldName) + StringUtil.SEPARATOR + fieldValue);
                } else
                    fieldMap.put(fieldName, fieldValue);
            }
        }
        return fieldMap;
    }

    /**
     * 获取上传文件映射
     */
    public Map<String, List<FileParam>> getFileMap() {
        Map<String, List<FileParam>> fileMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(this.fileParamList)) {
            for (FileParam fileParam : this.fileParamList) {
                List<FileParam> fileParams;
                String fieldName = fileParam.getFieldName();
                if (fileMap.containsKey(fieldName))
                    fileParams = fileMap.get(fieldName);
                else
                    fileParams = new ArrayList<>();
                fileParams.add(fileParam);
                fileMap.put(fieldName, fileParams);
            }
        }
        return fileMap;
    }

    /**
     * 获取 某字段名 对应的所有上传文件
     */
    public List<FileParam> getFileList(String fieldName) {
        return getFileMap().get(fieldName);
    }

    /**
     * 获取唯一上传文件
     */
    public FileParam getFile(String fieldName) {
        List<FileParam> fileParams = getFileMap().get(fieldName);
        if (CollectionUtil.isNotEmpty(fileParams) && fileParams.size() == 1)
            return fileParams.get(0);
        return null;
    }

    /**
     * 判断 Param 是否为空
     */
    public boolean isEmpty() {
        return CollectionUtil.isEmpty(this.formParamList) && CollectionUtil.isEmpty(this.fileParamList);
    }

    /**
     * 根据参数名获取 String 型参数值
     */
    public String getString(String name) {
        return CastUtil.castString(getFieldMap().get(name));
    }

    /**
     * 根据参数名获取 double 型参数值
     */
    public double getDouble(String name) {
        return CastUtil.castDouble(getFieldMap().get(name));
    }

    /**
     * 根据参数名获取 long 型参数值
     */
    public long getLong(String name) {
        return CastUtil.castLong(getFieldMap().get(name));
    }

    /**
     * 根据参数名获取 int 型参数值
     */
    public int getInt(String name) {
        return CastUtil.castInt(getFieldMap().get(name));
    }

    /**
     * 根据参数名获取 boolean 型参数值
     */
    public boolean getBoolean(String name) {
        return CastUtil.castBoolean(getFieldMap().get(name));
    }

}
