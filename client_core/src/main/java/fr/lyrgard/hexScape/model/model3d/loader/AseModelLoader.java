package fr.lyrgard.hexScape.model.model3d.loader;

/*
 * Copyright (c) 2003, jMonkeyEngine - Mojo Monkey Coding All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * 
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * Neither the name of the Mojo Monkey Coding, jME, jMonkey Engine, nor the
 * names of its contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *  
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.util.BufferUtils;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.model.model3d.AseExternalModel;
import fr.lyrgard.hexScape.model.model3d.AseExternalModel.MeshMaterialPair;
import fr.lyrgard.hexScape.model.model3d.ExternalModel;


/**
 * <code>ASEModel</code> defines a model using the ASE model format. 
 * This loader builds the mesh of the model but currently does not
 * build any animations defined for the format. Therefore, if a 
 * call to <code>getAnimationController</code> is made, null will
 * be returned.
 * 
 * @author Mark Powell
 * @version $Id: ASEModel.java,v 1.5 2004-02-15 20:22:39 mojomonkey Exp $
 */
public class AseModelLoader extends AbstractModelLoader {
	
	private static final Logger LOGGER = Logger.getLogger(AseModelLoader.class.getCanonicalName());

	//ASE file tags.
	private static final String OBJECT = "*GEOMOBJECT";
	private static final String NUM_VERTEX = "*MESH_NUMVERTEX";
	private static final String NUM_FACES = "*MESH_NUMFACES";
	private static final String NUM_TVERTEX = "*MESH_NUMTVERTEX";
	private static final String VERTEX = "*MESH_VERTEX";
	private static final String FACE = "*MESH_FACE";
//	private static final String NORMALS = "*MESH_NORMALS";
//	private static final String FACE_NORMAL = "*MESH_FACENORMAL";
	private static final String NVERTEX = "*MESH_VERTEXNORMAL";
	private static final String TVERTEX = "*MESH_TVERT";
	private static final String TFACE = "*MESH_TFACE";
	private static final String TEXTURE = "*BITMAP";
	private static final String UTILE = "*UVW_U_TILING";
	private static final String VTILE = "*UVW_V_TILING";
//	private static final String UOFFSET = "*UVW_U_OFFSET";
//	private static final String VOFFSET = "*UVW_V_OFFSET";
	private static final String MATERIAL_ID = "*MATERIAL_REF";
	private static final String MATERIAL_COUNT = "*MATERIAL_COUNT";
	private static final String MATERIAL = "*MATERIAL";
	private static final String MATERIAL_NAME = "*MATERIAL_NAME";
	private static final String MATERIAL_DIFFUSE = "*MATERIAL_DIFFUSE";
	private static final String MATERIAL_AMBIENT = "*MATERIAL_AMBIENT";
	private static final String MATERIAL_SPECULAR = "*MATERIAL_SPECULAR";
	private static final String MATERIAL_SHINE = "*MATERIAL_SHINE";

	
//	private String absoluteFilePath;
//	private BufferedReader reader = null;
//	private StringTokenizer tokenizer;
//	private String fileContents;
//	private File file;

//	private int numOfObjects; // The number of objects in the model
//	private int numOfMaterials; // The number of materials for the model
//	private ArrayList<ASEMaterialInfo> materials = new ArrayList<ASEMaterialInfo>();
//	private ArrayList<ASEObject> objectList = new ArrayList<ASEObject>();


	public AseModelLoader() {
		super();
	}
	
	public boolean canLoad(String name) {
		File file = getAseFile(name);
		return file != null && file.exists();
	}
	
	private File getAseFile(String name) {
		File file = null;
		for (File folder : getModelsFolders()) {
			File potentialFile = new File(new File(folder, name), name + ".ase");
			if (potentialFile.exists()) {
				file = potentialFile;
			}
		}
		return file;
	}
	
