angular.module("queroEventoApp").factory("userAPI", function ($http, configs){

	var _putUser = function (user){
		return $http.put(configs.baseUrl + configs.version + '/users', user);
	}
	
    return {
    	putUser: _putUser
    };
	
});