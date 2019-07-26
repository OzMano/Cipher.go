#ifndef CIPHERGO_EXTERN_KEYS_H
#define CIPHERGO_EXTERN_KEYS_H

#define LOAD_MAP(_map) \
    _map["dbkey"] = "hello db"; \
    _map["sessionkey"] = "hello session"; \
    _map["userkey"] = "hello user"; \

#define SECRET_KEY "Cipher.go@DEFAULT"

#define SIGNATURE ""

#endif //CIPHERGO_EXTERN_KEYS_H
