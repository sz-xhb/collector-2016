package com.xhb.sockserv.command;

public class CommandResponse {

	public static final CommandResponse SUCCESS = new CommandResponse(0, "success");
	public static final CommandResponse ERR1001 = new CommandResponse(1001, "bad request");
	public static final CommandResponse ERR1002 = new CommandResponse(1002, "DTU not connected");
	public static final CommandResponse ERR1003 = new CommandResponse(1003, "another command is executing");
	public static final CommandResponse ERR1004 = new CommandResponse(1004, "DTU not found");

	private Integer result;

	private String message;

	public CommandResponse(Integer result, String message) {
		this.result = result;
		this.message = message;
	}

	public Integer getResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
