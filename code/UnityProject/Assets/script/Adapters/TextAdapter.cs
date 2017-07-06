using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class TextAdapter : MonoBehaviour {
    public GameObject textField;

    private TextMesh text;

	// Use this for initialization
	void Start () {
        text = textField.GetComponentInChildren<TextMesh>();
    }
	
	// Update is called once per frame
	void Update () {
		
	}

    //对文本内容做换行处理，对边框做适应处理
    public void setText(string content)
    {
        int feet = 24;
        int size = content.Length;
        Debug.Log(size);
        int count = 0;
        int lines = 0;
        string newContent = "";
        foreach(char c in content)
        {
            if (count == 0)
            {
                lines++;
            }
            newContent += c;
            if ((int)c > 127)   //是中文
            {
                count += 2;
            }
            else    //不是中文
            {
                count++;
            }
            if (count >= feet)
            {
                newContent += "\n";
                count = 0;
            }
        }
        Debug.Log(newContent);
        text.text = newContent;
        text.transform.parent = null;
        textField.transform.localScale = new Vector3(
            textField.transform.localScale.x,
            textField.transform.localScale.y,
            0.01f * lines);
        text.transform.parent = textField.transform;
        text.transform.localPosition = new Vector3(-4.33f, 0, 4.12f);
    }
}
