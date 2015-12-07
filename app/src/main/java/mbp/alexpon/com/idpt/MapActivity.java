package mbp.alexpon.com.idpt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

/**
 * Created by apple on 15/6/29.
 */
public class MapActivity extends Activity {

    private ImageView nowPlace;
    private Animation move;
    private double[] RSSI;
    private double [] out;
    private Knn knn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.indoor_map);
        initView();
        changeMap();
       }

    public void initView(){
        nowPlace = (ImageView) findViewById(R.id.focus);
        RSSI = new double[10];
        out = new double [2];
        knn = new Knn();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void changeMap(){
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();//取得Bundle
        RSSI[0] = bundle.getDouble("KEY_1");
        RSSI[1] = bundle.getDouble("KEY_2");
        RSSI[2] = bundle.getDouble("KEY_3");
        RSSI[3] = bundle.getDouble("KEY_4");
        RSSI[4] = bundle.getDouble("KEY_5");
        RSSI[5] = bundle.getDouble("KEY_6");
        RSSI[6] = bundle.getDouble("KEY_7");
        RSSI[7] = bundle.getDouble("KEY_8");
        RSSI[8] = 0;
        RSSI[9] = 0;
        out = knn.formain(RSSI);
        //x平移50前 y平移10後
        int x = (int)(out[0]*60+10);
        int y = (int)(out[1]*60+50);

        move = new TranslateAnimation(y, y, x, x);
        //setDuration (long durationMillis) 設定動畫開始到結束的執行時間
        move.setDuration(3000);
        //setRepeatCount (int repeatCount) 設定重複次數 -1為無限次數 0
        move.setRepeatCount(-1);
        //將動畫參數設定到圖片並開始執行動畫
        nowPlace.startAnimation(move);
    }

}
