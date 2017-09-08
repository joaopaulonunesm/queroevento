angular.module("queroEventoApp").controller("eventByUrlTitleCtrl", function ($scope, $http, eventAPI, categoryAPI, $routeParams) {

	$scope.eventByUrl = [];
	
	$scope.getEventByUrl = function(url) {
		
		eventAPI.getEventByUrlTitle(url).then(function(response) {
			
			$scope.eventByUrl = response.data;
			
		}, function(response) {
			console.log(response.data);
			console.log(response.status);
		});

	};

	$scope.getEventByUrl($routeParams.urlTitle);

});