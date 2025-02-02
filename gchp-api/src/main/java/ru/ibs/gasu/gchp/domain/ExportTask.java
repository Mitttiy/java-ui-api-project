package ru.ibs.gasu.gchp.domain;

import lombok.*;

import javax.xml.bind.annotation.XmlTransient;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.Future;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class ExportTask {
    private double progress;
    private String status;
    private String result;
    private boolean done;
    @NonNull
    private UUID uuid;
    private Date startTime = new Date();
    private Date doneTime = new Date();
    @Getter(onMethod_={@XmlTransient})
    private Future<?> task;
    @Getter(onMethod_={@XmlTransient})
    private byte[] ods;
}
