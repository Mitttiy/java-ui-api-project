package ru.ibs.gasu.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.i18n.client.constants.TimeZoneConstants;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.cell.core.client.form.TextInputCell;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.core.client.util.Padding;
import com.sencha.gxt.data.shared.Converter;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.*;
import com.sencha.gxt.widget.core.client.form.error.DefaultEditorError;
import ru.ibs.gasu.client.api.GchpApi;
import ru.ibs.gasu.client.utils.StringUtils;
import ru.ibs.gasu.client.widgets.componens.FileUploader;
import ru.ibs.gasu.client.widgets.componens.TwinTriggerComboBox;
import ru.ibs.gasu.client.widgets.multiselectcombobox.MultiSelectComboBox;
import ru.ibs.gasu.common.models.SimpleIdNameModel;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;

public class WidgetUtils {
    /**
     * VerticalLayoutData(1, -1)
     */
    public static VerticalLayoutContainer.VerticalLayoutData STD_VC_LAYOUT = new VerticalLayoutContainer.VerticalLayoutData(1, -1);
    /**
     * VerticalLayoutData(1, 60)
     */
    public static VerticalLayoutContainer.VerticalLayoutData HEIGHT60_VC_LAYOUT = new VerticalLayoutContainer.VerticalLayoutData(1, 60);
    public static String GRID_EMPTY_TEXT_VAR1 = "Данные отсутствуют";


    public static FieldLabel createFieldLabelTop(Widget widget, String text) {
        FieldLabel fieldLabel = new FieldLabel(widget, text);
        fieldLabel.setLabelAlign(FormPanel.LabelAlign.TOP);
        fieldLabel.getElement().setPadding(new Padding(0));
        return fieldLabel;
    }

    public static FieldLabel createFieldLabelTop(Widget widget, SafeHtml text) {
        FieldLabel fieldLabel = new FieldLabel(widget, text);
        fieldLabel.setLabelAlign(FormPanel.LabelAlign.TOP);
        fieldLabel.getElement().setPadding(new Padding(0));
        return fieldLabel;
    }

    public static VerticalLayoutContainer createVerticalGap(Integer height) {
        VerticalLayoutContainer vc = new VerticalLayoutContainer();
        vc.setHeight(height);
        return vc;
    }

    public static HTML createHtml(String text) {
        HTML html = new HTML(text);
        html.getElement().getStyle().setFontSize(13, Style.Unit.PX);
        return html;
    }

    public static String doubleToString(Double d) {
        if (d == null) return "";
        return NumberFormat.getFormat("#,##0.0##############").format(BigDecimal.valueOf(d));
    }

    /**
     * Создает текстовое поле для корректного редактирования значений Double (через точку).
     * По-умолчанию происходит переключение с точки на запятую и в дальнейшем при редактировании
     * всплывает tooltip с ошибкой.
     *
     * @return
     */
    public static TextField createEditField() {
        TextField editField = new TextField(new TextInputCell(), new PropertyEditor<String>() {
            @Override
            public String parse(CharSequence text) {
                return text.toString().replace(" ", "");
            }

            @Override
            public String render(String object) {
                return object == null ? "" : object.replace(" ", "").replace(",", ".");
            }
        });

        editField.addValidator((editor, value) -> {
            List<EditorError> errors = new ArrayList<>();
            try {
                if (value != null && !value.isEmpty()) {
                    Double.valueOf(value);
                }
            } catch (Exception e) {
                String message = "Необходимо ввести числовое значение";
                if (value.matches("[\\-0-9,]+")) {
                    message = "Ввод осуществляется через точку";
                }
                errors.add(new DefaultEditorError(editor, message, value));
                editField.markInvalid(message);
            }
            return errors;
        });
        editField.setAutoValidate(true);
        return editField;
    }

    /**
     * TextField
     *
     * @return
     */
    public static TextField createEditTextField() {
        TextField editField = new TextField(new TextInputCell(), new PropertyEditor<String>() {
            @Override
            public String parse(CharSequence text) {
                return text.toString();
            }

            @Override
            public String render(String object) {
                return object == null ? "" : object;
            }
        });

        return editField;
    }

    public static String formatDate(Long value, String to) {
        if (value == null) {
            return "";
        }
        return formatDate(getDate(value), to);
    }

    public static String formatDate(Date value, String to) {
        if (value == null) return "";
        return DateTimeFormat.getFormat(to).format(value);
    }

    public static Date parseDate(String dateString) {
        if (dateString == null || dateString.isEmpty()) return null;
        return DateTimeFormat.getFormat("dd.MM.yyyy").parse(dateString);
    }

