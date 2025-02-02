package ru.ibs.gasu.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.AccordionLayoutContainer;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.DateField;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.error.DefaultEditorError;
import ru.ibs.gasu.client.widgets.componens.DateFieldFullDate;

import java.util.ArrayList;
import java.util.List;

import static ru.ibs.gasu.client.widgets.WidgetUtils.*;

public class FinancialAndEconomicIndicatorsView implements IsWidget {

    private CheckBox feiTaxIncentivesExist;
    private TextField feiResidualValue;
    private TextField feiAverageServiceLife;
    private DateField feiForecastValuesDate;
    private InvestmentsInObjectWidget investmentsInObjectWidget;
    private OperationalCostsWidget operationalCostsWidget;
    private TaxConditionWidget taxConditionWidget;
    private VerticalLayoutContainer taxConditionWidgetContainer;
    private RevenueIndicatorsWidget revenueIndicatorsWidget;

    private VerticalLayoutContainer container;

    public VerticalLayoutContainer getContainer() {
        return container;
    }

    public FinancialAndEconomicIndicatorsView() {
        initWidget();
    }

    public CheckBox getFeiTaxIncentivesExist() {
        return feiTaxIncentivesExist;
    }

    public TextField getFeiResidualValue() {
        return feiResidualValue;
    }

    public TextField getFeiAverageServiceLife() {
        return feiAverageServiceLife;
    }

    public DateField getFeiForecastValuesDate() {
        return feiForecastValuesDate;
    }

    public InvestmentsInObjectWidget getInvestmentsInObjectWidget() {
        return investmentsInObjectWidget;
    }

    public OperationalCostsWidget getOperationalCostsWidget() {
        return operationalCostsWidget;
    }

    public TaxConditionWidget getTaxConditionWidget() {
        return taxConditionWidget;
    }

    public RevenueIndicatorsWidget getRevenueIndicatorsWidget() {
        return revenueIndicatorsWidget;
    }

    private ContentPanel cp1;
    private ContentPanel cp2;
    private ContentPanel cp3;
    private ContentPanel cp4;

    public ContentPanel getCp3() {
        return cp3;
    }

    public ContentPanel getCp4() {
        return cp4;
    }

