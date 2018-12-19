package engineTester;



import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRendering.TextMaster;
import guis.GuiTexture;
import models.RawModel;
import models.TexturedModel;
import normalMappingObjConverter.NormalMappedObjLoader;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import particles.ParticleMaster;
import particles.ParticleSystem;
import particles.ParticleTexture;
import postProcessing.Fbo;
import postProcessing.PostProcessing;
import renderEngine.DisplayManager;
import renderEngine.GuiRenderer;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import shaders.TerrainShader;
import skybox.SkyboxRenderer;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import time.Time;
import toolbox.MousePicker;
import water.WaterFrameBuffers;
import water.WaterRenderer;
import water.WaterShader;
import water.WaterTile;

public class MainGameLoop {
	private static final int SEED = new Random().nextInt(1000000000);
	public static int frameSecs = 0;
	public static int hour;
	public static int minute;
	
	static List<Entity> entities = new ArrayList<Entity>();
	static List<Entity> waterRenderedEntites = new ArrayList<Entity>();
	static List<Entity> normalMapEntities = new ArrayList<Entity>();
	
	static Loader loader = new Loader();

	
	public static void main(String[] args) {
		
		DisplayManager.createDisplay();	
		TextMaster.init(loader);
		ModelData characterData = OBJFileLoader.loadOBJ("ground");
		RawModel characterModel = loader.loadToVAO(characterData.getVertices(), characterData.getTextureCoords(), characterData.getVertices(), characterData.getIndices());
		TexturedModel characterTexturedModel = new TexturedModel(characterModel, new ModelTexture(loader.loadTexture("ground" + "/diffMap")));
		characterTexturedModel.getTexture().setUseFakeLighting(false);
		
		
		Player player = new Player(characterTexturedModel, new Vector3f(0, 0, 0), 0, 0, 0, 0.5f);
		Vector3f playerHeadPosition = new Vector3f(player.getPosition());
		Camera camera = new Camera(player);
		
		//FBOS
		WaterFrameBuffers fbos = new WaterFrameBuffers();
		
		MasterRenderer renderer = new MasterRenderer(loader, camera,fbos);
		ParticleMaster.init(loader, renderer.getProjectionMatrix());
		
		
		//*************************MODELS********************************
		normalMapEntities.add(new Entity(loadAdvancedModel("picnicTable", loader, false, false), new Vector3f(0,0,0),0,0,0,0.5f));
		normalMapEntities.add(new Entity(loadAdvancedModel("picnicBench", loader, false, false), new Vector3f(0,0,0), 0,0,0, 0.5f));
		normalMapEntities.add(new Entity(loadAdvancedModel("swingset", loader, true, true), new Vector3f(0,0,0),0,0,0,0.5f));
		normalMapEntities.add(new Entity(loadAdvancedModel("artificialPond", loader, false, false), new Vector3f(0,0,0), 0,0,0,0.5f));
		normalMapEntities.add(new Entity(loadAdvancedModel("lamp", loader, false, false), new Vector3f(0,0,0), 0,0,0,0.5f));
		normalMapEntities.add(new Entity(loadAdvancedModel("road", loader, false, false), new Vector3f(0,0,0), 0,0,0,0.5f));
		
		//entities.add(new Entity(loadModel("ground", loader, false, false), new Vector3f(0,0,0), 0,0,0,0.5f));
		entities.add(new Entity(loadModel("windmill", loader, false, false), new Vector3f(0,0,0), 0,0,0,0.5f));
		entities.add(new Entity(loadModel("gazebo", loader, true, false), new Vector3f(0,0,0), 0,0,0,0.5f));
		entities.add(new Entity(loadModel("trees1", loader, true, true), new Vector3f(0,0,0), 0,0,0,0.5f));
		entities.add(new Entity(loadModel("trees2", loader, true, true), new Vector3f(0,0,0), 0,0,0,0.5f));
		entities.add(new Entity(loadModel("trees3", loader, true, true), new Vector3f(0,0,0), 0,0,0,0.5f));
		entities.add(new Entity(loadModel("waterLily", loader, false, false), new Vector3f(0,0,0), 0,0,0,0.5f));
		
		//*****************Water***********************
		
	    WaterShader waterShader = new WaterShader();
		WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader, renderer.getProjectionMatrix(),fbos);
		List<WaterTile> waters = new ArrayList<WaterTile>();
		WaterTile waterTile = new WaterTile(160,-250,0);
		waters.add(waterTile);
				 
		 
		 //***************Particle System**********************
		 ParticleTexture particleFireTexture = new ParticleTexture(loader.loadTexture("fire"), 4);
		 particleFireTexture.additive = true;
		 ParticleSystem fireSystem = new ParticleSystem(particleFireTexture, 32,0,-0.0025f,10, 4f);
		 fireSystem.randomizeRotation();
		 fireSystem.setDirection(new Vector3f(0,0.25f,0), 0.5f);
		 fireSystem.setSpeedError(0.4f);
		 fireSystem.setScaleError(0.8f);
		 
