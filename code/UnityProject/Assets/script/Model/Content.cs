using System.Collections;
using System.Collections.Generic;
using UnityEngine;

[System.Serializable]
public class Content {
    public string contentID;
    public string type;

    public void SetContentID(string newValue)
    {
        contentID = newValue;
    }

    public string GetContentID()
    {
        return contentID;
    }

    public void SetContentType(string newValue)
    {
        type = newValue;
    }

    public string GetContentType()
    {
        return type;
    }
}
