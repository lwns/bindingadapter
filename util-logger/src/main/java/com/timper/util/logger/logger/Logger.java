package com.timper.util.logger.logger;

public interface Logger {

    void println(int priority, String tag, String msg);

    void flush();

    void release();
}
