angular.module("queroEventoApp").controller("eventCtrl", function ($scope, $http, eventAPI, $location) {
	
	$scope.eventsGold = [];
	
	$scope.sliderImgs = [];
	
	$scope.eventsSilver = [];
	
	$scope.eventsBronze = [];
	
	$scope.search = {word: ""};
	
	$scope.eventsByWord = [];
	
	$scope.getEventsGold = function() {
		
		eventAPI.getEventsGold().then(function(response) {
			
			$scope.eventsGold = response.data;
			
			angular.forEach(response.data, function(element) {
				
				var img = {
							route: element.imageUrl,
							caption: element.title,
							slogan: element.shortDescription,
							align: 'center'
						  };
				
				$scope.sliderImgs.push(img);

			});
			
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