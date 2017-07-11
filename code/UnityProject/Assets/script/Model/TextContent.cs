using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class TextContent {
    public string text;
    public string userID;
    public string time;

    public void SetText(string newValue)
    {
        text = newValue;
    }

    public string GetText()
    {
        return text;
    }

    public void SetUserID(string newValue)
    {
        userID = newValue;
    }

    public string GetUserID()
    {
        return userID;
    }

    public void SetTime(string newValue)
    {
        time = newValue;
    }

    public string GetTime()
    {
        return time;
    }
}
