
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Quat4d;
import javax.vecmath.Vector3d;

import ubitrack.SimplePose;
import ubitrack.SimplePoseReceiver;

public class PoseReceiver extends SimplePoseReceiver {

	protected TransformGroup markerTransGroup = null;
	
	public void setTransformGroup(TransformGroup markerTransGroup) {
		this.markerTransGroup = markerTransGroup;
	}
	
	public void receivePose(SimplePose pose) {
		if (markerTransGroup == null){
			return;
		}

		double[] trans = new double[3];
		double[] rot = new double[4];
		trans[0] = pose.getTx();
		trans[1] = pose.getTy();
		trans[2] = pose.getTz();
		rot[0] = pose.getRx();
		rot[1] = pose.getRy();
		rot[2] = pose.getRz();
		rot[3] = pose.getRw();

		Vector3d transVec = new Vector3d(pose.getTx(), pose.getTy(), pose.getTz());
		Quat4d rotQ = new Quat4d(pose.getRx(), pose.getRy(), pose.getRz(), pose.getRw());
		Transform3D markerTransform = new Transform3D();
		markerTransform.set(rotQ, transVec, 1);
		markerTransGroup.setTransform(markerTransform);
		
//		System.out.println("Pos: " + pose.getTx() + ", " + pose.getTy() + ", " + pose.getTz());
	}
}
