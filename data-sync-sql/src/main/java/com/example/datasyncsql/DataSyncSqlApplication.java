package com.example.datasyncsql;

import com.ververica.cdc.connectors.mysql.source.MySqlSource;
import com.ververica.cdc.debezium.JsonDebeziumDeserializationSchema;
import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DataSyncSqlApplication {

    /*
    driver-class-name: com.mysql.cj.jdbc.Driver
        jdbcUrl: jdbc:mysql://123.57.175.74:13306/self_database
        url: jdbc:mysql://123.57.175.74:13306/self_database
        username: root
        password: mysqlMaster001
        #    初始化数量
        initialSize: 5
        #    允许的最小空闲连接数11
        minIdle: 5
        #    最大活跃数
        maxActive: 20
        #    最大连接等待超时时间，单位是毫秒(ms)
        maxWait: 60000
        hikari:
            maximum-pool-size: 5
     */
    public static void main(String[] args) throws Exception {
//        SpringApplication.run(DataSyncSqlApplication.class, args);

        MySqlSource<String> mySqlSource = MySqlSource.<String>builder()
            .hostname("123.57.175.74")
            .port(13306)
            .databaseList("nacos_server_table")
            .tableList("nacos_server_table.users")
            .username("root")
            .password("mysqlMaster001")
            .deserializer(new JsonDebeziumDeserializationSchema())
            .build();

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.enableCheckpointing(3000); // 检查间隔
        env.getConfig().disableClosureCleaner(); // 关闭清除
        env.fromSource(mySqlSource, WatermarkStrategy.noWatermarks(), "MySQL Source")
            .setParallelism(4)
            .print()
            .setParallelism(1)
        ;

        JobExecutionResult execute = env.execute("Print MySQL Snapshot + Binlog");

    }

}
