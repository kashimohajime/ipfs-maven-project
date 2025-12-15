package com.example;


import okhttp3.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class IPFSClient {

    private static final String IPFS_API = "http://127.0.0.1:5001/api/v0";
    private final OkHttpClient client = new OkHttpClient();

    public String uploadFile(File file) throws IOException {
        RequestBody fileBody = RequestBody.create(file, MediaType.parse("application/octet-stream"));
        MultipartBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), fileBody)
                .build();

        Request request = new Request.Builder()
                .url(IPFS_API + "/add")
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        String body = response.body().string();

        return body.split("\"Hash\":\"")[1].split("\"")[0];
    }

  public void download(String cid, File destinationFile) throws IOException {

        Request request = new Request.Builder()
                .url(IPFS_API + "/cat?arg=" + cid)
                .post(RequestBody.create(new byte[0]))
                .build();

        try (Response response = client.newCall(request).execute();
             InputStream in = response.body().byteStream();
             FileOutputStream out = new FileOutputStream(destinationFile)) {

            if (!response.isSuccessful()) {
                throw new IOException("IPFS download failed: " + response);
            }

            in.transferTo(out); // Java 9+
        }
    }

    public void pin(String cid) throws IOException {
        Request request = new Request.Builder()
                .url(IPFS_API + "/pin/add?arg=" + cid)
                .post(RequestBody.create(new byte[0]))
                .build();

        client.newCall(request).execute();
    }

    public void unpin(String cid) throws IOException {
        Request request = new Request.Builder()
                .url(IPFS_API + "/pin/rm?arg=" + cid)
                .post(RequestBody.create(new byte[0]))
                .build();

        client.newCall(request).execute();
    }
}
