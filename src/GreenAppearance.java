

import javax.media.j3d.Appearance;
import javax.media.j3d.Material;
import javax.vecmath.Color3f;

public class GreenAppearance extends Appearance {

	public GreenAppearance() {
		// Create the blue appearance node
		Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
		Color3f green = new Color3f(0.3f, 0.8f, 0.3f);
		Color3f specular = new Color3f(0.9f, 0.9f, 0.9f);
		
		// Ambient,emissive,diffuse,specular,shininess
		Material blueMat = new Material
		(green, black, green, specular,25.0f);
		
		//Switch on light
		blueMat.setLightingEnable(true);
		
		setMaterial(blueMat);
	}
}
