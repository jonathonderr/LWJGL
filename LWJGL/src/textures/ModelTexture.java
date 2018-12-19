package textures;

public class ModelTexture {

	private int textureID;
	private int normalMap;
	private int specularMap;
	
	private float shineDamper = 1000000;
	private float reflectivity = 0;
	
	private boolean hasTransparency = false;
	private boolean useFakeLighting = false;
	private boolean hasSpecularMap = false;
	
	private int numberOfRow = 1;
	
	public ModelTexture(int id){
		this.textureID = id;
	}
	
	
	public void setSpecularMap(int specMap){
		this.specularMap = specMap;
		this.hasSpecularMap = true;
	}
	
	public boolean hasSpecularMap(){
		return hasSpecularMap;
	}
	
	public int getSpecularMap(){
		return specularMap;
	}
	
	public int getNumberOfRow() {
		return numberOfRow;
	}





	public int getNormalMap() {
		return normalMap;
	}





	public void setNormalMap(int normalMap) {
		this.normalMap = normalMap;
	}





	public void setNumberOfRow(int numberOfRow) {
		this.numberOfRow = numberOfRow;
	}





	public boolean isUseFakeLighting() {
		return useFakeLighting;
	}




	public void setUseFakeLighting(boolean useFakeLighting) {
		this.useFakeLighting = useFakeLighting;
	}




	public boolean isHasTransparency() {
		return hasTransparency;
	}



	public void setHasTransparency(boolean hasTransparency) {
		this.hasTransparency = hasTransparency;
	}



	public int getId(){
		return this.textureID;
	}

	public float getShineDamper() {
		return shineDamper;
	}

	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}
	
	
	
}
