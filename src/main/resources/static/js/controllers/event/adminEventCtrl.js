angular.module("queroEventoApp").controller("adminEventCtrl", function ($scope, $location, eventAPI, configs) {

	$scope.myEvents = [];
	
	$scope.getMyEvents = function() {

		eventAPI.getEventByUser().then(function(response) {

			$scope.myEvents = response.data;

		}, function(response) {
			console.log(response.data);
			console.log(response.status);
		});
	};
	
	$scope.getMyEvents();

});