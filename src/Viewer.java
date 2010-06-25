
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.j3d.Transform3D;
import javax.vecmath.Matrix4d;

public class Viewer extends ViewerEx1 {

	private static final long serialVersionUID = 1L;

	public Viewer(String frameTitle, final UbitrackFacade ubitrack) {
		super(frameTitle);
		
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				closeApp();
			}
			public void windowClosed(WindowEvent e) {
				closeApp();
			}
			private void closeApp() {
				if (ubitrack != null) {
					ubitrack.stopUbitrack();
				}
			}
		});
		
		initCamera();
	}
	
	private void initCamera() {
		Transform3D projectionMatrix = new Transform3D();
		
		double nearClipping = 0.1;
		double farClipping = 5.0;
		double[] intrinsics = {402.97830200195313, 0, -161.19090270996094, 
				0, 403.01669311523437, -106.31525421142578, 
				0, 0, -1};

		double[] projectionArray = constructProjectionMatrix(intrinsics, nearClipping, farClipping);
		
//		for (int i = 0; i < projectionArray.length; i++) {
//			System.out.println(i + ":" + projectionArray[i]);
//		}
		
		projectionMatrix.set(projectionArray);
		universe.getViewer().getView().setCompatibilityModeEnable(true);
		universe.getViewer().getView().setLeftProjection(projectionMatrix);
	}
	
	private double[] constructProjectionMatrix(double[] i3x3, double near, double far) {
		double r = 320.0;
		double l = 0.0;
		double t = 240.0;
		double b = 0.0;

		double norm = Math.sqrt(i3x3[6] * i3x3[6] + i3x3[7] * i3x3[7] + i3x3[8] * i3x3[8]);
		
		double add = far * near * norm;

		Matrix4d m = new Matrix4d();
		m.m00 = i3x3[0];
		m.m01 = i3x3[1];
		m.m02 = i3x3[2];
		m.m10 = i3x3[3];
		m.m11 = i3x3[4];
		m.m12 = i3x3[5];
		m.m20 = i3x3[6] * (-far - near);
		m.m21 = i3x3[7] * (-far - near);
		m.m22 = i3x3[8] * (-far - near);
		m.m23 = add;
		m.m30 = i3x3[6];
		m.m31 = i3x3[7];
		m.m32 = i3x3[8];
	
		Matrix4d ortho = new Matrix4d();
		ortho.m00 = 2.0 / (r-l);
		ortho.m01 = 0.0;
		ortho.m02 = 0.0;
		ortho.m03 = (r+l) / (l-r);
		ortho.m10 = 0.0;
		ortho.m11 = 2.0 / (t-b);
		ortho.m12 = 0.0;
		ortho.m13 = (t+b) / (b-t);
		ortho.m20 = 0.0;
		ortho.m21 = 0.0;
		ortho.m22 = 2.0 / (near-far);
		ortho.m23 = (far+near) / (near-far);
		ortho.m30 = 0.0;
		ortho.m31 = 0.0;
		ortho.m32 = 0.0;
		ortho.m33 = 1.0;
		
		Matrix4d res = new Matrix4d();
		res.mul(ortho, m);
		
//		System.out.println("Res: " + res);
		
		double[] projectionMatrix4x4 = new double[16];
		projectionMatrix4x4[0] = res.m00;
		projectionMatrix4x4[1] = res.m01;
		projectionMatrix4x4[2] = res.m02;
		projectionMatrix4x4[3] = res.m03;
		projectionMatrix4x4[4] = res.m10;
		projectionMatrix4x4[5] = res.m11;
		projectionMatrix4x4[6] = res.m12;
		projectionMatrix4x4[7] = res.m13;
		projectionMatrix4x4[8] = res.m20;
		projectionMatrix4x4[9] = res.m21;
		projectionMatrix4x4[10] = -res.m22;
		projectionMatrix4x4[11] = -res.m23;
		projectionMatrix4x4[12] = res.m30;
		projectionMatrix4x4[13] = res.m31;
		projectionMatrix4x4[14] = res.m32;
		projectionMatrix4x4[15] = res.m33;
		
		return projectionMatrix4x4;
	}
	
	
}
