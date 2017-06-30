using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using Vuforia;

public class cloudRecoHandler : MonoBehaviour, ICloudRecoEventHandler {

    private CloudRecoBehaviour mCloudRecoBehaviour;
    private bool mIsScanning = false;
    private string mTargetMetadata = "";

	// Use this for initialization
	void Start () {
        mCloudRecoBehaviour = GetComponent<CloudRecoBehaviour>();
        if (mCloudRecoBehaviour) {
            mCloudRecoBehaviour.RegisterEventHandler(this);
        }
	}
	
	// Update is called once per frame
	void Update () {
		
	}

    public void OnNewSearchResult(TargetFinder.TargetSearchResult result) {
        mTargetMetadata = result.MetaData;
        mCloudRecoBehaviour.CloudRecoEnabled = false;
    }

    public void OnStateChanged(bool scanning) {
        mIsScanning = scanning;
        if (scanning) {
            ObjectTracker tracker = TrackerManager.Instance.GetTracker<ObjectTracker>();
            tracker.TargetFinder.ClearTrackables();
        }
    }

    public void OnGUI() {
        GUI.Box(new Rect(100, 100, 200, 50), mIsScanning ? "scanning" : "Not scanning");
        GUI.Box(new Rect(100, 200, 200, 50), "MetaData:" + mTargetMetadata);
        if (!mIsScanning)
        {
            if (GUI.Button(new Rect(100, 300, 200, 50), "Restart Scanning"))
            {
                mCloudRecoBehaviour.CloudRecoEnabled = true;
            }
        }
    }

    public void OnInitialized() {

    }

    public void OnInitError(TargetFinder.InitState initError) {

    }

    public void OnUpdateError(TargetFinder.UpdateState updateError) {

    }

}