    public static Long parseDateToLong(String dateString) {
        if (dateString == null || dateString.isEmpty()) return null;
        return getDate(DateTimeFormat.getFormat("dd.MM.yyyy").parse(dateString));
    }


    public static HTML createHeadingHtml(String text) {
        SafeHtml safeHtml = new SafeHtmlBuilder().appendHtmlConstant("<hr style=\"border-top: dashed 1px;\">").appendEscaped(text).toSafeHtml();
        HTML html = new HTML(safeHtml);
        html.getElement().getStyle().setFontSize(17, Style.Unit.PX);
        html.getElement().getStyle().setPaddingTop(10, Style.Unit.PX);
        html.getElement().getStyle().setPaddingBottom(10, Style.Unit.PX);
        html.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        return html;
    }

    // Обертка, чтобы каждый раз не проверять на NPE.

    /**
     * Обертка для получения значений из вложенных классов, чтобы каждый раз не проверять на NPE.
     * <pre>
     *   getStringValOrEmpty(() -> object.getObjectField().getName());
     * </pre>
     *
     * @param supplier
     * @return
     */
    public static String getStringValOrEmpty(Supplier<String> supplier) {
        try {
            String str = supplier.get();
            return "null".equals(str) ? "" : str;
        } catch (Exception ex) {
            return "";
        }
    }

    public static String getStringValOrDefault(Supplier<String> supplier, String defaultStr) {
        try {
            String str = supplier.get();
            return str == null || "null".equals(str) ? defaultStr : str;
        } catch (Exception ex) {
            return defaultStr;
        }
    }

    public static void selectComboById(ComboBox<SimpleIdNameModel> comboBox, String id) {
        if (id == null) return;
        SimpleIdNameModel model = comboBox.getStore().findModel(new SimpleIdNameModel(id));
        comboBox.setValue(model, true);
    }

    public static void selectComboByIdFire(ComboBox<SimpleIdNameModel> comboBox, String id) {
        if (id == null) return;
        SimpleIdNameModel model = comboBox.getStore().findModel(new SimpleIdNameModel(id));
        comboBox.setValue(model, true);
    }

    public static void selectComboById(ComboBox<SimpleIdNameModel> comboBox, Long id) {
        if (id == null) return;
        selectComboById(comboBox, String.valueOf(id));
    }

    public static void selectMulticomboById(MultiSelectComboBox<SimpleIdNameModel> multi, List<String> ids) {
        if (ids == null) return;
        for (String id : ids) {
            multi.select(id);
        }
    }


    public static FieldLabel createFieldLabelLeft(Widget widget, String text) {
        FieldLabel fieldLabel = new FieldLabel(widget, text);
        fieldLabel.setLabelAlign(FormPanel.LabelAlign.LEFT);
        return fieldLabel;
    }

    public static Long getRandId() {
        return new Date().getTime() + new Random().nextInt();
    }

    public static Converter<String, Date> createStringDateConverter() {
        return new Converter<String, Date>() {
            @Override
            public String convertFieldValue(Date object) {
                return formatDate(object, "dd.MM.yyyy");
            }

            @Override
            public Date convertModelValue(String object) {
                return parseDate(object);
            }
        };
    }

    public static ComboBox<SimpleIdNameModel> createCommonFilterModelComboBox(String emptyText) {
        ComboBox<SimpleIdNameModel> comboBox = new ComboBox<>(
                new ListStore<>(item -> item.getId()),
                item -> item.getName(),
                new AbstractSafeHtmlRenderer<SimpleIdNameModel>() {
                    @Override
                    public SafeHtml render(SimpleIdNameModel object) {
                        return WidgetUtils.wrapString(object.getName());
                    }
                });
        comboBox.setEmptyText(emptyText);
        comboBox.setEditable(false);
        comboBox.setWidth(300);
        comboBox.setTriggerAction(ComboBoxCell.TriggerAction.ALL);
        return comboBox;
    }

    public static TwinTriggerComboBox<SimpleIdNameModel> createCommonFilterModelTwinTriggerComboBox(String emptyText) {
        TwinTriggerComboBox<SimpleIdNameModel> comboBox = new TwinTriggerComboBox<>(
                new ListStore<>(item -> item.getId()),
                item -> item.getName(),
                new AbstractSafeHtmlRenderer<SimpleIdNameModel>() {
                    @Override
                    public SafeHtml render(SimpleIdNameModel object) {
                        return WidgetUtils.wrapString(object.getName());
                    }
                });
        comboBox.setEmptyText(emptyText);
        comboBox.setEditable(false);
        comboBox.setWidth(300);
        comboBox.setTriggerAction(ComboBoxCell.TriggerAction.ALL);
        return comboBox;
    }

