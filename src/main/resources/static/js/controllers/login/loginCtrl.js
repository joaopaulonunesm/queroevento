angular.module("queroEventoApp").controller("loginCtrl", function ($scope, $location, loginAPI, companyAPI, configs) {

	$scope.login = {};
	
	$scope.signin = {};

	$scope.company = {};
	
	$scope.createLogin = function() {

		loginAPI.postNewLogin($scope.login).then(function(response) {
			
			$scope.authenticationNewLogin(response.data);

		}, function(response) {
			console.log(response.data);
			console.log(response.status);
		});
	};
	
	$scope.putCompany = function() {

		companyAPI.putCompany($scope.login.company).then(function(response) {

			alert("Perfil atualizado com sucesso!");
			
		}, function(response) {
			console.log(response.data);
			console.log(response.status);
		});
	};
	
	$scope.authenticationNewLogin = function(login) {

		loginAPI.postAuthenticate(login).then(function(response) {

			localStorage.setItem("token", response.data.token);

			$(location).attr('href', configs.siteUrl + '/admin');

		}, function(response) {
			console.log(response.data);
			console.log(response.status);
		});
	};

	$scope.authentication = function() {

		loginAPI.postAuthenticate($scope.signin).then(function(response) {

			localStorage.setItem("token", response.data.token);

			$(location).attr('href', configs.siteUrl + '/admin');

		}, function(response) {
			console.log(response.data);
			console.log(response.status);
		});
	};	
	
	$scope.openLogin = function(){

		if(localStorage.getItem("token")){
			
			$(location).attr('href', configs.siteUrl + '/admin');

		} else {
			$location.path("/login");
		}
	};
	
	$scope.validateLogin = function(){
		
		if(localStorage.getItem("token")){
		
			loginAPI.getLogin().then(function(response) {
	
				$scope.login = response.data;
	
			}, function(response) {
				
			});
		
		}
	};
	
	$scope.validateLogin();

	$scope.logout = function(){
		
		localStorage.removeItem("token");
		
		$(location).attr('href', configs.siteUrl + '/');
	};

});