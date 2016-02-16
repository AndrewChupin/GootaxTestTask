package com.example.barsik.gootax;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import com.actionbarsherlock.app.SherlockActivity;

public class SplashActivity extends SherlockActivity
{
    Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        /**Delete TabBar ActionBar...*/
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);



        thread = new Thread()
        {
            public void run()
            {
                try
                {
                    sleep(2000);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    Intent myIntent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(myIntent);
                }
            }
        };
        thread.start();
    }
}
