package cc.iandroid.yundoured.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cc.iandroid.yundoured.R;
import cc.iandroid.yundoured.common.EventMsg;
import cc.iandroid.yundoured.widget.ClearEditText;

public class ManualActivity extends BaseActivity {

    private ClearEditText manualCmd;
    private Button manualSend;
    private TextView manualResult;
    private String topic;
    private ImageView imageBack;
    private TextView textTitle;
    private TextView manualClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initViews() {
        imageBack = (ImageView) findViewById(R.id.image_toolbar_back);
        textTitle = (TextView) findViewById(R.id.text_toolbar_title);

        manualCmd = (ClearEditText) findViewById(R.id.manual_cmd);
        manualSend = (Button) findViewById(R.id.manual_send);
        manualResult = (TextView) findViewById(R.id.manual_result);
        manualClear = (TextView) findViewById(R.id.manual_clear);
    }

    @Override
    protected void initEvents() {
        manualSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publish(topic,manualCmd.getText().toString().trim());
            }
        });
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        manualClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manualResult.setText("");
            }
        });
    }

    @Override
    protected void initDatas() {
        topic = getIntent().getExtras().getString("topic", "");
        if (topic.equals("")){
            finish();
            return;
        }
        textTitle.setText(topic+getString(R.string.debug));
        subscription(topic);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_manual;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMesg(EventMsg event) {
        String info = event.getString();
        if (info != null && info.contains(topic)){
            manualResult.setText(info);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unsubscribe(topic);
    }
}
