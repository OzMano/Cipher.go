package dom.team.ciphergo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class SignatureUtils {

    @SuppressLint("PackageManagerGetSignatures")
    public static String getSignature(Context context) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo != null) {
            return String.valueOf(packageInfo.signatures[0].hashCode());
        }
        return null;
    }
}
