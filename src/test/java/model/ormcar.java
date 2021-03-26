package model;

import annotations.Column;
import annotations.PrimaryKey;

//create user class
public class ormcar {
    //custom primary key annotation
    @PrimaryKey
    private int id;
    //custom column annotation
    @Column
    private String make;

    @Column
    private String model;

    public ormcar(int id, String make, String model) {
        this.id = id;
        this.make = make;
        this.model = model;
    }
    public ormcar() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Override
    public String toString() {
        return "ormcar{" +
                "id=" + id +
                ", make='" + make + '\'' +
                ", model='" + model + '\'' +
                '}';
    }


}