    public static MultiSelectComboBox<SimpleIdNameModel> createCommonFilterModelMultiSelectComboBox(String emptyText) {
        MultiSelectComboBox<SimpleIdNameModel> multiSelectComboBox = new MultiSelectComboBox<>(
                item -> item.getId(),
                item -> item.getName(),
                null,
                false);
        multiSelectComboBox.setEmptyText(emptyText);
        multiSelectComboBox.setEditable(false);

        return multiSelectComboBox;
    }

    public static Comparator<String> createStringDoubleComparator() {
        return Comparator.comparingDouble(new ToDoubleFunction<String>() {
            @Override
            public double applyAsDouble(String value) {
                if (value == null || value.isEmpty())
                    return 0;
                else
                    return Double.parseDouble(value);
            }
        });

    }

    public static SafeHtml wrapString(String str) {
        SafeHtmlBuilder sb = new SafeHtmlBuilder();
        sb.appendHtmlConstant("<span style='white-space: normal;'>");
        sb.appendEscaped(str);
        sb.appendHtmlConstant("</span>");
        return sb.toSafeHtml();
    }

    public static SafeHtml wrapStringForTreeGrid(String str) {
        SafeHtmlBuilder sb = new SafeHtmlBuilder();
        sb.appendHtmlConstant("<span style='white-space: normal; display: inline-block; padding-right: 40px;'>");
        sb.appendEscaped(str);
        sb.appendHtmlConstant("</span>");
        return sb.toSafeHtml();
    }

    public static SafeHtml noWrapString(String str) {
        SafeHtmlBuilder sb = new SafeHtmlBuilder();
        sb.appendHtmlConstant("<span style='white-space: nowrap;'>");
        sb.appendEscaped(str);
        sb.appendHtmlConstant("</span>");
        return sb.toSafeHtml();
    }

    public static TimeZoneConstants timeZoneConstants = GWT.create(TimeZoneConstants.class);

    /**
     * Создать объект даты, соответствующий указанному времени (в Moscow TimeZone), со смещением, соответствующим пользовательскому времени
     * Важно !!! Даты нужно создавать только с использованием данного метода, избегая new Date()
     */
    public static Date getDate(Long time) {
        if (time == null) return null;
        Date temp = new Date(time);
        try {
            // сдвиг по времени для московской TZ для конкретной даты (-3 часа)
            int moscowOffset = -180 * 60 * 1000;
            return new Date(temp.getTime() + temp.getTimezoneOffset() * 60 * 1000 - moscowOffset);
        } catch (Exception ex) {
            return temp;
        }
    }

    /**
     * Получить таймстамп, соответствующий переданной дате, но по московскому времени.
     * Например: 2018-01-01 04:00:00+0 -> таймстамп от 2018-01-01 04:00:00+03 (по москве)
     * <p>
     * Важно !!! Даты нужно конвертировать перед отправкой на сервер только с использованием данного метода, избегая Date#getTime()
     */
    public static Long getDate(Date date) {
        if (date == null) {
            return null;
        }
        int moscowOffset = -180 * 60 * 1000;
        return date.getTime() - date.getTimezoneOffset() * 60 * 1000 + moscowOffset;
    }

    /**
     * Форматировать дату (Date) в строку в формате Constant.DATE_FORMAT
     *
     * @param value
     * @return
     */
    public static String formatDate(Date value) {
        if (value == null) return "";
        return DateTimeFormat.getFormat("dd.MM.yyyy").format(value);
    }

    public static void setFileApiUrlsToUploader(FileUploader uploader) {
        uploader.setFileDownloadUrl(GchpApi.FILE_DOWNLOAD_PATH);
        uploader.getUploader().setUploadUrl(GchpApi.FILE_UPLOAD_PATH);
    }

    public static HorizontalLayoutContainer createTwoFielsRowFileUploader(FileUploader leftWidget, Widget rightWidget) {
        HorizontalLayoutContainer container = new HorizontalLayoutContainer();
        container.add(leftWidget, new HorizontalLayoutContainer.HorizontalLayoutData(0.5, 1));
        container.add(rightWidget, new HorizontalLayoutContainer.HorizontalLayoutData(0.5, 1, new Margins(0, 0, 0, 0)));
        return container;
    }

    public static HorizontalLayoutContainer createTwoFieldWidgetsRow(Widget leftWidget, Widget rightWidget) {
        HorizontalLayoutContainer container = new HorizontalLayoutContainer();
        container.add(leftWidget, new HorizontalLayoutContainer.HorizontalLayoutData(0.5, 1));
        container.add(rightWidget, new HorizontalLayoutContainer.HorizontalLayoutData(0.5, 1, new Margins(0, 0, 0, 5)));
        return container;
    }

