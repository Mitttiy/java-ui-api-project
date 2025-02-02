package ru.ibs.gasu.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.AccordionLayoutContainer;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.validator.MaxLengthValidator;
import ru.ibs.gasu.client.widgets.componens.FileUploader;
import ru.ibs.gasu.common.models.SimpleIdNameModel;
import ru.ibs.gasu.common.models.view_attr.ViewAttrState;
import ru.ibs.gasu.common.models.view_attr.ViewAttrStateColumns;

import java.util.ArrayList;
import java.util.Arrays;

import static com.sencha.gxt.cell.core.client.form.TextAreaInputCell.Resizable.VERTICAL;
import static ru.ibs.gasu.client.widgets.WidgetUtils.*;

/**
 * Раздел "Описание объекта".
 */
public class ObjectDescriptionView implements IsWidget {
    private VerticalLayoutContainer container;

    private TextArea objectNameField;
    private TextArea objectCharacteristic;
    private CheckBox isPropertyStaysWithPrivateSide;
    private CheckBox isNewPropertyBeGivenToPrivateSide;
    private CheckBox isObjectImprovementsGiveAway;
    private ComboBox<SimpleIdNameModel> rentObject;
    private VerticalLayoutContainer rentObjectContainer;
    /**
     * Энергетический паспорт объекта
     */
    private FileUploader fileUploader;
    private TechEcononomyIndicatorsWidget techEcononomyIndicatorsWidget;
    private EemWidget eemWidget;
    private CheckBox isObjInListWithConcessionalAgreements;

    public ContentPanel getCp2() {
        return cp2;
    }

    private ContentPanel cp2;
    private ContentPanel cp3;

    public VerticalLayoutContainer getContainer() {
        return container;
    }

    public TextArea getObjectNameField() {
        return objectNameField;
    }

    public TextArea getObjectCharacteristic() {
        return objectCharacteristic;
    }

    public CheckBox getIsPropertyStaysWithPrivateSide() {
        return isPropertyStaysWithPrivateSide;
    }

    public CheckBox getIsNewPropertyBeGivenToPrivateSide() {
        return isNewPropertyBeGivenToPrivateSide;
    }

    public CheckBox getIsObjectImprovementsGiveAway() {
        return isObjectImprovementsGiveAway;
    }

    public ComboBox<SimpleIdNameModel> getRentObject() {
        return rentObject;
    }

    public FileUploader getFileUploader() {
        return fileUploader;
    }


    public TechEcononomyIndicatorsWidget getTechEcononomyIndicatorsWidget() {
        return techEcononomyIndicatorsWidget;
    }

    public EemWidget getEemWidget() {
        return eemWidget;
    }

    public CheckBox getIsObjInListWithConcessionalAgreements() {
        return isObjInListWithConcessionalAgreements;
    }

