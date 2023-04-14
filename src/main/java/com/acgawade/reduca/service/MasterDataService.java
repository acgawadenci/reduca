package com.acgawade.reduca.service;

import com.acgawade.reduca.model.MasterData;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MasterDataService {
    MasterData data;

    public MasterData fetchMasterData() {
        try {
            data = new MasterData();
            data.setLocations(List.of("Dublin 1", "Dublin 2", "Dublin 3", "Dublin 4",
                    "Dublin 5", "Dublin 6", "Dublin 7", "Dublin 8", "Dublin 9", "Dublin 10",
                    "Dublin 11", "Dublin 12", "Dublin 13", "Dublin 14", "Dublin 15", "Dublin 16",
                    "Dublin 17", "Dublin 18", "Dublin 19", "Dublin 20", "Dublin 21", "Dublin 22"));
            data.setProductType(List.of("Electronics", "Furniture", "Laptop", "Mobile", "Vehicles",
                    "Apparels", "Footwear", "Hardware", "Tools", "Crockery"));
        } catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        return data;
    }
}
