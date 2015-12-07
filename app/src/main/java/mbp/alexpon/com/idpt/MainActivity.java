package mbp.alexpon.com.idpt;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Iterator;

public class MainActivity extends ActionBarActivity implements BeaconConsumer {

    private static final String MODE_SCANNING = "Stop Scanning";
    private static final String MODE_STOPPED = "Start Scanning";
    private BeaconManager beaconManager;
    private Region region;
    private Button switchButton;
    private Button btn_indoor;
    private TextView distance1;
    private TextView distance2;
    private TextView distance3;
    private TextView distance4;
    private TextView distance5;
    private TextView distance6;
    private TextView distance7;
    private TextView distance8;
    private int sum1 = 0, cnt1 = 0;
    private int sum2 = 0, cnt2 = 0;
    private int sum3 = 0, cnt3 = 0;
    private int sum4 = 0, cnt4 = 0;
    private int sum5 = 0, cnt5 = 0;
    private int sum6 = 0, cnt6 = 0;
    private int sum7 = 0, cnt7 = 0;
    private int sum8 = 0, cnt8 = 0;

    private Boolean adFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init_View();
        setListener();
        verifyBluetooth();

        ScanApp app = (ScanApp) this.getApplication();
        beaconManager = app.getBeaconManager();
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        //beaconManager.setForegroundBetweenScanPeriod((long)2000);
        beaconManager.bind(this);

        region = new Region("myRangUniqueId", null, Identifier.fromInt(4660), null);

