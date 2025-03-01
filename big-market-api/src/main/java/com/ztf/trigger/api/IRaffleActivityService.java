package com.ztf.trigger.api;

import com.ztf.trigger.api.dto.ActivityDrawRequestDTO;
import com.ztf.trigger.api.dto.ActivityDrawResponseDTO;
import com.ztf.types.model.Response;
import org.springframework.web.bind.annotation.RequestParam;

//抽奖活动服务
public interface IRaffleActivityService {

    /**
     * 活动装配，数据预热缓存
     * @param activityId 活动ID
     * @return 装配结果
     */
    Response<Boolean> armory(Long activityId);

    /**
     * 活动抽奖接口
     * @param request 请求对象
     * @return 返回结果
     */
    Response<ActivityDrawResponseDTO> draw(ActivityDrawRequestDTO request);


    /**
     * 日历签到返利接口
     *
     * @param userId 用户ID
     * @return 签到结果
     */
    Response<Boolean> calendarSignRebate(String userId);
}