	public ExternalModel load(String name) {
		AseExternalModel model = new AseExternalModel();
		
		File aseFile = getAseFile(name);
		String fileContents = null;
		ArrayList<ASEObject> objectList = new ArrayList<ASEObject>();
	
		try {
			BufferedReader reader = new BufferedReader(new FileReader(aseFile));

			StringBuffer fc = new StringBuffer();

			String line;
			while ((line = reader.readLine()) != null) {
				fc.append(line + "\n");
			}

			fileContents = fc.toString();
			reader.close();
			
			int numOfObjects = getObjectCount(fileContents);
			int numOfMaterials = getMaterialCount(fileContents);
			ArrayList<ASEMaterialInfo> materials = new ArrayList<ASEMaterialInfo>();
			

			parseFile(numOfObjects, numOfMaterials, objectList, materials, fileContents, name);
			//computeNormals(numOfObjects, objectList);
		} catch (IOException e) {
			LOGGER.log(
				Level.WARNING,
				"Could not load " + name);
		}
	
		model.setName(name);
		for (ASEObject object : objectList) {
			MeshMaterialPair pair = new MeshMaterialPair(object.getMesh(), object.getMaterial());
			model.getMeshMaterialPairs().add(pair);
		}
		return model;
	}
	
	/**
	 * 
	 * <code>parseFile</code> reads the file contents. First, the
	 * number of materials and objects are read, then each material
	 * is read and each object is read.
	 *
	 */
	private void parseFile(int numOfObjects, int numOfMaterials, ArrayList<ASEObject> objectList, ArrayList<ASEMaterialInfo> materials, String fileContents, String name) {
		ASEMaterialInfo textureInfo = new ASEMaterialInfo();

		//Build texture list (not sure if this makes since, there can only be
		//one texture per mesh, and the are reading it in for the entire
		//object, not on a per object basis.
		for (int i = 0; i < numOfMaterials; i++) {
			materials.add(textureInfo);

			getMaterialInfo((ASEMaterialInfo) materials.get(i), i + 1, fileContents);
		}

		
		for (int i = 0; i < numOfObjects; i++) {	
			ASEObject mesh = new ASEObject(name);
			mesh.materialID = -1;
			moveToObject(i + 1, fileContents);
			readObjectInfo(mesh, i + 1, fileContents);
			readObjectData(mesh, i + 1, materials, fileContents);
			objectList.add(mesh);
		}

	}

	/**
	 * 

		for (int j = 0; j < numOfMaterials; j++) {
			ASEMaterialInfo mat = materials.get(j);
//			if (mat.file.length() > 0) {
//				MaterialState ms =
//					DisplaySystem
//						.getDisplaySystem()
//						.getRenderer()
//						.getMaterialState();
//				ms.setEnabled(true);
//				ms.setAmbient(
//					new ColorRGBA(
//						mat.ambient[0],
//						mat.ambient[1],
//						mat.ambient[2],
//						1));
//				ms.setDiffuse(
//					new ColorRGBA(
//						mat.diffuse[0],
//						mat.diffuse[1],
//						mat.diffuse[2],
//						1));
//				ms.setSpecular(
//					new ColorRGBA(
//						mat.specular[0],
//						mat.specular[1],
//						mat.specular[2],
//						1));
//				ms.setEmissive(new ColorRGBA(0, 0, 0, 1));
//				ms.setShininess(mat.shine);
//				this.setRenderState(ms);
//			}
		}

		for (int j = 0; j < numOfMaterials; j++) {
			// Check if the current material has a file name
			if ((materials.get(j)).file.length()
				> 0) {
//				TextureState ts =
//					DisplaySystem
//						.getDisplaySystem()
//						.getRenderer()
//						.getTextureState();
//				ts.setEnabled(true);
//				ts.setTexture(
//					TextureManager.loadTexture(
//						((ASEModel.ASEMaterialInfo) materials.get(j)).file,
//						Texture.MM_LINEAR,
//						Texture.FM_LINEAR,
//						true));
//				this.setRenderState(ts);
			}

		}
	}

	/**
	 * 
	 * <code>getObjectCount</code> counts the number of Geomobject entries
	 * in the ASE file. This count is then returned.
	 * @return the number of Geomobject entries.
	 */
	private int getObjectCount(String fileContents) {
		int objectCount = 0;
		StringTokenizer tokenizer = new StringTokenizer(fileContents);

		while (tokenizer.hasMoreTokens()) {
			// Check if we hit the start of an object
			if (OBJECT.equals(tokenizer.nextToken())) {
				objectCount++;
			}
		}

		return objectCount;
	}

