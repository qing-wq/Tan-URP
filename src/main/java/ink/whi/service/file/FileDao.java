package ink.whi.service.file;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ink.whi.api.model.dto.FileDTO;
import ink.whi.service.converter.FileConverter;
import ink.whi.service.user.repo.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author: qing
 * @Date: 2023/5/8
 */
@Repository
public class FileDao extends ServiceImpl<FileMapper, FileDO> {

    @Autowired
    private UserDao userDao;
    /**
     * 保存文件上传信息
     * @param fileName
     * @param userId
     * @param meetingId
     * @param filePath
     * @return
     */
    public FileDTO saveFileRecord(String fileName, Long userId, Long meetingId, String filePath) {
        FileDO file = new FileDO();
        file.setFileName(fileName);
        file.setUserId(userId);
        file.setMeetId(meetingId);
        file.setFilePath(filePath);
        file.setDownload(0);
        save(file);
        FileDTO dto = FileConverter.toDto(file);
        dto.setUserInfo(userDao.queryBasicUserInfo(userId));
        return dto;
    }

}
