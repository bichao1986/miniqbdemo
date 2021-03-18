package filedemo.tencent.com.filedemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.ValueCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends Activity implements ValueCallback<String>
{
    private Context context;
    private Button openBtn;
    private Button checkBtn;
    public String				mFilePath	= null;
    private Handler hander = new Handler(Looper.getMainLooper());
    private ListView mListView	= null;

    private ArrayList<String> mFileList	= new ArrayList<String>();

    private FileListAdapter		mAdapter	= null;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context= this;
        openBtn = findViewById(R.id.open_btn);
        checkBtn = findViewById(R.id.check_btn);
        openBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,DemoActivity.class);
                startActivity(intent);
            }
        });
        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(context,WebviewActivity.class);
                startActivity(intent);
            }
        });
        //setContentView(R.layout.activity_miniqb_file);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
//        QbSdk.forceSysWebView();
//        String[] list = new String[1];
//        list[0]="android.permission.WRITE_EXTERNAL_STORAGE";
//        MainActivity.this.requestPermissions(list, 105);
//        initX5Enviroment();
//        QbSdk.initX5Environment(context,cb);

//        mFilePath = Environment.getExternalStorageDirectory().toString();
//        mAdapter = new FileListAdapter(this, R.layout.vlist, mFileList);
//        mListView = findViewById(R.id.miniqb_file_list_view);
//        mListView.setAdapter(mAdapter);
//        mListView.setOnItemClickListener(mAdapter);

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                File file = new File("/sdcard/littlihotspot_1015.pptx");
//                HashMap<String, String> params =  new HashMap<>();
//                params.put("local", "true");
//                params.put("entryId", "2");
//                params.put("allowAutoDestory", "true");
//
//                JSONObject Object = new JSONObject();
//                try
//                {
//                    Object.put("pkgName",MainActivity.this.getApplication().getPackageName());
//                }
//                catch (JSONException e)
//                {
//                    e.printStackTrace();
//                }
//                params.put("menuData",Object.toString());
//                openFileReader(MainActivity.this,file.getAbsolutePath());
//                //QbSdk.openFileReader(MainActivity.this,"/sdcard/edit.pptx",params,MainActivity.this);
//            }
//        });

//        getFileFromSD(mFilePath);
    }

    private void initX5Enviroment(){

        QbSdk.initX5Environment(context,cb);
    }

    /**
     * 搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
     */
    QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

        @Override
        public void onViewInitFinished(boolean isX5Core) {
            //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
            Log.e("APPAplication", " onViewInitFinished is " + isX5Core);
            if (!isX5Core){
                hander.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initX5Enviroment();
                    }
                },1000*30);
            }
        }

        @Override
        public void onCoreInitFinished() {
            Log.e("APPAplication", " onCoreInitFinished");
        }
    };
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public void onCoreInitFinished()
//    {
//        Log.d("test","onCoreInitFinished");
//    }
//
//    @Override
//    public void onViewInitFinished(boolean isX5Core)
//    {
//        Log.d("test","onViewInitFinished,isX5Core ="+isX5Core);
//    }

    @Override
    public void onReceiveValue(String val)
    {
        Log.d("test","onReceiveValue,val ="+val);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {

    }

    private void getFileFromSD(String filePath)
    {
        File mfile = new File(filePath);
        File[] files = mfile.listFiles();
        for (int i = 0; i < files.length; i++)
        {
            File file = files[i];
            checkIsTestFile(file.getPath());
        }
    }

    private void checkIsTestFile(final String fName)
    {
        //#if ${enable.tbs_reader}
        QbSdk.canOpenFile(this, fName, new ValueCallback<Boolean>()
        {

            @Override
            public void onReceiveValue(Boolean arg0)
            {
                if (arg0 == true)
                {
                    mFileList.add(fName);
                    mAdapter.notifyDataSetChanged();
                }
                else
                {
//                    mFileList.add(fName);
//                    mAdapter.notifyDataSetChanged();
                    Log.d("test", "unsupported file:" + fName);
                }
            }
        });
        //#endif
    }

    public void openFileReader(Context context, String pathName)
    {

        HashMap<String, String> params = new HashMap<>();
        params.put("local", "true");
        JSONObject Object = new JSONObject();
        try
        {
            Object.put("pkgName",context.getApplicationContext().getPackageName());
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        params.put("menuData",Object.toString());
        QbSdk.getMiniQBVersion(context);
        int ret = QbSdk.openFileReader(context, pathName, params, this);

    }

    class FileListAdapter extends ArrayAdapter<String> implements AdapterView.OnItemClickListener
    {

        Context mContext	= null;
        int				mRes		= 0;

        List<String> mObjects	= null;

        public FileListAdapter(Context context, int textViewResourceId, List<String> objects)
        {
            super(context, textViewResourceId, objects);
            mContext = context;
            mRes = textViewResourceId;
            mObjects = objects;
        }


        public View getView(int position, View convertView, ViewGroup parent)
        {

            if (convertView == null)
            {
                convertView = View.inflate(mContext, R.layout.vlist, null);
            }

            TextView textView = (TextView) convertView.findViewById(R.id.title);
            final CharSequence text = (CharSequence) mObjects.get(position);
            textView.setText(text);
            textView.setTextColor(Color.BLACK);
            textView.setTextSize(16);

            return convertView;
        }


        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            File file = new File((java.lang.String) mObjects.get(position));
            openFileReader(mContext, file.getAbsolutePath());
        }

    }

}