    public ObjectDescriptionView() {
        container = new VerticalLayoutContainer();

        objectNameField = new TextArea();
        FieldLabel projectNameFieldLabel = createFieldLabelTop(objectNameField, "Наименование объекта");
        projectNameFieldLabel.setId("objectNameField");

        objectNameField.setName("Наименование объекта");
        objectNameField.addValidator(new MaxLengthValidator(5000));
        objectNameField.setResizable(VERTICAL);
//        objectNameField.setHeight(100);

        objectCharacteristic = new TextArea();
        objectCharacteristic.setResizable(VERTICAL);
//        objectCharacteristic.setHeight(100);
        objectCharacteristic.setName("Краткая характеристика объекта/товара");
        objectCharacteristic.addValidator(new MaxLengthValidator(5000));
        FieldLabel objectCharacteristicLabel = createFieldLabelTop(objectCharacteristic, "Краткая характеристика объекта/товара");
        objectCharacteristicLabel.hide(); // GASU30-1352 - hide
        objectCharacteristicLabel.setId("objectCharacteristic");

        isPropertyStaysWithPrivateSide = new CheckBox();
        isPropertyStaysWithPrivateSide.setBoxLabel(
                wrapString("Создаваемое Концессионером/ Частной стороной имущество, не входящее в Объект соглашения, " +
                        "остается у Концессионера/ Частной стороны"));
        isPropertyStaysWithPrivateSide.hide(); // GASU30-1352 - hide
        isNewPropertyBeGivenToPrivateSide = new CheckBox();
        isNewPropertyBeGivenToPrivateSide.setBoxLabel(wrapString("В рамках соглашения Концессионеру/Частной стороне передается иное имущество"));
        isNewPropertyBeGivenToPrivateSide.hide(); // GASU30-1352 - hide

        isObjectImprovementsGiveAway = new CheckBox();
        isObjectImprovementsGiveAway.setBoxLabel(wrapString("Осуществляемые  арендатором отделимые улучшения объекта по истечении срока действия договора передаются арендодателю"));

        rentObject = createCommonFilterModelComboBox("Объектом договора аренды является");
        FieldLabel rentObjectLabel = createFieldLabelTop(rentObject, "Объектом договора аренды является");
        rentObjectContainer = new VerticalLayoutContainer();
        rentObjectContainer.add(rentObjectLabel, STD_VC_LAYOUT);


        fileUploader = new FileUploader();
        fileUploader.setHeadingText("Энергетический паспорт объекта/результаты энергообследования (Приложение из контракта с перечнем товаров):");

        techEcononomyIndicatorsWidget = new TechEcononomyIndicatorsWidget();
        eemWidget = new EemWidget();

        isObjInListWithConcessionalAgreements = new CheckBox();
        isObjInListWithConcessionalAgreements.setBoxLabel(wrapString("Объект соглашения  включен в перечень объектов, в отношении которых планируется заключение концессионных соглашений"));

        AccordionLayoutContainer.AccordionLayoutAppearance appearance = GWT.<AccordionLayoutContainer.AccordionLayoutAppearance>create(AccordionLayoutContainer.AccordionLayoutAppearance.class);
        MarginData cpMargin = new MarginData(10, 10, 10, 20);
        ContentPanel cp1 = new ContentPanel(appearance);
        cp1.setAnimCollapse(false);
        cp1.setHeading("Общая информация");
        VerticalLayoutContainer cp1V = new VerticalLayoutContainer();
        cp1V.add(projectNameFieldLabel, STD_VC_LAYOUT);
//        cp1V.add(objectCharacteristicLabel, STD_VC_LAYOUT);
//        cp1V.add(isPropertyStaysWithPrivateSide, STD_VC_LAYOUT);
//        cp1V.add(isNewPropertyBeGivenToPrivateSide, STD_VC_LAYOUT);
//        cp1V.add(isObjectImprovementsGiveAway, STD_VC_LAYOUT);
//        cp1V.add(rentObjectContainer, STD_VC_LAYOUT);
//        cp1V.add(fileUploader, STD_VC_LAYOUT);
        cp1.add(cp1V, cpMargin);

        cp2 = new ContentPanel(appearance);
        cp2.setAnimCollapse(false);
        cp2.setHeading("Технико-экономические показатели");
        cp2.add(techEcononomyIndicatorsWidget.asWidget(), cpMargin);

        techEcononomyIndicatorsWidget.getTepGrid().addViewReadyHandler(viewReadyEvent -> {
            if (techEcononomyIndicatorsWidget.getTepGrid().getTreeStore().getAll().size() > 0)
                cp2.expand();
        });

//        cp3 = new ContentPanel(appearance);
//        cp3.setAnimCollapse(false);
//        cp3.setHeading("План энергоэффективных мероприятий (ЭЭМ)");
//        cp3.add(eemWidget.asWidget(), cpMargin);

        AccordionLayoutContainer accordion = new AccordionLayoutContainer();
        accordion.setExpandMode(AccordionLayoutContainer.ExpandMode.MULTI);
        accordion.add(cp1);
        accordion.add(cp2);
//        accordion.add(cp3);
        accordion.setActiveWidget(cp1);
        cp2.collapse();
//        cp3.collapse();
        container.add(accordion, STD_VC_LAYOUT);
    }

