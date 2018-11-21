# Arma Config Viewer

## How to generate the HTML files

1. Go to Eden editor

2. Place a unit with the content of script.sqf in its init field 

   ```SQF
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
   ```

3. Launch the scenario to log all the classes and properties in the RPT file, it will take some time (around 1 minute if no mods are used) for the process to finish and get in the mission

4. Once in the mission quit the game

5. Launch ArmaConfigViewer.jar (located in \out\artifacts\ArmaConfigViewer_jar\)

6. Choose the last RPT file as input file and a folder where you want all the HTML files to be created for the output folder

7. Click Generate and wait (it took me about 7 minutes and the output folder size was 650 Mo)

8. You can open a file and navigate through the Arma config