package com.example.front_village.gogi.Entity;



public class Menu {

	private Long menuNo;  //메뉴번호

	private String menuNm;  //메뉴이름

	private String menuPrice;  //메뉴가격


	public Long getMenuNo() {
		return menuNo;
	}

	public void setMenuNo(Long menuNo) {
		this.menuNo = menuNo;
	}

	public String getMenuNm() {
		return menuNm;
	}

	public void setMenuNm(String menuNm) {
		this.menuNm = menuNm;
	}

	public String getMenuPrice() {
		return menuPrice;
	}

	public void setMenuPrice(String menuPrice) {
		this.menuPrice = menuPrice;
	}


	@Override
	public String toString() {
		return "Menu{" +
				"menuNo=" + menuNo +
				", menuNm='" + menuNm + '\'' +
				", menuPrice='" + menuPrice + '\'' +
				'}';
	}
}
