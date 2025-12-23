package com.example;

import okhttp3.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Classe responsable de la communication avec IPFS.
 * Elle permet d'uploader, télécharger, pinner et dépinner des fichiers.
 */
public class IPFSClient {

    // URL de l'API IPFS locale (nœud IPFS lancé en local)
    private static final String IPFS_API = "http://127.0.0.1:5001/api/v0";

    // Client HTTP utilisé pour envoyer les requêtes à IPFS
    private final OkHttpClient client = new OkHttpClient();

    /**
     * Upload un fichier local vers IPFS.
     * @param file fichier à envoyer
     * @return CID généré par IPFS
     */
    public String uploadFile(File file) throws IOException {

        // Corps de la requête contenant le fichier
        RequestBody fileBody = RequestBody.create(
                file,
                MediaType.parse("application/octet-stream")
        );

        // Requête multipart nécessaire pour l'upload vers IPFS
        MultipartBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), fileBody)
                .build();

        // Construction de la requête HTTP POST vers /add
        Request request = new Request.Builder()
                .url(IPFS_API + "/add")
                .post(requestBody)
                .build();

        // Envoi de la requête
        Response response = client.newCall(request).execute();

        // Réponse JSON contenant le CID
        String responseBody = response.body().string();

        // Extraction du CID depuis la réponse JSON
        return responseBody.split("\"Hash\":\"")[1].split("\"")[0];
    }

    /**
     * Télécharge un fichier depuis IPFS et le sauvegarde localement.
     * @param cid identifiant IPFS du fichier
     * @param outputFile fichier de destination
     */
    public void downloadFile(String cid, File outputFile) throws IOException {

        // Construction de la requête vers /cat
        Request request = new Request.Builder()
                .url(IPFS_API + "/cat?arg=" + cid)
                .post(RequestBody.create(new byte[0]))
                .build();

        // Exécution de la requête
        Response response = client.newCall(request).execute();

        // Écriture du contenu reçu dans un fichier local
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            fos.write(response.body().bytes());
        }
    }

    /**
     * Pin un fichier IPFS pour le conserver localement.
     * @param cid identifiant IPFS
     */
    public void pin(String cid) throws IOException {

        Request request = new Request.Builder()
                .url(IPFS_API + "/pin/add?arg=" + cid)
                .post(RequestBody.create(new byte[0]))
                .build();

        client.newCall(request).execute();
    }

    /**
     * Supprime le pin d’un fichier IPFS.
     * @param cid identifiant IPFS
     */
    public void unpin(String cid) throws IOException {

        Request request = new Request.Builder()
                .url(IPFS_API + "/pin/rm?arg=" + cid)
                .post(RequestBody.create(new byte[0]))
                .build();

        client.newCall(request).execute();
    }
}
