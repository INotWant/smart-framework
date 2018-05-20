package top.inotwant.helper;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.inotwant.bean.FileParam;
import top.inotwant.bean.FormParam;
import top.inotwant.bean.Param;
import top.inotwant.utils.CollectionUtil;
import top.inotwant.utils.FileUtil;
import top.inotwant.utils.StreamUtil;
import top.inotwant.utils.StringUtil;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 上传文件帮助类
 */
public final class UploadHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(UploadHelper.class);

    // apache common fileUpload 提供的 Servlet 文件上传对象
    private static ServletFileUpload servletFileUpload;

    /**
     * 初始化
     */
    public static void init(ServletContext servletContext) {
        File cacheFile = (File) servletContext.getAttribute("javax.servlet.context.temmpdir");   // 缓存满时，文件暂存的位置
        servletFileUpload = new ServletFileUpload(new DiskFileItemFactory(
                DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD,     // 缓存的大小，默认为 10k
                cacheFile
        ));
        int uploadLimit = ConfigHelper.getAppUploadLimit();
        if (uploadLimit != 0)
            servletFileUpload.setFileSizeMax(uploadLimit * 1024 * 1024);
    }

    /**
     * 判断请求是否为 multipart 类型
     */
    public static boolean isMultipart(HttpServletRequest request) {
        return ServletFileUpload.isMultipartContent(request);
    }

    /**
     * 创建 param
     */
    public static Param createParam(HttpServletRequest request) throws IOException {
        List<FormParam> formParamList = new ArrayList<>();
        List<FileParam> fileParamList = new ArrayList<>();
        try {
            Map<String, List<FileItem>> parameterMap = servletFileUpload.parseParameterMap(request);
            if (CollectionUtil.isNotEmpty(parameterMap)) {
                for (Map.Entry<String, List<FileItem>> entry : parameterMap.entrySet()) {
                    String fieldName = entry.getKey();
                    List<FileItem> value = entry.getValue();
                    if (CollectionUtil.isNotEmpty(value)) {
                        for (FileItem fileItem : value) {
                            if (fileItem.isFormField()) {
                                String fieldValue = fileItem.getString("UTF-8");
                                formParamList.add(new FormParam(fieldName, fieldValue));
                            } else {
                                String fileName = FileUtil.getRealFileName(
                                        new String(
                                                fileItem.getName().getBytes(),
                                                "UTF-8"
                                        ));
                                if (StringUtil.isNotEmpty(fileName)) {
                                    long fileSize = fileItem.getSize();
                                    String contentType = fileItem.getContentType();
                                    InputStream in = fileItem.getInputStream();
                                    FileParam fileParam = new FileParam(fieldName, fileName, fileSize, contentType, in);
                                    fileParamList.add(fileParam);
                                }
                            }
                        }
                    }
                }
            }
        } catch (FileUploadException e) {
            LOGGER.error("create param fail", e);
            throw new RuntimeException(e);
        }
        return new Param(fileParamList, formParamList);
    }

    /**
     * 上传文件
     */
    public static void uploadFile(String basePath, FileParam fileParam) {
        String fileName = fileParam.getFileName();
        if (StringUtil.isNotEmpty(fileName)) {
            try {
                String filePath = basePath + fileName;
                InputStream in = new BufferedInputStream(fileParam.getIn());
                FileUtil.createFile(filePath);
                OutputStream out = new BufferedOutputStream(new FileOutputStream(filePath));
                StreamUtil.copyStream(in, out);
            } catch (FileNotFoundException e) {
                LOGGER.error("upload file fail", e);
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 批量上传文件
     */
    public static void uploadFiles(String basePath, List<FileParam> fileParams) {
        if (CollectionUtil.isNotEmpty(fileParams)) {
            for (FileParam fileParam : fileParams) {
                uploadFile(basePath, fileParam);
            }
        }
    }

}
