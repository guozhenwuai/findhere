using System.Collections;
using System.Collections.Generic;
using System.Drawing;
using System.Drawing.Imaging;
using System.IO;
using UnityEngine;

public class InfoLoader : MonoBehaviour {
    public TextAdapter textAdapter;
    public ImageAdapter imageAdapter;

    // Use this for initialization
    void Start () {
		
	}

    public void LoadText(string id)
    {
        textAdapter.setText(id);
    }

    public void LoadImage(string id)
    {
        Image pic = Image.FromFile("D:\terun.jpg");
        Debug.Log("width:" + pic.Width + " height:" + pic.Height);
        ImageFormat format = pic.RawFormat;
        MemoryStream ms = new MemoryStream();
        if (format.Equals(ImageFormat.Jpeg))
        {
            pic.Save(ms, ImageFormat.Jpeg);
        }
        //byte[] img = 
        //imageAdapter.setBytesToImage()
    }
}
