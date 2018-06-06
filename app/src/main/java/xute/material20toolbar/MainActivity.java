package xute.material20toolbar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import xute.materialtoolbar.BottomToolbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        BottomToolbar bottomToolbar = findViewById(R.id.bottom_toolbar);
        bottomToolbar.setButtonClickListener(new BottomToolbar.ButtonClickListener() {
            @Override
            public void onPlusButtonClicked() {
                Toast.makeText(MainActivity.this,"Button clicked!",Toast.LENGTH_LONG).show();
            }
        });

    }
}
