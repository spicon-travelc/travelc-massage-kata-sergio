
package com.trc.massage.binding;

import java.util.List;

public class Response {

    private String error;
    private List<Massage> massages;

    public List<Massage> getMassages() {
        return massages;
    }

    public void setMassages(List<Massage> massages) {
        this.massages = massages;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
