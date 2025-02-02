package ru.ibs.gasu.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.AccordionLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.DateField;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.validator.MaxLengthValidator;
import ru.ibs.gasu.client.widgets.componens.DateFieldFullDate;
import ru.ibs.gasu.client.widgets.componens.FileUploader;
import ru.ibs.gasu.client.widgets.componens.TwinTriggerComboBox;
import ru.ibs.gasu.common.models.SimpleIdNameModel;
import ru.ibs.gasu.common.models.view_attr.ViewAttrState;
import ru.ibs.gasu.common.models.view_attr.ViewAttrStateColumns;

import java.util.ArrayList;
import java.util.Arrays;

import static ru.ibs.gasu.client.widgets.WidgetUtils.*;

/**
 * Раздел "Изменение условий"
 */
public class ConditionChangeView implements IsWidget {

    private VerticalLayoutContainer container;
    private CheckBox isChanged;
    private TwinTriggerComboBox<SimpleIdNameModel> cause;
    private TextField actNumber;
    private DateField actDate;
    private FileUploader actFileUpload;
    private FieldLabel causeLabel;

    public VerticalLayoutContainer getContainer() {
        return container;
    }

    public CheckBox getIsChanged() {
        return isChanged;
    }

    public TwinTriggerComboBox<SimpleIdNameModel> getCause() {
        return cause;
    }

    public TextField getActNumber() {
        return actNumber;
    }

    public DateField getActDate() {
        return actDate;
    }

    public FileUploader getActFileUpload() {
        return actFileUpload;
    }

    public ConditionChangeView() {
        initWidget();
    }

    private ContentPanel cp1;
    private ContentPanel cp2;

    public ContentPanel getCp2() {
        return cp2;
    }

    private void initWidget() {
        container = new VerticalLayoutContainer();
        isChanged = new CheckBox();
        isChanged.setBoxLabel(WidgetUtils.wrapString("В соглашение (контракт) внесены изменения"));

        cause = WidgetUtils.createCommonFilterModelTwinTriggerComboBox("Выберите причину изменения условий соглашения (контракта)");
        causeLabel = createFieldLabelTop(cause, "Изменение условий соглашения (контракта)");

        actNumber = new TextField();
        actNumber.setName("Номер решения (соглашения)");
        actNumber.addValidator(new MaxLengthValidator(255));
        FieldLabel actNumberLabel = createFieldLabelTop(actNumber, "Номер решения (соглашения)");
        actDate = new DateFieldFullDate();
        FieldLabel actDateLabel = createFieldLabelTop(actDate, "Дата решения (соглашения)");

        HorizontalLayoutContainer actRow = createTwoFieldWidgetsRow(actNumberLabel, actDateLabel);

        actFileUpload = new FileUploader();
        actFileUpload.setHeadingText("Текст решения (соглашения)");

        AccordionLayoutContainer.AccordionLayoutAppearance appearance = GWT.<AccordionLayoutContainer.AccordionLayoutAppearance>create(AccordionLayoutContainer.AccordionLayoutAppearance.class);
        MarginData cpMargin = new MarginData(10, 10, 10, 20);

        cp1 = new ContentPanel(appearance);
        cp1.setAnimCollapse(false);
        cp1.setHeading("Общая информация");
        VerticalLayoutContainer cp1V = new VerticalLayoutContainer();
        cp1V.add(isChanged, STD_VC_LAYOUT);
        cp1V.add(causeLabel, STD_VC_LAYOUT);
        cp1.add(cp1V, cpMargin);

        cp2 = new ContentPanel(appearance);
        cp2.setAnimCollapse(false);
        cp2.setHeading("Реквизиты и копия решения (соглашения)");
        VerticalLayoutContainer cp2V = new VerticalLayoutContainer();
        cp2V.add(actRow, HEIGHT60_VC_LAYOUT);
        cp2V.add(actFileUpload, STD_VC_LAYOUT);
        cp2.add(cp2V, cpMargin);

        AccordionLayoutContainer accordion = new AccordionLayoutContainer();
        accordion.setExpandMode(AccordionLayoutContainer.ExpandMode.MULTI);
        accordion.add(cp1);
        accordion.add(cp2);
        accordion.setActiveWidget(cp1);

        cp2.collapse();

        isChanged.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                if (event.getValue() != null && event.getValue()) {
                    causeLabel.show();
                    cp2.show();
                    container.forceLayout();
                } else {
                    causeLabel.hide();
                    cp2.hide();
                }
            }
        });


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
        String formIdStr = (formId == null) ? null : String.valueOf(formId);

        if (viewAttrState.isAttrActive(formIdStr, "isChanged")) {
            cp1.show();
            cp2.show();
        } else {
            cp1.hide();
            cp2.hide();
            isChanged.setValue(null);
            cause.setValue(null);
            actDate.setValue(null);
            actFileUpload.setFiles(new ArrayList<>());
        }

        if (isChanged.getValue() != null && isChanged.getValue()) {
            causeLabel.show();
            cp2.show();

        } else {
            causeLabel.hide();
            cp2.hide();
        }

        container.forceLayout();

//        if (viewAttrState.isAttrActive(formIdStr, "cause")) {
//            causeLabel.show();
//        } else {
//            causeLabel.hide();
//            cause.setValue(null);
//            actDate.setValue(null);
//        }
//
//        if (viewAttrState.isAttrActive(formIdStr, "actDate")) {
//            actDate.show();
//        } else {
//            actDate.hide();
//            actDate.setValue(null);
//        }
//
//        if (viewAttrState.isAttrActive(formIdStr, "actFileUpload")) {
//            actFileUpload.toggle(true);
//        } else {
//            actFileUpload.toggle(false);
//            actFileUpload.setFiles(new ArrayList<>());
//        }

    }

    private static ViewAttrState viewAttrState;

    static {
        viewAttrState = new ViewAttrState();

        viewAttrState.getColumns().addAll(ViewAttrStateColumns.getFormIds());

        viewAttrState.getState().put("isChanged",     Arrays.asList(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // 8.1 // TODO - can be missed, always show
        viewAttrState.getState().put("cause",         Arrays.asList(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // 8.2 // TODO - can be missed, always show
        viewAttrState.getState().put("actNumber" ,    Arrays.asList(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // 8.3 // TODO - can be missed, always show
        viewAttrState.getState().put("actDate",       Arrays.asList(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // 8.4 // TODO - can be missed, always show
        viewAttrState.getState().put("actFileUpload", Arrays.asList(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // 8.5 // TODO - can be missed, always show

    }
}