	/**
	 * 
	 * <code>getMaterialCount</code> retrieves the number of materials in the
	 * ASE file. The file is read until the *MATERIAL flag is encountered. Once
	 * this flag is found, the value is read.
	 * 
	 * @return the number of materials as defined in the ASE file.
	 */
	private int getMaterialCount(String fileContents) {
		int materialCount = 0;

		// Go to the beginning of the file
		StringTokenizer tokenizer = new StringTokenizer(fileContents);

		// GO through the whole file until we hit the end
		while (tokenizer.hasMoreTokens()) {
			if (MATERIAL_COUNT.equals(tokenizer.nextToken())) {
				materialCount = Integer.parseInt(tokenizer.nextToken());
				return materialCount;
			}
		}

		//Material tag never found
		return 0;
	}

	/**
	 * 
	 * <code>getMaterialInfo</code> reads the data for a given material
	 * entry in the file. The material state information is read and 
	 * set as well as the texture state information.
	 * @param material the material structure to store into.
	 * @param desiredMaterial the material to load from the file.
	 */
	private void getMaterialInfo(
		ASEMaterialInfo material,
		int desiredMaterial,
		String fileContents) {
		String strWord;
		int materialCount = 0;

		// Go to the beginning of the file
		StringTokenizer tokenizer = new StringTokenizer(fileContents);

		//read through the file until the correct material entry is found.
		while (tokenizer.hasMoreTokens()) {
			if (MATERIAL.equals(tokenizer.nextToken())) {
				materialCount++;

				// Check if it's the one we want to stop at, if so break
				if (materialCount == desiredMaterial)
					break;
			}
		}

		while (tokenizer.hasMoreTokens()) {
			strWord = tokenizer.nextToken();

			if (strWord.equals(MATERIAL)) {
				return;
			}

			//read material properites.
			if (strWord.equals(MATERIAL_AMBIENT)) {
				material.ambient[0] = Float.parseFloat(tokenizer.nextToken());
				material.ambient[1] = Float.parseFloat(tokenizer.nextToken());
				material.ambient[2] = Float.parseFloat(tokenizer.nextToken());
			} else if (strWord.equals(MATERIAL_DIFFUSE)) {
				material.diffuse[0] = Float.parseFloat(tokenizer.nextToken());
				material.diffuse[1] = Float.parseFloat(tokenizer.nextToken());
				material.diffuse[2] = Float.parseFloat(tokenizer.nextToken());
			} else if (strWord.equals(MATERIAL_SPECULAR)) {
				material.specular[0] = Float.parseFloat(tokenizer.nextToken());
				material.specular[1] = Float.parseFloat(tokenizer.nextToken());
				material.specular[2] = Float.parseFloat(tokenizer.nextToken());
			} else if (strWord.equals(MATERIAL_SHINE)) {
				//material.shine = Float.parseFloat(tokenizer.nextToken());
			}

			//read texture information.
			if (strWord.equals(TEXTURE)) {
				//material.file = tokenizer.nextToken().replace('"', ' ').trim();
			} else if (strWord.equals(MATERIAL_NAME)) {
				//material.name = tokenizer.nextToken();
			} else if (strWord.equals(UTILE)) {
				material.uTile = Float.parseFloat(tokenizer.nextToken());
			} else if (strWord.equals(VTILE)) {
				material.vTile = Float.parseFloat(tokenizer.nextToken());
			}
		}
	}

	/**
	 * 
	 * <code>moveToObject</code> moves the file pointer to a specific 
	 * GEOMOBJECT entry in the ase file.
	 * @param desiredObject the object number to move to.
	 */
	private StringTokenizer moveToObject(int desiredObject, String fileContents) {
		int objectCount = 0;

		StringTokenizer tokenizer = new StringTokenizer(fileContents);

		while (tokenizer.hasMoreTokens()) {
			if (OBJECT.equals(tokenizer.nextToken())) {
				objectCount++;

				if (objectCount == desiredObject)
					return tokenizer;
			}
		}
		return null;
	}

