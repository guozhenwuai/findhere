using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class ImageAdapter : MonoBehaviour {
    public GameObject imageField;
    public ContentManager contentManager;

	// Use this for initialization
	void Start () {
		
	}
	
	public void setBytesToImage(byte[] bytes,int width,int height)
    {
        Texture2D texture = new Texture2D(width, height);
        texture.LoadImage(bytes);
        imageField.GetComponent<Renderer>().material.mainTexture = texture;
        Vector3 scale = imageField.transform.localScale;
        imageField.transform.localScale = new Vector3(
            scale.z, scale.y, scale.z * height/width);
    }

    public void setTexture(Texture2D texture)
    {
        float width = texture.width;
        float height = texture.height;
        imageField.GetComponent<Renderer>().material.mainTexture = texture;
        Vector3 scale = imageField.transform.parent.localScale;
        float h = scale.x * height / width;
        float w = scale.x;
        if (h >= 0.15f)
        {
            h = 0.15f;
            w = h * width / height;
        }
        imageField.transform.parent.localScale = new Vector3(w, scale.y,h);
        contentManager.ShowField(imageField.transform.parent.gameObject, true);
    }
}
