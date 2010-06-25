import java.io.File;

import javax.vecmath.Vector3d;

public class Sketch3D implements PoseUpdatedNotification {
	public final static String COMPONENT_DIRECTORY = System.getProperty("user.dir") + File.separator + "libs" + File.separator + "ubitrack" + File.separator + "bin" + File.separator + "ubitrack";
	public final static String DATAFLOW_PATH = System.getProperty("user.dir") + File.separator + "dataflow" + File.separator + "3D-UI-SS-2010-Markertracker.dfg";


	public static final String WINDOW_TITLE = "Sketch3D";
	
	private UbitrackFacade ubitrackFacade;
	
	/** Pose receiver for the editing volume marker. */
	private PoseReceiver poseReceiverEditingVolume;
	
	/** Pose receiver for the pen marker. */
	private PoseReceiver poseReceiverPen;

	/** The main paint controller. */
	private static PaintController paintController;
	
	public static final double EDITING_VOLUME_RADIUS = 0.25; //25 cm??
	
	private Vector3d latestPenTranslation = null;
	private Vector3d latestEditingVolumeTranslation = null;
	
	private ImageReceiver imageReceiver;
	private Viewer viewer;
	
	public static PaintController getPaintController() {
		return paintController;
	}

	public Sketch3D() {
		ubitrackFacade = new UbitrackFacade();
		paintController = new PaintController();
	}

	public static void main(String[] args) {
		Sketch3D sketch3D = new Sketch3D();
		sketch3D.initializeJava3D();
		sketch3D.initializeUbitrack();
		sketch3D.linkUbitrackToViewer();
	}
	
	private void initializeUbitrack() {
		ubitrackFacade.initUbitrack();
		
		poseReceiverEditingVolume = new PoseReceiver(this, 1);	
		if (!ubitrackFacade.setPoseCallback("posesink", poseReceiverEditingVolume)) {
			return;
		}
		
		poseReceiverPen = new PoseReceiver(this, 2);
		if (!ubitrackFacade.setPoseCallback("posesink2", poseReceiverPen)) {
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
	}

	private void initializeJava3D() {
		System.out.println("Creating Viewer - " + WINDOW_TITLE);
		viewer = new Viewer(WINDOW_TITLE, ubitrackFacade);
		
		System.out.println("Done");
	}

	@Override
	public void handlePoseUpdatedNotification(PoseReceiver poseReceiver) {
		System.out.println("Update notification from marker " + poseReceiver.getTag() + " -- pos: " + poseReceiver.getTranslationVector());
		
		switch (poseReceiver.getTag()) {
		case 1:
			latestEditingVolumeTranslation = poseReceiver.getTranslationVector();
			break;

		case 2:
			latestPenTranslation = poseReceiver.getTranslationVector();
			break;
			
		default:
			throw new RuntimeException("Unknown tag value for poseReceiver");
		}
		
		if (paintController.shouldDraw(latestPenTranslation, latestEditingVolumeTranslation, EDITING_VOLUME_RADIUS)) {
			Vector3d drawCoords = paintController.getDrawCoords(latestPenTranslation, latestEditingVolumeTranslation);
			System.out.println("Drawing at coords " + drawCoords);
		}
	}

}