	/**
	 * 
	 * <code>readObjectInfo</code> reads the mesh information defined by
	 * the GEOMOBJECT entry in the file. This information is kept in the
	 * ASEObject class until it is ready to be converted to a TriMesh.
	 * @param currentObject the object to store the data in.
	 * @param desiredObject the object to read.
	 */
	private void readObjectInfo(ASEObject currentObject, int desiredObject, String fileContents) {
		String word;

		StringTokenizer tokenizer = moveToObject(desiredObject, fileContents);

		while (tokenizer.hasMoreTokens()) {
			word = tokenizer.nextToken();

			if (word.equals("*NODE_NAME")) {
				currentObject.setName(tokenizer.nextToken().replaceAll("\"", ""));
			}

			if (word.equals(NUM_VERTEX)) {
				int numOfVerts = Integer.parseInt(tokenizer.nextToken());
				Vector3f[] verts = new Vector3f[numOfVerts];
				currentObject.setVertices(verts);
				currentObject.setNormals(new Vector3f[numOfVerts]);
			} else if (word.equals(NUM_FACES)) {
				int numOfFaces = Integer.parseInt(tokenizer.nextToken());
				currentObject.faces = new Face[numOfFaces];
			} else if (word.equals(NUM_TVERTEX)) {
				int numTexVertex = Integer.parseInt(tokenizer.nextToken());

				currentObject.tempTexVerts = new Vector2f[numTexVertex];
			} else if (word.equals(OBJECT)) {
				return;
			}
		}
	}

	/**
	 * 
	 * <code>readObjectData</code> reads each bit of data defined by a
	 * GEOMOBJECT. Namely, material id, vertices, texture vertices, faces,
	 * texture faces, texture file, u and v tiling.
	 * @param currentObject the object to store the information in.
	 * @param desiredObject the object to read.
	 */
	private void readObjectData(ASEObject currentObject, int desiredObject, ArrayList<ASEMaterialInfo> materials, String fileContents) {
		// Load the material ID for this object
		getData(currentObject, MATERIAL_ID, desiredObject, materials, fileContents);

		// Load the vertices for this object
		getData(currentObject, VERTEX, desiredObject, materials, fileContents);

		// Load the texture coordinates for this object
		getData(currentObject, TVERTEX, desiredObject, materials, fileContents);

		// Load the vertex faces list for this object
		getData(currentObject, FACE, desiredObject, materials, fileContents);

		// Load the texture face list for this object
		getData(currentObject, TFACE, desiredObject, materials, fileContents);

		// Load the texture for this object
		getData(currentObject, TEXTURE, desiredObject, materials, fileContents);

		// Load the U tile for this object
		getData(currentObject, UTILE, desiredObject, materials, fileContents);

		// Load the V tile for this object
		getData(currentObject, VTILE, desiredObject, materials, fileContents);
		
		// Load the VERTEX NORMAL for this object
		getData(currentObject, NVERTEX, desiredObject, materials, fileContents);
	}

