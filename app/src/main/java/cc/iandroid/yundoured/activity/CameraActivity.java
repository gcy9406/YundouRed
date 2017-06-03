package cc.iandroid.yundoured.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import cc.iandroid.yundoured.R;

public class CameraActivity extends BaseActivity implements View.OnClickListener {
    private CaptureFragment captureFragment;
    public static boolean isOpen = false;
    public TextView textTitle;
    public ImageView imageSetting;
    public ImageView imageBack;
    private CheckBox led;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        captureFragment = new CaptureFragment();
        // 为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(captureFragment, R.layout.my_camera);
        captureFragment.setAnalyzeCallback(analyzeCallback);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commit();
    }
    @Override
    protected void initViews() {
        imageBack = (ImageView) findViewById(R.id.image_toolbar_back);
        textTitle = (TextView) findViewById(R.id.text_toolbar_title);
        imageSetting = (ImageView) findViewById(R.id.image_toolbar_setting);
        led = (CheckBox) findViewById(R.id.led);
    }

    @Override
    protected void initEvents() {
        imageBack.setOnClickListener(this);
        imageSetting.setOnClickListener(this);
        led.setOnClickListener(this);

    }

    @Override
    protected void initDatas() {
        textTitle.setText(R.string.qr_code);
        imageSetting.setVisibility(View.GONE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_diy_camera;
    }

    /**
     * 二维码解析回调函数
     */
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
            bundle.putString(CodeUtils.RESULT_STRING, result);
            resultIntent.putExtras(bundle);
            CameraActivity.this.setResult(RESULT_OK, resultIntent);
            CameraActivity.this.finish();
        }

        @Override
        public void onAnalyzeFailed() {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_FAILED);
            bundle.putString(CodeUtils.RESULT_STRING, "");
            resultIntent.putExtras(bundle);
            CameraActivity.this.setResult(RESULT_OK, resultIntent);
            CameraActivity.this.finish();
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_toolbar_back:
                finish();
                break;
            case R.id.led:
                if (!isOpen) {
                    CodeUtils.isLightEnable(true);
                    isOpen = true;
                } else {
                    CodeUtils.isLightEnable(false);
                    isOpen = false;
                }
                break;
        }
    }
}
