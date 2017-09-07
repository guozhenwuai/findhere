using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class ObjectTransform {

    public int Sx, Sy, Sz;
    public int Rx, Ry, Rz;
    public int Px, Py, Pz;

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
