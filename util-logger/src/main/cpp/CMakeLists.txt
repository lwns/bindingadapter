
cmake_minimum_required(VERSION 3.4.1)

#set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${PROJECT_SOURCE_DIR}/jniLibs/${ANDROID_ABI})


#aux_source_directory(src/main/cpp/ DIR_LIB_SOURCE)
#add_library (log4a-lib SHARED ${DIR_LIB_SOURCE})


add_library(
    log4a-lib

    SHARED

    AsyncFileFlush.cpp
    FlushBuffer.cpp
    log4a-lib.cpp
    LogBuffer.cpp
    LogBufferHeader.cpp

)

include_directories(includes)


find_library(
              log-lib

              log )

target_link_libraries(
                       log4a-lib

                       ${log-lib} )