    public static HorizontalLayoutContainer createTwoFieldWidgetsRow(Widget leftWidget, Widget rightWidget, double leftRate, double rightRate) {
        HorizontalLayoutContainer container = new HorizontalLayoutContainer();
        container.add(leftWidget, new HorizontalLayoutContainer.HorizontalLayoutData(leftRate, 1));
        container.add(rightWidget, new HorizontalLayoutContainer.HorizontalLayoutData(rightRate, 1, new Margins(0, 0, 0, 5)));
        return container;
    }


    public static HorizontalLayoutContainer createThreeFieldWidgetsRow(Widget leftWidget, Widget centralWidget, Widget rightWidget) {
        HorizontalLayoutContainer container = new HorizontalLayoutContainer();
        double d = 1.0 / 3.0;
        container.add(leftWidget, new HorizontalLayoutContainer.HorizontalLayoutData(d, 1));
        container.add(centralWidget, new HorizontalLayoutContainer.HorizontalLayoutData(d, 1, new Margins(0, 0, 0, 5)));
        container.add(rightWidget, new HorizontalLayoutContainer.HorizontalLayoutData(d, 1, new Margins(0, 0, 0, 5)));
        return container;
    }

    public static HorizontalLayoutContainer createThreeFieldWidgetsRow(Widget leftWidget, Widget centralWidget, Widget rightWidget, double left, double center, double right) {
        HorizontalLayoutContainer container = new HorizontalLayoutContainer();
        container.add(leftWidget, new HorizontalLayoutContainer.HorizontalLayoutData(left, 1));
        container.add(centralWidget, new HorizontalLayoutContainer.HorizontalLayoutData(center, 1, new Margins(0, 0, 0, 5)));
        container.add(rightWidget, new HorizontalLayoutContainer.HorizontalLayoutData(right, 1, new Margins(0, 0, 0, 5)));
        return container;
    }

    public static HorizontalLayoutContainer createThreeFieldWidgetsNdsDateRow(Widget leftWidget, Widget centralWidget, Widget rightWidget) {
        HorizontalLayoutContainer container = new HorizontalLayoutContainer();
        double d = 1.0 / 3.0;
        container.add(leftWidget, new HorizontalLayoutContainer.HorizontalLayoutData(0.1, 1));
        container.add(centralWidget, new HorizontalLayoutContainer.HorizontalLayoutData(0.15, 1, new Margins(0, 0, 0, 5)));
        container.add(rightWidget, new HorizontalLayoutContainer.HorizontalLayoutData(0.15, 1, new Margins(0, 0, 0, 5)));
        return container;
    }

    public static Double calcYearsBetweenDates(Date date1, Date date2) {
        if (date1 == null || date2 == null) return null;
        double res = (double) (date1.getTime() - date2.getTime()) / (1000L * 60L * 60L * 24L * 365L);
        return Math.round(res * 100.0) / 100.0;
    }

    public static void toggleHorizontalLayoutContainerColumn(HorizontalLayoutContainer container, String columnId, boolean toggle) {
        try {
            if (container == null) {
                GWT.log("hideHorizontalLayoutContainerColumn - container is empty.");
                return;
            }

            if (StringUtils.isEmpty(columnId)) {
                GWT.log("hideHorizontalLayoutContainerColumn - columnId is empty.");
                return;
            }

            container.forEach(x -> {
                Component component = (Component) x.asWidget();

                if (columnId.equals(component.getId())) {
                    GWT.log("hideHorizontalLayoutContainerColumn - find column by columnId = " + columnId + ", toggle = " + toggle);
                    if (toggle) {
                        component.show();
                    } else {
                        component.hide();
                    }
                }
            });
        } catch (Exception e) {
            GWT.log("hideHorizontalLayoutContainerColumn - error on find column by columnId = " + columnId + ", toggle = " + toggle, e);
        }
    }

    public static <T> ValueProvider<T, String> createReadOnlyVP(Function<T, String> function, String path) {
        return new ValueProvider<T, String>() {
            @Override
            public String getValue(T object) {
                return function.apply(object);
            }

            @Override
            public void setValue(T object, String value) {
            }

            @Override
            public String getPath() {
                return path;
            }
        };
    }

    public static String validation(TextArea textArea, Integer maxValue) {
        if (textArea.getCurrentValue() != null && textArea.getCurrentValue().length() > maxValue) {
            return textArea.getName() + ", ";
        }
        return "";
    }

    public static String validation(TextField textField, Integer maxValue) {
        if (textField.getCurrentValue() != null && textField.getCurrentValue().length() > maxValue) {
            return textField.getName() + ", ";
        }
        return "";
    }
}
