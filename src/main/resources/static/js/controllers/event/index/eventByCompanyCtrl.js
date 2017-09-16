angular.module("queroEventoApp").controller("eventByCompanyCtrl", function ($scope, $http, eventAPI, categoryAPI, $routeParams) {

	$scope.eventsByCompany = [];
	
	$scope.company = {};
	
	$scope.getEventByCompany = function(url) {
		
		eventAPI.getEventsByCompanyUrl(url).then(function(response) {
			
			$scope.eventsByCompany = response.data;
			
			$scope.company = response.data[0].company;
			
		}, function(response) {
			console.log(response.data);
			console.log(response.status);
		});

	};

	$scope.getEventByCompany($routeParams.nameUrl);

});