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
		
		CoordAxis x_axis = new CoordAxis(0.001f, 0.2f, new GreenAppearance());
		CoordAxis y_axis = new CoordAxis(0.001f, 0.2f, new BlueAppearance());
		CoordAxis z_axis = new CoordAxis(0.001f, 0.2f, new RedAppearance());
		
		Transform3D x_axisT3D = new Transform3D();
		x_axisT3D.rotZ(Math.PI / 2.0f);
		x_axisT3D.setTranslation(new Vector3d(0.1f, 0.0f, 0.0f));
		x_axis.getTransformGroup().setTransform(x_axisT3D);
		
		Transform3D z_axisT3D = new Transform3D();
		z_axisT3D.rotX(Math.PI / 2.0f);
		z_axisT3D.setTranslation(new Vector3d(0.0f, 0.0f, 0.1f));
		z_axis.getTransformGroup().setTransform(z_axisT3D);
		
		Transform3D y_axisT3D = new Transform3D();
		y_axisT3D.setTranslation(new Vector3d(0.0f, 0.1f, 0.0f));
		y_axis.getTransformGroup().setTransform(y_axisT3D);
		
		TransformGroup axis = new TransformGroup();
		axis.setCapability(axis.ALLOW_TRANSFORM_WRITE);
		axis.addChild(z_axis);
		axis.addChild(y_axis);
		axis.addChild(x_axis);
		
		//this.addChild(axis);
		transGroup.addChild(axis);
		
		view.universe.addBranchGraph(this);
		//view.addObject(this);
		
		drawDot(new Vector3d(0,0,0));
		//drawDot(new Vector3d(0.02,0,0));
		//drawDot(new Vector3d(0,0.02,0));
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
	
	void drawDot(Transform3D pos) {
		BlueAppearance appearance = new BlueAppearance();
		SphereObject pixelObj = new SphereObject(0.005f, appearance);

		transGroup.addChild(pixelObj);

//		view.addObject(pixelObj);

		pixelObj.getTransformGroup().setTransform(pos);
	}
}