	/**
	 * 
	 * <code>getData</code> reads a specified bit of data out of a GEOMOBECT
	 * entry in the ase file. 
	 * @param currentObject the object to save the data in.
	 * @param desiredData the object type to read.
	 * @param desiredObject the object to read.
	 */
	private void getData(
		ASEObject currentObject,
		String desiredData,
		int desiredObject,
		ArrayList<ASEMaterialInfo> materials,
		String fileContents) {
		String word;

		StringTokenizer tokenizer = moveToObject(desiredObject, fileContents);

		// Go through the file until we reach the end
		while (tokenizer.hasMoreTokens()) {
			word = tokenizer.nextToken();

			// If we reached an object tag, stop read because we went to far
			if (word.equals(OBJECT)) {
				// Stop reading because we are done with the current object
				return;
			}
			// If we hit a vertex tag
			else if (word.equals(VERTEX)) {
				// Make sure that is the data that we want to read in
				if (desiredData.equals(VERTEX)) {
					// Read in a vertex
					readVertex(currentObject, tokenizer);
				}
			}
			// If we hit a texture vertex
			else if (word.equals(TVERTEX)) {
				// Make sure that is the data that we want to read in
				if (desiredData.equals(TVERTEX)) {
					// Read in a texture vertex
					if (currentObject.materialID != -1) {
						readTextureVertex(currentObject,materials.get(currentObject.materialID), tokenizer);
					} else {
						readTextureVertex(currentObject,null, tokenizer);
					}
				}
			}
			// If we hit a vertice index to a face
			else if (word.equals(FACE)) {
				// Make sure that is the data that we want to read in
				if (desiredData.equals(FACE)) {
					// Read in a face
					readFace(currentObject, tokenizer);
				}
			}
			// If we hit a texture index to a face
			else if (word.equals(TFACE)) {
				// Make sure that is the data that we want to read in
				if (desiredData.equals(TFACE)) {
					// Read in a texture indice for a face
					readTextureFace(currentObject, tokenizer);
				}
			}
			// If we hit the material ID to the object
			else if (word.equals(MATERIAL_ID)) {
				// Make sure that is the data that we want to read in
				if (desiredData.equals(MATERIAL_ID)) {
					// Read in the material ID assigned to this object
					currentObject.materialID =
						(int) Float.parseFloat(tokenizer.nextToken());
					return;
				}
			}
//			// If we hit the normal to vertex
//			else if (word.equals(NVERTEX)) {
//				// Make sure that is the data that we want to read in
//				if (desiredData.equals(NVERTEX)) {
//					// Read in the material ID assigned to this object
//					readVertexNormal(currentObject, tokenizer);
//				}
//			}
		}
	}

	/**
	 * 
	 * <code>readVertex</code> reads the vertices information from a 
	 * GEOMOBJECT entry. Some converting is required to get the 
	 * coordinate axes into the default jme axes.
	 * @param currentObject the object to start the vertex in.
	 */
	private void readVertex(ASEObject currentObject, StringTokenizer tokenizer) {
		// Read past the vertex index
		int index = Integer.parseInt(tokenizer.nextToken());
		float x = Float.parseFloat(tokenizer.nextToken());
		float z = -Float.parseFloat(tokenizer.nextToken());
		float y = Float.parseFloat(tokenizer.nextToken());
		
		
		Vector3f vector = new Vector3f(x, y , z);
		currentObject.getVertices()[index] = vector;

	}
	
//	/**
//	 * 
//	 * <code>readVertex</code> reads the vertices information from a 
//	 * GEOMOBJECT entry. Some converting is required to get the 
//	 * coordinate axes into the default jme axes.
//	 * @param currentObject the object to start the vertex in.
//	 */
//	private void readVertexNormal(ASEObject currentObject, StringTokenizer tokenizer) {
//		// Read past the vertex index
//		int index = Integer.parseInt(tokenizer.nextToken());
//		float x = Float.parseFloat(tokenizer.nextToken());
//		float z = -Float.parseFloat(tokenizer.nextToken());
//		float y = Float.parseFloat(tokenizer.nextToken());
//
//
//		Vector3f vector = new Vector3f(x, y , z);
//		currentObject.getNormals()[index] = vector;
//	}

	/**
	 * 
	 * <code>readTextureVertex</code> reads in a single texture coordinate
	 * from the ase file.
	 * @param currentObject the object that has the coordinate.
	 * @param texture the object that defines the texture.
	 */
	private void readTextureVertex(
		ASEObject currentObject,
		ASEMaterialInfo texture,
		StringTokenizer tokenizer) {
		int index = 0;

		// Here we read past the index of the texture coordinate
		index = Integer.parseInt(tokenizer.nextToken());
		currentObject.tempTexVerts[index] = new Vector2f();

		// Next, we read in the (U, V) texture coordinates.
		currentObject.tempTexVerts[index].x = Float.parseFloat(tokenizer.nextToken());
		currentObject.tempTexVerts[index].y = Float.parseFloat(tokenizer.nextToken());

		if (texture != null) {
			currentObject.tempTexVerts[index].x *= texture.uTile;
			currentObject.tempTexVerts[index].y *= texture.vTile;
		}

	}

