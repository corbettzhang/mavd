package cn.monkeyapp.mavd.common.sqlite;


import cn.monkeyapp.mavd.cache.LocalCache;
import cn.monkeyapp.mavd.common.Properties;
import cn.monkeyapp.mavd.common.manage.LogManager;
import cn.monkeyapp.mavd.entity.Task;
import cn.monkeyapp.mavd.util.StringUtils;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Sqlite处理器
 *
 * @author Corbett Zhang
 * @date 2020年12月18日
 */
public class SqliteHandler<T> {

    private static final Logger LOGGER = LogManager.getLogger(SqliteHandler.class);

    private static final String TABLE_NAME_TASK = "task";

    private static final String CREATE_ABLE_TASK = "create table if not exists main.task\n" +
            "(\n" +
            "    id          INTEGER\n" +
            "        primary key autoincrement,\n" +
            "    url         CHAR(200) not null,\n" +
            "    title       CHAR(200),\n" +
            "    description CHAR(1000),\n" +
            "    type        CHAR(200),\n" +
            "    tag         CHAR(200),\n" +
            "    status      integer default 0 not null,\n" +
            "    has_subtitle      integer default 0 not null\n" +
            ");";
    private static Statement stmt;
    private static Connection connect;


    private void createTable() {
        try {
            if (tableIsExist()) {
                connect.setAutoCommit(false);
                stmt = connect.createStatement();
                stmt.executeUpdate(SqliteHandler.CREATE_ABLE_TASK);
                connect.commit();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    /**
     * 查询表是否存在
     *
     * @return 是否存在
     */
    private boolean tableIsExist() {
        boolean isExist = false;
        String sql = String.format("SELECT name FROM sqlite_master WHERE type = 'table' and tbl_name ='%s';", SqliteHandler.TABLE_NAME_TASK);
        try {
            connect.setAutoCommit(false);
            stmt = connect.createStatement();
            connect.commit();
            ResultSet result = stmt.executeQuery(sql);
            while (result.next()) {
                if (StringUtils.isNotEmptyOrNull(result.getString("name"))) {
                    isExist = true;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        return !isExist;
    }


    public SqliteHandler() {
        try {
            // 获取SQLite数据库链接
            Class.forName("org.sqlite.JDBC");
            connect = DriverManager.getConnection("jdbc:sqlite:" + LocalCache.getInstance().get(Properties.MONKEY_TOOLS_DB_KEY));
        } catch (SQLException | ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        createTable();
    }

    /**
     * 保存
     *
     * @param sql SQL语句
     * @return 是否保存成功
     * @throws SQLException SQL异常
     */
    public boolean save(String sql) throws SQLException {
        boolean saved;
        if (tableIsExist()) {
            createTable();
        }
        connect.setAutoCommit(false);
        PreparedStatement ps = connect.prepareStatement(sql);
        saved = ps.executeUpdate() == 1;
        connect.commit();
        connect.close();
        return saved;
    }

    /**
     * 删除
     *
     * @param delSql SQL语句
     * @return 是否删除成功
     * @throws SQLException SQL异常
     */
    public boolean delete(String delSql) throws SQLException {
        connect.setAutoCommit(false);
        stmt = connect.createStatement();
        int count = stmt.executeUpdate(delSql);
        boolean isDeleted = count > 0;
        connect.commit();
        stmt.close();
        connect.close();
        return isDeleted;
    }

    /**
     * 通用查询
     *
     * @param querySql 查询SQL语句
     * @param callBack 处理器
     * @return 查询结果
     * @throws SQLException 异常
     */
    public T queryCallBack(String querySql, SqliteCallBack<T> callBack) throws SQLException {
        if (tableIsExist()) {
            createTable();
        }
        connect.setAutoCommit(false);
        stmt = connect.createStatement();
        ResultSet result = stmt.executeQuery(querySql);
        T t = callBack.handler(result);
        connect.commit();
        result.close();
        stmt.close();
        connect.close();
        return t;
    }

    /**
     * 修改数据
     *
     * @param updateSql SQL语句
     * @return 是否修改成功
     * @throws SQLException SQL异常
     */
    public Boolean update(String updateSql) throws SQLException {
        connect.setAutoCommit(false);
        stmt = connect.createStatement();
        int count = stmt.executeUpdate(updateSql);
        boolean isUpdated = count > 0;
        connect.commit();
        stmt.close();
        connect.close();
        return isUpdated;
    }

    public static String appendSql(Task task, String... field) throws NoSuchFieldException, IllegalAccessException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("update main.task ");
        stringBuilder.append("set ");
        for (String s : field) {
            final Field field1 = Task.class.getDeclaredField(s);
            field1.setAccessible(true);
            Object value = field1.get(task);
            stringBuilder.append(s);
            stringBuilder.append("=");
            if (value instanceof Integer) {
                stringBuilder.append(value);
            } else if (value instanceof String) {
                stringBuilder.append("'").append(value).append("'");
            }
            stringBuilder.append(" ");
        }
        stringBuilder.append("where id=").append(task.getId());
        return stringBuilder.toString();
    }
}
