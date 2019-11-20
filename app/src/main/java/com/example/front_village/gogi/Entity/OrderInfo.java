package com.example.front_village.gogi.Entity;


import java.util.Date;

public class OrderInfo {

	private String orderId;    //영수증 번호

	private String orderTableNo;  //테이블 번호

	private Date orderDate;  //주문 날짜

	private String orderResult; //계산 YN

	private String totalPrice; //총 가격

	private String orderType; //주문 타입

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrderTableNo() {
		return orderTableNo;
	}

	public void setOrderTableNo(String orderTableNo) {
		this.orderTableNo = orderTableNo;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public String getOrderResult() {
		return orderResult;
	}

	public void setOrderResult(String orderResult) {
		this.orderResult = orderResult;
	}

	public String getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	@Override
	public String toString() {
		return "OrderInfo{" +
				"orderId='" + orderId + '\'' +
				", orderTableNo='" + orderTableNo + '\'' +
				", orderDate=" + orderDate +
				", orderResult='" + orderResult + '\'' +
				", totalPrice='" + totalPrice + '\'' +
				", orderType='" + orderType + '\'' +
				'}';
	}
}