    /**
     * Обновить форму (скрыть + очистить/показать поля) в зависимости от формы реализации проекта
     * и способа инициации проекта.
     *
     * @param formId - форма реализации проекта
     * @param initId - способ инициации проекта
     */
    public void update(Long formId, Long initId) {
        String formIdStr = String.valueOf(formId);

        // checkbox isPropertyStaysWithPrivateSide
        if (viewAttrState.isAttrActive(formIdStr, "isPropertyStaysWithPrivateSide")) {
            isPropertyStaysWithPrivateSide.show();
        } else {
            isPropertyStaysWithPrivateSide.hide();
            isPropertyStaysWithPrivateSide.setValue(null);
        }
        // checkbox isNewPropertyBeGivenToPrivateSide
        if (viewAttrState.isAttrActive(formIdStr, "isNewPropertyBeGivenToPrivateSide")) {
            isNewPropertyBeGivenToPrivateSide.show();
        } else {
            isNewPropertyBeGivenToPrivateSide.hide();
            isNewPropertyBeGivenToPrivateSide.setValue(null);
        }
        // checkbox isObjectImprovementsGiveAway
        if (viewAttrState.isAttrActive(formIdStr, "isObjectImprovementsGiveAway")) {
            isObjectImprovementsGiveAway.show();
        } else {
            isObjectImprovementsGiveAway.hide();
            isObjectImprovementsGiveAway.setValue(null);
        }
        // combobox rentObject
        if (viewAttrState.isAttrActive(formIdStr, "rentObject")) {
            rentObjectContainer.show();
        } else {
            rentObjectContainer.hide();
            rentObject.setValue(null);
        }
        // file fileUploader
        if (viewAttrState.isAttrActive(formIdStr, "fileUploader")) {
            fileUploader.toggle(true);
        } else {
            fileUploader.toggle(false);
            fileUploader.setFiles(new ArrayList<>());
        }

        // table techEcononomyIndicatorsWidget
        if (viewAttrState.isAttrActive(formIdStr, "techEcononomyIndicatorsWidget")) {
            cp2.show();
        } else {
            cp2.hide();
            techEcononomyIndicatorsWidget.getTreeStore().clear();
        }
        // table eemWidget
//        if (viewAttrState.isAttrActive(formIdStr, "eemWidget")) {
//            cp3.show();
//        } else {
//            cp3.hide();
//            eemWidget.getEemGrid().getStore().clear();
//        }
    }

    @Override
    public Widget asWidget() {
        return container;
    }

    private static ViewAttrState viewAttrState;

    static {
        viewAttrState = new ViewAttrState();

        viewAttrState.getColumns().addAll(ViewAttrStateColumns.getFormIds());

        viewAttrState.getState().put("objectNameField", Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)); // TODO - can be missed, always show

        // GASU30-1352 - hide
        // viewAttrState.getState().put("objectCharacteristic",                        Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)); // GASU30-1352 - for restore: uncomment
        viewAttrState.getState().put("objectCharacteristic", Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // GASU30-1352 - for restore: delete

        // GASU30-1352 - hide
        // viewAttrState.getState().put("isPropertyStaysWithPrivateSide",              Arrays.asList(1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // GASU30-1352 - for restore: uncomment
        viewAttrState.getState().put("isPropertyStaysWithPrivateSide", Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // GASU30-1352 - for restore: delete

        // GASU30-1352 - hide
        // viewAttrState.getState().put("isNewPropertyBeGivenToPrivateSide",           Arrays.asList(1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // GASU30-1352 - for restore: uncomment
        viewAttrState.getState().put("isNewPropertyBeGivenToPrivateSide", Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // GASU30-1352 - for restore: delete

        viewAttrState.getState().put("isObjectImprovementsGiveAway", Arrays.asList(0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0)); // TODO - can be missed, always show
        viewAttrState.getState().put("rentObject", Arrays.asList(0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0)); // TODO - can be missed, always show
        viewAttrState.getState().put("fileUploader", Arrays.asList(0, 0, 0, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0)); // TODO - can be missed, always show
        viewAttrState.getState().put("techEcononomyIndicatorsWidget", Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1)); // TODO - can be missed, always show
        viewAttrState.getState().put("eemWidget", Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0));
    }
}
