package org.litespring.service.v3;

public class MultiConstructorPetStoreService {
    private PetStoreDao v1;
    private PetStoreDao v2;
    private String name;
    private int num;

    public MultiConstructorPetStoreService(PetStoreDao v1, PetStoreDao v2) {
        this.v1 = v1;
        this.v2 = v2;
    }

    public MultiConstructorPetStoreService(String name, String num) {
        this.name = name;
        this.num = -1;
    }

    public MultiConstructorPetStoreService(String name, int num) {
        this.name = name;
        this.num = num;
    }

    public PetStoreDao getV1() {
        return v1;
    }

    public PetStoreDao getV2() {
        return v2;
    }

    public String getName() {
        return name;
    }

    public int getNum() {
        return num;
    }
}