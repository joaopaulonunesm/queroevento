angular.module("queroEventoApp").factory("companyAPI", function ($http, configs){

	var _putCompany = function (company){
		return $http.put(configs.baseUrl + configs.version + '/companies', company);
	}
	
    return {
    	putCompany: _putCompany
    };
	
});