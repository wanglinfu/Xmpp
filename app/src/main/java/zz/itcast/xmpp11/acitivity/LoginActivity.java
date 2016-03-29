package zz.itcast.xmpp11.acitivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

import java.io.IOException;

import zz.itcast.xmpp11.R;
import zz.itcast.xmpp11.base.MyApp;
import zz.itcast.xmpp11.service.ContactService;
import zz.itcast.xmpp11.tools.ThreadUtil;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String HOST = "192.168.11.72";
    private static final int PORT = 5222;
    private EditText et_account;
    private EditText et_password;
    private Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();


//        2.权限 线程
//        1.创建消息通道
        // ctrl +  P
        ThreadUtil.runOnBackThread(new Runnable() {
            @Override
            public void run() {

                try {
                    ConnectionConfiguration config = new ConnectionConfiguration(HOST, PORT);
                    config.setDebuggerEnabled(true); //  是否打印调试日志
                    config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled); // 将安全模式关掉
                    XMPPConnection conn = new XMPPTCPConnection(config);
                    conn.connect();
                    // 保留消息通道
                    MyApp.conn = conn;
                    //连接成功
                    ThreadUtil.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, "连接成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    ThreadUtil.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

//        4.保留帐号
    }

    private void initView() {
        et_account = (EditText) findViewById(R.id.et_account);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_login = (Button) findViewById(R.id.btn_login);

        btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                submit();
                break;
        }
    }

    private void submit() {
        // validate
        String account = et_account.getText().toString().trim();
        if (TextUtils.isEmpty(account)) {
            Toast.makeText(this, "account不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String password = et_password.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "password不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        login(account, password);
    }

    /**
     * 登录
     *
     * @param account
     * @param password
     */
    private void login(final String account, final String password) {
        //  连接成功才去登录
        if (MyApp.conn != null && MyApp.conn.isConnected()) {
            ThreadUtil.runOnBackThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        MyApp.conn.login(account, password);
                        //用户名
                        MyApp.me = account;
                        //帐号 MyApp.conn.getServiceName()  获取服务器域名  动态的
                        System.out.println("MyApp.conn.getServiceName() = "+MyApp.conn.getServiceName());
                        MyApp.account = account+"@"+ MyApp.conn.getServiceName();
                        //  进入到主界面
                        startActivity(new Intent(getBaseContext(),MainActivity.class));
                        startService(new Intent(getBaseContext(), ContactService.class));
                        finish();
                        ThreadUtil.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        ThreadUtil.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }
    }


}
