package com.serotonin.modbus4j;

import com.alibaba.ttl.TransmittableThreadLocal;

public class ModbusContext {

    // crc校验大小端
    private static final TransmittableThreadLocal<Boolean> crcLittleEndian = new TransmittableThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() {
            return true;
        }
    };

    public ModbusContext() {}

    public static Boolean isCrcLittleEndian() {
        return crcLittleEndian.get();
    }

    public static void setLittleEndian(Boolean littleEndian) {
        crcLittleEndian.set(littleEndian);
    }

    public static void destroyCrcLittleEndian() {
        crcLittleEndian.remove();
    }

}
