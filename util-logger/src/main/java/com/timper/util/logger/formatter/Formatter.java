package com.timper.util.logger.formatter;

public interface Formatter {
    String format(int logLevel, String tag, String msg);
}
