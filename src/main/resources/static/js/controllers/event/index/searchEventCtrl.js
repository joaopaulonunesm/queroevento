angular.module("queroEventoApp").controller("searchEventCtrl", function ($scope, $http, eventAPI, $location) {
	
	$scope.search = {word: ""};
	
	$scope.eventsByWord = [];
	
	$scope.getEventsByWord = function() {
		
		if($scope.search.word != ""){
			
			eventAPI.getEventsByWord($scope.search.word).then(function(response) {
				
				$scope.eventsByWord = response.data;
				
				$location.path("/event/search");
				
			}, function(response) {
				console.log(response.data);
				console.log(response.status);
			});
		} else {
			$location.path("/");
		}
		
	};
	

});