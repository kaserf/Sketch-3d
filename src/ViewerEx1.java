

import java.awt.GraphicsConfiguration;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.PointLight;
import javax.media.j3d.TransformGroup;
import javax.swing.JFrame;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import com.sun.j3d.utils.universe.SimpleUniverse;

public class ViewerEx1 {

	private static final long serialVersionUID = 1L;

	protected final JFrame frame;
	protected final SimpleUniverse universe;

	private final BranchGroup rootNode;
	
	public ViewerEx1(String frameTitle) {
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


}
