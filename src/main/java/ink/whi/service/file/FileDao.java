package ink.whi.service.file;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ink.whi.api.model.dto.FileDTO;
import ink.whi.api.model.dto.base.BaseDO;
import ink.whi.api.model.enums.YesOrNoEnum;
import ink.whi.service.converter.FileConverter;
import ink.whi.service.user.repo.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

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
        file.setCreateTime(new Date());
        save(file);
        return fillFileInfo(file);
    }

    /**
     * 查询文件列表
     * @param meetingId
     * @return
     */
    public List<FileDTO> listFileByMeetingId(String meetingId) {
        List<FileDO> list = lambdaQuery().eq(FileDO::getMeetId, meetingId)
                .eq(FileDO::getDeleted, YesOrNoEnum.NO.getCode())
                .orderByDesc(BaseDO::getCreateTime)
                .list();
        return buildListFileDTO(list);
    }

    /**
     * 获取用户上传的文件
     * @param userId
     * @return
     */
    public List<FileDTO> listFileByUserId(Long userId) {
        List<FileDO> list = lambdaQuery().eq(FileDO::getUserId, userId)
                .eq(FileDO::getDeleted, YesOrNoEnum.NO.getCode())
                .orderByDesc(BaseDO::getCreateTime)
                .list();
        return buildListFileDTO(list);
    }


    public List<FileDTO> buildListFileDTO(List<FileDO> list) {
        return list.stream().map(this::fillFileInfo).toList();
    }

    /**
     * 补充上传文件的用户信息
     * @param file
     * @return
     */
    public FileDTO fillFileInfo(FileDO file) {
        FileDTO dto = FileConverter.toDto(file);
        dto.setUserInfo(userDao.queryBasicUserInfo(file.getUserId()));
        return dto;
    }
}
