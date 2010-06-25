import ubitrack.SimplePose;
import javax.vecmath.Quat4d;
import javax.vecmath.Vector3d;

public class PaintController {
	/**
	 * Converts a SimplePose translation into a Java3D vector object.
	 * @param pose the pose to convert
	 * @return the pose's translation as a Vector3d
	 */
	Vector3d poseTranslationToVector3d(SimplePose pose) {
		return new Vector3d(pose.getTx(), pose.getTy(), pose.getTz());
	}
	
	Quat4d poseRotationToQuat4d(SimplePose pose) {
		return new Quat4d(pose.getRx(), pose.getRy(), pose.getRz(), pose.getRw());
	}
	
	/**
	 * Returns the distance vector between the translations of two ubitrack poses.
	 * @param a the first pose
	 * @param b the second pose
	 * @return the distance vector between the two poses
	 */
	Vector3d getDistanceBetweenPoses(SimplePose a, SimplePose b) {
		Vector3d aTranslationVec = poseTranslationToVector3d(a);	
		Vector3d bTranslationVec = poseTranslationToVector3d(b);
		
		Vector3d distanceVec = new Vector3d();
		distanceVec.sub(aTranslationVec, bTranslationVec);
		
		return distanceVec;
	}
	
	/**
	 * Checks whether the pose of the pen marker is inside the editing volume.
	 * @param penPose the pose of the pen marker
	 * @param editingVolumePose the pose of the editing volume
	 * @param radius the radius of the editing volume
	 * @return true if inside, false if not
	 */
	boolean shouldDraw(SimplePose penPose, SimplePose editingVolumePose, double radius) {
		Vector3d distanceVec = getDistanceBetweenPoses(penPose, editingVolumePose);
		
		System.out.println("distance is " + distanceVec.length());
		
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
		Vector3d distanceVec = getDistanceBetweenPoses(penPose, editingVolumePose);
		distanceVec.normalize();
		return distanceVec;
	}
}
