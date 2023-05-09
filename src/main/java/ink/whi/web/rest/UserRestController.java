package ink.whi.web.rest;

import ink.whi.api.model.dto.BaseUserInfoDTO;
import ink.whi.api.model.dto.FileDTO;
import ink.whi.api.model.exception.BusinessException;
import ink.whi.api.model.exception.StatusEnum;
import ink.whi.api.model.vo.ResVo;
import ink.whi.service.file.FileDao;
import ink.whi.service.user.repo.UserDao;
import ink.whi.web.vo.UserDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户接口
 * @author: qing
 * @Date: 2023/5/7
 */
@RestController
@RequestMapping(path = "user")
public class UserRestController {
    @Autowired
    private UserDao userDao;
    @Autowired
    private FileDao fileDao;

    @GetMapping(path = "/{userId}")
    public ResVo<UserDetailVo> getUserInfo(@PathVariable(name = "userId") Long userId) {
        UserDetailVo vo = new UserDetailVo();
        BaseUserInfoDTO user = userDao.queryBasicUserInfo(userId);
        if (user == null) {
            throw BusinessException.newInstance(StatusEnum.RECORDS_NOT_EXISTS, "用户不存在");
        }
        vo.setUserInfo(user);

        List<FileDTO> fileList = fileDao.listFileByUserId(userId);
        vo.setFileList(fileList);
        return ResVo.ok(vo);
    }
}
