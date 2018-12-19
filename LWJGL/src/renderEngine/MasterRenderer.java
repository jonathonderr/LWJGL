package renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.TexturedModel;
import normalMappingRenderer.NormalMappingRenderer;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

import shaders.StaticShader;
import shaders.TerrainShader;
import shadows.ShadowMapMasterRenderer;
import skybox.CubeMap;
import skybox.SkyboxRenderer;
import terrains.Terrain;
import textures.ModelTexture;
import water.WaterFrameBuffers;
import entities.Camera;
import entities.Entity;
import entities.Light;

public class MasterRenderer {
	
	public static final float FOV = 70;
	public static final float NEAR_PLANE = 1f;
	public static final float FAR_PLANE = 10000;
	
	public static float RED = 192/255f;
	public static float BLUE = 192/255f;
	public static float GREEN = 192/255f;
	
	private Matrix4f projectionMatrix;
	
	private StaticShader shader = new StaticShader();
	private EntityRenderer renderer;
	private ModelTexture modelTexture = new ModelTexture(0);
	
	private TerrainRenderer terrainRenderer;
	private TerrainShader terrainShader = new TerrainShader();
	
	private NormalMappingRenderer normalMapRenderer;
	
    private static String[] ENVIROMAP_SNOW = { "posx", "negx", "posy", "negy", "posz", "negz" };
	
	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	private Map<TexturedModel, List<Entity>> normalMapEntities = new HashMap<TexturedModel, List<Entity>>();

	
	private List<Terrain> terrains = new ArrayList<Terrain>();
	
	private SkyboxRenderer skyboxRenderer;
	private ShadowMapMasterRenderer shadowRenderer;
	
	public MasterRenderer(Loader loader, Camera cam, WaterFrameBuffers fbos){
		enableCulling();
		createProjectionMatrix();
		terrainRenderer = new TerrainRenderer(terrainShader,projectionMatrix);
		CubeMap enviroMap = new CubeMap(ENVIROMAP_SNOW, loader);
		renderer = new EntityRenderer(shader,projectionMatrix, fbos, enviroMap);
		skyboxRenderer = new SkyboxRenderer(loader ,projectionMatrix);
		normalMapRenderer = new NormalMappingRenderer(projectionMatrix, fbos, enviroMap);
		this.shadowRenderer = new ShadowMapMasterRenderer(cam);
	}
	
	
	
	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}
	
	public void renderScene(List<Entity> entities, List<Entity> normalMapEntities, List<Terrain> terrains, List<Light> lights, Camera camera, Vector4f clipPlane){
		for(Terrain terrain: terrains){
			processTerrain(terrain);
		}
		for (Entity entity: entities){
			processEntity(entity);
		}
		for (Entity entity: normalMapEntities){
			processNormalMapEntity(entity);
		}
		render(lights,camera,clipPlane);
	}
	
	public void renderScene(List<Entity> entities, List<Entity> normalMapEntities, List<Light> lights, Camera camera, Vector4f clipPlane){
		for (Entity entity: entities){
			processEntity(entity);
		}
		for (Entity entity: normalMapEntities){
			processNormalMapEntity(entity);
		}
		render(lights,camera,clipPlane);
	}

	public static void enableCulling(){
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
	
	public static void disableCulling(){
		GL11.glDisable(GL11.GL_CULL_FACE);
	}
	
	public void render(List<Light> lights, Camera camera, Vector4f clipPlane){
		prepare();
		skyboxRenderer.render(camera, RED, GREEN, BLUE);
		shader.start();
		shader.loadClipPlane(clipPlane);
		shader.loadSkyColor(RED, GREEN, BLUE);
		shader.loadLights(lights);
		shader.loadViewMatrix(camera);
		renderer.bindEnviromentMap();
		renderer.render(entities, shadowRenderer.getToShadowMapSpaceMatrix());
		shader.stop();
		normalMapRenderer.render(normalMapEntities, clipPlane, lights, camera,shadowRenderer.getToShadowMapSpaceMatrix());
		terrainShader.start();
		terrainShader.loadSkyColor(RED, GREEN, BLUE);
		terrainShader.loadLights(lights);
		terrainShader.loadViewMatrix(camera);
		terrainShader.loadClipPlane(clipPlane);
		terrainRenderer.render(terrains, shadowRenderer.getToShadowMapSpaceMatrix());
		terrainShader.stop();
		terrains.clear();
		entities.clear();
		normalMapEntities.clear();
	}
	
	public void processTerrain(Terrain terrain){
		terrains.add(terrain);
	}
	
	public void processEntity(Entity entity)
	{
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);
		if(batch!=null){
			batch.add(entity);
		}else{
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
	}
	
	public void processNormalMapEntity(Entity entity)
	{
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = normalMapEntities.get(entityModel);
		if(batch!=null){
			batch.add(entity);
		}else{
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			normalMapEntities.put(entityModel, newBatch);
		}
	}
	
	public int getShadowMapTexture(){
		return shadowRenderer.getShadowMap();
	}
	
	public void renderShadowMap(List<Entity> entityList, List<Entity> normalMappedEntities, Light sun){
		  for(Entity entity : entityList){
		   processEntity(entity);
		  }
		  for(Entity entity : normalMappedEntities){
		   processEntity(entity);
		  }
		  shadowRenderer.render(entities, sun);
		  entities.clear();
		 }
	
	public void cleanUp(){
		shader.cleanUp();
		terrainShader.cleanUp();
		normalMapRenderer.cleanUp();
		shadowRenderer.cleanUp();
	}
	
	public void prepare(){
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT); 
		GL11.glClearColor(RED, GREEN, BLUE, 1);
		GL13.glActiveTexture(GL13.GL_TEXTURE5);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, getShadowMapTexture());
	}
	
	
	private void createProjectionMatrix(){
		projectionMatrix = new Matrix4f();
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))));
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;

		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;
	}



	public static void setRED(float rED) {
		RED = rED;
	}



	public static void setBLUE(float bLUE) {
		BLUE = bLUE;
	}



	public static void setGREEN(float gREEN) {
		GREEN = gREEN;
	}
	
	

}
