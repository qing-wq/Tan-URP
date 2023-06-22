package ink.whi.service.file;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ink.whi.api.model.context.ReqInfoContext;
import ink.whi.api.model.dto.FileDTO;
import ink.whi.api.model.dto.base.BaseDO;
import ink.whi.api.model.enums.YesOrNoEnum;
import ink.whi.api.model.exception.BusinessException;
import ink.whi.api.model.exception.StatusEnum;
import ink.whi.api.model.vo.ResVo;
import ink.whi.service.converter.FileConverter;
import ink.whi.service.user.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Objects;

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
     *
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
        FileDO record = getFileByFileNameAndMeetingID(fileName, meetingId);  // 上传文件冲突
        if (record != null) {
            file.setId(record.getId());
            updateById(record);
        } else {
            save(file);
        }
        return fillFileInfo(file);
    }

    private FileDO getFileByFileNameAndMeetingID(String fileName, Long MeetingId) {
        List<FileDO> list = lambdaQuery().eq(FileDO::getFileName, fileName)
                .eq(FileDO::getMeetId, MeetingId)
                .eq(FileDO::getDeleted, YesOrNoEnum.NO.getCode())
                .list();
        return CollectionUtils.isEmpty(list) ? null : list.get(0);
    }

    /**
     * 查询文件列表
     *
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
     *
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
     *
     * @param file
     * @return
     */
    public FileDTO fillFileInfo(FileDO file) {
        FileDTO dto = FileConverter.toDto(file);
        dto.setUserInfo(userDao.queryBasicUserInfo(file.getUserId()));
        return dto;
    }

    /**
     * 删除文件
     * @param fileId
     */
    public void deleteFile(Long fileId) {
        FileDO record = getById(fileId);
        if (record == null || record.getDeleted() == YesOrNoEnum.YES.getCode()) {
            throw BusinessException.newInstance(StatusEnum.RECORDS_NOT_EXISTS, fileId);
        }
        Long userId = ReqInfoContext.getReqInfo().getUserId();
        if (!Objects.equals(userId, record.getUserId())) {
            throw BusinessException.newInstance(StatusEnum.FORBID_ERROR_MIXED, "您无法操作其他人的文件");
        }
        record.setDeleted(YesOrNoEnum.YES.getCode());
        updateById(record);
    }
}
