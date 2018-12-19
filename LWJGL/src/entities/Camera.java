package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.DisplayManager;
import renderEngine.MasterRenderer;
import terrains.Terrain;

public class Camera {

	private static final float JUMP_POWER = 30;
	private static float GRAVITY = -60;
	public static float RUN_SPEED = 40;
	
	public boolean stop;
	public boolean cameraIsAboveGround = true;
	
	private float distanceFromPlayer = 50;
	private float angleAroundPlayer = 0;
	
	public Vector3f directionalVelocity,angularVelocity;
	
	
	public Vector3f position = new Vector3f(0,0,0);
	private float yaw = 0.0f;
	private float pitch = 20.0f;
	
	private boolean isInAir = false;
	private float upwardSpeed = 0;
	
	private Player player;

	public Camera(Player player) {
		this.player = player;
		this.angularVelocity = new Vector3f(0,0,0);
		this.directionalVelocity = new Vector3f(0,0,0);
	}
	
	private float walkAcc = 0.2f;
	private float mouseAcc = 0.2f;
	
	public void moveFirstPerson(){
		RUN_SPEED = 40;
		upwardSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
		increasePosition(0, upwardSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		//float terrainHeight = terrain.getHeightOfTerrain(position.x, position.z);
		float terrainHeight = 0;
		if(position.y <terrainHeight + 13){
			upwardSpeed = 0;
			isInAir = false;
			position.y = terrainHeight + 13;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_W))
        {
			
			position.x += (float) (Math.sin(Math.toRadians(yaw)) * DisplayManager.getFrameTimeSeconds()* RUN_SPEED);
    		position.z -= (float) (Math.cos(Math.toRadians(yaw)) * DisplayManager.getFrameTimeSeconds()* RUN_SPEED);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S))
        {
        	position.x -= (float) (Math.sin(Math.toRadians(yaw)) * DisplayManager.getFrameTimeSeconds()* RUN_SPEED);
    		position.z += (float) (Math.cos(Math.toRadians(yaw)) * DisplayManager.getFrameTimeSeconds()* RUN_SPEED);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A))
        {
        	position.x -= (float) (Math.sin(Math.toRadians(yaw + 90)) * DisplayManager.getFrameTimeSeconds()* RUN_SPEED);
    		position.z += (float) (Math.cos(Math.toRadians(yaw + 90)) * DisplayManager.getFrameTimeSeconds()* RUN_SPEED);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D))
        {
        	position.x -= (float) (Math.sin(Math.toRadians(yaw - 90)) * DisplayManager.getFrameTimeSeconds()*RUN_SPEED);
    		position.z += (float) (Math.cos(Math.toRadians(yaw -90)) * DisplayManager.getFrameTimeSeconds()* RUN_SPEED);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
        	jump();
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
        	position.y -= 10;
        	RUN_SPEED = 5;
        }else{
        	RUN_SPEED = 40;
        }
	     //calculateFirstCameraPosition();
	    // calculateYaw();
	}

	public void moveThirdPerson(){
		calculateZoom();
		thirdLook();
		float horizontalDistance = calculateHorizontalDistance();
		float verticalDistance = calculateVerticalDistance();
		calculateThirdCameraPosition(horizontalDistance,verticalDistance);
	}
	
	public void yaw(float amount) {

		yaw += amount;
	}

	public void pitch(float amount) {
		
		pitch += amount;
	}
	
	public void invertPitch(){
		this.pitch = -pitch;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public float getYaw() {
		return yaw;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public float getPitch() {
		return pitch;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public void lookThrough() {
		GL11.glRotatef(pitch, 1.0f, 0.0f, 0.0f);
		GL11.glRotatef(yaw, 0.0f, 1.0f, 0.0f);
		GL11.glTranslatef(position.x, position.y, position.z);
	}
	
	private void calculateThirdCameraPosition(float horizontalDistance, float verticalDistance){
		float theta = player.getRotY() + angleAroundPlayer;
		float offsetX = (float) (horizontalDistance * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (horizontalDistance * Math.cos(Math.toRadians(theta)));
		position.x = player.getPosition().x - offsetX;
		position.z = player.getPosition().z - offsetZ;
		position.y = player.getPosition().y + verticalDistance + 13; 
		this.yaw = 180 - (player.getRotY() + angleAroundPlayer);
	}
	
	private float calculateHorizontalDistance(){
		return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
		
	}
	
	private float calculateVerticalDistance(){
		return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
		
	}
		
	private void calculateZoom(){	
		float zoomLevel = Mouse.getDWheel() * 0.1f;
		distanceFromPlayer -= zoomLevel;
		if(distanceFromPlayer <= 10){
			distanceFromPlayer = 10;
			distanceFromPlayer -=zoomLevel;
		}
		if(distanceFromPlayer >= 200){
			distanceFromPlayer = 200;
			distanceFromPlayer -=zoomLevel;
		}

		
	}
	
	private void thirdLook(){
		if(Mouse.isButtonDown(0)){
			float pitchChange = Mouse.getDY() * 0.1f;
			if(Math.signum(pitchChange) == 1 & cameraIsAboveGround == true){
			pitch -= pitchChange;
			}else if(Math.signum(pitchChange) == -1){
				pitch -= pitchChange;
			}
			float angleChange = Mouse.getDX() * 0.3f;
			angleAroundPlayer -= angleChange;
		}
	}
	
	private void jump(){
		if(!isInAir){
			this.upwardSpeed = JUMP_POWER;
			isInAir = true;
		}
	}
	
	private void swim(){
			this.upwardSpeed = JUMP_POWER/2;
			isInAir = true;
	}
	
	private void increasePosition(float dx, float dy, float dz){
		this.position.x+=dx;
		this.position.y+=dy;
		this.position.z+=dz;
	}
	
	private void enableFrustumCulling(){
		double viewRayX = position.x + (Math.sin(Math.toRadians(yaw)));
		double viewRayY = position.y;
		double viewRayZ = position.z - (float) (Math.cos(Math.toRadians(yaw)));
		
		Vector3f viewRay = new Vector3f((int) viewRayX, (int) viewRayY, (int)viewRayZ);
		
		double nearDist = MasterRenderer.NEAR_PLANE;
		double farDist = MasterRenderer.FAR_PLANE;
		
		double Hnear = 2 * Math.tan(MasterRenderer.FOV / 2) * nearDist;
		double Wnear = Hnear * (MasterRenderer.FOV / 10);
		
		double Hfar = 2 * Math.tan(MasterRenderer.FOV / 2) * farDist;
		double Wfar = Hfar * (MasterRenderer.FOV / 10);
		
		Vector3f up = new Vector3f(pitch, 0.0f, 1.0f);
		up.normalise();
		
		Vector3f right = new Vector3f(up.x * viewRay.x, up.y * viewRay.y, up.z * viewRay.z);
		
		Vector3f fc = new Vector3f(position.x + viewRay.x * (float) farDist, position.y + viewRay.y * (float) farDist, position.z + viewRay.z * (float) farDist);
		
		Vector3f ftl = new Vector3f((float)(fc.x + (up.x * Hfar/2) - (right.x * Wfar/2)),(float)(fc.y + (up.y * Hfar/2) - (right.y * Wfar/2)),(float)(fc.z + (up.z * Hfar/2) - (right.z * Wfar/2)));
		Vector3f ftr = new Vector3f((float)(fc.x + (up.x * Hfar/2) + (right.x * Wfar/2)),(float)(fc.y + (up.y * Hfar/2) + (right.y * Wfar/2)),(float)(fc.z + (up.z * Hfar/2) + (right.z * Wfar/2)));
		Vector3f fbl = new Vector3f((float)(fc.x - (up.x * Hfar/2) - (right.x * Wfar/2)),(float)(fc.y - (up.y * Hfar/2) - (right.y * Wfar/2)),(float)(fc.z - (up.z * Hfar/2) - (right.z * Wfar/2)));
		Vector3f fbr = new Vector3f((float)(fc.x - (up.x * Hfar/2) + (right.x * Wfar/2)),(float)(fc.y - (up.y * Hfar/2) + (right.y * Wfar/2)),(float)(fc.z - (up.z * Hfar/2) + (right.z * Wfar/2)));
		
		Vector3f nc = new Vector3f(position.x + viewRay.x * (float) nearDist, position.y + viewRay.y * (float) nearDist, position.z + viewRay.z * (float) nearDist);
		
		Vector3f ntl = new Vector3f((float)(fc.x + (up.x * Hnear/2) - (right.x * Wnear/2)),(float)(fc.y + (up.y * Hnear/2) - (right.y * Wnear/2)),(float)(fc.z + (up.z * Hnear/2) - (right.z * Wnear/2)));
		Vector3f ntr = new Vector3f((float)(fc.x + (up.x * Hnear/2) + (right.x * Wnear/2)),(float)(fc.y + (up.y * Hnear/2) + (right.y * Wnear/2)),(float)(fc.z + (up.z * Hnear/2) + (right.z * Wnear/2)));
		Vector3f nbl = new Vector3f((float)(fc.x - (up.x * Hnear/2) - (right.x * Wnear/2)),(float)(fc.y - (up.y * Hnear/2) - (right.y * Wnear/2)),(float)(fc.z - (up.z * Hnear/2) - (right.z * Wnear/2)));
		Vector3f nbr = new Vector3f((float)(fc.x - (up.x * Hnear/2) + (right.x * Wnear/2)),(float)(fc.y - (up.y * Hnear/2) + (right.y * Wnear/2)),(float)(fc.z - (up.z * Hnear/2) + (right.z * Wnear/2)));
	}
	
	public void moveFirstPersonWater(Terrain terrain){
		RUN_SPEED = 3;
		GRAVITY = -30;
		upwardSpeed += ((GRAVITY) * DisplayManager.getFrameTimeSeconds()/2);
		increasePosition(0, 0 * DisplayManager.getFrameTimeSeconds(), 0);
		float terrainHeight = terrain.getHeightOfTerrain(position.x, position.z);
		if(position.y <terrainHeight + 13){
			upwardSpeed = 0;
			isInAir = false;
			position.y = terrainHeight + 13;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_W))
        {
			
			position.x += (float) (Math.sin(Math.toRadians(yaw)) * DisplayManager.getFrameTimeSeconds()* RUN_SPEED);
    		position.z -= (float) (Math.cos(Math.toRadians(yaw)) * DisplayManager.getFrameTimeSeconds()* RUN_SPEED);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S))
        {
        	position.x -= (float) (Math.sin(Math.toRadians(yaw)) * DisplayManager.getFrameTimeSeconds()* RUN_SPEED);
    		position.z += (float) (Math.cos(Math.toRadians(yaw)) * DisplayManager.getFrameTimeSeconds()* RUN_SPEED);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A))
        {
        	position.x -= (float) (Math.sin(Math.toRadians(yaw + 90)) * DisplayManager.getFrameTimeSeconds()* RUN_SPEED);
    		position.z += (float) (Math.cos(Math.toRadians(yaw + 90)) * DisplayManager.getFrameTimeSeconds()* RUN_SPEED);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D))
        {
        	position.x -= (float) (Math.sin(Math.toRadians(yaw - 90)) * DisplayManager.getFrameTimeSeconds()*RUN_SPEED);
    		position.z += (float) (Math.cos(Math.toRadians(yaw -90)) * DisplayManager.getFrameTimeSeconds()* RUN_SPEED);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
        	swim();
        }
	
	}
	}
