using System.Collections;
using System.Collections.Generic;
using System.Drawing;
using System.Drawing.Imaging;
using System.IO;
using UnityEngine;

public class InfoLoader : MonoBehaviour {
    public TextAdapter textAdapter;
    public ImageAdapter imageAdapter;
    public GameObject infoPoint;
    public GameObject textInfo;
    public GameObject imageInfo;

    private AndroidJavaObject androidPlugin;

    // Use this for initialization
    void Start () {
        androidPlugin = new AndroidJavaObject("com.FindHere.control.TargetControl");
    }

    public void LoadText(string id)
    {
        textAdapter.setText(id);
    }

    public void LoadImage(string id)
    {
        Image pic = Image.FromFile("D:\\terun.jpg");
        Debug.Log("width:" + pic.Width + " height:" + pic.Height);
        ImageFormat format = pic.RawFormat;
        MemoryStream ms = new MemoryStream();
        if (format.Equals(ImageFormat.Jpeg))
        {
            pic.Save(ms, ImageFormat.Jpeg);
        }
        imageAdapter.setBytesToImage(ms.ToArray(), pic.Width, pic.Height);
    }

    public void LoadInfoPoint(string targetId)
    {
        Debug.Log("load info point "+targetId);
        string a = androidPlugin.Call<string>("sayHello");
        Debug.Log(a);
        string str = androidPlugin.CallStatic<string>("getCommentType",targetId,0);
        Debug.Log("string:" + str);
        /*WWW www = new WWW("http://115.159.184.211:8080/FindHere/GetComments/GetIDsByTargetID?targetID=1&pageNum=20&pageIndex=0");
        //yield return www;
        while (!www.isDone);
        //记录取得的comments中各类型的id
        string str = www.text;
        */

        List<string> textId = new List<string>();
        List<string> imageId = new List<string>();
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
            else
            {
                Debug.Log("comment type: "+type);
            }
        }
        int textSize = GameObject.FindGameObjectsWithTag("TextInfo").Length;
        //如果已有TextInfo数目不够，重新实例化
        if(textSize<textId.Count)InitializeInfoPoint(textInfo, textId.Count - textSize);
        int imageSize = GameObject.FindGameObjectsWithTag("ImageInfo").Length;
        if (imageSize<imageId.Count) InitializeInfoPoint(imageInfo, imageId.Count - imageSize);

        GenerateInfoPoints("TextInfo", textId);
        GenerateInfoPoints("ImageInfo", imageId);
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
                Random.Range(0, 0.8f),
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
