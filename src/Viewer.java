

import java.awt.GraphicsConfiguration;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.PointLight;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.JFrame;
import javax.vecmath.Color3f;
import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import com.sun.j3d.utils.universe.SimpleUniverse;

public class Viewer {

	private static final long serialVersionUID = 1L;

	protected final JFrame frame;
	protected final SimpleUniverse universe;

	private final BranchGroup rootNode;
	
	public Viewer(String frameTitle) {
		frame = new JFrame("3D UI - " + frameTitle);
		
		// Get display information like color depth, etc...
		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
		
		// Create a 3D canvas using the settings of your display
		Canvas3D canvas3D = new Canvas3D(config);
		canvas3D.setSize(800,600);
		universe = new SimpleUniverse(canvas3D);
		
		// Attach lights, shapes, etc here
		rootNode = new BranchGroup();
		rootNode.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		universe.addBranchGraph(rootNode);
		
		createLight();
		initKeyNavigation();
		
		frame.getContentPane().add(canvas3D);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public Viewer(String frameTitle, final UbitrackFacade ubitrack) {
		this(frameTitle);
		
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
	
	public void addObject(BranchGroup object) {
		rootNode.addChild(object);
	}
	
	public void removeObject(BranchGroup object) {
		rootNode.removeChild(object);
	}
	
	public TransformGroup getCameraTransformGroup() {
		// TODO Die kann eigentlich raus
		return universe.getViewingPlatform().getViewPlatformTransform();
	}
	
	private void createLight() {
		// First, create Ambient Light
		//Red, Green, Blue values in 0-1 range
		Color3f white = new Color3f(1.0f, 1.0f, 1.0f);
		
		// Set up the ambient light
		AmbientLight ambientLight = new AmbientLight(white);
		BoundingSphere boundingSphere = new BoundingSphere();
		boundingSphere.setRadius(40.0);
		ambientLight.setInfluencingBounds(boundingSphere);
		
		BranchGroup ambientLightBranchGroup = new BranchGroup();
		ambientLightBranchGroup.addChild(ambientLight);
		
		// Attach it to your scene
		addObject(ambientLightBranchGroup);
		
		// Second, create Directional Light
		// (x,y,z) left, down, backwards
		Vector3f lightDirection = new Vector3f(1.0f, -1.0f, -1.0f);
		DirectionalLight light = new DirectionalLight(
		white, lightDirection);
		light.setInfluencingBounds(boundingSphere);
		
		BranchGroup directionalLightBranchGroup = new BranchGroup();
		directionalLightBranchGroup.addChild(light);

		// Attach it to your scene
		addObject(directionalLightBranchGroup);
		
		// Third, create Point Light
		// Create point ligth with standard attenuation
		PointLight pointLight = new PointLight();
		Point3f position = new Point3f(0.0f,2.f,0.f);
		pointLight.setPosition(position);
		pointLight.setInfluencingBounds(boundingSphere);
		
		BranchGroup pointLightBranchGroup = new BranchGroup();
		pointLightBranchGroup.addChild(pointLight);
		
		//attach it to your scene
		addObject(pointLightBranchGroup);
	}
	
	private void initKeyNavigation() {
		// Get ViewingPlatform from your simple universe
		TransformGroup transformGroupCamera = getCameraTransformGroup();
		
		// Create Behavior and attach it
		KeyNavigatorBehavior keyNavBehav = new KeyNavigatorBehavior(transformGroupCamera);
		
		// Set Bounds
		keyNavBehav.setSchedulingBounds(new BoundingSphere(new Point3d(), 200.0));

		BranchGroup bg = new BranchGroup();
		bg.addChild(keyNavBehav);
		rootNode.addChild(bg);
	}
	
	private void initCamera() {
		Transform3D projectionMatrix = new Transform3D();
		
		double nearClipping = 0.1;
		double farClipping = 5.0;
		// Intrinsic 3x3 Matrix from CameraCalibration - REPLACE if you had to calibrate your own camera
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
		
		// Wrong values to explain a wrongly set frustum
//		projectionMatrix4x4[0] = 0.7*res.m00;
//		projectionMatrix4x4[5] = 0.7*res.m11;
//		projectionMatrix4x4[0] = 1.3*res.m00;
//		projectionMatrix4x4[5] = 1.3*res.m11;
		
		return projectionMatrix4x4;
	}
	
	
}
