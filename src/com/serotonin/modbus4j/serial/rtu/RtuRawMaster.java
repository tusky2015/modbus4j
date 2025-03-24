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

import com.serotonin.modbus4j.serial.SerialPortWrapper;
import com.serotonin.modbus4j.serial.SerialWaitingRoomKeyFactory;
import com.serotonin.modbus4j.sero.messaging.MessageControl;
import com.serotonin.modbus4j.sero.messaging.MessageParser;
import com.serotonin.modbus4j.sero.messaging.StreamTransport;

/**
 * <p>RtuRawMaster class.</p>
 *
 * @author Matthew Lohbihler
 * @version 5.0.0
 */
public class RtuRawMaster extends RtuMaster {

    private MessageParser messageParser;

    /**
     * <p>Constructor for RtuMaster.</p>
     *
     * Default to validating the slave id in responses
     *
     * @param wrapper a {@link SerialPortWrapper} object.
     */
    public RtuRawMaster(SerialPortWrapper wrapper, MessageParser messageParser) {
        super(wrapper, true);
        this.messageParser = messageParser;
    }

    /**
     * <p>Constructor for RtuMaster.</p>
     *
     * @param wrapper a {@link SerialPortWrapper} object.
     * @param validateResponse - confirm that requested slave id is the same in the response
     */
    public RtuRawMaster(SerialPortWrapper wrapper, boolean validateResponse, MessageParser messageParser) {
        super(wrapper, validateResponse);
        this.messageParser = messageParser;
    }

    /** {@inheritDoc} */
    @Override
    protected void openConnection(MessageControl toClose) throws Exception {

        this.conn = getMessageControl();
        this.conn.start(transport, this.messageParser, null, new SerialWaitingRoomKeyFactory());
        if (getePoll() == null) {
            ((StreamTransport) transport).start("Modbus RTU master: " + this.wrapper.toString());
        }
    }
}
