angular.module("queroEventoApp").controller("eventByCompanyCtrl", function ($scope, $http, eventAPI, categoryAPI, companyAPI, $routeParams, $location) {

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

	$scope.getEventByCompany($routeParams.urlName);
	
	$scope.getCompanyByUrlName = function(url) {
		
		companyAPI.getCompanyByUrlName(url).then(function(response) {
			
			$scope.company = response.data;
			
		}, function(response) {
			$location.path("/");
			console.log(response.data);
			console.log(response.status);
		});

	};
	
	$scope.getCompanyByUrlName($routeParams.urlName);

});