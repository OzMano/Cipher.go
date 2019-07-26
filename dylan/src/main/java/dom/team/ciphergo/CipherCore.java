package dom.team.ciphergo;

final class CipherCore {
    static {
        System.loadLibrary("cipher-lib");
        init();
    }

    private CipherCore() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    static String get(String key) {
        return getString(key);
    }

    private static native void init();

    private static native String getString(String key);
}
