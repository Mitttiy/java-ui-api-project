package ru.ibs.gasu.client.widgets;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.core.client.util.Padding;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.validator.MaxLengthValidator;
import ru.ibs.gasu.client.widgets.componens.IconButton;
import ru.ibs.gasu.common.models.GiProjectStatus;
import ru.ibs.gasu.common.models.SimpleIdNameModel;

import static com.sencha.gxt.cell.core.client.form.TextAreaInputCell.Resizable.VERTICAL;
import static ru.ibs.gasu.client.widgets.WidgetUtils.*;

public class CommentsView implements IsWidget {
    private TextArea comment;
    private TextArea contacts;
    private CheckBox isAlwaysDraftState;
    private FieldLabel giProjectStatusLabel;
    private ComboBox<SimpleIdNameModel> projectStatus;

    public TextArea getComment() {
        return comment;
    }

    public TextArea getContacts() {
        return contacts;
    }
    public CheckBox getIsAlwaysDraftState() {
        return isAlwaysDraftState;
    }

    public ComboBox<SimpleIdNameModel> getProjectStatus() {
        return projectStatus;
    }

    public void hideProjectStatus() {
        giProjectStatusLabel.setVisible(false);
    }

    private VerticalLayoutContainer container;

    public VerticalLayoutContainer getContainer() {
        return container;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    private Long projectId;

    public CommentsView() {
        initWidget();
    }

    private void initWidget() {
        container = new VerticalLayoutContainer();

        comment = new TextArea();
        comment.setName("Комментарий");
        comment.addValidator(new MaxLengthValidator(5000));
        comment.setResizable(VERTICAL);
//        comment.setHeight(100);
        FieldLabel commentLabel = createFieldLabelTop(comment, "Комментарии");

        contacts = new TextArea();
        contacts.setName("Комментарий");
        contacts.addValidator(new MaxLengthValidator(5000));
        contacts.setResizable(VERTICAL);
//        comment.setHeight(100);
        FieldLabel contactsLabel = createFieldLabelTop(contacts, "Контактные данные");

        isAlwaysDraftState = new CheckBox();
        isAlwaysDraftState.setBoxLabel("Проект принудительно в статусе Черновик");
        isAlwaysDraftState.getElement().setPadding(new Padding(50, 0, 10, 0));

        projectStatus = createCommonFilterModelComboBox("Выберите статус проекта");
        giProjectStatusLabel = createFieldLabelTop(projectStatus, "Статус проекта");

        isAlwaysDraftState.addValueChangeHandler(_valueChangeEvent -> {
            if (_valueChangeEvent.getValue()) {
                selectComboById(getProjectStatus(), GiProjectStatus.DRAFT.getId());
                getProjectStatus().setEnabled(false);
            } else {
                getProjectStatus().setEnabled(true);
            }
        });

        container.add(commentLabel, STD_VC_LAYOUT);
        container.add(contactsLabel, STD_VC_LAYOUT);
        container.add(isAlwaysDraftState, STD_VC_LAYOUT);
        container.add(giProjectStatusLabel, STD_VC_LAYOUT);

        HorizontalLayoutContainer horizontalLayoutContainer = new HorizontalLayoutContainer();
        IconButton downloadButton = new IconButton("Скачать файлы проекта", "fa fa-download");
        horizontalLayoutContainer.add(downloadButton, new HorizontalLayoutContainer.HorizontalLayoutData(0.2, 30, new Margins(15, 20, 35, 0)));
        container.add(horizontalLayoutContainer);

        downloadButton.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                Window.open("api/files/project/download" + "/" + projectId, "_blank", "");
            }
        });
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

    }
}
