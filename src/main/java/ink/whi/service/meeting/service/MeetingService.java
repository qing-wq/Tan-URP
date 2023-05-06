package ink.whi.service.meeting.service;

import ink.whi.service.meeting.repo.entity.MeetingDO;

import java.util.List;

/**
 * @author: qing
 * @Date: 2023/5/3
 */
public interface MeetingService {

    /**
     * 查询所有会议记录
     * @return
     */
    List<MeetingDO> listMeeting();
}
