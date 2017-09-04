angular.module("queroEventoApp").factory("loginAPI", function ($http, configs){

	var _postNewLogin = function (login){
		return $http.post(configs.baseUrl + '/logins', login);
	};
	
	var _putLoginPassword = function (login){
		return $http.put(configs.baseUrl + configs.version + '/logins/password', login);
	};
	
	var _putLoginActive = function (login){
		return $http.put(configs.baseUrl + configs.version + '/logins/active', login);
	};
	
	var _deleteLogin = function (){
		return $http.delete(configs.baseUrl + configs.version + '/logins');
	};
	
	var _postAuthenticate = function (login){
		return $http.post(configs.baseUrl + '/logins/authenticate', login);
	};
	
	var _getLogin = function (){
		return $http.get(configs.baseUrl + configs.version + '/logins');
	};
	
    return {
    	postNewLogin: _postNewLogin,
    	putLoginPassword: _putLoginPassword,
    	putLoginActive: _putLoginActive,
    	deleteLogin: _deleteLogin,
    	postAuthenticate: _postAuthenticate,
    	getLogin: _getLogin
    };
	
});