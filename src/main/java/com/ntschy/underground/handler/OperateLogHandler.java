package com.ntschy.underground.handler;

import com.ntschy.underground.entity.base.OperationLog;

public class OperateLogHandler {

    private final static ThreadLocal<OperationLog> OPERATION_LOG_THREAD_LOCAL = new ThreadLocal<>();

    public static void set(OperationLog operationLogs) {
        OPERATION_LOG_THREAD_LOCAL.set(operationLogs);
    }

    public static OperationLog getOperationLog() {
        return OPERATION_LOG_THREAD_LOCAL.get();
    }

    public static void remove() {
        OPERATION_LOG_THREAD_LOCAL.remove();
    }
}
