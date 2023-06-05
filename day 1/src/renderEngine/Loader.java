package renderEngine;

import models.*;

import java.io.IOException;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.assimp.*;

import graphics.*;

public class Loader {
    static ArrayList <VAO> vaos = new ArrayList<>();
    static ArrayList <VBO> vbos = new ArrayList<>();
    static ArrayList <EBO> ebos = new ArrayList<>();

    public static BaseModel createBaseModel(float[] vertices, int[] indices) {
        VAO vao = new VAO();
        vao.bind();
        VBO vertexVBO = new VBO(vertices);
        vertexVBO.bind();
        EBO ebo = new EBO(indices);
        vao.linkToVAO(0, 3);
        return new BaseModel(vao, ebo);
    }
    public static TexturedModel createTexturedModel(float[] vertices, float[] normals, float[] uv, int[] indices, String textureFilePath) {
        VAO vao = new VAO();
        vao.bind();

        VBO vertexVBO = new VBO(vertices);
        vertexVBO.bind();
        vao.linkToVAO(0, 3);

        VBO normalsVBO = new VBO(normals);
        normalsVBO.bind();
        vao.linkToVAO(1, 3);

        VBO uvVBO = new VBO(uv);
        uvVBO.bind();
        vao.linkToVAO(2, 2);

        EBO ebo = new EBO(indices);
        Texture texture;
        try {
            texture = new Texture(textureFilePath);
        } catch (IOException e) {
            texture = null;
        }

        vaos.add(vao);
        vbos.add(vertexVBO);
        vbos.add(normalsVBO);
        vbos.add(uvVBO);
        ebos.add(ebo);

        return new TexturedModel(vao, ebo, texture);
    }

    public static TexturedModel loadObjFile(String filePath, String texturePath) {
        AIScene scene = Assimp.aiImportFile(filePath, Assimp.aiProcess_Triangulate | Assimp.aiProcess_GenNormals | Assimp.aiProcess_FixInfacingNormals);
        if (scene == null) {
            throw new RuntimeException("Failed to load .obj file: " + filePath);
        }
    
        VAO vao = new VAO();

        AIMesh aiMesh = AIMesh.create(scene.mMeshes().get(0));
    
        // Retrieve vertices
        AIVector3D.Buffer aiVertices = aiMesh.mVertices();
        int numVertices = aiVertices.remaining();
        float[] vertices = new float[numVertices * 3];
        for (int i = 0; i < numVertices; i++) {
            AIVector3D vertex = aiVertices.get(i);
            vertices[i * 3] = vertex.x();
            vertices[i * 3 + 1] = vertex.y();
            vertices[i * 3 + 2] = vertex.z();
        }
        vao.bind();
        VBO vbo = new VBO(vertices);
        vbo.bind();
        vao.linkToVAO(0, 3);
        vbos.add(vbo);
    
        // Retrieve normals
        AIVector3D.Buffer aiNormals = aiMesh.mNormals();
        if (aiNormals != null) {
            int numNormals = aiNormals.remaining();
            float[] normals = new float[numNormals * 3];
            for (int i = 0; i < numNormals; i++) {
                AIVector3D normal = aiNormals.get(i);
                normals[i * 3] = normal.x();
                normals[i * 3 + 1] = normal.y();
                normals[i * 3 + 2] = normal.z();
            }
            vao.bind();
            VBO normVBO = new VBO(normals);
            normVBO.bind();
            vao.linkToVAO(1, 3);
            vbos.add(normVBO);
        }
    
        // Retrieve texture coordinates
        AIVector3D.Buffer aiTexCoords = aiMesh.mTextureCoords(0);
        if (aiTexCoords != null) {
            int numTexCoords = aiTexCoords.remaining();
            float[] texCoords = new float[numTexCoords * 2];
            for (int i = 0; i < numTexCoords; i++) {
                AIVector3D texCoord = aiTexCoords.get(i);
                texCoords[i * 2] = texCoord.x();
                texCoords[i * 2 + 1] = texCoord.y();
            }
            vao.bind();
            VBO uvVBO = new VBO(texCoords);
            uvVBO.bind();
            vao.linkToVAO(2, 2);
            vbos.add(uvVBO);
        }
    
        // Retrieve indices
        AIFace.Buffer aiFaces = aiMesh.mFaces();
        int numFaces = aiFaces.remaining();
        int[] indices = new int[numFaces * 3];
        for (int i = 0; i < numFaces; i++) {
            AIFace face = aiFaces.get(i);
            IntBuffer indicesBuffer = face.mIndices();
            for (int j = 0; j < indicesBuffer.remaining(); j++) {
                indices[i * 3 + j] = indicesBuffer.get(j);
            }
        }
        EBO ebo = new EBO(indices);
    
        // Clean up
        Assimp.aiReleaseImport(scene);
        
        Texture texture;
        try {
            texture = new Texture(texturePath);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            texture = null;
        }

        vaos.add(vao);
        ebos.add(ebo);
    
        return new TexturedModel(vao, ebo, texture);
    }

    public static void dispose() {
        for(VAO vao : vaos) {
           vao.dispose(); 
        }
        for(VBO vbo : vbos) {
           vbo.dispose(); 
        }
        for(EBO ebo : ebos) {
           ebo.dispose(); 
        }
    }
}
