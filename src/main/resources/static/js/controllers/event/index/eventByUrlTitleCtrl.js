angular.module("queroEventoApp").controller("eventByUrlTitleCtrl", function ($scope, $http, eventAPI, categoryAPI, $routeParams, $location) {

	$scope.eventByUrl = [];
	
	$scope.getEventByUrl = function(url) {
		
		eventAPI.getEventByUrlTitle(url).then(function(response) {
			
			$scope.eventByUrl = response.data;
			
		}, function(response) {
			$location.path("/");
			console.log(response.data);
			console.log(response.status);
		});

	};

	$scope.getEventByUrl($routeParams.urlTitle);

});