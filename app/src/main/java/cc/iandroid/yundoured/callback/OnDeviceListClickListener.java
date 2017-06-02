package cc.iandroid.yundoured.callback;

import android.widget.LinearLayout;

import cc.iandroid.yundoured.bean.DeviceInfo;

/**
 * Created by gcy on 2017/5/29.
 */

public interface OnDeviceListClickListener {
    void doClick(int pos, DeviceInfo deviceInfo);
    void doLongClick(int pos, DeviceInfo deviceInfo, LinearLayout item);
}
