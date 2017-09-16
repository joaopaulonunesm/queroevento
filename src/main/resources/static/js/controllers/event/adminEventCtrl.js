angular.module("queroEventoApp").controller("adminEventCtrl", function ($scope, $location, eventAPI) {

	$scope.myEvents = [];
	
	$scope.event = {};
	
	$scope.eventDate = {};
	
	$scope.getMyEvents = function() {

		eventAPI.getEventByCompany().then(function(response) {

			$scope.myEvents = response.data;

		}, function(response) {
			console.log(response.data);
			console.log(response.status);
		});
	};
	
	$scope.getMyEvents();
	
	$scope.createEvent = function() {
		
		$scope.event.eventDate = stringToDate($scope.eventDate.date, $scope.eventDate.time);

		eventAPI.postEvent($scope.event).then(function(response) {
			
			$scope.event = {};
			$scope.eventDate = {};

			$location.path("/event/list");
			
		}, function(response) {
			console.log(response.data);
			console.log(response.status);
		});
	};
	
	function stringToDate(date, time) {
		
		var timeZone = "-0300";
		
		var split = $scope.eventDate.date.split("/");
		var dateFormat = split[2] + "-" + split[1] + "-" + split[0];
		
		var dateFormated =  dateFormat + "T" + time + timeZone;
		
		return dateFormated; 
	}
	
	$scope.deleteEvent = function(id) {

		eventAPI.deleteEvent(id).then(function(response) {
			
			$scope.getMyEvents();
			
			$location.path("/event/list");
			
		}, function(response) {
			console.log(response.data);
			console.log(response.status);
		});
	};

});