package renderEngine;

import java.util.List;
import java.util.Map;

import models.RawModel;
import models.TexturedModel;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import shaders.StaticShader;
import skybox.CubeMap;
import skybox.SkyboxRenderer;
import textures.ModelTexture;
import toolbox.Maths;
import water.WaterFrameBuffers;
import engineTester.MainGameLoop;
import entities.Entity;

public class EntityRenderer {

	private StaticShader shader;
	private WaterFrameBuffers fbos;
	private SkyboxRenderer skybox;
	private Loader loader;
	private CubeMap cubeMap;
	
    private static String[] ENVIROMAP_SNOW = { "posx", "negx", "posy", "negy", "posz", "negz" };

	
	public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix, WaterFrameBuffers fbos, CubeMap environmentMap){
		this.fbos = fbos;
		this.shader = shader;
		this.loader = new Loader();
		this.skybox = new SkyboxRenderer(loader, projectionMatrix);
		this.cubeMap = environmentMap;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.connectTextureUnits();
		shader.stop();
	}
	
	public void bindEnviromentMap(){
		GL13.glActiveTexture(GL13.GL_TEXTURE8);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP,cubeMap.getTexture());
	}
	
	
	public void render(Map<TexturedModel, List<Entity>> entities, Matrix4f toShadowMapSpace){
		shader.loadToShadowMapSpaceMatrix(toShadowMapSpace);
		for(TexturedModel model:entities.keySet()){
			prepareTexturedModel(model);
			List<Entity> batch = entities.get(model);
			for(Entity entity:batch){
				prepareInstance(entity);
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			unbindTexturedModel();
		}
	}
	
	private void prepareTexturedModel(TexturedModel model){
		RawModel rawModel = model.getRawModel();
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		ModelTexture texture = model.getTexture();
		shader.loadNumberOfRows(texture.getNumberOfRow());
		if(texture.isHasTransparency()){
			MasterRenderer.disableCulling();
		}
		shader.loadFakeLightingVariable(texture.isUseFakeLighting());
		//shader.loadUnderwater(MainGameLoop.isUnderwater);
		shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getId());
		shader.loadUseSpecularMap(texture.hasSpecularMap());
		bindEnviromentMap();
		if(texture.hasSpecularMap()){
			GL13.glActiveTexture(GL13.GL_TEXTURE1);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getSpecularMap());
			GL13.glActiveTexture(GL13.GL_TEXTURE6);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getReflectionTexture());
			GL13.glActiveTexture(GL13.GL_TEXTURE7);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getRefractionTexture());
			
		}
		
	}
	
	private void unbindTexturedModel(){
		MasterRenderer.enableCulling();
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	
	private void prepareInstance(Entity entity){
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
		shader.loadOffset(entity.getTextureXOffset(), entity.getTextureZOffset());
	}
	
}