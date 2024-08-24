package com.lightspeed.parser;

class Sort {
	private String column;
	private String direction; // ASC or DESC

	public Sort(String column, String direction) {
		this.column = column;
		this.direction = direction;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	@Override
	public String toString() {
		return "Sort{" +
				"column='" + column + '\'' +
				", direction='" + direction + '\'' +
				'}';
	}
}
