package cc.iandroid.yundoured.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.flyco.dialog.widget.popup.BubblePopup;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.ArrayList;
import java.util.List;

import cc.iandroid.yundoured.R;
import cc.iandroid.yundoured.adapter.DeviceListAdapter;
import cc.iandroid.yundoured.bean.DeviceInfo;
import cc.iandroid.yundoured.bean.DeviceInfoDao;
import cc.iandroid.yundoured.bean.QRCode;
import cc.iandroid.yundoured.callback.OnDeviceListClickListener;
import cc.iandroid.yundoured.utils.MiPictureHelper;
import rx.functions.Action1;

public class MainActivity extends BaseActivity implements View.OnClickListener, OnDeviceListClickListener {

    private static final int REQUEST_ADD = 3;
    private RecyclerView deviceList;
    public TextView textTitle;
    public ImageView imageSetting;
    public ImageView imageBack;
    private int REQUEST_CODE = 1;
    private int REQUEST_IMAGE = 2;
    private static final String IMAGE_UNSPECIFIED = "image/*";
    private List<DeviceInfo> deviceData;
    private DeviceListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initViews() {
        imageBack = (ImageView) findViewById(R.id.image_toolbar_back);
        textTitle = (TextView) findViewById(R.id.text_toolbar_title);
        imageSetting = (ImageView) findViewById(R.id.image_toolbar_setting);

        //从数据库读取设别列表
        deviceList = (RecyclerView) findViewById(R.id.device_list);
    }

    @Override
    protected void initEvents() {
        imageBack.setOnClickListener(this);
        imageSetting.setOnClickListener(this);
    }

