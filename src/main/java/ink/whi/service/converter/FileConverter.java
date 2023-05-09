package ink.whi.service.converter;

import ink.whi.api.model.dto.FileDTO;
import ink.whi.service.file.FileDO;
import ink.whi.service.user.repo.UserDao;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: qing
 * @Date: 2023/5/8
 */
public class FileConverter {

    public static FileDTO toDto(FileDO fileDO) {
        FileDTO dto = new FileDTO();
        BeanUtils.copyProperties(fileDO, dto);
        dto.setFileId(fileDO.getId());
        return dto;
    }
}
