package com.FindHere.cloudReco;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;

import com.vuforia.ObjectTracker;
import com.vuforia.TargetFinder;
import com.vuforia.TrackerManager;
/*import com.vuforia.samples.Books.app.Books.Book;

/**
 * Created by msi on 2017/6/30.
 */
/*
public class RecoActivity extends Activity implements View.OnClickListener {
    // Stores the current status of the target ( if is being displayed or not )
    private static final int BOOKINFO_NOT_DISPLAYED = 0;
    private static final int BOOKINFO_IS_DISPLAYED = 1;
    // These codes match the ones defined in TargetFinder in Vuforia.jar
    static final int INIT_SUCCESS = 2;
    static final int INIT_ERROR_NO_NETWORK_CONNECTION = -1;
    static final int INIT_ERROR_SERVICE_NOT_AVAILABLE = -2;
    static final int UPDATE_ERROR_AUTHORIZATION_FAILED = -1;
    static final int UPDATE_ERROR_PROJECT_SUSPENDED = -2;
    static final int UPDATE_ERROR_NO_NETWORK_CONNECTION = -3;
    static final int UPDATE_ERROR_SERVICE_NOT_AVAILABLE = -4;
    static final int UPDATE_ERROR_BAD_FRAME_QUALITY = -5;
    static final int UPDATE_ERROR_UPDATE_SDK = -6;
    static final int UPDATE_ERROR_TIMESTAMP_OUT_OF_RANGE = -7;
    static final int UPDATE_ERROR_REQUEST_TIMEOUT = -8;

    // Active Book Data
    private Book mBookData;
    private String mBookJSONUrl;

    private static final String kAccessKey = "669ab267d2332a9c8f8c05730f2abd00a8c34fbd";
    private static final String kSecretKey = "7afac700a02bd5d68ab2b0b4dcaca982dda5a17e";

    // declare scan line and its animation
    private View scanLine;
    private TranslateAnimation scanAnimation;
    private boolean scanLineStarted;
    private Activity mActivity = null;
    public void deinitBooks()
    {
        // Get the object tracker:
        TrackerManager trackerManager = TrackerManager.getInstance();
        ObjectTracker objectTracker = (ObjectTracker) trackerManager
                .getTracker(ObjectTracker.getClassType());
        if (objectTracker == null)
        {
            Log.e(LOGTAG,
                    "Failed to destroy the tracking data set because the ObjectTracker has not"
                            + " been initialized.");
            return;
        }

        // Deinitialize target finder:
        TargetFinder finder = objectTracker.getTargetFinder();
        finder.deinit();
    }
    public void cleanTargetTrackedId()
    {
        synchronized (lastTargetId)
        {
            lastTargetId = "";
        }
    }

}*/
