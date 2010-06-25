import ubitrack.SimplePose;
import javax.vecmath.Quat4d;
import javax.vecmath.Vector3d;

public class PaintController {
	Vector3d poseTranslationToVector3d(SimplePose pose) {
		return new Vector3d(pose.getTx(), pose.getTy(), pose.getTz());
	}
	
	Quat4d poseRotationToQuat4d(SimplePose pose) {
		return new Quat4d(pose.getRx(), pose.getRy(), pose.getRz(), pose.getRw());
	}
	
	boolean shouldDraw(SimplePose markerPose, SimplePose editingVolumePose) {
		Vector3d markerTranslationVec = poseTranslationToVector3d(markerPose);
		//Quat4d markerRotationQuat = poseRotationToQuat4d(markerPose);
		
		Vector3d editingVolumeTranslationVec = poseTranslationToVector3d(editingVolumePose);
		//Quat4d editingVolumeRotationQuat = poseRotationToQuat4d(editingVolumePose);
		
		Vector3d distanceVec = new Vector3d();
		distanceVec.sub(markerTranslationVec, editingVolumeTranslationVec);
		
		System.out.println("distance is " + distanceVec.length());
		
		return true;
	}
}