		 ParticleTexture particleSmokeTexture = new ParticleTexture(loader.loadTexture("smoke"), 8);
		 ParticleSystem smokeSystem = new ParticleSystem(particleSmokeTexture, 32,1,-0.01f,20, 10f);
		 smokeSystem.randomizeRotation();
		 smokeSystem.setDirection(new Vector3f(0,1f,0), 1f);
		 smokeSystem.setSpeedError(0.4f);
		 smokeSystem.setScaleError(0.8f);
		 
		 
		
		//****************Character****************************
		
		entities.add(player);
		waterRenderedEntites.add(player);
		
		//*******************Lights***************************
		List<Light> lights = new ArrayList<Light>();
		Light sun = new Light(new Vector3f(-250,200, -200), new Vector3f(1f,0.875f,0.635f));
		lights.add(sun);
		
		
		
		
		//*******************GUI**************************
		GuiRenderer guiRenderer = new GuiRenderer(loader);
		List<GuiTexture> guis = new ArrayList<GuiTexture>();
		GuiTexture gameMenuLogo = new GuiTexture(loader.loadTexture("The Retrospect Logo"), new Vector2f(0.5f,0.5f), new Vector2f(-0.49f, 0.6f));
		GuiTexture shadowMap = new GuiTexture(renderer.getShadowMapTexture(), new Vector2f(0.5f,0.5f), new Vector2f(0.5f,0.5f));
		guis.add(gameMenuLogo);
		boolean isMenuUp = false;


		
		
		//*****************FONT*******************
		
		FontType font = new FontType(loader.loadTexture("candara"), new File("res/Textures/candara.fnt"));
		GUIText title = new GUIText("The RETROSPECT", 1, font, new Vector2f(0,0), 1f, true);
		GUIText version = new GUIText("Alpha Version .001", 1, font, new Vector2f(0,0.02f), 1f, true);

		
		
		
		//**********Camera Movement*********************
	    float mouseSensitivity = 0.05f;
	    boolean isFirstPerson = false;
	    boolean isGrabbed = false;
	    
		
		
	    
	    Time time = new Time();
	    TerrainShader tShader = new TerrainShader();
	    
	    
	    //*******************Post Processing****************
	    Fbo multisampledFBO = new Fbo(Display.getWidth(),Display.getHeight());
	    Fbo outputFBO = new Fbo(Display.getWidth(),Display.getHeight(),Fbo.DEPTH_TEXTURE);
	    Fbo outputFBO2 = new Fbo(Display.getWidth(),Display.getHeight(),Fbo.DEPTH_TEXTURE);

	    PostProcessing.init(loader);
	    
