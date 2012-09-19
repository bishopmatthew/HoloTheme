package com.airlocksoftware.holo.interfaces;

import com.airlocksoftware.holo.pages.Page;

/** Any view that is designed to hold Pages should implement this interface **/
public interface PageHolder {

	public Page getPage();

	public void setPage(Page page);

	public PageHolder addPage(Page page);

	public PageHolder removePage(Page page);

}
