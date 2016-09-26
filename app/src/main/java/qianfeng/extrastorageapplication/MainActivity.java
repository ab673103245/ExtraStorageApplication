package qianfeng.extrastorageapplication;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        File externalCacheDir = this.getExternalCacheDir();
        Log.d("google-my:", "onCreate: externalCacheDir : " + externalCacheDir.getAbsolutePath());

        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        Log.d("google-my:", "onCreate: externalStorageDirectory :  " + externalStorageDirectory);

        File externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        Log.d("google-my:", "onCreate: externalStoragePublicDirectory : " + externalStoragePublicDirectory);

        File externalFilesDir = this.getExternalFilesDir(Environment.DIRECTORY_DCIM);
                            // getExternalFilesDir(String type), 指明类型


    }
}