	/**
	 * 
	 * <code>readFace</code> reads the face of a triangle, that
	 * is how vertices are put together to
	 * form the mesh.
	 * @param currentObject the object to store the information
	 * in.
	 */
	private void readFace(ASEObject currentObject, StringTokenizer tokenizer) {
		int index = 0;

		// Read past the index of this Face
		String temp = tokenizer.nextToken();
		if (temp.indexOf(":") > 0) {
			temp = temp.substring(0, temp.length() - 1);
		}
		index = Integer.parseInt(temp);
		currentObject.faces[index] = new Face();

		tokenizer.nextToken(); // "A:"
		currentObject.faces[index].vertIndex[0] =
			Integer.parseInt(tokenizer.nextToken());
		tokenizer.nextToken(); // "B:"
		currentObject.faces[index].vertIndex[1] =
			Integer.parseInt(tokenizer.nextToken());
		tokenizer.nextToken(); // "C:"
		currentObject.faces[index].vertIndex[2] =
			Integer.parseInt(tokenizer.nextToken());
	}

	/**
	 * 
	 * <code>readFace</code> reads the face of a triangle, that
	 * is how texture vertices are put together to
	 * form the mesh.
	 * @param currentObject the object to store the information
	 * in.
	 */
	private void readTextureFace(ASEObject currentObject, StringTokenizer tokenizer) {
		int index = 0;

		// Read past the index for this texture coordinate
		index = Integer.parseInt(tokenizer.nextToken());

		// Now we read in the UV coordinate index for the current face.
		// This will be an index into pTexCoords[] for each point in the face.
		currentObject.faces[index].coordIndex[0] =
			Integer.parseInt(tokenizer.nextToken());
		currentObject.faces[index].coordIndex[1] =
			Integer.parseInt(tokenizer.nextToken());
		currentObject.faces[index].coordIndex[2] =
			Integer.parseInt(tokenizer.nextToken());
	}

//	/**
//	 * 
//	 * <code>computeNormals</code> normals are not defined in the 
//	 * ase file, so we calculate them manually. Each vertex has a
//	 * matching normal. This normal is the average of all the face
//	 * normals surrounding the vertex.
//	 *
//	 */
//	private void computeNormals(int numOfObjects, List<ASEObject> objectList) {
//		if (numOfObjects <= 0) {
//			return;
//		}
//		
//		Vector3f vector1 = new Vector3f();
//		Vector3f vector2 = new Vector3f();
//		Vector3f[] triangle = new Vector3f[3];
//
//		// Go through each of the objects to calculate their normals
//		for (int index = 0; index < numOfObjects; index++) {
//			// Get the current object
//			ASEObject object = objectList.get(index);
//			// Here we allocate all the memory we need to calculate the normals
//			Vector3f[] tempNormals = new Vector3f[object.faces.length];
//			Vector3f[] normals = new Vector3f[object.getVertices().length];
//
//			// Go though all of the faces of this object
//			for (int i = 0; i < object.faces.length; i++) {
//				vector1 =
//					object
//						.getVertices()[object
//						.faces[i]
//						.vertIndex[0]]
//						.subtract(
//						object.getVertices()[object.faces[i].vertIndex[2]]);
//				vector2 =
//					object
//						.getVertices()[object
//						.faces[i]
//						.vertIndex[2]]
//						.subtract(
//						object.getVertices()[object.faces[i].vertIndex[1]]);
//
//				tempNormals[i] = vector1.cross(vector2).normalize();
//			}
//
//			Vector3f sum = new Vector3f();
//			Vector3f zero = sum;
//			int shared = 0;
//
//			for (int i = 0; i < object.getVertices().length; i++) {
//				for (int j = 0; j < object.faces.length; j++) {
//					if (object.faces[j].vertIndex[0] == i
//						|| object.faces[j].vertIndex[1] == i
//						|| object.faces[j].vertIndex[2] == i) {
//						sum = sum.add(tempNormals[j]);
//
//						shared++;
//					}
//				}
//
//				normals[i] = sum.divide((float) (-shared)).normalize();
//
//				sum = zero; // Reset the sum
//				shared = 0; // Reset the shared
//			}
//
//			object.setNormals(normals);
//
//		}
//	}

