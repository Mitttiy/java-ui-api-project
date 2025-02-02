package ru.ibs.gasu.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import ru.ibs.gasu.common.models.SimpleNameValModel;
import ru.ibs.gasu.common.soap.generated.gchpdocs.TechEconomicsIndicator;
import ru.ibs.gasu.common.soap.generated.gchpdocs.TechEconomicsObjectIndicator;

import java.util.List;
import java.util.stream.Collectors;

import static ru.ibs.gasu.client.widgets.WidgetUtils.getStringValOrDefault;

public class ProjectPassportView implements IsWidget {
    interface Resources extends ClientBundle {
        @Source("gerb.jpg")
        ImageResource gerb();

        @Source("printer.png")
        ImageResource printer();
    }

    interface PrintTemplate extends XTemplates {
        @XTemplate(source = "PrintTemplate.html")
        SafeHtml template(Passport printer);

        @XTemplate(source = "TechEcoObjectIndicatorsTemplate.html")
        SafeHtml templateObjectIndicator(TechEconomicsObjectIndicator object);
        @XTemplate(source = "TechEcoIndicatorsTemplate.html")
        SafeHtml templateIndicator(SimpleNameValModel indicator);
    }

    private Resources resources = GWT.create(Resources.class);
    private PrintTemplate template = GWT.create(PrintTemplate.class);

    private VerticalLayoutContainer container;
    private Passport passport = new Passport();

    public void setSubjectLabel(String subjectLabel) {
        passport.subjectLabel = subjectLabel;
    }

    public void setPopulationSubjectLabel(String populationSubjectLabel) {
        passport.populationSubjectLabel = populationSubjectLabel;
    }
    public void setEmblem(String emblem) {
        passport.emblem = emblem;
    }

    public void setMoLabel(String moLabel) {
        passport.moLabel = moLabel;
    }

    public void setOpfLabel(String opfLabel) {
        passport.opfLabel = opfLabel;
    }

    public void setPopulationMoLabel(String populationMoLabel) {
        passport.populationMoLabel = populationMoLabel;
    }

    public void setNameLabel(String nameLabel) {
        passport.nameLabel = nameLabel;
    }

    public void setStatusLabel(String statusLabel) {
        passport.statusLabel = statusLabel;
    }

    public void setIdLabel(String idLabel) {
        passport.idLabel = idLabel;
    }

    public void setAgreementMethodLabel(String agreementMethodLabel) {
        passport.agreementMethodLabel = agreementMethodLabel;
    }

    public void setImplFormLabel(String implFormLabel) {
        passport.implFormLabel = implFormLabel;
    }

    public void setImplLevelLabel(String implLevelLabel) {
        passport.implLevelLabel = implLevelLabel;
    }

    public void setImplSphereLabel(String implSphereLabel) {
        passport.implSphereLabel = implSphereLabel;
    }

    public void setImplSectorLabel(String implSectorLabel) {
        passport.implSectorLabel = implSectorLabel;
    }

    public void setPublicNameLabel(String publicNameLabel) {
        passport.publicNameLabel = publicNameLabel;
    }

    public void setPrivateNameLabel(String privateNameLabel) {
        passport.privateNameLabel = privateNameLabel;
    }

    public void setAgreementDateLabel(String agreementDateLabel) {
        passport.agreementDateLabel = agreementDateLabel;
    }

    public void setValidityLabel(String validityLabel) {
        passport.validityLabel = validityLabel;
    }

    public void setCommissioningDateAgreementLabel(String commissioningDateAgreementLabel) {
        passport.commissioningDateAgreementLabel = commissioningDateAgreementLabel;
    }

    public void setCommissioningDateFactLabel(String commissioningDateFactLabel) {
        passport.commissioningDateFactLabel = commissioningDateFactLabel;
    }

    public void setTotalInvestmentSizeLabel(String totalInvestmentSizeLabel) {
        passport.totalInvestmentSizeLabel = totalInvestmentSizeLabel;
    }

    public void setPrivateInvestmentSizeLabel(String privateInvestmentSizeLabel) {
        passport.privateInvestmentSizeLabel = privateInvestmentSizeLabel;
    }

    public void setBudgetExpendituresLabel(String budgetExpendituresLabel) {
        passport.budgetExpendituresLabel = budgetExpendituresLabel;
    }

    public void setTable(List<TechEconomicsObjectIndicator> objectIndicators) {
        if (!objectIndicators.isEmpty()) {
            String table = objectIndicators.stream().map(object -> {
                StringBuilder sb = new StringBuilder(template.templateObjectIndicator(object).asString());
                for(TechEconomicsIndicator indicator : object.getTechEconomicsIndicators()) {
                    SimpleNameValModel model = new SimpleNameValModel();
                    model.setName(indicator.getName());
                    model.setValue(getStringValOrDefault(() -> String.valueOf(indicator.getPlan()),"-"));
                    sb.append(template.templateIndicator(model).asString());
                }
                return sb;
            }).collect(Collectors.joining(" "));
            passport.table = SafeHtmlUtils.fromTrustedString(table);
        }
    }

    public void attachWidget() {
        container.add(new HTML(template.template(passport)));
        initPrintFunction(getPageHtml());
    }

    public ProjectPassportView() {
        container = new VerticalLayoutContainer();
    }


    private String getPageHtml() {
        String innerHTML = container.getElement().getInnerHTML();
        int printerImgIndex = innerHTML.indexOf("<img src=\"" + resources.printer().getSafeUri().asString());

        if (printerImgIndex < 0) {
            return innerHTML;
        }

        int closePrinterImgIndex = innerHTML.indexOf(">", printerImgIndex);
        return container.getElement().getInnerHTML().replace(innerHTML.substring(printerImgIndex, closePrinterImgIndex + 1), "");
    }


    private native void initPrintFunction(String html) /*-{
        $wnd.printer = $entry(function () {
            var winprint = window.open('', "PrintWindow", "toolbars=no,scrollbars=no,status=no,resizable=no");
            winprint.document.open();
            winprint.document.write('<html><head></head><body onload="window.print();">');
            winprint.document.write(html);
            winprint.document.write('</body></html>');
            winprint.document.close();
            winprint.focus();
        });

    }-*/;

    public class Passport {
        public String emblem;
        public SafeUri print;

        public String subjectLabel = "-";
        public String populationSubjectLabel = "-";
        public String moLabel = "-";
        public String opfLabel = "-";

        public String populationMoLabel = "-";

        public String nameLabel = "-";
        public String statusLabel = "-";
        public String idLabel = "-";
        public String agreementMethodLabel = "-";
        public String implFormLabel = "-";
        public String implLevelLabel = "-";
        public String implSphereLabel = "-";
        public String implSectorLabel = "-";
        public String publicNameLabel = "-";
        public String privateNameLabel = "-";
        public String agreementDateLabel = "-";
        public String validityLabel = "-";
        public String commissioningDateAgreementLabel = "-";
        public String commissioningDateFactLabel = "-";
        public String totalInvestmentSizeLabel = "-";
        public String privateInvestmentSizeLabel = "-";
        public String budgetExpendituresLabel = "-";
        public SafeHtml table = SafeHtmlUtils.fromTrustedString("-");

        public Passport() {
            emblem = resources.gerb().getSafeUri().toString();
            print = resources.printer().getSafeUri();
        }
    }

    @Override
    public Widget asWidget() {
        return container;
    }
}