    @Override
    protected void initDatas() {
        //设置toolbar
        imageBack.setVisibility(View.GONE);
        textTitle.setText(R.string.device_manager);
        imageSetting.setImageResource(R.mipmap.icon_qrcode);

        //设置设备列表
        deviceData = new ArrayList<>();
        deviceList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DeviceListAdapter(this);
        deviceList.setAdapter(adapter);
        deviceData = deviceInfoDao.queryBuilder().list();
        adapter.setData(deviceData);
        adapter.setOnDeviceListClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    /**
     * 选择添加设备的方式
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.image_toolbar_setting:
                final String[] stringItems = {getString(R.string.camera_code), getString(R.string.album_code)};
                final ActionSheetDialog dialog = new ActionSheetDialog(this, stringItems, null);
                dialog.cancelText(getString(R.string.cancel));
                dialog.title(getString(R.string.add_device))//
                        .titleTextSize_SP(14.5f)//
                        .show();
                dialog.setOnOperItemClickL(new OnOperItemClickL() {
                    @Override
                    public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0:
                                rxPermissions.request(Manifest.permission.CAMERA)
                                        .subscribe(new Action1<Boolean>() {
                                            @Override
                                            public void call(Boolean granted) {
                                                if (granted) {
                                                    Intent intent = new Intent(MainActivity.this, CameraActivity.class);
                                                    startActivityForResult(intent, REQUEST_CODE);
                                                } else {
                                                    Toast.makeText(MainActivity.this, R.string.camera_reject, Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                break;
                            case 1:
                                rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE)
                                        .subscribe(new Action1<Boolean>() {
                                            @Override
                                            public void call(Boolean granted) {
                                                if (granted) {
                                                    Intent intentPic = new Intent(Intent.ACTION_PICK, null);
                                                    intentPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
                                                    startActivityForResult(intentPic, REQUEST_IMAGE);
                                                } else {
                                                    Toast.makeText(MainActivity.this, R.string.album_reject, Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                break;
                        }
                        dialog.dismiss();
                    }
                });
                break;
        }
    }

    /**
     * 处理添加设备的结果
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == REQUEST_CODE) {
                //处理扫描结果（在界面上显示）
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                        String result = bundle.getString(CodeUtils.RESULT_STRING);
                        if (result.contains("NET_RED8CH")) {
                            if (result.contains("sn")) {
                                setDeviceName(result);
                            } else {
                                Toast.makeText(this, R.string.code_wrong, Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(this, R.string.code_wrong, Toast.LENGTH_SHORT).show();
                        }
                    } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                        Toast.makeText(this, R.string.code_wrong, Toast.LENGTH_SHORT).show();
                    }
                }
            } else if (requestCode == REQUEST_IMAGE) {
                Uri uri = data.getData();
                String path = MiPictureHelper.getPath(this, uri);  // 获取图片路径的方法调用
                CodeUtils.analyzeBitmap(path, new CodeUtils.AnalyzeCallback() {
                    @Override
                    public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                        if (result.contains("NET_RED8CH")) {
                            if (result.contains("sn")) {
                                setDeviceName(result);
                            } else {
                                Toast.makeText(MainActivity.this, R.string.code_wrong, Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(MainActivity.this, R.string.code_wrong, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onAnalyzeFailed() {
                        Toast.makeText(MainActivity.this, R.string.code_wrong, Toast.LENGTH_SHORT).show();
                    }
                });
            }else if (requestCode == REQUEST_ADD){
                Bundle resultBundle = data.getExtras();
                String result = resultBundle.getString("result");
                String name = resultBundle.getString("name");
                boolean modify = resultBundle.getBoolean("modify",false);
                int pos = resultBundle.getInt("pos",0);
                //更新列表
                QRCode qrCode = gson.fromJson(result, QRCode.class);
                DeviceInfo device = new DeviceInfo(qrCode.getSn(),qrCode.getUser(),qrCode.getPsw(),qrCode.getVer(),"",name,true);
                if (modify){
                    deviceData.set(pos,device);
                }else {
                    deviceData.add(device);
                }
                adapter.setData(deviceData);

                //更新数据库
                List<DeviceInfo> dbList = deviceInfoDao.queryBuilder().where(DeviceInfoDao.Properties.Sn.eq(qrCode.getSn())).list();
                if (dbList.size() > 0 ){
                    DeviceInfo temp = dbList.get(0);
                    temp.setUser(qrCode.getUser());
                    temp.setPsw(qrCode.getPsw());
                    temp.setVer(qrCode.getVer());
                    temp.setName(name);
                    deviceInfoDao.update(temp);
                }else {
                    deviceInfoDao.insert(device);
                }
            }
        }
    }

    /**
     * 进入设置ip的界面
     * @param result
     */
    private void setDeviceName(String result) {
        Intent editIntent = new Intent(MainActivity.this, DeviceParamActivity.class);
        editIntent.putExtra("result", result);
        startActivityForResult(editIntent, REQUEST_ADD);
    }

    /**
     * 进入相应设备
     * @param pos
     * @param deviceInfo
     */
    @Override
    public void doClick(int pos, DeviceInfo deviceInfo) {
        //进入设备
        Intent intent = new Intent(MainActivity.this, DeviceActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("device", deviceInfo);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 处理长按动作
     * @param pos
     * @param device
     * @param item
     */
    @Override
    public void doLongClick(final int pos, final DeviceInfo device, LinearLayout item) {
        //弹出bubble对话框
        View inflate = View.inflate(this, R.layout.popup_bubble_line, null);
        TextView modify = (TextView) inflate.findViewById(R.id.bubble_modify);
        TextView unbind = (TextView) inflate.findViewById(R.id.bubble_unbind);
        TextView debug = (TextView) inflate.findViewById(R.id.bubble_debug);
        final BubblePopup bubblePopup = new BubblePopup(this, inflate);
        bubblePopup.anchorView(item)
                .gravity(Gravity.BOTTOM)
                .show();

        //修改动作
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DeviceParamActivity.class);
                Bundle bundle = new Bundle();
                QRCode code = new QRCode(device.getSn(),device.getUser(),device.getPsw(),device.getVer());
                bundle.putString("result",gson.toJson(code));
                bundle.putBoolean("modify",true);
                bundle.putInt("pos",pos);
                intent.putExtras(bundle);
                startActivityForResult(intent,REQUEST_ADD);
                bubblePopup.dismiss();
            }
        });

        //解绑动作
        unbind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                List<DeviceInfo> devices = deviceInfoDao.queryBuilder().where(DeviceInfoDao.Properties.Sn.eq(device.getSn())).list();
                if (devices != null){
                    for (int i = 0; i < devices.size(); i++) {
                        deviceInfoDao.delete(devices.get(i));
                    }
                }

                deviceData.remove(pos);
                adapter.setData(deviceData);
                bubblePopup.dismiss();
            }
        });

        //手动调试
        debug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ManualActivity.class);
                startActivity(intent.putExtra("topic",deviceData.get(pos).getSn()));
                bubblePopup.dismiss();
            }
        });
    }
}
