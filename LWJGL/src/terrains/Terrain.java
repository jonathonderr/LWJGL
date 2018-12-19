package terrains;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import models.RawModel;
import renderEngine.Loader;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.Maths;

public class Terrain {
	
	private static final float SIZE = 1000;
	public  float MAX_HEIGHT = 100;
	private static final float MAX_PIXEL_COLOR = 256 * 256 * 256;
	private static final int VERTEX_COUNT = 256;
	private static final int SEED = new Random().nextInt(1000000000);
	
	public boolean generateTerrain;
	
	private float x;
	private float z;
	private RawModel model;
	private TerrainTexturePack texturePack;
	private TerrainTexture blendMap;
	private HeightsGenerator generator;
	
	private float[][] heights;
	
	public Terrain(int gridX, int gridZ, Loader loader,TerrainTexturePack texturePack, TerrainTexture blendMap, String heightMap, boolean generateTerrain)
	{
		this.texturePack = texturePack;
		this.blendMap = blendMap;
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
		generator = new HeightsGenerator(gridX,gridZ, VERTEX_COUNT, SEED);
		this.generateTerrain = generateTerrain;
		this.model = generateTerrain(loader, heightMap, generateTerrain);
	}
	
	
	
	public float getX() {
		return x;
	}



	public float getZ() {
		return z;
	}



	public RawModel getModel() {
		return model;
	}


	public TerrainTexturePack getTexturePack() {
		return texturePack;
	}



	public TerrainTexture getBlendMap() {
		return blendMap;
	}
	
	
	
	public float getHeightOfTerrain(float worldX, float worldZ){
		float terrainX = worldX - this.x;
		float terrainZ = worldZ - this.z;
		float gridSquareSize = SIZE/ ((float)heights.length - 1); 
		int gridX = (int) Math.floor(terrainX / gridSquareSize);
		int gridZ = (int) Math.floor(terrainZ / gridSquareSize);
		if(gridX >= heights.length - 1 || gridZ >= heights.length  -1){
			return 0;
		}
		
		float xCoord = (terrainX % gridSquareSize)/gridSquareSize;
		float zCoord = (terrainZ % gridSquareSize)/gridSquareSize;
		float answer;
		if (xCoord <= (1-zCoord)) {
			answer = Maths
					.barryCentric(new Vector3f(0, heights[gridX][gridZ], 0), new Vector3f(1,
							heights[gridX + 1][gridZ], 0), new Vector3f(0,
							heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		} else {
			answer = Maths
					.barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(1,
							heights[gridX + 1][gridZ + 1], 1), new Vector3f(0,
							heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		}
		
		return answer;

	}
	
	public static Terrain getTerrain(List<Terrain> terrains, float worldX, float worldZ) {
		for(Terrain terrain : terrains) {
			float terrainX = worldX - terrain.getX();
			float terrainZ = worldZ - terrain.getZ();
			float gridSquareSize = Terrain.SIZE / ((float) terrain.getModel().getVertexCount() - 1f);
			int gridX = (int) Math.floor(terrainX / gridSquareSize);
			int gridZ = (int) Math.floor(terrainZ / gridSquareSize);
			if(!(gridX < 0 || gridX >= terrain.getModel().getVertexCount() - 1 || gridZ < 0 || gridZ >= terrain.getModel().getVertexCount() - 1))
				System.out.println(terrain);
				return terrain;
		}
		
		return null;
	}



	private RawModel generateTerrain(Loader loader, String heightMap, boolean generateTerrain){
		
		HeightsGenerator generator = new HeightsGenerator();
		
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File("res/Textures/" + heightMap +".png"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(generateTerrain){
		int VERTEX_COUNT = 128;
		} else{
			int VERTEX_COUNT = image.getHeight();
		}
		
		int count = VERTEX_COUNT * VERTEX_COUNT;
		heights = new float[VERTEX_COUNT][VERTEX_COUNT];
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count*2];
		int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT*1)];
		int vertexPointer = 0;
		for(int i=0;i<VERTEX_COUNT;i++){
			for(int j=0;j<VERTEX_COUNT;j++){
				vertices[vertexPointer*3] = (float)j/((float)VERTEX_COUNT - 1) * SIZE;
				float height = getHeight(j, i, generator, image);
				heights[j][i] = height;
				vertices[vertexPointer*3+1] = height;
				vertices[vertexPointer*3+2] = (float)i/((float)VERTEX_COUNT - 1) * SIZE;
				Vector3f normal = calculateNormal(j,i, generator, image);
				normals[vertexPointer*3] = normal.x;
				normals[vertexPointer*3+1] = normal.y;
				normals[vertexPointer*3+2] = normal.z;
				textureCoords[vertexPointer*2] = (float)j/((float)VERTEX_COUNT - 1);
				textureCoords[vertexPointer*2+1] = (float)i/((float)VERTEX_COUNT - 1);
				vertexPointer++;
			}
		}
		int pointer = 0;
		for(int gz=0;gz<VERTEX_COUNT-1;gz++){
			for(int gx=0;gx<VERTEX_COUNT-1;gx++){
				int topLeft = (gz*VERTEX_COUNT)+gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz+1)*VERTEX_COUNT)+gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		return loader.loadToVAO(vertices, textureCoords, normals, indices);
	}
	
	private Vector3f calculateNormal(int x, int z, HeightsGenerator generator, BufferedImage image){
		float heightL = getHeight(x-1, z, generator,image);
		float heightR = getHeight(x+1, z, generator,image);
		float heightD = getHeight(x, z-1, generator,image); 
		float heightU = getHeight(x, z+1, generator,image); 
		Vector3f normal = new Vector3f(heightL - heightR,2f, heightD - heightU);
		normal.normalise();
		return normal;
	}
	
	private float getHeight(int x, int z, HeightsGenerator generator, BufferedImage image){
		if(generateTerrain){
		return generator.generateHeight(x, z);
		}else{
			if(x<0 || x>=image.getHeight() || z<0 || z>image.getHeight()){
				return 0;
			}
			float height = image.getRGB(x,z);
			height += MAX_PIXEL_COLOR/2f;
			height /= MAX_PIXEL_COLOR/2f;
			height *= MAX_HEIGHT;
			return height;
		}
	}
}
