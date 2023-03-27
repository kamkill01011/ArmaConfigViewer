startLoadingScreen ["Script running ..."];


_inidbi = ["new", "allCfg"] call OO_INIDBI;

KAM_Log_Property = {
	["write", ["all", _this # 0, _this # 1]] call _inidbi;
};
KAM_process_Class = {
	private _parent = "";
	private _parents = (configHierarchy (inheritsFrom _this));
	if (count _parents > 0) then {
		_parent = str (_parents # -1);
	};
	([_this, _parent]) call KAM_Log_Property;
	
	{
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
