import ubitrack.SimplePose;
import javax.vecmath.Vector3d;

public class PaintController {
	/**
	 * Checks whether the pose of the pen marker is inside the editing volume.
	 * @param penPose the pose of the pen marker
	 * @param editingVolumePose the pose of the editing volume
	 * @param radius the radius of the editing volume
	 * @return true if inside, false if not
	 */
	boolean shouldDraw(SimplePose penPose, SimplePose editingVolumePose, double radius) {
		if (penPose == null || editingVolumePose == null) {
			return false;
		}
		
		Vector3d distanceVec = Utils.getDistanceBetweenPoses(penPose, editingVolumePose);
		
		//System.out.println("distance is " + distanceVec.length());
		
		if (distanceVec.length() <= radius) {
			System.out.println("inside the editing volume " + distanceVec.length());
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Returns normalized draw coordinates inside the editing volume.
	 * If shouldDraw() returns true, this is where the pixel should be drawn at.
	 * @param penPose
	 * @param editingVolumePose
	 * @return
	 */
	Vector3d getDrawCoords(SimplePose penPose, SimplePose editingVolumePose) {
		// We probably already know that. But what the hell let's just compute it once more.
		Vector3d distanceVec = Utils.getDistanceBetweenPoses(penPose, editingVolumePose);
		distanceVec.normalize();
		return distanceVec;
	}
}
