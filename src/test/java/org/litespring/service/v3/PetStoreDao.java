package org.litespring.service.v3;

public class PetStoreDao {
    private int version;

    public PetStoreDao(int version) {
        this.version = version;
    }

    public int getVersion() {
        return version;
    }
}
