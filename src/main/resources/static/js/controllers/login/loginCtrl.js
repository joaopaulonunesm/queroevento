angular.module("queroEventoApp").controller("loginCtrl", function ($scope, $location, loginAPI, configs) {

	$scope.login = {};

	$scope.user = {};
	
	$scope.admin = "/";
	
	$scope.createLogin = function() {

		loginAPI.postNewLogin($scope.login).then(function(response) {

			$scope.authentication();

		}, function(response) {
			console.log(response.data);
			console.log(response.status);
		});
	};

	$scope.authentication = function() {

		loginAPI.postAuthenticate($scope.login).then(function(response) {

			localStorage.setItem("token", response.data.token);

			$scope.admin = "/admin";
			
			$(location).attr('href', configs.baseUrl + '/admin');

		}, function(response) {
			console.log(response.data);
			console.log(response.status);
		});
	};	
	
	$scope.openLogin = function(){

		if(localStorage.getItem("token")){
			
			$(location).attr('href', configs.baseUrl + '/admin');

		} else {
			$location.path("/login");
		}
	};
	
	$scope.validateLogin = function(){
		
		if(localStorage.getItem("token")){
		
		loginAPI.getLogin().then(function(response) {

			$scope.login = response.data;

		}, function(response) {
			console.log(response.data);
		});
		
		}
	};
	
	$scope.validateLogin();

	$scope.logout = function(){
		localStorage.removeItem("token");
		$(location).attr('href', configs.baseUrl + '/');
	};

});