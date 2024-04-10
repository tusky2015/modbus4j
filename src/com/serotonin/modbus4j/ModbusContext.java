package com.serotonin.modbus4j;

public class ModbusContext {

    // crc校验大小端
    private static final ThreadLocal<Boolean> crcLittleEndian = new ThreadLocal<Boolean>() {
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
