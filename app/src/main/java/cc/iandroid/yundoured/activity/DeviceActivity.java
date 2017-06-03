package cc.iandroid.yundoured.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cc.iandroid.yundoured.R;
import cc.iandroid.yundoured.adapter.InputAdapter;
import cc.iandroid.yundoured.adapter.OutputAdapter;
import cc.iandroid.yundoured.bean.DeviceInfo;
import cc.iandroid.yundoured.callback.OnRelayControlClickLister;
import cc.iandroid.yundoured.common.EventMsg;

public class DeviceActivity extends BaseActivity implements OnRelayControlClickLister {

    private static final String TAG = "@@@";
    private ImageView imageBack;
    private TextView textTitle;
    private ImageView imageFinish;
    private Bundle bundle;
    private DeviceInfo deviceInfo;
    private RecyclerView deviceView;
    private OutputAdapter outoutAdapter;
    private List<String> relayData;
    private List<String> inputData;
    private TextView deviceTemp;
    private TextView deviceHum;
    private RecyclerView inputView;
    private InputAdapter inputAdapter;
    private static String RELAY_OFF = "0";
    private static String RELAY_ON = "1";
    private static String RELAY_PLUSE = "2";
    private static String RELAY_TURN = "3";
    private static String RELAY_LOCK = "4";
    private LinearLayout lineTemHum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initViews() {
        imageBack = (ImageView) findViewById(R.id.image_toolbar_back);
        textTitle = (TextView) findViewById(R.id.text_toolbar_title);
        imageFinish = (ImageView) findViewById(R.id.image_toolbar_setting);
        deviceView = (RecyclerView) findViewById(R.id.device_relay);
        inputView = (RecyclerView) findViewById(R.id.device_input);
        deviceTemp = (TextView) findViewById(R.id.device_temp);
        deviceHum = (TextView) findViewById(R.id.device_hum);
        lineTemHum = (LinearLayout) findViewById(R.id.line_tem_hum);
    }

    @Override
    protected void initEvents() {
        outoutAdapter.setOnRelayControlClickLister(this);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initDatas() {
        bundle = getIntent().getExtras();
        deviceInfo = (DeviceInfo) bundle.getSerializable("device");
        textTitle.setText(deviceInfo.getName());
        relayData = new ArrayList<>();
        inputData = new ArrayList<>();
        deviceView.setLayoutManager(new LinearLayoutManager(this));
        outoutAdapter = new OutputAdapter(this);
        deviceView.setAdapter(outoutAdapter);

        inputView.setLayoutManager(new GridLayoutManager(this,8,LinearLayoutManager.VERTICAL,false));
        inputAdapter = new InputAdapter(this);
        inputView.setAdapter(inputAdapter);

        lineTemHum.setVisibility(View.GONE);

        subscription(deviceInfo.getSn());
        publish(deviceInfo.getSn(),"state=?");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_device;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unsubscribe(deviceInfo.getSn());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMesg(EventMsg event) {
        String info = event.getString();
        if (info != null){
            try {
                JSONObject json = new JSONObject(info);
                String cmd = json.optString("cmd");
                String sn = json.optString("sn");
                if (cmd.equals("post") && sn.equals(deviceInfo.getSn())){
                    String outs = json.optString("outs");
                    String ins = json.optString("ins");
                    String temp = json.optString("temp");
                    String hum = json.optString("hum");
                    char [] tempOuts = outs.toCharArray();
                    relayData.clear();
                    for (int i = 0; i < tempOuts.length; i++) {
                        relayData.add(tempOuts[i]+"");
                    }
                    outoutAdapter.setData(relayData);

                    char [] tempIns = ins.toCharArray();
                    inputData.clear();
                    for (int i = 0; i < tempIns.length; i++) {
                        inputData.add(tempIns[i]+"");
                    }
                    inputAdapter.setData(inputData);
                    if (temp.equals("")&&hum.equals("")){
                        lineTemHum.setVisibility(View.GONE);
                    }else {
                        lineTemHum.setVisibility(View.VISIBLE);
                    }
                    deviceTemp.setText(getString(R.string.temp)+Double.parseDouble(temp.equals("")?"0":temp)/10);
                    deviceHum.setText(getString(R.string.hum)+Double.parseDouble(hum.equals("")?"0":hum)/10);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void doOnClick(int pos,int size) {
        publish(deviceInfo.getSn(),sendCmd(pos,size,RELAY_ON));
    }

    @Override
    public void doOffClick(int pos,int size) {
        publish(deviceInfo.getSn(),sendCmd(pos,size,RELAY_OFF));
    }

    @Override
    public void doPluseClick(int pos,int size) {
        publish(deviceInfo.getSn(),sendCmd(pos,size,RELAY_PLUSE));
    }

    @Override
    public void doTurnClick(int pos,int size) {
        publish(deviceInfo.getSn(),sendCmd(pos,size,RELAY_TURN));
    }

    @Override
    public void doLockClick(int pos,int size) {
        publish(deviceInfo.getSn(),sendCmd(pos,size,RELAY_LOCK));
    }

    public String sendCmd(int pos,int size,String type){
        StringBuilder sb = new StringBuilder();
        sb.append("setr=");
        for (int i = 0; i < size; i++) {
            if (i == pos){
                sb.append(type);
            }else {
                sb.append("x");
            }
        }
        return sb.toString();
    }
}
