package com.cegep.sportify;

import com.cegep.sportify.model.Equipment;

public interface EquipmentListItemClickListener {

    void onEquipmentClicked(Equipment equipment);

    void onFavoriteButtonClicked(Equipment equipment, boolean favorite);

}
