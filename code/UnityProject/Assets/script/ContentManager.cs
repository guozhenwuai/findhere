using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using Vuforia;

public class ContentManager : MonoBehaviour,ITrackableEventHandler {
    private GameObject infoPoint;
    private CloudRecoBehaviour mCloudRecoBehaviour;
    private string status;
    private ObjectTracker mObjectTracker;
    private bool isTrackable;
    private string targetId;

    public GameObject textInfo;
    public GameObject imageInfo;
    public GameObject TextField;
    public GameObject ImageField;

    public InfoLoader infoLoader;
    
    //public Button CancelButton;
    public AnimationsManager AnimationsManager;

    // Use this for initialization
    void Start () {
        infoPoint = textInfo.transform.parent.gameObject;

        TrackableBehaviour trackableBehaviour = infoPoint.transform.parent.GetComponent<TrackableBehaviour>();
        if (trackableBehaviour)
        {
            trackableBehaviour.RegisterTrackableEventHandler(this);
        }
        mCloudRecoBehaviour = FindObjectOfType<CloudRecoBehaviour>();
        mObjectTracker = TrackerManager.Instance.GetTracker<ObjectTracker>();

        //SetCancelButtonVisible(false);
        status = "scanning";
        targetId = "";
        isTrackable = false;
    }

    void Update()
    {
        //信息点的移动效果
        if (status == "infoPoint")
        {
            float v = 0.1f*Time.deltaTime;
            foreach(Transform point in infoPoint.transform)
            {
                Vector3 pos = point.localPosition;
                float x, y, z;
                //确定移动坐标
                if (pos.x <= -1f)
                {
                    x = Random.Range(0, v);
                }
                else if (pos.x >= 1f)
                {
                    x = Random.Range(-v, 0);
                }
                else
                {
                    x = Random.Range(-v, v);
                }
                if(pos.y <= 0)
                {
                    y = Random.Range(0, v);
                }
                else if (pos.y >= 0.8f)
                {
                    y = Random.Range(-v, 0);
                }
                else
                {
                    y = Random.Range(-v, v);
                }
                if(pos.z <= -1f)
                {
                    z = Random.Range(0, v);
                }
                else if (pos.z >= 1f)
                {
                    z = Random.Range(-v, 0);
                }
                else
                {
                    z = Random.Range(-v, v);
                }
                point.localPosition = new Vector3(pos.x + x, pos.y + y, pos.z + z);
            }
        }
        if (Input.GetMouseButtonUp(0))
        {
            if (status=="scanning")
            {
                Debug.Log("enableCloudReco");
                return;
            }
            RaycastHit hit;
            Ray ray = Camera.main.ScreenPointToRay(Input.mousePosition);
            if (Physics.Raycast(ray, out hit, 1000.0f))
            {
                GameObject hitObject = hit.collider.gameObject;
                if (hitObject == null)
                {
                    return;
                }
                if (hitObject.transform.parent.tag == "InfoPoint")
                {
                    Debug.Log("touch info point");
                    OnDisplay(hitObject);
                }
                else if (hitObject.name == "TextField")
                {
                    Debug.Log("touch text field");
                    AnimationsManager.PlayAnimationTo2D(TextField);
                    mObjectTracker.Stop();
                }
                else if(hitObject.name == "ImageField")
                {
                    Debug.Log("touch image field");
                    AnimationsManager.PlayAnimationTo2D(ImageField);
                    mObjectTracker.Stop();
                }
            }
            else
            {
                OnCancel();
                mObjectTracker.Start();
            }
        }
    }

    public string GetTargetId()
    {
        return targetId;
    }

    public void Show(bool tf)
    {
        if (status == "infoPoint")
        {
            ShowInfoPoint(tf);
        }
        else if (status== "textField")
        {
            if (tf)
            {
                AnimationsManager.PlayAnimationTo3D(TextField);
            }
            else
            {
                AnimationsManager.PlayAnimationTo2D(TextField);
            }
            ShowTextField(tf);
        }
        else if (status == "imageField")
        {
            if (tf)
            {
                AnimationsManager.PlayAnimationTo3D(ImageField);
            }
            else
            {
                AnimationsManager.PlayAnimationTo2D(ImageField);
            }
            ShowImageField(tf);
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
            isTrackable = true;
            Show(true);
            Debug.Log("content manager show: true");
        }
        else
        {
            isTrackable = false;
            targetId = "";
            Show(false);
            Debug.Log("content manager show: false");
        }
    }

    public void TargetCreated(string target)
    {
        targetId = target;
        status = "infoPoint";
        Vector3 parentPosition = infoPoint.transform.position;

        for(int i = 0; i < 10; i++)
        {
            GameObject sphere = Instantiate(textInfo);
            sphere.transform.parent = infoPoint.transform;
            sphere.transform.position = new Vector3(
                parentPosition.x + Random.Range(-1f, 1f), 
                parentPosition.y + Random.Range(0, 0.8f), 
                parentPosition.z + Random.Range(-1f, 1f));
            sphere.transform.name = target + i;
        }
        textInfo.SetActive(false);

        for (int i = 0; i < 5; i++)
        {
            GameObject sphere = Instantiate(imageInfo);
            sphere.transform.parent = infoPoint.transform;
            sphere.transform.localPosition = new Vector3(
                Random.Range(-1f, 1f),
                Random.Range(0, 0.8f),
                Random.Range(-1f, 1f));
            sphere.transform.name = target + i;
        }
        imageInfo.SetActive(false);
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
        //SetCancelButtonVisible(false);
        if (isTrackable)
        {
            ShowInfoPoint(true);
        }
        if (status == "textField") ShowTextField(false);
        else if (status == "imageField") ShowImageField(false);
        status = "infoPoint";
    }

    public void OnDisplay(GameObject obj)
    {
        if (obj.tag == "TextInfo")
        {
            infoLoader.LoadText(obj.transform.parent.name);
            ShowTextField(true);
            status = "textField";
        }
        else if (obj.tag== "ImageInfo")
        {
            infoLoader.LoadImage(obj.transform.parent.name);
            ShowImageField(true);
            status = "imageField";
        }

        //SetCancelButtonVisible(true);
        ShowInfoPoint(false);
        
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

    private void ShowImageField(bool visible)
    {
        Renderer[] rendererComponents = ImageField.GetComponentsInChildren<Renderer>();
        Collider[] colliderComponents = ImageField.GetComponentsInChildren<Collider>();

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

    /*
    private void SetCancelButtonVisible(bool visible)
    {
        if (CancelButton == null) return;

        if (CancelButton.enabled != visible)
        {
            CancelButton.enabled = visible;
            CancelButton.image.enabled = visible;
        }
    }
    */

}
