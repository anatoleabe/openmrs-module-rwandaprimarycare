var $j = jQuery.noConflict();


var addressMap = new Object();
addressMap.country = new Object();
addressMap.province = new Object();
addressMap.district = new Object();
addressMap.sector = new Object();
addressMap.cell = new Object();
addressMap.umudugudu = new Object();
addressMap.country.name = "";
addressMap.country.nameId = "";
addressMap.country.level = 1;
addressMap.province.name = "";
addressMap.province.nameId = "";
addressMap.province.level = 2;
addressMap.district.name = "";
addressMap.district.nameId = "";
addressMap.district.level = 3;
addressMap.sector.name = "";
addressMap.sector.nameId = "";
addressMap.sector.level = 4;
addressMap.cell.name = "";
addressMap.cell.nameId = "";
addressMap.cell.level = 5;
addressMap.umudugudu.name = "";
addressMap.umudugudu.nameId = "";
addressMap.umudugudu.level = 6;

//Sets the value in the addressMap
function setObjectValue(addressLevel){
	var name = '';
	if (addressLevel == "COUNTRY"){
		try{
			name = document.getElementsByName("COUNTRY")[1].value;
		} catch(Exception){
			name = document.getElementsByName("COUNTRY")[0].value
		}
		if (addressMap.country.name != name){
			validateHierarchy(addressLevel, name);	
		}
		addressMap.country.name = name;
	} else if (addressLevel == "PROVINCE"){
		try{
			name = document.getElementsByName("PROVINCE")[1].value;
		} catch(Exception){
			name = document.getElementsByName("PROVINCE")[0].value
		}
		if (addressMap.province.name != name){
			validateHierarchy(addressLevel, name);
		}	
		addressMap.province.name = name;
	} else if (addressLevel == "DISTRICT"){
		try{
			name = document.getElementsByName("DISTRICT")[1].value;
		} catch(Exception){
			name = document.getElementsByName("DISTRICT")[0].value
		}
		if (addressMap.district.name != name){
			validateHierarchy(addressLevel, name);
		}	
		addressMap.district.name = name;
	} else if (addressLevel == "SECTOR"){
		try{
			name = document.getElementsByName("SECTOR")[1].value;
		} catch(Exception){
			name = document.getElementsByName("SECTOR")[0].value
		}
		if (addressMap.sector.name != name){
			validateHierarchy(addressLevel, name);
		}	
		addressMap.sector.name = name;
	} else if (addressLevel == "CELL"){
		try{
			name = document.getElementsByName("CELL")[1].value;
		} catch(Exception){
			name = document.getElementsByName("CELL")[0].value
		}
		if (addressMap.cell.name != name){
			validateHierarchy(addressLevel, name);
		}	
		addressMap.cell.name = name;
	} else if (addressLevel == "UMUDUGUDU"){
		try{
			name = document.getElementsByName("UMUDUGUDU")[1].value;
		} catch(Exception){
			name = document.getElementsByName("UMUDUGUDU")[0].value
		}
		addressMap.umudugudu.name = name;
		//no need to validate hierarchy for umudugudu
	}
	
}


