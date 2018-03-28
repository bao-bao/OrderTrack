package com.ordertrack.database;

/* Created by AMXPC on 2018/3/28. */

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.druid.pool.DruidPooledConnection;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

public class DbPoolConnection {
    private static DbPoolConnection databasePool=null;
    private static DruidDataSource dds = null;

    static {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("mysql.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            dds = (DruidDataSource) DruidDataSourceFactory
                    .createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private DbPoolConnection() {}

    public static synchronized DbPoolConnection getInstance() {
        if (null == databasePool) {
            databasePool = new DbPoolConnection();
        }
        return databasePool;
    }

    public DruidPooledConnection getConnection() throws SQLException {
        return dds.getConnection();
    }
}
