package com.example.authserver;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.AbstractEventListener;
import com.alibaba.nacos.api.naming.listener.Event;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * @author: 长安
 */
public class NacosTests {


    @Test
    void test_nacosNaming() throws NacosException, InterruptedException {
        Properties properties = new Properties();
        properties.setProperty("serverAddr", System.getProperty("serverAddr", "118.31.44.4:8848"));
        properties.setProperty("namespace", System.getProperty("namespace", "f17c01aa-6a56-43e0-a64e-12cc2f04d88f"));
        properties.setProperty("username", "cloud-rw");
        properties.setProperty("password", "cloud&master001");
//        properties.setProperty("username", "nacos");
//        properties.setProperty("password", "nacos&master001");

        NamingService naming = NamingFactory.createNamingService(properties);

//        naming.registerInstance("nacos.test.3", "11.11.11.11", 8888, "TEST1");

        naming.registerInstance("aaaaa", "DEFAULT_GROUP", "11.11.11.11", 8888);

        System.out.println("instances after register: " + naming.getAllInstances("nacos.test.3"));

        Executor executor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(),
            runnable -> {
                Thread thread = new Thread(runnable);
                thread.setName("test-thread");
                return thread;
            });

        naming.subscribe("nacos.test.3", new AbstractEventListener() {

            //EventListener onEvent is sync to handle, If process too low in onEvent, maybe block other onEvent callback.
            //So you can override getExecutor() to async handle event.
            @Override
            public Executor getExecutor() {
                return executor;
            }

            @Override
            public void onEvent(Event event) {
                System.out.println("serviceName: " + ((NamingEvent) event).getServiceName());
                System.out.println("instances from event: " + ((NamingEvent) event).getInstances());
            }
        });

        naming.deregisterInstance("nacos.test.3", "11.11.11.11", 8888, "TEST1");

        Thread.sleep(1000);

        System.out.println("instances after deregister: " + naming.getAllInstances("nacos.test.3"));

        Thread.sleep(1000);
    }


    @Test
    void test_zz() {
        String resource = ":*:*";
        String permissionResource = resource.replaceAll("\\*", ".*");

        StringBuilder sb = new StringBuilder();
        sb.append("public"); // namespaceId
        sb.append(":DEFAULT_GROUP"); // group
        sb.append(":aaaaa"); // serviceName
        sb.append("naming/aaaaa");
        System.out.println( sb.toString() );

        System.out.println(Pattern.matches(permissionResource, sb.toString()));

    }

    @Test
    void test_generalToken() throws UnsupportedEncodingException {

        String  secret = UUID.randomUUID().toString().replace("-", "");

        byte[] encode = Base64.getEncoder().encode(secret.getBytes("UTF-8"));

        System.out.println(new String(encode, "UTF-8"));
    }

    @Test
    void test_initPassword() {
//        new BCryptPasswordEncoder().matches(raw, encoded);
        String encode = new BCryptPasswordEncoder().encode("123456");
        System.out.println(encode);
    }

}
