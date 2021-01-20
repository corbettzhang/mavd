package cn.monkeyapp.mavd.service;

import cn.monkeyapp.mavd.entity.Task;

import java.util.List;
import java.util.Map;

/**
 * @author Corbett Zhang
 */
public interface SqliteService {

    /**
     * 保存数据
     *
     * @return task
     */
    boolean save(Task task);

    /**
     * 查询最后增加的id
     *
     * @return id
     */
    int queryLastId();

    /**
     * 删除数据
     *
     * @param id id
     * @return 删除成功
     */
    boolean deleteById(int id);

    /**
     * 查询数据
     *
     * @return 集合
     */
    List<Task> getTaskList();

    /**
     * 修改数据
     *
     * @param sql sql
     * @return 修改是否成功
     */
    boolean update(String sql);

    /**
     * 主界面执行操作
     *
     * @return 修改是否成功
     */
    boolean updateExecute();

    /**
     * 统计任务数，进行中，已完成
     *
     * @return
     */
    Map<String, Integer> queryTaskStatus();

    /**
     * 查询任务状态为2的任务数量
     *
     * @param i 状态
     * @return 任务数量
     */
    int queryTaskThreshold(int i);

    /**
     * 查询指定数量的任务
     *
     * @param limit 分页数量
     * @return 任务集合
     */
    List<Task> queryTaskPage(int limit);
}
