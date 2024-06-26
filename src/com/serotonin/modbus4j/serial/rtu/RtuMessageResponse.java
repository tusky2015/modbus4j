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
package com.serotonin.modbus4j.serial.rtu;

import com.serotonin.modbus4j.ModbusConfig;
import com.serotonin.modbus4j.base.ModbusUtils;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.msg.ModbusResponse;
import com.serotonin.modbus4j.sero.messaging.IncomingResponseMessage;
import com.serotonin.modbus4j.sero.messaging.OutgoingResponseMessage;
import com.serotonin.modbus4j.sero.util.queue.ByteQueue;

/**
 * Handles the RTU enveloping of modbus responses.
 *
 * @author mlohbihler
 * @version 5.0.0
 */
public class RtuMessageResponse extends RtuMessage implements OutgoingResponseMessage, IncomingResponseMessage {
    static RtuMessageResponse createRtuMessageResponse(ByteQueue queue) throws ModbusTransportException {
        ModbusResponse response = ModbusResponse.createModbusResponse(queue);
        RtuMessageResponse rtuResponse = new RtuMessageResponse(response);

        // 如果启用了CRC校验，才做检查
        if (ModbusConfig.isEnableRtuCrc()) {
            // Check the CRC
            ModbusUtils.checkCRC(rtuResponse.modbusMessage, queue);
        }

        // Return the data.
        return rtuResponse;
    }

    /**
     * <p>Constructor for RtuMessageResponse.</p>
     *
     * @param modbusResponse a {@link com.serotonin.modbus4j.msg.ModbusResponse} object.
     */
    public RtuMessageResponse(ModbusResponse modbusResponse) {
        super(modbusResponse);
    }

    /**
     * <p>getModbusResponse.</p>
     *
     * @return a {@link com.serotonin.modbus4j.msg.ModbusResponse} object.
     */
    public ModbusResponse getModbusResponse() {
        return (ModbusResponse) modbusMessage;
    }
}
