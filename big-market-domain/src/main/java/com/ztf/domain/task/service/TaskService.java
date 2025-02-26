package com.ztf.domain.task.service;

import com.ztf.domain.task.model.entity.TaskEntity;
import com.ztf.domain.task.repository.ITaskRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 防止异常的MQ发送失败
 * 以为MQ有概率发送失败，所以在保存数据到数据库的时候同时保存一份数据到task表格中，然后task转台设置为create
 * 然后新开一个事务用来发送MQ消息，并且将task中的状态更新为completed
 *
 * 上面的内容为award领域的实现功能
 * 下面为task领域的实现
 *
 * 如果MQ消息发送失败，就一定会有部分的task数据状态为create
 * 使用一个定时监听来扫描到这些task并且重发MQ消息，更新task状态
 */
@Service
public class TaskService implements ITaskService{

    @Resource
    private ITaskRepository taskRepository;

    //这里是在定时的扫描job中调用的，定时检查数据库中是否有失败或者create状态的task数据【即MQ发送失败】
    //如果有就说明MQ发送失败，就重新发送MQ并且更新task状态为completed
    @Override
    public List<TaskEntity> queryNoSendMessageTaskList() {
        return taskRepository.queryNoSendMessageTaskList();
    }

    //用来在检查到有MQ发送失败的时候重新发送MQ消息
    @Override
    public void sendMessage(TaskEntity taskEntity) {
        taskRepository.sendMessage(taskEntity);
    }

    //重新发送完MQ消息后，更新task状态为completed
    @Override
    public void updateTaskSendMessageCompleted(String userId, String messageId) {
        taskRepository.updateTaskSendMessageCompleted(userId, messageId);
    }

    //如果上述的更新还是失败的话，就将状态更新为fail
    @Override
    public void updateTaskSendMessageFail(String userId, String messageId) {
        taskRepository.updateTaskSendMessageFail(userId, messageId);
    }
}
