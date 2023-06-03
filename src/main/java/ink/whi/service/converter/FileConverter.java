package ink.whi.service.converter;

import ink.whi.api.model.dto.FileDTO;
import ink.whi.service.file.FileDO;
import org.springframework.beans.BeanUtils;

import java.util.List;

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

    public static List<FileDTO> toDtoList(List<FileDO> list) {
        return list.stream().map(FileConverter::toDto).toList();
    }
}
