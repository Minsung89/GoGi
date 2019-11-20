package com.example.front_village.gogi.Entity;


public class OrderDetail {

	private Integer menuNo;  //메뉴 번호

	private String orderId;  //주문번호 - 영수증 번호

	private String orderQuantity; // 메뉴 수량


	public Integer getMenuNo() {
		return menuNo;
	}

	public void setMenuNo(Integer menuNo) {
		this.menuNo = menuNo;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrderQuantity() {
		return orderQuantity;
	}

	public void setOrderQuantity(String orderQuantity) {
		this.orderQuantity = orderQuantity;
	}
}
