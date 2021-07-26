/**
 * Copyright 2016,Smart Haier.All rights reserved
 */
package mi;

import android.content.res.Resources;

import com.example.libbase.AndroidApplication;
import com.example.libbase.utils.LoadUtil;
import com.geek.libutils.app.AppUtils;
import com.liulishuo.filedownloader.util.FileDownloadUtils;

import java.io.File;

/**
 * <p class="note">File Note</p>
 * created by geek at 2017/6/6
 */
public class EzhibuApplication extends AndroidApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        if (!AppUtils.isProcessAs(getPackageName(), this)) {
            return;
        }
        //TODO commonbufen
        configBugly("测试", "3aeeb18e5e");
        configHios();
        configmmkv();
        configShipei();
        configRetrofitNet();
        others();
        //TODO 业务bufen


    }

    @Override
    public void onHomePressed() {
        super.onHomePressed();
//        AddressSaver.addr = null;
    }
}