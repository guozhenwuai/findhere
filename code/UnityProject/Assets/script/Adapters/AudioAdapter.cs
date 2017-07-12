using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class AudioAdapter : MonoBehaviour {

    public GameObject audioField;
    public ContentManager contentManager;

	// Use this for initialization
	void Start () {
		
	}
	
	// Update is called once per frame
	void Update () {
		
	}

    public void SetAudioSource(AudioClip clip, GameObject parent)
    {
        AudioSource source = audioField.GetComponent<AudioSource>();
        source.clip = clip;
        
        source.transform.parent = parent.transform.parent;
        source.transform.localPosition = new Vector3(0, 0, 0);

        contentManager.AudioPlaying(true);
    }
}