		while(!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
				//fireSystem.generateParticles(new Vector3f(0,0,0));
				//smokeSystem.generateParticles(new Vector3f(0,5,0));
				//ParticleMaster.update(camera);
			
			renderer.renderShadowMap(entities,normalMapEntities, sun);

			if(Keyboard.isKeyDown(Keyboard.KEY_Y)){
				System.out.println(camera.getPosition());
			}
			
			
			//*****************WATER*******************
			GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
			
			
			//Render to reflection
			fbos.bindReflectionFrameBuffer();
			float distance = 2* (camera.getPosition().y - waterTile.getHeight());
			camera.getPosition().y -= distance;
			camera.invertPitch();
			renderer.renderScene(entities, normalMapEntities, lights, camera, new Vector4f(0, 1, 0, -waterTile.getHeight()));
			ParticleMaster.update(camera);
			ParticleMaster.renderParticles(camera);
			camera.getPosition().y += distance;
			camera.invertPitch();
			fbos.unbindCurrentFrameBuffer();
			
			//Render to refraction
			fbos.bindRefractionFrameBuffer();
			renderer.renderScene(entities, normalMapEntities, lights, camera, new Vector4f(0, -1, 0 ,waterTile.getHeight()));
			ParticleMaster.update(camera);
			ParticleMaster.renderParticles(camera);
			fbos.unbindCurrentFrameBuffer();
			
			
			//*****************TIME********************
			frameSecs +=100;
			hour = time.getHour();
			minute = time.getMinute();
			int seconds = minute* 60;
			time.calculateMinute(frameSecs);
			time.formatTime();
			
			//render to screen
			multisampledFBO.bindFrameBuffer();
			renderer.renderScene(entities, normalMapEntities, lights, camera, new Vector4f(0,-1,0,100000));
			waterRenderer.render(waters, camera,sun );
			TextMaster.render();
			ParticleMaster.renderParticles(camera);
			multisampledFBO.unbindFrameBuffer();
			multisampledFBO.resolveToFBO(GL30.GL_COLOR_ATTACHMENT0, outputFBO);
			multisampledFBO.resolveToFBO(GL30.GL_COLOR_ATTACHMENT1, outputFBO2);
			PostProcessing.doPostProcessing(outputFBO.getColourTexture(), outputFBO2.getColourTexture());
			if(Keyboard.isKeyDown(Keyboard.KEY_M) && isMenuUp == false){
				isMenuUp = true;
				Mouse.setGrabbed(true);
			}else if(Keyboard.isKeyDown(Keyboard.KEY_M) && isMenuUp == true){
				isMenuUp = false;
				Mouse.setGrabbed(false);
			}
			
			if(isMenuUp){
				
				guiRenderer.render(guis);
			}
			DisplayManager.updateDisplay();
			
			//*****************Character******************
			if(Keyboard.isKeyDown(Keyboard.KEY_F5)){
			 	camera.stop = true;
				isFirstPerson = false;
				Mouse.setGrabbed(false);
		    }else if(Keyboard.isKeyDown(Keyboard.KEY_F6)){
		    	isFirstPerson = true;
		    	Mouse.setGrabbed(true);
		    }
			
			
			if(isFirstPerson){
		    	camera.setPosition(playerHeadPosition);
				camera.yaw(Mouse.getDX() * mouseSensitivity);
			    camera.pitch(-Mouse.getDY() * mouseSensitivity);
			    camera.moveFirstPerson();
			    
			    
		    }
		    else{
		    	
		    	//player.moveThirdPerson();
		    	camera.moveThirdPerson();
		    }
			
		}
		//*****************Clean Up*****************
		multisampledFBO.cleanUp();
		outputFBO.cleanUp();
		outputFBO2.cleanUp();
		PostProcessing.cleanUp();
		ParticleMaster.cleanUp();
		TextMaster.cleanUp();
		fbos.cleanUp();
		waterShader.cleanUp();
		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
		
	}
	
	private static TexturedModel loadModel(String fileName, Loader loader, Boolean hasTransparency,Boolean useFakeLighting){
		ModelData modelData = OBJFileLoader.loadOBJ(fileName);
		RawModel  rawModel = loader.loadToVAO(modelData.getVertices(), modelData.getTextureCoords(), modelData.getVertices(), modelData.getIndices());
		TexturedModel texturedModel = new TexturedModel(rawModel, new ModelTexture(loader.loadTexture(fileName + "/diffMap")));
		texturedModel.getTexture().setHasTransparency(hasTransparency);
		texturedModel.getTexture().setUseFakeLighting(useFakeLighting);
		return texturedModel;
	}
	
	private static TexturedModel loadAdvancedModel(String fileName, Loader loader, Boolean hasTransparency, Boolean useFakeLighting){
		TexturedModel texturedModel = new TexturedModel(NormalMappedObjLoader.loadOBJ(fileName, loader), new ModelTexture(loader.loadTexture(fileName + "/diffMap")));
		texturedModel.getTexture().setNormalMap(loader.loadTexture(fileName + "/normMap"));
		texturedModel.getTexture().setSpecularMap(loader.loadTexture(fileName + "/specMap"));
		texturedModel.getTexture().setHasTransparency(hasTransparency);
		texturedModel.getTexture().setUseFakeLighting(useFakeLighting);
		texturedModel.getTexture().setReflectivity(20);
		texturedModel.getTexture().setShineDamper(20.0f);
		return texturedModel;
		}
	}
	





