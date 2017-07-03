using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class InfoButtonTriggerListener : MonoBehaviour
{
    private string message;
    private void OnMouseDown()
    {
        message = this.transform.parent.name;
        Debug.Log("Click:"+message);
    }
}
