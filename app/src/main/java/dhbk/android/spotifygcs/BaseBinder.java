package dhbk.android.spotifygcs;

import android.app.Service;
import android.os.Binder;

/**
 * Created by phongdth.ky on 7/21/2016.
 *  a base binder for child to get and set service
 */
public abstract class BaseBinder<S extends Service> extends Binder {
    private S mService;

    public S getService() {
        return mService;
    }

    public void setService(S service) {
        mService = service;
    }
}