        switchButton.setText(MODE_STOPPED);

    }

    @Override
    protected void onResume() {
        super.onResume();
        sum1 = 0;
        sum2 = 0;
        sum3 = 0;
        sum4 = 0;
        sum5 = 0;
        sum6 = 0;
        sum7 = 0;
        sum8 = 0;
        cnt1 = 0;
        cnt2 = 0;
        cnt3 = 0;
        cnt4 = 0;
        cnt5 = 0;
        cnt6 = 0;
        cnt7 = 0;
        cnt8 = 0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }

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

    private void init_View() {
        switchButton = (Button) findViewById(R.id.switchB);
        btn_indoor = (Button) findViewById(R.id.button);
        distance1 = (TextView) findViewById(R.id.distance1);
        distance2 = (TextView) findViewById(R.id.distance2);
        distance3 = (TextView) findViewById(R.id.distance3);
        distance4 = (TextView) findViewById(R.id.distance4);
        distance5 = (TextView) findViewById(R.id.distance5);
        distance6 = (TextView) findViewById(R.id.distance6);
        distance7 = (TextView) findViewById(R.id.distance7);
        distance8 = (TextView) findViewById(R.id.distance8);
    }

    public void setListener() {
        switchButton.setOnClickListener(myListener);
        btn_indoor.setOnClickListener(myListener);
    }

    private Button.OnClickListener myListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.switchB:
                    if (switchButton.getText().toString().equals(MODE_SCANNING)) {
                        stopScanning();
                    } else {
                        startScanning();
                    }
                    break;
                case R.id.button:
                    Bundle bundle = new Bundle();
                    if (cnt1 == 0) {
                        break;
                    } else {
                        bundle.putDouble("KEY_1", sum1 / cnt1);
                        bundle.putDouble("KEY_2", sum2 / cnt2);
                        bundle.putDouble("KEY_3", sum3 / cnt3);
                        bundle.putDouble("KEY_4", sum4 / cnt4);
                        bundle.putDouble("KEY_5", sum5 / cnt5);
                        bundle.putDouble("KEY_6", sum6 / cnt6);
                        bundle.putDouble("KEY_7", sum7 / cnt7);
                        bundle.putDouble("KEY_8", sum8 / cnt8);
                        Intent idIntent = new Intent();
                        idIntent.putExtras(bundle); //將參數放入
                        idIntent.setClass(MainActivity.this, MapActivity.class);
                        startActivity(idIntent);
                        finish();
                    }
                    break;
            }
        }
    };

    private void startScanning() {
        // Set UI elements to the correct state.
        switchButton.setText(MODE_SCANNING);
        adFlag = true;
        //Start scanning again.
        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    //迭代器
                    Iterator<Beacon> beaconIterator = beacons.iterator();
                    if (beaconIterator.hasNext()) {
                        do {
                            Beacon beacon = beaconIterator.next();
                            logBeaconData(beacon);
                        } while (beaconIterator.hasNext());
                    }
                }
            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(region);
        } catch (RemoteException e) {

        }

    }

    /**
     * Stop looking for beacons.
     */
    private void stopScanning() {
        switchButton.setText(MODE_STOPPED);
        sum1 = 0;
        sum2 = 0;
        sum3 = 0;
        sum4 = 0;
        sum5 = 0;
        sum6 = 0;
        sum7 = 0;
        sum8 = 0;
        cnt1 = 0;
        cnt2 = 0;
        cnt3 = 0;
        cnt4 = 0;
        cnt5 = 0;
        cnt6 = 0;
        cnt7 = 0;
        cnt8 = 0;
        try {
            beaconManager.stopRangingBeaconsInRegion(region);
        } catch (RemoteException e) {
        }
    }

    private void logBeaconData(Beacon beacon) {

        int txPower = beacon.getTxPower();
        int rssi = beacon.getRssi();
        double dis = beacon.getDistance();
        StringBuilder scanString = new StringBuilder();
        scanString.append("Maj. Mnr.: ").append(beacon.getId2()).append("-").append(beacon.getId3());
        scanString.append(" Dis:  ").append(dis);
        if (beacon.getId2().toInt() == 4660 && beacon.getId3().toInt() == 5 && dis < 1 && adFlag) {
            adFlag = false;
            Intent adIntent = new Intent();
            adIntent.setClass(MainActivity.this, AdActivity.class);
            startActivity(adIntent);
        }
        logToDisplay(scanString.toString(), beacon.getId3(), rssi);

    }

    private void logToDisplay(final String line, final Identifier minor, final int rssi) {
        final DecimalFormat df = new DecimalFormat("0.00");
        runOnUiThread(new Runnable() {
            public void run() {
                //detailT.setText(line);
                if (minor.equals(Identifier.fromInt(1))) {
                    sum1 = sum1 + rssi;
                    cnt1++;
                    distance1.setText(line + "\nRssi: " + (sum1 / cnt1));
                } else if (minor.equals(Identifier.fromInt(2))) {
                    sum2 = sum2 + rssi;
                    cnt2++;
                    distance2.setText(line + "\nRssi: " + (sum2 / cnt2));
                } else if (minor.equals(Identifier.fromInt(3))) {
                    sum3 = sum3 + rssi;
                    cnt3++;
                    distance3.setText(line + "\nRssi: " + (sum3 / cnt3));
                } else if (minor.equals(Identifier.fromInt(4))) {
                    sum4 = sum4 + rssi;
                    cnt4++;
                    distance4.setText(line + "\nRssi: " + (sum4 / cnt4));
                } else if (minor.equals(Identifier.fromInt(5))) {
                    sum5 = sum5 + rssi;
                    cnt5++;
                    distance5.setText(line + "\nRssi: " + (sum5 / cnt5));
                } else if (minor.equals(Identifier.fromInt(6))) {
                    sum6 = sum6 + rssi;
                    cnt6++;
                    distance6.setText(line + "\nRssi: " + (sum6 / cnt6));
                } else if (minor.equals(Identifier.fromInt(7))) {
                    sum7 = sum7 + rssi;
                    cnt7++;
                    distance7.setText(line + "\nRssi: " + (sum7 / cnt7));
                } else if (minor.equals(Identifier.fromInt(8))) {
                    sum8 = sum8 + rssi;
                    cnt8++;
                    distance8.setText(line + "\nRssi: " + (sum8 / cnt8));
                }
            }
        });
    }

    private void verifyBluetooth() {
        try {
            if (!BeaconManager.getInstanceForApplication(this).checkAvailability()) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Bluetooth not enabled");
                builder.setMessage("Please enable bluetooth in settings and restart this application.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        finish();
                        System.exit(0);
                    }
                });
                builder.show();
            }
        } catch (RuntimeException e) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Bluetooth LE not available");
            builder.setMessage("Sorry, this device does not support Bluetooth LE.");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface dialog) {
                    finish();
                    System.exit(0);
                }

            });
            builder.show();

        }

    }

    @Override
    public void onBeaconServiceConnect() {
    }

}