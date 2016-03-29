package zz.itcast.xmpp11.acitivity;

import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import zz.itcast.xmpp11.R;
import zz.itcast.xmpp11.tools.ThreadUtil;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ThreadUtil.runOnBackThread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(2000);
                startActivity(new Intent(getBaseContext(),LoginActivity.class));
                finish();
            }
        });

    }
}