	/**
	 * 
	 * <code>ASEMaterialInfo</code> holds material and texture information.
	 */
	private class ASEMaterialInfo {
//		String name; // The texture name
//		public String file;
		// The texture file name (If this is set it's a texture map)
		public float[] diffuse = new float[3];
		public float[] ambient = new float[3];
		public float[] specular = new float[3];
//		public float shine;
		// The color of the object (R, G, B)
		float uTile; // u tiling of texture (Currently not used)
		float vTile; // v tiling of texture (Currently not used)
//		float uOffset; // u offset of texture (Currently not used)
//		float vOffset; // v offset of texture (Currently not used)
	};

	/**
	 * 
	 * <code>ASEObject</code> holds the data for the mesh.
	 */
	public class ASEObject {
		
		private ASEObject(String mainObjectName) {
			super();
			this.mainObjectName = mainObjectName;
		}
		private int materialID;
		private Vector3f[] vertices;
		private Vector2f[] texCoords;
		private Vector3f[] normals;
		private Vector2f[] tempTexVerts; // The texture's UV coordinates
		private Face[] faces; // The faces information of the object
		private String mainObjectName;
		private String name;
		
		public Mesh getMesh() {
			Vector3f[] endVertices = new Vector3f[faces.length * 3];
			Vector2f[] endTexCoord = new Vector2f[faces.length * 3];
			Vector3f[] endNormals = new Vector3f[faces.length * 3];
			
			int[] endIndices = new int[faces.length * 3];
			int i = 0;
			for (Face face : faces) {
				Vector3f vec1 = vertices[face.vertIndex[0]].subtract(vertices[face.vertIndex[1]]);
				Vector3f vec2 = vertices[face.vertIndex[2]].subtract(vertices[face.vertIndex[1]]);
				Vector3f normal = vec2.cross(vec1).normalize();
				for (int j = 0; j < 3; j++) {
					endVertices[i] = vertices[face.vertIndex[j]];
					endTexCoord[i] = tempTexVerts[face.coordIndex[j]];
					endIndices[i] = i;
					endNormals[i] = normal;
					i++;
				}
				
				
			}
			
			Mesh mesh = new Mesh();
			mesh.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(endVertices));
			mesh.setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(endTexCoord));
			mesh.setBuffer(Type.Index,    1, BufferUtils.createIntBuffer(endIndices));
			mesh.setBuffer(Type.Normal,   3, BufferUtils.createFloatBuffer(endNormals));
			mesh.updateBound();
			
			return mesh;
		}
		
		
		private File getTextureFile(String modelName, String textureName) {
			File file = null;
			for (File folder : getModelsFolders()) {
				File potentialFile = new File(new File(new File(folder, modelName), "Textures"),textureName + ".bmp");
				if (potentialFile.exists()) {
					file = potentialFile;
				}
			}
			return file;
		}
		
		public Material getMaterial() {
			AssetManager assetManager = HexScapeCore.getInstance().getHexScapeJme3Application().getAssetManager();
			Material mat = new Material(assetManager, "model/materialDef/LightBlow.j3md");
			
			Texture TileTexture = assetManager.loadTexture(HexScapeCore.APP_DATA_FOLDER.toURI().relativize(getTextureFile(mainObjectName, name).toURI()).getPath());
			TileTexture.setWrap(WrapMode.Repeat);
			mat.setTexture("DiffuseMap", TileTexture);
			mat.setBoolean("UseMaterialColors",true);    
			mat.setColor("Ambient", ColorRGBA.White.mult(0.5f));
			mat.setColor("Diffuse",ColorRGBA.White);  // minimum material color
	        mat.setColor("Specular",ColorRGBA.White); // for shininess
	        mat.setFloat("Shininess", 50f);
			
			return mat;
		}
		
		
		
		Vector3f[] getVertices() {
			return vertices;
		}
		public void setVertices(Vector3f[] vertices) {
			this.vertices = vertices;
		}
		Vector2f[] getTexCoords() {
			return texCoords;
		}
		public void setTexCoords(Vector2f[] texCoords) {
			this.texCoords = texCoords;
		}
		Vector3f[] getNormals() {
			return normals;
		}
		public void setNormals(Vector3f[] normals) {
			this.normals = normals;
		}

		String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setMainObjectName(String mainObjectName) {
			this.mainObjectName = mainObjectName;
		}
		
		
	};
	
	public class Face {
		public int[] vertIndex = new int[3];
		public int[] coordIndex= new int[3];
	}

}
