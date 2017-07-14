using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class TextAdapter : MonoBehaviour {
    public GameObject textField;
    public ContentManager contentManager;
    public EmojiHelper emojiHelper;

    private TextMesh text;
    private static char emSpace = '\u2001';

    // Use this for initialization
    void Start () {
        text = textField.GetComponentInChildren<TextMesh>();
    }
	
	// Update is called once per frame
	void Update () {
		
	}

    //对文本内容做换行处理，对边框做适应处理，处理emoji
    public void setText(string inputString)
    {
        CleanEmoji();
        inputString = "bicyclist:\U0001F6B4哈, and US flag:\U0001F1FA\U0001F1F8⚽➕❤ = I love football \U0001F3B5 \U0001F3B6 \U0001F3B7 \U0001F3B8 \U0001F3B9 \U0001F3BA";
        int feet = 60;
        int count = 0;
        int lines = 0;
        int i = 0;
        int length = inputString.Length;
        string newContent = "";
        //emojiHelper.SetUITextThatHasEmoji(text, content);
        while (i < length)
        {
            if (count == 0)
            {
                lines++;
            }
            string singleChar = inputString.Substring(i, 1);
            string doubleChar = "";
            string fourChar = "";

            if (i < (length - 1))
            {
                doubleChar = inputString.Substring(i, 2);
            }

            if (i < (length - 3))
            {
                fourChar = inputString.Substring(i, 4);
            }

            if (emojiHelper.IsEmoji(fourChar))
            {
                // Check 64 bit emojis first
                newContent += "  ";
                emojiHelper.AppendEmojiReplacements(count, lines, fourChar);
                count += 6;
                i += 4;
            }
            else if (emojiHelper.IsEmoji(doubleChar))
            {
                // Then check 32 bit emojis
                newContent += "  ";
                emojiHelper.AppendEmojiReplacements(count, lines, doubleChar);
                count += 6;
                i += 2;
            }
            else if (emojiHelper.IsEmoji(singleChar))
            {
                // Finally check 16 bit emojis
                newContent += "  ";
                emojiHelper.AppendEmojiReplacements(count, lines, singleChar);
                count += 6;
                i++;
            }
            else
            {
                char c = inputString[i];
                newContent += c;
                if ((int)c > 127)  //是中文
                {
                    count += 5;
                }
                else   //不是中文
                {
                    count += 3;
                }
                i++;
            }
            if (feet - count < 3)
            {
                newContent += "\n";
                count = 0;
            }
        }

        Debug.Log(newContent);
        text.text = newContent;
        //text.text = content;
        text.transform.parent = null;
        float zScale = 0.008f * lines;
        textField.transform.localScale = new Vector3(
            textField.transform.localScale.x,
            textField.transform.localScale.y,
            zScale);
        text.transform.parent = textField.transform;
        text.transform.localPosition = new Vector3(-4.33f, 0, (0.02f+(lines-1)*0.036f)/zScale);
        emojiHelper.LoadEmoji(text.gameObject);
        contentManager.ShowField(textField, true);
    }

    private void CleanEmoji()
    {
        Transform textFieldTransform = textField.transform;
        foreach(Transform childTransform in textFieldTransform)
        {
            if (childTransform.tag == "Emoji")
            {
                Destroy(childTransform.gameObject);
            }
        }
    }
}
