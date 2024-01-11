package com.gustavo.utils;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {

    public static Stage currentStage(ActionEvent event) {
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }

    public static Integer tryParseToInt(String str) {

        try {
            return Integer.parseInt(str);

        } catch(NumberFormatException e) {
            return null;
        }
    }

    public static <T> void formatTableColumnDate(TableColumn<T, Date> column, String format) {
        column.setCellFactory(col -> {
            TableCell<T, Date> cell = new TableCell<>() {
                private SimpleDateFormat sdf = new SimpleDateFormat(format);

                @Override
                protected void updateItem(Date item, boolean empty) {
                    super.updateItem(item, empty);

                    if(empty) setText(null);
                    else setText(sdf.format(item));
                }

            };
            return cell;
        });
    }

    public static <T> void formatTableColumnDouble(TableColumn<T, Double> column,
                                                   int decimalPlaces) {
        column.setCellFactory(col -> {
            TableCell<T, Double> cell = new TableCell<T, Double> () {

                @Override
                protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty) setText(null);
                    else {
                        Locale.setDefault(Locale.US);
                        setText(String.format("%." + decimalPlaces + "f", item));
                    }
                }
            };
            return cell;
        });

    }
}
