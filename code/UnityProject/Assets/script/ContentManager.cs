using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using Vuforia;

public class ContentManager : MonoBehaviour,ITrackableEventHandler {
    private GameObject infoPoint;
    private CloudRecoBehaviour mCloudRecoBehaviour;
    private string status;

    public GameObject AugmentationObject;
    public GameObject TextField;
    public Button CancelButton;
    public AnimationsManager AnimationsManager;

    // Use this for initialization
    void Start () {
        infoPoint = AugmentationObject.transform.parent.gameObject;

        TrackableBehaviour trackableBehaviour = infoPoint.transform.parent.GetComponent<TrackableBehaviour>();
        if (trackableBehaviour)
        {
            trackableBehaviour.RegisterTrackableEventHandler(this);
        }
        mCloudRecoBehaviour = FindObjectOfType<CloudRecoBehaviour>();
        SetCancelButtonVisible(false);
        ShowInfoPoint(false);
        ShowTextField(false);
        status = "infoPoint";
    }

    public void Show(bool tf)
    {
        if (status == "infoPoint")
        {
            ShowInfoPoint(tf);
        }
        else if (status== "textField")
        {
            if(tf)AnimationsManager.PlayAnimationTo3D(TextField);
            else AnimationsManager.PlayAnimationTo2D(TextField);
            ShowTextField(tf);
        }
    }

    public void OnTrackableStateChanged(
                                    TrackableBehaviour.Status previousStatus,
                                    TrackableBehaviour.Status newStatus)
    {
        if (newStatus == TrackableBehaviour.Status.DETECTED ||
                newStatus == TrackableBehaviour.Status.TRACKED ||
                newStatus == TrackableBehaviour.Status.EXTENDED_TRACKED)
        {
            Show(true);
            Debug.Log("content manager show: true");
        }
        else
        {
            Show(false);
            Debug.Log("content manager show: false");
        }
    }

    public void TargetCreated(string targetId)
    {
        Vector3 parentPosition = infoPoint.transform.position;

        for(int i = 0; i < 20; i++)
        {
            GameObject sphere = Instantiate(AugmentationObject);
            sphere.transform.parent = AugmentationObject.transform.parent;
            sphere.transform.position = new Vector3(
                parentPosition.x + Random.Range(-1f, 1f), 
                parentPosition.y + Random.Range(0, 0.5f), 
                parentPosition.z + Random.Range(-1f, 1f));
            sphere.transform.name = targetId + i;
        }
        AugmentationObject.SetActive(false);
    }

    public void ShowInfoPoint(bool tf)
    {
        Renderer[] rendererComponents = infoPoint.GetComponentsInChildren<Renderer>();
        Collider[] colliderComponents = infoPoint.GetComponentsInChildren<Collider>();

        // Enable rendering:
        foreach (Renderer component in rendererComponents)
        {
            component.enabled = tf;
        }

        // Enable colliders:
        foreach (Collider component in colliderComponents)
        {
            component.enabled = tf;
        }
    }

    public void OnCancel()
    {
        status = "infoPoint";
        SetCancelButtonVisible(false);
        ShowInfoPoint(true);
        ShowTextField(false);
    }

    public void OnDisplay()
    {
        status = "textField";
        SetCancelButtonVisible(true);
        ShowInfoPoint(false);
        ShowTextField(true);
    }

    private void ShowTextField(bool visible)
    {
        Renderer[] rendererComponents = TextField.GetComponentsInChildren<Renderer>();
        Collider[] colliderComponents = TextField.GetComponentsInChildren<Collider>();

        // Enable rendering:
        foreach (Renderer component in rendererComponents)
        {
            component.enabled = visible;
        }

        // Enable colliders:
        foreach (Collider component in colliderComponents)
        {
            component.enabled = visible;
        }
    }

    private void SetCancelButtonVisible(bool visible)
    {
        if (CancelButton == null) return;

        if (CancelButton.enabled != visible)
        {
            CancelButton.enabled = visible;
            CancelButton.image.enabled = visible;
        }
    }

}
