using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using Vuforia;

public class CloudRecoHandler : MonoBehaviour, ICloudRecoEventHandler
{
    private ObjectTracker mObjectTracker;
    private ContentManager mContentManager;
    private CloudRecoBehaviour mCloudRecoBehaviour;
    private bool mIsScanning = false;
    private float touchduration;
    private Touch touch;
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
	void Update () {
        if (Input.touchCount > 0)
        {
            touchduration += Time.deltaTime;
            touch = Input.GetTouch(0);
            if (touch.phase == TouchPhase.Ended && touchduration < 0.2f &&touch.tapCount==2)
            {
                StartCoroutine(TriggerAutoFocusAndEnableContinuousFocusIfSet());
            }
        }
        else
        {
            touchduration = 0;
        }
    }

    private IEnumerator TriggerAutoFocusAndEnableContinuousFocusIfSet()
    {
        Debug.Log("Double-click: auto focus.");
        CameraDevice.Instance.SetFocusMode(CameraDevice.FocusMode.FOCUS_MODE_TRIGGERAUTO);
        yield return new WaitForSeconds(1.0f);
        CameraDevice.Instance.SetFocusMode(CameraDevice.FocusMode.FOCUS_MODE_CONTINUOUSAUTO);
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
            mContentManager.ShowInfoPoint(false);
        }

        ShowScanLine(scanning);
    }

    private void ShowScanLine(bool show)
    {
        // Toggle scanline rendering
        if (scanLine != null)
        {
            Renderer scanLineRenderer = scanLine.GetComponent<Renderer>();
            if (show)
            {
                // Enable scan line rendering
                if (!scanLineRenderer.enabled)
                    scanLineRenderer.enabled = true;

                scanLine.ResetAnimation();
            }
            else
            {
                // Disable scanline rendering
                if (scanLineRenderer.enabled)
                    scanLineRenderer.enabled = false;
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
