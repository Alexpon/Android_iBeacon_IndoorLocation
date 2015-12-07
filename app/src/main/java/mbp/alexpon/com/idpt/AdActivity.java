package mbp.alexpon.com.idpt;

/**
 * Created by apple on 15/6/29.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/**
 * Created by alexpon on 2015/5/24.
 */


public class AdActivity extends Activity {

    private Button buy;
    private Button leave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad);
        initView();
        setListener();
    }

    public void initView(){
        buy = (Button) findViewById(R.id.buy);
        leave = (Button) findViewById(R.id.leave);
    }

    public void setListener(){
        buy.setOnClickListener(adListener);
        leave.setOnClickListener(adListener);
    }

    private Button.OnClickListener adListener = new Button.OnClickListener(){
        @Override
        public void onClick(View v){
            switch (v.getId()) {
                case R.id.buy:
                    Intent nfcIntent = new Intent();
                    nfcIntent.setClass(AdActivity.this, MainActivity.class);
                    startActivity(nfcIntent);
                    break;
                case R.id.leave:
                    finish();
                    break;
            }
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}