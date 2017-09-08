angular.module("queroEventoApp").controller("eventByCategoryCtrl", function ($scope, $http, eventAPI, categoryAPI, $routeParams) {

	$scope.eventsByCategory = [];
	
	$scope.category = {};
	
	$scope.getEventsByCategory = function(url) {
		
		eventAPI.getEventsByCategory(url).then(function(response) {
			
			$scope.eventsByCategory = response.data;
			
		}, function(response) {
			console.log(response.data);
			console.log(response.status);
		});

	};

	$scope.getEventsByCategory($routeParams.urlName);
	
	$scope.getCategory = function(url) {
		
		categoryAPI.getCategoryByUrlName(url).then(function(response) {
			
			$scope.category = response.data;
			
		}, function(response) {
			console.log(response.data);
			console.log(response.status);
		});

	};

	$scope.getCategory($routeParams.urlName);

});