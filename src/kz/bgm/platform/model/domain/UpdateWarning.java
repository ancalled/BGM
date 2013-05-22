package kz.bgm.platform.model.domain;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateWarning {

    private Integer number;
    private Integer row;
    private String column;
    private String message;

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void parseMessage(String message) {
        Pattern p = Pattern.compile(REGEXP);
        Matcher m = p.matcher(message);

        if (m.matches()) {

            this.message = m.group(1);
            this.column = m.group(2);
            this.row = Integer.parseInt(m.group(3));
        }

    }

    public static final String REGEXP = "(.*) for column '([^']+)' at row ([0-9]+)";


}
