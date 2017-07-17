using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using System;
using System.IO;
using System.Text;
using UnityEngine.UI;

public class EmojiHelper : MonoBehaviour {

    public TextAsset textAsset;
    public GameObject rawImageToClone;

    private Dictionary<string, string> emojiName = new Dictionary<string, string>();
    private List<PosStringTuple> emojiReplacements = new List<PosStringTuple>();

    private static char emSpace = '\u2001';

    void Awake()
    {
        #if !UNITY_EDITOR && UNITY_WEBGL
        WebGLInput.captureAllKeyboardInput = false;
        #endif
    }

    // Use this for initialization
    void Start ()
    {
        this.ParseEmojiInfo(this.textAsset.text);
    }

    private static string GetConvertedString(string inputString)
    {
        string[] converted = inputString.Split('-');
        for (int j = 0; j < converted.Length; j++)
        {
            converted[j] = char.ConvertFromUtf32(Convert.ToInt32(converted[j], 16));
        }
        return string.Join(string.Empty, converted);
    }

    private void ParseEmojiInfo(string inputString)
    {
        using (StringReader reader = new StringReader(inputString))
        {
            string line = reader.ReadLine();
            while (line != null && line.Length > 1)
            {
                // We add each emoji to emojiName
                string[] split = line.Split(' ');
                /*float x = float.Parse(split[1], System.Globalization.CultureInfo.InvariantCulture);
                float y = float.Parse(split[2], System.Globalization.CultureInfo.InvariantCulture);
                float width = float.Parse(split[3], System.Globalization.CultureInfo.InvariantCulture);
                float height = float.Parse(split[4], System.Globalization.CultureInfo.InvariantCulture);
                this.emojiName[GetConvertedString(split[0])] = 
                    new Vector2[]{ new Vector2(x,y),new Vector2(x,y+height),
                        new Vector2(x+width,y+height),new Vector2(x+width,y)};
                */
                this.emojiName[GetConvertedString(split[0])] = split[0];
                line = reader.ReadLine();
            }
        }
    }

    private struct PosStringTuple
    {
        public int x;
        public int y;
        public string emoji;

        public PosStringTuple(int x,int y, string s)
        {
            this.x = x;
            this.y = y;
            this.emoji = s;
        }
    }

    public void AppendEmojiReplacements(int x, int y, string unicode)
    {
        emojiReplacements.Add(new PosStringTuple(x,y, unicode));
    }

    public bool IsEmoji(string s)
    {
        return this.emojiName.ContainsKey(s);
    }

    public void LoadEmoji(GameObject textField)
    {
        int count = emojiReplacements.Count;
        for (int j = 0; j < count; j++)
        {
            int x = emojiReplacements[j].x;
            int y = emojiReplacements[j].y;
            GameObject newRawImage = GameObject.Instantiate(this.rawImageToClone);

            newRawImage.transform.localScale = new Vector3(0.0065f, 1, 0.0065f);
            newRawImage.transform.parent = textField.transform;
            Vector3 imagePos = new Vector3(0.46f+0.14f*x, -0.21f-0.92f*(y-1), 0);
            newRawImage.transform.localPosition = imagePos;
            newRawImage.transform.parent = textField.transform.parent.parent;
            newRawImage.transform.localPosition = new Vector3(
                newRawImage.transform.localPosition.x,0,
                newRawImage.transform.localPosition.z);

            Renderer renderer = newRawImage.GetComponent<Renderer>();
            Texture2D texture = (Texture2D)Resources.Load("emojis/" + emojiName[emojiReplacements[j].emoji]);
            renderer.material.mainTexture = texture;
        }
        emojiReplacements.Clear();
    }
}
