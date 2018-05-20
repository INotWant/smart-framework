import org.junit.Test;
import top.inotwant.helper.BeanHelper;

import java.util.Map;

public class TestHelper {

    @Test
    public void testBean() {
        Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap();
        System.out.println(beanMap);
    }

    @Test
    public void testIOC() {
        try {
            Class.forName("top.inotwant.helper.IocHelper");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
