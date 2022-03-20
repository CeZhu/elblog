package com.example.elblog.util;

import cn.hutool.core.util.IdUtil;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelUtil;
import com.example.elblog.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author 朱策
 */
@Slf4j
@Component
public class FileUtil {

    public static final String WINDOWS_PATH = "d:/uploadImage/";

    public static final String WINDOWS_TEMP = "e:/temp/";

    public static final String LINUX_PATH = "/home/elblog/uploadImage/";

    public static final String SYS_TEM_DIR = System.getProperty("java.io.tmpdir") + File.separator;

    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public static final String SERVER_IP = "8.136.44.26";

    public static String upload(MultipartFile file, HttpServletRequest request) {
        String format = sdf.format(new Date());
        File folder;
        String OS = System.getProperty("os.name").toLowerCase();
        if (OS.contains("windows")) {
            folder = new File(WINDOWS_PATH + format);
        } else {
            folder = new File(LINUX_PATH + format);
        }
        if (!folder.isDirectory()) {
            boolean mkdir = folder.mkdirs();
            System.out.println("mkdir=" + mkdir);
            if (!mkdir) {
                throw new BadRequestException("创建文件夹失败");
            }
        }

        String oldName = file.getOriginalFilename();
        String newName = UUID.randomUUID() + oldName.substring(oldName.lastIndexOf("."));

        try {
            String filePath = request.getScheme() + "://" + SERVER_IP + ":" + request.getServerPort() + "/" + format + "/" + newName;
            System.out.println(filePath);
            file.transferTo(new File(folder, newName));
            return filePath;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new BadRequestException("文件保存失败");
        }
    }

    public static void downloadExcel(List<Map<String, Object>> list, HttpServletResponse response) throws IOException {
        String fileName = IdUtil.fastSimpleUUID()+".xlsx";
        File file = new File(SYS_TEM_DIR,fileName);
        String absolutePath = file.getAbsolutePath();
        System.out.println(absolutePath);
        BigExcelWriter writer = ExcelUtil.getBigWriter(file);
        writer.write(list, true);
        SXSSFSheet sheet = (SXSSFSheet)writer.getSheet();
        //上面需要强转SXSSFSheet  不然没有trackAllColumnsForAutoSizing方法
        sheet.trackAllColumnsForAutoSizing();
        writer.autoSizeColumnAll();
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=file.xlsx");
        ServletOutputStream out = response.getOutputStream();
        file.deleteOnExit();
        writer.flush(out, true);
        writer.close();
    }
}
