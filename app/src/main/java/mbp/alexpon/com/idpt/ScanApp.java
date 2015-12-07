package mbp.alexpon.com.idpt;

/**
 * Created by apple on 15/6/29.
 */

import android.app.Application;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;

public class ScanApp extends Application{
    private BackgroundPowerSaver backgroundPowerSaver;
    private BeaconManager beaconManager;

    public BeaconManager getBeaconManager() {
        return beaconManager;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // Allow scanning to continue in the background.
        backgroundPowerSaver = new BackgroundPowerSaver(this);
        beaconManager = BeaconManager.getInstanceForApplication(this);
    }

}