angular.module("queroEventoApp").controller("loginModeratorCtrl", function ($scope, $location, loginAPI, companyAPI, configs) {
	
	$scope.validateLogin = function(){
		
		if(localStorage.getItem("token")){
		
			loginAPI.getLogin().then(function(response) {
	
				$scope.login = response.data;
				
				if($scope.login.company.moderator == false ){
					$(location).attr('href', configs.siteUrl + '/');
				}
				
	
			}, function(response) {
				
			});
		
		} else {
			$(location).attr('href', configs.siteUrl + '/');
		}
	};
	
	$scope.validateLogin();
	
	$scope.logout = function(){
		
		localStorage.removeItem("token");
		
		$(location).attr('href', configs.siteUrl + '/');
	};
	
});