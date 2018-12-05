package cc.iandroid.yundoured.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cc.iandroid.yundoured.R;
import cc.iandroid.yundoured.adapter.LocalAdapter;
import cc.iandroid.yundoured.bean.DeviceInfo;
import cc.iandroid.yundoured.bean.DeviceInfoDao;
import cc.iandroid.yundoured.bean.LocalDeviceBean;
import cc.iandroid.yundoured.common.EventMsg;
import cc.iandroid.yundoured.common.UdpMsg;
import cc.iandroid.yundoured.common.UpdateLocal;
import cc.iandroid.yundoured.utils.UDPUtils;

public class DeviceLocalActivity extends BaseActivity implements View.OnClickListener, LocalAdapter.IDevice {

    private ImageView mFindDevice;
    private RecyclerView mLocalDevices;
    private LocalAdapter mLocalAdapter;
    private List<LocalDeviceBean> data = new ArrayList<>();
    private ImageView mBack;
    private TextView mTitle;
    private LinearLayout mLineAuto;
    private LinearLayout mLocalManual;
    private LinearLayout mLineManual;
    private EditText mDeviceIp;
    private EditText mDeviceName;
    private Button mDeviceAdd;
    private EditText mDeviceSn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initViews() {
        mFindDevice = (ImageView) findViewById(R.id.find_device);
        mBack = (ImageView) findViewById(R.id.image_toolbar_back);
        mTitle = (TextView) findViewById(R.id.text_toolbar_title);
        mLineAuto = (LinearLayout) findViewById(R.id.line_auto);
        mLineManual = (LinearLayout) findViewById(R.id.line_manual);
        mLocalManual = (LinearLayout) findViewById(R.id.local_manual);
        mLocalDevices = (RecyclerView) findViewById(R.id.local_device);
        mDeviceIp = (EditText) findViewById(R.id.device_ip);
        mDeviceName = (EditText) findViewById(R.id.device_name);
        mDeviceSn = (EditText) findViewById(R.id.device_sn);
        mDeviceAdd = (Button) findViewById(R.id.device_add);
        mLocalDevices.setLayoutManager(new LinearLayoutManager(this));
        mLocalAdapter = new LocalAdapter();
        mLocalDevices.setAdapter(mLocalAdapter);
    }

    @Override
    protected void initEvents() {
        mBack.setOnClickListener(this);
        mLineAuto.setOnClickListener(this);
        mLineManual.setOnClickListener(this);
        mDeviceAdd.setOnClickListener(this);
        mLocalAdapter.setDevice(this);
    }

    @Override
    protected void initDatas() {
        mTitle.setText(R.string.find_device);
        UDPUtils.sendUdp("255.255.255.255","device=?");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_device_local;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.image_toolbar_back:
                finish();
                break;
            case R.id.line_manual:
                mLocalManual.setVisibility(View.VISIBLE);
                break;
            case R.id.line_auto:
                data.clear();
                mLocalManual.setVisibility(View.GONE);
                UDPUtils.sendUdp("255.255.255.255","device=?");
                break;
            case R.id.device_add:
                if (mDeviceName.getText().toString().length() >0 && mDeviceName.getText().toString().length() < 20){
                    List<DeviceInfo> dbList = deviceInfoDao.queryBuilder().where(DeviceInfoDao.Properties.Sn.eq(mDeviceSn.getText().toString().trim())).list();
                    if (dbList.size() > 0 ){
                        Toast.makeText(this,R.string.device_exist,Toast.LENGTH_SHORT).show();
                    }else {
                        DeviceInfo deviceInfo = new DeviceInfo();
                        deviceInfo.setIp(mDeviceIp.getText().toString().trim());
                        deviceInfo.setName(mDeviceName.getText().toString().trim());
                        deviceInfo.setSn(mDeviceSn.getText().toString().trim());
                        deviceInfoDao.insertOrReplace(deviceInfo);
                        EventBus.getDefault().post(deviceInfo);
                        finish();

                    }
                }else {
                    Toast.makeText(this, R.string.set_device_name,Toast.LENGTH_SHORT).show();
                }

                break;
                default:
                    break;
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMesg(UdpMsg event) {
        String info = event.getString();
        if (info != null){
            try {
                JSONObject json = new JSONObject(info);
                LocalDeviceBean deviceBean = new LocalDeviceBean();
                deviceBean.setIp(json.optString("ip"));
                deviceBean.setSn(json.optString("sn"));
                deviceBean.setVer(json.optString("hver"));
                if (json.optString("hver").contains("RED")){
                    data.add(deviceBean);
                }
                mLocalAdapter.setData(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void doClick(int pos, LocalDeviceBean bean) {
        mLocalManual.setVisibility(View.VISIBLE);
        mDeviceIp.setText(bean.getIp());
        mDeviceSn.setText(bean.getSn());
    }
}
