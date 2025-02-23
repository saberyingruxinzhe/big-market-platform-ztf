package com.ztf.domain.activity.service;

import com.ztf.domain.activity.repository.IActivityRepository;
import org.springframework.stereotype.Service;

//抽奖活动服务
@Service
public class RaffleActivityService extends AbstractRaffleActivity {

    public RaffleActivityService(IActivityRepository activityRepository) {
        super(activityRepository);
    }

}
