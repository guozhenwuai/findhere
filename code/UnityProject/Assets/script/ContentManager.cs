using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using Vuforia;

public class ContentManager : MonoBehaviour,ITrackableEventHandler {
    private GameObject targetImage;
    public GameObject AugmentationObject;

    // Use this for initialization
    void Start () {
        targetImage = AugmentationObject.transform.parent.gameObject;

        TrackableBehaviour trackableBehaviour = AugmentationObject.transform.parent.GetComponent<TrackableBehaviour>();
        if (trackableBehaviour)
        {
            trackableBehaviour.RegisterTrackableEventHandler(this);
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
            ShowObject(true);
        }
        else
        {
            ShowObject(false);
        }
    }

    public void TargetCreated(string targetId)
    {
        Vector3 parentPosition = targetImage.transform.position;

        for(int i = 0; i < 10; i++)
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

    public void ShowObject(bool tf)
    {
        Renderer[] rendererComponents = targetImage.GetComponentsInChildren<Renderer>();
        Collider[] colliderComponents = targetImage.GetComponentsInChildren<Collider>();

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
}
