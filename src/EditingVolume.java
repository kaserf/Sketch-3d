import javax.media.j3d.BranchGroup;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Quat4d;
import javax.vecmath.Vector3d;

import com.sun.j3d.utils.universe.SimpleUniverse;


public class EditingVolume extends TransformableObject {
	private final BranchGroup rootNode;
	protected TransformGroup transformGroup = null;
	
	public void setTransformGroup(TransformGroup transformGroup) {
		this.transformGroup = transformGroup;
	}

	public EditingVolume(SimpleUniverse universe, TransformGroup transformGroup) {
		super();
		this.transformGroup = transformGroup;
		rootNode = new BranchGroup();
		rootNode.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		universe.addBranchGraph(rootNode);
	}
	
	public void updateLocation(Vector3d translation, Quat4d rotation) {
		Transform3D transform = new Transform3D();
		transform.set(rotation, translation, 1);
		transformGroup.setTransform(transform);
	}

	void drawDot(Vector3d pos) {
		BlueAppearance appearance = new BlueAppearance();
		SphereObject pixelObj = new SphereObject(0.005f, appearance);

		rootNode.addChild(pixelObj);

		Transform3D objT3D = new Transform3D();
		objT3D.setTranslation(pos);

		pixelObj.getTransformGroup().setTransform(objT3D);
	}
}
