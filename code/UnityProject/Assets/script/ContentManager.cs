using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using Vuforia;

public class ContentManager : MonoBehaviour,ITrackableEventHandler {
    //0:scanning 1:infopoint 2:textfield 3:imagefield 4:model
    private int status;
    private bool isTrackable;
    private string keepTargetId;
    private string targetId;
    private bool isAudioShow;
    private Transform pickedObject = null;
    private bool showModelStatus;

    //左右滑动事件两指的坐标
    private Vector2 position;
    //双指触控时，两指的坐标。用于实现图片的两指放大
    private Vector2 oldPosition1;
    private Vector2 oldPosition2;

    // 移动加权，使移动与手指移动同步
    private float xSpeed = 0.01f;
    private float ySpeed = 0.01f;
    private float zSpeed = 0.01f;

    public GameObject VerifyContent;
    public GameObject infoPoint;
    public GameObject TextField;
    public GameObject ImageField;
    public GameObject AudioField;

    public InfoLoader infoLoader;
    
    public AnimationsManager AnimationsManager;

    // Use this for initialization
    void Start () {

        TrackableBehaviour trackableBehaviour = infoPoint.transform.parent.GetComponent<TrackableBehaviour>();
        if (trackableBehaviour)
        {
            trackableBehaviour.RegisterTrackableEventHandler(this);
        }

        //SetCancelButtonVisible(false);
        status = 0;
        targetId = "";
        isTrackable = false;
        isAudioShow = false;
        showModelStatus = false;
    }

