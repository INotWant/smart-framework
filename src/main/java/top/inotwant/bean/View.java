package top.inotwant.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * action method 返回为 jsp 的封装类（返回视图对象）
 */
public class View {

    private String path; // jsp 对应的路径
    private Map<String, Object> params; // 模型参数

    public View(String path) {
        this.path = path;
        this.params = new HashMap<>();
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public View addModel(String key, Object value) {
        this.params.put(key, value);
        return this;
    }

    public String getPath() {
        return path;
    }
}
