
import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Geometry;
import javax.media.j3d.TransformGroup;

import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.geometry.Sphere;

public class SphereObject extends BranchGroup {

	protected final TransformGroup transGroup;
	protected final Sphere sphere;
	
	public SphereObject(float radius) {
		this(radius, null);
	}
	
	public SphereObject(float radius, Appearance app) {
		transGroup = new TransformGroup();
		transGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		
		// Create your Shape
		sphere = new Sphere(radius, Primitive.GENERATE_NORMALS, 60, app);
		transGroup.addChild(sphere);
		
		addChild(transGroup);
	}

	public TransformGroup getTransformGroup() {
		return transGroup;
	}
	
	public Geometry getGeometry() {
		return sphere.getShape().getGeometry();
	}
	
}