    void Update()
    {
        //模型显示
        if (showModelStatus && isTrackable && infoLoader.EndObjectLoad())
        {
            ShowField(VerifyContent,true);
            infoLoader.ResetObjectLoad();
        }
        //声音停止后喇叭消失
        if (AudioField.GetComponent<AudioSource>()!=null&&!AudioField.GetComponent<AudioSource>().isPlaying&&isAudioShow)
        {
            ShowField(AudioField,false);
        }
        //点击事件处理
        if (Input.GetMouseButtonUp(0))
        {
            if (status==0)
            {
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
                    Debug.Log("touch info point"+hitObject.name);
                    OnDisplay(hitObject);
                }
                else if (hitObject.name == "TextBackground")
                {
                    Debug.Log("touch text field");
                    AnimationsManager.PlayAnimationTo2D(TextField);
                    TrackerManager.Instance.GetTracker<ObjectTracker>().Stop();
                }
                else if(hitObject.name == "ImageContent")
                {
                    Debug.Log("touch image field");
                    AnimationsManager.PlayAnimationTo2D(ImageField);
                    TrackerManager.Instance.GetTracker<ObjectTracker>().Stop();
                }
                else if(hitObject.name == "AudioField")
                {
                    Debug.Log("touch audio field");
                    AudioPlaying(false);
                }
                else
                {
                    Debug.Log(hitObject.name);
                }
            }
            else
            {
                OnCancel();
                TrackerManager.Instance.GetTracker<ObjectTracker>().Start();
            }
        }
        if(status==1||(status==3&&AnimationsManager.IsShowingOverlay()))DragEvent();       //拖拽事件
        if(status==3&&AnimationsManager.IsShowingOverlay())ImageZoomEvent();      //图片的放大缩小事件
    }

    private void ImageZoomEvent()
    {
        if (Input.touchCount > 1)
        {
            if (Input.GetTouch(0).phase == TouchPhase.Moved || Input.GetTouch(1).phase == TouchPhase.Moved)
            {
                Vector2 tempPosition1 = Input.GetTouch(0).position;
                Vector2 tempPosition2 = Input.GetTouch(1).position;

                float distance = AnimationsManager.GetDistance();
                if (IsEnlarge(oldPosition1, oldPosition2, tempPosition1, tempPosition2))
                {
                    if (distance < 10)
                    {
                        distance += 0.1f;
                        Vector3 body = ImageField.transform.localScale;
                        ImageField.transform.localScale += new Vector3(body.x * Time.deltaTime,0, body.z * Time.deltaTime);
                    }
                }
                else
                {
                    if (distance > 1)
                    {
                        distance -= 0.1f;
                        Vector3 body = ImageField.transform.localScale;
                        ImageField.transform.localScale += new Vector3(-body.x * Time.deltaTime,0, -body.z * Time.deltaTime);
                    }
                }
                AnimationsManager.SetDistance(distance);

                oldPosition1 = tempPosition1;
                oldPosition2 = tempPosition2;
            }
        }
    }

    private bool IsEnlarge(Vector2 oP1, Vector2 oP2, Vector2 nP1, Vector2 nP2)
    {
        //old distance  
        float oldDistance = Mathf.Sqrt((oP1.x - oP2.x) * (oP1.x - oP2.x) + (oP1.y - oP2.y) * (oP1.y - oP2.y));
        //new distance  
        float newDistance = Mathf.Sqrt((nP1.x - nP2.x) * (nP1.x - nP2.x) + (nP1.y - nP2.y) * (nP1.y - nP2.y));

        if (oldDistance < newDistance)
        {
            //zoom+  
            return true;
        }
        else
        {
            //zoom-  
            return false;
        }
    }

    private void DragEvent()
    {
        foreach (Touch touch in Input.touches)
        {
            //获取摄像头近平面到屏幕触摸点的射线
            Ray ray = Camera.main.ScreenPointToRay(touch.position);
            //获取沿着射线在距离dist位置的点
            //按下手指触碰屏幕
            if (touch.phase == TouchPhase.Began)
            {
                RaycastHit hit = new RaycastHit();
                // 判断是否有碰撞到对象
                if (Physics.Raycast(ray, out hit, 1000))
                {
                    pickedObject = hit.transform;
                }
                else
                {
                    position = touch.position;
                    pickedObject = null;
                }

            }//选中模型后拖拽
            else if (touch.phase == TouchPhase.Moved)
            {
                if (pickedObject != null)
                {
                    // 设置移动位移
                    float x = -Input.GetAxis("Mouse X") * xSpeed;
                    float z = -Input.GetAxis("Mouse Y") * ySpeed;
                    float y = Input.GetAxis("Mouse Z") * zSpeed;

                    pickedObject.transform.parent.position += new Vector3(x, y, z);
                }
                else if (status == 1&&targetId!="")
                {
                    //左右滑动
                    Vector2 newPos = touch.position;
                    int result = DragDirection(position, newPos);
                    if (result == 1)
                    {
                        infoLoader.LastPost(targetId);
                    }
                    else if(result == -1)
                    {
                        infoLoader.NextPost(targetId);
                    }
                }
                //释放
            }
            else if (touch.phase == TouchPhase.Ended)
            {
                pickedObject = null;
            }
        }
    }

    private int DragDirection(Vector2 oldPos,Vector2 newPos)
    {
        float distance = 50;
        float x = newPos.x - oldPos.x;
        float y = newPos.y - oldPos.y;
        //右滑
        if (x > distance && y < distance && y > -distance)
        {
            return 1;
        }
        else if (x < -distance && y < distance && y > -distance)
        {
            return -1;
        }
        else
        {
            return 0;
        }
    }

    public void Show(bool tf)
    {
        if (showModelStatus)
        {
            if (infoLoader.ShowContents())
            {
                ShowField(VerifyContent, tf);
            }
            return;
        }
        if (status == 1)
        {
            ShowField(infoPoint,tf);
        }
        else if (status== 2)
        {
            if (tf)
            {
                AnimationsManager.PlayAnimationTo3D(TextField);
            }
            else
            {
                AnimationsManager.PlayAnimationTo2D(TextField);
            }
        }
        else if (status == 3)
        {
            if (tf)
            {
                AnimationsManager.PlayAnimationTo3D(ImageField);
            }
            else
            {
                AnimationsManager.PlayAnimationTo2D(ImageField);
            }
        }
    }

    public void AudioPlaying(bool tf)
    {
        if (status == 1)
        {
            ShowField(AudioField, tf);
        }
        if (tf)
        {
            AudioField.GetComponent<AudioSource>().Play();
        }
        else
        {
            AudioField.GetComponent<AudioSource>().Stop();
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
            targetId = keepTargetId;
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
        Debug.Log("targetId: " + targetId);
        infoLoader.SetTargetId(targetId);
    }

    public void TargetCreated(string target)
    {
        keepTargetId = target;
        status = 1;
        infoLoader.ResetPageIndex();
        infoLoader.LoadInfo(target,showModelStatus);

        /*for(int i = 0; i < 10; i++)
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
        */
    }

    public void ResetInfo()
    {
        if (!showModelStatus && keepTargetId != "")
        {
            Show(false);
            infoLoader.ResetInfo(keepTargetId);
        }
    }

    public void ResetKeepTargetId()
    {
        keepTargetId = "";
    }

    public void LoadInfo(string targetId)
    {
        if (!showModelStatus)
        {
            infoLoader.LoadInfoPoint(targetId);
        }
    }

    public void SwitchModelStatus()
    {
        if (showModelStatus)
        {
            Show(false);
            showModelStatus = false;
            if (status != 0)
            {
                status = 1;
                if (keepTargetId != "")
                {
                    infoLoader.FirstLoadInfoPoint(keepTargetId);
                }
            }
        }
        else
        {
            if (infoLoader.isLoading())
            {
                infoLoader.stopLoading();
            }
            Show(false);
            showModelStatus = true;
            if (status != 0)
            {
                if (!infoLoader.EmptyContent())
                {
                    infoLoader.ContentMessage(true);
                    infoLoader.LoadOneContent();
                }
                else
                {
                    infoLoader.ContentMessage(false);
                }
                status = 4;
            }
        }
    }

    public void RemoveAll()
    {
        if (infoLoader.isLoading())
        {
            infoLoader.stopLoading();
        }
        if (isAudioShow)
        {
            ShowField(AudioField, false);
        }
        if (status == 1)
        {
            ShowField(infoPoint, false);
        }
        else if (status == 2)
        {
            ShowField(TextField, false);
            AnimationsManager.PlayAnimationTo3D(TextField);

        }
        else if (status == 3)
        {
            ShowField(ImageField, false);
            AnimationsManager.PlayAnimationTo3D(ImageField);
        }
        status = 0;
    }

    public void OnCancel()
    {
        //SetCancelButtonVisible(false);
        if (infoLoader.isLoading())
        {
            infoLoader.stopLoading();
        }
        if (isTrackable)
        {
            ShowField(infoPoint,true);
        }
        if (status == 2)
        {
            ShowField(TextField, false);
            AnimationsManager.PlayAnimationTo3D(TextField);

        } 
        else if (status == 3)
        {
            ShowField(ImageField,false);
            AnimationsManager.PlayAnimationTo3D(ImageField);
        }
        status = 1;
    }

    public int GetStatus()
    {
        return status;
    }

    public void SetStatus(int newValue)
    {
        status = newValue;
    }

    public void OnDisplay(GameObject obj)
    {
        string tag = obj.tag;
        if (tag == "TextInfo")
        {
            ShowField(infoPoint, false);
            infoLoader.LoadText(obj.name);
            status = 2;
        }
        else if (tag == "ImageInfo")
        {
            ShowField(infoPoint, false);
            infoLoader.LoadImage(obj.name);
            status = 3;
        }
        else if (tag == "AudioInfo")
        {
            if (!AudioField.GetComponent<AudioSource>().isPlaying)
            {
                infoLoader.LoadAudio(obj);
            }
            else if (AudioField.transform.parent == obj.transform.parent)
            {
                AudioPlaying(false);
            }
            else
            {
                infoLoader.LoadAudio(obj);
            }
            status = 1;
        }

        //SetCancelButtonVisible(true);
        
        
    }

    public void ShowField(GameObject Field, bool visible)
    {
        if (Field == AudioField)
        {
            isAudioShow = visible;
            if (!visible)
            {
                AudioField.transform.parent = TextField.transform.parent;
            }
        }
        Renderer[] rendererComponents = Field.GetComponentsInChildren<Renderer>();
        Collider[] colliderComponents = Field.GetComponentsInChildren<Collider>();

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
