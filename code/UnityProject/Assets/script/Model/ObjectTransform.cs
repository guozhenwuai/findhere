using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class ObjectTransform {

    public float Sx, Sy, Sz;
    public float Rx, Ry, Rz;
    public float Px, Py, Pz;

    public Vector3 GetObjectPosition()
    {
        return new Vector3(Px, Py, Pz);
    }

    public Vector3 GetObjectRotation()
    {
        return new Vector3(Rx, Ry, Rz);
    }

    public Vector3 GetObjectScale()
    {
        return new Vector3(Sx, Sy, Sz);
    }
}
