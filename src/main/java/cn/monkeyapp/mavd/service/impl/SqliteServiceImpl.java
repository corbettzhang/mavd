package cn.monkeyapp.mavd.service.impl;

import cn.monkeyapp.mavd.common.manage.LogManager;
import cn.monkeyapp.mavd.common.sqlite.SqliteHandler;
import cn.monkeyapp.mavd.common.MySystemTray;
import cn.monkeyapp.mavd.entity.Task;
import cn.monkeyapp.mavd.service.SqliteService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Corbett Zhang
 */
public class SqliteServiceImpl implements SqliteService {

    private static final Logger LOGGER = LogManager.getLogger(MySystemTray.class);

    @Override
    public boolean save(Task task) {
        String insertSql = String.format("insert into main.task (url,title,description,`type`,tag) " +
                "values ('%s','%s','%s','%s','%s');", task.getUrl(), task.getTitle(), task.getDescription(), task.getType(), task.getTag());
        SqliteHandler sqliteHandler = new SqliteHandler();
        try {
            return sqliteHandler.save(insertSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public int queryLastId() {
        int id = -1;
        String sql = "select max(id) from task;";
        try {
            SqliteHandler<Integer> sqliteHandler = new SqliteHandler<>();
            id = sqliteHandler.queryCallBack(sql, result -> {
                if (result.next()) {
                    return result.getInt(1);
                }
                return -1;
            });
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        return id;
    }

    @Override
    public boolean deleteById(int id) {
        String sql = "delete from main.task where id = " + id;
        SqliteHandler sqliteHandler = new SqliteHandler();
        try {
            return sqliteHandler.delete(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Map<String, Integer> queryTaskStatus() {
        String sql = "\n" +
                "select s1.initial, s2.active, s3.completed\n" +
                "from (select count(status) initial from task where status = 0) s1,\n" +
                "     (select count(status) active from task where status = 2) s2,\n" +
                "     (select count(status) completed from task where status = 3) s3;";
        try {
            SqliteHandler<Map<String, Integer>> help = new SqliteHandler<>();
            return help.queryCallBack(sql, set -> {
                Map<String, Integer> map = new HashMap<>();
                map.put("initial", set.getInt("initial"));
                map.put("active", set.getInt("active"));
                map.put("completed", set.getInt("completed"));
                return map;
            });
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }

    @Override
    public int queryTaskThreshold(int i) {
        String sql = "SELECT count(id) FROM main.task where status=" + i;
        SqliteHandler<Integer> handler = new SqliteHandler<>();
        try {
            return handler.queryCallBack(sql, result -> {
                if (result.next()) {
                    return result.getInt(1);
                }
                return -1;
            });
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        return -1;
    }

    @Override
    public List<Task> queryTaskPage(int i) {
        List<Task> taskList = null;
        String sql = String.format("SELECT * FROM main.task where status=1 order by id  limit %s;", i);
        SqliteHandler<List<Task>> handler = new SqliteHandler<>();
        try {
            taskList = handler.queryCallBack(sql, result -> {
                List<Task> temp = new ArrayList<>();
                while (result.next()) {
                    final Task task = new Task();
                    task.setId(result.getInt("id"));
                    task.setUrl(result.getString("url"));
                    task.setTitle(result.getString("title"));
                    task.setDescription(result.getString("description"));
                    task.setTag(result.getString("tag"));
                    task.setType(result.getString("type"));
                    task.setStatus(result.getInt("status"));
                    task.setHasSubTitle(result.getInt("has_subtitle"));
                    temp.add(task);
                }
                return temp;
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return taskList;
    }

    @Override
    public List<Task> getTaskList() {
        List<Task> taskList = null;
        String sql = "SELECT * FROM main.task";
        SqliteHandler<List<Task>> handler = new SqliteHandler<>();
        try {
            taskList = handler.queryCallBack(sql, result -> {
                List<Task> temp = new ArrayList<>();
                while (result.next()) {
                    final Task task = new Task();
                    task.setId(result.getInt("id"));
                    task.setUrl(result.getString("url"));
                    task.setTitle(result.getString("title"));
                    task.setDescription(result.getString("description"));
                    task.setTag(result.getString("tag"));
                    task.setType(result.getString("type"));
                    task.setStatus(result.getInt("status"));
                    temp.add(task);
                }
                return temp;
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return taskList;
    }

    @Override
    public boolean update(String sql) {
        SqliteHandler handler = new SqliteHandler();
        try {
            return handler.update(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateExecute() {
        return update("update main.task set status = 1 where status = 0;");
    }
}

