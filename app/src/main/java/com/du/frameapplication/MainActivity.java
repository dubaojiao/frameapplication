package com.du.frameapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.du.frameapplication.activity.ConfirmActivity;
import com.du.frameapplication.fragment.AppFragment;
import com.du.frameapplication.fragment.BaseFragment;
import com.du.frameapplication.fragment.GameFragment;
import com.du.frameapplication.fragment.HomeFragment;
import com.du.frameapplication.fragment.MeasureFragment;
import com.du.frameapplication.service.SocketService;
import com.du.frameapplication.util.HttpConnection;
import com.dyhdyh.widget.loading.bar.LoadingBar;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.common.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final int REQUEST_CODE_SCAN = 3333;


    private FrameLayout frameLayout;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            BaseFragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    setTitle("主页");
                    //加载HomeFragment
                    fragment =new HomeFragment();
                    openFragment(fragment);
                    return true;
                case R.id.navigation_dashboard:
                    setTitle("应用");
                    //加载HomeFragment
                    fragment =new AppFragment();
                    openFragment(fragment);
                    return true;
                case R.id.navigation_notifications:
                    setTitle("游戏");
                    //加载HomeFragment
                    fragment =new GameFragment();
                    openFragment(fragment);
                    return true;
                case R.id.navigation_measure:
                    setTitle("测试");
                    //加载HomeFragment
                    fragment =new MeasureFragment();
                    openFragment(fragment);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getSharedPreferences("guide",MODE_PRIVATE);
        boolean isfirst = preferences.getBoolean("isLogin",false);
        if(!isfirst){
            //没有登陆
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
            finish();
        }
        setContentView(R.layout.activity_main);
        frameLayout = (FrameLayout)findViewById(R.id.content_layout);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //加载HomeFragment
        HomeFragment homeFragment =new HomeFragment();
        this.openFragment(homeFragment);
        this.inProcess();
    }

    //进程函数
    private void inProcess(){
        Intent intent = new Intent(MainActivity.this, SocketService.class);
        startService(intent);
    }

    public void openFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_layout, fragment);
        transaction.commit();
    }




    //点击菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //选中菜单item
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.scan) {
            Log.i("AMIN","scan");
            Toast.makeText(getApplicationContext(),"点了扫描按钮",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), CaptureActivity.class);
            startActivityForResult(intent, REQUEST_CODE_SCAN);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG,"onActivityResult");
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra(Constant.CODED_CONTENT);
                Log.i(TAG,"扫描结果为：" + content);
                Toast.makeText(getApplicationContext(),"扫描结果为：" + content,Toast.LENGTH_SHORT).show();
                final String[] d = content.split("C&&A&&&Ac");
                String type = d[0];
                if(d.length != 4){
                    Toast.makeText(getApplicationContext(),"结果解析失败" ,Toast.LENGTH_SHORT).show();
                    return;
                }
                if("login".equals(type)){
                    LoadingBar.make(frameLayout).show();
                    String url = d[1]+d[3];
                    HttpConnection.get(url,new RequestParams(),new  JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            //取消Loading
                            LoadingBar.cancel(frameLayout);
                            try {
                                if("成功".equals(response.getString("data"))){
                                    //登陆 接口
                                    Intent intent = new Intent(getApplicationContext(), ConfirmActivity.class);
                                    //用Bundle携带数据
                                    Bundle bundle=new Bundle();
                                    //传递name参数为tinyphp
                                    bundle.putString("url", d[2]);
                                    bundle.putString("key", d[3]);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }else {
                                    Toast.makeText(getApplicationContext(),"服务器请求失败" ,Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }
            }
        }
    }
}
