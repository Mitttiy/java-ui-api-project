package ru.ibs.gasu.server.service;

import com.google.gwt.thirdparty.guava.common.io.ByteStreams;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.ibs.gasu.common.soap.generated.fileapi.FileDomain;
import ru.ibs.gasu.common.soap.generated.fileapi.FileWebService;
import ru.ibs.gasu.common.soap.generated.gchpdocs.*;

import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


@Service
public class FileService {

    @Autowired
    private FileWebService fileWebService;

    @Autowired
    private GchpDocumentsWebService gchpDocumentsWebService;

    public FileDomain uploadFile(@RequestParam("Filedata") MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String[] split = originalFilename.split("\\\\");
        return fileWebService.uploadFile(file.getBytes(), split[split.length - 1]);
    }

    public ResponseEntity<InputStreamResource> downloadVersion(long id) throws IOException {
        FileDomain fileDomain = fileWebService.getFileVersion(id);
        return download(fileDomain);
    }

    public HttpEntity<ByteArrayResource> downloadProjectFiles(long id) throws IOException {
        DocumentsFilterPaginateCriteria criteria = new DocumentsFilterPaginateCriteria();
        criteria.setDocId(id);
        ProjectsResult projectsResult = gchpDocumentsWebService.paginateAndFilterDocuments(criteria);
        ProjectDetailDTO project = projectsResult.getList().get(0);

        final ByteArrayOutputStream fos = new ByteArrayOutputStream();
        ZipOutputStream zipOut = new ZipOutputStream(fos);

        if (!ObjectUtils.isEmpty(project.getCrAgreementTextFvId())) {
            for (AgreementTextFile file : project.getCrAgreementTextFvId()) {
                addEntry(zipOut, "Создание", file.getId());
            }
        }
        if (!ObjectUtils.isEmpty(project.getCrInvestmentVolumeStagOfCreationActFVId())) {
            for (InvestmentVolumeStagOfCreationActFile file : project.getCrInvestmentVolumeStagOfCreationActFVId()) {
                addEntry(zipOut, "Создание", file.getId());
            }
        }
        if (!ObjectUtils.isEmpty(project.getCrCalcInvestCostsActFVId())) {
            for (CalcInvestCostsActFile file : project.getCrCalcInvestCostsActFVId()) {
                addEntry(zipOut, "Создание", file.getId());
            }
        }
        if (!ObjectUtils.isEmpty(project.getCrConfirmationDocFVId())) {
            for (ConfirmationDocFile file : project.getCrConfirmationDocFVId()) {
                addEntry(zipOut, "Создание", file.getId());
            }
        }
        if (!ObjectUtils.isEmpty(project.getPpCompetitionResultsDocFvId())) {
            for (CompetitionResultsFile file : project.getPpCompetitionResultsDocFvId()) {
                addEntry(zipOut, "Подготовка проекта", file.getId());
            }
        }
        if (!ObjectUtils.isEmpty(project.getPpCompetitionResultsProtocolTextFvId())) {
            for (CompetitionResultsProtocolFile file : project.getPpCompetitionResultsProtocolTextFvId()) {
                addEntry(zipOut, "Подготовка проекта", file.getId());
            }
        }
        if (!ObjectUtils.isEmpty(project.getPpConclusionUOTextFileVersionId())) {
            for (ConclusionUOTextFile file : project.getPpConclusionUOTextFileVersionId()) {
                addEntry(zipOut, "Подготовка проекта", file.getId());
            }
        }
        if (!ObjectUtils.isEmpty(project.getPpFinancialModelTextFileVersionId())) {
            for (FinancialModelTextFile file : project.getPpFinancialModelTextFileVersionId()) {
                addEntry(zipOut, "Подготовка проекта", file.getId());
            }
        }
        if (!ObjectUtils.isEmpty(project.getPpActTextFileVersionId())) {
            for (ActTextFile file : project.getPpActTextFileVersionId()) {
                addEntry(zipOut, "Подготовка проекта", file.getId());
            }
        }
        if (!ObjectUtils.isEmpty(project.getTmSupportingDocuments())) {
            for (TerminationSupportingDocumentsFile file : project.getTmSupportingDocuments()) {
                addEntry(zipOut, "Прекращение", file.getId());
            }
        }
        if (!ObjectUtils.isEmpty(project.getTmTextFileVersionId())) {
            for (TerminationActFile file : project.getTmTextFileVersionId()) {
                addEntry(zipOut, "Прекращение", file.getId());
            }
        }
        if (!ObjectUtils.isEmpty(project.getExLastObjectActFVId())) {
            for (LastObjectActFile file : project.getExLastObjectActFVId()) {
                addEntry(zipOut, "Эксплуатация", file.getId());
            }
        }
        if (!ObjectUtils.isEmpty(project.getExCalculationPlannedAmountFVIds())) {
            for (ExCalculationPlannedAmountFile file : project.getExCalculationPlannedAmountFVIds()) {
                addEntry(zipOut, "Эксплуатация", file.getId());
            }
        }
        if (!ObjectUtils.isEmpty(project.getCbcNonPaymentConsumersFVId())) {
            for (NonPaymentConsumersFile file : project.getCbcNonPaymentConsumersFVId()) {
                addEntry(zipOut, "Условные бюджетные обязательства", file.getId());
            }
        }
        if (!ObjectUtils.isEmpty(project.getCbcArisingProvisionOfBenefitFVId())) {
            for (ArisingProvisionOfBenefitFile file : project.getCbcArisingProvisionOfBenefitFVId()) {
                addEntry(zipOut, "Условные бюджетные обязательства", file.getId());
            }
        }
        if (!ObjectUtils.isEmpty(project.getCbcCompensationArisingProvisionOfBenefitsFVId())) {
            for (CompensationArisingProvisionOfBenefitsFile file : project.getCbcCompensationArisingProvisionOfBenefitsFVId()) {
                addEntry(zipOut, "Условные бюджетные обязательства", file.getId());
            }
        }
        if (!ObjectUtils.isEmpty(project.getCbcMinimumGuaranteedFVId())) {
            for (MinimumGuaranteedFile file : project.getCbcMinimumGuaranteedFVId()) {
                addEntry(zipOut, "Условные бюджетные обязательства", file.getId());
            }
        }
        if (!ObjectUtils.isEmpty(project.getCbcCompensationAdditionalCostsAgreementFVId())) {
            for (CompensationAdditionalCostsAgreementFile file : project.getCbcCompensationAdditionalCostsAgreementFVId()) {
                addEntry(zipOut, "Условные бюджетные обязательства", file.getId());
            }
        }
        if (!ObjectUtils.isEmpty(project.getCcTextFileVersionId())) {
            for (ChangeTextFile file : project.getCcTextFileVersionId()) {
                addEntry(zipOut, "Изменение условий", file.getId());
            }
        }
        if (!ObjectUtils.isEmpty(project.getAdsDecisionTextFileId())) {
            for (AdsDecisionTextFile file : project.getAdsDecisionTextFileId()) {
                addEntry(zipOut, "Дополнительно", file.getId());
            }
        }
        if (!ObjectUtils.isEmpty(project.getPpProposalTextFileVersionId())) {
            for (ProposalTextFile file : project.getPpProposalTextFileVersionId()) {
                addEntry(zipOut, "Подготовка проекта", file.getId());
            }
        }

        // неизвестные
        if (!ObjectUtils.isEmpty(project.getPpCompetitionTextFVId())) {
            for (CompetitionText file : project.getPpCompetitionTextFVId()) {
                addEntry(zipOut, "Прочее", file.getId());
            }
        }
        if (!ObjectUtils.isEmpty(project.getGiCompletedTemplateTextFVId())) {
            for (CompletedTemplateFile file : project.getGiCompletedTemplateTextFVId()) {
                addEntry(zipOut, "Прочее", file.getId());
            }
        }
        if (!ObjectUtils.isEmpty(project.getPpConcludeAgreementFvId())) {
            for (ConcludeAgreementFile file : project.getPpConcludeAgreementFvId()) {
                addEntry(zipOut, "Прочее", file.getId());
            }
        }
        if (!ObjectUtils.isEmpty(project.getCrAgreementTextFiles())) {
            for (CreationAgreementFile file : project.getCrAgreementTextFiles()) {
                addEntry(zipOut, "Прочее", file.getId());
            }
        }
        if (!ObjectUtils.isEmpty(project.getPpDecisionTextFileVersionId())) {
            for (DecisionTextFile file : project.getPpDecisionTextFileVersionId()) {
                addEntry(zipOut, "Прочее", file.getId());
            }
        }
        if (!ObjectUtils.isEmpty(project.getExAcceptActAAMFVIds())) {
            for (ExploitationAcceptActAAMFile file : project.getExAcceptActAAMFVIds()) {
                addEntry(zipOut, "Прочее", file.getId());
            }
        }
        if (!ObjectUtils.isEmpty(project.getExAcceptActFVIds())) {
            for (ExploitationAcceptActFile file : project.getExAcceptActFVIds()) {
                addEntry(zipOut, "Прочее", file.getId());
            }
        }
        if (!ObjectUtils.isEmpty(project.getExAgreementFVIds())) {
            for (ExploitationAgreementFile file : project.getExAgreementFVIds()) {
                addEntry(zipOut, "Прочее", file.getId());
            }
        }
        if (!ObjectUtils.isEmpty(project.getExFinModelFVIds())) {
            for (ExploitationFinModelFile file : project.getExFinModelFVIds()) {
                addEntry(zipOut, "Прочее", file.getId());
            }
        }
        if (!ObjectUtils.isEmpty(project.getExSupportCompensDocFVIds())) {
            for (ExploitationSupportCompensDoclFile file : project.getExSupportCompensDocFVIds()) {
                addEntry(zipOut, "Прочее", file.getId());
            }
        }

        if (!ObjectUtils.isEmpty(project.getExSupportDocFVIds())) {
            for (ExploitationSupportDoclFile file : project.getExSupportDocFVIds()) {
                addEntry(zipOut, "Прочее", file.getId());
            }
        }
        if (!ObjectUtils.isEmpty(project.getCrFinancialClosingActFVId())) {
            for (FinancialClosingActFile file : project.getCrFinancialClosingActFVId()) {
                addEntry(zipOut, "Прочее", file.getId());
            }
        }
        if (!ObjectUtils.isEmpty(project.getPpFinancialModelFVId())) {
            for (FinancialModelFile file : project.getPpFinancialModelFVId()) {
                addEntry(zipOut, "Прочее", file.getId());
            }
        }
        if (!ObjectUtils.isEmpty(project.getCrFirstObjectCompleteActFVId())) {
            for (FirstObjectCompleteActFile file : project.getCrFirstObjectCompleteActFVId()) {
                addEntry(zipOut, "Прочее", file.getId());
            }
        }
        if (!ObjectUtils.isEmpty(project.getExInvestmentRecoveryFinancialModelFileVersionId())) {
            for (InvestmentRecoveryFinancialModelFile file : project.getExInvestmentRecoveryFinancialModelFileVersionId()) {
                addEntry(zipOut, "Прочее", file.getId());
            }
        }
        if (!ObjectUtils.isEmpty(project.getCrActFVId())) {
            for (InvestmentsActFile file : project.getCrActFVId()) {
                addEntry(zipOut, "Прочее", file.getId());
            }
        }
        if (!ObjectUtils.isEmpty(project.getCrReferenceFVId())) {
            for (InvestmentsLinkFile file : project.getCrReferenceFVId()) {
                addEntry(zipOut, "Прочее", file.getId());
            }
        }
        if (!ObjectUtils.isEmpty(project.getExInvestmentVolumeStagOfExploitationActFVId())) {
            for (InvestmentVolumeStagOfExploitationActFile file : project.getExInvestmentVolumeStagOfExploitationActFVId()) {
                addEntry(zipOut, "Прочее", file.getId());
            }
        }
        if (!ObjectUtils.isEmpty(project.getCrLandActFVId())) {
            for (LandActFile file : project.getCrLandActFVId()) {
                addEntry(zipOut, "Прочее", file.getId());
            }
        }
        if (!ObjectUtils.isEmpty(project.getCrLastObjectCompleteActFVId())) {
            for (LastObjectCompleteActFile file : project.getCrLastObjectCompleteActFVId()) {
                addEntry(zipOut, "Прочее", file.getId());
            }
        }
        if (!ObjectUtils.isEmpty(project.getPpLeaseAgreementTextFileVersionId())) {
            for (LeaseAgreementTextFile file : project.getPpLeaseAgreementTextFileVersionId()) {
                addEntry(zipOut, "Прочее", file.getId());
            }
        }

        if (!ObjectUtils.isEmpty(project.getPpProjectAgreementFileVersionId())) {
            for (ProjectAgreementFile file : project.getPpProjectAgreementFileVersionId()) {
                addEntry(zipOut, "Прочее", file.getId());
            }
        }
        if (!ObjectUtils.isEmpty(project.getPpProtocolFileVersionId())) {
            for (ProtocolFile file : project.getPpProtocolFileVersionId()) {
                addEntry(zipOut, "Прочее", file.getId());
            }
        }
        if (!ObjectUtils.isEmpty(project.getOdRentPassportFileVersionId())) {
            for (RentPassportFilesEntity file : project.getOdRentPassportFileVersionId()) {
                addEntry(zipOut, "Прочее", file.getId());
            }
        }
        if (!ObjectUtils.isEmpty(project.getPpSupportingDocumentsFileVersionIds())) {
            for (SupportingDocumentsFile file : project.getPpSupportingDocumentsFileVersionIds()) {
                addEntry(zipOut, "Прочее", file.getId());
            }
        }
        if (!ObjectUtils.isEmpty(project.getTmTaActTextFileVersionId())) {
            for (TerminationActTextFile file : project.getTmTaActTextFileVersionId()) {
                addEntry(zipOut, "Прочее", file.getId());
            }
        }
        if (!ObjectUtils.isEmpty(project.getTmCompensationFVIds())) {
            for (TerminationCompensationFile file : project.getTmCompensationFVIds()) {
                addEntry(zipOut, "Прочее", file.getId());
            }
        }

        zipOut.close();
        fos.close();

        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "force-download"));
        header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=files_" + id + ".zip");
        return new HttpEntity<>(new ByteArrayResource(fos.toByteArray()), header);
    }

    @SneakyThrows
    private void addEntry(ZipOutputStream zipOut, String folder, Long id) {
        FileDomain fileDomain = fileWebService.getFileVersion(id);
        if (fileDomain != null) {
            Path temp = Files.createTempFile(fileDomain.getFileName(), ".temp");
            File file = temp.toFile();
            try (FileOutputStream outputStream = new FileOutputStream(file)) {
                outputStream.write(getFileVersionId(fileDomain.getFileVersionId()));
            }
            try (FileInputStream fis = new FileInputStream(file)) {
                ZipEntry zipEntry = new ZipEntry(folder + "/" + fileDomain.getFileName());
                zipOut.putNextEntry(zipEntry);

                byte[] bytes = new byte[1024];
                int length;
                while ((length = fis.read(bytes)) >= 0) {
                    zipOut.write(bytes, 0, length);
                }
            }
            file.delete();
        }
    }


    @SneakyThrows
    private byte[] getFileVersionId(Long id) {
        InputStream fileStream = new ByteArrayInputStream(fileWebService.getFileStream(id));
        return ByteStreams.toByteArray(fileStream);

    }

    private ResponseEntity<InputStreamResource> download(FileDomain fileDomain) throws IOException {
        String headerDispose = "attachment; filename*=UTF-8''" + URLEncoder.encode(fileDomain.getFileName());
        InputStream fileStream = new ByteArrayInputStream(fileWebService.getFileStream(fileDomain.getFileVersionId()));
        return ResponseEntity
                .status(HttpStatus.OK)
                .header("Content-Length", String.valueOf(fileDomain.getFileSize()))
                .header("Content-Type", fileDomain.getMimeType())
                .header("Content-Disposition", headerDispose)
                .body(new InputStreamResource(fileStream));
    }
}
