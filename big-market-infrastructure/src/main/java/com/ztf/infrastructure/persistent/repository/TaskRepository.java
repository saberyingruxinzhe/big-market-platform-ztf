package com.ztf.infrastructure.persistent.repository;

import com.ztf.domain.task.model.entity.TaskEntity;
import com.ztf.domain.task.repository.ITaskRepository;
import com.ztf.infrastructure.event.EventPublisher;
import com.ztf.infrastructure.persistent.dao.ITaskDao;
import com.ztf.infrastructure.persistent.po.Task;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TaskRepository implements ITaskRepository {

    @Resource
    private ITaskDao taskDao;
    @Resource
    private EventPublisher eventPublisher;

    @Override
    public List<TaskEntity> queryNoSendMessageTaskList() {
        //这里获取到的task条件为
        // state = 'fail' or (state = 'create' and now() - update_time > 6
        //这里是在定时的扫描job中调用的，定时检查数据库中是否有这样的task数据
        //如果有就说明MQ发送失败，就重新发送MQ并且更新task状态为completed
        List<Task> tasks = taskDao.queryNoSendMessageTaskList();
        List<TaskEntity> taskEntities = new ArrayList<>(tasks.size());
        for (Task task : tasks) {
            TaskEntity taskEntity = new TaskEntity();
            taskEntity.setUserId(task.getUserId());
            taskEntity.setTopic(task.getTopic());
            taskEntity.setMessageId(task.getMessageId());
            taskEntity.setMessage(task.getMessage());
            taskEntities.add(taskEntity);
        }
        return taskEntities;
    }

    //用来在检查到有MQ发送失败的时候重新发送MQ消息
    @Override
    public void sendMessage(TaskEntity taskEntity) {
        eventPublisher.publish(taskEntity.getTopic(), taskEntity.getMessage());
    }

    //重新发送完MQ消息后，更新task状态为completed
    //将对应userId、messageId的task的状态改为completed
    @Override
    public void updateTaskSendMessageCompleted(String userId, String messageId) {
        Task taskReq = new Task();
        taskReq.setUserId(userId);
        taskReq.setMessageId(messageId);
        taskDao.updateTaskSendMessageCompleted(taskReq);
    }

    //如果上述的更新还是失败的话，就将状态更新为fail
    //将对应userId、messageId的task的状态改为fail
    @Override
    public void updateTaskSendMessageFail(String userId, String messageId) {
        Task taskReq = new Task();
        taskReq.setUserId(userId);
        taskReq.setMessageId(messageId);
        taskDao.updateTaskSendMessageFail(taskReq);
    }
}
