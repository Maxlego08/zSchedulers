package fr.maxlego08.zscheduler.zcore.enums;

public enum Permission {

	ZSCHEDULERS_USE,
	ZSCHEDULERS_RELOAD,
	ZSCHEDULERS_LIST,

	;

	private final String permission;

	private Permission() {
		this.permission = this.name().toLowerCase().replace("_", ".");
	}

	public String getPermission() {
		return permission;
	}

}
