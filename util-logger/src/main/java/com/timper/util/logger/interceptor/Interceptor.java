package com.timper.util.logger.interceptor;

import com.timper.util.logger.LogData;

public interface Interceptor {
    boolean intercept(LogData logData);
}
