package com.example.authserver.server.common.custom.store;

import com.example.authserver.server.common.custom.ex.SmsCodeStoreException;
import io.micrometer.common.util.StringUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static java.lang.String.format;

/**
 * @author: 长安
 */
public class SmsCodeStoreServiceImpl implements SmsCodeStoreService {

    private long expireMillis;

    private Map<String/*mobile*/, String/*smsCode*/> smsCodeStore = new ConcurrentHashMap<>();
    private Map<String/*mobile*/, Instant/*expire time*/> smsCodeExpire = new ConcurrentHashMap<>();

    public SmsCodeStoreServiceImpl() {
        this(30, TimeUnit.MINUTES);
    }

    public SmsCodeStoreServiceImpl(long expireTime, TimeUnit expireTimeUnit) {
        this.expireMillis = expireTimeUnit.toMillis(expireTime);

        // for testing
        smsCodeStore.put("18846439952", "123456");
        smsCodeExpire.put("18846439952", Instant.now().plus(100, ChronoUnit.DAYS));
    }

    @Override
    public void setSmsCode(String mobile, String smsCode) {
        if(smsCodeStore.containsKey(mobile)) {
            evictSmsCode(mobile);
        }
        Instant expire = Instant.now().plusMillis(expireMillis);
        smsCodeStore.put(mobile, smsCode);
        smsCodeExpire.put(mobile, expire);
    }

    @Override
    public boolean hasSmsCode(String mobile) {
        if(!smsCodeStore.containsKey(mobile)) {
            return false;
        }
        Instant expire = smsCodeExpire.get(mobile);
        return expire.isAfter(Instant.now());
    }

    @Override
    public String getSmsCode(String mobile) {
        if(!hasSmsCode(mobile)) {
            return null;
        }
        return smsCodeStore.get(mobile);
    }

    @Override
    public boolean evictSmsCode(String mobile) {
        String r1 = smsCodeStore.remove(mobile);
        Instant r2 = smsCodeExpire.remove(mobile);
        return StringUtils.isNotBlank(r1) && Objects.nonNull(r2);
    }
}
