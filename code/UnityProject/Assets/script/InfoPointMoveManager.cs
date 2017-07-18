using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class InfoPointMoveManager : MonoBehaviour {

    private bool DoZoomingIn = false;
    private bool DoZoomingOut = false;
    private List<InfoPoint> InfoPoints = new List<InfoPoint>();

    public GameObject infoPoint;

	// Use this for initialization
	void Start () {
		
	}
	
	// Update is called once per frame
	void Update () {
        if (InfoPoints == null)
        {
            DoZoomingIn = false;
            DoZoomingOut = false;
            return;
        }
        if (DoZoomingIn)
        {
            bool flag = true;
            foreach(InfoPoint point in InfoPoints)
            {
                Transform transform = point.gameObject.transform;
                if (Vector3.Distance(transform.localPosition, new Vector3(0, 0, 0)) >= 0.1f)
                {
                    flag = false;
                    transform.localPosition = Vector3.Lerp(transform.localPosition, new Vector3(0, 0, 0), Time.deltaTime * 2f);
                }
            }
            infoPoint.transform.Rotate(Vector3.up, 40 * Time.deltaTime);
            if (flag)
            {
                DoZoomingIn = false;
                Debug.Log("Stop zooming in");
            }
        }
        if (!DoZoomingIn && DoZoomingOut)
        {
            bool flag = true;
            foreach (InfoPoint point in InfoPoints)
            {
                Transform transform = point.gameObject.transform;
                Vector3 position = point.position;
                if (Vector3.Distance(transform.localPosition, position) >= 0.1f)
                {
                    flag = false;
                    transform.localPosition = Vector3.Lerp(transform.localPosition, position, Time.deltaTime * 2f);
                }

            }
            infoPoint.transform.Rotate(Vector3.up, 40 * Time.deltaTime);
            if (flag)
            {
                DoZoomingOut = false;
                Debug.Log("Stop Zooming out");
                Quaternion q = Quaternion.Euler(0, 180, 0); ;
                foreach (InfoPoint point in InfoPoints)
                {
                    Transform transform = point.gameObject.transform;
                    transform.rotation = q;
                }
            }
        }
	}

    public void ZoomingIn()
    {
        InfoPoints.Clear();
        foreach(Transform transform in infoPoint.transform)
        {
            InfoPoint point = new InfoPoint(transform.gameObject, transform.localPosition);
            InfoPoints.Add(point);
        }

        DoZoomingIn = true;
    }

    public void ZoomingOut()
    {
        InfoPoints.Clear();
        foreach(Transform transform in infoPoint.transform)
        {
            Vector3 position = new Vector3(
                Random.Range(-0.8f, 0.8f),
                Random.Range(0, 0.4f),
                Random.Range(-0.8f, 0.8f));
            InfoPoint point = new InfoPoint(transform.gameObject,position);
            InfoPoints.Add(point);
        }

        DoZoomingOut = true;
    }

    private struct InfoPoint
    {
        public GameObject gameObject;
        public Vector3 position;

        public InfoPoint(GameObject infoPoint, Vector3 position)
        {
            this.gameObject = infoPoint;
            this.position = position;
        }
    }
}
