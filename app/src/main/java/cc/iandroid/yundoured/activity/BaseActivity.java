package cc.iandroid.yundoured.activity;

import com.google.gson.Gson;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.tbruyelle.rxpermissions.RxPermissions;

import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.Callback;
import org.fusesource.mqtt.client.CallbackConnection;
import org.fusesource.mqtt.client.Listener;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.URISyntaxException;

import cc.iandroid.yundoured.bean.DaoMaster;
import cc.iandroid.yundoured.bean.DaoSession;
import cc.iandroid.yundoured.bean.DeviceInfoDao;
import cc.iandroid.yundoured.common.EventMsg;

/**
 * Created by gcy on 2017/5/29.
 */

public abstract class BaseActivity extends AppCompatActivity{
    public RxPermissions rxPermissions;
    private DaoSession daoSession;
    public DeviceInfoDao deviceInfoDao;
    public Gson gson;
    public MQTT mqtt;
    public CallbackConnection connection;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        getSupportActionBar().hide();
        gson = new Gson();
        rxPermissions = new RxPermissions(this);
        initMqtt();
        initDatabase();
        initViews();
        initDatas();
        initEvents();
        EventBus.getDefault().register(this);
    }

    /**
     * 配置MQTT
     */
    private void initMqtt() {
        //实例化mqtt对象
        mqtt = new MQTT();
        try {
            mqtt.setHost("tcp://182.61.18.191:1883");//设置地址
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        //获得链接
        connection = mqtt.callbackConnection();
        //开始链接
        connection.connect(new Callback<Void>() {
            @Override
            public void onSuccess(Void value) {

            }
            @Override
            public void onFailure(Throwable value) {

            }
        });

        //订阅
        connection.listener(new Listener() {
            @Override
            public void onConnected() {

            }

            @Override
            public void onDisconnected() {

            }

            /**
             * 得到订阅的结果，发送出去
             * @param topic
             * @param body
             * @param ack
             */
            @Override
            public void onPublish(UTF8Buffer topic, Buffer body, Runnable ack) {
                EventMsg msg = new EventMsg(body.utf8().toString());
                EventBus.getDefault().post(msg);
                Log.d("@@@",body.utf8().toString());
            }

            @Override
            public void onFailure(Throwable value) {
                Log.d("@@@", "onFailure: listener");
            }
        });
    }

    private void initDatabase() {
        daoSession = DaoMaster.newDevSession(this, "Red8");
        deviceInfoDao = daoSession.getDeviceInfoDao();
    }

    protected abstract void initViews();
    protected abstract void initEvents();
    protected abstract void initDatas();
    protected abstract int getLayoutId();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disconnect();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMesg(EventMsg event) {}

    public void publish(String topic,String cmd) {
        connection.publish(topic+"ctr", cmd.getBytes(), QoS.AT_MOST_ONCE, false, new Callback<Void>() {
            @Override
            public void onSuccess(Void value) {

            }

            @Override
            public void onFailure(Throwable value) {

            }
        });
    }

    public void subscription(String topic) {
        Topic[] topics = {new Topic(topic+ "state", QoS.AT_MOST_ONCE)};
        connection.subscribe(topics, new Callback<byte[]>() {
            @Override
            public void onSuccess(byte[] value) {

            }
            @Override
            public void onFailure(Throwable value) {
                Log.d("@@@", "onFailure: subscription");
            }
        });
    }

    public void unsubscribe(String topic) {
        UTF8Buffer topicU = new UTF8Buffer(topic+"state");
        UTF8Buffer [] tArr = new UTF8Buffer[1];
        tArr[0] = topicU;
        connection.unsubscribe(tArr, new Callback<Void>() {
            @Override
            public void onSuccess(Void value) {

            }

            @Override
            public void onFailure(Throwable value) {

            }
        });
    }
    private void disconnect() {
        connection.disconnect(new Callback<Void>() {
            @Override
            public void onSuccess(Void value) {

            }

            @Override
            public void onFailure(Throwable value) {

            }
        });
    }
}
