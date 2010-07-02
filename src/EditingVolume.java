import javax.media.j3d.BranchGroup;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Quat4d;
import javax.vecmath.Vector3d;

import com.sun.j3d.utils.universe.SimpleUniverse;


public class EditingVolume extends TransformableObject {
	private Viewer view;

	public EditingVolume(Viewer v) {
		super();
		view = v;
		this.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		transGroup.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
		
//		transformGroup.addChild(rootNode);
//		rootNode.addChild(transformGroup);
		
		view.universe.addBranchGraph(this);
		
		drawDot(new Vector3d(0,0,0));
		drawDot(new Vector3d(0.02,0,0));
		drawDot(new Vector3d(0,0.02,0));
	}
	
	public void updateLocation(Vector3d translation, Quat4d rotation) {
		Transform3D transform = new Transform3D();
		transform.set(rotation, translation, 1);
		transGroup.setTransform(transform);
	}

	void drawDot(Vector3d pos) {
		BlueAppearance appearance = new BlueAppearance();
		SphereObject pixelObj = new SphereObject(0.005f, appearance);

		transGroup.addChild(pixelObj);

//		view.addObject(pixelObj);

		Transform3D objT3D = new Transform3D();
		objT3D.setTranslation(pos);

		pixelObj.getTransformGroup().setTransform(objT3D);
	}
}
