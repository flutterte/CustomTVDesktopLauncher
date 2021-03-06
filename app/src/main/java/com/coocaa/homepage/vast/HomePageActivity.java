package com.coocaa.homepage.vast;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.tianci.movieplatform.R;

public class HomePageActivity extends AppCompatActivity {

    private static final String TAG = "HomePageActivity";
    private ImageView iv;
    private TextView tvDefaultName;
    private SharedPreferences sharedPreferences;
    private Handler handler;

    /* access modifiers changed from: protected */
    @Override
    // androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, androidx.fragment.app.FragmentActivity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hasCancel=false;
        setContentView(R.layout.activity_main);
        iv = ((ImageView) findViewById(R.id.iv_app));
        tvDefaultName = ((TextView) findViewById(R.id.tv_default_app));
        sharedPreferences = getSharedPreferences("config", Context.MODE_WORLD_READABLE);
        String packagename = sharedPreferences.getString("packagename", "");
        Log.w(TAG,"PACKAGENAME:"+packagename);
        handler=new Handler();
        updateUi(packagename);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                doDefaultLaunch(packagename);
            }
        },5000);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(HomePageActivity.this, "?????????,????????????????????????,??????????????????????????????????????????,????????????????????????", Toast.LENGTH_SHORT).show();
            }
        },500);

    }

    private void doDefaultLaunch(String packagename) {
        if(TextUtils.isEmpty(packagename)){

            launchDesktop();
        }  else if(packagename.equals(this.getPackageName())){
            Toast.makeText(this, "??????????????????!", Toast.LENGTH_SHORT).show();
        } else if(packagename.equals("android")){
            Toast.makeText(this, "????????????", Toast.LENGTH_SHORT).show();
        }else{
            PackageManager packageManager = getPackageManager();
            Intent intent = packageManager.getLaunchIntentForPackage(packagename);
            launchByIntent(intent, ""+packagename);
        }
    }
    boolean hasCancel=false;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.w(TAG,"KEYCODE_:"+keyCode);
        switch (keyCode){

            case KeyEvent.KEYCODE_ENTER:     //?????????enter
            case KeyEvent.KEYCODE_DPAD_CENTER:
                handler.removeCallbacksAndMessages(null);
                Log.d(TAG,"enter--->");
                break;
            case KeyEvent.KEYCODE_CHANNEL_DOWN:
                launchTv();
               return true;
            case KeyEvent.KEYCODE_MENU:
                launchTv();
                break;
            case KeyEvent.KEYCODE_CHANNEL_UP:
                launchMacket();
                break;
            case KeyEvent.KEYCODE_BACK:    //?????????
                Log.d(TAG,"back--->");
                if(!hasCancel){
                    cancelAutolAUNCH();
                    return true;
                }else{

                }



            case KeyEvent.KEYCODE_SETTINGS: //?????????
                Log.d(TAG,"setting--->");
              break;

            case KeyEvent.KEYCODE_DPAD_DOWN:   //?????????

                /*    ?????????????????????????????????????????????????????????????????????????????? ???????????????????????????
                 *    exp:KeyEvent.ACTION_UP
                 */
                if (event.getAction() == KeyEvent.ACTION_DOWN){

                    Log.d(TAG,"down--->");
                }
                if(!hasCancel){
                    cancelAutolAUNCH();
                    return true;
                }
                break;

            case KeyEvent.KEYCODE_DPAD_UP:   //?????????
                Log.d(TAG,"up--->");
                if(!hasCancel){
                    cancelAutolAUNCH();
                    return true;
                }
                break;


            case KeyEvent.KEYCODE_DPAD_LEFT: //?????????
                if(!hasCancel){
                    cancelAutolAUNCH();
                    return true;
                }
                Log.d(TAG,"left--->");

                break;

            case KeyEvent.KEYCODE_DPAD_RIGHT:  //?????????
                Log.d(TAG,"right--->");
                if(!hasCancel){
                    cancelAutolAUNCH();
                    return true;
                }
                break;


        }

        return super.onKeyDown(keyCode, event);

    }

    private void cancelAutolAUNCH() {
        handler.removeCallbacksAndMessages(null);
        Toast.makeText(this, "?????????????????????,????????????????????????..", Toast.LENGTH_SHORT).show();
        hasCancel=true;
    }

    private void launchDesktop() {
        Log.w(TAG,"Launch desktop");
        Intent paramIntent = new Intent("android.intent.action.MAIN");
        paramIntent.setComponent(new ComponentName("android", "com.android.internal.app.ResolverActivity"));
        paramIntent.addCategory("android.intent.category.DEFAULT");
        paramIntent.addCategory("android.intent.category.HOME");
        startActivity(paramIntent);
    }

    public void open(View view) {
        handler.removeCallbacksAndMessages(null);
        switch (view.getId()) {
            case R.id.btn_desktop:
                launchDesktop();
                break;
            case R.id.btn_market:
                launchMacket();
                break;
            case R.id.btn_tv:
                launchTv();
                break;
            case R.id.btn_choose_app:
                Intent intent=new Intent(this,ChooseAppActivity.class);
                startActivityForResult(intent,1);
                break;
        }


    }

    private void launchTv() {
//        com.dianshijia.newlive
        PackageManager packageManager = getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage("com.dianshijia.newlive");
        launchByIntent(intent, "?????????");


    }

    private void launchMacket() {

        Intent intent = getPackageManager().getLaunchIntentForPackage("com.ant.store.appstore");
        launchByIntent(intent,"????????????");
    }

    private void launchByIntent(Intent intent, String packageName) {
        try {
            startActivity(intent);
        } catch (Throwable e) {
            Toast.makeText(this, "??????"+packageName+"???????????????????????????????????????!", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){

            String packagename = data.getStringExtra("packagename");
          sharedPreferences.edit().putString("packagename", packagename).apply();
            updateUi(packagename);
            Toast.makeText(this, "????????????", Toast.LENGTH_SHORT).show();

        }else{
            new AlertDialog.Builder(this).setMessage("??????????????????,?????????????????????????????????????").setPositiveButton("??????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(HomePageActivity.this, "????????????!", Toast.LENGTH_SHORT).show();
                    sharedPreferences.edit().putString("packagename", "").apply();
                    updateUi("");

                }
            }).setNegativeButton("??????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();

        }
    }

    private void updateUi(String packagename) {
        if(TextUtils.isEmpty(packagename)){
            iv.setImageResource(R.mipmap.ic_launcher);
            tvDefaultName.setText("??????");
            return;
        } else if(packagename.equals(this.getPackageName())){
            iv.setImageResource(R.mipmap.ic_launcher);
            tvDefaultName.setText("???????????????");
            return;
        }
        try {
            PackageInfo packageInfo = this.getPackageManager().getPackageInfo(packagename, PackageManager.GET_UNINSTALLED_PACKAGES);
            ApplicationInfo applicationInfo =packageInfo.applicationInfo;
                    String appName= this.getPackageManager().getApplicationLabel(applicationInfo).toString();  //
            tvDefaultName.setText(appName+"/"+applicationInfo.packageName+"/V"+packageInfo.versionName+" build "+packageInfo.versionCode);
            Drawable drawable = applicationInfo.loadIcon(getPackageManager());
            iv.setImageDrawable(drawable);



        } catch (Throwable e) {
            Toast.makeText(this, "????????????!"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}