angular.module("queroEventoApp").controller("moderatorEventCtrl", function ($scope, $http, eventAPI) {

	$scope.pendingEvents = [];
	
	$scope.refusedEvents = [];
	
	$scope.publishedEvents = [];

	$scope.event = {};
	
	$scope.getPendingEvents = function() {
		
		eventAPI.getEventByCatalogStatusPending().then(function(response) {
			
			$scope.pendingEvents = response.data;
			
		}, function(response) {
			console.log(response.data);
			console.log(response.status);
		});

	};

	$scope.getPendingEvents();
	
	$scope.getPublishedEvents = function() {
		
		eventAPI.getEvents().then(function(response) {
			
			$scope.publishedEvents = response.data;
			
		}, function(response) {
			console.log(response.data);
			console.log(response.status);
		});

	};

	$scope.getPublishedEvents();
	
	$scope.getRefusedEvents  = function() {
		
		eventAPI.getEventByCatalogStatusRefused().then(function(response) {
			
			$scope.refusedEvents = response.data;
			
		}, function(response) {
			console.log(response.data);
			console.log(response.status);
		});

	};

	$scope.getRefusedEvents();

	$scope.putCatalogStatus = function(id, status) {
		
		$scope.event.catalogStatus = status;
		
		eventAPI.putEventCatalogStatus(id, $scope.event).then(function(response) {
			
			$scope.getRefusedEvents();
			$scope.getPublishedEvents();
			$scope.getPendingEvents();
			
		}, function(response) {
			console.log(response.data);
			console.log(response.status);
		});

	};
	
});