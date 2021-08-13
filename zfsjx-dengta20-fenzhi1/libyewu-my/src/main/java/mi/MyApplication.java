/**
 * Copyright 2016,Smart Haier.All rights reserved
 */
package mi;

import com.geek.libbase.AndroidApplication;
import com.geek.libutils.app.AppUtils;
import com.lxj.xpopup.XPopup;

/**
 * <p class="note">File Note</p>
 * created by geek at 2017/6/6
 */
public class MyApplication extends AndroidApplication {

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

        XPopup.setPrimaryColor(getResources().getColor(com.lxj.xpopupdemo.R.color.colorPrimary));


    }

    @Override
    public void onHomePressed() {
        super.onHomePressed();
//        AddressSaver.addr = null;
    }
}