/*
 * ============================================================================
 * GNU General Public License
 * ============================================================================
 *
 * Copyright (C) 2006-2011 Serotonin Software Technologies Inc. http://serotoninsoftware.com
 * @author Matthew Lohbihler
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.serotonin.modbus4j.base;

import com.serotonin.modbus4j.ModbusConfig;
import com.serotonin.modbus4j.ModbusContext;
import com.serotonin.modbus4j.code.RegisterRange;
import com.serotonin.modbus4j.exception.IllegalSlaveIdException;
import com.serotonin.modbus4j.exception.ModbusIdException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.msg.ModbusMessage;
import com.serotonin.modbus4j.sero.util.queue.ByteQueue;

/**
 * <p>ModbusUtils class.</p>
 *
 * @author Matthew Lohbihler
 * @version 5.0.0
 */
public class ModbusUtils {
    /** Constant <code>TCP_PORT=502</code> */
    public static final int TCP_PORT = 502;
    /** Constant <code>IP_PROTOCOL_ID=0</code> */
    public static final int IP_PROTOCOL_ID = 0; // Modbus protocol

    //    public static final int MAX_READ_BIT_COUNT = 2000;
    //    public static final int MAX_READ_REGISTER_COUNT = 125;
    //    public static final int MAX_WRITE_REGISTER_COUNT = 120;

    /**
     * <p>pushByte.</p>
     *
     * @param queue a {@link com.serotonin.modbus4j.sero.util.queue.ByteQueue} object.
     * @param value a int.
     */
    public static void pushByte(ByteQueue queue, int value) {
        queue.push((byte) value);
    }

    /**
     * <p>pushShort.</p>
     *
     * @param queue a {@link com.serotonin.modbus4j.sero.util.queue.ByteQueue} object.
     * @param value a int.
     */
    public static void pushShort(ByteQueue queue, int value) {
        queue.push((byte) (0xff & (value >> 8)));
        queue.push((byte) (0xff & value));
    }

    /**
     * <p>popByte.</p>
     *
     * @param queue a {@link com.serotonin.modbus4j.sero.util.queue.ByteQueue} object.
     * @return a int.
     */
    public static int popByte(ByteQueue queue) {
        return queue.pop();
    }

    /**
     * <p>popUnsignedByte.</p>
     *
     * @param queue a {@link com.serotonin.modbus4j.sero.util.queue.ByteQueue} object.
     * @return a int.
     */
    public static int popUnsignedByte(ByteQueue queue) {
        return queue.pop() & 0xff;
    }

    /**
     * <p>popShort.</p>
     *
     * @param queue a {@link com.serotonin.modbus4j.sero.util.queue.ByteQueue} object.
     * @return a int.
     */
    public static int popShort(ByteQueue queue) {
        return toShort(queue.pop(), queue.pop());
    }

    /**
     * <p>popUnsignedShort.</p>
     *
     * @param queue a {@link com.serotonin.modbus4j.sero.util.queue.ByteQueue} object.
     * @return a int.
     */
    public static int popUnsignedShort(ByteQueue queue) {
        return ((queue.pop() & 0xff) << 8) | (queue.pop() & 0xff);
    }

    /**
     * <p>toShort.</p>
     *
     * @param b1 a byte.
     * @param b2 a byte.
     * @return a short.
     */
    public static short toShort(byte b1, byte b2) {
        return (short) ((b1 << 8) | (b2 & 0xff));
    }

    /**
     * <p>toByte.</p>
     *
     * @param value a short.
     * @param first a boolean.
     * @return a byte.
     */
    public static byte toByte(short value, boolean first) {
        if (first)
            return (byte) (0xff & (value >> 8));
        return (byte) (0xff & value);
    }

    /**
     * <p>validateRegisterRange.</p>
     *
     * @param range a int.
     */
    public static void validateRegisterRange(int range) {
        if (RegisterRange.getReadFunctionCode(range) == -1)
            throw new ModbusIdException("Invalid register range: " + range);
    }

    /**
     * <p>validateSlaveId.</p>
     *
     * @param slaveId a int.
     * @param includeBroadcast a boolean.
     */
    public static void validateSlaveId(int slaveId, boolean includeBroadcast) {
        if (slaveId < (includeBroadcast ? 0 : 1) /* || slaveId > 240 */)
            throw new IllegalSlaveIdException("Invalid slave id: " + slaveId);
    }

    /**
     * <p>validateBit.</p>
     *
     * @param bit a int.
     */
    public static void validateBit(int bit) {
        if (bit < 0 || bit > 15)
            throw new ModbusIdException("Invalid bit: " + bit);
    }

    /**
     * <p>validateOffset.</p>
     *
     * @param offset a int.
     * @throws com.serotonin.modbus4j.exception.ModbusTransportException if any.
     */
    public static void validateOffset(int offset) throws ModbusTransportException {
        if (offset < 0 || offset > 65535)
            throw new ModbusTransportException("Invalid offset: " + offset);
    }

    /**
     * <p>validateEndOffset.</p>
     *
     * @param offset a int.
     * @throws com.serotonin.modbus4j.exception.ModbusTransportException if any.
     */
    public static void validateEndOffset(int offset) throws ModbusTransportException {
        if (offset > 65535)
            throw new ModbusTransportException("Invalid end offset: " + offset);
    }

