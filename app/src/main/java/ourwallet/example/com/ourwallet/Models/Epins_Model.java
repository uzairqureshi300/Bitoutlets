package ourwallet.example.com.ourwallet.Models;

/**
 * Created by uzair on 22/05/2017.
 */

public class Epins_Model {
    private String  epin_id;
    private String  pins;
    private String  status;
    private String  dateadded;
    private String  amount;
    private String  used_by;
    private String  user_id;

    public String getEpin_id() {
        return epin_id;
    }

    public void setEpin_id(String epin_id) {
        this.epin_id = epin_id;
    }

    public String getPins() {
        return pins;
    }

    public void setPins(String pins) {
        this.pins = pins;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDateadded() {
        return dateadded;
    }

    public void setDateadded(String dateadded) {
        this.dateadded = dateadded;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getUsed_by() {
        return used_by;
    }

    public void setUsed_by(String used_by) {
        this.used_by = used_by;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
