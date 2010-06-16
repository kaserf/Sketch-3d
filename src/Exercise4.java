package de.tum.in.far.threedui.ex4.solution.amal;

import java.io.File;
import java.io.FileNotFoundException;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.AxisAngle4d;
import javax.vecmath.Vector3d;

import org.jdesktop.j3d.loaders.vrml97.VrmlLoader;

import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.ParsingErrorException;
import com.sun.j3d.loaders.Scene;

import de.tum.in.far.threedui.ex2.solution.BackgroundObject;
import de.tum.in.far.threedui.ex2.solution.ImageReceiver;
import de.tum.in.far.threedui.ex2.solution.ModelObject;
import de.tum.in.far.threedui.ex2.solution.PoseReceiver;
import de.tum.in.far.threedui.ex2.solution.UbitrackFacade;
import de.tum.in.far.threedui.ex2.solution.Viewer;









public class Exercise4 {
	public final static String COMPONENT_DIRECTORY = System.getProperty("user.dir") + File.separator + "libs" + File.separator + "ubitrack" + File.separator + "bin" + File.separator + "ubitrack";
	public final static String DATAFLOW_PATH = System.getProperty("user.dir") + File.separator + "dataflow" + File.separator + "3D-UI-SS-2010-Markertracker.dfg";


	public static final String EXERCISE = "Exercise 4 Solution";
	
	private UbitrackFacade ubitrackFacade;
	private PoseReceiverButton poseReceiverButton;
	private PoseReceiver poseReceiver2;
	private PoseReceiver poseReceiver3;
	private ImageReceiver imageReceiver;
	
	private Viewer viewer;
	private ButtonObject buttonObject;
	private ModelObject sheepObject;
	private MotionInterpolator motionInterpolator;
	
	public Exercise4() {
		ubitrackFacade = new UbitrackFacade();
	}

	public static void main(String[] args) {
		Exercise4 exercise4 = new Exercise4();
		exercise4.initializeJava3D();
		exercise4.loadSheep();
		exercise4.initializeUbitrack();
		exercise4.linkUbitrackToViewer();
	}
	
	private void initializeUbitrack() {
		ubitrackFacade.initUbitrack();
		
		motionInterpolator = new MotionInterpolator(sheepObject.getTransformGroup(), buttonObject);
		poseReceiverButton = new PoseReceiverButton(buttonObject, motionInterpolator);	
		if (!ubitrackFacade.setPoseCallback("posesink", poseReceiverButton)) {
			return;
		}
		poseReceiver2 = new PoseReceiver();
		if (!ubitrackFacade.setPoseCallback("posesink2", poseReceiver2)) {
			return;
		}
		poseReceiver3 = new PoseReceiver();
		if (!ubitrackFacade.setPoseCallback("posesink3", poseReceiver3)) {
			return;
		}
		imageReceiver = new ImageReceiver();
		if (!ubitrackFacade.setImageCallback("imgsink", imageReceiver)) {
			return;
		}
		ubitrackFacade.startDataflow();
	}
	
	private void linkUbitrackToViewer() {
		BackgroundObject backgroundObject = new BackgroundObject();
		viewer.addObject(backgroundObject);
		imageReceiver.setBackground(backgroundObject.getBackground());
		
		poseReceiverButton.setTransformGroup(buttonObject.getTransformGroup());
		poseReceiver2.setTransformGroup(motionInterpolator.getStartTG());
		poseReceiver3.setTransformGroup(motionInterpolator.getDestTG());
	}
	
	protected void loadSheep() {
		VrmlLoader loader = new VrmlLoader();
		Scene myScene = null;
		try {
			myScene = loader.load( "models" + File.separator + "Sheep.wrl");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IncorrectFormatException e) {
			e.printStackTrace();
		} catch (ParsingErrorException e) {
			e.printStackTrace();
		}

		BranchGroup bg = new BranchGroup();
		TransformGroup offset = new TransformGroup();
		Transform3D t3d = new Transform3D();
		t3d.setRotation(new AxisAngle4d(1.0, 0.0, 0.0, Math.PI/2));
		t3d.setTranslation(new Vector3d(0.0, 0.0, 0.025));
		offset.setTransform(t3d);
		bg.addChild(offset);
		offset.addChild(myScene.getSceneGroup());
		
		sheepObject = new ModelObject(bg);
		viewer.addObject(sheepObject);
		System.out.println("Sheep loaded");
	}

	private void initializeJava3D() {
		System.out.println("Creating Viewer - " + EXERCISE);
		viewer = new Viewer(EXERCISE, ubitrackFacade);

		buttonObject = new ButtonObject();
		viewer.addObject(buttonObject);
		
		System.out.println("Done");
	}

}
