package top.inotwant.helper;

import top.inotwant.bean.FormParam;
import top.inotwant.bean.Param;
import top.inotwant.utils.ArrayUtil;
import top.inotwant.utils.CodecUtil;
import top.inotwant.utils.StreamUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * 创建请求参数（除请求类型为 multipart 外）
 */
public final class RequestHelper {

    public static Param createParam(HttpServletRequest request) throws IOException {
        List<FormParam> formParamList = new ArrayList<>();
        formParamList.addAll(fromInside(request));
        formParamList.addAll(fromURL(request));
        return new Param(formParamList);
    }

    /**
     * 来自封装的参数
     */
    private static List<FormParam> fromInside(HttpServletRequest request) {
        List<FormParam> formParamList = new ArrayList<>();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            String paramValue = request.getParameter(paramName);
            formParamList.add(new FormParam(paramName, paramValue));
        }
        return formParamList;
    }

    /**
     * 来自 url 中的参数
     */
    private static List<FormParam> fromURL(HttpServletRequest request) throws IOException {
        List<FormParam> formParamList = new ArrayList<>();
        String urlStr = CodecUtil.decodeURL(StreamUtil.getString(request.getInputStream()));
        String[] split = urlStr.split("&");
        for (String str : split) {
            String[] array = str.split("=");
            if (ArrayUtil.isNotEmpty(array) && array.length == 2) {
                formParamList.add(new FormParam(array[0], array[1]));
            }
        }
        return formParamList;
    }


}
