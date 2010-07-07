

import javax.media.j3d.Appearance;

import com.sun.j3d.utils.geometry.Cylinder;

public class CoordAxis extends TransformableObject {

	public CoordAxis(float radius, float height, Appearance app){
		Cylinder cyl = new Cylinder(radius, height, app);
		transGroup.addChild(cyl);
	}
}
