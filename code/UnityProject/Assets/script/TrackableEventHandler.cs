
using UnityEngine;

namespace Vuforia
{
    public class TrackableEventHandler : MonoBehaviour,
                                                ITrackableEventHandler
    {
        private TrackableBehaviour mTrackableBehaviour;
        public ContentManager mContentManager;

        void Start()
        {
            mTrackableBehaviour = GetComponent<TrackableBehaviour>();
            if (mTrackableBehaviour)
            {
                mTrackableBehaviour.RegisterTrackableEventHandler(this);
            }
        }

        public void OnTrackableStateChanged(
                                        TrackableBehaviour.Status previousStatus,
                                        TrackableBehaviour.Status newStatus)
        {
            if (newStatus == TrackableBehaviour.Status.DETECTED ||
                newStatus == TrackableBehaviour.Status.TRACKED ||
                newStatus == TrackableBehaviour.Status.EXTENDED_TRACKED)
            {
                OnTrackingFound();
            }
            else
            {
                OnTrackingLost();
            }
        }

        private void OnTrackingFound()
        {
            mContentManager.Show(true);

            Debug.Log("Trackable " + mTrackableBehaviour.TrackableName + " found");
        }


        private void OnTrackingLost()
        {
            mContentManager.Show(false);

            Debug.Log("Trackable " + mTrackableBehaviour.TrackableName + " lost");
        }

    }
}
