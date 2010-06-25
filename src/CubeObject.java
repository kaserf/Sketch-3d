
import javax.media.j3d.Appearance;

import com.sun.j3d.utils.geometry.Sphere;

public class CubeObject extends TransformableObject {
	
	public CubeObject(Appearance app) {
		Sphere iAmASphereButIllBeABoxSoon = new Sphere(0.005f, app);
		transGroup.addChild(iAmASphereButIllBeABoxSoon);
	}

	
}