function validateHierarchy(addressLevel, name){
	
	var tmp = "";
	if (addressLevel == 'COUNTRY'){
		tmp = document.getElementsByName("PROVINCE");
		for (var i = 0; i < tmp.length ; i++){
			tmp[i].value="";
		}
		addressMap.province.name="";
		addressMap.province.nameId="";
		tmp = document.getElementsByName("DISTRICT");
		for (var i = 0; i < tmp.length ; i++){
			tmp[i].value="";
		}
		addressMap.district.name="";
		addressMap.district.nameId="";
		tmp = document.getElementsByName("SECTOR");
		for (var i = 0; i < tmp.length ; i++){
			tmp[i].value="";
		}
		addressMap.sector.name="";
		addressMap.sector.nameId="";
		tmp = document.getElementsByName("CELL");
		for (var i = 0; i < tmp.length ; i++){
			tmp[i].value="";
		}
		addressMap.cell.name="";
		addressMap.cell.nameId="";
		tmp = document.getElementsByName("UMUDUGUDU");
		for (var i = 0; i < tmp.length ; i++){
			tmp[i].value="";
		}
		addressMap.umudugudu.name="";
		addressMap.umudugudu.nameId="";
		
		//level, id, searchString
		setIDBasedOnPreviousId(addressLevel, name);
		
	} else if (addressLevel == 'PROVINCE'){
		tmp = document.getElementsByName("DISTRICT");
		for (var i = 0; i < tmp.length ; i++){
			tmp[i].value="";
		}
		addressMap.district.name="";
		addressMap.district.nameId="";
		tmp = document.getElementsByName("SECTOR");
		for (var i = 0; i < tmp.length ; i++){
			tmp[i].value="";
		}
		addressMap.sector.name="";
		addressMap.sector.nameId="";
		tmp = document.getElementsByName("CELL");
		for (var i = 0; i < tmp.length ; i++){
			tmp[i].value="";
		}
		addressMap.cell.name="";
		addressMap.cell.nameId="";
		tmp = document.getElementsByName("UMUDUGUDU");
		for (var i = 0; i < tmp.length ; i++){
			tmp[i].value="";
		}
		addressMap.umudugudu.name="";
		addressMap.umudugudu.nameId="";
		
		//level, id, searchString
		setIDBasedOnPreviousId(addressLevel, name);
	} else if (addressLevel == 'DISTRICT'){
		tmp = document.getElementsByName("SECTOR");
		for (var i = 0; i < tmp.length ; i++){
			tmp[i].value="";
		}
		addressMap.sector.name="";
		addressMap.sector.nameId="";
		tmp = document.getElementsByName("CELL");
		for (var i = 0; i < tmp.length ; i++){
			tmp[i].value="";
		}
		addressMap.cell.name="";
		addressMap.cell.nameId="";
		tmp = document.getElementsByName("UMUDUGUDU");
		for (var i = 0; i < tmp.length ; i++){
			tmp[i].value="";
		}
		addressMap.umudugudu.name="";
		addressMap.umudugudu.nameId="";
		setIDBasedOnPreviousId(addressLevel, name);
	} else if (addressLevel == 'SECTOR'){
		tmp = document.getElementsByName("CELL");
		for (var i = 0; i < tmp.length ; i++){
			tmp[i].value="";
		}
		addressMap.cell.name="";
		addressMap.cell.nameId="";
		tmp = document.getElementsByName("UMUDUGUDU");
		for (var i = 0; i < tmp.length ; i++){
			tmp[i].value="";
		}
		addressMap.umudugudu.name="";
		addressMap.umudugudu.nameId="";
		setIDBasedOnPreviousId(addressLevel, name);
	} else if (addressLevel == 'CELL'){
		tmp = document.getElementsByName("UMUDUGUDU");
		for (var i = 0; i < tmp.length ; i++){
			tmp[i].value="";
		}
		addressMap.umudugudu.name="";
		addressMap.umudugudu.nameId="";
		setIDBasedOnPreviousId(addressLevel, name);
	}
}

function getObjectName(addressLevel){
	if (addressLevel == "COUNTRY"){
		addressMap.country.name = name;
		addressMap.country.nameId = nameId;
	} else if (addressLevel == "PROVINCE"){
		addressMap.province.name = name;
		addressMap.province.nameId = nameId;
	} else if (addressLevel == "DISTRICT"){
		addressMap.district.name = name;
		addressMap.district.nameId = nameId;
	} else if (addressLevel == "SECTOR"){
		addressMap.sector.name = name;
		addressMap.sector.nameId = nameId;
	} else if (addressLevel == "CELL"){
		addressMap.cell.name = name;
		addressMap.cell.nameId = nameId;
	} else if (addressLevel == "UMUDUGUDU"){
		addressMap.umudugudu.name = name;
		addressMap.umudugudu.nameId = nameId;
	}
}

function getObjectNameByLevelNum(addressLevel){
	if (addressLevel == 1){
		addressMap.country.name = name;
		addressMap.country.nameId = nameId;
	} else if (addressLevel == 2){
		addressMap.province.name = name;
		addressMap.province.nameId = nameId;
	} else if (addressLevel == 3){
		addressMap.district.name = name;
		addressMap.district.nameId = nameId;
	} else if (addressLevel == 4){
		addressMap.sector.name = name;
		addressMap.sector.nameId = nameId;
	} else if (addressLevel == 5){
		addressMap.cell.name = name;
		addressMap.cell.nameId = nameId;
	} else if (addressLevel == 6){
		addressMap.umudugudu.name = name;
		addressMap.umudugudu.nameId = nameId;
	}
}

