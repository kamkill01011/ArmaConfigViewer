startLoadingScreen ["Script running ..."];

KAM_Log_Class = {
	diag_log ["KAM_ACV_Class", _this];
};
KAM_Log_Property = {
	diag_log ["KAM_ACV_Property", _this];
};
KAM_process_Class = {
	([_this, inheritsFrom _this]) call KAM_Log_Class;
	
	{
		_value = "ERROR";
		if(isNumber _x) then {([_x, (getNumber _x)]) call KAM_Log_Property};
		if(isText _x) then {([_x, (getText _x)]) call KAM_Log_Property};
		if(isArray _x) then {([_x, (getArray _x)]) call KAM_Log_Property};
		if(isClass _x) then {_x call KAM_process_Class};
	}forEach (configProperties [_this, "true", false]);
};

(configFile >> "CfgVehicles") call KAM_process_Class;
(configFile >> "CfgWeapons") call KAM_process_Class;
(configFile >> "CfgMagazines") call KAM_process_Class;
(configFile >> "CfgAmmo") call KAM_process_Class;

endLoadingScreen;
