angular.module("queroEventoApp").controller("eventCtrl", function ($scope, $http, eventAPI) {
	
	$scope.events = [];
	
	$scope.getPublishedEvents = function() {
		
		eventAPI.getEvents().then(function(response) {
			
			$scope.events = response.data;
			
		}, function(response) {
			console.log(response.data);
			console.log(response.status);
		});

	};

	$scope.getPublishedEvents();
	
});