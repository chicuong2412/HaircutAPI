package com.haircutAPI.HaircutAPI.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.haircutAPI.HaircutAPI.enity.Images;
import com.haircutAPI.HaircutAPI.utils.ServicesUtils;

@Service
public class ImagesUploadService {
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    private static final String SERVICE_ACCOUNT_KEY_PATH = getPathToGoodleCredentials();

    @Autowired
    ServicesUtils servicesUtils;

    private static String getPathToGoodleCredentials() {
        // TODO Auto-generated method stub
        String currentDirectory = System.getProperty("user.dir");
        Path filePath = Paths.get(currentDirectory, "cred.json");
        return filePath.toString();
    }

    public Images uploadImageToGoogleDrive(File file) {
        Images fileSourse = new Images();
        String folderID = "1OMZ4TM-Kn46hI1FTsfuw4YYyvekmcxFv";
        try {
            Drive drive = createDriveService();
            com.google.api.services.drive.model.File fileMetaData = new com.google.api.services.drive.model.File();
            fileMetaData.setName(file.getName());
            fileMetaData.setParents(Collections.singletonList(folderID));
            FileContent fileContent = new FileContent("image/jpeg", file);
            com.google.api.services.drive.model.File uploadedFile = drive.files()
                    .create(fileMetaData, fileContent)
                    .setFields("id").execute();
            String imageURL = "https://drive.google.com/thumbnail?id=" + uploadedFile.getId() + "&sz=w1000";
            file.delete();
            fileSourse.setImgSrc(imageURL);
        } catch (GeneralSecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return fileSourse;
    }

    public void deleteFile(String id) throws GeneralSecurityException, IOException {
        Drive drive = createDriveService();
        // drive.files().list().forEach((t, u) -> System.out.println(t.toString() + " " + u.toString()));
        try {
            drive.files().delete(id).execute();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    private Drive createDriveService() throws GeneralSecurityException, IOException {
        GoogleCredential googleCredentials = GoogleCredential
                .fromStream(new FileInputStream(SERVICE_ACCOUNT_KEY_PATH))
                .createScoped(Collections.singleton(DriveScopes.DRIVE));

        return new Drive.Builder(
                GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, googleCredentials).build();
    }

}
