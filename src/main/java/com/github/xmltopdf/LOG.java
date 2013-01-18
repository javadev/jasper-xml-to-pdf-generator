/*
 * $Id$
 *
 * Copyright 2013 Valentyn Kolesnikov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.xmltopdf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**.
 * @author Valentyn Kolesnikov
 * @version $Revision$ $Date$
 */
public class LOG {
    private enum MessageType { DEBUG, INFO, WARN, ERROR };

    public static void debug(String aParam) {
        debug(null, aParam);
    }

    public static void debug(Object obj, String param) {
        log(MessageType.DEBUG, obj, param);
    }

    public static void error(Object obj, String param) {
        log(MessageType.ERROR, obj, param);
    }

    public static void error(Object obj, Throwable aProblem, String param) {
        log(MessageType.ERROR, obj, problem2String(param, aProblem));
     }

    public static void info(Object obj, String param) {
        log(MessageType.INFO, obj, param);
    }

    public static void warn(Object obj, String param) {
        log(MessageType.WARN, obj, param);
    }

    public static void warn(Object aObj, Throwable aProblem, String param) {
        warn(aObj, problem2String(param, aProblem));
    }

    private static void log(MessageType messageType, Object obj, String localParam) {
        String aClassName = getLoggerName(obj == null ? whoCalledMe() : obj);
        Logger log = LoggerFactory.getLogger(aClassName);
        String param = modifyString(localParam);
        if (MessageType.DEBUG.equals(messageType)) {
            log.debug(param);
        } else if (MessageType.INFO.equals(messageType)) {
            log.info(param);
        } else if (MessageType.WARN.equals(messageType)) {
            log.warn(param);
        } else {
            log.error(param);
        }
    }

    private static String whoCalledMe() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        StackTraceElement caller = stackTraceElements[4];
        String classname = caller.getClassName();
        return classname;
    }

    private static String problem2String(String aMsg, Throwable aProblem) {
        StringBuilder sb = new StringBuilder();
        if (aMsg != null) {
            sb.append(aMsg).append('\n');
        }
        sb.append("Error is: ").append(aProblem.getClass().getName()).
                append(" Message: ").append(aProblem.getMessage()).append('\n');
        makeGoodTrace(sb, aProblem.getStackTrace());
        Throwable cause = aProblem.getCause();
        while (cause != null) {
            sb.append("The cause is ").append(cause.getClass().getName()).
                    append(" Message: ").append(aProblem.getMessage()).append('\n');
            makeGoodTrace(sb, cause.getStackTrace());
            cause = cause.getCause();
        }
        return sb.toString();
    }

    public static String modifyString(String param) {
         if (null == param && "".equals(param)) {
             return "";
         }
         return param;
     }

    private static String getLoggerName(Object aObj) {
        if (aObj == null) {
            return LOG.class.getName();
        }
        if (aObj instanceof String) {
            return (String) aObj;
        }
        if (aObj instanceof Class) {
            return ((Class) aObj).getName();
        }
        return aObj.getClass().getName();
    }

    private static void makeGoodTrace(StringBuilder sb, StackTraceElement[] trace) {
        for (StackTraceElement entry : trace) {
            if (entry.getClassName().startsWith("org.bitbucket")) {
                sb.append("\t-->");
            } else {
                sb.append('\t');
            }
            sb.append(entry).append('\n');
        }
    }

    /**
     * Set logger NDC marker to mark current therad in LOG using specified marker.
     * use org.apache.log4j.NDC.push(String );
     *
     * @param marker marker
     * */
    public static void setNCDMarger(String marker) {
        org.apache.log4j.NDC.push(marker);
    }

    /**
     * Set logger NDC marker to mark current therad in LOG.
     * use org.apache.log4j.NDC.peek()
     * @return curent NDC marker
     * */
    public static String getNDCMarker() {
        return org.apache.log4j.NDC.peek();
    }

    /**
     * Remove logger NDC marker twhich mark current therad in LOG.
     * use org.apache.log4j.NDC.peek()
     * @return curent NDC marker
     * */
    public static String removeNDCMarker() {
        return org.apache.log4j.NDC.pop();
    }
}
