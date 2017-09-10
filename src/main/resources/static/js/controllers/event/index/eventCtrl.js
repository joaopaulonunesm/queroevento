angular.module("queroEventoApp").controller("eventCtrl", function ($scope, $http, eventAPI) {
	
	$scope.events = [];
	
	$scope.eventsGold = [];
	
	$scope.eventsSilver = [];
	
	$scope.eventsBronze = [];

	$scope.getPublishedEvents = function() {
		
		eventAPI.getEvents().then(function(response) {
			
			$scope.events = response.data;
			
		}, function(response) {
			console.log(response.data);
			console.log(response.status);
		});

	};

	$scope.getPublishedEvents();
	
	$scope.getEventsGold = function() {
		
		eventAPI.getEventsGold().then(function(response) {
			
			$scope.eventsGold = response.data;
			
		}, function(response) {
			console.log(response.data);
			console.log(response.status);
		});

	};

	$scope.getEventsGold();
	
	$scope.getEventsSilver = function() {
		
		eventAPI.getEventsSilver().then(function(response) {
			
			$scope.eventsSilver = response.data;
			
		}, function(response) {
			console.log(response.data);
			console.log(response.status);
		});

	};

	$scope.getEventsSilver();
	
	$scope.getEventsBronze = function() {
		
		eventAPI.getEventsBronze().then(function(response) {
			
			$scope.eventsBronze = response.data;
			
		}, function(response) {
			console.log(response.data);
			console.log(response.status);
		});

	};

	$scope.getEventsBronze();
});