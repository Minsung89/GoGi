package com.example.front_village.gogi.Entity;



public class OrderCancel{


	private Long cancelId;

	private Long menuNo;
	
	private String orderResult;
	
	private int orderTotalPrice;

	public Long getCancelId() {
		return cancelId;
	}

	public void setCancelId(Long cancelId) {
		this.cancelId = cancelId;
	}

	public Long getMenuNo() {
		return menuNo;
	}

	public void setMenuNo(Long menuNo) {
		this.menuNo = menuNo;
	}


	public String getOrderResult() {
		return orderResult;
	}

	public void setOrderResult(String orderResult) {
		this.orderResult = orderResult;
	}

	public int getOrderTotalPrice() {
		return orderTotalPrice;
	}

	public void setOrderTotalPrice(int orderTotalPrice) {
		this.orderTotalPrice = orderTotalPrice;
	}

	@Override
	public String toString() {
		return "OrderCancel [cancelId=" + cancelId + ", menuNo=" + menuNo + ", orderDate=" + ", orderResult=" + orderResult + ", orderTotalPrice=" + orderTotalPrice + "]";
	}

	
}
