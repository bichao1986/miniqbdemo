package filedemo.tencent.com.filedemo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsReaderView;
import com.tencent.smtt.sdk.ValueCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

public class DemoActivity extends Activity {

    private TbsReaderView mTbsReaderView;
    private Context context;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        context = this;
        initView();
        File basePath = Environment.getExternalStorageDirectory();
//        String path = basePath.getAbsolutePath()+File.separator+"Download/123.pptx";
        String path = basePath.getAbsolutePath()+File.separator+"/littlihotspot_1015.pptx";
//        String path = basePath.getAbsolutePath()+File.separator+"littlihotspot_1015.pptx";
//        String filename = "/sdcard/123.pptx";
        displayFile(path);
    }
    private void initView(){

        mTbsReaderView = new TbsReaderView(context, readerCallback);

        RelativeLayout rootRl = findViewById(R.id.rl_root);
        rootRl.addView(mTbsReaderView, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
//    {
//        Log.d("test",requestCode+"");
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                String filename = "/sdcard/littlihotspot_1015.pptx";
//                displayFile(filename);
//            }
//        },1000*30);
//
//    }

    private TbsReaderView.ReaderCallback readerCallback = new TbsReaderView.ReaderCallback() {
        @Override
        public void onCallBackAction(Integer integer, Object o, Object o1) {

        }
    };

    private void displayFile(final String filename) {
        File file = new File(filename);
        if (file.exists()){
            Log.d("test","文件存在");
        }
        QbSdk.canOpenFile(this, filename, new ValueCallback<Boolean>()
        {

            @Override
            public void onReceiveValue(Boolean arg0)
            {
                if (arg0 == true)
                {
                  openFileReader(context,filename);
                }
                else
                {
                    Toast.makeText(context,"未成功加載內核",1000*3).show();
//                    mFileList.add(fName);
//                    mAdapter.notifyDataSetChanged();
//                    Log.d("test", "unsupported file:" + fName);
                }
            }
        });
    }

    public void openFileReader(Context context, String pathName)
    {

        HashMap<String, String> params = new HashMap<>();
        params.put("local", "true");
        JSONObject Object = new JSONObject();
        try{
            Object.put("pkgName",context.getApplicationContext().getPackageName());
        }catch (JSONException e){
            e.printStackTrace();
        }
        params.put("menuData",Object.toString());
        QbSdk.getMiniQBVersion(context);
        int ret = QbSdk.openFileReader(context, pathName, params, valueCallback);

    }

    private ValueCallback valueCallback = new ValueCallback() {
        @Override
        public void onReceiveValue(Object o) {
            Log.d("test",o.toString());
        }
    };
}