function getObjectId(addressLevel){
	if (addressLevel == "COUNTRY"){
		addressMap.country.name = name;
		addressMap.country.nameId = nameId;
	} else if (addressLevel == "PROVINCE"){
		addressMap.province.name = name;
		addressMap.province.nameId = nameId;
	} else if (addressLevel == "DISTRICT"){
		addressMap.district.name = name;
		addressMap.district.nameId = nameId;
	} else if (addressLevel == "SECTOR"){
		addressMap.sector.name = name;
		addressMap.sector.nameId = nameId;
	} else if (addressLevel == "CELL"){
		addressMap.cell.name = name;
		addressMap.cell.nameId = nameId;
	} else if (addressLevel == "UMUDUGUDU"){
		addressMap.umudugudu.name = name;
		addressMap.umudugudu.nameId = nameId;
	}
}


function getObjectIdByLevelNum(addressLevel){
	if (addressLevel == 1){
		addressMap.country.name = name;
		addressMap.country.nameId = nameId;
	} else if (addressLevel == 2){
		addressMap.province.name = name;
		addressMap.province.nameId = nameId;
	} else if (addressLevel == 3){
		addressMap.district.name = name;
		addressMap.district.nameId = nameId; 
	} else if (addressLevel == 4){
		addressMap.sector.name = name;
		addressMap.sector.nameId = nameId;
	} else if (addressLevel == 5){
		addressMap.cell.name = name;
		addressMap.cell.nameId = nameId;
	} else if (addressLevel == 6){
		addressMap.umudugudu.name = name;
		addressMap.umudugudu.nameId = nameId;
	}
}
	
function updateAddressHierarchyCache(){    
    		setObjectValue('COUNTRY');
			setObjectValue('PROVINCE');
			setObjectValue('DISTRICT');
			setObjectValue('SECTOR');
			setObjectValue('CELL');
			setObjectValue('UMUDUGUDU');
}

function setIDBasedOnPreviousId(addressLevel, searchString){
	//get previous level's Id, and then run query with previous and search string
	//result of query should be to set the appropriate level's values.
	ret = "";
	var newId = getAddressIdFromName(searchString);
	
	if (addressLevel == 'COUNTRY'){
		
		//alert('addressMap.country.name' + addressMap.country.name + ' ' + "searchString " + searchString);
		if (addressMap.country.name != searchString || addressMap.country.nameId == ""){
		    try {
		    	
		    	addressMap.country.nameId = newId;
			} catch (Exception) {
				addressMap.country.nameId = "";
			}
		}
		
	} else if (addressLevel == 'PROVINCE' && (addressMap.province.name != searchString || addressMap.province.nameId == "")){
	    try {
			addressMap.province.nameId = newId;
		} catch (Exception) {
			addressMap.province.nameId = "";
		}
	} else if (addressLevel == 'DISTRICT' && (addressMap.district.name != searchString || addressMap.district.nameId == "")){
	    try {
			addressMap.district.nameId = newId;
		} catch (Exception) {
			addressMap.district.nameId = "";
		}
	} else if (addressLevel == 'SECTOR' && (addressMap.sector.name != searchString || addressMap.sector.nameId == "")){
	    try {
			addressMap.sector.nameId = newId;
		} catch (Exception) {
			addressMap.sector.nameId = "";
		}
	} else if (addressLevel == 'CELL' && (addressMap.cell.name != searchString || addressMap.cell.nameId == "")){
	    try {
			addressMap.cell.nameId = newId;
		} catch (Exception) {
			addressMap.cell.nameId = "";
		}
	} else if (addressLevel == 'UMUDUGUDU' && addressMap.umudugudu.name != searchString){
	    try {
			umudugudu.cell.nameId = newId;
		} catch (Exception) {
			umudugudu.cell.nameId = "";
		}	
	}
	return ret;
}

		