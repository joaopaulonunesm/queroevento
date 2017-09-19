angular.module("queroEventoApp").controller("eventByCompanyCtrl", function ($scope, $http, eventAPI, categoryAPI, companyAPI, $routeParams) {

	$scope.eventsByCompany = [];
	
	$scope.company = {};
	
	$scope.getEventByCompany = function(url) {
		
		eventAPI.getEventsByCompanyUrl(url).then(function(response) {
			
			$scope.eventsByCompany = response.data;
			
		}, function(response) {
			console.log(response.data);
			console.log(response.status);
		});

	};

	$scope.getEventByCompany($routeParams.nameUrl);
	
	$scope.getCompanyByNameUrl = function(url) {
		
		companyAPI.getCompanyByNameUrl(url).then(function(response) {
			
			$scope.company = response.data;
			
		}, function(response) {
			console.log(response.data);
			console.log(response.status);
		});

	};
	
	$scope.getCompanyByNameUrl($routeParams.nameUrl);

});