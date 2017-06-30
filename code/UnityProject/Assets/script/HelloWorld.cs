using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class HelloWorld : MonoBehaviour {

    private bool hello = false;

	// Use this for initialization
	void Start () {
		
	}
	
	// Update is called once per frame
	void Update () {
		
	}

    public void sayHello() {
        hello = true;
    }

    public void OnGUI() {
        if (hello) {
            GUI.Box(new Rect(300, 300, 200, 200), "Hello World!");
        }
    }

}
