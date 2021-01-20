package cn.monkeyapp.mavd.common.sqlite;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * sqlite回调接口
 *
 * @param <T> 返回结果集类型
 * @author Corbett Zhang
 */
public interface SqliteCallBack<T> {

    /**
     * sqlite处理器
     *
     * @param set java.sql.ResultSet
     * @return 结果集
     * @throws SQLException sql执行异常
     */
    T handler(ResultSet set) throws SQLException;

}