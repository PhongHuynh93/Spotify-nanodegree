package dhbk.android.spotifygcs;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by phongdth.ky on 7/21/2016.
 */
public abstract class BaseService extends Service{
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return getBinder();
    }

    // Called by the system every time a client explicitly starts the service by calling startService(Intent)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initService(intent);
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }

    public abstract IBinder getBinder();
    public abstract void initService(Intent intent);
}
