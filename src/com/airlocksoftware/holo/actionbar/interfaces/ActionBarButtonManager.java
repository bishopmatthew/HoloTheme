package com.airlocksoftware.holo.actionbar.interfaces;

import com.airlocksoftware.holo.actionbar.ActionBarButton;

public interface ActionBarButtonManager {

	public void addButton(ActionBarButton button);

	public void removeButton(ActionBarButton button);

	public void findButtonById(int id);

}
