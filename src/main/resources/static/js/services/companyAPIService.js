angular.module("queroEventoApp").factory("companyAPI", function ($http, configs){

	var _putCompany = function (company){
		return $http.put(configs.baseUrl + configs.version + '/companies', company);
	}
	
	var _getCompanyByUrlName = function (urlName){
		return $http.get(configs.baseUrl + '/companies/' + urlName);
	}
	
    return {
    	putCompany: _putCompany,
    	getCompanyByUrlName: _getCompanyByUrlName
    };
	
});