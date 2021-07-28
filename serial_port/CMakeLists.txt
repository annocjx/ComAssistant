cmake_minimum_required(VERSION 3.4.1)

add_library(serial_port
        SHARED
        src/main/jni/SerialPort.c)


#include_directories(src/main/jni/)

find_library(log-lib # 库路径的变量名
        log) # 对应的库名

#将预构建库关联到您自己的原生库
target_link_libraries(serial_port
        ${log-lib})
