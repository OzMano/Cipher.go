#ifndef CIPHERGO_LOGGER_H
#define CIPHERGO_LOGGER_H

#include <android/log.h>
#include <string>
#include <sstream>

#define INFO(...)__android_log_print(ANDROID_LOG_INFO, "Cipher.go", __VA_ARGS__)
#define ERROR(...)__android_log_print(ANDROID_LOG_ERROR, "Cipher.go", __VA_ARGS__)

template<typename T>
std::string to_string(const T &n) {
    std::ostringstream stream;
    stream << n;
    return stream.str();
}



#endif //CIPHERGO_LOGGER_H
