package com.timper.util.logger.interceptor;

import com.timper.util.logger.Level;
import com.timper.util.logger.LogData;

public class LevelInterceptor implements Interceptor {

    private int level = Level.VERBOSE;

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public boolean intercept(LogData logData) {
        return logData != null && logData.logLevel >= level;
    }
}
