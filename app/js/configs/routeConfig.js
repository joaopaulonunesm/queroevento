angular.module("eventApp").config(function ($routeProvider) {
	
	$routeProvider.when("/", {
		templateUrl: "views/home.html",
	});
		
	$routeProvider.otherwise({redirectTo: "/"});
	
});