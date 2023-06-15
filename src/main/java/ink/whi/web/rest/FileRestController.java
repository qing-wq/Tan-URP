package ink.whi.web.rest;

import ink.whi.api.model.context.ReqInfoContext;
import ink.whi.api.model.dto.FileDTO;
import ink.whi.api.model.exception.BusinessException;
import ink.whi.api.model.exception.StatusEnum;
import ink.whi.api.model.vo.ResVo;
import ink.whi.core.util.FileUtil;
import ink.whi.service.converter.FileConverter;
import ink.whi.service.file.FileDO;
import ink.whi.service.file.FileDao;
import ink.whi.service.meeting.MeetingDO;
import ink.whi.service.meeting.MeetingDao;
import ink.whi.service.user.UserDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * 文件上传下载接口
 *
 * @author: qing
 * @Date: 2023/5/8
 */
@Slf4j
@RestController
@RequestMapping(path = "file")
public class FileRestController {

    @Value("${file.upload.meeting}")
    private String uploadPath;

    @Autowired
    private MeetingDao meetingDao;
    @Autowired
    private FileDao fileDao;
    @Autowired
    private UserDao userDao;

    /**
     * 会议文件上传
     *
     * @param file
     * @param meetingId
     * @return 文件路径
     */
    @PostMapping(path = "upload")
    public ResVo<FileDTO> upload(@RequestParam(name = "file") MultipartFile file,
                                 @RequestParam(name = "meetingId") Long meetingId) {
        MeetingDO meeting = meetingDao.getById(meetingId);
        if (meeting == null) {
            return ResVo.fail(StatusEnum.RECORDS_NOT_EXISTS, "会议不存在：" + meetingId);
        }
        FileDTO fileDTO = saveFileToLocal(file, meetingId);
        return ResVo.ok(fileDTO);
    }

    /**
     * 根据ID获取文件
     * @param fileId
     * @return
     */
    @GetMapping(path = "/{fileId}")
    public ResVo<FileDTO> get(@PathVariable(name = "fileId") Long fileId) {
        FileDO file = fileDao.getById(fileId);
        if (file == null) {
            return ResVo.fail(StatusEnum.RECORDS_NOT_EXISTS, fileId);
        }
        FileDTO dto = FileConverter.toDto(file);
        dto.setUserInfo(userDao.queryBasicUserInfo(file.getUserId()));
        dto.setFileSize(FileUtil.getFileSize(file.getFilePath()));
        return ResVo.ok(dto);
    }

    /**
     * 文件下载
     *
     * @param fileId
     * @param response
     */
    @GetMapping("/download")
    public ResVo<String> download(@RequestParam(name = "fileId") Long fileId, HttpServletResponse response) {
        FileDO record = fileDao.getById(fileId);
        if (record == null) {
            return ResVo.fail(StatusEnum.RECORDS_NOT_EXISTS, fileId);
        }
        // keypoint: 文件以流的形式一次性读取到内存，通过响应输出流输出到前端
        String path = record.getFilePath();
        try {
            File file = new File(path);
            String filename = file.getName();

            // todo: 文件上传优化（缓冲区、断点传输）
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStream fis = new BufferedInputStream(fileInputStream);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();

            response.reset();
            response.setCharacterEncoding("UTF-8");
            // Content-Disposition的作用：告知浏览器以何种方式显示响应返回的文件，用浏览器打开还是以附件的形式下载到本地保存
            // attachment表示以附件方式下载   inline表示在线打开   "Content-Disposition: inline; filename=文件名.mp3"
            // filename表示文件的默认名称，因为网络传输只支持URL编码的相关支付，因此需要将文件名URL编码后进行传输,前端收到后需要反编码才能获取到真正的名称
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, StandardCharsets.UTF_8));
            // 告知浏览器文件的大小
            response.addHeader("Content-Length", "" + file.length());
            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            outputStream.write(buffer);
            outputStream.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return ResVo.ok("ok");
    }

    private FileDTO saveFileToLocal(MultipartFile file, Long meetingId) {
        try {
            String fileName = file.getOriginalFilename();
            String path = uploadPath + meetingId + File.separator + fileName;
            File uploadDir = new File(uploadPath + meetingId);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            File uploadFile = new File(path);
            file.transferTo(uploadFile);
            return fileDao.saveFileRecord(fileName, ReqInfoContext.getReqInfo().getUserId(), meetingId, path);
        } catch (Exception e) {
            e.printStackTrace();
            throw BusinessException.newInstance(StatusEnum.UNEXPECT_ERROR, e.getMessage());
        }
    }

    /**
     * 文件删除接口
     * @param fileId
     * @return
     */
    @GetMapping(path = "del/{fileId}")
    public ResVo<String> delete(@PathVariable(name = "fileId") Long fileId) {
        fileDao.deleteFile(fileId);
        return ResVo.ok("ok");
    }
}
