package ru.ibs.gasu.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.ibs.gasu.client.widgets.componens.FileUploader;
import ru.ibs.gasu.common.models.FileModel;
import ru.ibs.gasu.common.models.SimpleIdNameModel;
import ru.ibs.gasu.common.soap.generated.fileapi.FileDomain;
import ru.ibs.gasu.server.service.FileService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping(
            value = "/upload",
            produces = "application/json; charset=utf-8",
            consumes = "multipart/form-data; charset=utf-8")
    @ResponseBody
    private SimpleIdNameModel fileUpload(@RequestParam("Filedata") MultipartFile file) throws IOException {
        FileDomain fileDomain = fileService.uploadFile(file);
        return new SimpleIdNameModel(String.valueOf(fileDomain.getFileVersionId()), fileDomain.getFileName());
    }

    @PostMapping(
            value = "/uploadlist",
            produces = "application/json; charset=utf-8",
            consumes = "multipart/form-data; charset=utf-8")
    @ResponseBody
    private FileUploader.GridFilesModel filesUploadList(@RequestParam("file") List<MultipartFile> files) throws IOException {
        FileUploader.GridFilesModel res = new FileUploader.GridFilesModel();
        for (MultipartFile file : files) {
            FileDomain fileDomain = fileService.uploadFile(file);
            res.getFiles().add(new FileUploader.GridFileModel(fileDomain.getFileName(), fileDomain.getFileVersionId()));
        }
        if (res.getFiles().size() != files.size()) return new FileUploader.GridFilesModel();
        return res;
    }


    @RequestMapping(value = "/download/{id}", produces = "application/json; charset=utf-8")
    @ResponseBody
    private ResponseEntity<InputStreamResource> downloadVersion(@PathVariable Long id) throws IOException {
        return fileService.downloadVersion(id);
    }

    @RequestMapping(value = "project/download/{id}")
    @ResponseBody
    private HttpEntity<ByteArrayResource> downloadProjectFiles(@PathVariable Long id) throws IOException {
        return fileService.downloadProjectFiles(id);
    }
}
