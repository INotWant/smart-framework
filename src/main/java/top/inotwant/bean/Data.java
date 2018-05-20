package top.inotwant.bean;

/**
 * action method 返回为 json 的封装类（返回模型对象）
 */
public class Data {

    private Object model; // json 数据

    public Data(Object model) {
        this.model = model;
    }

    public Object getModel() {
        return model;
    }
}
