package kz.bgm.platform.utils.jasperreports;


import java.util.List;

public class BasketReport<Track> extends JasReport {

    private String customerName;
    private String company;
    private String contract;

    private List<Track> tracks;

    public void setData(List<Track> data) {
        tracks = data;
    }

    @Override
    public List<Track> getData() {
        return tracks;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }
}

