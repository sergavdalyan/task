package com.lightspeed.parser;

class Join {
	private String type; // INNER, LEFT, RIGHT, FULL
	private Source source;
	private String onClause;

	public Join(String type, Source source, String onClause) {
		this.type = type;
		this.source = source;
		this.onClause = onClause;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Source getSource() {
		return source;
	}

	public void setSource(Source source) {
		this.source = source;
	}

	public String getOnClause() {
		return onClause;
	}

	public void setOnClause(String onClause) {
		this.onClause = onClause;
	}

	@Override public String toString() {
		return "Join{" +
				"type='" + type + '\'' +
				", source=" + source +
				", onClause='" + onClause + '\'' +
				'}';
	}
}
