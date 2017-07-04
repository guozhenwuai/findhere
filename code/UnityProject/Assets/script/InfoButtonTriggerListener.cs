using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class InfoButtonTriggerListener : MonoBehaviour
{
    private string message;
    private ContentManager mContentManager;

    public void Start()
    {
        mContentManager = FindObjectOfType<ContentManager>();
    }

    private void OnMouseDown()
    {
        mContentManager.OnDisplay();
        message = this.transform.parent.name;
        Debug.Log("Click:"+message);
    }
}
