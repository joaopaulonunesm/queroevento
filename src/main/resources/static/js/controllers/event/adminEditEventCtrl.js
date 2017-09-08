angular.module("queroEventoApp").controller("adminEditEventCtrl", function ($scope, $http, eventAPI, $routeParams, $location) {

	$scope.event = {};

	$scope.getEvent = function(url) {
		
		eventAPI.getEventByUrlTitle(url).then(function(response) {
			
			$scope.event = response.data;
			
		}, function(response) {
			console.log(response.data);
			console.log(response.status);
		});

	};

	$scope.getEvent($routeParams.url);
	
	$scope.putEvent = function(url) {
		
		$scope.event.eventDate = stringToDate($scope.eventDate.date, $scope.eventDate.time);
		
		eventAPI.putEvent(url, $scope.event).then(function(response) {

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

});