using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using Vuforia;

public class CloudRecoHandler : MonoBehaviour, ICloudRecoEventHandler
{
    private ObjectTracker mObjectTracker;
    //private ContentManager mContentManager;
    private CloudRecoBehaviour mCloudRecoBehaviour;
    private bool mIsScanning = false;
    private string mTargetName = "";

    // the parent gameobject of the referenced ImageTargetTemplate - reused for all target search results
    private GameObject mParentOfImageTargetTemplate;
    public ImageTargetBehaviour ImageTargetTemplate;

    // Use this for initialization
    void Start () {
        // look up the gameobject containing the ImageTargetTemplate:
        mParentOfImageTargetTemplate = ImageTargetTemplate.gameObject;

        mCloudRecoBehaviour = GetComponent<CloudRecoBehaviour>();
        if (mCloudRecoBehaviour) {
            mCloudRecoBehaviour.RegisterEventHandler(this);
        }
	}
	
	// Update is called once per frame
	void Update () {
		
	}

    public void OnNewSearchResult(TargetFinder.TargetSearchResult result) {
        mTargetName = result.TargetName;
        mCloudRecoBehaviour.CloudRecoEnabled = false;
        // First clear all trackables
        mObjectTracker.TargetFinder.ClearTrackables(false);
        // enable the new result with the same ImageTargetBehaviour:
        ImageTargetBehaviour imageTargetBehaviour = mObjectTracker.TargetFinder.EnableTracking(result, mParentOfImageTargetTemplate) as ImageTargetBehaviour;
    }

    public void OnStateChanged(bool scanning) {
        mIsScanning = scanning;
        if (scanning) {
            mObjectTracker.TargetFinder.ClearTrackables(false);
            //mContentManager.ShowObject(false);
        }
    }

    public void OnGUI() {
        GUI.Box(new Rect(100, 100, 200, 50), mIsScanning ? "scanning" : "Not scanning");
        GUI.Box(new Rect(100, 200, 200, 50), "Name:" + mTargetName);
        if (!mIsScanning)
        {
            if (GUI.Button(new Rect(100, 300, 200, 50), "Restart Scanning"))
            {
                mCloudRecoBehaviour.CloudRecoEnabled = true;
            }
        }
    }

    public void OnInitialized() {
        Debug.Log("Cloud Reco initialized successfully.");

        // get a reference to the Object Tracker, remember it
        mObjectTracker = TrackerManager.Instance.GetTracker<ObjectTracker>();
        //mContentManager = FindObjectOfType<ContentManager>();
    }

    public void OnInitError(TargetFinder.InitState initError) {

    }

    public void OnUpdateError(TargetFinder.UpdateState updateError) {

    }

}
