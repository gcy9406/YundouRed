package cc.iandroid.yundoured.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cc.iandroid.yundoured.R;
import cc.iandroid.yundoured.bean.DeviceInfo;
import cc.iandroid.yundoured.bean.DeviceInfoDao;
import cc.iandroid.yundoured.bean.QRCode;
import cc.iandroid.yundoured.common.EventMsg;
import cc.iandroid.yundoured.widget.ClearEditText;

public class DeviceParamActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "@@@";
    private ImageView imageBack;
    private TextView textTitle;
    private ImageView imageFinish;
    private EditText editText;
    private EditText editIp;
    private String result;
    private boolean modify;
    private QRCode qrCode;
    private int pos;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initViews() {
        imageBack = (ImageView) findViewById(R.id.image_toolbar_back);
        textTitle = (TextView) findViewById(R.id.text_toolbar_title);
        imageFinish = (ImageView) findViewById(R.id.image_toolbar_setting);
        editText = (EditText) findViewById(R.id.edit_name);
        editIp = (EditText) findViewById(R.id.edit_ip);
    }

    @Override
    protected void initEvents() {
        imageBack.setOnClickListener(this);
        imageFinish.setOnClickListener(this);
    }

    @Override
    protected void initDatas() {
        imageFinish.setImageResource(R.mipmap.icon_finish);
        textTitle.setText(R.string.set_name);

        result = getIntent().getExtras().getString("result","");
        modify = getIntent().getExtras().getBoolean("modify",false);
        pos = getIntent().getExtras().getInt("pos",0);
        name = getIntent().getExtras().getString("name","");
        editText.setText(name);
        qrCode = gson.fromJson(result, QRCode.class);
        if (!modify){
            List<DeviceInfo> deviceListInDb = deviceInfoDao.queryBuilder().where(DeviceInfoDao.Properties.Sn.eq(qrCode.getSn())).list();
            if (deviceListInDb != null && deviceListInDb.size() > 0){
                Toast.makeText(this, R.string.device_exist, Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_device_param;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.image_toolbar_back:
                finish();
                break;
            case R.id.image_toolbar_setting:
                String name = editText.getText().toString().trim();
                if (name == null || name.equals("") || name.length() > 20){
                    Toast.makeText(this, R.string.set_right_name,Toast.LENGTH_SHORT).show();
                    return;
                }
                //完成的时候，更新一下名称
                publishNet(qrCode.getSn(),"devicename="+name);
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("result",result);
                bundle.putString("name",name);
                bundle.putString("ip",editIp.getText().toString());
                bundle.putBoolean("modify",modify);
                bundle.putInt("pos",pos);
                intent.putExtras(bundle);
                setResult(RESULT_OK,intent);
                finish();
                break;
                default:
                    break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
