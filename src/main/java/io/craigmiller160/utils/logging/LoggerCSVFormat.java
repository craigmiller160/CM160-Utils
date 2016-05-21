/*
 * Copyright 2016 Craig Miller
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package io.craigmiller160.utils.logging;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * A special Formatter implementation for use
 * with java.util.logging to format the log
 * file as a CSV so it can be opened as
 * a spreadsheet.
 *
 * NOTE ABOUT EXCEPTIONS: The full stack trace is outputted for any exceptions
 * this formatter receives via the <tt>LogRecord</tt>. However, the format places
 * each line of the stack trace on its own line within a single cell. Therefore, the
 * stack trace might not initially be visible when the .csv file is opened, until the
 * cell's height is changed.
 *
 * This allows for quick, at a glance viewing of an exception, and the ability to
 * expand and see the full stack trace if needed.
 *
 * Created by craigmiller on 4/19/16.
 */
public class LoggerCSVFormat extends Formatter {

    /**
     * Incremented index value for all records formatted by
     * this class.
     */
    private int index;

    /**
     * Constructs a new <tt>Formatter</tt> to format records
     * for a CSV.
     */
    public LoggerCSVFormat() {

    }

    @Override
    public String format(LogRecord record) {
        StringBuffer buffer = new StringBuffer();

        synchronized(this){
            if(index == 0){ //Create csv column headers
                buffer.append("Index,Level,Time,ThreadName.ID,"
                        + "Class,Method,Count: Parameters (Expand Cell),Message,Throwable (Expand Cell)");
                buffer.append(System.lineSeparator());
            }
            index++;
            buffer.append(index + ",");
        }

        buffer.append(record.getLevel() + ",");
        buffer.append(formatTime(record.getMillis()) + ",");

        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        String threadName = threadMXBean.getThreadInfo(record.getThreadID()).getThreadName();

        buffer.append(threadName + "." + record.getThreadID() + ",");
        buffer.append(record.getSourceClassName() + ",");
        buffer.append(record.getSourceMethodName() + ",");

        Object[] paramArr = record.getParameters();
        if(paramArr != null){
            buffer.append("\"" + paramArr.length + ": ");
            for(Object o : paramArr){
                buffer.append("(" + o + ") \n");
            }
            buffer.append("\",");
        }
        else{
            buffer.append(" ,");
        }

        buffer.append("\"" + record.getMessage() + "\" ,");

        Throwable thrown = record.getThrown();
        if(thrown != null){
            buffer.append("\"" + thrown);
            StackTraceElement[] steArr = thrown.getStackTrace();
            for(StackTraceElement ste : steArr){
                buffer.append("\nat " + ste);
            }
            buffer.append("\"");
        }

        buffer.append(System.lineSeparator());

        return buffer.toString();
    }

    /**
     * Formats the raw millisecond time from the <tt>LogRecord</tt> into a
     * more readable format.
     *
     * @param millisecs the raw millisecond time from the <tt>LogRecord</tt>.
     * @return the formatted time value.
     */
    private String formatTime(long millisecs){
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss:SSS");
        Date resultTime = new Date(millisecs);
        return timeFormat.format(resultTime);
    }

}
