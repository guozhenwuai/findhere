using AsImpL;
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
    public GameObject VerifyContent;
    public GameObject infoPoint;
    public GameObject textInfo;
    public GameObject imageInfo;
    public GameObject audioInfo;
    public GameObject infoPointPool;
    public RawImage loadingSpinner;
    public InfoPointMoveManager infoPointMoveManager;
    public ObjectImporter objImporter;

    private AndroidJavaObject androidPlugin;
    private bool isLoadingImage;
    private bool isLoadingText;
    private bool isLoadingAudio;
    private GameObject audioFrom;
    private WWW incompatibleContent;
    private WWW audioContent;
    private int pageIndex;
    private bool isLastPage;
    private Content[] contents;
    private int contentIndex;
    private bool isLoadingObject;
    private bool hasContents;
    private bool endLoad;

    // Use this for initialization
    void Start () {
        AndroidJavaClass jc = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
        androidPlugin = jc.GetStatic<AndroidJavaObject>("currentActivity");
        isLoadingImage = false;
        isLoadingText = false;
        isLoadingAudio = false;
        pageIndex = 0;
        isLastPage = false;
        contentIndex = 0;
        isLoadingObject = false;
        hasContents = false;
        endLoad = false;
    }

    void Update()
    {
        if (isLoadingImage)
        {
            LoadImage();
        }
        if (isLoadingAudio)
        {
            LoadAudio();
        }
        if (isLoadingText)
        {
            LoadText();
        }
        SetLoadingSpinner(isLoadingImage || isLoadingText || isLoadingAudio);
    }

    public void ObjectLoadFinish()
    {
        isLoadingObject = false;
        endLoad = true;
    }

    public bool EndObjectLoad()
    {
        return endLoad;
    }

    public void ResetObjectLoad()
    {
        endLoad = false;
    }

    public bool ShowContents()
    {
        return hasContents && !isLoadingObject;
    }

    public bool isLoading()
    {
        return isLoadingImage || isLoadingText ;
    }

    public void stopLoading()
    {
        isLoadingImage = false;
        isLoadingText = false;
        incompatibleContent = null;
    }

    private void LoadText()
    {
        if (incompatibleContent.error != null)
        {
            isLoadingText = false;
        }
        else if (incompatibleContent.progress >= 1)
        {
            string content = incompatibleContent.text;
            TextContent text = JsonUtility.FromJson<TextContent>(content);
            textAdapter.setText(text.GetText());
            isLoadingText = false;
        }
    }

    public void LoadText(string id)
    {
        //string url = "http://115.159.184.211:8080/FindHere/GetComments/ByID?commentID=" + id;
        string url = "http://115.159.184.211:8080/FindHere/GetComments/ByID?commentID=" + id;
        incompatibleContent = new WWW(url);
        isLoadingText = true;
        //string content = www.text;
        //string content = androidPlugin.Call<string>("getCommentContent",id);
        //Debug.Log("string: " + content);
        //TextContent text = JsonUtility.FromJson<TextContent>(content);
        //textAdapter.setText(text.GetText());
        //textAdapter.setText(content);
    }

    private void LoadImage()
    {
        if (incompatibleContent.error != null)
        {
            isLoadingImage = false;
        }
        else if (incompatibleContent.progress >= 1)
        {
            int width = incompatibleContent.texture.width;
            int height = incompatibleContent.texture.height;
            Texture2D texture = new Texture2D(width, height);
            incompatibleContent.LoadImageIntoTexture(texture);
            imageAdapter.setTexture(texture);
            isLoadingImage = false;
        }
    }

    public void LoadImage(string id)
    {
        string url = "http://115.159.184.211:8080/FindHere/GetComments/ByID?commentID=" + id;
        incompatibleContent = new WWW(url);
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
            AudioClip music = audioContent.GetAudioClip(false, true, AudioType.WAV);
            audioAdapter.SetAudioSource(music, audioFrom);
            isLoadingAudio = false;
        }
    }

    public void LoadAudio(GameObject obj)
    {
        audioContent = new WWW("http://115.159.184.211:8080/FindHere/GetComments/ByID?commentID=" + obj.name);
        audioFrom = obj;
        isLoadingAudio = true;
    }

    public void SetTargetId(string id)
    {
        androidPlugin.Call("setTargetID", id);
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

    public void NextPost(string targetId)
    {
        string str;
        if (isLastPage)
        {
            androidPlugin.Call("setToast", "没有更多信息了~");
            str = GetCommentsFromAndroid(targetId);
            ParseInfoPoint(str);
            return;
        }
        infoPointMoveManager.ZoomingIn();
        pageIndex++;
        str = GetCommentsFromAndroid(targetId);
        if (str == "" || str == "[]")
        {
            androidPlugin.Call("setToast", "没有更多信息了~");
            pageIndex--;
            ResetInfoPool();
            str = GetCommentsFromAndroid(targetId);
            ParseInfoPoint(str);
            infoPointMoveManager.ZoomingOut();
        }
        else
        {
            ResetInfoPool();
            ParseInfoPoint(str);
            infoPointMoveManager.ZoomingOut();
        }
    }

    public void LastPost(string targetId)
    {
        infoPointMoveManager.ZoomingIn();
        if (pageIndex == 0)
        {
            androidPlugin.Call("setToast", "已经是第一页啦！");
        }
        else
        {
            pageIndex--;
        }
        LoadInfoPoint(targetId);
    }

    public void ResetPageIndex()
    {
        pageIndex = 0;
    }

    public void LoadInfo(string targetId)
    {
        LoadContents(targetId);
        FirstLoadInfoPoint(targetId);
        if (contents != null && contents.Length > 0)
        {
            LoadOneContent();
        }
    }

    private void NextContent()
    {
        if(hasContents && contentIndex + 1 < contents.Length)
        {
            contentIndex++;
            LoadOneContent();
        }
        else
        {
            androidPlugin.Call("setToast", "没有更多信息了~");
        }
    }

    private void LastContent()
    {
        if(hasContents&&contentIndex > 0&&contentIndex - 1 < contents.Length)
        {
            contentIndex--;
            LoadOneContent();
        }
        else
        {
            androidPlugin.Call("setToast", "没有更多信息了~");
        }
    }

    private void LoadOneContent()
    {
        Content content = contents[contentIndex];
        string type = content.GetContentType();
        switch (type)
        {
            case "ARObject":
                isLoadingObject = true;
                WWW www = new WWW("http://115.159.184.211:8080/FindHere/GetContent/Options?ARManagerID=" + content.GetContentID());
                while (!www.isDone) ;
                string str = www.text;
                ObjectTransform objTransform = JsonUtility.FromJson<ObjectTransform>(str);
                ImportOptions options = new ImportOptions();
                options = new ImportOptions();
                options.localScale = objTransform.GetObjectScale();
                options.localEulerAngles = objTransform.GetObjectRotation();
                options.localPosition = objTransform.GetObjectPosition();
                objImporter.ImportModelAsync(content.GetContentID(), content.GetContentID(), VerifyContent.transform, options);
                break;
            default:
                Debug.Log("cannot load content type:" + type);
                break;
        }
    }

    private void FirstLoadInfoPoint(string targetId)
    {
        Debug.Log("load info point " + targetId);
        ResetInfoPool();

        string str = GetCommentsFromAndroid(targetId);
        //string str = GetTestComments(targetId);

        if (str == ""||str=="[]")
        {
            isLastPage = true;
            androidPlugin.Call("setToast", "此处还没有信息哦~快来发布吧！");
        }
        else
        {
            isLastPage = false;
            //androidPlugin.Call("setToast", "识别成功！");
            ParseInfoPoint(str);
            infoPointMoveManager.ZoomingOut();
        }
    }

    public void LoadInfoPoint(string targetId)
    {
        Debug.Log("load info point "+targetId);
        ResetInfoPool();

        string str = GetCommentsFromAndroid(targetId);

        if (str != "" && str != "[]")
        {
            isLastPage = false;
            ParseInfoPoint(str);
            infoPointMoveManager.ZoomingOut();
        }
        else
        {
            isLastPage = true;
        }
    }

    public void LoadContents(string targetId)
    {
        contents = null;
        contentIndex = 0;
        string str = GetContents(targetId);

        if (str != "" && str !="[]")
        {
            hasContents = true;
            Debug.Log("contents:" + str);
            contents = JsonHelper.FromJson<Content>(fixJson(str));
        }
    }

    private string GetContents(string targetId)
    {
        WWW www = new WWW("http://115.159.184.211:8080/FindHere/GetContent/ByTargetID?targetID=" + targetId);
        while (!www.isDone) ;
        if (www.error != null)
        {
            return "";
        }
        string str = www.text;
        return str;
    }

    private string GetTestComments(string targetId)
    {
        WWW www = new WWW("http://115.159.184.211:8080/FindHere/GetComments/GetIDsByTargetID?targetID="+targetId+"&pageNum=20&pageIndex=0");
        while (!www.isDone) ;
        //记录取得的comments中各类型的id
        if (www.error != null)
        {
            return "";
        }
        string str = www.text;
        return str;
    }

    private string GetCommentsFromAndroid(string targetId)
    {
        string str = androidPlugin.Call<string>("getCommentType", targetId, pageIndex);
        if (str == "[]" || str == "")
        {
            return "";
        }
        else
        {
            return str;
        }
    }

    private void ParseInfoPoint(string str)
    {
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
                Debug.Log("comment type: " + type);
            }
        }

        int textSize = GameObject.FindGameObjectsWithTag("TextInfo").Length;
        //如果已有TextInfo数目不够，重新实例化
        if (textSize < textId.Count) InitializeInfoPoint(textInfo, textId.Count - textSize);
        int imageSize = GameObject.FindGameObjectsWithTag("ImageInfo").Length;
        if (imageSize < imageId.Count) InitializeInfoPoint(imageInfo, imageId.Count - imageSize);
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
            loadingSpinner.rectTransform.Rotate(Vector3.forward, 240.0f * Time.deltaTime);
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
            point.transform.parent.localPosition = new Vector3(0, 0, 0);
            point.transform.name = comments[count];
            count++;
            if (count >= size) break;
        }
    }

    private void InitializeInfoPoint(GameObject prototype,int num)
    {
        for(int i = 0; i < num; i++)
        {
            GameObject newPoint = Instantiate(prototype);
            newPoint.tag = "InfoPoint";
            Transform newSphere = newPoint.transform.GetChild(0);
            newSphere.tag = prototype.name;
        }
    }

    private string fixJson(string value)
    {
        value = "{\"Items\":" + value + "}";
        Debug.Log(value);
        return value;
    }

}
