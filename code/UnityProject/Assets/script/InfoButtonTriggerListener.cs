using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class InfoButtonTriggerListener : MonoBehaviour
{
    private void OnMouseDown()
    {
        string text = this.name;
        Debug.Log("Click:"+text);
    }
}
