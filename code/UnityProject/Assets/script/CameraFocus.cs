using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using Vuforia;

public class CameraFocus : MonoBehaviour {

    private float touchduration;
    private Touch touch;

    // Use this for initialization
    void Start () {
		
	}
	
	// Update is called once per frame
	void Update () {
        if (Input.touchCount > 0)
        {
            Debug.Log("cloud reco handler update");
            touchduration += Time.deltaTime;
            touch = Input.GetTouch(0);
            if (touch.phase == TouchPhase.Ended && touchduration < 0.2f)
            {
                Debug.Log("touch");
                StartCoroutine(TriggerAutoFocusAndEnableContinuousFocusIfSet());
            }
        }
        else
        {
            touchduration = 0;
        }
    }

    private IEnumerator TriggerAutoFocusAndEnableContinuousFocusIfSet()
    {
        yield return new WaitForSeconds(0.3f);
        if (touch.tapCount == 2)
        {
            StopCoroutine("TriggerAutoFocusAndEnableContinuousFocusIfSet");
            Debug.Log("Double-click: auto focus.");
            CameraDevice.Instance.SetFocusMode(CameraDevice.FocusMode.FOCUS_MODE_TRIGGERAUTO);
            yield return new WaitForSeconds(1.0f);
            CameraDevice.Instance.SetFocusMode(CameraDevice.FocusMode.FOCUS_MODE_CONTINUOUSAUTO);
        }
    }

}
