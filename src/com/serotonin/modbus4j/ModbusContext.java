package com.serotonin.modbus4j;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ModbusContext {

    // 通道名称
    private static final ThreadLocal<Long> mainThreadId = new ThreadLocal<Long>() {
        @Override
        protected Long initialValue() {
            return 0L;
        }
    };
    // 全局CRC大小端Map
    private static final Map<Long, Boolean> crcLittleEndian = new ConcurrentHashMap<>();

    private ModbusContext() {}

    public static Long getMainThreadId() {
        return mainThreadId.get();
    }

    public static void setMainThreadId(Long threadId) {
        mainThreadId.set(threadId);
    }

    public static void destroyMainThreadId() {
        mainThreadId.remove();
    }

    public static Boolean isCrcLittleEndian() {
        Long threadId = getMainThreadId();
        if (threadId != 0 && crcLittleEndian.containsKey(threadId)) {
            return crcLittleEndian.get(threadId);
        }
        return true;
    }

    public static void setCrcLittleEndian(Boolean littleEndian) {
        Long threadId = getMainThreadId();
        if (threadId != 0) {
            crcLittleEndian.put(threadId, littleEndian);
        }
    }

}