    /**
     * <p>checkCRC.</p>
     *
     * @param modbusMessage a {@link com.serotonin.modbus4j.msg.ModbusMessage} object.
     * @param queue a {@link com.serotonin.modbus4j.sero.util.queue.ByteQueue} object.
     * @throws com.serotonin.modbus4j.exception.ModbusTransportException if any.
     */
    public static void checkCRC(ModbusMessage modbusMessage, ByteQueue queue) throws ModbusTransportException {
        // Check the CRC
        int calcCrc = calculateCRC(modbusMessage);
        int givenCrc = ModbusUtils.popUnsignedShort(queue);

        if (calcCrc != givenCrc)
            throw new ModbusTransportException("CRC mismatch: given=" + givenCrc + ", calc=" + calcCrc,
                    modbusMessage.getSlaveId());
    }

    /**
     * <p>calculateCRC.</p>
     *
     * @param modbusMessage a {@link com.serotonin.modbus4j.msg.ModbusMessage} object.
     * @return a int.
     */
    public static int calculateCRC(ModbusMessage modbusMessage) {
        ByteQueue queue = new ByteQueue();
        modbusMessage.write(queue);

        int high = 0xff;
        int low = 0xff;
        int nextByte = 0;
        int uIndex;

        while (queue.size() > 0) {
            nextByte = 0xFF & queue.pop();
            uIndex = high ^ nextByte;
            high = low ^ lookupCRCHi[uIndex];
            low = lookupCRCLo[uIndex];
        }

        // rtu的crc校验为小端
        if (ModbusContext.isCrcLittleEndian()) {
            return (high << 8) | low;
        } else {    // rtu的crc校验为大端
            return (low << 8) | high;
        }
    }

    // Table of CRC values for high-order byte
    private final static short[] lookupCRCHi = { 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80,
            0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01,
            0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80,
            0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x01,
            0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81,
            0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01,
            0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81,
            0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x00,
            0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80,
            0x41, 0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00,
            0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80,
            0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x01,
            0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80,
            0x41, 0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x01,
            0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40 };

    // Table of CRC values for low-order byte
    private final static short[] lookupCRCLo = { 0x00, 0xC0, 0xC1, 0x01, 0xC3, 0x03, 0x02, 0xC2, 0xC6, 0x06, 0x07,
            0xC7, 0x05, 0xC5, 0xC4, 0x04, 0xCC, 0x0C, 0x0D, 0xCD, 0x0F, 0xCF, 0xCE, 0x0E, 0x0A, 0xCA, 0xCB, 0x0B, 0xC9,
            0x09, 0x08, 0xC8, 0xD8, 0x18, 0x19, 0xD9, 0x1B, 0xDB, 0xDA, 0x1A, 0x1E, 0xDE, 0xDF, 0x1F, 0xDD, 0x1D, 0x1C,
            0xDC, 0x14, 0xD4, 0xD5, 0x15, 0xD7, 0x17, 0x16, 0xD6, 0xD2, 0x12, 0x13, 0xD3, 0x11, 0xD1, 0xD0, 0x10, 0xF0,
            0x30, 0x31, 0xF1, 0x33, 0xF3, 0xF2, 0x32, 0x36, 0xF6, 0xF7, 0x37, 0xF5, 0x35, 0x34, 0xF4, 0x3C, 0xFC, 0xFD,
            0x3D, 0xFF, 0x3F, 0x3E, 0xFE, 0xFA, 0x3A, 0x3B, 0xFB, 0x39, 0xF9, 0xF8, 0x38, 0x28, 0xE8, 0xE9, 0x29, 0xEB,
            0x2B, 0x2A, 0xEA, 0xEE, 0x2E, 0x2F, 0xEF, 0x2D, 0xED, 0xEC, 0x2C, 0xE4, 0x24, 0x25, 0xE5, 0x27, 0xE7, 0xE6,
            0x26, 0x22, 0xE2, 0xE3, 0x23, 0xE1, 0x21, 0x20, 0xE0, 0xA0, 0x60, 0x61, 0xA1, 0x63, 0xA3, 0xA2, 0x62, 0x66,
            0xA6, 0xA7, 0x67, 0xA5, 0x65, 0x64, 0xA4, 0x6C, 0xAC, 0xAD, 0x6D, 0xAF, 0x6F, 0x6E, 0xAE, 0xAA, 0x6A, 0x6B,
            0xAB, 0x69, 0xA9, 0xA8, 0x68, 0x78, 0xB8, 0xB9, 0x79, 0xBB, 0x7B, 0x7A, 0xBA, 0xBE, 0x7E, 0x7F, 0xBF, 0x7D,
            0xBD, 0xBC, 0x7C, 0xB4, 0x74, 0x75, 0xB5, 0x77, 0xB7, 0xB6, 0x76, 0x72, 0xB2, 0xB3, 0x73, 0xB1, 0x71, 0x70,
            0xB0, 0x50, 0x90, 0x91, 0x51, 0x93, 0x53, 0x52, 0x92, 0x96, 0x56, 0x57, 0x97, 0x55, 0x95, 0x94, 0x54, 0x9C,
            0x5C, 0x5D, 0x9D, 0x5F, 0x9F, 0x9E, 0x5E, 0x5A, 0x9A, 0x9B, 0x5B, 0x99, 0x59, 0x58, 0x98, 0x88, 0x48, 0x49,
            0x89, 0x4B, 0x8B, 0x8A, 0x4A, 0x4E, 0x8E, 0x8F, 0x4F, 0x8D, 0x4D, 0x4C, 0x8C, 0x44, 0x84, 0x85, 0x45, 0x87,
            0x47, 0x46, 0x86, 0x82, 0x42, 0x43, 0x83, 0x41, 0x81, 0x80, 0x40 };
}
