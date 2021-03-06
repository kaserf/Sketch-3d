import java.text.DecimalFormat;

import javax.vecmath.Quat4d;
import javax.vecmath.Vector3d;

import ubitrack.SimplePose;

public class Utils {
	/**
	 * Converts a SimplePose translation into a Java3D vector object.
	 * @param pose the pose to convert
	 * @return the pose's translation as a Vector3d
	 */
	static Vector3d poseTranslationToVector3d(SimplePose pose) {
		return new Vector3d(pose.getTx(), pose.getTy(), pose.getTz());
	}
	
	static Quat4d poseRotationToQuat4d(SimplePose pose) {
		return new Quat4d(pose.getRx(), pose.getRy(), pose.getRz(), pose.getRw());
	}
	
	/**
	 * Returns the distance vector between the translations of two ubitrack poses.
	 * @param a the first pose
	 * @param b the second pose
	 * @return the distance vector between the two poses
	 */
	static Vector3d getDistanceBetweenPoses(SimplePose a, SimplePose b) {
		Vector3d aTranslationVec = poseTranslationToVector3d(a);	
		Vector3d bTranslationVec = poseTranslationToVector3d(b);
			
		Vector3d distanceVec = new Vector3d();
		distanceVec.sub(aTranslationVec, bTranslationVec);
		
		return distanceVec;
	}
	
	/**
	 * round to one digit after the comma
	 * @param coords
	 * @return
	 */
	static Vector3d roundCoords(Vector3d coords){
		return new Vector3d(roundTwoDecimals(coords.x),
				roundTwoDecimals(coords.y), roundTwoDecimals(coords.z));
		//return new Vector3d(roundOneDecimals(coords.x),
		//		roundOneDecimals(coords.y), roundOneDecimals(coords.z));
	}
	
	private static double roundTwoDecimals(double d) {
    	DecimalFormat twoDForm = new DecimalFormat("#.##");
		return Double.valueOf(twoDForm.format(d));
	}
	
	private static double roundOneDecimals(double d) {
    	DecimalFormat twoDForm = new DecimalFormat("#.#");
		return Double.valueOf(twoDForm.format(d));
	}
}
