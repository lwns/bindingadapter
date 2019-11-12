package com.timper.myapplication.featurn;

import android.content.Context;
import com.timper.util.logger.Level;
import com.timper.util.logger.Log4a;
import com.timper.util.logger.LogData;
import com.timper.util.logger.appender.AndroidAppender;
import com.timper.util.logger.appender.FileAppender;
import com.timper.util.logger.formatter.DateFileFormatter;
import com.timper.util.logger.interceptor.Interceptor;
import com.timper.util.logger.logger.AppenderLogger;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LogInit {

  public static final int BUFFER_SIZE = 1024 * 400; //400k

  public static void init(Context context) {
    int level = Level.DEBUG;
    AndroidAppender androidAppender = new AndroidAppender.Builder().setLevel(level)
                                                                   .create();

    File log = FileUtils.getLogDir(context);
    String buffer_path = log.getAbsolutePath() + File.separator + ".logCache";
    String time = new SimpleDateFormat("yyyy_MM_dd", Locale.getDefault()).format(new Date());
    String log_path = log.getAbsolutePath() + File.separator + time + ".txt";
    FileAppender fileAppender = new FileAppender.Builder(context).setLogFilePath(log_path)
                                                                 .setLevel(level)
                                                                 .setBufferFilePath(buffer_path)
                                                                 .setFormatter(new DateFileFormatter())
                                                                 .setCompress(false)
                                                                 .setBufferSize(BUFFER_SIZE)
                                                                 .create();

    AppenderLogger logger = new AppenderLogger.Builder().addAppender(androidAppender)
                                                        .addAppender(fileAppender)
                                                        .create();
    Log4a.setLogger(logger);
  }
}
