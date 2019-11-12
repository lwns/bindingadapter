package com.timper.util.logger.appender;

public interface Appender {

    void append(int logLevel, String tag, String msg);
    void flush();
    void release();

}
