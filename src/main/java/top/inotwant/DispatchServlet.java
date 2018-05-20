package top.inotwant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.inotwant.bean.*;
import top.inotwant.helper.*;
import top.inotwant.utils.JsonUtil;
import top.inotwant.utils.ReflectUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Map;

@WebServlet(urlPatterns = "/*", loadOnStartup = 0)
public class DispatchServlet extends HttpServlet {

    private final static Logger LOGGER = LoggerFactory.getLogger(DispatchServlet.class);

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        LOGGER.warn("===================DispatchServlet Init===================");

        // 1. 加载 helper 类
        HelperLoader.init();

        // 2. 加载 上传文件 组件
        UploadHelper.init(servletConfig.getServletContext());

        //TODO (上面已经让 DispatchServlet 管理 /* 了，所以有必要说明 jsp 与 asset 的路径)
        ServletContext servletContext = servletConfig.getServletContext();
        ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
        jspServlet.addMapping(ConfigHelper.getAppJspPath() + "*");  // jsp 通配路径
        ServletRegistration defaultServlet = servletContext.getServletRegistration("default");
        defaultServlet.addMapping(ConfigHelper.getAppAssetPath() + "*");

        super.init(servletConfig);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // 初始化 ServletHelper ，以实现与 Servlet API 的解耦
        ServletHelper.init(req, resp);

        try {
            String requestMethod = req.getMethod();
            String requestPath = req.getPathInfo();
            Request request = new Request(requestMethod, requestPath);
            Handler handler = ControllerHelper.getHandler(request);
            if (handler != null) {
                Class<?> controllerClass = handler.getControllerClass();
                Object controllerObj = BeanHelper.getBean(controllerClass);
                Method actionMethod = handler.getActionMethod();

                // 创建请求参数
                Param param;
                if (UploadHelper.isMultipart(req))
                    param = UploadHelper.createParam(req);
                else
                    param = RequestHelper.createParam(req);

                Object result;
                if (param.isEmpty())
                    result = ReflectUtil.invokeMethod(controllerObj, actionMethod);
                else
                    result = ReflectUtil.invokeMethod(controllerObj, actionMethod, param);
                if (result instanceof View) {   // 返回 jsp
                    handlerViewResult((View) result, req, resp);
                } else if (result instanceof Data) {   // 返回 json
                    handlerDataResult((Data) result, resp);
                }
            }
        } finally {
            ServletHelper.destory();
        }
    }

    /**
     * view 结果处理
     */
    private void handlerViewResult(View view, HttpServletRequest req, HttpServletResponse resp
    ) throws IOException, ServletException {
        String path = view.getPath();
        if (path.startsWith("/")) {  // 重定向
            // 重定向 要带的数据数据可以放到 url 或 会话中。
            resp.sendRedirect(req.getContextPath() + path);
        } else {                     // 转发
            Map<String, Object> params = view.getParams();
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                req.setAttribute(entry.getKey(), entry.getValue());
            }
            req.getRequestDispatcher(ConfigHelper.getAppJspPath() + path).forward(req, resp);
        }
    }

    /**
     * data 结果处理
     */
    private void handlerDataResult(Data data, HttpServletResponse resp) throws IOException {
        Object model = data.getModel();
        if (model != null) {
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            PrintWriter writer = resp.getWriter();
            String jsonStr = JsonUtil.toJson(model);
            writer.write(jsonStr);
            writer.flush();
            writer.close();
        }
    }
}
