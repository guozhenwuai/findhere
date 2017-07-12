using System.Collections;
using System.Collections.Generic;
using System.Drawing;
using System.Drawing.Imaging;
using System.IO;
using UnityEngine;
using UnityEngine.UI;

public class InfoLoader : MonoBehaviour {
    public TextAdapter textAdapter;
    public ImageAdapter imageAdapter;
    public AudioAdapter audioAdapter;
    public GameObject infoPoint;
    public GameObject textInfo;
    public GameObject imageInfo;
    public GameObject audioInfo;
    public GameObject infoPointPool;
    public RawImage loadingSpinner;

    private AndroidJavaObject androidPlugin;
    private bool isLoadingImage;
    private bool isLoadingAudio;
    private GameObject audioFrom;
    private WWW imageContent;
    private WWW audioContent;

    // Use this for initialization
    void Start () {
        AndroidJavaClass jc = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
        androidPlugin = jc.GetStatic<AndroidJavaObject>("currentActivity");
        isLoadingImage = false;
        isLoadingAudio = false;
    }

    void Update()
    {
        SetLoadingSpinner(isLoadingImage||isLoadingAudio);
        if (isLoadingImage)
        {
            LoadImage();
        }
        if (isLoadingAudio)
        {
            LoadAudio();
        }
    }

    public bool isLoading()
    {
        return isLoadingImage;
    }

    public void stopLoading()
    {
        isLoadingImage = false;
        imageContent = null;
    }

    public void LoadText(string id)
    {
        string content = id;
        //string content = androidPlugin.Call<string>("getCommentContent",id);
        Debug.Log("string: " + content);
        //TextContent text = JsonUtility.FromJson<TextContent>(content);
        //textAdapter.setText(text.GetText());
        textAdapter.setText(content);
    }

    private void LoadImage()
    {
        if (imageContent.error != null)
        {
            isLoadingImage = false;
        }
        else if (imageContent.progress >= 1)
        {
            Texture2D texture = imageContent.texture;
            imageAdapter.setTexture(texture);
            isLoadingImage = false;
        }
    }

    public void LoadImage(string id)
    {
        string url = "http://192.168.1.8:8080/FindHere/GetComments/ByID?commentID=" + id;
        imageContent = new WWW(url);
        isLoadingImage = true;   
    }

    private void LoadAudio()
    {
        if (audioContent.error != null)
        {
            isLoadingAudio = false;
        }
        else if(audioContent.progress >= 1)
        {
            AudioClip music = audioContent.GetAudioClip(false, true, AudioType.MPEG);
            audioAdapter.SetAudioSource(music, audioFrom);
            isLoadingAudio = false;
        }
    }

    public void LoadAudio(GameObject obj)
    {
        audioContent = new WWW("http://192.168.1.8:8080/FindHere/GetComments/ByID?commentID=596468ea37fe3e08765850c5");
        audioFrom = obj;
        isLoadingAudio = true;
    }

    public void SetTargetId(string id)
    {
        //androidPlugin.Call("setTargetID", id);
    }

    private void ResetInfoPool()
    {
        GameObject[] points = GameObject.FindGameObjectsWithTag("InfoPoint");
        foreach (GameObject point in points)
        {
            point.transform.parent = infoPointPool.transform;
            point.transform.localPosition = new Vector3(0, 0, 0);
        }
    }

    public void LoadInfoPoint(string targetId)
    {
        Debug.Log("load info point "+targetId);
        ResetInfoPool();
        /*string str = androidPlugin.Call<string>("getCommentType",targetId,0);
        if (str == "[]"||str=="") {
            androidPlugin.Call("setToast","此处还没有信息哦~快来发布吧！");
            return;
        }
        else {
            androidPlugin.Call("setToast", "识别成功！");
        }*/
        WWW www = new WWW("http://115.159.184.211:8080/FindHere/GetComments/GetIDsByTargetID?targetID=1&pageNum=20&pageIndex=0");
        while (!www.isDone);
        //记录取得的comments中各类型的id
        string str = www.text;
        

        List<string> textId = new List<string>();
        List<string> imageId = new List<string>();
        List<string> audioId = new List<string>();
        Comment[] comments = JsonHelper.FromJson<Comment>(fixJson(str));
        foreach (Comment comment in comments)
        {
            string type = comment.GetCommentType();
            if (type == "picture")
            {
                imageId.Add(comment.GetCommentID());
            }
            else if (type == "text")
            {
                textId.Add(comment.GetCommentID());
            }
            else if (type == "sound")
            {
                audioId.Add(comment.GetCommentID());
            }
            else
            {
                Debug.Log("comment type: "+type);
            }
        }
        audioId.Add("1");
        audioId.Add("2");
        audioId.Add("3");
        int textSize = GameObject.FindGameObjectsWithTag("TextInfo").Length;
        //如果已有TextInfo数目不够，重新实例化
        if(textSize<textId.Count)InitializeInfoPoint(textInfo, textId.Count - textSize);
        int imageSize = GameObject.FindGameObjectsWithTag("ImageInfo").Length;
        if (imageSize<imageId.Count) InitializeInfoPoint(imageInfo, imageId.Count - imageSize);
        int audioSize = GameObject.FindGameObjectsWithTag("AudioInfo").Length;
        if (audioSize < audioId.Count) InitializeInfoPoint(audioInfo, audioId.Count - audioSize);

        GenerateInfoPoints("TextInfo", textId);
        GenerateInfoPoints("ImageInfo", imageId);
        GenerateInfoPoints("AudioInfo", audioId);
    }

    private void SetLoadingSpinner(bool visible)
    {
        if (loadingSpinner == null)
        {
            return;
        }
        if (loadingSpinner.enabled != visible)
        {
            loadingSpinner.enabled = visible;
        }
        if (visible)
        {
            loadingSpinner.rectTransform.Rotate(Vector3.forward, 120.0f * Time.deltaTime);
        }
    }

    private void GenerateInfoPoints(string tag,List<string> comments)
    {
        int size = comments.Count;
        if (size == 0) return;
        int count = 0;
        GameObject[] points = GameObject.FindGameObjectsWithTag(tag);
        foreach(GameObject point in points)
        {
            point.transform.parent.parent = infoPoint.transform;
            point.transform.parent.localPosition = new Vector3(
                Random.Range(-1f, 1f),
                Random.Range(0, 0.5f),
                Random.Range(-1f, 1f));
            point.transform.name = comments[count];
            count++;
            if (count >= size) break;
        }
    }

    private void InitializeInfoPoint(GameObject prototype,int num)
    {
        for(int i = 0; i < num; i++)
        {
            GameObject sphere = Instantiate(prototype);
        }
    }

    private string fixJson(string value)
    {
        value = "{\"Items\":" + value + "}";
        Debug.Log(value);
        return value;
    }

}
