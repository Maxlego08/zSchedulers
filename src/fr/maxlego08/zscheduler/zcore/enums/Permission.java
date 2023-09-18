package fr.maxlego08.zscheduler.zcore.enums;

public enum Permission {
	
	SCHEDULERS_USE,
	SCHEDULERS_RELOAD,

	;

	private final String permission;

	private Permission() {
		this.permission = this.name().toLowerCase().replace("_", ".");
	}

	public String getPermission() {
		return permission;
	}

}