    private void initWidget() {
        container = new VerticalLayoutContainer();

        investmentsInObjectWidget = new InvestmentsInObjectWidget();
        VerticalLayoutContainer investmentsInObjectWidgetContainer = new VerticalLayoutContainer();
        investmentsInObjectWidgetContainer.add(investmentsInObjectWidget);

        AccordionLayoutContainer.AccordionLayoutAppearance appearance = GWT.<AccordionLayoutContainer.AccordionLayoutAppearance>create(AccordionLayoutContainer.AccordionLayoutAppearance.class);
        MarginData cpMargin = new MarginData(10, 10, 10, 20);
        cp1 = new ContentPanel(appearance);
        cp1.setAnimCollapse(false);
        cp1.setHeading("Инвестиции в объект");
        VerticalLayoutContainer cp1V = new VerticalLayoutContainer();
        cp1V.add(investmentsInObjectWidgetContainer);
        cp1.add(cp1V, cpMargin);


        feiTaxIncentivesExist = new CheckBox();
        feiTaxIncentivesExist.setBoxLabel(wrapString("Существуют налоговые льготы"));

        feiTaxIncentivesExist.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                if (event.getValue() != null && event.getValue()) {
                    taxConditionWidgetContainer.show();
                    container.forceLayout();
                } else {
                    taxConditionWidgetContainer.hide();
                }
            }
        });

        VerticalLayoutContainer operationalCostsWidgetContainer = new VerticalLayoutContainer();
        operationalCostsWidget = new OperationalCostsWidget();
        operationalCostsWidgetContainer.add(operationalCostsWidget);

        taxConditionWidgetContainer = new VerticalLayoutContainer();
        taxConditionWidget = new TaxConditionWidget();
        taxConditionWidgetContainer.add(taxConditionWidget);

        cp2 = new ContentPanel(appearance);
        cp2.setAnimCollapse(true);
        cp2.setHeading("Расходы концессионера/частного партнера на эксплуатационной стадии");
        VerticalLayoutContainer cp2V = new VerticalLayoutContainer();
        cp2V.add(operationalCostsWidgetContainer, STD_VC_LAYOUT);
        cp2V.add(feiTaxIncentivesExist, STD_VC_LAYOUT);
        cp2V.add(taxConditionWidgetContainer);
        cp2.add(cp2V, cpMargin);

        operationalCostsWidget.getTreeGrid().addViewReadyHandler(viewReadyEvent -> {
            if (operationalCostsWidget.getTreeStore().getAll().stream()
                    .anyMatch(planFactYear -> planFactYear.getPlan() != null && planFactYear.getPlan() != 0
                            || planFactYear.getFact() != null && planFactYear.getFact() != 0))
                cp2.expand();
        });

        VerticalLayoutContainer revenueIndicatorsContainer = new VerticalLayoutContainer();
        revenueIndicatorsWidget = new RevenueIndicatorsWidget();
        revenueIndicatorsContainer.add(revenueIndicatorsWidget);

        cp3 = new ContentPanel(appearance);
        cp3.setAnimCollapse(true);
        cp3.setHeading("Показатели выручки по проекту");
        VerticalLayoutContainer cp3V = new VerticalLayoutContainer();
        cp3V.add(revenueIndicatorsContainer);
        cp3.add(cp3V, cpMargin);

        revenueIndicatorsWidget.getTreeGrid().addViewReadyHandler(viewReadyEvent -> {
            if (revenueIndicatorsWidget.getTreeGrid().getTreeStore().getAll().size() > 0)
                cp3.expand();
        });

        feiResidualValue = new TextField();
        validation(feiResidualValue);
        FieldLabel feiResidualValueLabel = createFieldLabelTop(feiResidualValue, new SafeHtmlBuilder().appendHtmlConstant("Остаточная стоимость передаваемого концедентом/публичным партнером концессионеру/частному партнеру имущества на дату заключения соглашения, тыс. руб. " +
                "<i class='fa fa-info-circle' aria-hidden='true' style='cursor: pointer'" +
                "title='Балансовая стоимость передаваемого объекта'></i>").toSafeHtml());
        feiAverageServiceLife = new TextField();
        validation(feiAverageServiceLife);
        FieldLabel feiAverageServiceLifeLabel = createFieldLabelTop(feiAverageServiceLife, new SafeHtmlBuilder().appendHtmlConstant("Средний срок эксплуатации переданного концессионеру/частному партнеру имущества, существующего на дату заключения соглашения, лет " +
                "<i class='fa fa-info-circle' aria-hidden='true' style='cursor: pointer'" +
                "title='Срок эксплуатации объекта до его передачи концессионеру / частному партнёру'></i>").toSafeHtml());
        feiForecastValuesDate = new DateFieldFullDate();
        FieldLabel feiForecastValuesDateLabel = createFieldLabelTop(feiForecastValuesDate, "Дата внесения данных о прогнозных значениях финансово-экономических показателях реализации соглашения");


        cp4 = new ContentPanel(appearance);
        cp4.setAnimCollapse(true);
        cp4.setHeading("Иные финансово-экономические показатели");
        VerticalLayoutContainer cp4V = new VerticalLayoutContainer();
        cp4V.add(feiResidualValueLabel, STD_VC_LAYOUT);
        cp4V.add(feiAverageServiceLifeLabel, STD_VC_LAYOUT);
        cp4V.add(feiForecastValuesDateLabel, STD_VC_LAYOUT);
        cp4.add(cp4V, cpMargin);

        AccordionLayoutContainer accordion = new AccordionLayoutContainer();
        accordion.setExpandMode(AccordionLayoutContainer.ExpandMode.MULTI);
        accordion.add(cp1);
        accordion.add(cp2);
        accordion.add(cp3);
        accordion.add(cp4);

        accordion.setActiveWidget(cp1);
        cp2.collapse();
        cp3.collapse();
        cp4.collapse();

        container.add(accordion, STD_VC_LAYOUT);
    }

    @Override
    public Widget asWidget() {
        return container;
    }

    /**
     * Обновить форму (скрыть + очистить/показать поля) в зависимости от формы реализации проекта
     * и способа инициации проекта.
     *
     * @param formId - форма реализации проекта
     * @param initId - способ инициации проекта
     */
    public void update(Long formId, Long initId) {

        if (feiTaxIncentivesExist.getValue() != null && !feiTaxIncentivesExist.getValue()) {
            taxConditionWidgetContainer.hide();
        } else {
            taxConditionWidgetContainer.show();
            container.forceLayout();
        }
    }

    public void validation(TextField field) {
        field.addValidator((editor, value) -> {
            List<EditorError> errors = new ArrayList<>();
            try {
                if (value != null && !value.isEmpty()) {
                    Double.valueOf(value);
                    field.setValue(NumberFormat.getFormat("#,##0.0#############").format(Double.parseDouble(field.getValue())));
                }
            } catch (Exception e) {
                String message = "Необходимо ввести числовое значение";
                if (!value.matches("[0-9\\s,]+")) {
                    errors.add(new DefaultEditorError(editor, message, value));
                    field.markInvalid(message);
                }
            }
            return errors;
        });
        field.setAutoValidate(true);
    }
}
