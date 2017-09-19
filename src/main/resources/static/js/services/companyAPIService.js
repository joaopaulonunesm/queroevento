angular.module("queroEventoApp").factory("companyAPI", function ($http, configs){

	var _putCompany = function (company){
		return $http.put(configs.baseUrl + configs.version + '/companies', company);
	}
	
	var _getCompanyByNameUrl = function (nameUrl){
		return $http.get(configs.baseUrl + '//companies/' + nameUrl);
	}
	
    return {
    	putCompany: _putCompany,
    	getCompanyByNameUrl: _getCompanyByNameUrl
    };
	
});