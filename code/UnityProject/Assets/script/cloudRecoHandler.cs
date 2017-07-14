using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using Vuforia;

public class cloudRecoHandler : MonoBehaviour, ICloudRecoEventHandler
{
    private ObjectTracker mObjectTracker;
    private ContentManager mContentManager;
    private CloudRecoBehaviour mCloudRecoBehaviour;
    private bool mIsScanning = false;

    // the parent gameobject of the referenced ImageTargetTemplate - reused for all target search results
    private GameObject mParentOfImageTargetTemplate;

    public ImageTargetBehaviour ImageTargetTemplate;
    public ScanLine scanLine;

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
	void Update ()
    {

    }

    public void OnNewSearchResult(TargetFinder.TargetSearchResult result) {
        // First clear all trackables
        mObjectTracker.TargetFinder.ClearTrackables(false);
        // enable the new result with the same ImageTargetBehaviour:
        ImageTargetBehaviour imageTargetBehaviour = mObjectTracker.TargetFinder.EnableTracking(result, mParentOfImageTargetTemplate) as ImageTargetBehaviour;

        if (imageTargetBehaviour != null)
        {
            // Stop the target finder
            mCloudRecoBehaviour.CloudRecoEnabled = false;

            // Stop showing the scan-line
            ShowScanLine(false);

            mContentManager.TargetCreated(result.UniqueTargetId);
        }
    }

    public void OnStateChanged(bool scanning) {
        mIsScanning = scanning;
        if (scanning) {
            mObjectTracker.TargetFinder.ClearTrackables(false);
            mCloudRecoBehaviour.CloudRecoEnabled = true;
            mContentManager.OnCancel();
        }

        ShowScanLine(scanning);
    }

    public void RestartScanning()
    {
        OnStateChanged(true);
    }

    private void ShowScanLine(bool show)
    {
        // Toggle scanline rendering
        if (scanLine != null)
        {
            Renderer scanLineRenderer = scanLine.GetComponent<Renderer>();
            Collider scanLineCollider = scanLine.GetComponent<Collider>();
            if (show)
            {
                // Enable scan line rendering
                if (!scanLineRenderer.enabled)
                    scanLineRenderer.enabled = true;
                if (!scanLineCollider.enabled)
                    scanLineCollider.enabled = true;

                scanLine.ResetAnimation();
            }
            else
            {
                // Disable scanline rendering
                if (scanLineRenderer.enabled)
                    scanLineRenderer.enabled = false;
                if (scanLineCollider.enabled)
                    scanLineCollider.enabled = false;
            }
        }
    }

    public void OnInitialized() {
        Debug.Log("Cloud Reco initialized successfully.");

        // get a reference to the Object Tracker, remember it
        mObjectTracker = TrackerManager.Instance.GetTracker<ObjectTracker>();
        mContentManager = FindObjectOfType<ContentManager>();
    }

    public void OnInitError(TargetFinder.InitState initError) {

    }

    public void OnUpdateError(TargetFinder.UpdateState updateError) {

    }

}
