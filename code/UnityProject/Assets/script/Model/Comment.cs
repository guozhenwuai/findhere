using System.Collections;
using System.Collections.Generic;
using UnityEngine;

[System.Serializable]
public class Comment {
    public string commentID;
    public string type;

    public void SetCommentID(string newValue)
    {
        commentID = newValue;
    }

    public string GetCommentID()
    {
        return commentID;
    }

    public void SetCommentType(string newValue)
    {
        type = newValue;
    }

    public string GetCommentType()
    {
        return type;
    }
}
